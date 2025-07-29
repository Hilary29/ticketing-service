-- Migration pour créer les types de billets pour l'application Trip Plus
-- Tous les billets appartiennent à la compagnie Trip Plus

-- Insertion des types de billets de bus pour la compagnie Trip Plus
INSERT INTO ticket_types (code, name, base_price, configuration) VALUES
-- Routes Douala ↔ Yaoundé
('TRIP_PLUS_DOUALA_YAOUNDE_STANDARD', 'Trip Plus - Douala → Yaoundé (Standard)', 3500.00, 
 '{"company": "Trip Plus", "route": "Douala-Yaoundé", "duration": "4h", "category": "transport", "vehicle_type": "bus", "service_level": "standard"}'),
('TRIP_PLUS_DOUALA_YAOUNDE_COMFORT', 'Trip Plus - Douala → Yaoundé (Confort)', 4000.00, 
 '{"company": "Trip Plus", "route": "Douala-Yaoundé", "duration": "4h30", "category": "transport", "vehicle_type": "bus", "service_level": "comfort"}'),
('TRIP_PLUS_DOUALA_YAOUNDE_VIP', 'Trip Plus - Douala → Yaoundé (VIP)', 4500.00, 
 '{"company": "Trip Plus", "route": "Douala-Yaoundé", "duration": "4h", "category": "transport", "vehicle_type": "bus", "service_level": "vip"}'),

('TRIP_PLUS_YAOUNDE_DOUALA_STANDARD', 'Trip Plus - Yaoundé → Douala (Standard)', 3500.00, 
 '{"company": "Trip Plus", "route": "Yaoundé-Douala", "duration": "4h", "category": "transport", "vehicle_type": "bus", "service_level": "standard"}'),
('TRIP_PLUS_YAOUNDE_DOUALA_COMFORT', 'Trip Plus - Yaoundé → Douala (Confort)', 4000.00, 
 '{"company": "Trip Plus", "route": "Yaoundé-Douala", "duration": "4h30", "category": "transport", "vehicle_type": "bus", "service_level": "comfort"}'),
('TRIP_PLUS_YAOUNDE_DOUALA_VIP', 'Trip Plus - Yaoundé → Douala (VIP)', 4500.00, 
 '{"company": "Trip Plus", "route": "Yaoundé-Douala", "duration": "4h", "category": "transport", "vehicle_type": "bus", "service_level": "vip"}'),

-- Routes Douala ↔ Bafoussam
('TRIP_PLUS_DOUALA_BAFOUSSAM_STANDARD', 'Trip Plus - Douala → Bafoussam (Standard)', 2500.00, 
 '{"company": "Trip Plus", "route": "Douala-Bafoussam", "duration": "3h30", "category": "transport", "vehicle_type": "bus", "service_level": "standard"}'),
('TRIP_PLUS_DOUALA_BAFOUSSAM_COMFORT', 'Trip Plus - Douala → Bafoussam (Confort)', 2800.00, 
 '{"company": "Trip Plus", "route": "Douala-Bafoussam", "duration": "3h45", "category": "transport", "vehicle_type": "bus", "service_level": "comfort"}'),

('TRIP_PLUS_BAFOUSSAM_DOUALA_STANDARD', 'Trip Plus - Bafoussam → Douala (Standard)', 2500.00, 
 '{"company": "Trip Plus", "route": "Bafoussam-Douala", "duration": "3h30", "category": "transport", "vehicle_type": "bus", "service_level": "standard"}'),
('TRIP_PLUS_BAFOUSSAM_DOUALA_COMFORT', 'Trip Plus - Bafoussam → Douala (Confort)', 2800.00, 
 '{"company": "Trip Plus", "route": "Bafoussam-Douala", "duration": "3h45", "category": "transport", "vehicle_type": "bus", "service_level": "comfort"}'),

-- Routes Yaoundé ↔ Bafoussam
('TRIP_PLUS_YAOUNDE_BAFOUSSAM_STANDARD', 'Trip Plus - Yaoundé → Bafoussam (Standard)', 2200.00, 
 '{"company": "Trip Plus", "route": "Yaoundé-Bafoussam", "duration": "3h", "category": "transport", "vehicle_type": "bus", "service_level": "standard"}'),
('TRIP_PLUS_YAOUNDE_BAFOUSSAM_COMFORT', 'Trip Plus - Yaoundé → Bafoussam (Confort)', 2400.00, 
 '{"company": "Trip Plus", "route": "Yaoundé-Bafoussam", "duration": "3h15", "category": "transport", "vehicle_type": "bus", "service_level": "comfort"}'),

('TRIP_PLUS_BAFOUSSAM_YAOUNDE_STANDARD', 'Trip Plus - Bafoussam → Yaoundé (Standard)', 2200.00, 
 '{"company": "Trip Plus", "route": "Bafoussam-Yaoundé", "duration": "3h", "category": "transport", "vehicle_type": "bus", "service_level": "standard"}'),
('TRIP_PLUS_BAFOUSSAM_YAOUNDE_COMFORT', 'Trip Plus - Bafoussam → Yaoundé (Confort)', 2400.00, 
 '{"company": "Trip Plus", "route": "Bafoussam-Yaoundé", "duration": "3h15", "category": "transport", "vehicle_type": "bus", "service_level": "comfort"}');