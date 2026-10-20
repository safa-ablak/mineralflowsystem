package be.kdg.prog6.warehousing.adapter.in.web.controller;

import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.port.in.usecase.GetSellerProfilePictureUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.RemoveSellerProfilePictureUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.UploadSellerProfilePictureUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static java.lang.String.format;

@RestController
@RequestMapping("/warehousing/sellers")
public class SellerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SellerController.class);

    private final GetSellerProfilePictureUseCase getSellerProfilePictureUseCase;
    private final UploadSellerProfilePictureUseCase uploadSellerProfilePictureUseCase;
    private final RemoveSellerProfilePictureUseCase removeSellerProfilePictureUseCase;

    public SellerController(final GetSellerProfilePictureUseCase getSellerProfilePictureUseCase,
                            final UploadSellerProfilePictureUseCase uploadSellerProfilePictureUseCase,
                            final RemoveSellerProfilePictureUseCase removeSellerProfilePictureUseCase) {
        this.getSellerProfilePictureUseCase = getSellerProfilePictureUseCase;
        this.uploadSellerProfilePictureUseCase = uploadSellerProfilePictureUseCase;
        this.removeSellerProfilePictureUseCase = removeSellerProfilePictureUseCase;
    }

    @GetMapping("/{id}/profile-picture")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable final UUID id,
                                                    @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing the Profile Picture of Seller with ID %s", id));
        return getSellerProfilePictureUseCase.getProfilePicture(SellerId.of(id), jwt)
            .map(picture -> ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(picture.contentType()))
                .body(picture.content()))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/profile-picture")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> uploadProfilePicture(
        @PathVariable final UUID id,
        @RequestParam("file") final MultipartFile file,
        @AuthenticationPrincipal final Jwt jwt
    ) throws IOException {
        logUserActivity(LOGGER, jwt, format("is uploading a Profile Picture for Seller with ID %s", id));
        uploadSellerProfilePictureUseCase.uploadProfilePicture(SellerId.of(id), file.getBytes(), file.getContentType(), jwt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/profile-picture")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> removeProfilePicture(@PathVariable final UUID id,
                                                     @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is removing the Profile Picture of Seller with ID %s", id));
        removeSellerProfilePictureUseCase.removeProfilePicture(SellerId.of(id), jwt);
        return ResponseEntity.noContent().build();
    }
}