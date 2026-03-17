package be.kdg.prog6.warehousing.adapter.in.web.controller;

import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.port.out.BuyerProfilePicturePort;
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
@RequestMapping("/warehousing/buyers")
public class BuyerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerController.class);

    private final BuyerProfilePicturePort buyerProfilePicturePort;

    public BuyerController(final BuyerProfilePicturePort buyerProfilePicturePort) {
        this.buyerProfilePicturePort = buyerProfilePicturePort;
    }

    @GetMapping("/{id}/profile-picture")
    @PreAuthorize("hasAnyRole('ROLE_BUYER', 'ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable final UUID id,
                                                    @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing the Profile Picture of Buyer with ID %s", id));
        return buyerProfilePicturePort.loadProfilePicture(BuyerId.of(id))
            .map(picture -> ResponseEntity.ok().contentType(MediaType.parseMediaType(picture.contentType()))
                .body(picture.content()))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/profile-picture")
    @PreAuthorize("hasAnyRole('ROLE_BUYER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> uploadProfilePicture(
        @PathVariable final UUID id,
        @RequestParam("file") final MultipartFile file,
        @AuthenticationPrincipal final Jwt jwt
    ) throws IOException {
        logUserActivity(LOGGER, jwt, format("is uploading a Profile Picture for Buyer with ID %s", id));
        buyerProfilePicturePort.saveProfilePicture(BuyerId.of(id), file.getBytes(), file.getContentType());
        return ResponseEntity.noContent().build();
    }
}
