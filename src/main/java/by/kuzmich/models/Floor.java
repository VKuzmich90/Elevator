package by.kuzmich.models;

import by.kuzmich.service.CallService;
import by.kuzmich.service.FloorService;
import by.kuzmich.service.PassengerService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Floor extends Thread {

    @Getter
    private boolean stopped;
    @Getter
    private final int floorNumber;
    @Getter
    private final FloorType floorType;
    @Getter
    private boolean isPressedDown;
    @Getter
    private boolean isPressedUp;

    private CallService callService;
    private FloorService floorService;
    @Getter
    private PassengerService passengerService;

    private final Queue<Passenger> queueDown;
    private final Queue<Passenger> queueUp;

    private final Lock lock = new ReentrantLock(true);

    private Floor(int floorNumber, FloorType floorType) {
        log.debug("The floor begins to be created");

        this.floorNumber = floorNumber;
        this.floorType = floorType;
        this.queueDown = new ConcurrentLinkedQueue<>();
        this.queueUp = new ConcurrentLinkedQueue<>();
        this.setName("Floor #" + floorNumber);

        log.debug("The floor was created");
    }

    public static Floor of(int floorNumber, FloorType floorType) {
        checkArgument(floorNumber > 0, "Floor number must be positive");
        checkNotNull(floorType, "Floor type must exist");

        return new Floor(floorNumber, floorType);
    }

    public void addPassengerUp(Passenger passenger) {
        checkNotNull(passenger, "Passenger must exist");
        checkArgument(floorNumber < passenger.getFloorNumber(), "The floor number must be < needed floor number");

        queueUp.add(passenger);
    }

    public void addPassengerDown(Passenger passenger) {
        checkNotNull(passenger, "Passenger must exist");
        checkArgument(floorNumber > passenger.getFloorNumber(), "The floor number must be > needed floor number");

        queueDown.add(passenger);
    }

    public List<Passenger> getQueueDown() {
        return List.copyOf(queueDown);
    }

    public List<Passenger> getQueueUp() {
        return List.copyOf(queueUp);
    }

    @SneakyThrows
    @Override
    public void run() {

        checkNotNull(callService, "Call service must exist!");
        checkNotNull(floorService, "Floor service must exist");
        checkNotNull(passengerService, "Passenger service must exist");

        while (!isStopped()) {

            Passenger passenger = passengerService.spawnPassenger(floorService.getNumberOfFloors());

            if (floorNumber > passenger.getFloorNumber()) {
                addPassengerDown(passenger);
                callService.addCall(floorNumber, DirectionType.DOWN);

            } else if (floorNumber < passenger.getFloorNumber()) {
                addPassengerUp(passenger);
                callService.addCall(floorNumber, DirectionType.UP);
            }
            log.info("Up: {}, Down: {}", queueUp, queueDown);
            TimeUnit.SECONDS.sleep(12);
        }
    }

    public void setCallService(CallService callService) {
        checkNotNull(callService, "Call service must exist");

        this.callService = callService;
    }

    public void setFloorService(FloorService floorService) {
        checkNotNull(floorService, "Floor service must exist");

        this.floorService = floorService;
    }

    public void setPassengerService(PassengerService passengerService) {
        checkNotNull(passengerService, "Passenger service must exist");

        this.passengerService = passengerService;
    }

    public void stopThread() {
        stopped = true;
    }

    public void setPressDownOff() {
        isPressedDown = false;
    }

    public void setPressDownOn() {
        isPressedDown = true;
    }

    public void setPressUpOff() {
        isPressedUp = false;
    }

    public void setPressUpOn() {
        isPressedUp = true;
    }

    public Passenger takePassengerFromQueueUp(int freeWeightInElevator) {
        checkArgument(freeWeightInElevator >= 0, "free elevator capacity les than 0!");

        lock.lock();

        Passenger passenger = null;
        Passenger firstPassenger = this.queueUp.peek();

        if (firstPassenger != null && firstPassenger.getWeight() <= freeWeightInElevator) {
            passenger = this.queueUp.poll();
            log.debug("Taken a passenger from the queue up: {}", firstPassenger);
        }

        lock.unlock();

        return passenger;
    }

    public Passenger takePassengerFromQueueDown(int freeWeightInElevator) {
        checkArgument(freeWeightInElevator >= 0, "free elevator capacity less than 0!");

        lock.lock();

        Passenger passenger = null;
        Passenger firstPassenger = this.queueDown.peek();

        if (firstPassenger != null && firstPassenger.getWeight() <= freeWeightInElevator) {
            passenger = this.queueDown.poll();
            log.debug("Taken a person from the queue down: {}", passenger);
        }

        lock.unlock();

        return passenger;
    }

    public boolean isEmptyQueueUp() {
        return this.queueUp.isEmpty();
    }

    public boolean isEmptyQueueDown() {
        return this.queueDown.isEmpty();
    }

    public boolean needToPressButtonDown() {
        return !isEmptyQueueDown();
    }

    public boolean needToPressButtonUp() {
        return !isEmptyQueueUp();
    }

}
