package by.kuzmich.models;

import by.kuzmich.service.BuildingService;
import by.kuzmich.service.CallService;
import by.kuzmich.service.FloorService;
import by.kuzmich.service.PassengerService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
@EqualsAndHashCode
public class Building {
    private final int numberOfFloors;
    private final int numberOfElevators;
    private final List<Floor> floors;
    private final BlockingQueue<Call> callQueue = new LinkedBlockingQueue<>();
    private final List<Elevator> elevators;

    @SneakyThrows
    private Building(int numberOfFloors, int numberOfElevators) {
        log.info("The building begins to be created");

        BuildingService buildingService = new BuildingService();

        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.floors = buildingService.createFloor(numberOfFloors);
        this.elevators = buildingService.createListElevator(numberOfElevators);

        log.info("The building was created");
    }

    public static Building of(int numberOfFloors, int numberOfElevators) {
        checkArgument(numberOfFloors > 1, "Incorrect number of floors");
        checkArgument(numberOfElevators > 0, "Number of elevators must be positive");

        return new Building(numberOfFloors, numberOfElevators);
    }

    public void start() {
        CallService callService = CallService.of(callQueue);
        FloorService serviceFloor = FloorService.of(floors);
        PassengerService passengerService = new PassengerService();

        floors.forEach(floor -> {
            floor.setCallService(callService);
            floor.setFloorService(serviceFloor);
            floor.setPassengerService(passengerService);
            floor.start();
        });

        elevators.forEach(elevator -> {
            elevator.setCallService(callService);
            elevator.setFloorService(serviceFloor);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            elevator.start();
        });
    }

    public List<Floor> getFloors() {
        return List.copyOf(floors);
    }

    public List<Elevator> getElevators() {
        return List.copyOf(elevators);
    }

    public List<Call> getCallQueue() {
        return List.copyOf(callQueue);
    }
}
