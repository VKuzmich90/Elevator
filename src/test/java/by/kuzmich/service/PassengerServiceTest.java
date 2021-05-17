package by.kuzmich.service;

import by.kuzmich.models.Passenger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerServiceTest {

    @Test
    void spawnPassengerWell() {
        PassengerService passengerService = new PassengerService();

        Passenger passenger = passengerService.spawnPassenger(8);

        assertNotNull(passenger);
    }

    @Test
    void invalidNumberFloors() {
        PassengerService passengerService = new PassengerService();

        assertThrows(IllegalArgumentException.class, () ->
                passengerService.spawnPassenger(0));

    }
}
