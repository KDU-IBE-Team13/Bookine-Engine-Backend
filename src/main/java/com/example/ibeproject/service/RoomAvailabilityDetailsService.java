package com.example.ibeproject.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ibeproject.dto.roomdetails.RoomDetailsDTO;
import com.example.ibeproject.dto.roomdetails.RoomDetailsPriceListResponseDTO;
import com.example.ibeproject.dto.roomdetails.RoomDetailsPricesDTO;

@Service
public class RoomAvailabilityDetailsService {

        private final RoomDetailsService roomDetailsService;
        private final RoomAvailabilityService roomAvailabilityService;

        @Autowired
        public RoomAvailabilityDetailsService(RoomDetailsService roomDetailsService,
                        RoomAvailabilityService roomAvailabilityService) {
                this.roomDetailsService = roomDetailsService;
                this.roomAvailabilityService = roomAvailabilityService;
        }

        public RoomDetailsPriceListResponseDTO getAvailableRoomDetails(int tenantId, int propertyId, String checkInDate,
                        String checkOutDate, int page, int pageSize, Double minPrice, Double maxPrice, Integer beds,
                        List<String> roomTypes,
                        Integer guestCount, String bedType, String sort, String order) {

                List<RoomDetailsPricesDTO> availableRoomDetails = calculateAvailableRoomsDetailsAndPrices(tenantId,
                                propertyId,
                                checkInDate, checkOutDate);

                RoomDetailsPriceListResponseDTO responseDTO = new RoomDetailsPriceListResponseDTO();
                responseDTO.setRoomsDetails(availableRoomDetails);

                if (minPrice != null) {
                        responseDTO.getRoomsDetails().removeIf(room -> room.getBasicNightlyPrice() < minPrice);
                }
                if (maxPrice != null) {
                        responseDTO.getRoomsDetails().removeIf(room -> room.getBasicNightlyPrice() > maxPrice);
                }
                if (beds != null) {
                        responseDTO.getRoomsDetails()
                                        .removeIf(room -> (room.getSingleBed() + room.getDoubleBed()) != beds);
                }

                if (roomTypes != null && !roomTypes.isEmpty()) {
                        List<RoomDetailsPricesDTO> filteredRooms = new ArrayList<>();
                        for (RoomDetailsPricesDTO room : responseDTO.getRoomsDetails()) {
                                for (String roomType : roomTypes) {
                                        if (roomType != null && room.getRoomTypeName().toLowerCase()
                                                        .contains(roomType.toLowerCase())) {
                                                filteredRooms.add(room);
                                                break;
                                        }
                                }

                        }
                        responseDTO.setRoomsDetails(filteredRooms);
                }

                if (guestCount != null) {
                        responseDTO.getRoomsDetails()
                                        .removeIf(room -> (room.getMaxCapacity() != guestCount));
                }

                if (bedType != null) {
                        List<RoomDetailsPricesDTO> filteredRooms = new ArrayList<>();
                        for (RoomDetailsPricesDTO roomDetail : responseDTO.getRoomsDetails()) {
                                if (bedType.equals("single")) {
                                        if (roomDetail.getSingleBed() > 0 && roomDetail.getDoubleBed() == 0) {
                                                filteredRooms.add(roomDetail);
                                        }
                                } else if (bedType.equals("double")) {
                                        if (roomDetail.getDoubleBed() > 0 && roomDetail.getSingleBed() == 0) {
                                                filteredRooms.add(roomDetail);
                                        }
                                } else {
                                        throw new IllegalArgumentException("Wrong bed type: " + bedType);
                                }
                        }
                        responseDTO.setRoomsDetails(filteredRooms);
                }

                if (sort != null && order != null) {
                        Comparator<RoomDetailsPricesDTO> comparator = null;
                        switch (sort.toLowerCase()) {
                                case "price":
                                        comparator = Comparator.comparing(RoomDetailsPricesDTO::getBasicNightlyPrice);
                                        break;
                                case "beds":
                                        comparator = Comparator.comparingInt(
                                                        room -> room.getSingleBed() + room.getDoubleBed());
                                        break;
                                case "area":
                                        comparator = Comparator.comparing(RoomDetailsPricesDTO::getAreaInSquareFeet);
                                        break;
                                default:
                                        throw new IllegalArgumentException("Wrong Sort type");
                        }

                        if (comparator != null) {
                                if ("dsc".equalsIgnoreCase(order)) {
                                        comparator = comparator.reversed();
                                }
                                responseDTO.getRoomsDetails().sort(comparator);
                        }
                }
                List<RoomDetailsPricesDTO> list = responseDTO.getRoomsDetails();
                responseDTO.setResponseLength(list.size());

                int startIdx = (page - 1) * pageSize;
                int endIdx = Math.min(startIdx + pageSize, responseDTO.getRoomsDetails().size());
                if (startIdx >= 0 && endIdx >= startIdx) {
                        responseDTO.setRoomsDetails(responseDTO.getRoomsDetails().subList(startIdx, endIdx));
                } else {
                        responseDTO.setRoomsDetails(Collections.emptyList());
                }
                
                return responseDTO;
        }

        public List<RoomDetailsPricesDTO> calculateAvailableRoomsDetailsAndPrices(int tenantId, int propertyId,
                        String checkInDate, String checkOutDate) {
                Map<String, Boolean> roomAvailabilityMap = roomAvailabilityService.getRoomsAvailability(
                                tenantId, propertyId, checkInDate, checkOutDate);

                List<RoomDetailsDTO> allRoomDetails = roomDetailsService.getAllRooms();

                Map<String, Double> roomPrices = roomDetailsService.getRoomDetails(tenantId, propertyId, checkInDate,
                                checkOutDate);

                return allRoomDetails.stream()
                                .filter(roomDetail -> roomAvailabilityMap.getOrDefault(roomDetail.getRoomTypeName(),
                                                false))
                                .map(roomDetail -> {
                                        RoomDetailsPricesDTO roomDetailsPricesDTO = new RoomDetailsPricesDTO();
                                        roomDetailsPricesDTO.setRoomTypeId(roomDetail.getRoomTypeId());
                                        roomDetailsPricesDTO.setRoomTypeName(roomDetail.getRoomTypeName());
                                        roomDetailsPricesDTO.setAreaInSquareFeet(roomDetail.getAreaInSquareFeet());
                                        roomDetailsPricesDTO.setSingleBed(roomDetail.getSingleBed());
                                        roomDetailsPricesDTO.setDoubleBed(roomDetail.getDoubleBed());
                                        roomDetailsPricesDTO.setMaxCapacity(roomDetail.getMaxCapacity());
                                        roomDetailsPricesDTO.setPropertyAddress(roomDetail.getPropertyAddress());
                                        roomDetailsPricesDTO.setBasicNightlyPrice(
                                                        roomPrices.get(roomDetail.getRoomTypeName()));
                                        return roomDetailsPricesDTO;
                                })
                                .collect(Collectors.toList());
        }
}
