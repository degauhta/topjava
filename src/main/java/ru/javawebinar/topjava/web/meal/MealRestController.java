package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        return service.getAll(AuthorizedUser.id());
    }

    public Meal save(Meal meal) {
        return service.save(meal);
    }

    public Meal get(int id, int userId) {
        return service.get(id, userId);
    }

    public void delete(int id, int userId) {
        service.delete(id, userId);
    }
}