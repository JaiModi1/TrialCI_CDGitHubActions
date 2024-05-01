package com.digitallending.userservice.model.entity.msmebusiness;

import com.digitallending.userservice.model.entity.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "msme_business_details")
public class MsmeBusinessDetails {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "company_name",length = 30)
    private String companyName;

    @Column(name = "company_pan",length = 10)
    private String companyPan;

    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(name = "business_experience")
    private Date businessExperience;

    @ManyToOne
    @JoinColumn(name = "business_type_id", referencedColumnName = "business_type_id")
    private BusinessType businessType;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDetails user;
}
