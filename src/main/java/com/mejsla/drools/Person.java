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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Person {

    private static final AtomicInteger IDS = new AtomicInteger(0);

    private final String name;
    private final Gender gender;

    Person() {
        int id = IDS.getAndIncrement();
        name = Integer.toString(id);
        gender = (id % 2 == 0) ? Gender.M : Gender.F;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isMale() { return gender == Gender.M; }

    public boolean isFemale() { return gender == Gender.F; }

    @Override
    public String toString() {
        return "[" + name + ":" + gender + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person that = (Person) o;
        return Objects.equals(this.name, that.name) && this.gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender);
    }

    public enum Gender {
        M,
        F;

        public Gender other() {
            return (this == M) ? F : M;
        }
    }
}
