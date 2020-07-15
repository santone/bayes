package gg.bayes.challenge.processor;

import gg.bayes.challenge.model.entity.HeroKill;
import gg.bayes.challenge.model.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class HeroKillProcessor implements Processor {

    private static final String REGEXP = "^\\[[0-9\\.:]+\\] npc\\_dota\\_hero\\_.+ is killed by npc\\_dota\\_hero\\_.+$";
    private final EntityManager entityManager;

    @Override
    public void process(String line, Match match) {
        if (line.matches(REGEXP)) {

            if (match.getHeroKills() == null) {
                match.setHeroKills(new HashSet<>());
            }

            String[] data = line.split(" ");

            HeroKill heroKill = new HeroKill();
            heroKill.setKiller(retrieveHeroName(data[5]));
            heroKill.setVictim(retrieveHeroName(data[1]));
            heroKill.setMatch(match);

            match.getHeroKills().add(entityManager.merge(heroKill));

        }
    }
}
