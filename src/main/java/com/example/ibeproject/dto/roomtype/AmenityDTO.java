package com.example.ibeproject.dto.roomtype;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AmenityDTO {
    @JsonProperty("title")
    private String title;

    @JsonProperty("show")
    private boolean show;
}
