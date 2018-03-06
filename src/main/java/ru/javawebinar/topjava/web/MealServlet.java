package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setAttribute("meals", MealsUtil.getFilteredWithExceeded(MealsUtil.TEST_MEALS,
//                LocalTime.of(10, 0), LocalTime.of(12, 0), 2000));
        MealService mealServiceImpl = new MealServiceImpl();
        request.setAttribute("meals", mealServiceImpl.getAll());
        log.debug("forward to meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealService mealServiceImpl = new MealServiceImpl();
        if (request.getParameter("action").equals("add") || request.getParameter("action").equals("update")) {
            String id = request.getParameter("id");
            mealServiceImpl.save(id.isEmpty() ? -1 : Integer.parseInt(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            log.debug("forward to meals.jsp");
            response.sendRedirect("meals");
        } else if (request.getParameter("action").equals("edit")) {
            Meal meal = mealServiceImpl.get(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("meals", mealServiceImpl.getAll());
            request.setAttribute("editMeal", meal);
            log.debug("forward to meals.jsp");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (request.getParameter("action").equals("delete")) {
            if (mealServiceImpl.delete(Integer.parseInt(request.getParameter("id")))) {
                log.debug("forward to meals.jsp");
                response.sendRedirect("meals");
            }
        }
    }
}