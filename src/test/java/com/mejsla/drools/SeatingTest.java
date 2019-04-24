package com.mejsla.drools;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static com.mejsla.drools.Seating.isHostessNextToDoor;
import static com.mejsla.drools.Seating.isSeatingTraditional;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

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
        kieSession.setGlobal("globalRandom", new Random());
    }

    @Test
    public void shouldSeatAll() {
        List<Person> unseatedPersons = Utils.createPersons(10);
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
        List<Person> unseatedPersons = Utils.createPersons(50);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);
        Person hostess = Utils.findHostess(unseatedPersons);

        Seating seating = new Seating(unseatedPersons, seats, hostess);

        kieSession.insert(seating);
        kieSession.fireAllRules();

        // Verify
        assertTrue(unseatedPersons.isEmpty()); // No unseated persons
        assertFalse(seats.contains(null)); // No empty seats
        assertEquals(seats.size(), new HashSet<>(seats).size()); // All seated persons are unique
        assertTrue(isHostessNextToDoor(hostess, seats)); // Hostess next to door
    }

    @Test
    public void shouldSeatMixingGenders() {
        List<Person> unseatedPersons = Utils.createPersons(50);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);
        Person hostess = Utils.findHostess(unseatedPersons);

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

    @Test
    public void shouldSeatSixten() {
        List<Person> unseatedPersons = Utils.createPersons(10);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);
        Person hostess = Utils.findHostess(unseatedPersons);
        Person sixten = Utils.findSixten(unseatedPersons);

        Seating seating = new Seating(unseatedPersons, seats, hostess);
        SixtenFact sixtenFact = new SixtenFact(sixten);

        kieSession.insert(seating);
        kieSession.insert(sixtenFact);
        kieSession.fireAllRules();

        // Verify
        assertTrue(unseatedPersons.isEmpty()); // No unseated persons
        assertFalse(seats.contains(null)); // No empty seats
        assertEquals(seats.size(), new HashSet<>(seats).size()); // All seated persons are unique
        assertTrue(isHostessNextToDoor(hostess, seats)); // Hostess next to door
        assertTrue(sixtenFact.isSixtenNextToRestroom(seats)); // Sixten next to other door (close to restroom)
        assertTrue(isSeatingTraditional(seats)); // Every second man and woman
    }
}
