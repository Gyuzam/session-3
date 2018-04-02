package ru.sbt.jschool.session3.problem2;

import java.util.HashMap;

public class Parking {
    private HashMap<Long, Long> carsIn = new HashMap<>();
    private Long capacity;
    private float hourCost;
    
    public HashMap<Long, Long> getCarsIn() {
        return carsIn;
    }

    public Parking(Long capacity, float hCost) {
        this.capacity = capacity;
        this.hourCost = hCost;
    }

    public boolean moveCarIn(Long carId, Long hourStart) {
        if (carsIn.size() + 1 > capacity) {
            return false;
        }
        if (carsIn.containsKey(carId)) {
            return false;
        }
        carsIn.put(carId, hourStart);
        return true;
    }

    public float moveCarOut(Long carId, Long hourEnd) {
        if (carsIn.containsKey(carId)) {
            Long hours = 0L;
            Long hourStart = carsIn.get(carId);
            for (Long i = hourStart; i < hourEnd; i++) {
                hours++;
                Long k = i % 24;
                if (k < 6 || k == 23) {
                    hours++;
                }
            }
            float cost = hourCost * hours;
            carsIn.remove(carId);
            return cost;
        }
        return 0f;
    }
}
