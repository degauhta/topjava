package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.getAll().forEach(System.out::println);

            Meal meal = mealRestController.save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 5, 0),
                    "save", 501, AuthorizedUser.id()));
            mealRestController.getAll().forEach(System.out::println);

            System.out.println(mealRestController.get(meal.getId(), AuthorizedUser.id()));

            try {
                mealRestController.get(meal.getId(), 123);
            } catch (NotFoundException ex) {
                System.out.println("NotFoundException");
            }

            System.out.println(mealRestController.save(new Meal(meal.getId(),
                    LocalDateTime.of(2015, Month.MAY, 31, 5, 0),
                    "update", 501, AuthorizedUser.id())));

            try {
                mealRestController.save(new Meal(meal.getId(),
                        LocalDateTime.of(2015, Month.MAY, 31, 5, 0),
                        "update with wrong userID", 501, 123));
            } catch (NotFoundException ex) {
                System.out.println("NotFoundException");
            }

            try {
                mealRestController.save(new Meal(123,
                        LocalDateTime.of(2015, Month.MAY, 31, 5, 0),
                        "update with wrong id", 501, AuthorizedUser.id()));
            } catch (NotFoundException ex) {
                System.out.println("NotFoundException");
            }

            try {
                mealRestController.delete(meal.getId(), 123);
            } catch (NotFoundException ex) {
                System.out.println("NotFoundException");
            }

            mealRestController.delete(meal.getId(), AuthorizedUser.id());
            mealRestController.getAll().forEach(System.out::println);
        }
    }
}
