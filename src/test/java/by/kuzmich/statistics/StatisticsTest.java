package by.kuzmich.statistics;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatisticsTest {

    @Test
    void addStatisticsWell() {
        Statistics statistics = new Statistics();
        Map<StatisticsService, Integer> serviceMap = new HashMap<>();
        serviceMap.put(StatisticsService.of(1, 2), 1);
        statistics.addStatistics(serviceMap);

        assertThat(statistics.getStatisticsMap().size(), is(1));
    }

    @Test
    void addStatisticsInvalidNull() {
        Statistics statistics = new Statistics();

        assertThrows(NullPointerException.class, () -> statistics.addStatistics(null));
    }

}