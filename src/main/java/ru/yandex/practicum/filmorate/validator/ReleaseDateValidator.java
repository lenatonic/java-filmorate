package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    @Override
    public void initialize(ReleaseDate releaseDate) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        final LocalDate limitDate = LocalDate.of(1895, 12, 28);
        return (localDate != null && !localDate.isBefore(limitDate));
    }
}


