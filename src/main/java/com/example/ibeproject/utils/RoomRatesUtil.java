package com.example.ibeproject.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ibeproject.dto.roomdetails.RoomPricesDTO;

public class RoomRatesUtil {

    public static Map<String, Double> findRoomPricesByRoomTypeName(List<RoomPricesDTO> priceList) {
        Map<String, Map<String, Double>> roomNameRatesInDateRange = new HashMap<>();

        for (RoomPricesDTO price : priceList) {
            String roomName = price.getRoomTypeName();
            String currentDate = price.getDate();
            double roomRatePerNight = price.getBasicNightlyRate();

            roomNameRatesInDateRange.computeIfAbsent(roomName, k -> new HashMap<>())
            .compute(currentDate, (k, v) -> v == null || roomRatePerNight < v ? roomRatePerNight : v);
        }

        return RoomRatesUtil.findAveragePrice(roomNameRatesInDateRange);
    }

    private static Map<String, Double> findAveragePrice(Map<String, Map<String, Double>> ratesinDateRange) {
        Map<String, Double> roomTypeAveragePrice = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> entry : ratesinDateRange.entrySet()) {
            String roomName = entry.getKey();
            Map<String, Double> mapDateRate = entry.getValue();

            double totalPrice = 0;
            int totalNumberOfNights = 0;

            for (Double nightlyRate : mapDateRate.values()) {
                totalPrice += nightlyRate;
                totalNumberOfNights++;
            }

            double averagePrice = totalNumberOfNights > 0 ? totalPrice / totalNumberOfNights : 0;

            roomTypeAveragePrice.put(roomName, averagePrice);
        }

        return roomTypeAveragePrice;
    }
}
