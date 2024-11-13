package org.example.algorithm.abc;

import java.util.ArrayList;
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

    private List<Bee> beePopulation;

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

    public List<Integer> generateRandomRoute() {
        List<Integer> route = IntStream
                .range(0, mapOfAreas.getNumberOfAreas())
                .boxed().collect(Collectors.toList());

        Collections.shuffle(route, new Random());

        return route;
    }

    public double calculateRouteFitness(List<Integer> route) {
        return mapOfAreas.calculateRouteFitness(route);
    }

    public List<Integer> generateNewRoute(Bee bee) {
        Random random = new Random();

        List<Integer> newRoute = new ArrayList<>(bee.getRoute());
        int city1Index = random.nextInt(bee.getRoute().size());
        int city2Index = random.nextInt(bee.getRoute().size());

        Collections.swap(newRoute, city1Index, city2Index);

        return newRoute;
    }

    private List<Bee> initializeBeePopulation() {
        return IntStream.range(0, numberEmployedBees)
                .mapToObj(i -> new Bee(generateRandomRoute(), 0.0))
                .collect(Collectors.toList());
    }
}


