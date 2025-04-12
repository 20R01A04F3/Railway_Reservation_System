package com.ticketreservation.controllerTest;



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
 
import com.ticketreservation.controller.ReservationController;

import com.ticketreservation.model.Reservation;

import com.ticketreservation.model.ResponseData;

import com.ticketreservation.service.ReservationService;
 
class ReservationControllerTest {
 
    @InjectMocks

    private ReservationController reservationController;
 
    @Mock

    private ReservationService reservationService;
 
    @BeforeEach

    void setUp() {

        MockitoAnnotations.openMocks(this);

    }
 
    // ==================== TEST CASES ====================
 
    // 1. viewTicket()

    @Test

    void testViewTicket_Success() {

        // Arrange

        String pnr = "PNR123";

        Reservation mockReservation = new Reservation(1, "AC", "Address1", "Bank1", "1234", 2, 500.0, pnr, 101, 201, 301);

        when(reservationService.viewTicket(pnr)).thenReturn(mockReservation);
 
        // Act

        Reservation response = reservationController.viewTikcet(pnr);
 
        // Assert

        assertNotNull(response);

        assertEquals(pnr, response.getPnrNumber());

    }
 
    // 2. reserveTickets()

    @Test

    void testReserveTickets_Success() {

        // Arrange

        Reservation reservation = new Reservation(1, "AC", "Address1", "Bank1", "1234", 2, 500.0, "PNR123", 101, 201, 301);

        when(reservationService.reserveTickets(reservation)).thenReturn("Ticket Reserved Successfully");
 
        // Act

        String response = reservationController.reserveTickets(reservation);
 
        // Assert

        assertEquals("Ticket Reserved Successfully", response);

    }
 
    @Test

    void testReserveTickets_Failure() {

        // Arrange

        Reservation reservation = new Reservation();

        when(reservationService.reserveTickets(reservation)).thenReturn("Reservation Failed");
 
        // Act

        String response = reservationController.reserveTickets(reservation);
 
        // Assert

        assertEquals("Reservation Failed", response);

    }
 
    // 3. reservedTickets()

    @Test

    void testReservedTickets_Success() {

        // Arrange

        String passengerEmail = "john.doe@email.com";

        List<Object[]> mockTickets = new ArrayList<>();

        mockTickets.add(new Object[] { "PNR123", "Express", "AC" });

        when(reservationService.reservedTickets(passengerEmail)).thenReturn(mockTickets);
 
        // Act

        ResponseEntity<List<Object[]>> response = reservationController.reservedTickets(passengerEmail);
 
        // Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(1, response.getBody().size());

    }
 
    @Test

    void testReservedTickets_Empty() {

        // Arrange

        String passengerEmail = "test@email.com";

        when(reservationService.reservedTickets(passengerEmail)).thenReturn(Collections.emptyList());
 
        // Act

        ResponseEntity<List<Object[]>> response = reservationController.reservedTickets(passengerEmail);
 
        // Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(0, response.getBody().size());

    }
 
    // 4. cancelTicket()

    @Test

    void testCancelTicket_Success() {

        // Arrange

        String pnrNumber = "PNR123";

        when(reservationService.cancelTicket(pnrNumber)).thenReturn("Ticket Cancelled");
 
        // Act

        ResponseEntity<String> response = reservationController.cancelTicket(pnrNumber);
 
        // Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Ticket Cancelled", response.getBody());

    }
 
    @Test

    void testCancelTicket_Failure() {

        // Arrange

        String pnrNumber = "PNR123";

        when(reservationService.cancelTicket(pnrNumber)).thenReturn("Cancellation Failed");
 
        // Act

        ResponseEntity<String> response = reservationController.cancelTicket(pnrNumber);
 
        // Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Cancellation Failed", response.getBody());

    }
 
    // 5. bookingDetails()

    @Test

    void testBookingDetails_Success() {

        // Arrange

        List<ResponseData> mockDetails = Arrays.asList(

            new ResponseData(1, "PNR123", 2, "Express", "AC"),

            new ResponseData(2, "PNR124", 3, "Superfast", "Sleeper")

        );

        when(reservationService.bookingDetails()).thenReturn(mockDetails);
 
        // Act

        ResponseEntity<List<ResponseData>> response = reservationController.bookingDetails();
 
        // Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(2, response.getBody().size());

    }
 
    @Test

    void testBookingDetails_Empty() {

        // Arrange

        when(reservationService.bookingDetails()).thenReturn(Collections.emptyList());
 
        // Act

        ResponseEntity<List<ResponseData>> response = reservationController.bookingDetails();
 
        // Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(0, response.getBody().size());

    }
 
    // 6. extractPassenger()

    @Test

    void testExtractPassenger_Success() {

        // Arrange

        Integer id = 1;
 
        // Act

        reservationController.extractPassenger(id);
 
        // Assert

        verify(reservationService, times(1)).extractPassenger(id);

    }

}
 