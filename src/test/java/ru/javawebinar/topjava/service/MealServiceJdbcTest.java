package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.JDBC;

/**
 * MealServiceJdbcTest class.
 *
 * @author Denis
 * @since 04.04.2018
 */
@ActiveProfiles(JDBC)
public class MealServiceJdbcTest extends AbstractMealService {
}
