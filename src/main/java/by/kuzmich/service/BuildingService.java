package by.kuzmich.service;

import by.kuzmich.models.Elevator;
import by.kuzmich.models.Floor;
import by.kuzmich.models.FloorType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class BuildingService {

    public List<Floor> createFloor(int numberOfFloors) {

        checkArgument(numberOfFloors > 0, "Number of Floors must be positive");

        List<Floor> floorList = new ArrayList<>();

        int i = 1;
        while (i <= numberOfFloors){
            FloorType floorType;
            if(i == 1) {
                floorType = FloorType.FIRST;
            }
            else if (i == numberOfFloors){
                floorType = FloorType.LAST;
            }
            else {
                floorType = FloorType.INTERMEDIATE;
            }

            Floor floor = Floor.of( i, floorType);
            floor.setName("Floor #" + i);
            floorList.add(floor);
            i++;
        }
        return floorList;
    }


    public List<Elevator> createListElevator(int numberOfElevator){
        checkArgument(numberOfElevator > 0, "Number of elevators must be positive!");

        List<Elevator> elevatorList = new ArrayList<>();

        int i = 1;
        while (i <= numberOfElevator){
            Elevator elevator = new Elevator();
            elevator.setName("Elevator #" + i);
            elevatorList.add(elevator);
            i++;
        }

        return elevatorList;
    }
}
