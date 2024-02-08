package com.edteam.reservations.dao;

import com.edteam.reservations.dto.SearchReservationCriteriaDTO;
import com.edteam.reservations.enums.APIError;
import com.edteam.reservations.exception.EdteamException;
import com.edteam.reservations.model.Reservation;
import com.edteam.reservations.service.ReservationService;
import com.edteam.reservations.specification.ReservationSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationDaoImpl implements ReservationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private TransactionTemplate transactionTemplate;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Reservation> findAll(SearchReservationCriteriaDTO criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> query = criteriaBuilder.createQuery(Reservation.class);
        Root<Reservation> root = query.from(Reservation.class);

        Predicate predicate = ReservationSpecification.withSearchCriteria(criteria).toPredicate(root, query,
                criteriaBuilder);
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }


    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Optional<Reservation> findById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        return Optional.of(reservation);
    }

    @Override
    public Reservation save(Reservation reservation) {
        LOGGER.debug("Saving reservation {}", reservation);
        transactionTemplate.execute(status -> {
            try {
                if (Objects.nonNull(reservation.getItinerary())) {
                    entityManager.merge(reservation);
                    entityManager.flush();
                } else {
                    entityManager.persist(reservation);
                    entityManager.flush();
                }
                // throw new EdteamException(APIError.BAD_FORMAT);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
            return null;
        });
        return reservation;
    }

    @Override
    public void deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (Objects.nonNull(reservation)) {
            entityManager.remove(reservation);
            entityManager.flush();
        }
    }

    @Override
    public boolean existsById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        return Objects.nonNull(reservation);
    }
}
