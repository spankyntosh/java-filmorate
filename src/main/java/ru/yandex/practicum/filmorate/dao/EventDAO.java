package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

public interface EventDAO {

    void addEvent(Event event);

    Collection<Event> getEventsByUserId(Integer userId);
}
