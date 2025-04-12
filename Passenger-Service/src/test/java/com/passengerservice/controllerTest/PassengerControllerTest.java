package com.passengerservice.controllerTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import java.util.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import com.passengerservice.controller.PassengerController;
import com.passengerservice.dto.ReservationDto;
import com.passengerservice.feign.AuthFeign;
import com.passengerservice.feign.PaymentFeign;
import com.passengerservice.feign.ReservationFeign;
import com.passengerservice.feign.TrainFeign;
import com.passengerservice.model.User;
import com.passengerservice.payment.ProductRequest;
 
class PassengerControllerTest {
 
    @InjectMocks
    private PassengerController passengerController;
 
    @Mock
    private TrainFeign trainFeign;
 
    @Mock
    private ReservationFeign reservationFeign;
 
    @Mock
    private PaymentFeign paymentFeign;
 
    @Mock
    private AuthFeign authFeign;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    // ==================== TEST CASES ====================
 
    // 1. fetchTrainDetails()
    @Test
    void testFetchTrainDetails_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("source", "StationA");
        request.put("destination", "StationB");
 
        List<Object> mockTrainList = Arrays.asList(
            Map.of("trainName", "Express A", "trainId", 101),
            Map.of("trainName", "Express B", "trainId", 102)
        );
 
        when(trainFeign.fetchTrainDetails(request)).thenReturn(mockTrainList);
 
        // Act
        List<Object> response = passengerController.fetchTrainDetails(request);
 
        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
    }
 
    @Test
    void testFetchTrainDetails_EmptyRequest() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
 
        when(trainFeign.fetchTrainDetails(request)).thenReturn(Collections.emptyList());
 
        // Act
        List<Object> response = passengerController.fetchTrainDetails(request);
 
        // Assert
        assertNotNull(response);
        assertEquals(0, response.size());
    }
 
    // 2. reserveTickets()
    @Test
    void testReserveTickets_Success() {
        // Arrange
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setPassengerId(1);
 
        User mockUser = new User(1, "John Doe", "john.doe@email.com");
        when(authFeign.getUser()).thenReturn(mockUser);
 
        when(reservationFeign.reserveTickets(any(ReservationDto.class)))
            .thenReturn("Reservation Successful");
 
        // Act
        ResponseEntity<String> response = passengerController.reserveTickets(reservationDto);
 
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation Successful", response.getBody());
    }
 
    @Test
    void testReserveTickets_Failure() {
        // Arrange
        ReservationDto reservationDto = new ReservationDto();
 
        User mockUser = new User(1, "John Doe", "john.doe@email.com");
        when(authFeign.getUser()).thenReturn(mockUser);
 
        when(reservationFeign.reserveTickets(any(ReservationDto.class)))
            .thenReturn("Reservation Failed");
 
        // Act
        ResponseEntity<String> response = passengerController.reserveTickets(reservationDto);
 
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation Failed", response.getBody());
    }
 
    // 3. reservedTickets()
    @Test
    void testReservedTickets_Success() {
        // Arrange
        User mockUser = new User(1, "John Doe", "john.doe@email.com");
        when(authFeign.getUser()).thenReturn(mockUser);
 
        List<Object[]> mockTickets = new ArrayList<>();
        mockTickets.add(new Object[] { "12345", "Express A", "2024-12-30" });
 
        when(reservationFeign.reservedTickets(mockUser.getPassengerEmail()))
            .thenReturn(new ResponseEntity<>(mockTickets, HttpStatus.OK));
 
        // Act
        ResponseEntity<List<Object[]>> response = passengerController.reservedTickets();
 
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
 
    // 4. cancelTicket()
    @Test
    void testCancelTicket_Success() {
        // Arrange
        String pnrNumber = "PNR123";
        when(reservationFeign.cancelTicket(pnrNumber))
            .thenReturn(new ResponseEntity<>("Ticket Cancelled", HttpStatus.OK));
 
        // Act
        ResponseEntity<String> response = passengerController.cancelTicket(pnrNumber);
 
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ticket Cancelled", response.getBody());
    }
 
 
    // 6. viewTicket()
    @Test
    void testViewTicket_Success() {
        // Arrange
        String pnr = "PNR123";
        Map<String, Object> mockTicket = Map.of("pnr", "PNR123", "status", "Confirmed");
 
        when(reservationFeign.viewTicket(pnr)).thenReturn(mockTicket);
 
        // Act
        ResponseEntity<Object> response = passengerController.viewTicket(pnr);
 
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PNR123", ((Map<?, ?>) response.getBody()).get("pnr"));
    }
}
