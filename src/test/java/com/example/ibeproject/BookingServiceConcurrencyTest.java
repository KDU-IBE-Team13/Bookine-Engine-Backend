package com.example.ibeproject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.ibeproject.dto.booking.BookingRequestDTO;
import com.example.ibeproject.service.BookingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class BookingServiceConcurrencyTest {

    @Autowired
    private BookingService bookingService;

    @Test
    public void testConcurrentRequests() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        List<Long> successfulThreadIds = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                System.out.println("Thread " + Thread.currentThread().getId() + " attempting booking");

                BookingRequestDTO request = createBookingRequest();
                boolean success = bookingService.makeBooking(request);

                if (success) {
                    synchronized (successfulThreadIds) {
                        successfulThreadIds.add(Thread.currentThread().getId());
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Successful threads:");
        for (Long threadId : successfulThreadIds) {
            System.out.println("Thread " + threadId + " successfully booked a room");
        }
    }

    private BookingRequestDTO createBookingRequest() {
        LocalDate checkInDate = LocalDate.now().plusDays((int) (Math.random() * 10)); 
        LocalDate checkOutDate = checkInDate.plusDays((int) (Math.random() * 10)); 
        int roomTypeId = 73; 
        int numRooms = 1;

        return new BookingRequestDTO(checkInDate, checkOutDate, roomTypeId, numRooms);
    }
}
