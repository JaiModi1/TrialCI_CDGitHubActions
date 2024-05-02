package com.digitallending.userservice.controller;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.BankAccountDTO;
import com.digitallending.userservice.service.def.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user/bank-account")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping
    public ResponseEntity<APIResponseDTO<List<BankAccountDTO>>> getAllBankAccountsByUserId(@RequestHeader("UserId") UUID userId) {
        List<BankAccountDTO> bankAccountDTOList = bankAccountService.getAllBankAccountByUserId(userId);
        APIResponseDTO<List<BankAccountDTO>> apiResponseDTO = APIResponseDTO.<List<BankAccountDTO>>builder()
                .payload(bankAccountDTOList)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{bankAccountId}")
    public ResponseEntity<APIResponseDTO<BankAccountDTO>> getBankAccountsByAccountId(@PathVariable("bankAccountId") UUID bankAccountId) {
        BankAccountDTO bankAccountDTO = bankAccountService.getBankAccountByAccountId(bankAccountId);
        APIResponseDTO<BankAccountDTO> apiResponseDTO = APIResponseDTO.<BankAccountDTO>builder()
                .payload(bankAccountDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }
    @GetMapping("/link-status")
    public ResponseEntity<APIResponseDTO<String>> checkBankAccountByUserIdAndBankAccountId(@RequestParam("userId") UUID userId, @RequestParam("bankAccountId") UUID bankAccountId) {
        bankAccountService.bankAccountExist(userId, bankAccountId);
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload("Bank Account Exist")
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<APIResponseDTO<BankAccountDTO>> createBankAccountsByUserId(@RequestHeader("UserId") UUID userId, @Valid @RequestBody BankAccountDTO bankAccountDTO, @RequestHeader("Role") Role role) {
        BankAccountDTO savedBankAccountDTO = bankAccountService.createBankAccountByUserId(userId, bankAccountDTO, role);
        APIResponseDTO<BankAccountDTO> apiResponseDTO = APIResponseDTO.<BankAccountDTO>builder()
                .payload(savedBankAccountDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<APIResponseDTO<BankAccountDTO>> updateBankAccountsByUserId(@RequestHeader("UserId") UUID userId, @Valid @RequestBody BankAccountDTO bankAccountDTO) {
        BankAccountDTO savedBankAccountDTO = bankAccountService.updateBankAccountByUserId(userId, bankAccountDTO.getBankAccountId(), bankAccountDTO);
        APIResponseDTO<BankAccountDTO> apiResponseDTO = APIResponseDTO.<BankAccountDTO>builder()
                .payload(savedBankAccountDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{bankAccountId}")
    public ResponseEntity<APIResponseDTO<String>> deleteBankAccountsByUserId(@RequestHeader("UserId") UUID userId, @PathVariable UUID bankAccountId) {
        bankAccountService.deleteBankAccountByUserId(userId, bankAccountId);
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload("Bank Account Successfully Deleted")
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }


}
