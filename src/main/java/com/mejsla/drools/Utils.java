/*
 * Copyright (C) 2019 Johan Dykstrom
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mejsla.drools;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Utils {

    private Utils() { }

    public static List<Person> createPersons(int size) {
        if (size % 2 != 0) {
            throw new IllegalArgumentException("number of persons must be even");
        }
        return IntStream.range(0, size).mapToObj(i -> new Person()).collect(Collectors.toList());
    }

    public static Person findHostess(List<Person> unseatedPersons) {
        // The hostess is the first women in the list of persons to seat
        return unseatedPersons.stream().filter(Person::isFemale).findFirst().orElseThrow(IllegalStateException::new);
    }

    public static Person findSixten(List<Person> unseatedPersons) {
        // Sixten is the first man in the list of persons to seat
        return unseatedPersons.stream().filter(Person::isMale).findFirst().orElseThrow(IllegalStateException::new);
    }
}
