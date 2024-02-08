package com.edteam.reservations.repository;

import com.edteam.reservations.dto.SearchReservationCriteriaDTO;
import com.edteam.reservations.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    String QUERY_FIND_BY_CREATION_DATE = "SELECT r FROM Reservation r WHERE r.creationDate = :creationDate";

    @Query(QUERY_FIND_BY_CREATION_DATE)
    List<Reservation> findByCreationDate(@Param("creationDate") LocalDate creationDate);

    List<Reservation> findByCreationDateAndPassengersFirstName(LocalDate creationDate, String firstName);

    List<Reservation> findByCreationDateAndPassengersFirstNameAndPassengersLastName(LocalDate creationDate,
            String firstName, String lastName);

    @Transactional(readOnly = true, timeout = 1000)
    List<Reservation> findAll(Specification<Reservation> specification, Pageable pageable);

    List<Reservation> findAllByOrderByCreationDateAsc();

}
