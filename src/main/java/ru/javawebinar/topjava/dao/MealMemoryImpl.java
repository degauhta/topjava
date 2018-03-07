package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MealMemoryImpl implements DaoMeal {

    private ConcurrentMap<Integer, Meal> mealsMap;

    public MealMemoryImpl() {
        this.mealsMap = MealsUtil.TEST_MEALS.stream().collect(Collectors.toConcurrentMap(Meal::getId,
                Function.identity()));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(this.mealsMap.values());
    }

    @Override
    public Meal save(Meal meal) {
        this.mealsMap.merge(meal.getId(), meal, (a, b) -> b);
        return null;
    }

    @Override
    public Meal get(int id) {
        return this.mealsMap.get(id);
    }

    @Override
    public boolean delete(int id) {
        return this.mealsMap.remove(id) != null;
    }
}