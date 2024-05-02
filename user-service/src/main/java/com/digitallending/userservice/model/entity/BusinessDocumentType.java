package com.digitallending.userservice.model.entity;

import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "business_document_type")
public class BusinessDocumentType {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "documentTypeList")
    List<BusinessType> businessTypeList;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_type_id")
    private UUID businessDocumentTypeId;

    @Column(name = "document_type",length = 30)
    private String businessDocumentType;

}
