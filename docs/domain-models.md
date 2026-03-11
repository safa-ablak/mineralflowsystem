# Domain Models

This section documents the key domain models designed across each bounded context. It highlights the
aggregates, entities, value objects, and enums that encapsulate core business logic and enforce critical
invariants through rich, behavior-driven models. Each model is placed strategically within its own context
to uphold the principles of DDD (Domain-Driven Design).

# Table of Contents

- [Landside](#-landside)
- [Warehousing](#-warehousing)
- [Waterside](#-waterside)
- [Invoicing](#-invoicing)
- [Cross-Context Notes](#cross-context-notes)

## 🚚 **Landside**
**Package**: [`be.kdg.prog6.landside.domain`](../landside/src/main/java/be/kdg/prog6/landside/domain)

---

### DailySchedule (Aggregate Root)

Manages truck appointment scheduling for a single day; owns a list of `TimeSlot`s.

**Fields:**
- `date: LocalDate`
- `timeSlots: List<TimeSlot>`

**Behaviors:**
- `makeAppointment(SupplierId, TruckLicensePlate, RawMaterial, LocalDateTime, WarehouseId, LocalDateTime now)`
- `fulfillAppointment(AppointmentId)`
- `cancelAppointment(AppointmentId)`
- `findAppointmentById(AppointmentId)`
- `findAppointmentByTruckAndTime(TruckLicensePlate, LocalDateTime)`
- `findAllAppointments()`

---

### TimeSlot

Represents a one-hour time window for scheduling truck appointments (max 40 active appointments).

**Fields:**
- `TimeSlotId`
- `startTime: LocalDateTime`
- `endTime: LocalDateTime`
- `appointments: List<Appointment>`

**Behaviors:**
- `addAppointment(Appointment)`
- `findActiveAppointments()`
- `isAvailable()`
- `isFullyBooked()`
- `isBookableAt(LocalDateTime now)`
- `calculateAvailableCapacity()`
- `countActiveAppointments()`
- `contains(LocalDateTime)`

---

### Appointment

Represents a scheduled delivery of raw materials by a supplier's truck (60-minute arrival window).

**Fields:**
- `AppointmentId`
- `SupplierId`
- `WarehouseId`
- `TruckLicensePlate`
- `RawMaterial`
- `arrivalWindowStart: LocalDateTime`
- `status: AppointmentStatus`

**Behaviors:**
- `fulfill()`
- `cancel()`
- `getArrivalWindowEnd()`
- `ensureWithinArrivalWindow(arrivalTime)`
- `isScheduled()`
- `isFulfilled()`
- `isCancelled()`
- `isActive()`

---

### Visit (Aggregate Root)

Tracks the lifecycle of a truck visit from entry to departure.

**Fields:**
- `VisitId`
- `AppointmentId`
- `WarehouseId`
- `TruckLicensePlate`
- `RawMaterial`
- `arrivalTime: LocalDateTime`
- `dockTime: LocalDateTime`
- `departureTime: LocalDateTime`
- `weighBridgeTransaction: WeighBridgeTransaction`
- `status: VisitStatus`

**Behaviors:**
- `recordWeighIn(BigDecimal grossWeight)`
- `recordWeighOut(BigDecimal tareWeight)`
- `dockTruck()`
- `complete() : TruckDepartedEvent`
- `isTruckOnSite()`
- `isEnteredFacility()`
- `isWeighedIn()`
- `isDocked()`
- `isWeighedOut()`
- `isCompleted()`

---

### WeighBridge

Represents a physical weighbridge station; can be occupied by one visit at a time.

**Fields:**
- `WeighBridgeId`
- `WeighBridgeNumber`
- `occupiedByVisitId: VisitId`

**Behaviors:**
- `occupy(VisitId)`
- `release()`
- `isOccupied()`
- `isAvailable()`

---

### WeighBridgeTransaction

Records the gross weight (weigh-in) and tare weight (weigh-out) of a truck visit.

**Fields:**
- `WeighBridgeTransactionId`
- `TruckLicensePlate`
- `grossWeight: BigDecimal`
- `tareWeight: BigDecimal`
- `weighInTime: LocalDateTime`
- `weighOutTime: LocalDateTime`

**Behaviors:**
- `recordWeighOut(BigDecimal tareWeight, LocalDateTime weighOutTime)`
- `calculateNetWeight()`
- `isWeighOutRecorded()`

---

### Warehouse

A lightweight local projection of a warehouse; synced from the Warehousing BC.

**Fields:**
- `WarehouseId`
- `SupplierId`
- `RawMaterial`
- `isAvailable: boolean`

**Behaviors:**
- `updateAvailability(BigDecimal percentageFilled)` – marks unavailable if >= 80%
- `setRawMaterial(RawMaterial)`
- `ensureAvailableForNewAppointment()`

---

### Domain Events

- **`TruckDepartedEvent(TruckLicensePlate, AppointmentId)`** – emitted by `Visit.complete()`

---

### Value Objects

- `AppointmentId(UUID)`
- `VisitId(UUID)`
- `WarehouseId(UUID)`
- `SupplierId(UUID)` – context map: "Sellers" in Warehousing = "Suppliers" in Landside
- `TruckLicensePlate(String)` – validated: `^[A-Z0-9\-]{2,12}$`, normalized to uppercase
- `TimeSlotId(UUID)`
- `WeighBridgeId(UUID)`
- `WeighBridgeTransactionId(UUID)`
- `WeighBridgeNumber(String)` – format `WB-XX`
- `Dock(String)` – format `D-XX`

### Enums

- `RawMaterial` – enum (`GYPSUM`, `IRON_ORE`, `CEMENT`, `PETCOKE`, `SLAG`), factory: `fromString(String)`
- `AppointmentStatus` – enum (`SCHEDULED`, `CANCELLED`, `FULFILLED`)
- `VisitStatus` – enum (`ENTERED_FACILITY`, `WEIGHED_IN`, `DOCKED`, `WEIGHED_OUT`, `COMPLETED`)

### Domain Exceptions

| Class                                | Extends                     | Description                                        |
|--------------------------------------|-----------------------------|----------------------------------------------------|
| `LandsideDomainException`            | `InvalidOperationException` | Base for all Landside domain exceptions            |
| `EarlyArrivalException`              | `LandsideDomainException`   | Truck arrived before arrival window                |
| `LateArrivalException`               | `LandsideDomainException`   | Truck arrived after arrival window                 |
| `InvalidRawMaterialException`        | `LandsideDomainException`   | Invalid raw material string                        |
| `NoWeighBridgeAvailableException`    | `LandsideDomainException`   | No free weighbridge available                      |
| `WeighBridgeOccupiedException`       | `LandsideDomainException`   | Weighbridge already occupied                       |
| `WarehouseNotAvailableException`     | `LandsideDomainException`   | Warehouse cannot accept appointments (>= 80% full) |
| `DailyScheduleNotAvailableException` | `LandsideDomainException`   | No schedule found for date                         |
| `TruckNotRecognizedException`        | `LandsideDomainException`   | License plate not in system                        |
| `AppointmentNotFoundException`       | `RuntimeException`          | Appointment not found by ID                        |
| `VisitNotFoundException`             | `NotFoundException`         | Active visit not found                             |
| `WarehouseNotFoundException`         | `NotFoundException`         | Warehouse not found for supplier and raw material  |

---

## 📦 **Warehousing**
**Package**: [`be.kdg.prog6.warehousing.domain`](../warehousing/src/main/java/be/kdg/prog6/warehousing/domain)

---

### Warehouse (Aggregate Root)

Represents a physical warehouse operated by a seller where raw materials are stored and shipped. Regular capacity is 500,000 tons with a maximum overflow of 110%.

**Fields:**
- `WarehouseId`
- `WarehouseNumber`
- `SellerId`
- `RawMaterial`
- `Balance` (base – latest snapshot)
- `StockLedger`
- `SiteLocation`

**Behaviors:**
- `recordDelivery(BigDecimal)` – validates capacity (max 110% of 500kt)
- `recordShipment(BigDecimal)` – validates sufficient stock, FIFO allocation
- `assignRawMaterial(RawMaterial)`
- `snapshotBalance(LocalDateTime now)`
- `calculateStockLevel()`
- `balance()`
- `isEmpty()`
- `isRawMaterialAssigned()`
- `getStoredDeliveryRemainders(LocalDateTime)`
- `getDeliveries()`
- `getShipments()`
- `getShipmentAllocations()`

---

### StockLedger

Append-only ledger tracking deliveries and shipments to/from a warehouse. Supports FIFO allocation.

**Fields:**
- `WarehouseId`
- `List<Delivery>`
- `List<Shipment>`
- `List<ShipmentAllocation>`

**Behaviors:**
- `recordDelivery(BigDecimal)`
- `recordShipment(BigDecimal)` – FIFO allocation across deliveries
- `calculateBalance(Balance baseBalance)`
- `getStoredDeliveryRemainders(LocalDateTime)`
- `getShippableDeliveries()`
- `getShippableDeliveriesSortedByOldest()`

**Factory:**
- `emptyFor(WarehouseId)`

---

### PurchaseOrder (Aggregate Root)

Represents a purchase agreement between a buyer and a seller for raw materials. Maximum total weight is 150,000 tons.

**Fields:**
- `PurchaseOrderId`
- `PurchaseOrderNumber`
- `BuyerId`
- `buyerName: String`
- `SellerId`
- `sellerName: String`
- `orderDate: LocalDateTime`
- `vesselNumber: String`
- `List<OrderLine>`
- `status: PurchaseOrderStatus`

**Behaviors:**
- `calculateTotalAmount()`
- `fulfill()`
- `isPending()`
- `isFulfilled()`
- `fillVesselNumber(String)`

**Invariants:**
- Order lines must be non-empty
- No duplicate raw materials across lines
- Total weight must not exceed 150,000 tons

---

### OrderLine

Represents a single line item within a Purchase Order.

**Fields:**
- `OrderLineId`
- `lineNumber: int`
- `RawMaterial`
- `amount: BigDecimal`

---

### Buyer

Represents a buyer organization placing orders for raw materials.

**Fields:**
- `BuyerId`
- `name: String`
- `Address`

---

### Seller

Represents a seller organization responsible for fulfilling purchase orders sent by the buyers.

**Fields:**
- `SellerId`
- `name: String`
- `Address`

---

### Delivery

Represents a raw material delivery into a warehouse (immutable ledger entry).

**Fields:**
- `DeliveryId`
- `time: LocalDateTime`
- `amount: BigDecimal`

---

### Shipment

Represents a raw material shipment out of a warehouse (immutable ledger entry).

**Fields:**
- `ShipmentId`
- `time: LocalDateTime`
- `amount: BigDecimal`

---

### ShipmentAllocation

Represents how much of a delivery was used to fulfill a shipment (for FIFO tracking).

**Fields:**
- `WarehouseId`
- `ShipmentId`
- `DeliveryId`
- `amountAllocated: BigDecimal`

---

### ShipmentRecord

Groups a Shipment together with the Delivery allocations it was drawn from.

**Fields:**
- `Shipment`
- `List<ShipmentAllocation>`

---

### PayloadDeliveryTicket

Represents a physical delivery ticket issued to trucks for raw material drop-offs (after docking at a warehouse).

**Fields:**
- `PayloadDeliveryTicketId`
- `rawMaterial: String`
- `generationTime: LocalDateTime`
- `WarehouseId`
- `warehouseNumber: String`
- `dockNumber: String`

---

### StoredDeliveryRemainder

Represents the remaining unshipped quantity from a delivery and the number of full days it has been stored.

**Fields:**
- `DeliveryId`
- `remainingAmount: BigDecimal`
- `daysStored: int`

---

### Balance

Represents the net amount of raw materials at a point in time.

**Fields:**
- `time: LocalDateTime`
- `amount: BigDecimal`

**Constants:**
- `ORIGIN` – zero balance at `LocalDateTime.MIN`

**Behaviors:**
- `isZero()`

---

### StockLevel

Snapshot of warehouse stock level including computed fill percentage.

**Fields:**
- `Balance balance`
- `BigDecimal percentageFilled`

**Factory:**
- `from(Balance, BigDecimal capacity)`

---

### Domain Services

- **`ShipmentService`** – Ships raw materials for a purchase order's order lines from eligible warehouses.
  - `shipForPurchaseOrderFrom(PurchaseOrder, List<Warehouse>) : Map<Warehouse, ShipmentRecord>`

---

### Value Objects

- `WarehouseId(UUID)`
- `WarehouseNumber(String)` – validated format: `WH-XX`
- `SellerId(UUID)`
- `BuyerId(UUID)`
- `DeliveryId(WarehouseId, UUID)` – composite key scoping delivery to warehouse
- `ShipmentId(WarehouseId, UUID)` – composite key scoping shipment to warehouse
- `PayloadDeliveryTicketId(UUID)`
- `PurchaseOrderId(UUID)`
- `PurchaseOrderNumber(String)` – validated format: `PO######`
- `OrderLineId(UUID)`
- `Address(String streetName, String streetNumber, String city, String country)`
- `SiteLocation(double easting, double northing)` – physical position on site
- `SiteBounds(double minEasting, double maxEasting, double minNorthing, double maxNorthing)`
- `WarehouseFootprint(double width, double height)` – physical dimensions

### Enums

- `RawMaterial` – enum (`GYPSUM`, `IRON_ORE`, `CEMENT`, `PETCOKE`, `SLAG`) with display names, factory: `fromString(String)`
- `PurchaseOrderStatus` – enum (`PENDING`, `FULFILLED`)

### Domain Exceptions

| Class                                       | Extends                      | Description                                         |
|---------------------------------------------|------------------------------|-----------------------------------------------------|
| `WarehousingDomainException`                | `InvalidOperationException`  | Base for all Warehousing domain exceptions          |
| `InsufficientStockException`                | `WarehousingDomainException` | Not enough stock to ship                            |
| `DeliveryCapacityExceededException`         | `WarehousingDomainException` | Delivery would exceed 110% capacity                 |
| `RawMaterialConflictException`              | `WarehousingDomainException` | Warehouse has non-empty stock of different material |
| `InvalidRawMaterialException`               | `WarehousingDomainException` | Invalid raw material name                           |
| `PurchaseOrderAlreadyFulfilledException`    | `WarehousingDomainException` | PO already fulfilled                                |
| `DuplicateRawMaterialException`             | `WarehousingDomainException` | Duplicate raw material in PO order lines            |
| `EmptyPurchaseOrderException`               | `WarehousingDomainException` | PO created with no order lines                      |
| `PurchaseOrderWeightLimitExceededException` | `WarehousingDomainException` | PO total exceeds 150kt limit                        |
| `PurchaseOrderNotFoundException`            | `NotFoundException`          | PO not found                                        |
| `WarehouseNotFoundException`                | `NotFoundException`          | Warehouse not found                                 |
| `SellerNotFoundException`                   | `NotFoundException`          | Seller not found                                    |
| `BuyerNotFoundException`                    | `NotFoundException`          | Buyer not found                                     |

---

## 🚢 **Waterside**
**Package**: [`be.kdg.prog6.waterside.domain`](../waterside/src/main/java/be/kdg/prog6/waterside/domain)

---

### ShippingOrder (Aggregate Root)

Represents the lifecycle of a ship arriving to load purchased raw materials.

**Fields:**
- `ShippingOrderId`
- `ReferenceId` (links to Purchase Order)
- `BuyerId`
- `VesselNumber`
- `scheduledArrivalDate: LocalDateTime`
- `scheduledDepartureDate: LocalDateTime`
- `actualArrivalDate: LocalDateTime`
- `actualDepartureDate: LocalDateTime`
- `InspectionOperation`
- `BunkeringOperation`
- `status: ShippingOrderStatus`

**Behaviors:**
- `match()`
- `dockShip()`
- `performInspection(inspectorSignature)`
- `performBunkering()`
- `departShip()`
- `amendDetails(BuyerId, ReferenceId)`
- `ensureCanInitiateLoading()`
- `isReadyForLoading()`
- State checks:
    - `isPendingValidation()`
    - `isMatched()`
    - `isShipDocked()`
    - `isShipInspected()`
    - `isShipBunkered()`
    - `isShipDeparted()`

---

### InspectionOperation

Represents the safety and regulatory inspection of a ship.

**Fields:**
- `InspectionOperationId`
- `performedOn: LocalDate`
- `inspectorSignature: String`
- `status: InspectionOperationStatus`

**Behaviors:**
- `complete(inspectorSignature)`
- `isScheduled()`
- `isCompleted()`

---

### BunkeringOperation

Represents the fueling (bunkering) operation for a ship.

**Fields:**
- `BunkeringOperationId`
- `queuedAt: LocalDateTime`
- `performedAt: LocalDateTime`
- `status: BunkeringOperationStatus`

**Behaviors:**
- `complete()`
- `isQueued()`
- `isCompleted()`

---

### Domain Services

- **`BunkeringOperationService`** – Enforces the daily bunkering limit (max 6 per day).
  - `validateBunkeringLimit(int dailyOperationCount)` – throws `BunkeringLimitExceededException`
  - `canPerformBunkering(int dailyOperationCount) : boolean`

---

### Value Objects

- `ShippingOrderId(UUID)`
- `ReferenceId(UUID)` – Purchase Order reference
- `BuyerId(UUID)`
- `VesselNumber(String)` – validated format: `^[A-Z0-9\-]{3,15}$`
- `InspectionOperationId(UUID)`
- `BunkeringOperationId(UUID)`

### Enums

- `ShippingOrderStatus`:
    - `PENDING_VALIDATION`
    - `MATCHED`
    - `SHIP_DOCKED`
    - `SHIP_INSPECTED`
    - `SHIP_BUNKERED`
    - `SHIP_DEPARTED`

- `InspectionOperationStatus`:
    - `SCHEDULED`
    - `COMPLETED`

- `BunkeringOperationStatus`:
    - `QUEUED`
    - `COMPLETED`

### Domain Exceptions

| Class                                 | Extends                     | Description                                    |
|---------------------------------------|-----------------------------|------------------------------------------------|
| `WatersideDomainException`            | `InvalidOperationException` | Base for all Waterside domain exceptions       |
| `BunkeringAlreadyCompletedException`  | `WatersideDomainException`  | Bunkering operation already completed          |
| `BunkeringLimitExceededException`     | `WatersideDomainException`  | Daily limit of 6 bunkering operations exceeded |
| `InspectionAlreadyCompletedException` | `WatersideDomainException`  | Inspection already completed                   |
| `ShippingOrderNotFoundException`      | `NotFoundException`         | Shipping order not found                       |

---

## 🧾 **Invoicing**
**Package**: [`be.kdg.prog6.invoicing.domain`](../invoicing/src/main/java/be/kdg/prog6/invoicing/domain)

---

### Invoice (Aggregate Root)

Represents an invoice issued to a customer (seller) based on commission and storage fees.

**Fields:**
- `InvoiceId`
- `CustomerId` – maps to a Seller in the shared model
- `draftedDate: LocalDate`
- `sentDate: LocalDate`
- `status: InvoiceStatus`
- `invoiceLines: List<InvoiceLine>`

**Behaviors:**
- `addInvoiceLine(InvoiceLine)`
- `addCommissionFee(Money baseAmount, BigDecimal rate)`
- `addStorageCostFee(RawMaterial, List<StorageEntry>)`
- `send(LocalDate sentDate)`
- `calculateTotalAmount()`
- `isReadyToSend()`

---

### InvoiceLine

Represents a line item on an invoice.

**Fields:**
- `InvoiceLineId`
- `amount: Money`
- `type: InvoiceLineType`

**Factory methods:**
- `createCommissionFee(Money baseAmount, BigDecimal rate)`
- `createStorageCostFee(RawMaterial, BigDecimal amount)`

---

### RawMaterial

Represents a persistable raw material with pricing data used in invoice calculations.

**Fields:**
- `RawMaterialId`
- `name: String`
- `storagePricePerTonPerDay: Money`
- `unitPricePerTon: Money`

**Behaviors:**
- `modifyStoragePricePerTonPerDay(Money)`
- `modifyUnitPricePerTon(Money)`

---

### Money

Represents a monetary amount in a specific currency (default: USD). Amounts are rounded to the currency's default fraction digits using HALF_UP.

**Fields:**
- `amount: BigDecimal`
- `currency: Currency`

**Factory methods:**
- `Money.of(BigDecimal)`
- `Money.of(double)`
- `Money.of(BigDecimal, Currency)`
- `Money.ofNullable(BigDecimal)`

**Behaviors:**
- `add(Money)`
- `subtract(Money)`
- `multiply(BigDecimal)`
- `divide(BigDecimal)`

---

### YearlyCommissionRate

Represents a commission rate (e.g. 1%) applied to total raw material costs for a given year.

**Fields:**
- `year: int`
- `rate: BigDecimal` – between `0.0` and `1.0`

**Behaviors:**
- `asPercentage(): BigDecimal`

---

### Domain Services

- **`RawMaterialCostCalculator`** – Calculates total raw material cost for use in commission fee computation.
  - `calculateTotalRawMaterialCosts(Map<RawMaterial, BigDecimal> rawMaterialToAmount) : Money`

---

### Value Objects

- `InvoiceId(UUID)`
- `CustomerId(UUID)` – context map: "Sellers" in Warehousing = "Customers" in Invoicing
- `InvoiceLineId(UUID)`
- `RawMaterialId(UUID)`
- `Money(BigDecimal, Currency)`
- `YearlyCommissionRate(int, BigDecimal)`

### Enums

- `InvoiceStatus`:
    - `DRAFT`
    - `SENT`

- `InvoiceLineType`:
    - `COMMISSION`
    - `STORAGE_COST`

### Domain Exceptions

| Class                                   | Extends                     | Description                              |
|-----------------------------------------|-----------------------------|------------------------------------------|
| `InvoicingDomainException`              | `InvalidOperationException` | Base for all Invoicing domain exceptions |
| `EmptyInvoiceException`                 | `InvoicingDomainException`  | Cannot send invoice with no lines        |
| `RawMaterialNotFoundException`          | `NotFoundException`         | Raw material not found                   |
| `YearlyCommissionRateNotFoundException` | `NotFoundException`         | Commission rate not found for year       |

---

## Cross-Context Notes

### Context Mapping

The same real-world concept has different names across bounded contexts:

- **Sellers** (Warehousing) = **Suppliers** (Landside) = **Customers** (Invoicing)
- Each context maintains its own ID type (`SellerId` / `SupplierId` / `CustomerId`) wrapping the same underlying UUID

### RawMaterial across contexts

- **Landside**: simple `enum` with 5 values, `fromString()` factory
- **Warehousing**: `enum` with display names, richer `fromString()` supporting both display name and enum name matching
- **Invoicing**: full `class` entity with pricing data (`storagePricePerTonPerDay`, `unitPricePerTon`) persisted to DB
