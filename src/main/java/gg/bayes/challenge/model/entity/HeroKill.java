package gg.bayes.challenge.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "hero_kill", indexes = @Index(columnList = "match_id", name = "ix_mid"))
public class HeroKill implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", insertable = false, updatable = false)
    private Match match;

    @NotNull
    private String killer;
    @NotNull
    private String victim;
}
