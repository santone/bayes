package gg.bayes.challenge.processor;

import gg.bayes.challenge.model.entity.HeroDamage;
import gg.bayes.challenge.model.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class HeroDamageProcessor implements Processor {

    private static final String REGEXP = "^\\[[0-9\\.:]+\\] npc\\_dota\\_hero\\_.+ hits npc\\_dota\\_hero\\_.+ with .* for [0-9]+ damage .*$";
    private final EntityManager entityManager;

    @Override
    public void process(String line, Match match) {
        if (line.matches(REGEXP)) {

            if (match.getHeroDamages() == null) {
                match.setHeroDamages(new HashSet<>());
            }

            String[] data = line.split(" ");

            HeroDamage heroDamage = new HeroDamage();
            heroDamage.setHeroName(retrieveHeroName(data[1]));
            heroDamage.setTarget(retrieveHeroName(data[3]));
            heroDamage.setDamage(Integer.parseInt(data[7]));
            heroDamage.setMatch(match);

            match.getHeroDamages().add(entityManager.merge(heroDamage));

        }
    }
}
