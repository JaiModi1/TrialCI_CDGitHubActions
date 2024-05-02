package com.digitallending.userservice.model.dto.userregistration;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.dto.business.BusinessDetailsDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.BankAccountDTO;
import com.digitallending.userservice.model.dto.userdetails.MsmeUserDetailsDTO;
import com.digitallending.userservice.model.dto.userdetails.MsmeUserDocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFullProfileDTO {
    private String firstName;
    private String lastName;
    private Long phoneNo;
    private Date dob;
    private Role role;
    private String email;
    private UserOnBoardingStatus onBoardingStatus;
    private MsmeUserDetailsResponseDTO msmeUserDetails;
    private List<MsmeUserDocumentDTO> msmeUserDocuments;
    private BusinessDetailsDTO msmeBusinessDetails;
    private List<BusinessDocumentDTO> msmeBusinessDocuments;
    private List<BankAccountDTO> bankAccountList;
}
