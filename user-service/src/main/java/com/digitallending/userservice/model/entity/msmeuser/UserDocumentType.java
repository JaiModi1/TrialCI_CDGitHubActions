package com.digitallending.userservice.model.entity.msmeuser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "user_document_type")
public class UserDocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_type_id")
    private UUID documentTypeId;

    @Column(name = "document_type",length = 30)
    private String documentType;


}
