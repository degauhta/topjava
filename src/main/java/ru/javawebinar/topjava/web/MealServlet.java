package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.DaoMeal;
import ru.javawebinar.topjava.dao.MealMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger log = getLogger(MealServlet.class);

    private DaoMeal daoMeal = new MealMemoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meals", MealsUtil.getFilteredWithExceeded(daoMeal.getAll(),
                LocalTime.MIN, LocalTime.MAX, 2000));
        log.debug("forward to meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("action").equals("add") || request.getParameter("action").equals("update")) {
            String id = request.getParameter("id");
            daoMeal.save(new Meal(id.isEmpty() ? null : Integer.parseInt(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories"))));
            log.debug("forward to meals.jsp");
            response.sendRedirect("meals");
        } else if (request.getParameter("action").equals("edit")) {
            Meal meal = daoMeal.get(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("meals", MealsUtil.getFilteredWithExceeded(daoMeal.getAll(),
                    LocalTime.MIN, LocalTime.MAX, 2000));
            request.setAttribute("editMeal", meal);
            log.debug("forward to meals.jsp");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (request.getParameter("action").equals("delete")) {
            if (daoMeal.delete(Integer.parseInt(request.getParameter("id")))) {
                log.debug("forward to meals.jsp");
                response.sendRedirect("meals");
            }
        }
    }
}