# stepup-drools-2019

Code repository for rule engines StepUp

## Instructions

1. git clone https://github.com/mejsla/stepup-drools-2019.git
2. mvn clean test

Use project JDK 1.8 in your IDE.

## Lab

The project contains rules aimed at creating a seating plan for a dinner party. There is currently only two rules in *seating.drl*.
One rule which will seat all persons randomly, the other makes sure the hostess is seated next to the door. 
There are tests in *SeatingTest.java* that makes sure the end result is as expected.

## Exercise 1 
 
Make a rule that mixes the seating of genders, so that no men sit next to each other and vice versa for women. 
There is a test case already written, *shouldSeatMixingGenders*, and some helper methods.

## Exercise 2

Some people really dislike each other, so they must not be seated next to each other. Add this to the facts and create
a rule that makes sure they aren't. When writing the test case, remember that same gender individuals are already seated
apart, so the enemies should be from different genders. 

## Tips on drools syntax

A rule can insert/delete/update/modify facts. This will trigger a drools revaluation of that fact.
When changing an existing fact by, e.g., calling a set method, drools must be told of the change by calling the update or modify method.

```
ìnsert(new ShouldEmailAction()); <- inserting a new fact
delete($defaultFact); <- deleting a fact
update($seating); <- tell drools that seating has been updated
modify($person) { setName("Mario Fusco") } <- updates fact and lets drools know at the same time
```

Facts are assumed to follow the java bean standard, meaning that the there must be getters for any fields that should be accessed.

```
$seating : Seating($hostess: hostess, $seats: seats, !isHostessNextToDoor(hostess, seats))
```

This line will do a number of things:

```
$hostess: hostess
```

This will call getHostess() from Seating and assign its value to $hostess.

```
!isHostessNextToDoor(hostess, seats)
```

This will call the method *isHostessNextToDoor* from Seating with values getHostess() and getSeats().