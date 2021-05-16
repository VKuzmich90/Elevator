package by.kuzmich.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
public class Call {
    private final int number;
    private final DirectionType direction;

    private Call(int number, DirectionType direction) {
        log.debug("The call begins to be created");

        this.number = number;
        this.direction = direction;

        log.debug("The call was created");
    }

    public static Call of(int number, DirectionType direction) {
        checkArgument(number > 0, "Number must be positive!");
        checkNotNull(direction, "Direction must exists");

        return new Call(number, direction);
    }
}
