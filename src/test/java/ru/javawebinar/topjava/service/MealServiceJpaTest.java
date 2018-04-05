package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.JPA;

/**
 * MealServiceJpaTest class.
 *
 * @author Denis
 * @since 04.04.2018
 */
@ActiveProfiles(JPA)
public class MealServiceJpaTest extends AbstractMealService {
}
