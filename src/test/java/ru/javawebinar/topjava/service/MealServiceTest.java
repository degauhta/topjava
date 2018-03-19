package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2018, Month.MARCH, 18, 10, 0), "test meal", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), newMeal, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL_3, ADMIN_MEAL_2, ADMIN_MEAL_1);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createSameUserSameTime() {
        Meal meal = new Meal(LocalDateTime.of(2018, Month.MARCH, 18, 10, 0), "test meal", 500);
        service.create(meal, USER_ID);
        Meal mealDuplicate = new Meal(LocalDateTime.of(2018, Month.MARCH, 18, 10, 0), "test meal", 500);
        service.create(mealDuplicate, USER_ID);
        System.out.println();
    }

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL1_ID, USER_ID);
        assertMatch(meal, USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFoundMeal() {
        service.get(1, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL_3, USER_MEAL_2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundMeal() {
        service.delete(1, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        assertMatch(service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 31), USER_ID),
                USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
                LocalDateTime.of(2015, Month.MAY, 30, 12, 0), USER_ID), USER_MEAL_1);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("Updated Description");
        updated.setCalories(330);
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundMeal() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setId(1);
        service.update(updated, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundUser() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("Updated Description");
        service.update(updated, 1);
    }
}