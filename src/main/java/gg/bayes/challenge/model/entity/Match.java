package gg.bayes.challenge.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "match")
public class Match implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Set<HeroKill> heroKills;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Set<HeroDamage> heroDamages;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Set<HeroSpell> heroSpells;
}
