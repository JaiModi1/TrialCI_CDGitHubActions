package com.digitallending.userservice.model.entity;


import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDocument;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_details")
public class UserDetails {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "first_name",length = 30)
    private String firstName;

    @Column(name = "last_name",length = 30)
    private String lastName;

    @Column(name = "phone_no")
    private Long phoneNo;

    private Date dob;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "on_boarding_status")
    private UserOnBoardingStatus onBoardingStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<BankAccount> bankAccountList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private List<MsmeBusinessDocument> msmeBusinessDocuments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<MsmeUserDocument> msmeUserDocuments;


}
