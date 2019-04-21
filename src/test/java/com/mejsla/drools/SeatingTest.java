package com.mejsla.drools;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mejsla.drools.Seating.isHostessNextToDoor;
import static com.mejsla.drools.Seating.isSeatingTraditional;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeatingTest {

    private static final String K_SESSION_NAME = "SeatingKS";
    private KieSession kieSession;

    @Before
    public void before() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kieSession = kContainer.newKieSession(K_SESSION_NAME);

        Logger ruleLogger = LoggerFactory.getLogger(K_SESSION_NAME);
        kieSession.setGlobal("log", ruleLogger);
    }

    @Test
    public void shouldSeatAll() {
        List<Person> unseatedPersons = createPersons(10);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);

        Seating seating = new Seating(unseatedPersons, seats, null);

        kieSession.insert(seating);
        kieSession.fireAllRules();

        assertTrue(unseatedPersons.isEmpty()); // No unseated persons
        assertFalse(seats.contains(null)); // No empty seats
        assertEquals(seats.size(), new HashSet<>(seats).size()); // All seated persons are unique
    }

    @Test
    public void shouldSeatHostessNextToDoor() {
        List<Person> unseatedPersons = createPersons(50);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);
        Person hostess = findHostess(unseatedPersons);

        Seating seating = new Seating(unseatedPersons, seats, hostess);

        kieSession.insert(seating);
        kieSession.fireAllRules();

        // Verify
        assertTrue(unseatedPersons.isEmpty()); // No unseated persons
        assertFalse(seats.contains(null)); // No empty seats
        assertEquals(seats.size(), new HashSet<>(seats).size()); // All seated persons are unique
        assertTrue(isHostessNextToDoor(hostess, seats)); // Hostess next to door
    }

    @Ignore
    @Test
    public void shouldSeatMixingGenders() {
        List<Person> unseatedPersons = createPersons(50);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);
        Person hostess = findHostess(unseatedPersons);

        Seating seating = new Seating(unseatedPersons, seats, hostess);

        kieSession.insert(seating);
        kieSession.fireAllRules();

        // Verify
        assertTrue(unseatedPersons.isEmpty()); // No unseated persons
        assertFalse(seats.contains(null)); // No empty seats
        assertEquals(seats.size(), new HashSet<>(seats).size()); // All seated persons are unique
        assertTrue(isHostessNextToDoor(hostess, seats)); // Hostess next to door
        assertTrue(isSeatingTraditional(seats)); // Every second man and woman
    }

    private Person findHostess(List<Person> unseatedPersons) {
        // The hostess is the first women in the list of persons to seat
        return unseatedPersons.stream().filter(Person::isFemale).findFirst().orElseThrow(IllegalStateException::new);
    }

    private static List<Person> createPersons(int size) {
        if (size % 2 != 0) {
            throw new IllegalArgumentException("number of persons must be even");
        }
        return IntStream.range(0, size).mapToObj(i -> new Person()).collect(Collectors.toList());
    }
}
