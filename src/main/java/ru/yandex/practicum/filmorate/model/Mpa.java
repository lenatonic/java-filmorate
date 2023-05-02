package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
public class Mpa {
    int id;
    String name;

    public Mpa(int id) {
        this.id = id;
    }
}
