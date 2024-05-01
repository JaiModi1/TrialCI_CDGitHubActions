package com.digitallending.userservice.model.entity.msmebusiness;


import com.digitallending.userservice.model.entity.BusinessDocumentType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "business_type")
public class BusinessType {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "document_type_by_business_type",
            joinColumns = @JoinColumn(name = "business_type_id", referencedColumnName = "business_type_id"),
            inverseJoinColumns = @JoinColumn(name = "document_type_id", referencedColumnName = "document_type_id"))
    List<BusinessDocumentType> documentTypeList;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "business_type_id")
    private UUID businessTypeId;
    @Column(name = "business_type",length = 30)
    private String businessType;

}
