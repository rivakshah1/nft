package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.CreateNFTRequest;
import com.nft.app.dto.request.ImageData;
import com.nft.app.dto.response.CreateNFTResponse;
import com.nft.app.dto.response.PageResponse;
import com.nft.app.entity.NFTDetails;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.InvestmentTypException;
import com.nft.app.exception.NftException;
import com.nft.app.repository.NFTRepository;
import com.nft.app.util.ChecksumUtil;
import com.nft.app.util.InvestmentIdGenerator;
import com.nft.app.vo.Base64MultipartFileVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NFTService {

    private final NFTRepository nftRepository;
    private final GridFsService gridFsService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public PageResponse<List<CreateNFTResponse> > getAllNFTDetails(Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<NFTDetails> investmentTypePage = nftRepository.findAll(pageable);

            List<NFTDetails> content = investmentTypePage.getContent();
            List<String> imageIds = content.stream().map(NFTDetails::getImageId).toList();
            List<ImageData> imageData = gridFsService.getFileDetailsByIds(imageIds);
            Map<String, ImageData> imageDataMap = imageData.stream().collect(Collectors.toMap(ImageData::getImageId, img -> img));
            List<CreateNFTResponse> investmentResponses = getInvestmentResponses(content, imageDataMap);
            return new PageResponse<>(
                    investmentTypePage.getTotalElements(),
                    investmentResponses
            );
        }
        return new PageResponse<>(0L, List.of());
    }

    public PageResponse<List<CreateNFTResponse> > getAllNFTDetails(String investmentType, String level,
                                                                   Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<NFTDetails> investmentTypePage = null;
            if (Objects.nonNull(investmentType) && Objects.nonNull(level)) {
                investmentTypePage = nftRepository.findByInvestmentTypeIdAndLevelId(
                        investmentType, level, pageable);
            } else if (Objects.nonNull(investmentType)) {
                investmentTypePage = nftRepository.findByInvestmentTypeId(
                        investmentType, pageable);
            } else if (Objects.nonNull(level)) {
                investmentTypePage = nftRepository.findByLevelId(level, pageable);
            } else
                investmentTypePage = nftRepository.findAll(pageable);

            List<NFTDetails> content = investmentTypePage.getContent();
            List<String> imageIds = content.stream().map(NFTDetails::getImageId).toList();
            List<ImageData> imageData = gridFsService.getFileDetailsByIds(imageIds);
            Map<String, ImageData> imageDataMap = imageData.stream().collect(Collectors.toMap(ImageData::getImageId, img -> img));
            List<CreateNFTResponse> investmentResponses = getInvestmentResponses(content, imageDataMap);
            return new PageResponse<>(
                    investmentTypePage.getTotalElements(),
                    investmentResponses
            );
        }
        return new PageResponse<>(0L, List.of());
    }

    private static List<CreateNFTResponse> getInvestmentResponses(List<NFTDetails> content, Map<String, ImageData> imageDataMap) {
        return getCreateInvestmentResponses(content, imageDataMap);
    }

    private static List<CreateNFTResponse> getCreateInvestmentResponses(List<NFTDetails> NFTDetailss, Map<String, ImageData> imageDataMap) {

        List<CreateNFTResponse> NFTDetailsResponses = new ArrayList<>();

        for (NFTDetails nftDetails : NFTDetailss) {
            NFTDetailsResponses.add(
                    new CreateNFTResponse(
                            nftDetails,
                            imageDataMap.get(nftDetails.getImageId())
                    )
            );
        }
        return NFTDetailsResponses;
    }

    public void createNFTDetails(CreateNFTRequest NFTDetailsRequest) {

        try {
            Base64MultipartFileVo multipartFileVo = new Base64MultipartFileVo(
                    NFTDetailsRequest.getImage().getImage(),
                    NFTDetailsRequest.getImage().getName(),
                    NFTDetailsRequest.getImage().getContentType());

            String checkSum = ChecksumUtil.getCheckSum(multipartFileVo.getBytes());

            Optional<NFTDetails> NFTDetailsCheckSum = nftRepository.findByCheckSum(checkSum);
            if (NFTDetailsCheckSum.isPresent())
                throw new RuntimeException("");
            String fileId = gridFsService.uploadFile(multipartFileVo);
            NFTDetails nftDetails = new NFTDetails();
            nftDetails.setName(NFTDetailsRequest.getName());
            nftDetails.setNftCode(InvestmentIdGenerator.generateInvestmentId(NFTDetailsRequest.getInvestmentTypeId()));
            nftDetails.setCategory(NFTDetailsRequest.getCategory());
            nftDetails.setBuyPrice(NFTDetailsRequest.getBuyPrice());
            nftDetails.setProfit(NFTDetailsRequest.getProfit());
            nftDetails.setStatus(NFTDetailsRequest.getStatus());
            nftDetails.setBlockPeriod(NFTDetailsRequest.getBlockPeriod());
            nftDetails.setInvestmentTypeId(NFTDetailsRequest.getInvestmentTypeId());
            nftDetails.setLevelId(NFTDetailsRequest.getLevelId());
            nftDetails.setOwnerName(NFTDetailsRequest.getOwnerName());
            nftDetails.setImageId(fileId);
            nftDetails.setCheckSum(checkSum);
            nftDetails.setCreatedAt(LocalDateTime.now());
            nftDetails.setStatus(NFTDetailsRequest.getStatus());
            nftRepository.save(nftDetails);
        } catch (NftException e) {
          throw e;
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_USER_LEVEL_FAILED);
        }
    }


    public CreateNFTResponse getNFTDetailsById(String id) {
        Optional<NFTDetails> nftDetails = nftRepository.findById(id);
        if (nftDetails.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_NFT_DETAILS);
        List<ImageData> imageData = gridFsService.getFileDetailsByIds(List.of(nftDetails.get().getImageId()));
        return new CreateNFTResponse(nftDetails.get(), Objects.nonNull(imageData)? imageData.getFirst(): null);
    }


    public void deleteNFTDetails(String id) {
        Optional<NFTDetails> NFTDetails = nftRepository.findById(id);
        if (NFTDetails.isEmpty())
            throw new RuntimeException("");
        gridFsService.deleteFile(NFTDetails.get().getImageId());
        nftRepository.deleteById(id);
    }
}