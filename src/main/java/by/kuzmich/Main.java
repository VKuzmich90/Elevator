package by.kuzmich;

import by.kuzmich.models.Building;

public class Main {
    public static void main(String[] args) {

        Building building = Building.of(20, 5);
        building.start();
    }
}
