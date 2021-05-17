package by.kuzmich.statistics;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@EqualsAndHashCode
@ToString
public class StatisticsService {
    private final int numberFloorTo;
    private final int numberFloorFrom;

    private StatisticsService(int numberFloorFrom, int numberFloorTo) {
        log.debug("Statistics service begins to be created!");

        this.numberFloorFrom = numberFloorFrom;
        this.numberFloorTo = numberFloorTo;

        log.debug("Statistics service created!");
    }

    public static StatisticsService of(int numberFloorFrom, int numberFloorTo){
        Preconditions.checkArgument(numberFloorFrom > 0, "Number floor must be positive");
        Preconditions.checkArgument(numberFloorTo > 0, "Number floor must be positive");

        return new StatisticsService(numberFloorFrom, numberFloorTo);
    }
}
