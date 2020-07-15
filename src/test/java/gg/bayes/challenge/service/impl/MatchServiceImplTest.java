package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.processor.HeroDamageProcessor;
import gg.bayes.challenge.processor.HeroKillProcessor;
import gg.bayes.challenge.processor.HeroSpellProcessor;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(value = {MatchServiceImpl.class, HeroDamageProcessor.class, HeroKillProcessor.class, HeroSpellProcessor.class})
public class MatchServiceImplTest {
    @Autowired
    private MatchService matchService;


    @Test
    void testDamage(){
        Long matchId = matchService.ingestMatch(read("test_damage.txt"));
        List<HeroDamage> damages = matchService.getDamage(matchId, "bane");
        Assertions.assertNotNull(damages);
        Assertions.assertEquals(1, damages.size());

        HeroDamage heroDamage = damages.get(0);
        Assertions.assertEquals(2, heroDamage.getDamageInstances());
        Assertions.assertEquals(61, heroDamage.getTotalDamage());
        Assertions.assertEquals("puck", heroDamage.getTarget());
    }

    @Test
    void testKills(){
        Long matchId = matchService.ingestMatch(read("test_kills.txt"));
        List<HeroKills> kills = matchService.getMatch(matchId);
        Assertions.assertNotNull(kills);
        Assertions.assertEquals(1, kills.size());

        HeroKills heroKill = kills.get(0);
        Assertions.assertEquals("pangolier", heroKill.getHero());
        Assertions.assertEquals(1, heroKill.getKills());
    }

    @Test
    void testSpells(){
        Long matchId = matchService.ingestMatch(read("test_spells.txt"));
        List<HeroSpells> spells = matchService.getSpells(matchId, "mars");
        Assertions.assertNotNull(spells);
        Assertions.assertEquals(1, spells.size());

        HeroSpells heroSpells = spells.get(0);
        Assertions.assertEquals("mars_spear", heroSpells.getSpell());
        Assertions.assertEquals(2, heroSpells.getCasts());
    }

    private String read(String fileName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
