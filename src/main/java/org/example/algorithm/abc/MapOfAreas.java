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

    public void addNewAreaDistance(List<Double> areaDistance) {
        distancesOfAreas.put(distancesOfAreas.size(), areaDistance);
    }

    public Integer getNumberOfAreas() {
        return distancesOfAreas.size();
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

