package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class ErrorResponse {
    String error;
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
