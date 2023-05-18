package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
public class MpaController {
    private MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping(value = "/mpa")
    public List<Mpa> findAllMpa() {
        return mpaService.findAllMpa();
    }

    @GetMapping(value = "/mpa/{id}")
    public Mpa findMpa(@PathVariable("id") int id) {
        return mpaService.findMpa(id);
    }
}