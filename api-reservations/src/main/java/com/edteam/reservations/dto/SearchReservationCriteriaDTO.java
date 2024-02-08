package com.edteam.reservations.dto;

import java.time.LocalDate;

public class SearchReservationCriteriaDTO {
    private Long itineraryId;
    private String firstName;
    private String lastName;

    private LocalDate reservationDate;

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    private String sortField;

    private String sortDirection;

    private Integer pageActual = 0;

    private Integer pageSize = 10;

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getSortField() {
        return sortField;
    }

    public Integer getPageActual() {
        return pageActual;
    }

    public void setPageActual(Integer pageActual) {
        this.pageActual = pageActual;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
