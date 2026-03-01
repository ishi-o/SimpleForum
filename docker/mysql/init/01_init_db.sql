-- 用户服务数据库
CREATE DATABASE IF NOT EXISTS user_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'user_service'@'%' IDENTIFIED BY 'U3eR@2025!db';
GRANT ALL PRIVILEGES ON user_db.* TO 'user_service'@'%';

-- 内容服务数据库
CREATE DATABASE IF NOT EXISTS content_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'content_service'@'%' IDENTIFIED BY 'C0nt3ntSrv@2025!db';
GRANT ALL PRIVILEGES ON content_db.* TO 'content_service'@'%';

-- 互动服务数据库
CREATE DATABASE IF NOT EXISTS interaction_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'interaction_service'@'%' IDENTIFIED BY 'Int3rAct@2025!db';
GRANT ALL PRIVILEGES ON interaction_db.* TO 'interaction_service'@'%';

-- 搜索服务数据库
CREATE DATABASE IF NOT EXISTS search_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'search_service'@'%' IDENTIFIED BY 'S3arch@2025!db';
GRANT ALL PRIVILEGES ON search_db.* TO 'search_service'@'%';

-- 文件服务数据库
CREATE DATABASE IF NOT EXISTS file_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'file_service'@'%' IDENTIFIED BY 'F1le@2025!db';
GRANT ALL PRIVILEGES ON file_db.* TO 'file_service'@'%';

-- 推荐服务数据库
CREATE DATABASE IF NOT EXISTS recommend_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'recommend_service'@'%' IDENTIFIED BY 'R3c0mmend@2025!db';
GRANT ALL PRIVILEGES ON recommend_db.* TO 'recommend_service'@'%';

-- Nacos 数据库
CREATE DATABASE IF NOT EXISTS nacos_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'nacos'@'%' IDENTIFIED BY 'Nac0s@2025!db';
GRANT ALL PRIVILEGES ON nacos_db.* TO 'nacos'@'%';

-- Keycloak 数据库
CREATE DATABASE IF NOT EXISTS keycloak DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'keycloak'@'%' IDENTIFIED BY 'K3ycl0ak@2025!db';
GRANT ALL PRIVILEGES ON keycloak.* TO 'keycloak'@'%';

-- 应用所有权限
FLUSH PRIVILEGES;
