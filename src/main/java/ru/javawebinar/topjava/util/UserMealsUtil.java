package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 2005),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 100),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 100),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 30, 11, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 31, 10, 0), "Завтрак", 100),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 31, 13, 0), "Обед", 2005),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 31, 20, 0), "Ужин", 100),
                new UserMeal(LocalDateTime.of(2017, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2017, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2017, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2017, Month.MAY, 31, 10, 0), "Завтрак", 100),
                new UserMeal(LocalDateTime.of(2017, Month.MAY, 31, 13, 0), "Обед", 100),
                new UserMeal(LocalDateTime.of(2017, Month.MAY, 31, 20, 0), "Ужин", 2005)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(17, 0), LocalTime.of(22, 0), 2005)
                .forEach(System.out::println);
        System.out.println("cycle");
        getFilteredWithExceededCycle(mealList, LocalTime.of(17, 0), LocalTime.of(22, 0), 2005)
                .forEach(System.out::println);
        System.out.println("stream");
        getFilteredWithExceededStream(mealList, LocalTime.of(17, 0), LocalTime.of(22, 0), 2005)
                .forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        mealList.sort(Comparator.comparing(UserMeal::getDateTime));
        List<UserMealWithExceed> result = new ArrayList<>();
        List<UserMealWithExceed> tmpResultExceed = new ArrayList<>();
        List<UserMealWithExceed> tmpResultNoExceed = new ArrayList<>();
        NavigableMap<LocalDate, Integer> dayCalories = new TreeMap<>();
        LocalDate day = mealList.get(0).getDate();
        for (UserMeal userMeal : mealList) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            dayCalories.merge(mealDate, userMeal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean newDay = day.compareTo(mealDate) != 0;
                day = mealDate;
                if (!dayCalories.isEmpty() && newDay) {
                    result.addAll(getResultsPerDay(caloriesPerDay, dayCalories, tmpResultExceed, tmpResultNoExceed));
                }
                tmpResultExceed.add(new UserMealWithExceed(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(), true));
                tmpResultNoExceed.add(new UserMealWithExceed(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(), false));
            }
        }
        result.addAll(getResultsPerDay(caloriesPerDay, dayCalories, tmpResultExceed, tmpResultNoExceed));
        return result;
    }

    private static List<UserMealWithExceed> getResultsPerDay(int caloriesPerDay, Map<LocalDate, Integer> dayCalories,
                                                             List<UserMealWithExceed> tmpResultExceed, List<UserMealWithExceed> tmpResultNoExceed) {
        List<UserMealWithExceed> result = new ArrayList<>();
        int mergedCalories = dayCalories.get(tmpResultExceed.get(0).getDateTime().toLocalDate());
        dayCalories.remove(tmpResultExceed.get(0).getDateTime().toLocalDate());
        if (mergedCalories >= caloriesPerDay) {
            result.addAll(tmpResultExceed);
        } else {
            result.addAll(tmpResultNoExceed);
        }
        tmpResultExceed.clear();
        tmpResultNoExceed.clear();
        return result;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate))
                .entrySet().stream()
                .map(entry -> {
                    int calories = entry.getValue().stream().mapToInt(UserMeal::getCalories).sum();
                    return entry.getValue().stream()
                            .filter(x -> TimeUtil.isBetween(x.getDateTime().toLocalTime(), startTime, endTime))
                            .map(s -> new UserMealWithExceed(s.getDateTime(), s.getDescription(), s.getCalories(), calories > caloriesPerDay))
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    public static List<UserMealWithExceed> getFilteredWithExceededCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        mealList.sort(Comparator.comparing(UserMeal::getDateTime));
        Map<LocalDate, Warehouse> dayCalories = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            dayCalories.putIfAbsent(mealDate, new Warehouse());
            Warehouse currentWarehouse = dayCalories.get(mealDate);
            currentWarehouse.addCalories(userMeal.getCalories(), caloriesPerDay);
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                UserMealWithExceed meal = new UserMealWithExceed(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(), currentWarehouse.isBoundMealsIsExceeded());
                currentWarehouse.addMeal(meal);
            }
        }
        List<Warehouse> list = new ArrayList<>(dayCalories.values());
        return list.stream()
                .flatMap(s -> s.getBoundMeals().stream())
                .collect(Collectors.toList());
    }

    private static class Warehouse {
        private List<UserMealWithExceed> boundMeals;
        private int sumOfCaloriesPerDay;
        private boolean boundMealsIsExceeded;

        Warehouse() {
            this.boundMeals = new ArrayList<>();
        }

        boolean isBoundMealsIsExceeded() {
            return this.boundMealsIsExceeded;
        }

        void addCalories(int calories, int caloriesPerDay) {
            this.sumOfCaloriesPerDay += calories;
            if (!this.boundMealsIsExceeded && this.sumOfCaloriesPerDay >= caloriesPerDay) {
                this.boundMeals.forEach(s -> s.setExceed(true));
                this.boundMealsIsExceeded = true;
            }
        }

        void addMeal(UserMealWithExceed meal) {
            this.boundMeals.add(meal);
        }

        List<UserMealWithExceed> getBoundMeals() {
            return this.boundMeals;
        }
    }
}
