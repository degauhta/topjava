package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {

    List<MealWithExceed> getAll();

    Meal save(int id, LocalDateTime dateTime, String description, int calories);

    Meal get(int id);

    boolean delete(int id);
}