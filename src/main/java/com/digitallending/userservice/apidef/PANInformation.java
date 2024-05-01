package com.digitallending.userservice.apidef;

import com.digitallending.userservice.model.dto.kyc.paninformation.PANInformationRequestDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.PANInformationResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PANInformation {

    @POST("/v3/tasks/sync/verify_with_source/ind_pan")
    @Headers({"content-type: application/json",
            "X-RapidAPI-Host: pan-card-verification1.p.rapidapi.com"})
    Call<PANInformationResponseDTO> getAadharPanLinkStatus(
            @Body PANInformationRequestDTO panInformationRequestDTO,
            @Header("X-RapidAPI-Key") String key
    );
}
