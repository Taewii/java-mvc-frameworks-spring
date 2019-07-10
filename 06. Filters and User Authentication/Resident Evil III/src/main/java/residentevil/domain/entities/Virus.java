package residentevil.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import residentevil.domain.enums.Magnitude;
import residentevil.domain.enums.Mutation;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "viruses")
public class Virus extends BaseEntity {

    @NotBlank
    @Size(min = 3, max = 10)
    @Column(nullable = false, length = 10)
    private String name;

    @NotBlank
    @Size(min = 5, max = 100)
    @Column(nullable = false, length = 100, columnDefinition = "TEXT")
    private String description;

    @Size(max = 50)
    @Column(length = 50)
    private String sideEffects;

    @Column
    private String creator;

    @Column
    private boolean IsDeadly;

    @Column
    private boolean IsCurable;

    @Column
    @Enumerated(EnumType.STRING)
    private Mutation mutation;

    @Min(0)
    @Max(100)
    @Column
    private Byte turnoverRate;

    @Min(1)
    @Max(12)
    @Column
    private Byte hoursUntilTurn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Magnitude magnitude;

    @Past
    @Temporal(TemporalType.DATE)
    @Column
    private Date releasedOn;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "capitals_viruses",
            joinColumns = @JoinColumn(name = "virus_id"),
            inverseJoinColumns = @JoinColumn(name = "capital_id")
    )
    private Set<Capital> capitals = new HashSet<>();

    public void addCapital(Capital capital) {
        this.capitals.add(capital);
    }
}
