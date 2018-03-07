package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MealHsqldbImpl implements DaoMeal {
    @Override
    public List<Meal> getAll() {
        List<Meal> meal = new ArrayList<>();
        try (Statement stmt = DaoFactory.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM meals");) {
            while (rs.next()) {
                meal.add(new Meal(rs.getInt("id"), rs.getTimestamp("dateTime").toLocalDateTime(),
                        rs.getString("description"), rs.getInt("calories")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meal;
    }

    @Override
    public Meal save(Meal meal) {
        Meal newMeal = null;
        if (meal.getId() == null) {
            try (PreparedStatement stmt = DaoFactory.getConnection()
                    .prepareStatement("INSERT INTO meals(description, calories, datetime) VALUES(?, ?, ?);",
                            RETURN_GENERATED_KEYS)) {
                stmt.setString(1, meal.getDescription());
                stmt.setInt(2, meal.getCalories());
                stmt.setTimestamp(3, Timestamp.valueOf(meal.getDateTime()));
                int updated = stmt.executeUpdate();
                if (updated == 1) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            newMeal = new Meal(generatedKeys.getInt(1), meal.getDateTime(),
                                    meal.getDescription(), meal.getCalories());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement stmt = DaoFactory.getConnection()
                    .prepareStatement("UPDATE meals SET description=?, calories=?, datetime=? WHERE ID=?;")) {
                stmt.setString(1, meal.getDescription());
                stmt.setInt(2, meal.getCalories());
                stmt.setTimestamp(3, Timestamp.valueOf(meal.getDateTime()));
                stmt.setInt(4, meal.getId());
                stmt.executeUpdate();
                newMeal = meal;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return newMeal;
    }

    @Override
    public Meal get(int id) {
        Meal meal = null;
        try (PreparedStatement stmt = DaoFactory.getConnection()
                .prepareStatement("SELECT * FROM meals WHERE id = ?;")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    meal = new Meal(rs.getInt("id"), rs.getTimestamp("dateTime").toLocalDateTime(),
                            rs.getString("description"), rs.getInt("calories"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meal;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (PreparedStatement stmt = DaoFactory.getConnection()
                .prepareStatement("DELETE FROM meals WHERE id = ?;")) {
            stmt.setInt(1, id);
            result = stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}