package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.model.entity.Match;
import gg.bayes.challenge.processor.Processor;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private static final String GET_MATCH_QUERY = "SELECT e.killer, COUNT(e.killer) FROM HeroKill e WHERE e.match.id = :matchId GROUP BY e.killer";
    private static final String GET_DAMAGE_QUERY = "SELECT e.target, COUNT(e.target), SUM(e.damage) FROM HeroDamage e WHERE e.match.id = :matchId and e.heroName = :heroName GROUP BY e.target";
    private static final String GET_SPELL_QUERY = "SELECT e.spell, COUNT(e.spell) FROM HeroSpell e WHERE e.match.id = :matchId and e.heroName = :heroName GROUP BY e.spell";

    private final List<Processor> processors;
    private final EntityManager entityManager;

    @Override
    public Long ingestMatch(String payload) {
        Match match = entityManager.merge(new Match());

        payload.lines().map(String::trim).filter(StringUtils::isNotBlank).forEach(
               line -> {
                   processors.forEach(p -> p.process(line, match));
               }
        );

        return match.getId();
    }

    @Override
    public List<HeroKills> getMatch(Long matchId) {
        Query query = entityManager.createQuery(GET_MATCH_QUERY);
        query.setParameter("matchId", matchId);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map( o -> {

            HeroKills heroKills = new HeroKills();
            heroKills.setHero((String)o[0]);
            heroKills.setKills(((Long)o[1]).intValue());
            return heroKills;

        }).collect(toList());
    }

    @Override
    public List<HeroDamage> getDamage(Long matchId, String heroName) {
        Query query = entityManager.createQuery(GET_DAMAGE_QUERY);
        query.setParameter("matchId", matchId);
        query.setParameter("heroName", heroName);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map( o -> {

            HeroDamage heroDamage = new HeroDamage();
            heroDamage.setTarget((String)o[0]);
            heroDamage.setDamageInstances(((Long)o[1]).intValue());
            heroDamage.setTotalDamage(((Long)o[2]).intValue());
            return heroDamage;

        }).collect(toList());
    }

    @Override
    public List<HeroSpells> getSpells(Long matchId, String heroName) {
        Query query = entityManager.createQuery(GET_SPELL_QUERY);
        query.setParameter("matchId", matchId);
        query.setParameter("heroName", heroName);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map( o -> {

            HeroSpells heroSpells = new HeroSpells();
            heroSpells.setSpell((String)o[0]);
            heroSpells.setCasts(((Long)o[1]).intValue());
            return heroSpells;

        }).collect(toList());
    }
}
