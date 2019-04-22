package com.mejsla.drools;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mejsla.drools.Seating.isAnyoneSeatedNextToEnemy;
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

        Seating seating = new Seating(unseatedPersons, seats, null, Map.of());

        kieSession.insert(seating);
        kieSession.fireAllRules();

        assertTrue(unseatedPersons.isEmpty());
        assertFalse(seats.contains(null));
    }

    @Test
    public void shouldSeatMixingGenders() {
        List<Person> unseatedPersons = createPersons(50);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);
        // The hostess is the first women in the list of persons to seat
        Person hostess = unseatedPersons.stream().filter(Person::isFemale).findFirst().orElseThrow(IllegalStateException::new);

        Seating seating = new Seating(unseatedPersons, seats, hostess, Map.of());

        kieSession.insert(seating);
        kieSession.fireAllRules();

        // Verify
        assertTrue(unseatedPersons.isEmpty());
        assertFalse(seats.contains(null));
        assertTrue(isSeatingTraditional(seats));
        assertTrue(isHostessNextToDoor(hostess, seats));
        assertEquals(seats.size(), new HashSet<>(seats).size()); // Verify that all persons are unique
    }

    @Test
    public void shouldNotSeatEnemiesNextToEachOther() {
        List<Person> unseatedPersons = createPersons(50);
        List<Person> seats = asList(new Person[unseatedPersons.size()]);
        // The hostess is the first women in the list of persons to seat
        Person hostess = unseatedPersons.stream().filter(Person::isFemale).findFirst().orElseThrow(IllegalStateException::new);

        Map<Person, Person> enemyPairs = new HashMap<>();
        int numberOfEnemyPairs = 19;
        IntStream.range(0, numberOfEnemyPairs).forEach(i -> enemyPairs.put(unseatedPersons.get(i), unseatedPersons.get(i + numberOfEnemyPairs)));

        Seating seating = new Seating(unseatedPersons, seats, hostess, enemyPairs);

        kieSession.insert(seating);
        kieSession.fireAllRules();

        // Verify
        assertFalse(isAnyoneSeatedNextToEnemy(seats, enemyPairs));
    }


    private static List<Person> createPersons(int size) {
        if (size % 2 != 0) {
            throw new IllegalArgumentException("number of persons must be even");
        }
        return IntStream.range(0, size).mapToObj(i -> new Person()).collect(Collectors.toList());
    }
}
