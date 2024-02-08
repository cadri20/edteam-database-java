package com.edteam.reservations.listener;

import com.edteam.reservations.model.Reservation;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PrePersist;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ReservationEntityListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationEntityListener.class);

    @PrePersist
    public void prePersist(Reservation reservation) {
        reservation.setCreationDate(LocalDate.now());
        validateEntity(reservation);
    }

    @PostPersist
    public void postPersist(Reservation reservation) {
        LOGGER.info("The reservation with id {} has been created", reservation.getId());
    }

    @PostRemove
    public void onPostRemove(Reservation reservation) {
        LOGGER.info("The reservation with id {} has been removed", reservation.getId());
    }

    @PostLoad
    public void onPostLoad(Reservation reservation) {
        LOGGER.info("The reservation with id {} has been loaded", reservation.getId());
    }

    private void validateEntity(Reservation transformed) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        var violations = validator.validate(transformed);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
