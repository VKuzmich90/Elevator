package by.kuzmich.models;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PassengerTest {


    @Test
    void wellCreated() {
        Passenger person = Passenger.of(50, 3);

        assertThat(person.getWeight(), is(50));
        assertThat(person.getFloorNumber(), is(3));
    }

    @Test
    void invalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> Passenger.of(0, 3));
    }

    @Test
    void invalidFloorNumber() {
        assertThrows(IllegalArgumentException.class, () -> Passenger.of(50, 0));
    }


}