# 🪄🔍 Command, Query & Event Catalogs 🔔

# 🗂️ Bounded Contexts
1. 🚚 [Landside Context](#landside-context)
2. 📦 [Warehousing Context](#warehousing-context)
3. 🚢 [Waterside Context](#waterside-context)
4. 🧾 [Invoicing Context](#invoicing-context)

## **Landside Context**
> 📌 In this context, the term **Supplier** refers to the **Seller**, based on **Context Mapping**.
Sellers deliver raw materials to KdG for storage.<br> Since they are not selling anything within the Landside Context
(that happens in the Warehousing context), the Landside Context refers to them as Suppliers.

### **Commands**
**Package:** [`be.kdg.prog6.landside.port.in.command`](../landside/src/main/java/be/kdg/prog6/landside/port/in/command)

| **Command Name**                                      | **Description**                                                                                               |
|-------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| **`MakeAppointmentCommand`**                          | Creates a delivery appointment for a truck to deliver raw material within the specified arrival window.       |
| **`FulfillAppointmentCommand`**                       | Fulfills the appointment of a truck after it completes its visit.                                             |
| **`RecognizeTruckAtEntranceGateCommand`**             | Recognizes a truck by its license plate at the entrance gate and assigns it a weigh bridge number.            |
| **`RecordTruckWeighInCommand`**                       | Records the gross weight (with payload inside) of a truck at the assigned weigh bridge.                       |
| **`DockTruckCommand`**                                | Docks the truck at a random dock connected to the warehouse it is delivering the raw materials to.            |
| **`RecordTruckWeighOutCommand`**                      | Records the tare weight of a truck after unloading at assigned weigh bridge for the weigh bridge ticket(wbt). |
| **`ConsolidateTruckAtExitGateCommand`**               | Recognizes a truck by its license plate at the exit gate and marks the end of truck's visit.                  |
| **`ProjectAvailabilityCommand`**                      | Projects the availability of a warehouse for making appointments.                                             |
| **`ProjectRawMaterialCommand`**                       | Projects the raw material assigned to a warehouse (synced from Warehousing BC).                               |
| **`CancelMismatchedAppointmentsForWarehouseCommand`** | Cancels scheduled appointments whose raw material no longer matches the warehouse's assigned raw material.    |

### **Queries**
**Package:** [`be.kdg.prog6.landside.port.in.query`](../landside/src/main/java/be/kdg/prog6/landside/port/in/query)

| **Query Name**               | **Description**                                                |
|------------------------------|----------------------------------------------------------------|
| **`GetTruckOverviewsQuery`** | Retrieves all truck overviews within the specified date range. |

### **Integration Events**
**Package:** [`be.kdg.prog6.common.event.landside`](../common/src/main/java/be/kdg/prog6/common/event/landside)

| **Event Name**             | **Listener(s)**                       | **Description**                                                                                                             |
|----------------------------|---------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| **`TruckDockedEvent`**     | TruckDockedListener (warehousing)     | Published when a truck docks at one of the conveyor belts connected to the warehouse it is delivering the raw materials to. |
| **`TruckWeighedOutEvent`** | TruckWeighedOutListener (warehousing) | Published when a truck completes the **weighing process** (weigh-out) on its way out.                                       |

### **Domain Events**
**Package:** [`be.kdg.prog6.landside.domain.event`](../landside/src/main/java/be/kdg/prog6/landside/domain/event)

| **Event Name**             | **Listener**                     | **Description**                                                                                                           |
|----------------------------|----------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| **`TruckDepartedEvent`**   | TruckDepartedListener (landside) | Published when a truck's license plate is scanned at the exit gate for final consolidation, marking the end of the visit. |

---

## **Warehousing Context**
### **Commands**
**Package:** [`be.kdg.prog6.warehousing.port.in.command`](../warehousing/src/main/java/be/kdg/prog6/warehousing/port/in/command)

| **Command Name**                             | **Description**                                                                                     |
|----------------------------------------------|-----------------------------------------------------------------------------------------------------|
| **`SendPurchaseOrderCommand`**               | Sends a purchase order for the specified buyer, seller, and raw materials.                          |
| **`FulfillPurchaseOrderCommand`**            | Fulfills a purchase order.                                                                          |
| **`ShipPurchaseOrderCommand`**               | Records shipments for a purchase order from relevant warehouses.                                    |
| **`ValidateReferencedPurchaseOrderCommand`** | Validates referenced PO for an entered/correction-requested SO.                                     |
| **`GeneratePayloadDeliveryTicketCommand`**   | Generates a payload delivery ticket (pdt) for the delivered raw materials at the warehouse.         |
| **`RecordDeliveryCommand`**                  | Records a delivery for the raw materials delivered to a warehouse.                                  |
| **`SnapshotSellerWarehousesBalanceCommand`** | Snapshots balances for all warehouses of a seller after daily storage reporting (9:00 AM schedule). |
| **`AssignRawMaterialToWarehouseCommand`**    | Assigns a raw material to a warehouse (triggered on first delivery of a new material).              |

### **Queries**
**Package:** [`be.kdg.prog6.warehousing.port.in.query`](../warehousing/src/main/java/be/kdg/prog6/warehousing/port/in/query)

| **Query Name**                              | **Description**                                                                                                                                                |
|---------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **`GetPurchaseOrdersQuery`**                | Retrieves purchase orders filtered by their fulfillment status and/or seller name.                                                                             |
| **`GetWarehouseBalanceQuery`**              | Retrieves the balance of a specific warehouse as of a given point in time by summing all deliveries and subtracting all shipments up to that timestamp.        |
| **`GetWarehouseActivityHistoryQuery`**      | Retrieves the activity history (deliveries and shipments) for a specific warehouse, with an optional view mode to include or exclude allocation details.       |
| **`CalculateWarehouseBalanceChangeQuery`**  | Calculates the net change in a warehouse's balance between two timestamps by summing all deliveries and subtracting all shipments within the specified period. |

### **Integration Events**
**Package:** [`be.kdg.prog6.common.event.warehousing`](../common/src/main/java/be/kdg/prog6/common/event/warehousing)

| **Event Name**                              | **Listener(s)**                                                                                                                | **Description**                                                                                          |
|---------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| **`PayloadDeliveryTicketGeneratedEvent`**   | PayloadDeliveryTicketGeneratedListener (landside)                                                                              | Published when a payload delivery ticket is generated for raw materials delivered to a warehouse.        |
| **`WarehouseDeliveryRecordedEvent`**        | WarehouseActivityListener (landside)                                                                                           | Published when a delivery is recorded at a warehouse.                                                    |
| **`WarehouseShipmentRecordedEvent`**        | WarehouseActivityListener (landside)                                                                                           | Published when a shipment is recorded at a warehouse.                                                    |
| **`WarehouseRawMaterialAssignedEvent`**     | WarehouseRawMaterialAssignedListener (landside)                                                                                | Published when a warehouse's assigned raw material changes (triggers appointment cancellation).          |
| **`PurchaseOrderShippedEvent`**             | PurchaseOrderShippedListener (waterside)                                                                                       | Published when shipment for a specific purchase order is completed.                                      |
| **`PurchaseOrderFulfilledEvent`**           | PurchaseOrderFulfilledListener (invoicing)                                                                                     | Published when a purchase order is fulfilled.                                                            |
| **`SellerWarehousesStorageReportedEvent`**  | WarehousingSellerWarehousesStorageReportedListener (warehousing), InvoicingSellerWarehousesStorageReportedListener (invoicing) | Published when the current storage details of a seller's warehouses are reported (at 9:00 AM every day). |
| **`ReferencedPurchaseOrderValidatedEvent`** | ReferencedPurchaseOrderValidationListener (waterside)                                                                          | Published when a referenced purchase order is found to be valid.                                         |
| **`ReferencedPurchaseOrderRejectedEvent`**  | ReferencedPurchaseOrderValidationListener (waterside)                                                                          | Published when a referenced purchase order does not exist or has already been fulfilled.                 |

---

## **Waterside Context**
### **Commands**
**Package:** [`be.kdg.prog6.waterside.port.in.command`](../waterside/src/main/java/be/kdg/prog6/waterside/port/in/command)

| **Command Name**                                 | **Description**                                                                                                                                |
|--------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------|
| **`EnterShippingOrderCommand`**                  | Enters a shipping order with the PO reference and specified buyer.                                                                             |
| **`RequestShippingOrderCorrectionCommand`**      | Requests a correction for a shipping order if it wasn't entered correctly (a SO with incorrect Reference ID or incorrect Buyer ID).            |
| **`MatchShippingOrderWithPurchaseOrderCommand`** | Matches the shipping order with the referenced purchase order.                                                                                 |
| **`DockShipCommand`**                            | Docks a ship at the port, so that necessary operations can be completed, and the ship can be loaded.                                           |
| **`InspectShipCommand`**                         | Performs the inspection operation for a ship.                                                                                                  |
| **`BunkerShipCommand`**                          | Performs the bunkering operation for a ship.                                                                                                   |
| **`InitiateShippingOrderLoadingCommand`**        | Initiates the loading of the shipping order onto the ship.                                                                                     |
| **`DepartShipCommand`**                          | Departs the ship from the port after the operations (IO, BO) are completed and the ship is loaded.                                             |

### **Integration Events**
**Package:** [`be.kdg.prog6.common.event.waterside`](../common/src/main/java/be/kdg/prog6/common/event/waterside)

| **Event Name**                              | **Listener(s)**                                        | **Description**                                                       |
|---------------------------------------------|--------------------------------------------------------|-----------------------------------------------------------------------|
| **`ShippingOrderEnteredEvent`**             | ShippingOrderEnteredListener (warehousing)             | Published when a new shipping order is entered.                       |
| **`ShippingOrderCorrectionRequestedEvent`** | ShippingOrderCorrectionRequestedListener (warehousing) | Published when a correction is requested for a shipping order.        |
| **`ShippingOrderLoadingInitiatedEvent`**    | ShippingOrderLoadingInitiatedListener (warehousing)    | Published when the loading process for a shipping order is initiated. |
| **`ShipDepartedEvent`**                     | ShipDepartedListener (warehousing)                     | Published when a ship departs from the port.                          |

---

## **Invoicing Context**
> 📌 In this context, the term **Customer** refers to the **Seller**, based on **Context Mapping**.
KdG invoices sellers for commission and storage fees.<br> Since sellers are the ones being billed,
the Invoicing Context refers to them as Customers.

### **Commands**
**Package:** [`be.kdg.prog6.invoicing.port.in.command`](../invoicing/src/main/java/be/kdg/prog6/invoicing/port/in/command)

| **Command Name**                      | **Description**                                                                        |
|---------------------------------------|----------------------------------------------------------------------------------------|
| **`CalculateCommissionFeeCommand`**   | Calculates a 1% commission fee for each purchase order (PO) that has been fulfilled.   |
| **`CalculateStorageFeeCommand`**      | Calculates storage fees each day per customer.                                         |
| **`InvoiceCustomerCommand`**          | Invoices a customer at request (invoicing outstanding credit).                         |
| **`UpdateRawMaterialPricingCommand`** | Updates pricing (storage price/ton/day, unit price/ton) of the specified raw material. |

### **Integration Events**
There are no events published by the Invoicing Context.
