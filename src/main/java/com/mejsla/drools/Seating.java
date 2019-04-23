package com.mejsla.drools;

import java.util.List;

public class Seating {

    private final List<Person> unseatedPersons;
    private final List<Person> seats;
    private final Person hostess;

    Seating(List<Person> unseatedPersons, List<Person> seats, Person hostess) {
        this.unseatedPersons = unseatedPersons;
        this.seats = seats;
        this.hostess = hostess;
    }

    @Override
    public String toString() {
        return "[Unseated: " + unseatedPersons.size() + ", seated: " + seats.size() + ", hostess: " + hostess + "]";
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

    public static boolean isSeatingTraditional(List<Person> seats) {
        return indexOfMisSeatedPerson(seats) == -1;
    }

    private static int indexOfMisSeatedPerson(List<Person> seats) {
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

    public static boolean isHostessNextToDoor(Person hostess, List<Person> seats) {
        int indexOfHostess = seats.indexOf(hostess);
        return indexOfHostess == -1 || indexOfHostess == seats.size() - 1;
    }
}
