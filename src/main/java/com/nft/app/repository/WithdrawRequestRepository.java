package com.nft.app.repository;

import com.nft.app.entity.WithdrawRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRequestRepository extends MongoRepository<WithdrawRequest, String> {

  List<WithdrawRequest> findByEmailAndStatus(String email, String status);

  List<WithdrawRequest> findByStatusIn(List<String> statusList, Pageable pageable);

  List<WithdrawRequest> findByStatusInOrderByUpdatedDateDesc(List<String> statusList, Pageable pageable);


}
