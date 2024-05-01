package com.digitallending.userservice.model.dto.userregistration;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponseDTO {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;

    private UserOnBoardingStatus onBoardingStatus;


}
