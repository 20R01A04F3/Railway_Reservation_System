package com.trainservice.controllerTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import java.time.LocalDateTime;
import java.util.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
 
import com.trainservice.controller.TrainController;
import com.trainservice.dto.ReservationDto;
import com.trainservice.dto.TrainDto;
import com.trainservice.feign.ReservationFeign;
import com.trainservice.model.Train;
import com.trainservice.model.TrainClass;
import com.trainservice.repository.TrainClassRepository;
import com.trainservice.repository.TrainRepository;
import com.trainservice.service.TrainService;
 
class TrainControllerTest {
 
    @InjectMocks
    private TrainController trainController;
 
    @Mock
    private TrainService trainService;
 
    @Mock
    private ReservationFeign reservationFeign;
 
    @Mock
    private TrainRepository trainRepository;
 
    @Mock
    private TrainClassRepository trainClassRepository;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    // Test 1: Add Train Details
    @Test
    void testAddTrainDetails() {
        Train train = new Train(1, "Express", "Source", "Destination", LocalDateTime.now(), LocalDateTime.now());
        TrainClass trainClass = new TrainClass(1, "AC", 500.0, 100, train);
 
        TrainDto trainDto = new TrainDto();
        trainDto.setTrain(train);
        trainDto.setTrainClass(Collections.singletonList(trainClass));
 
        doNothing().when(trainService).addTrainDetails(train, Collections.singletonList(trainClass));
 
        trainController.addTrainDetails(trainDto);
 
        verify(trainService, times(1)).addTrainDetails(train, Collections.singletonList(trainClass));
    }
 
    // Test 2: Fetch Train Details
    @Test
    void testFetchTrainDetails() {
        LocalDateTime date = LocalDateTime.now();
        Map<String, Object> request = Map.of(
                "source", "CityA",
                "destination", "CityB",
                "date", date.toString()
        );
 
        List<Object> mockResponse = List.of("Train1", "Train2");
 
        when(trainService.fetchTrainDetails("CityA", "CityB", date)).thenReturn(mockResponse);
 
        List<Object> result = trainController.fetchTrainDetails(request);
 
        assertEquals(2, result.size());
        assertEquals("Train1", result.get(0));
    }
 
    // Test 3: Fetch All Trains
    @Test
    void testFetchAllTrains() {
        Train train1 = new Train(1, "Express", "Source", "Destination", LocalDateTime.now(), LocalDateTime.now());
        Train train2 = new Train(2, "Superfast", "Source2", "Destination2", LocalDateTime.now(), LocalDateTime.now());
 
        when(trainService.fetchAll()).thenReturn(Arrays.asList(train1, train2));
 
        List<Train> result = trainController.fetchAllTrains();
 
        assertEquals(2, result.size());
        assertEquals("Express", result.get(0).getTrainName());
    }
 
    // Test 4: Delete Train - Success
    @Test
    void testDeleteTrain_Success() {
        int trainId = 1;
 
        when(trainRepository.existsById(trainId)).thenReturn(true);
        doNothing().when(trainClassRepository).deleteByTrainId(trainId);
        doNothing().when(trainRepository).deleteById(trainId);
 
        ResponseEntity<?> response = (ResponseEntity<?>) trainController.deleteTrain(trainId);
 
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Train with ID 1 deleted successfully.", response.getBody());
    }
 
    // Test 5: Delete Train - Not Found
    @Test
    void testDeleteTrain_NotFound() {
        int trainId = 1;
 
        when(trainRepository.existsById(trainId)).thenReturn(false);
 
        ResponseEntity<?> response = (ResponseEntity<?>) trainController.deleteTrain(trainId);
 
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Train with ID 1 not found.", response.getBody());
    }
 
    // Test 6: Booking Details
    @SuppressWarnings("deprecation")
	@Test
    void testBookingDetails() {
        List<ReservationDto> reservations = Arrays.asList(
                new ReservationDto(1, "PNR123", 3, "Express", "AC"),
                new ReservationDto(2, "PNR124", 2, "Superfast", "Sleeper")
        );
 
        when(reservationFeign.bookingDetails()).thenReturn(ResponseEntity.ok(reservations));
 
        ResponseEntity<List<ReservationDto>> response = trainController.bookingDetails();
 
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("PNR123", response.getBody().get(0).getPnr());
    }
 
    // Test 7: Fetch Train Name - Success
    @Test
    void testTrainName_Success() {
        Train train = new Train(1, "Express", "Source", "Destination", LocalDateTime.now(), LocalDateTime.now());
 
        when(trainRepository.findById(1)).thenReturn(Optional.of(train));
 

        String result = trainController.trainName(1);
 
        assertEquals("Express", result);
    }
 
}

