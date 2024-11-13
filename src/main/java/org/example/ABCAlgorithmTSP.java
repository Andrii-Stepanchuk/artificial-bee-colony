package org.example;

import java.util.Arrays;
import java.util.Random;

public class ABCAlgorithmTSP {
    private static final int NUM_CITIES = 5; // Number of cities
    private static final int NUM_EMPLOYED_BEES = 10; // Number of employed bees
    private static final int NUM_ONLOOKER_BEES = 10; // Number of onlooker bees
    private static final int MAX_CYCLES = 100; // Maximum number of iterations
    private static final int LIMIT = 50; // Limit for scouts to abandon food sources
    private static final Random random = new Random();

    // Distance matrix (symmetric TSP distance matrix)
    private static final double[][] distances = {
            {0, 29, 20, 21, 16},
            {29, 0, 15, 17, 28},
            {20, 15, 0, 23, 12},
            {21, 17, 23, 0, 30},
            {16, 28, 12, 30, 0}
    };

    // Calculate the total distance of a route
    private static double calculateRouteDistance(int[] route) {
        double totalDistance = 0;
        for (int i = 0; i < route.length - 1; i++) {
            totalDistance += distances[route[i]][route[i + 1]];
        }
        totalDistance += distances[route[route.length - 1]][route[0]]; // Return to start city
        return totalDistance;
    }

    // Calculate fitness (inverse of distance, higher fitness for shorter distance)
    private static double calculateFitness(int[] route) {
        double distance = calculateRouteDistance(route);
        return 1.0 / (1 + distance); // Fitness function
    }

    // Initialize the population with random solutions
    private static int[][] initializePopulation() {
        int[][] population = new int[NUM_EMPLOYED_BEES][NUM_CITIES];
        for (int i = 0; i < NUM_EMPLOYED_BEES; i++) {
            population[i] = generateRandomRoute();
        }
        return population;
    }

    // Generate a random route (a permutation of city indices)
    private static int[] generateRandomRoute() {
        int[] route = new int[NUM_CITIES];
        for (int i = 0; i < NUM_CITIES; i++) {
            route[i] = i;
        }
        for (int i = 0; i < NUM_CITIES; i++) {
            int j = random.nextInt(NUM_CITIES);
            int temp = route[i];
            route[i] = route[j];
            route[j] = temp;
        }
        return route;
    }

    // Update a solution by swapping two random cities in the route
    private static int[] updateSolution(int[] route) {
        int[] newRoute = Arrays.copyOf(route, NUM_CITIES);
        int i = random.nextInt(NUM_CITIES);
        int j = random.nextInt(NUM_CITIES);
        // Swap two cities
        int temp = newRoute[i];
        newRoute[i] = newRoute[j];
        newRoute[j] = temp;
        return newRoute;
    }

    // Run the ABC algorithm
    public static int[] runABC() {
        int[][] population = initializePopulation();
        int[] bestSolution = population[0];
        double bestFitness = calculateFitness(bestSolution);
        int[] trial = new int[NUM_EMPLOYED_BEES];

        for (int cycle = 0; cycle < MAX_CYCLES; cycle++) {

            // Employed bees phase
            for (int i = 0; i < NUM_EMPLOYED_BEES; i++) {
                int[] newSolution = updateSolution(population[i]);
                double newFitness = calculateFitness(newSolution);

                if (newFitness > calculateFitness(population[i])) {
                    population[i] = newSolution;
                    trial[i] = 0;
                } else {
                    trial[i]++;
                }

                if (newFitness > bestFitness) {
                    bestSolution = newSolution;
                    bestFitness = newFitness;
                }
            }

            // Onlooker bees phase
            for (int i = 0; i < NUM_ONLOOKER_BEES; i++) {
                int selectedBee = selectBee(population, bestFitness);
                int[] newSolution = updateSolution(population[selectedBee]);
                double newFitness = calculateFitness(newSolution);

                if (newFitness > calculateFitness(population[selectedBee])) {
                    population[selectedBee] = newSolution;
                    trial[selectedBee] = 0;
                } else {
                    trial[selectedBee]++;
                }

                if (newFitness > bestFitness) {
                    bestSolution = newSolution;
                    bestFitness = newFitness;
                }
            }

            // Scout bees phase
            for (int i = 0; i < NUM_EMPLOYED_BEES; i++) {
                if (trial[i] > LIMIT) {
                    population[i] = generateRandomRoute();
                    trial[i] = 0;
                }
            }

            // Print progress
            System.out.println("Cycle " + cycle + ": Best fitness = " + bestFitness);
        }

        return bestSolution;
    }

    // Select an onlooker bee based on fitness
    private static int selectBee(int[][] population, double bestFitness) {
        double totalFitness = Arrays.stream(population)
                .mapToDouble(ABCAlgorithmTSP::calculateFitness)
                .sum();
        double r = random.nextDouble() * totalFitness;
        double cumulative = 0;
        for (int i = 0; i < population.length; i++) {
            cumulative += calculateFitness(population[i]);
            if (cumulative >= r) {
                return i;
            }
        }
        return 0; // Default return value
    }

    // Main method to run the algorithm
    public static void main(String[] args) {
        int[] bestRoute = runABC();
        System.out.println("Best Route: " + Arrays.toString(bestRoute));
        System.out.println("Best Distance: " + calculateRouteDistance(bestRoute));
    }
}
