package com.digitallending.userservice.controller;

import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDetailsDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeDTO;
import com.digitallending.userservice.model.dto.business.SaveBusinessDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;
import com.digitallending.userservice.model.mapper.BusinessDetailsMapper;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.BusinessTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/business-details")
public class BusinessDetailsController {

    @Autowired
    private BusinessDetailsService businessDetailsService;

    @Autowired
    private BusinessTypeService businessTypeService;

    @Autowired
    private BusinessDetailsMapper businessDetailsMapper;

    @GetMapping("/business-types")
    public ResponseEntity<APIResponseDTO<List<BusinessTypeDTO>>> getListOfBusinessTypes() {


        return new ResponseEntity<>(APIResponseDTO.<List<BusinessTypeDTO>>builder()
                .payload(businessTypeService.getAllBusinessTypes())
                .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<APIResponseDTO<String>> saveBusinessDetails(@Valid @RequestBody SaveBusinessDetailsDTO businessDetailsDTO, @RequestHeader("UserId") UUID userId) {

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload(businessDetailsService.saveBusinessDetails(businessDetailsDTO, userId))
                .build(), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<APIResponseDTO<String>> updateBusinessDetails(@Valid @RequestBody SaveBusinessDetailsDTO businessDetailsDTO,
                                                                                    @RequestHeader("UserId") UUID userId) {

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload(businessDetailsService.updateBusinessDetails(businessDetailsDTO, userId))
                .build(), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<APIResponseDTO<BusinessDetailsDTO>> getBusinessDetails(@RequestHeader("UserId") UUID userId) throws DetailsNotFoundException {

        MsmeBusinessDetails details = businessDetailsService.getBusinessDetails(userId);

        return new ResponseEntity<>(APIResponseDTO.<BusinessDetailsDTO>builder()
                .payload(businessDetailsMapper.toResponseDTO(details))
                .build(), HttpStatus.OK);
    }
}
