package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
public class Film {
    int id;
    String name;
    String description;
    int rate;
    int duration;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getDuration() == film.getDuration() && Objects.equals(getName(), film.getName()) && Objects.equals(getDescription(), film.getDescription()) && Objects.equals(getReleaseDate(), film.getReleaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getReleaseDate(), getDuration());
    }
}
