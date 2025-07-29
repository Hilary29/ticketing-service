-- Drop cart-related tables
DROP TABLE IF EXISTS cart_item CASCADE;
DROP TABLE IF EXISTS cart CASCADE;

-- Update checkout_session table to make cart_id nullable since we no longer use carts
ALTER TABLE checkout_session ALTER COLUMN cart_id DROP NOT NULL;