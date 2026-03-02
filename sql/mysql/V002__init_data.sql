-- =============================================
-- module-platform 初始化数据 (MySQL)
-- =============================================

-- =============================================
-- 菜单数据
-- type: 1=目录, 2=菜单, 3=按钮
-- status: 0=正常, 1=停用
-- =============================================

-- 开放平台（一级目录）
INSERT IGNORE INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at)
VALUES (6, '开放平台', '', 1, 0, '/platform', NULL, 'ant-design:cloud-outlined', 6, 0, 0, 0);

-- =====================
-- 开放平台 (parent_id=6) - 二级菜单
-- =====================
INSERT IGNORE INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at)
VALUES (600, 'API管理', 'platform:api:list', 2, 6, 'api', 'platform/api/index', 'ant-design:api-outlined', 1, 0, 0, 0);

INSERT IGNORE INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at)
VALUES (601, '客户端管理', 'platform:client:list', 2, 6, 'client', 'platform/client/index', 'ant-design:desktop-outlined', 2, 0, 0, 0);

INSERT IGNORE INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at)
VALUES (602, '客户端API', 'platform:client-api:list', 2, 6, 'client-api', 'platform/clientapi/index', 'ant-design:link-outlined', 3, 0, 0, 0);

INSERT IGNORE INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at)
VALUES (603, '计费记录', 'platform:charge-record:list', 2, 6, 'charge-record', 'platform/chargerecord/index', 'ant-design:dollar-outlined', 4, 0, 0, 0);

INSERT IGNORE INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at)
VALUES (604, '调用日志', 'platform:log:list', 2, 6, 'log', 'platform/log/index', 'ant-design:file-text-outlined', 5, 0, 0, 0);

INSERT IGNORE INTO system_menus (id, name, permission, type, parent_id, path, component, icon, sort, status, created_at, updated_at)
VALUES (605, '统计分析', 'platform:stat:list', 2, 6, 'stat', 'platform/stat/index', 'ant-design:bar-chart-outlined', 6, 0, 0, 0);
