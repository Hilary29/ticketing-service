-- Migration pour créer des billets d'exemple avec horaires spécifiques pour Trip Plus

-- Billets Douala → Yaoundé
INSERT INTO tickets (ticket_number, user_id, type_code, valid_until, amount, status, data) VALUES
('TP001-DLA-YDE-STD-06H', 'system', 'TRIP_PLUS_DOUALA_YAOUNDE_STANDARD', '2025-12-31 23:59:59', 3500.00, 'ACTIVE', 
 '{"id": 1, "departure_time": "06:00", "arrival_time": "10:00", "seats_available": 15, "route": "Douala-Yaoundé", "company": "Trip Plus", "service_level": "Standard", "departure": "Douala", "destination": "Yaoundé"}'),
('TP002-DLA-YDE-COM-09H', 'system', 'TRIP_PLUS_DOUALA_YAOUNDE_COMFORT', '2025-12-31 23:59:59', 4000.00, 'ACTIVE', 
 '{"id": 2, "departure_time": "09:00", "arrival_time": "13:30", "seats_available": 8, "route": "Douala-Yaoundé", "company": "Trip Plus", "service_level": "Confort", "departure": "Douala", "destination": "Yaoundé"}'),
('TP003-DLA-YDE-VIP-14H', 'system', 'TRIP_PLUS_DOUALA_YAOUNDE_VIP', '2025-12-31 23:59:59', 4500.00, 'ACTIVE', 
 '{"id": 3, "departure_time": "14:00", "arrival_time": "18:00", "seats_available": 12, "route": "Douala-Yaoundé", "company": "Trip Plus", "service_level": "VIP", "departure": "Douala", "destination": "Yaoundé"}'),

-- Billets Yaoundé → Douala
('TP004-YDE-DLA-STD-07H', 'system', 'TRIP_PLUS_YAOUNDE_DOUALA_STANDARD', '2025-12-31 23:59:59', 3500.00, 'ACTIVE', 
 '{"id": 4, "departure_time": "07:00", "arrival_time": "11:00", "seats_available": 20, "route": "Yaoundé-Douala", "company": "Trip Plus", "service_level": "Standard", "departure": "Yaoundé", "destination": "Douala"}'),
('TP005-YDE-DLA-COM-11H', 'system', 'TRIP_PLUS_YAOUNDE_DOUALA_COMFORT', '2025-12-31 23:59:59', 4000.00, 'ACTIVE', 
 '{"id": 5, "departure_time": "11:00", "arrival_time": "15:30", "seats_available": 5, "route": "Yaoundé-Douala", "company": "Trip Plus", "service_level": "Confort", "departure": "Yaoundé", "destination": "Douala"}'),
('TP006-YDE-DLA-VIP-16H', 'system', 'TRIP_PLUS_YAOUNDE_DOUALA_VIP', '2025-12-31 23:59:59', 4500.00, 'ACTIVE', 
 '{"id": 6, "departure_time": "16:00", "arrival_time": "20:00", "seats_available": 18, "route": "Yaoundé-Douala", "company": "Trip Plus", "service_level": "VIP", "departure": "Yaoundé", "destination": "Douala"}'),

-- Billets Douala → Bafoussam
('TP007-DLA-BAF-STD-08H', 'system', 'TRIP_PLUS_DOUALA_BAFOUSSAM_STANDARD', '2025-12-31 23:59:59', 2500.00, 'ACTIVE', 
 '{"id": 7, "departure_time": "08:00", "arrival_time": "11:30", "seats_available": 25, "route": "Douala-Bafoussam", "company": "Trip Plus", "service_level": "Standard", "departure": "Douala", "destination": "Bafoussam"}'),
('TP008-DLA-BAF-COM-13H', 'system', 'TRIP_PLUS_DOUALA_BAFOUSSAM_COMFORT', '2025-12-31 23:59:59', 2800.00, 'ACTIVE', 
 '{"id": 8, "departure_time": "13:00", "arrival_time": "16:45", "seats_available": 10, "route": "Douala-Bafoussam", "company": "Trip Plus", "service_level": "Confort", "departure": "Douala", "destination": "Bafoussam"}'),

-- Billets Bafoussam → Douala
('TP009-BAF-DLA-STD-06H30', 'system', 'TRIP_PLUS_BAFOUSSAM_DOUALA_STANDARD', '2025-12-31 23:59:59', 2500.00, 'ACTIVE', 
 '{"id": 9, "departure_time": "06:30", "arrival_time": "10:00", "seats_available": 22, "route": "Bafoussam-Douala", "company": "Trip Plus", "service_level": "Standard", "departure": "Bafoussam", "destination": "Douala"}'),
('TP010-BAF-DLA-COM-15H', 'system', 'TRIP_PLUS_BAFOUSSAM_DOUALA_COMFORT', '2025-12-31 23:59:59', 2800.00, 'ACTIVE', 
 '{"id": 10, "departure_time": "15:00", "arrival_time": "18:45", "seats_available": 15, "route": "Bafoussam-Douala", "company": "Trip Plus", "service_level": "Confort", "departure": "Bafoussam", "destination": "Douala"}'),

-- Billets Yaoundé → Bafoussam
('TP011-YDE-BAF-STD-09H', 'system', 'TRIP_PLUS_YAOUNDE_BAFOUSSAM_STANDARD', '2025-12-31 23:59:59', 2200.00, 'ACTIVE', 
 '{"id": 11, "departure_time": "09:00", "arrival_time": "12:00", "seats_available": 30, "route": "Yaoundé-Bafoussam", "company": "Trip Plus", "service_level": "Standard", "departure": "Yaoundé", "destination": "Bafoussam"}'),
('TP012-YDE-BAF-COM-14H30', 'system', 'TRIP_PLUS_YAOUNDE_BAFOUSSAM_COMFORT', '2025-12-31 23:59:59', 2400.00, 'ACTIVE', 
 '{"id": 12, "departure_time": "14:30", "arrival_time": "17:45", "seats_available": 12, "route": "Yaoundé-Bafoussam", "company": "Trip Plus", "service_level": "Confort", "departure": "Yaoundé", "destination": "Bafoussam"}'),

-- Billets Bafoussam → Yaoundé
('TP013-BAF-YDE-STD-07H', 'system', 'TRIP_PLUS_BAFOUSSAM_YAOUNDE_STANDARD', '2025-12-31 23:59:59', 2200.00, 'ACTIVE', 
 '{"id": 13, "departure_time": "07:00", "arrival_time": "10:00", "seats_available": 28, "route": "Bafoussam-Yaoundé", "company": "Trip Plus", "service_level": "Standard", "departure": "Bafoussam", "destination": "Yaoundé"}'),
('TP014-BAF-YDE-COM-16H', 'system', 'TRIP_PLUS_BAFOUSSAM_YAOUNDE_COMFORT', '2025-12-31 23:59:59', 2400.00, 'ACTIVE', 
 '{"id": 14, "departure_time": "16:00", "arrival_time": "19:15", "seats_available": 8, "route": "Bafoussam-Yaoundé", "company": "Trip Plus", "service_level": "Confort", "departure": "Bafoussam", "destination": "Yaoundé"}');