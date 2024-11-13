package org.example.algorithm.abc;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class BeeResource {

    private List<Integer> routeDirection;
    private double routeDirectionFitness;

}
