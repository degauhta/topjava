package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL1_ID = START_SEQ + 2;
    public static final int USER_MEAL2_ID = START_SEQ + 3;
    public static final int USER_MEAL3_ID = START_SEQ + 4;
    public static final int ADMIN_MEAL1_ID = START_SEQ + 5;
    public static final int ADMIN_MEAL2_ID = START_SEQ + 6;
    public static final int ADMIN_MEAL3_ID = START_SEQ + 7;

    public static final Meal USER_MEAL_1 = new Meal(USER_MEAL1_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(USER_MEAL2_ID, LocalDateTime.of(2015, Month.MAY, 30, 14, 0), "Обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(USER_MEAL3_ID, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal ADMIN_MEAL_1 = new Meal(ADMIN_MEAL1_ID, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal ADMIN_MEAL_2 = new Meal(ADMIN_MEAL2_ID, LocalDateTime.of(2015, Month.MAY, 31, 14, 0), "Обед", 500);
    public static final Meal ADMIN_MEAL_3 = new Meal(ADMIN_MEAL3_ID, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}