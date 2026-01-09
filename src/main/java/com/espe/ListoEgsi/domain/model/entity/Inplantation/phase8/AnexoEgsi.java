package com.espe.ListoEgsi.domain.model.entity.Inplantation.phase8;

import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ANEXO_EGSI")
public class AnexoEgsi {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ANEXO_EGSI", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idAnexoEgsi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DECLARATION", referencedColumnName = "ID_DECLARATION")
    private DeclarationAplicationSoa declaration;

    @Size(max = 10)
    @Column(name = "ITEM")
    private String item;

    @Size(max = 10)
    @Column(name = "SECTION")
    private String section;

    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;

    @Size(max = 200)
    @Column(name = "ACTUAL_STATE")
    private String actualState;

    @Size(max = 200)
    @Column(name = "APPLIES")
    private String applies;

    @Size(max = 500)
    @Column(name = "SELECTION_JUSTIFY")
    private String selectionJustify;

    @Size(max = 500)
    @Column(name = "OBSERVATION")
    private String observation;
}
