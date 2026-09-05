-- platform 模块（postgresql）—— 1.0.0 beta1 合并基线。
--
-- 🔴 **只给全新数据库用。** 由原来的 2 个迁移脚本按执行顺序拼接而成：
-- 顺序不变、语句不变，所以结果与逐条执行完全一致。
--
-- 为什么是拼接而不是导出结构快照：这些脚本里有 init_data / seed_menus 这类
-- **种子数据**，`pg_dump --schema-only` 会把它们丢掉，而只导结构就得再手工把
-- INSERT 补回来——那一步没有任何东西能验证对错。拼接则由构造保证等价。
--
-- 拼接的代价是留下了少量互相抵消的步骤（先加列、后改列）。它们无害，但**不要**
-- 试图"顺手清理"：清理一次就等于重新引入一个没人验证过的结构。
--
-- 存量库怎么办：本发布不提供原地升级。Neton 的迁移器按 checksum 校验，V001 变了
-- 就会拒绝启动——这是有意的，见 MigrationEngine 的 CHECKSUM_MISMATCH。
--
-- 加新东西请新增 V002、V003…，不要改这个文件。


-- ─────────────────────────────────────────────────────────────
-- 原 V001__create_tables.sql
-- ─────────────────────────────────────────────────────────────

-- pg_dump --schema-only from dev DB (privchat-application)
--
-- PostgreSQL database dump
--

-- Dumped from database version 16.9 (Homebrew)
-- Dumped by pg_dump version 16.9 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: platform_apis; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.platform_apis (
    id bigint NOT NULL,
    name character varying(128) NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(512),
    price bigint DEFAULT 0 NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    created_at bigint DEFAULT 0 NOT NULL,
    updated_at bigint DEFAULT 0 NOT NULL
);


--
-- Name: platform_apis_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.platform_apis_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_apis_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.platform_apis_id_seq OWNED BY public.platform_apis.id;


--
-- Name: platform_charge_records; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.platform_charge_records (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    api_id bigint NOT NULL,
    order_id character varying(128),
    api_code character varying(64),
    price bigint DEFAULT 0 NOT NULL,
    amount integer DEFAULT 0 NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    created_at bigint DEFAULT 0 NOT NULL
);


--
-- Name: platform_charge_records_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.platform_charge_records_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_charge_records_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.platform_charge_records_id_seq OWNED BY public.platform_charge_records.id;


--
-- Name: platform_client_apis; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.platform_client_apis (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    api_id bigint NOT NULL,
    custom_price bigint,
    status smallint DEFAULT 0 NOT NULL,
    created_at bigint DEFAULT 0 NOT NULL,
    updated_at bigint DEFAULT 0 NOT NULL
);


--
-- Name: platform_client_apis_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.platform_client_apis_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_client_apis_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.platform_client_apis_id_seq OWNED BY public.platform_client_apis.id;


--
-- Name: platform_clients; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.platform_clients (
    id bigint NOT NULL,
    name character varying(128) NOT NULL,
    app_id character varying(64) NOT NULL,
    app_secret character varying(255) NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    remark character varying(512),
    contact_name character varying(64),
    contact_mobile character varying(32),
    deleted smallint DEFAULT 0 NOT NULL,
    created_at bigint DEFAULT 0 NOT NULL,
    updated_at bigint DEFAULT 0 NOT NULL
);


--
-- Name: platform_clients_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.platform_clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.platform_clients_id_seq OWNED BY public.platform_clients.id;


--
-- Name: platform_logs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.platform_logs (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    api_id bigint NOT NULL,
    request_url character varying(512),
    request_params text,
    response_body text,
    user_ip character varying(64),
    duration integer DEFAULT 0 NOT NULL,
    result_code integer DEFAULT 0 NOT NULL,
    created_at bigint DEFAULT 0 NOT NULL
);


--
-- Name: platform_logs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.platform_logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_logs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.platform_logs_id_seq OWNED BY public.platform_logs.id;


--
-- Name: platform_stats; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.platform_stats (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    api_id bigint NOT NULL,
    call_count integer DEFAULT 0 NOT NULL,
    total_charge bigint DEFAULT 0 NOT NULL,
    stat_date character varying(16) NOT NULL,
    created_at bigint DEFAULT 0 NOT NULL,
    updated_at bigint DEFAULT 0 NOT NULL
);


--
-- Name: platform_stats_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.platform_stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: platform_stats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.platform_stats_id_seq OWNED BY public.platform_stats.id;


--
-- Name: platform_apis id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_apis ALTER COLUMN id SET DEFAULT nextval('public.platform_apis_id_seq'::regclass);


