package com.mejsla.drools;

import java.util.List;
import java.util.Map;

public class Seating {

    private final List<Person> unseatedPersons;
    private final List<Person> seats;
    private final Person hostess;
    private final Map<Person, Person> enemyPairs;

    Seating(List<Person> unseatedPersons, List<Person> seats, Person hostess, Map<Person, Person> enemyPairs) {
        this.unseatedPersons = unseatedPersons;
        this.seats = seats;
        this.hostess = hostess;
        this.enemyPairs = enemyPairs;
    }

    public List<Person> getUnseatedPersons() {
        return unseatedPersons;
    }

    public List<Person> getSeats() {
        return seats;
    }

    public Person getHostess() {
        return hostess;
    }

    public Map<Person, Person> getEnemyPairs() {
        return enemyPairs;
    }

    public static boolean isSeatingTraditional(List<Person> seats) {
        return indexOfMisSeatedPerson(seats) == -1;
    }

    public static int indexOfMisSeatedPerson(List<Person> seats) {
        for (int index = 0; index < seats.size(); index += 2) {
            Person male = seats.get(index);
            Person female = seats.get(index + 1);
            if (male != null && male.isFemale()) {
                return index;
            }
            if (female != null && female.isMale()) {
                return index + 1;
            }
        }
        return -1;
    }

    public static boolean isAnyoneSeatedNextToEnemy(List<Person> seats, Map<Person, Person> enemyPairs) {
        return indexOfPersonSeatedNextToHisEnemy(seats, enemyPairs) != -1;
    }

    public static int indexOfPersonSeatedNextToHisEnemy(List<Person> seats, Map<Person, Person> enemyPairs) {
        for (Map.Entry<Person, Person> enemyPair: enemyPairs.entrySet()) {
            int keyPosition = seats.indexOf(enemyPair.getKey());
            int valuePosition = seats.indexOf(enemyPair.getValue());
            if(keyPosition == valuePosition + 1 || keyPosition == valuePosition - 1) {
                return keyPosition;
            }
        }

        return -1;
    }

    public static boolean isHostessNextToDoor(Person hostess, List<Person> seats) {
        int indexOfHostess = seats.indexOf(hostess);
        return indexOfHostess == -1 || indexOfHostess == seats.size() - 1;
    }
}
