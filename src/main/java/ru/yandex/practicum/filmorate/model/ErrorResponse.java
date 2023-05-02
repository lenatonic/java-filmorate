package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

public class ErrorResponse {
    String error;
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
