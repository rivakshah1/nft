package com.nft.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateUserLevelRequest {
    private String id;
    @NotBlank(message = "Name is required")
    private String name;
    private ImageData image;
    @NotBlank(message = "content type is required")
    private String contentType;
    @NotNull(message = "base level is required")
    private Boolean baseLevel;
    @NotNull(message = "start price is required")
    private Integer startPrice;
    @NotNull(message = "end price is required")
    private Integer endPrice;
    @NotNull(message = "start profit is required")
    private Integer startProfit;
    @NotNull(message = "end profit is required")
    private Integer endProfit;
    @NotNull(message = "handling fees is required")
    private BigDecimal handlingFees;
}