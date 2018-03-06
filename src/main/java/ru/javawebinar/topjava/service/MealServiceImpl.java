package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.DaoMeal;
import ru.javawebinar.topjava.dao.MealHsqldbImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MealServiceImpl implements MealService {
    @Override
    public List<MealWithExceed> getAll() {
        DaoMeal meal = new MealHsqldbImpl();
        return MealsUtil.getFilteredWithExceeded(meal.getAll(),
                LocalTime.MIN, LocalTime.MAX, 2000);
    }

    @Override
    public Meal save(int id, LocalDateTime dateTime, String description, int calories) {
        DaoMeal meal = new MealHsqldbImpl();
        return meal.save(new Meal(id, dateTime, description, calories));
    }

    @Override
    public Meal get(int id) {
        DaoMeal meal = new MealHsqldbImpl();
        return meal.get(id);
    }

    @Override
    public boolean delete(int id) {
        DaoMeal meal = new MealHsqldbImpl();
        return meal.delete(id);
    }
}