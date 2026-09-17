-- 权限动作词统一（RBAC spec §3.4 冻结词汇表）：读一律 query。
-- platform 原先用 page(6) / list(1) 表达读；其余动作本来就是标准的 create / update / delete。
-- 合并语义与 dependsOn 的理由见 module-payment 的同名迁移。
UPDATE system_menus
   SET permission = regexp_replace(permission, '^(platform:[a-z0-9-]+):(page|list)$', '\1:query'),
       updated_at = (extract(epoch from now()) * 1000)::bigint
 WHERE permission ~ '^platform:[a-z0-9-]+:(page|list)$';
