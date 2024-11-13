package org.example.algorithm.abc;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Beehive {

    private MapOfAreas mapOfAreas;
    private Integer numberEmployedBees;
    private Integer numberOnlookerBees;
    private Integer limitForScoutBeesToAbandonSources;

    private List<List<Integer>> beePopulation;

    @Builder
    public Beehive(MapOfAreas mapOfAreas,
                   Integer numberEmployedBees,
                   Integer numberOnlookerBees,
                   Integer limitForScoutBeesToAbandonSources) {
        this.mapOfAreas = mapOfAreas;
        this.numberEmployedBees = numberEmployedBees;
        this.numberOnlookerBees = numberOnlookerBees;
        this.limitForScoutBeesToAbandonSources = limitForScoutBeesToAbandonSources;
        this.beePopulation = initializeBeePopulation();
    }

    private List<List<Integer>> initializeBeePopulation() {
        return IntStream.range(0, numberEmployedBees)
                .mapToObj(employedBee -> generateRandomRoute())
                .collect(Collectors.toList());
    }

    public List<Integer> generateRandomRoute() {
        List<Integer> route = IntStream
                .range(0, mapOfAreas.getNumberOfAreas())
                .boxed().collect(Collectors.toList());

        Collections.shuffle(route, new Random());

        return route;
    }
}

