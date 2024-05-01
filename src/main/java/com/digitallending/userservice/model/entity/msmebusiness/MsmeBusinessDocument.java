package com.digitallending.userservice.model.entity.msmebusiness;

import com.digitallending.userservice.model.entity.BusinessDocumentType;
import com.digitallending.userservice.model.entity.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "msme_business_documents")
public class MsmeBusinessDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "business_document_id")
    private UUID businessDocumentId;

    @ManyToOne
    @JoinColumn(name = "document_type_id", referencedColumnName = "document_type_id")
    private BusinessDocumentType businessDocumentType;

    @Lob
    @Column(name = "document_content")
    private byte[] documentContent;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDetails user;
}
