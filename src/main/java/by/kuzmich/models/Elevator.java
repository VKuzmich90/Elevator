package by.kuzmich.models;

import by.kuzmich.service.CallService;
import by.kuzmich.service.FloorService;
import by.kuzmich.statistics.Statistics;
import by.kuzmich.statistics.StatisticsService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Elevator extends Thread {

    public static final int ELEVATOR_CAPACITY = 500;
    public static final int DOOR_SPEED = 1;
    public static final int ELEVATOR_SPEED = 1;
    public static final int WAITING_TIME = 2;

    @Getter
    private DirectionType direction;
    @Getter
    private Floor floor;
    private final List<Passenger> passengers = new ArrayList<>();
    @Getter
    private boolean isDoorOpened;

    private boolean stop;

    private FloorService floorService;
    private CallService callService;

    @Getter
    private final Statistics statistics = new Statistics();
    private final Map<StatisticsService, Integer> statisticsServiceMap = new HashMap<>();

    public Elevator() {
        this.direction = null;
        this.stop = false;
        this.isDoorOpened = false;
    }

    @SneakyThrows
    @Override
    public void run() {

        checkNotNull(callService, "Service call is null!");
        checkNotNull(floorService, "Service floor is null!");

        floor = floorService.getFloor(1);

        while (!isStop()) {
            Call call = getNextCall();

            if (call == null) {
                continue;
            }

            this.direction = call.getDirection();
            int minFloor = 1000;
            int maxFloor = 0;

            floor = floorService.moveToNeededFloor(floor.getFloorNumber(), call.getNumber(), direction);

            while (floor.getFloorNumber() != maxFloor && floor.getFloorNumber() != minFloor) {

                landPeople();
                if (direction == DirectionType.UP) {
                    int maxNeededFloorNumber = addAllPassengersUp();
                    maxFloor = Math.max(maxFloor, maxNeededFloorNumber);

                    doMoveLogic(maxFloor, call);

                } else if (direction == DirectionType.DOWN) {
                    int minNeededFloorNumber = addAllPassengersDown();
                    minFloor = Math.min(minFloor, minNeededFloorNumber);

                    doMoveLogic(minFloor, call);
                }
            }
        }
    }

    public int getPassengersWeight() {
        return passengers.stream().mapToInt(Passenger::getWeight).sum();
    }

    public int getFreeWeight() {
        return ELEVATOR_CAPACITY - getPassengersWeight();
    }

    public void setCallService(CallService callService) {
        checkNotNull(callService, "Call service is null!");
        this.callService = callService;
    }

    public void setFloorService(FloorService floorService) {
        checkNotNull(floorService, "Floor service is null!");
        this.floorService = floorService;
    }

    public void setFloor(Floor floor) {
        checkNotNull(floor, "Floor is null!");

        this.floor = floor;
    }

    public boolean isStop() {
        return stop;
    }

    public void stopTheThread() {
        stop = true;
    }

    public int addPassenger(Passenger passenger) {
        checkNotNull(passenger, "Passenger is null!");
        checkNotNull(floor, "Floor is null!");

        passengers.add(passenger);

        log.info("Floor: {}, Add {}", floor.getFloorNumber(), passenger);
        log.info("Load elevator = {}", getPassengersWeight());

        addServiceStatistics(passenger);
        return passenger.getFloorNumber();
    }

    private void addServiceStatistics(Passenger passenger) {
        checkNotNull(passenger, "passenger is null!");

        StatisticsService statisticsService = StatisticsService.of(floor.getFloorNumber(), passenger.getFloorNumber());

        if (!statisticsServiceMap.containsKey(statisticsService)) {
            statisticsServiceMap.put(statisticsService, 0);
        }
        statisticsServiceMap.put(statisticsService, statisticsServiceMap.get(statisticsService) + 1);
    }

    private int addPassengerDown() {
        floor.setPressDownOff();
        callService.deleteCall(floor.getFloorNumber(), direction);

        Passenger passengerAdd = floor.takePassengerFromQueueDown(getFreeWeight());
        if (passengerAdd != null) {
            return addPassenger(passengerAdd);
        }

        return 0;
    }

    @SneakyThrows
    private void closeDoor() {
        if (isDoorOpened()) {
            TimeUnit.SECONDS.sleep(WAITING_TIME);
            TimeUnit.SECONDS.sleep(DOOR_SPEED);
            isDoorOpened = false;
        }
    }

    private int addAllPassengersDown() {
        int minNeeded = 1000;
        while (!floor.isEmptyQueueDown()) {
            int neededFloorNumber = addPassengerDown();
            if (neededFloorNumber == 0) {
                break;
            }
            minNeeded = Math.min(minNeeded, neededFloorNumber);
        }
        addCallDown(floor);

        return minNeeded;
    }

    private int addPassengerUp() {
        floor.setPressUpOff();
        callService.deleteCall(floor.getFloorNumber(), direction);

        Passenger passengerAd = floor.takePassengerFromQueueUp(getFreeWeight());

        if (passengerAd != null) {
            return addPassenger(passengerAd);
        }

        return 0;
    }

    @SneakyThrows
    private int addAllPassengersUp() {
        int maxNeeded = 0;
        while (!floor.isEmptyQueueUp()) {
            if (!isDoorOpened()) {
                openTheDoor();
            }

            int neededFloorNumber = addPassengerUp();
            if (neededFloorNumber == 0) {
                break;
            }
            maxNeeded = Math.max(maxNeeded, neededFloorNumber);
        }
        addCallUp(floor);

        return maxNeeded;
    }


    private Call getNextCall() {
        if (!floor.isEmptyQueueUp()) {
            return callService.deleteCall(floor.getFloorNumber(), DirectionType.UP);
        } else if (!floor.isEmptyQueueDown()) {
            return callService.deleteCall(floor.getFloorNumber(), DirectionType.DOWN);
        } else {
            return callService.takeCall();
        }
    }

    private void addCallDown(Floor floor) {
        checkNotNull(floor, "Floor is null!");

        if (floor.needToPressButtonDown()) {
            floor.setPressDownOn();
            callService.addCall(floor.getFloorNumber(), direction);
        }
    }

    private void addCallUp(Floor floor) {
        checkNotNull(floor, "Floor is null!");

        if (floor.needToPressButtonUp()) {
            floor.setPressUpOn();
            callService.addCall(floor.getFloorNumber(), direction);
        }
    }

    @SneakyThrows
    public void openTheDoor() {
        TimeUnit.SECONDS.sleep(DOOR_SPEED);
        isDoorOpened = true;
    }

    public void landPeople() {
        List<Passenger> people = passengers.stream()
                .filter(person -> person.getFloorNumber() == floor.getFloorNumber())
                .collect(Collectors.toList());

        if (!people.isEmpty()) {
            if (!isDoorOpened()) {
                openTheDoor();
            }

            passengers.removeAll(people);
            int weightPeopleRemoved = people.stream().mapToInt(Passenger::getWeight).sum();

            log.info("Floor: {},remove: {}", floor.getFloorNumber(), people);
            addToStatistics();
        }
    }

    private void addToStatistics() {
        Map<StatisticsService, Integer> serviceToAddMap = statisticsServiceMap.entrySet().stream()
                .filter(entry -> entry.getKey().getNumberFloorTo() == floor.getFloorNumber())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        statistics.addStatistics(serviceToAddMap);
        serviceToAddMap.keySet().forEach(statisticsServiceMap::remove);
    }

    public List<Passenger> getPassengersList() {
        return List.copyOf(passengers);
    }

    @SneakyThrows
    public void doMoveLogic(int floorNumber, Call call) {
        if (floor.getFloorNumber() == call.getNumber() && passengers.isEmpty()) {
            return;
        }

        closeDoor();
        TimeUnit.SECONDS.sleep(ELEVATOR_SPEED);

        floor = floorService.nextFloor(floor, direction);

        if (floor.getFloorNumber() == floorNumber) {
            landPeople();
            closeDoor();
        }
    }
}
