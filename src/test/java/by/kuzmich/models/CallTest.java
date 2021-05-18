package by.kuzmich.models;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CallTest {

    @Test
    void wellCreated() {
        Call call = Call.of(5, DirectionType.UP);

        assertThat(call.getNumber(), is(5));
        assertThat(call.getDirection(), is(DirectionType.UP));
    }

    @Test
    void invalidDirection() {
        assertThrows(NullPointerException.class, () -> Call.of(5, null));

    }

    @Test
    void invalidFloorNumber() {
        assertThrows(IllegalArgumentException.class, () -> Call.of(0, DirectionType.UP));
    }

}