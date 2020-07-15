package gg.bayes.challenge.processor;

import gg.bayes.challenge.model.entity.HeroSpell;
import gg.bayes.challenge.model.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class HeroSpellProcessor implements Processor {

    private static final String REGEXP = "^\\[[0-9\\.:]+\\] npc\\_dota\\_hero\\_.+ casts ability .* \\(.+\\) on .+$";
    private final EntityManager entityManager;

    @Override
    public void process(String line, Match match) {
        if (line.matches(REGEXP)) {

            if (match.getHeroSpells() == null) {
                match.setHeroSpells(new HashSet<>());
            }

            String[] data = line.split(" ");

            HeroSpell heroSpell = new HeroSpell();
            heroSpell.setHeroName(retrieveHeroName(data[1]));
            heroSpell.setSpell(data[4]);
            heroSpell.setMatch(match);

            match.getHeroSpells().add(entityManager.merge(heroSpell));

        }
    }
}
