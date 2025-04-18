package com.nft.app.dto.response;

import com.nft.app.dto.request.ImageData;
import com.nft.app.entity.NFTDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNFTResponse {
    private String id;
    private String name;
    private ImageData image;
    private String checkSum;
    private String ownerName;
    private Double profit;
    private Double buyPrice;
    private Integer blockPeriod;
    private String category;
    private String investmentTypeId;
    private String userLevelId;
    private Boolean status;
    private String nftCode;

    public CreateNFTResponse(NFTDetails nftDetails, ImageData imageData) {
        this.id = nftDetails.getId();
        this.name = nftDetails.getName();
        this.image = imageData;
        this.checkSum = nftDetails.getCheckSum();
        this.ownerName = nftDetails.getOwnerName();
        this.profit = nftDetails.getProfit();
        this.buyPrice = nftDetails.getBuyPrice();
        this.blockPeriod = nftDetails.getBlockPeriod();
        this.category = nftDetails.getCategory();
        this.investmentTypeId = nftDetails.getInvestmentTypeId();
        this.userLevelId = nftDetails.getLevelId();
        this.status = nftDetails.getStatus();
        this.nftCode = nftDetails.getNftCode();
    }
}
