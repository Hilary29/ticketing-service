-- Insert initial ticket types
INSERT INTO ticket_types (code, name, base_price, configuration) VALUES
('CINEMA_STANDARD', 'Ticket Cinéma Standard', 12.50, '{"duration": 120, "category": "entertainment"}'),
('CINEMA_VIP', 'Ticket Cinéma VIP', 18.00, '{"duration": 120, "category": "entertainment", "benefits": ["priority_seating", "complimentary_drink"]}'),
('CONCERT_GENERAL', 'Concert - Placement Général', 45.00, '{"category": "music", "standing": true}'),
('CONCERT_VIP', 'Concert VIP', 85.00, '{"category": "music", "benefits": ["backstage_access", "meet_greet"]}'),
('TRANSPORT_BUS', 'Transport Bus', 2.50, '{"duration": 90, "category": "transport", "zones": ["A", "B"]}'),
('TRANSPORT_METRO', 'Transport Métro', 1.90, '{"duration": 120, "category": "transport", "zones": ["1", "2", "3"]}'),
('EVENT_CONFERENCE', 'Conférence', 25.00, '{"duration": 480, "category": "education", "includes_materials": true}'),
('EVENT_WORKSHOP', 'Atelier', 35.00, '{"duration": 240, "category": "education", "max_participants": 20}');