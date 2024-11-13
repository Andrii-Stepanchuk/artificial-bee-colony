package org.example.algorithm.abc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Builder;

@Builder
public class BeeColonyAlgorithm {

    private Beehive beehive;
    private Integer numberOfIterations;

    private static final Random random = new Random();

    public BeeResource calculateBestBeeResource() {
        List<Integer> beesStagnationCounter = new ArrayList<>(Collections.nCopies(beehive.getNumberEmployedBees(), 0));
        BeeResource bestBeeResource = initializeBeeResource();

        for (int cycle = 0; cycle < numberOfIterations; cycle++) {

            // Фаза зайнятих бджіл
            for (int i = 0; i < beehive.getNumberEmployedBees(); i++) {
                List<Integer> newSolution = generateNewRoute(beehive.getBeePopulation().get(i));
                double newFitness = calculateRouteFitness(newSolution);

                if (newFitness > calculateRouteFitness(beehive.getBeePopulation().get(i))) {
                    beehive.getBeePopulation().set(i, newSolution);
                    beesStagnationCounter.set(i, 0);
                } else {
                    beesStagnationCounter.set(i, beesStagnationCounter.get(i) + 1);
                }

                if (newFitness > bestBeeResource.getRouteDirectionFitness()) {
                    bestBeeResource = BeeResource.builder()
                            .routeDirection(new ArrayList<>(newSolution))
                            .routeDirectionFitness(newFitness)
                            .build();
                }
            }

            // Фаза спостережливих бджіл
            for (int i = 0; i < beehive.getNumberOnlookerBees(); i++) {
                int selectedBee = selectOnlookerBee();
                List<Integer> newSolution = generateNewRoute(beehive.getBeePopulation().get(selectedBee));
                double newFitness = calculateRouteFitness(newSolution);

                if (newFitness > calculateRouteFitness(beehive.getBeePopulation().get(selectedBee))) {
                    beehive.getBeePopulation().set(selectedBee, newSolution);
                    beesStagnationCounter.set(selectedBee, 0);
                } else {
                    beesStagnationCounter.set(selectedBee, beesStagnationCounter.get(selectedBee) + 1);
                }

                if (newFitness > bestBeeResource.getRouteDirectionFitness()) {
                    bestBeeResource = BeeResource.builder()
                            .routeDirection(new ArrayList<>(newSolution))
                            .routeDirectionFitness(newFitness)
                            .build();
                }
            }

            // Фаза розвідників
            for (int i = 0; i < beehive.getNumberEmployedBees(); i++) {
                if (beesStagnationCounter.get(i) > beehive.getLimitForScoutBeesToAbandonSources()) {
                    beehive.getBeePopulation().set(i, beehive.generateRandomRoute());
                    beesStagnationCounter.set(i, 0);
                }
            }

            // Виведення поточного прогресу
            System.out.println("Cycle " + cycle + ": Best fitness = " + bestBeeResource);
        }

        return bestBeeResource;
    }

    private int selectOnlookerBee() {
        List<Double> fitnessValues = beehive.getBeePopulation().stream()
                .map(this::calculateRouteFitness)
                .toList();

        double totalFitness = fitnessValues.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double r = random.nextDouble() * totalFitness;
        double cumulative = 0;

        for (int i = 0; i < fitnessValues.size(); i++) {
            cumulative += fitnessValues.get(i);
            if (cumulative >= r) {
                return i;
            }
        }

        return 0;
    }

    private static List<Integer> generateNewRoute(List<Integer> currentRoute) {
        List<Integer> newRoute = new ArrayList<>(currentRoute);
        int city1Index = random.nextInt(currentRoute.size());
        int city2Index = random.nextInt(currentRoute.size());

        Collections.swap(newRoute, city1Index, city2Index);

        return newRoute;
    }

    private BeeResource initializeBeeResource() {
        List<Integer> routeDirection = beehive.getBeePopulation().get(0);
        double bestFitness = calculateRouteFitness(routeDirection);

        return BeeResource.builder()
                .routeDirection(routeDirection)
                .routeDirectionFitness(bestFitness)
                .build();
    }

    private double calculateRouteFitness(List<Integer> routeDirection) {
        double distance = calculateRouteDistance(routeDirection);
        return 1.0 / (1 + distance);
    }

    private double calculateRouteDistance(List<Integer> route) {
        var distancesOfAreas = beehive.getMapOfAreas().getDistancesOfAreas();

        double totalDistance = 0;

        for (int i = 0; i < route.size() - 1; i++) {
            int fromCity = route.get(i);
            int toCity = route.get(i + 1);
            totalDistance += distancesOfAreas.get(fromCity).get(toCity);
        }

        int lastCity = route.get(route.size() - 1);
        int startCity = route.get(0);
        totalDistance += distancesOfAreas.get(lastCity).get(startCity);

        return totalDistance;
    }
}
