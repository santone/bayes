package gg.bayes.challenge.processor;

import gg.bayes.challenge.model.entity.Match;

@FunctionalInterface
public interface Processor {
    void process(String line, Match match);

    default String retrieveHeroName(String nameWithPrefix) {
        return nameWithPrefix.substring("npc_dota_hero_".length());
    }
}
