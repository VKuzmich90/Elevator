package by.kuzmich.service;

import by.kuzmich.models.Passenger;
import com.google.common.base.Preconditions;

import java.util.Random;

public class PassengerService {

    private final Random random = new Random();

       public Passenger spawnPassenger (int numberOfFloors){
        Preconditions.checkArgument(numberOfFloors > 0, "Floors number must be positive");

        int floorNumber = random.nextInt(numberOfFloors) + 1;
        int weight = random.nextInt(Passenger.MAX_WEIGHT) + 20;

        return Passenger.of(weight, floorNumber);
    }
}
