package by.kuzmich.models;

import by.kuzmich.service.CallService;
import by.kuzmich.service.FloorService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElevatorTest {

    @Test
    void runInvalidServiceCall() {
        Elevator elevator = new Elevator();
        FloorService service = FloorService.of(new ArrayList<>());
        elevator.setFloorService(service);

        assertThrows(NullPointerException.class, elevator::start);
    }

    @Test
    void runInvalidServiceFloor() {
        Elevator elevator = new Elevator();
        CallService service = CallService.of(new LinkedBlockingQueue<>());
        elevator.setCallService(service);

        assertThrows(NullPointerException.class, elevator::start);
    }

    @Test
    void runWell() {
        Elevator elevator = new Elevator();
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService serviceCall = CallService.of(queueCall);
        serviceCall.addCall(1, DirectionType.UP);
        elevator.setCallService(serviceCall);
        List<Floor> listFloor = new ArrayList<>();
        listFloor.add(Floor.of(1, FloorType.FIRST));
        FloorService floorService = FloorService.of(listFloor);
        elevator.setFloorService(floorService);

        elevator.stopTheThread();
        elevator.start();

    }

    @Test
    void setCallServiceInvalidNull() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, () -> elevator.setCallService(null));

    }

    @Test
    void setCallServiceWell() {
        Elevator elevator = new Elevator();
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(queueCall);
        callService.addCall(1, DirectionType.UP);
        elevator.setCallService(callService);
    }

    @Test
    void setFloorServiceInvalidNull() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, () -> elevator.setFloorService(null));
    }

    @Test
    void setFloorServiceWell() {
        Elevator elevator = new Elevator();
        List<Floor> listFloor = new ArrayList<>();
        listFloor.add(Floor.of(1, FloorType.FIRST));
        FloorService serviceFloor = FloorService.of(listFloor);
        elevator.setFloorService(serviceFloor);
    }

    @Test
    void addPassengerInvalidNullPassenger() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, () -> elevator.addPassenger(null));
    }

    @Test
    void addPassengerInvalidNullFloor() {
        Elevator elevator = new Elevator();


        assertThrows(NullPointerException.class, () ->
                elevator.addPassenger(Passenger.of(47, 1)));
    }

    @Test
    void addPersonWell() {
        Elevator elevator = new Elevator();
        elevator.setFloor(Floor.of(1, FloorType.FIRST));
        elevator.addPassenger(Passenger.of(47, 5));

        assertThat(elevator.getPassengersList().size(), is(1));
    }

    @Test
    void setFloorInvalid() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, () -> elevator.setFloor(null));
    }

    @Test
    void setFloorWell() {
        Elevator elevator = new Elevator();
        Floor floor = Floor.of(1, FloorType.FIRST);
        elevator.setFloor(floor);

        assertThat(elevator.getFloor(), is(floor));
    }

}