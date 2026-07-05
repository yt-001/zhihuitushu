CREATE DATABASE IF NOT EXISTS zhihuitushu CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zhihuitushu;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名/账号',
    password VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    email VARCHAR(100) NULL UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) NULL UNIQUE COMMENT '手机号',
    avatar VARCHAR(255) NULL COMMENT '头像URL',
    role VARCHAR(20) DEFAULT 'user' COMMENT '用户角色',
    status TINYINT DEFAULT 1 COMMENT '状态（0禁用，1启用）',
    failed_attempts INT DEFAULT 0 COMMENT '登录失败次数',
    locked_at DATETIME NULL COMMENT '锁定时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图书ID',
    title VARCHAR(200) NOT NULL COMMENT '书名',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    cover VARCHAR(500) NULL COMMENT '封面URL',
    category VARCHAR(50) NULL COMMENT '分类',
    publisher VARCHAR(100) NULL COMMENT '出版社',
    language VARCHAR(20) DEFAULT '中文' COMMENT '语言',
    pages INT NULL COMMENT '页数',
    isbn VARCHAR(50) NULL UNIQUE COMMENT 'ISBN',
    description TEXT NULL COMMENT '简介',
    rating DECIMAL(3,1) DEFAULT 0 COMMENT '评分',
    review_count INT DEFAULT 0 COMMENT '评论数',
    read_count INT DEFAULT 0 COMMENT '阅读次数',
    status TINYINT DEFAULT 1 COMMENT '状态（0下架，1上架）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书表';

CREATE TABLE IF NOT EXISTS user_books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '图书ID',
    status TINYINT DEFAULT 0 COMMENT '状态（0想读，1阅读中，2已完成）',
    progress INT DEFAULT 0 COMMENT '阅读进度（百分比）',
    current_page INT DEFAULT 0 COMMENT '当前页码',
    total_pages INT DEFAULT 0 COMMENT '总页数',
    read_time INT DEFAULT 0 COMMENT '阅读时长（分钟）',
    last_read_time DATETIME NULL COMMENT '上次阅读时间',
    added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_book (user_id, book_id),
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户书架表';

CREATE TABLE IF NOT EXISTS book_tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    book_id BIGINT NOT NULL COMMENT '图书ID',
    tag VARCHAR(50) NOT NULL COMMENT '标签',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_book_id (book_id),
    INDEX idx_tag (tag),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书标签表';

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

INSERT INTO categories (name, parent_id, sort_order) VALUES ('全部图书', 0, 0);
INSERT INTO categories (name, parent_id, sort_order) VALUES ('文学小说', 0, 1);
INSERT INTO categories (name, parent_id, sort_order) VALUES ('历史传记', 0, 2);
INSERT INTO categories (name, parent_id, sort_order) VALUES ('科普读物', 0, 3);
INSERT INTO categories (name, parent_id, sort_order) VALUES ('自我提升', 0, 4);
INSERT INTO categories (name, parent_id, sort_order) VALUES ('少儿读物', 0, 5);

INSERT INTO books (title, author, category, publisher, language, pages, description, rating, review_count, read_count) VALUES 
('百年孤独', '加西亚·马尔克斯', '文学小说', '南海出版公司', '中文', 360, '《百年孤独》是魔幻现实主义文学的代表作，描写了布恩迪亚家族七代人的传奇故事，以及加勒比海沿岸小镇马孔多的百年兴衰。', 5.0, 12580, 50000),
('三体', '刘慈欣', '科普读物', '重庆出版社', '中文', 400, '《三体》是刘慈欣创作的系列长篇科幻小说，讲述了地球人类文明和三体文明的信息交流、生死搏杀及两个文明在宇宙中的兴衰历程。', 5.0, 25600, 80000),
('活着', '余华', '文学小说', '作家出版社', '中文', 224, '《活着》讲述了农村人福贵悲惨的人生遭遇。福贵本是个富家少爷，可他嗜赌如命，终于赌光了家业，气死了父亲。', 5.0, 18900, 60000),
('人类简史', '尤瓦尔·赫拉利', '历史传记', '中信出版社', '中文', 440, '《人类简史》从认知革命、农业革命到科学革命，重新审视人类历史，探讨人类如何成为地球的主宰。', 4.9, 9800, 35000),
('小王子', '圣埃克苏佩里', '少儿读物', '人民文学出版社', '中文', 96, '《小王子》讲述了小王子从自己星球出发前往地球的过程中，所经历的各种历险。', 4.9, 15200, 45000),
('穷查理宝典', '彼得·考夫曼', '自我提升', '中信出版社', '中文', 552, '《穷查理宝典》收录了查理·芒格的个人传记与投资哲学，以及他的公开演讲和文章。', 4.8, 6700, 25000);

INSERT INTO book_tags (book_id, tag) VALUES (1, '魔幻现实主义');
INSERT INTO book_tags (book_id, tag) VALUES (1, '诺贝尔文学奖');
INSERT INTO book_tags (book_id, tag) VALUES (1, '经典名著');
INSERT INTO book_tags (book_id, tag) VALUES (2, '科幻小说');
INSERT INTO book_tags (book_id, tag) VALUES (2, '雨果奖');
INSERT INTO book_tags (book_id, tag) VALUES (3, '中国文学');
INSERT INTO book_tags (book_id, tag) VALUES (3, '经典');
INSERT INTO book_tags (book_id, tag) VALUES (4, '历史');
INSERT INTO book_tags (book_id, tag) VALUES (4, '人类学');
INSERT INTO book_tags (book_id, tag) VALUES (5, '童话');
INSERT INTO book_tags (book_id, tag) VALUES (5, '儿童文学');
INSERT INTO book_tags (book_id, tag) VALUES (6, '投资理财');
INSERT INTO book_tags (book_id, tag) VALUES (6, '思维方式');