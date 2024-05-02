package com.digitallending.userservice.service.def;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.model.dto.userdetails.BankAccountDTO;

import java.util.List;
import java.util.UUID;

public interface BankAccountService {

    List<BankAccountDTO> getAllBankAccountByUserId(UUID userId);

    BankAccountDTO getBankAccountByAccountId(UUID bankAccountId);

    BankAccountDTO createBankAccountByUserId(UUID userId, BankAccountDTO bankAccountDTO, Role role);

    BankAccountDTO updateBankAccountByUserId(UUID userId, UUID bankAccountId, BankAccountDTO bankAccountDTO);

    void deleteBankAccountByUserId(UUID userId, UUID bankAccountId);

    void bankAccountExist(UUID userId, UUID bankAccountId);

}
