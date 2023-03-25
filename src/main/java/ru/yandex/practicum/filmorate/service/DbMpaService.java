package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDAO;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DbMpaService {

    private final MpaDAO mpaDAO;

    public Collection<MPA> findAll() {
        return mpaDAO.findAll();
    }

    public MPA findById(Integer mpaId) {
        if (!mpaDAO.isMpaExists(mpaId)) {
            throw new EntityNotFoundException(String.format("Рейтинга MPA с id %s не существует", mpaId));
        }
        return mpaDAO.findById(mpaId);
    }
}
