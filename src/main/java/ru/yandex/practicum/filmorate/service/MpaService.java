package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {
    private MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> findAllMpa() {
        return mpaDbStorage.findAllMpa();
    }

    public Mpa findMpa(int id) {
        return mpaDbStorage.findMpa(id);
    }
}
