package by.kuzmich.statistics;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {

    @Test
    void invalidNumberFloorFrom() {
        assertThrows(IllegalArgumentException.class, ()->
                StatisticsService.of(0, 2));
    }

    @Test
    void invalidNumberFloorTo() {
        assertThrows(IllegalArgumentException.class, ()->
                StatisticsService.of(2, 0));
    }

    @Test
    void createWell() {
        StatisticsService statisticsService = StatisticsService.of(1,5);

        assertThat(statisticsService.getNumberFloorTo(), is(5));
        assertThat(statisticsService.getNumberFloorFrom(), is(1));
    }
}