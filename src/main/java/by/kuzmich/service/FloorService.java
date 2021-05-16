package by.kuzmich.service;

import by.kuzmich.models.DirectionType;
import by.kuzmich.models.Elevator;
import by.kuzmich.models.Floor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Getter
public class FloorService {
    private List<Floor> floorList;

    private FloorService(List<Floor> floorList) {
        log.debug("The floor service begins to be created");

        this.floorList = floorList;

        log.debug("The floor service was created");
    }

    public static FloorService of(List<Floor> floorList) {
        checkNotNull(floorList, "Floor list must exists!");

        return new FloorService(floorList);
    }

    public Floor getFloor(int numberFloor) {
        checkArgument(numberFloor > 0, "Number floor must be positive!");

        return floorList.get(numberFloor);
    }

//    public Floor nextFloor(Floor floor, DirectionType direction) {
//        checkNotNull(floor, "Floor is null!");
//        checkNotNull(direction, "Direction is null");
//
//        if (direction == DirectionType.UP) {
//            if (floor.getKindFloor() != KindFloor.LAST) {
//                int numberNextFloor = floor.getFloorNumber() + 1;
//                log.info("{} now on the {} floor", Thread.currentThread().getName(), floor.getFloorNumber());
//                floor = getFloor(numberNextFloor);
//                log.info("{} moved to the {} floor", Thread.currentThread().getName(), numberNextFloor);
//            }
//        } else {
//            if (floor.getKindFloor() != KindFloor.FIRST) {
//                int numberPreviewFloor = floor.getFloorNumber() - 1;
//                log.info("{} now in the {} floor", Thread.currentThread().getName(), floor.getFloorNumber());
//                floor = getFloor(numberPreviewFloor);
//                log.info("{} moved to the {} floor", Thread.currentThread().getName(), numberPreviewFloor);
//
//            }
//        }
//        return floor;
//    }
//
//    @SneakyThrows
//    public Floor moveToNeededFloor(int currentFloor, int neededFloor, DirectionType direction) {
//        checkNotNull(direction, "Direction is null");
//        checkArgument(currentFloor > 0 && neededFloor > 0, "Floor number is less than or equals 0!");
//
//        if ((currentFloor - neededFloor < 0 && direction == DirectionType.DOWN) ||
//                (neededFloor - currentFloor < 0 && direction == DirectionType.UP)) {
//
//            log.info("{} now on the {} floor", Thread.currentThread().getName(), currentFloor);
//
//            TimeUnit.SECONDS.sleep(Math.abs(currentFloor - neededFloor) * Elevator.LIFT_SPEED);
//
//            log.info("{} moved to {} floor", Thread.currentThread().getName(), neededFloor);
//
//            return getFloor(neededFloor);
        }
//
//        return getFloor(currentFloor);
//    }
//
//    public int getNumberOfFloors() {
//        return floorList.size();
//    }
//
//    public List<Floor> getFloorList() {
//        return List.copyOf(floorList);
//    }
//
//}
