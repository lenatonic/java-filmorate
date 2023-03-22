package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
public class Film {
    private int id;
   private String name;
    private String description;
    private int rate;
    private int duration;

    private LocalDate releaseDate;

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
