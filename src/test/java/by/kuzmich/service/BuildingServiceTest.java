package by.kuzmich.service;

import by.kuzmich.models.Elevator;
import by.kuzmich.models.Floor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class BuildingServiceTest {

    BuildingService buildingService = new BuildingService();


    @Test
    void createFloorWell() {
        List<Floor> floorList = buildingService.createFloor(4);

        assertThat(floorList.size(), is(4));
    }

    @Test
    void createFloorInvalidNumberOfFloors() {

        assertThrows(IllegalArgumentException.class, ()->
                buildingService.createFloor(0));
    }

    @Test
    void createListElevatorWell() {
        List<Elevator> elevatorList = buildingService.createListElevator(4);

        assertThat(elevatorList.size(), is(4));

    }

    @Test
    void createListElevatorInvalidNumberOfElevators() {
        assertThrows(IllegalArgumentException.class, ()->
                buildingService.createListElevator(0));
    }

}