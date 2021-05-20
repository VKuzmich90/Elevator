package by.kuzmich.service;

import by.kuzmich.models.Elevator;
import by.kuzmich.models.Floor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuildingServiceTest {

    final BuildingService buildingService = new BuildingService();


    @Test
    void createListFloorWell() {
        List<Floor> floorList = buildingService.createListFloors(4);

        assertThat(floorList.size(), is(4));
    }

    @Test
    void createListFloorInvalidNumberOfFloors() {

        assertThrows(IllegalArgumentException.class, () ->
                buildingService.createListFloors(0));
    }

    @Test
    void createListElevatorWell() {
        List<Elevator> elevatorList = buildingService.createListElevators(4);

        assertThat(elevatorList.size(), is(4));

    }

    @Test
    void createListElevatorInvalidNumberOfElevators() {
        assertThrows(IllegalArgumentException.class, () ->
                buildingService.createListElevators(0));
    }

}