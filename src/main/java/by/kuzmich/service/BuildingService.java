package by.kuzmich.service;

import by.kuzmich.models.Elevator;
import by.kuzmich.models.Floor;
import by.kuzmich.models.FloorType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class BuildingService {

    public List<Floor> createListFloors(int numberOfFloors) {

        checkArgument(numberOfFloors > 0, "Number of Floors must be positive");

        return IntStream.rangeClosed(1, numberOfFloors)
                .mapToObj(i -> {
                    Floor floor = Floor.of(i, getFloorType(numberOfFloors, i));
                    return floor;
                }).collect(Collectors.toList());
    }

    private FloorType getFloorType(final int numberOfFloors, int currentFloor) {
        FloorType floorType;
        if (currentFloor == 1) {
            floorType = FloorType.FIRST;
        } else if (currentFloor == numberOfFloors) {
            floorType = FloorType.LAST;
        } else {
            floorType = FloorType.INTERMEDIATE;
        }
        return floorType;
    }


    public List<Elevator> createListElevators(int numberOfElevator) {
        checkArgument(numberOfElevator > 0, "Number of elevators must be positive!");

        return IntStream.rangeClosed(1, numberOfElevator)
                .mapToObj(i -> {
                    Elevator elevator = new Elevator();
                    elevator.setName("Elevator #" + i);
                    return elevator;
                }).collect(Collectors.toList());
    }

}

