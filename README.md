# stepup-drools-2019

Code repository for rule engines StepUp

## Instructions

1. git clone https://github.com/mejsla/stepup-drools-2019.git
2. mvn clean test

## Lab

The project contains rules aimed at creating a seating plan for a dinner party. There are currently only two rules in *seating.drl*.
One rule which will seat all persons randomly, the other makes sure the hostess is seated next to the door. 
There are tests in *SeatingTest.java* that makes sure the end result is as expected.

## Exercise 1 
 
Make a rule that mixes the seating of genders, so that no men sit next to each other and vice versa for women. 
There is a test case already written, *shouldSeatMixingGenders*, and some helper methods.

## Exercise 2

Some people really dislike each other, so they must not be seated next to each other. Add this to the facts and create
a rule that makes sure they are not. When writing the test case, remember that same gender individuals are already seated
apart, so the enemies should be of different genders. 

## Exercise 3

Make up your own seating rule. Maybe uncle Sixten needs to visit the restroom often, so he needs to sit at the end of the table?

## Tips on Drools syntax

A rule can insert/delete/update/modify facts. This will trigger a drools revaluation of that fact.
When changing an existing fact by, e.g., calling a set method, drools must be told of the change by calling the update or modify method.

```
ìnsert(new ShouldEmailAction()); <- inserting a new fact
delete($defaultFact); <- deleting a fact
update($seating); <- tell drools that seating has been updated
modify($person) { setName("Mario Fusco") } <- updates fact and lets drools know at the same time
```

Facts are assumed to follow the java bean standard, meaning that the there must be getters for any fields that should be accessed.
This is an example of a *when* part of a rule:

```
$seating : Seating($hostess: hostess, $seats: seats, !isHostessNextToDoor(hostess, seats))
```

The line above will do a number of things. This part:

```
$hostess: hostess
```

will call getHostess() from Seating and assign its value to $hostess.

```
$seats: seats
```

will call getSeats() from Seating and assign its value to $seats. And this part:

```
!isHostessNextToDoor(hostess, seats)
```

will call the method isHostessNextToDoor() from Seating with values getHostess() and getSeats().
Finally, the part:

```
$seating: Seating(...)
```

will bind the entire Seating instance to $seating.

The rule will match only if isHostessNextToDoor() returns false.

The variables that have been bound in the *when* part ($hostess, $seats, $seating) can also be used in the *then* part.
For example, you can find the index of the hostess place like this using both $seats and $hostess:

```
int indexOfHostess = $seats.indexOf($hostess);
```

More examples of Drools syntax can be found at https://examples.javacodegeeks.com/enterprise-java/jboss-drools-tutorial-beginners
and https://nheron.gitbooks.io/droolsonboarding/content.
