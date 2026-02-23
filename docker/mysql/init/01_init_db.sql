-- content_db
CREATE DATABASE IF NOT EXISTS content_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'content_service'@'%' IDENTIFIED BY 'C0nt3ntSrv@2025!db';

GRANT ALL PRIVILEGES ON content_db.* TO 'content_service'@'%';
FLUSH PRIVILEGES;
