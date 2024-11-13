package org.example.algorithm.abc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.validator.MapMatrixValidator;

@NoArgsConstructor
public class MapOfAreas {

    @Getter
    private final Map<Integer, List<Double>> distancesOfAreas = new HashMap<>();

    public static MapBuilder builder() {
        return new MapBuilder();
    }

    public Integer getNumberOfAreas() {
        return distancesOfAreas.size();
    }

    public double calculateRouteFitness(List<Integer> route) {
        double distance = calculateRouteDistance(route);
        return 1.0 / (1 + distance);
    }

    private double calculateRouteDistance(List<Integer> route) {
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

    private void addNewAreaDistance(List<Double> areaDistance) {
        distancesOfAreas.put(distancesOfAreas.size(), areaDistance);
    }

    @NoArgsConstructor
    public static class MapBuilder {
        private final MapOfAreas mapOfAreas = new MapOfAreas();

        public MapBuilder addNewAreaDistance(List<Double> areaDistance) {
            mapOfAreas.addNewAreaDistance(areaDistance);
            return this;
        }

        public MapOfAreas build() {
            MapMatrixValidator.validateSquareMatrix(mapOfAreas);
            MapMatrixValidator.validateSymmetricMatrix(mapOfAreas);
            return mapOfAreas;
        }
    }
}

