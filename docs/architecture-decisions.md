# Architecture Decision Records (ADR)

## ADR-1: Explicit logging over AOP

**Status:** Accepted

**Context:** Controller endpoints log user activity via `UserActivityLogger.logUserActivity()`. An AOP-based approach with a custom `@LogUserActivity` annotation was considered.

**Decision:** Keep explicit `logUserActivity` calls in each controller method.

**Rationale:** As of now project has ~60 log calls across all bounded contexts, but an AOP-based approach introduces a custom annotation, an aspect class, placeholder resolution, and proxy overhead on every annotated method. The current approach keeps logging co-located with controller logic, is easy to debug, and has zero runtime overhead beyond the log call itself.

**Upgrade path:** If endpoint count grows significantly, introduce a `@LogUserActivity("is viewing {id}")` annotation with an aspect that resolves `{placeholders}` from method parameter names.

---

## ADR-2: Profile pictures stored as bytes in database

**Status:** Accepted

**Context:** Sellers and Buyers need profile pictures. Options considered: static frontend files, database BLOBs, or external object storage (S3/MinIO/Azure Blob).

**Decision:** Store profile pictures as `LONGBLOB` in MySQL, served directly via REST endpoints (`GET /warehousing/sellers/{id}/profile-picture`).

**Rationale:** The system has a small, fixed number of sellers and buyers. Introducing object storage infrastructure (S3, MinIO) for a handful of images is over-engineering. Database storage keeps the architecture simple, with no additional services to deploy or manage.

**Upgrade path:** Introduce a `FileStoragePort` (out port) with an adapter for S3/MinIO/Azure Blob. The web adapter would resolve file keys to URLs when building responses. Swapping storage providers means writing a new adapter; nothing else changes.