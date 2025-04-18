package com.nft.app.repository;

import com.nft.app.entity.DepositRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRequestRepository extends MongoRepository<DepositRequest, String> {

  Page<DepositRequest> findByStatusIn(List<String> statusList, Pageable pageable);

  Page<DepositRequest> findByStatusInOrderByUpdatedDateDesc(List<String> statusList, Pageable pageable);

  boolean existsByStatusAndTransactionId(String status, String txnId);

  Page<DepositRequest> findByEmailAndStatusInOrderByIdDesc(String email, List<String> statusList, Pageable pageable);

  Page<DepositRequest> findByEmailOrderByIdDesc(String email, Pageable pageable);


}
