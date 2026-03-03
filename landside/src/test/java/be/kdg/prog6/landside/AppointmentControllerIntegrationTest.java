package be.kdg.prog6.landside;

import be.kdg.prog6.common.config.testsupport.TestTimeConfig;
import be.kdg.prog6.common.security.testsupport.TestSecurityConfig;
import be.kdg.prog6.common.security.testsupport.WithMockJwt;
import be.kdg.prog6.landside.adapter.out.db.entity.WarehouseJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.repository.DailyScheduleJpaRepository;
import be.kdg.prog6.landside.adapter.out.db.repository.WarehouseJpaRepository;
import be.kdg.prog6.landside.domain.AppointmentStatus;
import be.kdg.prog6.landside.domain.RawMaterial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import({TestSecurityConfig.class, TestTimeConfig.class})
public class AppointmentControllerIntegrationTest extends AbstractDatabaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WarehouseJpaRepository warehouseJpaRepository;
    @Autowired
    private DailyScheduleJpaRepository dailyScheduleJpaRepository;

    @BeforeEach
    void setUp() {
        warehouseJpaRepository.deleteAll();
        dailyScheduleJpaRepository.deleteAll();
    }

    /*
     * 1. POST "/appointments" - Makes an appointment
     */
    // 1.a: Valid request by seller -> CREATED
    @Test
    @WithMockJwt(role = "SELLER", sub = "23f11460-22bb-4888-b52a-2c5f6c9d1ea5")
    void shouldMakeAppointment() throws Exception {
        // Arrange
        final LocalDateTime scheduledArrivalTime = TestIds.FUTURE_SCHEDULED_ARRIVAL_TIME;
        createAvailableWarehouseStoringGypsum();

        final var request = post("/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "truckLicensePlate": "%s",
                  "rawMaterial": "%s",
                  "scheduledArrivalTime": "%s"
                }
                """
                .formatted(
                    TestIds.TRUCK_LICENSE_PLATE.value(),
                    RawMaterial.GYPSUM.name(),
                    scheduledArrivalTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ));

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.supplierId").value(TestIds.SUPPLIER_ID.id().toString()))
            .andExpect(jsonPath("$.truckLicensePlate").value(TestIds.TRUCK_LICENSE_PLATE.value()))
            .andExpect(jsonPath("$.rawMaterial").value(RawMaterial.GYPSUM.name()))
            .andExpect(jsonPath("$.status").value(AppointmentStatus.SCHEDULED.name()))
            .andExpect(jsonPath("$.arrivalWindowStart").value(scheduledArrivalTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        assertEquals(1, dailyScheduleJpaRepository.count());
    }

    // 1.b: Invalid request -> Arrival time in the past -> BAD REQUEST
    @Test
    @WithMockJwt(role = "SELLER", sub = "23f11460-22bb-4888-b52a-2c5f6c9d1ea5")
    void shouldNotMakeAppointmentWhenScheduledArrivalTimeIsInPast() throws Exception {
        // Arrange
        final LocalDateTime scheduledArrivalTime = TestIds.PAST_SCHEDULED_ARRIVAL_TIME;
        createAvailableWarehouseStoringGypsum();

        final var request = post("/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "truckLicensePlate": "%s",
                  "rawMaterial": "%s",
                  "scheduledArrivalTime": "%s"
                }
                """.formatted(
                TestIds.TRUCK_LICENSE_PLATE.value(),
                RawMaterial.GYPSUM.name(),
                scheduledArrivalTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ));

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andExpect(status().isBadRequest());
    }

    private void createAvailableWarehouseStoringGypsum() {
        final WarehouseJpaEntity warehouseJpaEntity = new WarehouseJpaEntity();
        warehouseJpaEntity.setWarehouseId(TestIds.WAREHOUSE_ID.id());
        warehouseJpaEntity.setSupplierId(TestIds.SUPPLIER_ID.id());
        warehouseJpaEntity.setRawMaterial(RawMaterial.GYPSUM);
        warehouseJpaEntity.setAvailable(true);
        warehouseJpaRepository.save(warehouseJpaEntity);
    }
}
