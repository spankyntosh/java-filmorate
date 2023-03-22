package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDAO;
import ru.yandex.practicum.filmorate.dao.mappers.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

@Component
public class EventDAOImpl implements EventDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(Event event) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("id");

        insert.execute(event.toMap());
    }

    @Override
    public Collection<Event> getEventsByUserId(Integer userId) {
        String statement = "SELECT * "
                + "FROM events "
                + "WHERE user_id = ?";

        return jdbcTemplate.query(statement, new EventMapper(), userId);
    }
}
