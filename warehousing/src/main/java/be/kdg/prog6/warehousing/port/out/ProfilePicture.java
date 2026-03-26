package be.kdg.prog6.warehousing.port.out;

/**
 * Represents raw profile picture data returned by the persistence adapter.
 *
 * <p>This record lives in the port layer rather than the domain because it carries
 * raw byte content, which is a storage and transport concern. The domain has no
 * concept of profile picture data – it is loaded and served entirely through the
 * port and adapter layers.
 *
 * <p>Per ADR-2, if storage moves to an external service (S3/MinIO/Azure Blob),
 * a {@code FileStoragePort} adapter would handle persistence, and the web adapter
 * would resolve stored references to URLs when building responses.
 *
 * @param content     the image bytes
 * @param contentType the MIME type (e.g. {@code image/png})
 */
public record ProfilePicture(byte[] content, String contentType) {}
