package com.ticketreservation.daoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
 
import java.util.Arrays;
import java.util.List;
 
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 
import com.ticketreservation.dao.ReservationDao;
import com.ticketreservation.feign.TrainFeign;
import com.ticketreservation.model.Reservation;
import com.ticketreservation.model.ResponseData;
import com.ticketreservation.repository.ReservationRepository;
 
class ReservationDaoTest {
 
    @InjectMocks
    private ReservationDao reservationDao;
 
    @Mock
    private ReservationRepository reservationRepository;
 
    @Mock
    private TrainFeign trainFeign;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testReserveTickets_ExceedsLimit() {
        Reservation reservation = new Reservation();
        reservation.setReservedTickets(7);
 
        String result = reservationDao.reserveTickets(reservation);
        assertEquals("Maximum of only 6 tickets can be reserved in any class", result);
    }
 
 
    @Test
    void testCancelTicket_Success() {
        Reservation reservation = new Reservation();
        reservation.setReservedTickets(3);
 
        when(reservationRepository.classInfo("PNR123")).thenReturn(101);
        when(reservationRepository.findBypnrNumber("PNR123")).thenReturn(reservation);
        when(reservationRepository.cancelTicket("PNR123")).thenReturn(1);
 
        String result = reservationDao.cancelTicket("PNR123");
 
        verify(reservationRepository, times(1)).updateCancelledSeats(3, 101);
        assertEquals("Ticket Cancelled", result);
    }
 
    @Test
    void testCancelTicket_Failure() {
        when(reservationRepository.classInfo("PNR123")).thenThrow(new RuntimeException("PNR not found"));
 
        String result = reservationDao.cancelTicket("PNR123");
 
        assertEquals("PNR Number Does not exist", result);
    }
 
    @Test
    void testBookingDetails() {
        Reservation reservation = new Reservation(1, "AC", "Address", "Bank", "1234", 3, 1500, "PNR123", 101, 1, 201);
 
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));
        when(trainFeign.trainName(101)).thenReturn("Express");
 
        List<ResponseData> result = reservationDao.bookingDetails();
 
        assertEquals(1, result.size());
        assertEquals("PNR123", result.get(0).getPnr());
        assertEquals("Express", result.get(0).getTrainName());
    }
 
    @Test
    void testViewTicket() {
        Reservation reservation = new Reservation();
        reservation.setPnrNumber("PNR123");
 
        when(reservationRepository.findBypnrNumber("PNR123")).thenReturn(reservation);
 
        Reservation result = reservationDao.viewTicket("PNR123");
 
        assertEquals("PNR123", result.getPnrNumber());
    }
}


