package com.digitallending.userservice.model.dto.userregistration;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateDobDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private Date date;
}
