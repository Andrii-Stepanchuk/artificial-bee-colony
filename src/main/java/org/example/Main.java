package org.example;

import java.util.Arrays;
import org.example.algorithm.abc.BeeColonyAlgorithm;
import org.example.algorithm.abc.BeeResource;
import org.example.algorithm.abc.Beehive;
import org.example.algorithm.abc.MapOfAreas;

public class Main {

    public static void main(String[] args) {
        MapOfAreas mapOfAreas = iniziliazeMapOfAreas();
        Beehive beehive = iniziliazeBeehive(mapOfAreas);
        BeeColonyAlgorithm beeColonyAlgorithm = iniziliazeBeeColonyAlgorithm(beehive);

        BeeResource bestBeeResource = beeColonyAlgorithm.calculateBestBeeResource();

        System.out.println(bestBeeResource);
    }

    private static MapOfAreas iniziliazeMapOfAreas() {
        return MapOfAreas.builder()
                .addNewAreaDistance(Arrays.asList(0.0, 29.0, 20.0, 21.0, 16.0))
                .addNewAreaDistance(Arrays.asList(29.0, 0.0, 15.0, 17.0, 28.0))
                .addNewAreaDistance(Arrays.asList(20.0, 15.0, 0.0, 23.0, 12.0))
                .addNewAreaDistance(Arrays.asList(21.0, 17.0, 23.0, 0.0, 30.0))
                .addNewAreaDistance(Arrays.asList(16.0, 28.0, 12.0, 30.0, 0.0))
                .build();
    }

    private static Beehive iniziliazeBeehive(MapOfAreas mapOfAreas) {
        return Beehive.builder()
                .mapOfAreas(mapOfAreas)
                .numberEmployedBees(10)
                .numberOnlookerBees(10)
                .limitForScoutBeesToAbandonSources(50)
                .build();
    }

    private static BeeColonyAlgorithm iniziliazeBeeColonyAlgorithm(Beehive beehive) {
        return BeeColonyAlgorithm.builder()
                .beehive(beehive)
                .numberOfIterations(100)
                .build();
    }
}