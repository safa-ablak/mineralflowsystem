package be.kdg.prog6.invoicing.adapter.in.web.controller;

import be.kdg.prog6.invoicing.adapter.in.web.dto.RawMaterialDto;
import be.kdg.prog6.invoicing.adapter.in.web.dto.request.UpdateRawMaterialPricingDto;
import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.domain.RawMaterialId;
import be.kdg.prog6.invoicing.port.in.command.UpdateRawMaterialPricingCommand;
import be.kdg.prog6.invoicing.port.in.usecase.UpdateRawMaterialPricingUseCase;
import be.kdg.prog6.invoicing.port.in.usecase.query.GetRawMaterialsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static java.lang.String.format;

@RestController
@RequestMapping("/raw-materials")
public class RawMaterialController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RawMaterialController.class);

    private final GetRawMaterialsUseCase getRawMaterialsUseCase;
    private final UpdateRawMaterialPricingUseCase updateRawMaterialPricingUseCase;

    public RawMaterialController(final GetRawMaterialsUseCase getRawMaterialsUseCase,
                                 final UpdateRawMaterialPricingUseCase updateRawMaterialPricingUseCase) {
        this.getRawMaterialsUseCase = getRawMaterialsUseCase;
        this.updateRawMaterialPricingUseCase = updateRawMaterialPricingUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As an <b>accountant</b>, I want to view all raw materials
     * so that I can see their current pricing details.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ACCOUNTANT', 'ROLE_ADMIN')")
    public ResponseEntity<List<RawMaterialDto>> getRawMaterials(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing all Raw Materials");
        final List<RawMaterial> rawMaterials = getRawMaterialsUseCase.getRawMaterials();

        final List<RawMaterialDto> rawMaterialDtos = rawMaterials
            .stream()
            .map(RawMaterialDto::fromDomain)
            .toList();
        return ResponseEntity.ok(rawMaterialDtos);
    }

    /**
     * 📘 - User Story<br></br>
     * As an <b>accountant</b>, I want to update pricing for a raw material
     * so that I can keep pricing information up to date.
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ACCOUNTANT', 'ROLE_ADMIN')")
    public ResponseEntity<RawMaterialDto> updateRawMaterialPricing(
        @PathVariable("id") final UUID id,
        @RequestBody final UpdateRawMaterialPricingDto request,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        logUserActivity(LOGGER, jwt, format("is updating pricing for Raw Material with ID %s",
            id
        ));
        final UpdateRawMaterialPricingCommand command = new UpdateRawMaterialPricingCommand(
            RawMaterialId.of(id),
            Money.ofNullable(request.storagePricePerTonPerDay()),
            Money.ofNullable(request.unitPricePerTon())
        );
        final RawMaterial rawMaterial = updateRawMaterialPricingUseCase.updateRawMaterialPricing(
            command
        );
        return ResponseEntity.ok(RawMaterialDto.fromDomain(rawMaterial));
    }
}