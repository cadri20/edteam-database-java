INSERT INTO flights_reservation.price (base_price, total_price, total_tax, version, id) VALUES (9.00, 10.50, 11.00, 0, 12);
INSERT INTO flights_reservation.itinerary (id, version, itinerary_id) VALUES (1, 0, 12);
INSERT INTO flights_reservation.segment (carrier, destination, origin, id, version, itinerary_id, arrival, departure) VALUES ('AA', 'MIA', 'BUE', 1, 0, 1, '2024-01-01', '2023-12-31');
INSERT INTO flights_reservation.reservation (creation_date, id, version, itinerary_id) VALUES ('2024-02-07', 1, 0, 1);
