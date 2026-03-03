#  📝 Notes and Assumptions Made ❗️

## General Assumptions:
- All the weight inputs are considered in `tons` to make things easy :)
- A user can only have one role within the `Mineral Flow System`.
- **UTC Timezone Safety Net**: The JVM default timezone is forced to UTC at application startup (`TimeZone.setDefault`). 
This ensures `LocalDateTime.now()` always returns UTC regardless of the host machine's timezone, as required by the project description.
Note: MySQL's `NOW()` uses the database server's timezone, not the JVM's – seed data uses `NOW()` so the MySQL container should also be configured for UTC to avoid mismatches.

## Messaging Topology – Dual Configuration Strategy

The system maintains **two levels** of messaging topology configuration:

1. **Per-BC topology classes** (e.g., `LandsideMessagingTopology`, `WarehousingMessagingTopology`) – annotated with `@BoundedContextMessagingConfig`
2. **`GlobalMessagingTopology`** in the `application/` module – **not** annotated

### How Spring selects which one to load

In `MineralFlowApplication`:

```java
@ComponentScan(excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = BoundedContextMessagingConfig.class)
})
```

When running the **full monolith** (`MineralFlowApplication`), Spring **excludes** all classes annotated with `@BoundedContextMessagingConfig` – that's all 4 per-BC topology classes. Instead, `GlobalMessagingTopology` is loaded (it does NOT have that annotation).

If you were to run a **single BC as a standalone microservice** (e.g., `LandsideApplication`), the `GlobalMessagingTopology` wouldn't be on the classpath (it lives in the `application/` module). The per-BC `LandsideMessagingTopology` would be loaded instead, giving that BC exactly the queues, exchanges, and bindings it needs.

| Scenario                                 | GlobalMessagingTopology | Per-BC Topology                                      |
|------------------------------------------|-------------------------|------------------------------------------------------|
| Full monolith (`MineralFlowApplication`) | Loaded                  | Excluded via `@BoundedContextMessagingConfig` filter |
| Standalone BC (hypothetical)             | Not on classpath        | Loaded                                               |

### Why both exist

- **Per-BC topologies** = the BC owns its messaging contract. Each BC knows which exchanges it publishes to, which it subscribes to, and its queues/bindings. This is the DDD-correct approach and enables future extraction to microservices.
- **GlobalMessagingTopology** = operational convenience for the monolith deployment. A single file to see all messaging infrastructure. It's the one actually used at runtime today.

The duplication between them is the tradeoff. If you change a routing key in `GlobalMessagingTopology`, you must also update the per-BC topology (and vice versa), or they'll drift apart. In practice, the per-BC topologies serve as **documentation of intent** and a **future extraction path**, while `GlobalMessagingTopology` is the **runtime source of truth**.

## Landside Context:
- No truck with the same license plate enters the facility while one is already inside.
- Not realistic: More than one truck can dock at the same dock (since dock numbers are generated randomly).

## Warehousing Context:
- **From the project description**:  
  We will first load the oldest material stock  
  (‘oldest’ is determined by the PDT, we take abstraction of internal management of  
  warehouses in segments), then the second oldest stock, then the third and so on until the  
  complete SO is fulfilled. We were later told PDT should not be used for it, I am using Deliveries
  along with ShipmentAllocations to determine remaining Delivery amounts.

- In the Warehouse, I am not storing the whole Seller object (Warehouse does not own the Seller), storing only the sellerId and loading seller when needed.
- In the Purchase Order, for easy filtering I store the sellerName as a field (If Seller name changes, name in the PO will get stale,<br> 
another option would be to only store the sellerId, but in that case a complex join query with the Seller table would be necessary in the PurchaseOrderJpaRepository)

###  Scheduled Tasks – ReportStorageForWarehousesTask
- **Task Purpose**:  
  Reports the **current stock snapshot** of all warehouses for storage fee calculation(in Invoicing Ctx) and historical tracking(via balance snapshots).

- **Time Handling Decision**:
  - **Variable Name**: `reportingDateTime`
  - **Type**: `LocalDateTime`
  - **Reasoning**:
    - Storage reporting is **time-sensitive**; even a few minutes can change results due to new deliveries or shipments.
    - Using `LocalDateTime` ensures that snapshots are precise.

- **Testing**:
  - **TestIds Variable**: `REPORTING_DATE_TIME`
  - **Type**: `LocalDateTime`
  - Matches the snapshot nature of storage reporting and avoids ambiguity.

## Waterside Context:

###  Scheduled Tasks – ProcessQueuedBunkeringOperationsTask
- **Task Purpose**:  
  Processes queued bunkering operations for docked ships in **FIFO order**, enforcing the daily bunkering limit (currently 6/day).

- **Time Handling Decision**:
  - **Variable Name**: `processingDate`
  - **Type**: `LocalDate`
  - **Reasoning**:
    - The domain rule is based on a **per-day limit**.
    - Exact time is irrelevant; only the calendar day matters for enforcing the bunkering limit.

- **Testing**:
  - **TestIds Variable**: `PROCESSING_DATE`
  - **Type**: `LocalDate`
  - Keeps test fixtures simple and matches domain language (daily limit).

---

## Invoicing Context: