package gg.bayes.challenge.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "hero_damage", indexes = @Index(columnList = "match_id,hero_name", name = "ix_damage_mid_h"))
public class HeroDamage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", insertable = false, updatable = false)
    private Match match;

    @Column(name = "hero_name")
    @NotNull
    private String heroName;
    @NotNull
    private String target;
    @NotNull
    private Integer damage;
}
