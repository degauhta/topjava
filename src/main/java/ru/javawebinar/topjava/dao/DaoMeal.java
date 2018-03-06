package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface DaoMeal {
    List<Meal> getAll();

    Meal save(Meal meal);

    Meal get(int id);

    boolean delete(int id);
}