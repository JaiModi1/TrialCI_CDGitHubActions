package com.digitallending.userservice.controller;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsAttributeDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsRequestDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.UserBRERequestDTO;
import com.digitallending.userservice.service.def.MsmeUserDetailsService;
import com.digitallending.userservice.service.def.UserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class MsmeUserDetailsController {

    @Autowired
    private MsmeUserDetailsService msmeUserDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/msme-details")
    public ResponseEntity<APIResponseDTO<MsmeUserDetailsResponseDTO>> saveMsmeUserDetails(@RequestHeader("UserId") UUID userId, @Valid @RequestBody MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {
        MsmeUserDetailsResponseDTO savedMsmeUserDetailsResponseDTO = msmeUserDetailsService.saveMsmeUserDetails(userId, msmeUserDetailsRequestDTO);
        APIResponseDTO<MsmeUserDetailsResponseDTO> apiResponseDTO = APIResponseDTO.<MsmeUserDetailsResponseDTO>builder()
                .payload(savedMsmeUserDetailsResponseDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/msme-details")
    public ResponseEntity<APIResponseDTO<MsmeUserDetailsResponseDTO>> updateMsmeUserDetails(@RequestHeader("UserId") UUID userId, @Valid @RequestBody MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {
        UserOnBoardingStatus userStatus = userDetailsService.getUserStatus(userId);
        if (userStatus.compareTo(UserOnBoardingStatus.VERIFY_PAN) < 0 || userStatus.compareTo(UserOnBoardingStatus.ON_HOLD) > 0) {
            throw new UpdateException("This user can not update details");
        }
        MsmeUserDetailsResponseDTO savedMsmeUserDetailsResponseDTO = msmeUserDetailsService.updateMsmeUserDetails(userId, msmeUserDetailsRequestDTO);
        APIResponseDTO<MsmeUserDetailsResponseDTO> apiResponseDTO = APIResponseDTO.<MsmeUserDetailsResponseDTO>builder()
                .payload(savedMsmeUserDetailsResponseDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/msme-details")
    public ResponseEntity<APIResponseDTO<MsmeUserDetailsResponseDTO>> getMsmeUserDetails(@RequestHeader("UserId") UUID userId) {
        MsmeUserDetailsResponseDTO msmeUserDetailsResponseDTO = msmeUserDetailsService.getMsmeUserDetails(userId);
        APIResponseDTO<MsmeUserDetailsResponseDTO> apiResponseDTO = APIResponseDTO.<MsmeUserDetailsResponseDTO>builder()
                .payload(msmeUserDetailsResponseDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/msme-details/attribute-values")
    public ResponseEntity<APIResponseDTO<List<MsmeUserDetailsAttributeDTO>>> getMsmeUserDetailsPropertyValues() {
        APIResponseDTO<List<MsmeUserDetailsAttributeDTO>> apiResponseDTO = APIResponseDTO.<List<MsmeUserDetailsAttributeDTO>>builder()
                .payload(msmeUserDetailsService.getMsmeUserDetailsAttributeValues())
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}/bre-details")
    public ResponseEntity<APIResponseDTO<UserBRERequestDTO>> getUserDetailsForBRE(@PathVariable("userId") UUID userId) throws DetailsNotFoundException {
        UserBRERequestDTO savedUserBRERequestDTO = msmeUserDetailsService.getMsmeUserAllDetails(userId);
        APIResponseDTO<UserBRERequestDTO> apiResponseDTO = APIResponseDTO.<UserBRERequestDTO>builder()
                .payload(savedUserBRERequestDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }
}
