package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController {
    @Autowired
    private MealRestController controller;

    @GetMapping("/meals")
    public String meals(Model model) {
        model.addAttribute("meals", controller.getAll());
        return "meals";
    }

    @PostMapping("/meals")
    public String filter(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", controller.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/mealcreate")
    public String create(Model model) {
        model.addAttribute("meal",
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @PostMapping("/mealcreate")
    public String createMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (request.getParameter("id").isEmpty()) {
            controller.create(meal);
        } else {
            String paramId = Objects.requireNonNull(request.getParameter("id"));
            controller.update(meal, Integer.parseInt(paramId));
        }
        return "redirect:/meals";
    }

    @GetMapping("/mealupdate{id}")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("meal", controller.get(id));
        return "mealForm";
    }

    @GetMapping("/mealdelete{id}")
    public String delete(@PathVariable("id") int id) {
        controller.delete(id);
        return "redirect:/meals";
    }
}
