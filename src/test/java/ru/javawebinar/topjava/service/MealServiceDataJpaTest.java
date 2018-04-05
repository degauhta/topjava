package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

/**
 * MealServiceDataJpaTest class.
 *
 * @author Denis
 * @since 04.04.2018
 */
@ActiveProfiles(DATAJPA)
public class MealServiceDataJpaTest extends AbstractMealService {
}
