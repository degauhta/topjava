package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.JDBC;

/**
 * UserServiceJdbcTest class.
 *
 * @author Denis
 * @since 04.04.2018
 */
@ActiveProfiles(JDBC)
public class UserServiceJdbcTest extends AbstractUserService {
}
