package org.example.algorithm.abc;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Bee {

    private List<Integer> route;
    private double stagnationCounter;

    public void setNewRouteAndResetStagnation(List<Integer> newRoute) {
        this.route = new ArrayList<>(newRoute);
        this.stagnationCounter = 0;
    }

    public void increaseStagnationCounter() {
        this.stagnationCounter++;
    }
}
