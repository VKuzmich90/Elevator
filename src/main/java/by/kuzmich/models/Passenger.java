package by.kuzmich.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@Slf4j
@ToString
@EqualsAndHashCode
public class Passenger {

    public static final int MAX_WEIGHT = 130;

    private final int weight;
    private final int floorNumber;

    private Passenger(int weight, int floorNumber) {
        log.debug("The passenger begins to be created");

        this.weight = weight;
        this.floorNumber = floorNumber;

        log.debug("The passenger was created");
    }

    public static Passenger of(int weight, int floorNumber){
        checkArgument(weight > 0 && weight <= MAX_WEIGHT, "Incorrect weight!");
        checkArgument(floorNumber > 0, "floor number must exists");

        return new Passenger(weight, floorNumber);
    }

}