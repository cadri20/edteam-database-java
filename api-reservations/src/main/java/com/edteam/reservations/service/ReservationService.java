package com.edteam.reservations.service;

import com.edteam.reservations.dao.ReservationDao;
import com.edteam.reservations.dto.SearchReservationCriteriaDTO;
import com.edteam.reservations.enums.APIError;
import com.edteam.reservations.exception.EdteamException;
import com.edteam.reservations.dto.ReservationDTO;
import com.edteam.reservations.model.Reservation;
import com.edteam.reservations.repository.ReservationRepository;
import com.edteam.reservations.specification.ReservationSpecification;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    private ReservationDao repository;

    private ConversionService conversionService;

    @Autowired
    public ReservationService(ReservationDao repository, ConversionService conversionService) {
        this.repository = repository;
        this.conversionService = conversionService;
    }

    public List<ReservationDTO> getReservations(SearchReservationCriteriaDTO criteria) {
        Pageable pageable = PageRequest.of(criteria.getPageActual(), criteria.getPageSize());
        return conversionService.convert(repository.findAll(criteria), List.class);
    }

    public ReservationDTO getReservationById(Long id) {
        Optional<Reservation> result = repository.findById(id);
        if (result.isEmpty()) {
            LOGGER.debug("Not exist reservation with the id {}", id);
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }
        return conversionService.convert(result.get(), ReservationDTO.class);
    }

    public ReservationDTO save(ReservationDTO reservation) {
        if (Objects.nonNull(reservation.getId())) {
            throw new EdteamException(APIError.RESERVATION_WITH_SAME_ID);
        }

        Reservation transformed = conversionService.convert(reservation, Reservation.class);
        validateEntity(transformed);
        Reservation result = repository.save(Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class);
    }

    public ReservationDTO update(Long id, ReservationDTO reservation) {
        if (!repository.existsById(id)) {
            LOGGER.debug("Not exist reservation with the id {}", id);
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }

        LOGGER.info("Queries executes to obatin the information");
        Reservation transformed = conversionService.convert(reservation, Reservation.class);
        Reservation result = repository.save(Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            LOGGER.debug("Not exist reservation with the id {}", id);
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }

        repository.deleteById(id);
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
