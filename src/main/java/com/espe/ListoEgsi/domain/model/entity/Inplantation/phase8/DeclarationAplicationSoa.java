package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8;

import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DECLARATION_APLICATION_SOA")
public class DeclarationAplicationSoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_DECLARATION", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idDeclaration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESS", nullable = false, referencedColumnName = "ID_PROCESS")
    private ProcessEgsi process;

    @Column(name = "INSTITUTE", length = 50)
    private String institute;

    @Column(name = "NUMBER_CONTROLLER", length = 50)
    private String numberController;

    @Column(name = "LEVEL_CONFIDENTIALITY", length = 50)
    private String levelConfidentiality;

    @Column(name = "DOCUMENT_RESPONSIBLE", length = 100)
    private String documentResponsible;
}
