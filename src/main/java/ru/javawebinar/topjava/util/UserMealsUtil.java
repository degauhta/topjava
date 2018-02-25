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
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2005)
                .forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        mealList.sort(Comparator.comparing(UserMeal::getDateTime));
        List<UserMealWithExceed> result = new ArrayList<>();
        List<UserMealWithExceed> tmpResultExceed = new ArrayList<>();
        List<UserMealWithExceed> tmpResultNoExceed = new ArrayList<>();
        Map<LocalDate, Integer> dayCalories = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            LocalTime mealTime = userMeal.getDateTime().toLocalTime();
            dayCalories.merge(mealDate, userMeal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
            if (TimeUtil.isBetween(mealTime, startTime, endTime)) {
                boolean newDay = !dayCalories.keySet().iterator().next().equals(mealDate);
                if (!dayCalories.isEmpty() && newDay) {
                    result.addAll(getResultsPerDay(caloriesPerDay, dayCalories, tmpResultExceed, tmpResultNoExceed));
                }
                tmpResultExceed.add(new UserMealWithExceed(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(), true));
                tmpResultNoExceed.add(new UserMealWithExceed(LocalDateTime.of(mealDate, mealTime),
                        userMeal.getDescription(), userMeal.getCalories(), false));
            }
        }
        result.addAll(getResultsPerDay(caloriesPerDay, dayCalories, tmpResultExceed, tmpResultNoExceed));
        return result;
    }

    private static List<UserMealWithExceed> getResultsPerDay(int caloriesPerDay, Map<LocalDate, Integer> dayCalories,
                                                             List<UserMealWithExceed> tmpResultExceed, List<UserMealWithExceed> tmpResultNoExceed) {
        List<UserMealWithExceed> result = new ArrayList<>();
        Map.Entry<LocalDate, Integer> entry = dayCalories.entrySet().iterator().next();
        LocalDate key = entry.getKey();
        int value = entry.getValue();
        if (value >= caloriesPerDay) {
            result.addAll(tmpResultExceed);
        } else {
            result.addAll(tmpResultNoExceed);
        }
        tmpResultExceed.clear();
        tmpResultNoExceed.clear();
        dayCalories.remove(key);
        return result;
    }
}
