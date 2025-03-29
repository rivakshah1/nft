package com.nft.app.repository;

import com.nft.app.entity.TradingDetailsEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradingDetailsRepository extends MongoRepository<TradingDetailsEntity, String> {
    Optional<TradingDetailsEntity> findByCreatedByAndNftId(String userCode, @NotBlank(message = "nftId is required") String nftId);
}
