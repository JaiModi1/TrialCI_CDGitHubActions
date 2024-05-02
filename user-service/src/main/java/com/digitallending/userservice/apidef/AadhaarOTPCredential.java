package com.digitallending.userservice.apidef;

import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarOTPCredentialDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AccessTokenDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AadhaarOTPCredential {
    @POST("/gateway/v0.5/sessions")
    Call<AccessTokenDTO> getaccessToken(
            @Body AadhaarOTPCredentialDTO aadharOTPCredentialDTO
    );
}
