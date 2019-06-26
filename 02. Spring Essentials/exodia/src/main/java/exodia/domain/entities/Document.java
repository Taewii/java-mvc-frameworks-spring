package exodia.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Lob
    @Column(nullable = false)
    private String content;
}
