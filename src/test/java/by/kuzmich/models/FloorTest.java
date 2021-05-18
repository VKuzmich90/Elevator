package by.kuzmich.models;

import by.kuzmich.service.CallService;
import by.kuzmich.service.FloorService;
import by.kuzmich.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class FloorTest {

    private Floor floor;

    @BeforeEach
    void addFloor() {
        floor = Floor.of(5, FloorType.INTERMEDIATE);
    }

    @Test
    void createWell() {
        Floor floorAdd = Floor.of(9, FloorType.LAST);

        assertThat(floorAdd.getFloorNumber(), is(9));
        assertThat(floorAdd.getFloorType(), is(FloorType.LAST));
    }

    @Test
    void invalidNullFloorType() {
        assertThrows(NullPointerException.class, () -> Floor.of(2, null));
    }

    @Test
    void invalidFloorNumber() {
        assertThrows(IllegalArgumentException.class, () -> Floor.of(0, FloorType.FIRST));
    }

    @Test
    void getQueueDownWell() {
        Passenger passenger1 = Passenger.of(75, 2);
        Passenger passenger2 = Passenger.of(101, 3);

        floor.addPassengerDown(passenger1);
        floor.addPassengerDown(passenger2);
    }

    @Test
    void getQueueDownInvalid() {
        Passenger passenger1 = Passenger.of(75, 2);
        Passenger passenger2 = Passenger.of(101, 3);

        Queue<Passenger> queuePassenger = new ConcurrentLinkedQueue<>();
        queuePassenger.add(passenger1);

        floor.addPassengerDown(passenger1);
        floor.addPassengerDown(passenger2);

        assertNotEquals(floor.getQueueDown(), queuePassenger);
    }

    @Test
    void getQueueUpWell() {
        Passenger passenger1 = Passenger.of(75, 9);
        Passenger passenger2 = Passenger.of(101, 7);

        floor.addPassengerUp(passenger1);
        floor.addPassengerUp(passenger2);
    }

    @Test
    void getQueueUpInvalid() {
        Passenger passenger1 = Passenger.of(75, 9);
        Passenger passenger2 = Passenger.of(101, 7);

        Queue<Passenger> queuePassenger = new ConcurrentLinkedQueue<>();
        queuePassenger.add(passenger1);

        floor.addPassengerUp(passenger1);
        floor.addPassengerUp(passenger2);

        assertNotEquals(floor.getQueueUp(), queuePassenger);
    }

    @Test
    void setCallServiceInvalidNullFloor() {

        assertThrows(NullPointerException.class, () -> floor.setCallService(null));
    }

    @Test
    void setCallServiceWell() {
        CallService callService = CallService.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);
    }


    @Test
    void setFloorServiceWell() {
        FloorService floorService = FloorService.of(new ArrayList<>());
        floor.setFloorService(floorService);
    }

    @Test
    void setFloorServiceInvalidNullFloor() {
        assertThrows(NullPointerException.class, () -> floor.setFloorService(null));
    }


    @Test
    void setPassengerServiceWell() {
        PassengerService passengerService = new PassengerService();
        floor.setPassengerService(passengerService);

        assertThat(floor.getPassengerService(), is(passengerService));
    }

    @Test
    void setPassengerServiceInvalidNull() {
        assertThrows(NullPointerException.class, () -> floor.setPassengerService(null));
    }

    @Test
    void takePassengerFromQueueUpWell() {
        Passenger passenger = floor.takePassengerFromQueueUp(150);

        assertNull(passenger);
    }

    @Test
    void takePassenerFromQueueUpInvalidFreeWeight() {
        assertThrows(IllegalArgumentException.class, () ->
                floor.takePassengerFromQueueUp(-5));
    }

    @Test
    void takePassengerFromQueueDownWell() {
        Passenger passenger = floor.takePassengerFromQueueDown(150);

        assertNull(passenger);
    }

    @Test
    void takePassengerFromQueueDownInvalidFreeWeight() {
        assertThrows(IllegalArgumentException.class, () -> floor.takePassengerFromQueueDown(-5));
    }

    @Test
    void runInvalidNullServiceFloor() {
        PassengerService passengerService = new PassengerService();
        floor.setPassengerService(passengerService);
        CallService callService = CallService.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);

        assertThrows(NullPointerException.class, () -> floor.start());
    }

    @Test
    void runInvalidNullServiceCall() {
        FloorService floorService = FloorService.of(new ArrayList<>());
        floor.setFloorService(floorService);
        PassengerService passengerService = new PassengerService();
        floor.setPassengerService(passengerService);

        assertThrows(NullPointerException.class, () -> floor.start());
    }

    @Test
    void runInvalidNullPassengerService() {
        FloorService floorService = FloorService.of(new ArrayList<>());
        floor.setFloorService(floorService);
        CallService callService = CallService.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);

        assertThrows(NullPointerException.class, () -> floor.start());
    }

    @Test
    void runWell() {
        FloorService floorService = FloorService.of(new ArrayList<>());
        floor.setFloorService(floorService);
        CallService callService = CallService.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);
        PassengerService passengerService = new PassengerService();
        floor.setPassengerService(passengerService);

        floor.stoppedThread();
        floor.start();

        assertThat(floor.getPassengerService(), is(passengerService));
    }

    @Test
    void addPassengerUpWell() {
        Passenger passenger = Passenger.of(80, 7);
        floor.addPassengerUp(passenger);

        assertThat(floor.getQueueUp().size(), is(1));
    }

    @Test
    void addPassengerUpInvalidNullPerson() {
        assertThrows(NullPointerException.class, () -> floor.addPassengerUp(null));

        assertThrows(IllegalArgumentException.class, () -> floor.addPassengerUp(Passenger.of(60, 2)));
    }

    @Test
    void addPassengerDownWell() {
        Passenger passenger = Passenger.of(80, 3);
        floor.addPassengerDown(passenger);

        assertThat(floor.getQueueDown().size(), is(1));
    }

    @Test
    void addPassengerDownInvalidNullPerson() {
        assertThrows(NullPointerException.class, () -> floor.addPassengerDown(null));

        assertThrows(IllegalArgumentException.class, () -> floor.addPassengerDown(Passenger.of(40, 7)));
    }
}