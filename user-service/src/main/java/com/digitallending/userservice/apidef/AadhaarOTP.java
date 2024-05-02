package com.digitallending.userservice.apidef;


import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.TransactionIDResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.VerifyOTPReqeustDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AadhaarOTP {

    @POST("api/v1/registration/aadhaar/generateOtp")
    Call<TransactionIDResponseDTO> generateOTP(
            @Body AadhaarDTO aadharDTO,
            @Header("Authorization") String Authorization
    );

    @POST("api/v1/registration/aadhaar/verifyOTP")
    Call<TransactionIDResponseDTO> verifyOTP(
            @Body VerifyOTPReqeustDTO verifyOTPReqeustDTO,
            @Header("Authorization") String Authorization
    );
}
