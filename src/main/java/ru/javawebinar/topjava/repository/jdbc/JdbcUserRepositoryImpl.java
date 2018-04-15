package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final RowMapper<User> MAPPER = (rs, rowNum) -> {
        List<Role> roles = rs.getString("roles") == null ? null :
                Arrays.stream(rs.getString("roles").split("\\s*,\\s*"))
                        .map(Role::valueOf).collect(Collectors.toList());
        return new User(rs.getInt("id"), rs.getString("name"),
                rs.getString("email"), rs.getString("password"),
                rs.getInt("calories_per_day"), rs.getBoolean("enabled"),
                rs.getDate("registered"), roles);
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            sqlBatch(user.getRoles().stream().collect(Collectors.toList()), user.getId(),
                    "INSERT INTO user_roles (role, user_id) VALUES (?, ?)");
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) != 0) {
                sqlBatch(user.getRoles().stream().collect(Collectors.toList()), user.getId(),
                        "UPDATE user_roles SET role = ? WHERE user_id = ?");
            } else {
                return null;
            }
        }
        return user;
    }

    private void sqlBatch(final List<Role> roles, int id, String sql) {
        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, roles.get(i).toString());
                ps.setInt(2, id);
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, " +
                "string_agg(ur.role, ',') AS roles FROM users AS u LEFT JOIN user_roles AS ur ON u.id = ur.user_id " +
                "WHERE id=? GROUP BY u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day", MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, " +
                "string_agg(ur.role, ',') AS roles FROM users AS u LEFT JOIN user_roles AS ur ON u.id = ur.user_id " +
                "WHERE email=? GROUP BY u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day", MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, " +
                "string_agg(ur.role, ',') AS roles FROM users AS u LEFT JOIN user_roles AS ur ON u.id = ur.user_id " +
                "GROUP BY u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day " +
                "ORDER BY u.name, u.email", MAPPER);
    }
}
