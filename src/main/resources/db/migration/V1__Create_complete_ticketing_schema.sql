-- Create ticket types table
CREATE TABLE ticket_types (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    base_price DECIMAL(19,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    configuration JSONB NOT NULL DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- Create tickets table
CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    ticket_number VARCHAR(255) UNIQUE NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    type_code VARCHAR(255) NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    qr_code VARCHAR(255) UNIQUE,
    data JSONB DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- Create payments table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(255) UNIQUE NOT NULL,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    processed_at TIMESTAMP NOT NULL,
    ticket_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_payment_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);

-- Create media table
CREATE TABLE media (
    id BIGSERIAL PRIMARY KEY,
    media_id VARCHAR(255) UNIQUE NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size BIGINT NOT NULL,
    context VARCHAR(50),
    context_id VARCHAR(255) NOT NULL,
    url VARCHAR(500),
    thumbnail_url VARCHAR(500),
    metadata JSONB DEFAULT '{}',
    display_order INTEGER DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- Create media_variants table
CREATE TABLE media_variants (
    media_id BIGINT NOT NULL,
    variant_name VARCHAR(50) NOT NULL,
    variant_url VARCHAR(500) NOT NULL,
    PRIMARY KEY (media_id, variant_name),
    CONSTRAINT fk_media_variants_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE
);

-- Create cart table
CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) UNIQUE NOT NULL,
    total_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    total_items INTEGER NOT NULL DEFAULT 0,
    promo_code VARCHAR(100),
    discount_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    expires_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- Create cart_item table
CREATE TABLE cart_item (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    type_code VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(19,2) NOT NULL,
    total_price DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE
);

-- Create checkout_session table
CREATE TABLE checkout_session (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) UNIQUE NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    cart_id BIGINT NOT NULL,
    payment_method VARCHAR(50),
    total_amount DECIMAL(19,2) NOT NULL,
    discount_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    promo_code VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'INITIALIZED',
    payment_details JSONB DEFAULT '{}',
    metadata JSONB DEFAULT '{}',
    expires_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_checkout_session_cart FOREIGN KEY (cart_id) REFERENCES cart(id)
);

-- Create indexes for ticket_types
CREATE UNIQUE INDEX idx_ticket_types_code ON ticket_types(code);
CREATE INDEX idx_ticket_types_active ON ticket_types(active);

-- Create indexes for tickets
CREATE UNIQUE INDEX idx_tickets_ticket_number ON tickets(ticket_number);
CREATE INDEX idx_tickets_status ON tickets(status);
CREATE INDEX idx_tickets_user_id ON tickets(user_id);
CREATE INDEX idx_tickets_type_code ON tickets(type_code);

-- Create indexes for payments
CREATE UNIQUE INDEX idx_payments_transaction_id ON payments(transaction_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_processed_at ON payments(processed_at);

-- Create indexes for media
CREATE UNIQUE INDEX idx_media_media_id ON media(media_id);
CREATE INDEX idx_media_context ON media(context, context_id);
CREATE INDEX idx_media_active ON media(active);

-- Create indexes for cart
CREATE UNIQUE INDEX idx_cart_user_id ON cart(user_id);
CREATE INDEX idx_cart_active ON cart(active);
CREATE INDEX idx_cart_expires_at ON cart(expires_at);

-- Create indexes for cart_item
CREATE INDEX idx_cart_item_cart_id ON cart_item(cart_id);
CREATE INDEX idx_cart_item_type_code ON cart_item(type_code);

-- Create indexes for checkout_session
CREATE UNIQUE INDEX idx_checkout_session_id ON checkout_session(session_id);
CREATE INDEX idx_checkout_session_user_id ON checkout_session(user_id);
CREATE INDEX idx_checkout_session_status ON checkout_session(status);
CREATE INDEX idx_checkout_session_expires_at ON checkout_session(expires_at);