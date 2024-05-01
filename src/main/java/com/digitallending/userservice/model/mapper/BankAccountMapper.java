package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.userdetails.BankAccountDTO;
import com.digitallending.userservice.model.entity.BankAccount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountDTO toResponseDto(BankAccount bankAccount);

    BankAccount requestDtoTo(BankAccountDTO bankAccountDTO);

    List<BankAccountDTO> toResponseDtoList(List<BankAccount> bankAccountList);
}
