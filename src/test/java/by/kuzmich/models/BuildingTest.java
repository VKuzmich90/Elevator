package by.kuzmich.models;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    @Test
    void createWell() {
        Building building = Building.of(12, 5);

        assertThat(building.getNumberOfFloors(), is(12));
        assertThat(building.getNumberOfElevators(), is(5));
    }

    @Test
    void invalidNumberOfElevators() {
        assertThrows(IllegalArgumentException.class, () -> {
            Building building = Building.of(10, 0);
        });
    }

    @Test
    void getFloors() {
        Building building = Building.of(5, 2);

        assertThat(building.getFloors().size(), is(5));
    }

    @Test
    void getElevators() {
        Building building = Building.of(5, 2);

        assertThat(building.getElevators().size(), is(2));
    }

}