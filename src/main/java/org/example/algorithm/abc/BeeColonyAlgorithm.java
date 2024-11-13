package org.example.algorithm.abc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;

@Builder
public class BeeColonyAlgorithm {

    private Beehive beehive;
    private Integer numberOfIterations;

    public BeeResource calculateBestBeeResource() {
        BeeResource bestBeeResource = initializeBeeResource();

        for (int cycle = 0; cycle < numberOfIterations; cycle++) {
            bestBeeResource = executeEmployedBeesPhase(bestBeeResource);
            bestBeeResource = executeOnlookerBeesPhase(bestBeeResource);
            executeScoutsBeesPhase();
            System.out.println("Cycle " + cycle + ": Best fitness = " + bestBeeResource);
        }

        return bestBeeResource;
    }

    private BeeResource executeEmployedBeesPhase(BeeResource bestBeeResource) {
        return beehive.getBeePopulation().stream()
                .limit(beehive.getNumberEmployedBees())
                .reduce(bestBeeResource, this::executeDefaultBeesPhase, (resource1, resource2) -> resource1);
    }

    private BeeResource executeOnlookerBeesPhase(BeeResource bestBeeResource) {
        return IntStream.range(0, beehive.getNumberOnlookerBees())
                .mapToObj(i -> selectOnlookerBee())
                .reduce(bestBeeResource, this::executeDefaultBeesPhase, (resource1, resource2) -> resource1);
    }

    private void executeScoutsBeesPhase() {
        beehive.getBeePopulation().stream()
                .limit(beehive.getNumberEmployedBees())
                .filter(bee -> bee.getStagnationCounter() > beehive.getLimitForScoutBeesToAbandonSources())
                .forEach(bee -> bee.setNewRouteAndResetStagnation(beehive.generateRandomRoute()));
    }

    private Bee selectOnlookerBee() {
        List<Bee> bees = beehive.getBeePopulation();
        List<Double> fitnessValues = bees.stream()
                .map(bee -> beehive.calculateRouteFitness(bee.getRoute()))
                .collect(Collectors.toList());

        double totalFitness = fitnessValues.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double randomSelectionValue = new Random().nextDouble() * totalFitness;
        double cumulative = 0;

        for (int i = 0; i < bees.size(); i++) {
            cumulative += fitnessValues.get(i);
            if (cumulative >= randomSelectionValue) {
                return bees.get(i);
            }
        }

        return bees.get(0);
    }

    private BeeResource executeDefaultBeesPhase(BeeResource bestBeeResource, Bee bee) {
        List<Integer> newRoute = beehive.generateNewRoute(bee);
        double newFitness = beehive.calculateRouteFitness(newRoute);

        if (newFitness > beehive.calculateRouteFitness(bee.getRoute())) {
            bee.setNewRouteAndResetStagnation(newRoute);
        } else {
            bee.increaseStagnationCounter();
        }

        if (newFitness < bestBeeResource.getRouteDirectionFitness()) {
            return bestBeeResource;
        }

        return BeeResource.builder()
                .routeDirection(new ArrayList<>(newRoute))
                .routeDirectionFitness(newFitness)
                .build();
    }

    private BeeResource initializeBeeResource() {
        Bee bee = beehive.getBeePopulation().get(0);
        double bestFitness = beehive.calculateRouteFitness(bee.getRoute());

        return BeeResource.builder()
                .routeDirection(bee.getRoute())
                .routeDirectionFitness(bestFitness)
                .build();
    }
}
