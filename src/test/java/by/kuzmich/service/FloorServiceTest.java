package by.kuzmich.service;

import by.kuzmich.models.DirectionType;
import by.kuzmich.models.Floor;
import by.kuzmich.models.FloorType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FloorServiceTest {

    private FloorService floorService;
    private List<Floor> floorList;

    void initialize() {
        floorList = new ArrayList<>();

        floorList.add(Floor.of(1, FloorType.FIRST));
        floorList.add(Floor.of(2, FloorType.INTERMEDIATE));
        floorList.add(Floor.of(3, FloorType.LAST));
        floorService = FloorService.of(floorList);
    }

    @Test
    void wellCreated() {
        initialize();

        assertEquals(floorService.getFloorList(), floorList);
    }

    @Test
    void invalidNullFloorList() {
        assertThrows(NullPointerException.class, () -> FloorService.of(null));
    }

    @Test
    void getFloorWell() {
        initialize();
        Floor floor = floorService.getFloor(1);

        assertEquals(floor, floorList.get(0));
    }

    @Test
    void invalidNumberFloor() {
        initialize();

        assertThrows(IllegalArgumentException.class, () -> floorService.getFloor(0));
    }

    @Test
    void nextFloorUp() {
        initialize();
        Floor floor = floorService.nextFloor(Floor.of(1, FloorType.FIRST), DirectionType.UP);

        assertEquals(floorService.getFloor(2), floor);
    }

    @Test
    void nextFloorDown() {
        initialize();
        Floor floor = floorService.nextFloor(Floor.of(2, FloorType.LAST), DirectionType.DOWN);

        assertEquals(floorService.getFloor(1), floor);
    }

    @Test
    void nextFloorInvalidNullFloor() {
        initialize();

        assertThrows(NullPointerException.class, () ->
                floorService.nextFloor(Floor.of(1, FloorType.FIRST), null));
    }

    @Test
    void nextFloorInvalidNullDirection() {
        initialize();

        assertThrows(NullPointerException.class, () ->
                floorService.nextFloor(null, DirectionType.UP));
    }

    @Test
    void moveToNeededFloorWell() {
        initialize();
        Floor floor = floorService.moveToNeededFloor(1, 3, DirectionType.DOWN);

        assertEquals(floor, floorService.getFloor(3));

        floor = floorService.moveToNeededFloor(1, 3, DirectionType.UP);

        assertEquals(floor, floorService.getFloor(1));
    }

    @Test
    void moveToNeededFloorInvalidFloorNumber() {
        initialize();

        assertThrows(IllegalArgumentException.class, () ->
                floorService.moveToNeededFloor(0, 2, DirectionType.DOWN));

        assertThrows(IllegalArgumentException.class, () ->
                floorService.moveToNeededFloor(1, 0, DirectionType.DOWN));
    }

    @Test
    void moveToNeededFloorInvalidNullDirection() {
        initialize();

        assertThrows(NullPointerException.class, () ->
                floorService.moveToNeededFloor(1, 2, null));
    }

    @Test
    void getNumberOfFloorsWell() {
        initialize();

        assertThat(floorService.getNumberOfFloors(), is(3));
    }

}