package com.digitallending.userservice.service.impl;


import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.BankAccountNotFoundException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.model.dto.userdetails.BankAccountDTO;
import com.digitallending.userservice.model.entity.BankAccount;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.mapper.BankAccountMapper;
import com.digitallending.userservice.repository.BankAccountRepository;
import com.digitallending.userservice.service.def.BankAccountService;
import com.digitallending.userservice.service.def.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    private String errorMessage = "Bank Account Not Exist";

    @Override
    public List<BankAccountDTO> getAllBankAccountByUserId(UUID userId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByUserUserId(userId);
        return bankAccountMapper.toResponseDtoList(bankAccounts);
    }

    @Override
    public BankAccountDTO createBankAccountByUserId(UUID userId, BankAccountDTO bankAccountDTO, Role role) {
        BankAccount bankAccount = bankAccountMapper.requestDtoTo(bankAccountDTO);
        UserDetails userDetails = userDetailsService.getUserDetails(userId);
        UserOnBoardingStatus userStatus = userDetails.getOnBoardingStatus();
        if ((role.equals(Role.MSME) && userStatus.equals(UserOnBoardingStatus.BUSINESS_DOC)) || (role.equals(Role.LENDER) && userStatus.equals(UserOnBoardingStatus.VERIFY_PAN))) {
            // allow update and change status
            userDetails.setOnBoardingStatus(UserOnBoardingStatus.BANK_DETAILS);

        } else if (userStatus.compareTo(UserOnBoardingStatus.BANK_DETAILS) < 0) {
            throw new InvalidUserException("Can Not Update/add bank details");
        }

        bankAccount.setUser(userDetails);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        return bankAccountMapper.toResponseDto(savedBankAccount);
    }

    @Override
    public BankAccountDTO updateBankAccountByUserId(UUID userId, UUID bankAccountId, BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountRepository.findByUserUserIdAndBankAccountId(userId, bankAccountId);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException(errorMessage);
        }
        bankAccount.setAccountNumber(Long.valueOf(bankAccountDTO.getAccountNumber()));
        bankAccount.setBankName(bankAccountDTO.getBankName());
        bankAccount.setAccountHolderName(bankAccountDTO.getAccountHolderName());
        bankAccount.setIfscCode(bankAccountDTO.getIfscCode());
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toResponseDto(savedBankAccount);
    }

    @Override
    public void deleteBankAccountByUserId(UUID userId, UUID bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findByUserUserIdAndBankAccountId(userId, bankAccountId);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException(errorMessage);
        }
        bankAccountRepository.delete(bankAccount);

    }

    @Override
    public void bankAccountExist(UUID userId, UUID bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findByUserUserIdAndBankAccountId(userId, bankAccountId);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException(errorMessage);
        }
    }

    @Override
    public BankAccountDTO getBankAccountByAccountId(UUID bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account Not Exist"));
        return bankAccountMapper.toResponseDto(bankAccount);
    }

}
