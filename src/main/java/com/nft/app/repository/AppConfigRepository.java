package com.nft.app.repository;

import com.nft.app.entity.AppConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends MongoRepository<AppConfig, String> {
}
