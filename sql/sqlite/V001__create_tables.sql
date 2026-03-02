-- =============================================
-- module-platform 全部建表语句 (SQLite)
-- =============================================

CREATE TABLE IF NOT EXISTS platform_clients (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    app_id TEXT NOT NULL,
    app_secret TEXT NOT NULL,
    status INTEGER NOT NULL DEFAULT 0,
    remark TEXT,
    contact_name TEXT,
    contact_mobile TEXT,
    deleted INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL DEFAULT 0,
    updated_at INTEGER NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_platform_clients_app_id ON platform_clients(app_id);

CREATE TABLE IF NOT EXISTS platform_apis (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    code TEXT NOT NULL,
    description TEXT,
    price INTEGER NOT NULL DEFAULT 0,
    status INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL DEFAULT 0,
    updated_at INTEGER NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_platform_apis_code ON platform_apis(code);

CREATE TABLE IF NOT EXISTS platform_client_apis (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    client_id INTEGER NOT NULL,
    api_id INTEGER NOT NULL,
    custom_price INTEGER,
    status INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL DEFAULT 0,
    updated_at INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_client_apis_client ON platform_client_apis(client_id);
CREATE INDEX IF NOT EXISTS idx_platform_client_apis_api ON platform_client_apis(api_id);

CREATE TABLE IF NOT EXISTS platform_charge_records (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    client_id INTEGER NOT NULL,
    api_id INTEGER NOT NULL,
    order_id TEXT,
    api_code TEXT,
    price INTEGER NOT NULL DEFAULT 0,
    amount INTEGER NOT NULL DEFAULT 0,
    status INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_charge_records_client ON platform_charge_records(client_id);
CREATE INDEX IF NOT EXISTS idx_platform_charge_records_order ON platform_charge_records(order_id);

CREATE TABLE IF NOT EXISTS platform_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    client_id INTEGER NOT NULL,
    api_id INTEGER NOT NULL,
    request_url TEXT,
    request_params TEXT,
    response_body TEXT,
    user_ip TEXT,
    duration INTEGER NOT NULL DEFAULT 0,
    result_code INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_logs_client ON platform_logs(client_id);

CREATE TABLE IF NOT EXISTS platform_stats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    client_id INTEGER NOT NULL,
    api_id INTEGER NOT NULL,
    call_count INTEGER NOT NULL DEFAULT 0,
    total_charge INTEGER NOT NULL DEFAULT 0,
    stat_date TEXT NOT NULL,
    created_at INTEGER NOT NULL DEFAULT 0,
    updated_at INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_platform_stats_client ON platform_stats(client_id);
CREATE INDEX IF NOT EXISTS idx_platform_stats_date ON platform_stats(stat_date);
