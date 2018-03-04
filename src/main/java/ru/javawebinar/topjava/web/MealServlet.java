package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meals", MealsUtil.getFilteredWithExceeded(MealsUtil.TEST_MEALS,
                LocalTime.of(10, 0), LocalTime.of(12, 0), 2000));
        log.debug("forward to meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}