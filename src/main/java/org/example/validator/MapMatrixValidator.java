package org.example.validator;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.algorithm.abc.MapOfAreas;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapMatrixValidator {

    public static void validateSquareMatrix(MapOfAreas mapOfAreas) {
        mapOfAreas.getDistancesOfAreas()
                .values().stream()
                .filter(row -> row.size() != mapOfAreas.getNumberOfAreas())
                .forEach(row -> {
                    throw new IllegalArgumentException("Матриця не є квадратною");
                });
    }

    public static void validateSymmetricMatrix(MapOfAreas mapOfAreas) {
        var distancesOfAreas = mapOfAreas.getDistancesOfAreas();
        var numberOfAreas = mapOfAreas.getNumberOfAreas();

        for (int originCityIndex = 0; originCityIndex < numberOfAreas; originCityIndex++) {
            for (int destinationCityIndex = originCityIndex + 1; destinationCityIndex < numberOfAreas; destinationCityIndex++) {
                validateOnSymmetricValues(distancesOfAreas, originCityIndex, destinationCityIndex);
            }
        }

    }

    private static void validateOnSymmetricValues(Map<Integer, List<Double>> distancesOfCities, int originCityIndex, int destinationCityIndex) {
        double distanceDirect = distancesOfCities.get(originCityIndex).get(destinationCityIndex);
        double distanceReverse = distancesOfCities.get(destinationCityIndex).get(originCityIndex);

        if (distanceDirect != distanceReverse) {
            throw new IllegalArgumentException("Матриця не є симетричною: distances[%d][%d] = %s, але distances[%d][%d] = %s"
                    .formatted(originCityIndex, destinationCityIndex, distanceDirect, destinationCityIndex, originCityIndex, distanceReverse)
            );
        }
    }
}
