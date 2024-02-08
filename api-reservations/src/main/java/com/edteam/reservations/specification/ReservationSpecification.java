package com.edteam.reservations.specification;

import com.edteam.reservations.dto.SearchReservationCriteriaDTO;
import com.edteam.reservations.model.Reservation;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReservationSpecification {
    public static Specification<Reservation> withSearchCriteria(SearchReservationCriteriaDTO criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getItineraryId() != null) {
                // Generame la parte de la query y agregamela dentro de las condiciones, fijate en la entidad
                // itinerarios siempre y cuando se
                predicates.add(criteriaBuilder.equal(root.get("itinerary").get("id"), criteria.getItineraryId()));
            }

            if (criteria.getFirstName() != null) {
                predicates
                        .add(criteriaBuilder.equal(root.join("passengers").get("firstName"), criteria.getFirstName()));
            }

            if (criteria.getLastName() != null) {
                predicates.add(criteriaBuilder.equal(root.join("passengers").get("lastName"), criteria.getLastName()));
            }

            if (criteria.getReservationDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("creationDate"), criteria.getReservationDate()));
            }

            if(criteria.getSortDirection() != null && criteria.getSortField() != null){
                if(criteria.getSortDirection().equals("asc")){
                    query.orderBy(criteriaBuilder.asc(root.get(criteria.getSortField())));
                }else{
                    query.orderBy(criteriaBuilder.desc(root.get(criteria.getSortField())));
                }
            }



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
