-- =============================================
-- module-platform 全部建表语句 (PostgreSQL)
-- =============================================

CREATE TABLE IF NOT EXISTS platform_clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    app_id VARCHAR(64) NOT NULL,
    app_secret VARCHAR(255) NOT NULL,
    status SMALLINT NOT NULL DEFAULT 0,
    remark VARCHAR(512),
    contact_name VARCHAR(64),
    contact_mobile VARCHAR(32),
    deleted SMALLINT NOT NULL DEFAULT 0,
    created_at BIGINT NOT NULL DEFAULT 0,
    updated_at BIGINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_platform_clients_app_id ON platform_clients(app_id);

CREATE TABLE IF NOT EXISTS platform_apis (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    code VARCHAR(64) NOT NULL,
    description VARCHAR(512),
    price BIGINT NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 0,
    created_at BIGINT NOT NULL DEFAULT 0,
    updated_at BIGINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_platform_apis_code ON platform_apis(code);

CREATE TABLE IF NOT EXISTS platform_client_apis (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    api_id BIGINT NOT NULL,
    custom_price BIGINT,
    status SMALLINT NOT NULL DEFAULT 0,
    created_at BIGINT NOT NULL DEFAULT 0,
    updated_at BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_client_apis_client ON platform_client_apis(client_id);
CREATE INDEX IF NOT EXISTS idx_platform_client_apis_api ON platform_client_apis(api_id);

CREATE TABLE IF NOT EXISTS platform_charge_records (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    api_id BIGINT NOT NULL,
    order_id VARCHAR(128),
    api_code VARCHAR(64),
    price BIGINT NOT NULL DEFAULT 0,
    amount INT NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 0,
    created_at BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_charge_records_client ON platform_charge_records(client_id);
CREATE INDEX IF NOT EXISTS idx_platform_charge_records_order ON platform_charge_records(order_id);

CREATE TABLE IF NOT EXISTS platform_logs (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    api_id BIGINT NOT NULL,
    request_url VARCHAR(512),
    request_params TEXT,
    response_body TEXT,
    user_ip VARCHAR(64),
    duration INT NOT NULL DEFAULT 0,
    result_code INT NOT NULL DEFAULT 0,
    created_at BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_logs_client ON platform_logs(client_id);

CREATE TABLE IF NOT EXISTS platform_stats (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    api_id BIGINT NOT NULL,
    call_count INT NOT NULL DEFAULT 0,
    total_charge BIGINT NOT NULL DEFAULT 0,
    stat_date VARCHAR(16) NOT NULL,
    created_at BIGINT NOT NULL DEFAULT 0,
    updated_at BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_stats_client ON platform_stats(client_id);
CREATE INDEX IF NOT EXISTS idx_platform_stats_date ON platform_stats(stat_date);
