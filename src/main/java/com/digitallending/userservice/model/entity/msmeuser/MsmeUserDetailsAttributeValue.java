package com.digitallending.userservice.model.entity.msmeuser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="msme_user_details_attribute_value")
public class MsmeUserDetailsAttributeValue {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attribute_value_id")
    private UUID attributeValueId;

    @Column(length = 30)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", referencedColumnName = "attribute_id")
    private MsmeUserDetailsAttribute msmeUserDetailsAttribute;

}
