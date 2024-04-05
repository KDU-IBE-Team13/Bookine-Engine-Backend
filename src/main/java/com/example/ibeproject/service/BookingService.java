package com.example.ibeproject.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ibeproject.constants.BookingConstants;
import com.example.ibeproject.dto.booking.BookingRequestDTO;
import com.example.ibeproject.exceptions.InsufficientRoomsException;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;
  
    /**
     * Attempts to make a booking based on the provided booking request.
     *
     * @param request The booking request details.
     * @return True if booking is successful, false otherwise.
     * @throws InsufficientRoomsException if there are not enough rooms available for booking.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean makeBooking(BookingRequestDTO request) {
        int availableRooms = updateAvailableRoomsAtomically(request.getRoomTypeId(), request.getNumRooms());
        
        if (availableRooms >= request.getNumRooms()) {
            insertBookingRecord(request);
            return true;
        } else {
            throw new InsufficientRoomsException("Insufficient rooms available for booking");
        }
    }   

    /**
     * Atomically updates the available rooms and retrieves the updated count.
     *
     * @param roomTypeId   The ID of the room type.
     * @param bookedRooms  The number of rooms to book.
     * @return The updated count of available rooms.
     */
    private int updateAvailableRoomsAtomically(int roomTypeId, int bookedRooms) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            connection.setAutoCommit(false);
            String selectSql = BookingConstants.SELECT_ROOM_AVAILABILITY_CONC_QUERY;
            String updateSql = BookingConstants.UPDATE_ROOM_AVAILABILITY_CONC_QUERY;
            
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                selectStatement.setInt(1, roomTypeId);
                ResultSet resultSet = selectStatement.executeQuery();
                
                int availableRooms = 0;
                if (resultSet.next()) {
                    availableRooms = resultSet.getInt("available_rooms");
                }
                

                if ((availableRooms - bookedRooms >= 0)) {
                    updateStatement.setInt(1, bookedRooms);
                    updateStatement.setInt(2, roomTypeId);
                    updateStatement.executeUpdate();
                }
                
                connection.commit(); 
                return availableRooms;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Inserts a booking record into the database.
     *
     * @param request The booking request details.
     */
    private void insertBookingRecord(BookingRequestDTO request) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String sql = BookingConstants.INSERT_BOOKING_CONC_QUERY;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(request.getCheckInDate()));
                statement.setDate(2, java.sql.Date.valueOf(request.getCheckOutDate()));
                statement.setInt(3, request.getRoomTypeId());
                statement.setInt(4, request.getNumRooms());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}