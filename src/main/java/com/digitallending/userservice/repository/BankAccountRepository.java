package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    List<BankAccount> findByUserUserId(UUID userId);

    BankAccount findByUserUserIdAndBankAccountId(UUID userId, UUID bankAccountId);

    Boolean existsByUserUserId(UUID userId);
}
