package com.digitallending.userservice.apidef;

import com.digitallending.userservice.model.dto.kyc.aadhaarpanlink.AadhaarPANInfoDTO;
import com.digitallending.userservice.model.dto.kyc.aadhaarpanlink.AadhaarPANLinkResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AadhaarPANLink {

    @POST("/getEntity")
    @Headers({"content-type: application/json",
            "X-RapidAPI-Host: verify-pan-aadhaar-link1.p.rapidapi.com"})
    Call<AadhaarPANLinkResponseDTO> getAadharPANLinkStatus(
            @Body AadhaarPANInfoDTO aadharPanInfo,
            @Header("X-RapidAPI-Key") String key
    );

}
