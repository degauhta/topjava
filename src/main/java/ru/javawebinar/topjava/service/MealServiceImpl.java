package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {
    private static final Logger log = getLogger(MealServiceImpl.class);

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(Meal meal) throws NotFoundException {
        log.info("save {}", meal);
        return checkNotFoundWithId(repository.save(meal), meal.getId(), meal.getUserId());
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        log.info("delete meal={} of user={}", id, userId);
        checkNotFound(repository.delete(id, userId),
                String.format("Not found meal with id=%s, userId=%s", id, userId));
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        log.info("get meal={} of user={}", id, userId);
        return checkNotFoundWithId(repository.get(id, userId), id, userId);
    }

    @Override
    public List<MealWithExceed> getAll(int userId) {
        log.info("getAll of user={}", userId);
        return MealsUtil.getWithExceeded(repository.getAll(AuthorizedUser.id()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }
}