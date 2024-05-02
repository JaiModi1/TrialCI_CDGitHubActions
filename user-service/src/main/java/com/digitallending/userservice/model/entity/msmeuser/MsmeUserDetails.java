package com.digitallending.userservice.model.entity.msmeuser;

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

import java.util.UUID;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "msme_user_details")
public class MsmeUserDetails {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marital_status", referencedColumnName = "attribute_value_id")
    private MsmeUserDetailsAttributeValue maritalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender", referencedColumnName = "attribute_value_id")
    private MsmeUserDetailsAttributeValue gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "attribute_value_id")
    private MsmeUserDetailsAttributeValue category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_qualification", referencedColumnName = "attribute_value_id")
    private MsmeUserDetailsAttributeValue educationalQualification;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDetails user;

}
