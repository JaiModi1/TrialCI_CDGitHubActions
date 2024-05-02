package com.digitallending.userservice.model.entity.msmeuser;

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
@Table(name = "msme_user_documents")
public class MsmeUserDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "msme_user_document_id")
    private UUID msmeUserDocumentId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDetails user;

    @ManyToOne
    @JoinColumn(name = "document_type_id", referencedColumnName = "document_type_id")
    private UserDocumentType documentType;

    @Lob
    @Column(name = "document_content")
    private byte[] documentContent;

}