--
-- Name: platform_charge_records id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_charge_records ALTER COLUMN id SET DEFAULT nextval('public.platform_charge_records_id_seq'::regclass);


--
-- Name: platform_client_apis id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_client_apis ALTER COLUMN id SET DEFAULT nextval('public.platform_client_apis_id_seq'::regclass);


--
-- Name: platform_clients id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_clients ALTER COLUMN id SET DEFAULT nextval('public.platform_clients_id_seq'::regclass);


--
-- Name: platform_logs id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_logs ALTER COLUMN id SET DEFAULT nextval('public.platform_logs_id_seq'::regclass);


--
-- Name: platform_stats id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_stats ALTER COLUMN id SET DEFAULT nextval('public.platform_stats_id_seq'::regclass);


--
-- Name: platform_apis platform_apis_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_apis
    ADD CONSTRAINT platform_apis_pkey PRIMARY KEY (id);


--
-- Name: platform_charge_records platform_charge_records_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_charge_records
    ADD CONSTRAINT platform_charge_records_pkey PRIMARY KEY (id);


--
-- Name: platform_client_apis platform_client_apis_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_client_apis
    ADD CONSTRAINT platform_client_apis_pkey PRIMARY KEY (id);


--
-- Name: platform_clients platform_clients_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_clients
    ADD CONSTRAINT platform_clients_pkey PRIMARY KEY (id);


--
-- Name: platform_logs platform_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_logs
    ADD CONSTRAINT platform_logs_pkey PRIMARY KEY (id);


--
-- Name: platform_stats platform_stats_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.platform_stats
    ADD CONSTRAINT platform_stats_pkey PRIMARY KEY (id);


--
-- Name: idx_platform_apis_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX idx_platform_apis_code ON public.platform_apis USING btree (code);


--
-- Name: idx_platform_charge_records_client; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_charge_records_client ON public.platform_charge_records USING btree (client_id);


--
-- Name: idx_platform_charge_records_order; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_charge_records_order ON public.platform_charge_records USING btree (order_id);


--
-- Name: idx_platform_client_apis_api; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_client_apis_api ON public.platform_client_apis USING btree (api_id);


--
-- Name: idx_platform_client_apis_client; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_client_apis_client ON public.platform_client_apis USING btree (client_id);


--
-- Name: idx_platform_clients_app_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX idx_platform_clients_app_id ON public.platform_clients USING btree (app_id);


--
-- Name: idx_platform_logs_client; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_logs_client ON public.platform_logs USING btree (client_id);


--
-- Name: idx_platform_stats_client; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_stats_client ON public.platform_stats USING btree (client_id);


--
-- Name: idx_platform_stats_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_platform_stats_date ON public.platform_stats USING btree (stat_date);


--
-- PostgreSQL database dump complete
--


-- ─────────────────────────────────────────────────────────────
-- 原 V002__seed_menus.sql
-- ─────────────────────────────────────────────────────────────

-- module-platform V002: 开放平台菜单 seed (从 dev 库导出)
SET search_path = public;

INSERT INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at) VALUES (6, '开放平台', '', 1, 0, '/platform', NULL, 'ant-design:cloud-outlined', 6, 1, 0, 0) ON CONFLICT (id) DO NOTHING;
INSERT INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at) VALUES (600, 'API管理', 'platform:api:list', 2, 6, 'api', 'platform/api/index', 'ant-design:api-outlined', 1, 1, 0, 0) ON CONFLICT (id) DO NOTHING;
INSERT INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at) VALUES (601, '客户端管理', 'platform:client:list', 2, 6, 'client', 'platform/client/index', 'ant-design:desktop-outlined', 2, 1, 0, 0) ON CONFLICT (id) DO NOTHING;
INSERT INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at) VALUES (602, '客户端API', 'platform:client-api:list', 2, 6, 'client-api', 'platform/clientapi/index', 'ant-design:link-outlined', 3, 1, 0, 0) ON CONFLICT (id) DO NOTHING;
INSERT INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at) VALUES (603, '计费记录', 'platform:charge-record:list', 2, 6, 'charge-record', 'platform/chargerecord/index', 'ant-design:dollar-outlined', 4, 1, 0, 0) ON CONFLICT (id) DO NOTHING;
INSERT INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at) VALUES (604, '调用日志', 'platform:log:list', 2, 6, 'log', 'platform/log/index', 'ant-design:file-text-outlined', 5, 1, 0, 0) ON CONFLICT (id) DO NOTHING;
INSERT INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at) VALUES (605, '统计分析', 'platform:stat:list', 2, 6, 'stat', 'platform/stat/index', 'ant-design:bar-chart-outlined', 6, 1, 0, 0) ON CONFLICT (id) DO NOTHING;
