package com.nft.app.dto.response;

import com.nft.app.entity.InvestmentType;
import com.nft.app.entity.UserLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateInvestmentResponse {
    private String id;
    private String name;
    private List<UserLevel> allowedLevels;

    public CreateInvestmentResponse(InvestmentType investmentType) {
        this.id = investmentType.getId();
        this.name = investmentType.getName();
        this.allowedLevels = investmentType.getAllowedLevels();
    }
}