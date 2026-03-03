package be.kdg.prog6.landside;

import be.kdg.prog6.common.security.testsupport.TestSecurityConfig;
import be.kdg.prog6.common.security.testsupport.WithMockJwt;
import be.kdg.prog6.landside.adapter.out.db.entity.VisitJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.repository.VisitJpaRepository;
import be.kdg.prog6.landside.domain.VisitStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class LandsideMonitoringControllerIntegrationTest extends AbstractDatabaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VisitJpaRepository visitJpaRepository;

    @BeforeEach
    void setUp() {
        visitJpaRepository.deleteAll();
    }

    /*
     * 1. GET "/landside/monitoring/trucks-on-site/count" - Returns the number of trucks currently on site
     */
    // 1.a: With a role allowed to access the endpoint
    @Test
    @WithMockJwt(role = "WAREHOUSE_MANAGER")
    void shouldReturnNumberOfTrucksOnSite_whenRequestedByWarehouseManagerAndActiveVisitsExist() throws Exception {
        // Arrange
        final UUID appointmentId = UUID.randomUUID(); // Truck On site = Active Visit, so no need to create the Appointment
        final UUID visitId = UUID.randomUUID();
        visitJpaRepository.save(
            createVisit(visitId,
                appointmentId,
                LocalDateTime.now(),
                null, // Still on site
                VisitStatus.ENTERED_FACILITY
            )
        );

        // Act
        final ResultActions result = mockMvc.perform(get("/landside/monitoring/trucks-on-site/count"));

        // Assert
        result.andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    @WithMockJwt(role = "WAREHOUSE_MANAGER")
    void shouldReturnZero_whenRequestedByWarehouseManagerAndNoTrucksOnSite() throws Exception {
        // Arrange
        final UUID appointmentId = UUID.randomUUID();
        final UUID visitId = UUID.randomUUID();
        visitJpaRepository.save(
            createVisit(
                visitId,
                appointmentId,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now(), // Departed
                VisitStatus.COMPLETED
            )
        );

        // Act
        final ResultActions result = mockMvc.perform(
            get("/landside/monitoring/trucks-on-site/count")
        );

        // Assert
        result.andExpect(status().isOk())
            .andExpect(content().string("0"));
    }

    // 1.b: With a role not allowed to access the endpoint
    @Test
    @WithMockJwt(role = "TRUCK_DRIVER")
    void shouldReturnForbidden_whenTrucksOnSiteRequestedByTruckDriver() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/landside/monitoring/trucks-on-site/count"))
            .andExpect(status().isForbidden());
    }

    // 1.c: No authentication token provided
    @Test
    void shouldReturnUnauthorized_whenTrucksOnSiteRequestedWithoutAuthentication() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/landside/monitoring/trucks-on-site/count"))
            .andExpect(status().isUnauthorized());
    }

    /*
     * 2. GET "/landside/monitoring/truck-overviews" - Returns all truck overviews within the given date range.
     */
    // 2.a: With a role allowed to access the endpoint, (from = tomorrow, to = the day after tomorrow)
    @Test
    @WithMockJwt(role = "WAREHOUSE_MANAGER")
    void shouldReturnZero_whenWarehouseManagerRequestsTruckOverviewsForDateRangeAndNoneExist() throws Exception {
        // Arrange
        final String from = LocalDate.now().plusDays(1).toString();
        final String to = LocalDate.now().plusDays(2).toString();

        // Act
        final ResultActions result = mockMvc.perform(
            get("/landside/monitoring/truck-overviews")
                .param("from", from) // tomorrow
                .param("to", to)   // the day after tomorrow
        );

        // Assert
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    // 2.b: With a role not allowed to access the endpoint
    @Test
    @WithMockJwt(role = "TRUCK_DRIVER")
    void shouldReturnForbidden_whenTruckDriverRequestsTruckOverviewsForDateRange() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/landside/monitoring/truck-overviews")
                .param("from", LocalDate.now().toString())
                .param("to", LocalDate.now().toString()))
            .andExpect(status().isForbidden());
    }

    // 2.c: No authentication token provided
    @Test
    void shouldReturnUnauthorized_whenTruckOverviewsRequestedWithoutAuthentication() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/landside/monitoring/truck-overviews")
                .param("from", LocalDate.now().toString())
                .param("to", LocalDate.now().toString()))
            .andExpect(status().isUnauthorized());
    }

    private static VisitJpaEntity createVisit(
        final UUID visitId,
        final UUID appointmentId,
        final LocalDateTime arrivalTime,
        final LocalDateTime departureTime,
        final VisitStatus status
    ) {
        final VisitJpaEntity visit = new VisitJpaEntity();
        visit.setVisitId(visitId);
        visit.setAppointmentId(appointmentId);
        visit.setWarehouseId(TestIds.WAREHOUSE_ID.id());
        visit.setTruckLicensePlate(TestIds.TRUCK_LICENSE_PLATE.value());
        visit.setRawMaterial(TestIds.RAW_MATERIAL);
        visit.setArrivalTime(arrivalTime);
        visit.setDepartureTime(departureTime);
        visit.setStatus(status);
        return visit;
    }
}