/*
 * 智慧图书阅读平台 - 模拟数据
 * 生成时间：2026-07-04
 * 说明：请用户检查确认后再执行
 * 警告：请务必在测试环境验证后再应用到生产环境
 */

USE zhihuitushu;

-- 分类数据（ID从1开始自增）
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (1, '全部图书', 0, 0);
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (2, '文学小说', 0, 1);
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (3, '历史传记', 0, 2);
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (4, '科普读物', 0, 3);
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (5, '自我提升', 0, 4);
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (6, '少儿读物', 0, 5);
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (7, '经济管理', 0, 6);
INSERT INTO categories (id, name, parent_id, sort_order) VALUES (8, '艺术设计', 0, 7);

-- 图书数据（category_id对应categories的ID）
INSERT INTO books (id, title, author, cover, category_id, publisher, publish_date, language, pages, isbn, description, rating, review_count, read_count, download_count) VALUES
(1, '百年孤独', '加西亚·马尔克斯', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20magical%20realism&image_size=square', 2, '南海出版公司', '2011-06-01', '中文', 360, '978-7-5442-5864-5', '《百年孤独》是魔幻现实主义文学的代表作，描写了布恩迪亚家族七代人的传奇故事，以及加勒比海沿岸小镇马孔多的百年兴衰。作品融入神话传说、民间故事、宗教典故等神秘因素，巧妙地糅合了现实与虚幻，展现出一个瑰丽的想象世界。', 5.0, 12580, 50000, 12000),
(2, '三体', '刘慈欣', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20science%20fiction%20universe&image_size=square', 4, '重庆出版社', '2008-01-01', '中文', 400, '978-7-5366-9293-0', '《三体》是刘慈欣创作的系列长篇科幻小说，讲述了地球人类文明和三体文明的信息交流、生死搏杀及两个文明在宇宙中的兴衰历程。作品获得第73届雨果奖最佳长篇小说奖。', 5.0, 25600, 80000, 25000),
(3, '活着', '余华', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20chinese%20rural%20life&image_size=square', 2, '作家出版社', '2012-08-01', '中文', 224, '978-7-5063-6462-6', '《活着》讲述了农村人福贵悲惨的人生遭遇。福贵本是个富家少爷，可他嗜赌如命，终于赌光了家业，气死了父亲。他的母亲临终前也没能见上他最后一面。', 5.0, 18900, 60000, 18000),
(4, '人类简史', '尤瓦尔·赫拉利', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20human%20history%20evolution&image_size=square', 3, '中信出版社', '2017-02-01', '中文', 440, '978-7-5086-6338-3', '《人类简史》从认知革命、农业革命到科学革命，重新审视人类历史，探讨人类如何成为地球的主宰。作者以宏大的视角审视人类历史，挑战了许多我们习以为常的认知。', 4.9, 9800, 35000, 8500),
(5, '小王子', '圣埃克苏佩里', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20little%20prince%20stars%20illustration&image_size=square', 6, '人民文学出版社', '2003-08-01', '中文', 96, '978-7-02-004250-9', '《小王子》讲述了小王子从自己星球出发前往地球的过程中，所经历的各种历险。作者以小王子的孩子式的眼光，透视出成人的空虚、盲目，愚妄和死板教条。', 4.9, 15200, 45000, 12000),
(6, '穷查理宝典', '彼得·考夫曼', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20investment%20business%20wisdom&image_size=square', 5, '中信出版社', '2016-08-01', '中文', 552, '978-7-5086-5385-5', '《穷查理宝典》收录了查理·芒格的个人传记与投资哲学，以及他的公开演讲和文章。芒格的智慧涵盖投资、商业、人生等多个领域。', 4.8, 6700, 25000, 6000),
(7, '红楼梦', '曹雪芹', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20chinese%20classical%20novel&image_size=square', 2, '人民文学出版社', '1982-03-01', '中文', 1075, '978-7-02-000220-0', '《红楼梦》是中国古典四大名著之一，以贾、史、王、薛四大家族的兴衰为背景，以贾宝玉和林黛玉的爱情悲剧为主线，展现了封建社会的种种弊端。', 4.9, 22000, 75000, 15000),
(8, '围城', '钱钟书', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20chinese%20modern%20literature&image_size=square', 2, '人民文学出版社', '2018-01-01', '中文', 359, '978-7-02-013280-7', '《围城》是钱钟书所著的长篇小说，是中国现代文学史上一部风格独特的讽刺小说。被誉为"新儒林外史"。', 4.8, 8600, 30000, 7500),
(9, '明朝那些事儿', '当年明月', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20chinese%20history%20ming%20dynasty&image_size=square', 3, '浙江人民出版社', '2017-05-01', '中文', 960, '978-7-213-08063-5', '《明朝那些事儿》以史料为基础，以通俗幽默的笔法讲述了明朝两百多年的历史。从朱元璋建立明朝开始，到崇祯皇帝自尽结束。', 4.8, 11200, 40000, 9000),
(10, '原则', '瑞·达利欧', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20business%20principles%20success&image_size=square', 5, '中信出版社', '2018-01-01', '中文', 576, '978-7-5086-7764-3', '《原则》是桥水基金创始人瑞·达利欧的人生经验之作。作者分享了他在生活和工作中总结的原则，帮助读者做出更好的决策。', 4.7, 7800, 28000, 6500),
(11, '思考，快与慢', '丹尼尔·卡尼曼', 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20psychology%20thinking%20brain&image_size=square', 5, '中信出版社', '2017-11-01', '中文', 420, '978-7-5086-7385-5', '《思考，快与慢》是诺贝尔经济学奖得主丹尼尔·卡尼曼的代表作。作者将人类的思考分为两个系统，探讨了我们如何做决策。', 4.7, 8900, 32000, 7000);

-- 图书标签数据
INSERT INTO book_tags (book_id, tag) VALUES (1, '魔幻现实主义');
INSERT INTO book_tags (book_id, tag) VALUES (1, '诺贝尔文学奖');
INSERT INTO book_tags (book_id, tag) VALUES (1, '经典名著');
INSERT INTO book_tags (book_id, tag) VALUES (2, '科幻小说');
INSERT INTO book_tags (book_id, tag) VALUES (2, '雨果奖');
INSERT INTO book_tags (book_id, tag) VALUES (2, '刘慈欣');
INSERT INTO book_tags (book_id, tag) VALUES (3, '中国文学');
INSERT INTO book_tags (book_id, tag) VALUES (3, '余华');
INSERT INTO book_tags (book_id, tag) VALUES (4, '历史');
INSERT INTO book_tags (book_id, tag) VALUES (4, '人类学');
INSERT INTO book_tags (book_id, tag) VALUES (4, '社会学');
INSERT INTO book_tags (book_id, tag) VALUES (5, '童话');
INSERT INTO book_tags (book_id, tag) VALUES (5, '儿童文学');
INSERT INTO book_tags (book_id, tag) VALUES (6, '投资理财');
INSERT INTO book_tags (book_id, tag) VALUES (6, '思维方式');
INSERT INTO book_tags (book_id, tag) VALUES (7, '四大名著');
INSERT INTO book_tags (book_id, tag) VALUES (7, '古典文学');
INSERT INTO book_tags (book_id, tag) VALUES (8, '现代文学');
INSERT INTO book_tags (book_id, tag) VALUES (8, '钱钟书');
INSERT INTO book_tags (book_id, tag) VALUES (9, '中国历史');
INSERT INTO book_tags (book_id, tag) VALUES (9, '明朝');
INSERT INTO book_tags (book_id, tag) VALUES (10, '商业');
INSERT INTO book_tags (book_id, tag) VALUES (10, '成功学');
INSERT INTO book_tags (book_id, tag) VALUES (11, '心理学');
INSERT INTO book_tags (book_id, tag) VALUES (11, '决策');

-- 用户数据（密码统一为：Password123，BCrypt哈希值）
INSERT INTO users (id, username, password, email, phone, nickname, role, status) VALUES
(1, 'zhangsan', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'zhangsan@example.com', '13800138001', '张三', 'user', 1),
(2, 'lisi', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'lisi@example.com', '13800138002', '李四', 'user', 1),
(3, 'wangwu', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'wangwu@example.com', '13800138003', '王五', 'user', 1),
(4, 'zhaoliu', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'zhaoliu@example.com', '13800138004', '赵六', 'user', 1),
(5, 'sunqi', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'sunqi@example.com', '13800138005', '孙七', 'admin', 1),
(6, 'zhouba', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'zhouba@example.com', '13800138006', '周八', 'user', 1),
(7, 'wujiu', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'wujiu@example.com', '13800138007', '吴九', 'user', 0),
(8, 'zhengshi', '$2b$12$Sqqkdl0WywQrgCE7rELmZ.j6FYsxCn54.VhjUdwRbKY.FKvfNI4SG', 'zhengshi@example.com', '13800138008', '郑十', 'user', 1);

-- 用户书架数据
INSERT INTO user_books (user_id, book_id, status, progress, current_page, total_pages, read_time, last_read_time) VALUES
(1, 1, 2, 100, 360, 360, 180, '2026-06-15 10:30:00'),
(1, 2, 1, 45, 180, 400, 90, '2026-07-03 20:15:00'),
(1, 3, 0, 0, 0, 224, 0, NULL),
(2, 2, 2, 100, 400, 400, 200, '2026-06-20 15:00:00'),
(2, 4, 1, 30, 132, 440, 60, '2026-07-02 19:30:00'),
(3, 5, 2, 100, 96, 96, 30, '2026-06-10 11:00:00'),
(3, 1, 1, 60, 216, 360, 120, '2026-07-01 21:00:00'),
(4, 6, 1, 25, 138, 552, 50, '2026-07-03 14:45:00'),
(5, 7, 2, 100, 1075, 1075, 500, '2026-05-20 09:00:00'),
(5, 8, 1, 50, 180, 359, 80, '2026-07-03 16:30:00'),
(6, 9, 1, 70, 672, 960, 200, '2026-07-02 18:00:00'),
(6, 10, 0, 0, 0, 576, 0, NULL),
(8, 11, 1, 40, 168, 420, 70, '2026-07-01 20:00:00');

-- 用户评论数据
INSERT INTO user_comments (user_id, book_id, content, rating, likes, status) VALUES
(1, 1, '这本书真的太棒了！魔幻现实主义的写法让人叹为观止，布恩迪亚家族的故事让人久久不能忘怀。强烈推荐！', 5.0, 128, 1),
(2, 2, '刘慈欣的想象力太丰富了！三体世界的设定非常精彩，黑暗森林法则让人深思。这是我读过最好的科幻小说。', 5.0, 256, 1),
(3, 5, '经典的童话，适合所有年龄段的人阅读。小王子的故事教会我们什么是真正重要的东西。', 5.0, 89, 1),
(1, 3, '余华的文笔朴实无华，但却直击人心。福贵的一生让人唏嘘不已，珍惜当下吧。', 4.5, 67, 1),
(4, 6, '芒格的智慧值得反复学习。这本书改变了我的思维方式，让我在做决策时更加理性。', 4.8, 45, 1),
(2, 4, '从全新的角度解读人类历史，非常有启发性。推荐给所有对历史感兴趣的朋友。', 4.5, 34, 1),
(5, 7, '中国古典文学的巅峰之作，每次重读都有新的收获。曹雪芹的才华令人折服。', 5.0, 198, 1),
(6, 9, '用通俗的语言讲述历史，非常有趣。当年明月让历史不再枯燥。', 4.5, 78, 1),
(8, 11, '心理学的经典之作，帮助我更好地理解自己的思维方式。卡尼曼的研究很有价值。', 4.7, 23, 1),
(3, 2, '科幻迷必看！宏大的世界观和精彩的故事线，让人欲罢不能。', 5.0, 145, 1);

-- 收藏数据
INSERT INTO favorites (user_id, book_id) VALUES (1, 1), (1, 2), (1, 7);
INSERT INTO favorites (user_id, book_id) VALUES (2, 2), (2, 4), (2, 9);
INSERT INTO favorites (user_id, book_id) VALUES (3, 5), (3, 1), (3, 8);
INSERT INTO favorites (user_id, book_id) VALUES (4, 6), (4, 10), (4, 11);
INSERT INTO favorites (user_id, book_id) VALUES (5, 7), (5, 8), (5, 1);

-- 阅读记录数据
INSERT INTO reading_records (user_id, book_id, start_page, end_page, read_duration, read_date) VALUES
(1, 2, 160, 180, 30, '2026-07-03'),
(1, 2, 140, 160, 25, '2026-07-02'),
(1, 2, 120, 140, 20, '2026-07-01'),
(2, 4, 100, 132, 45, '2026-07-02'),
(3, 1, 180, 216, 50, '2026-07-01'),
(4, 6, 100, 138, 40, '2026-07-03'),
(5, 8, 150, 180, 35, '2026-07-03'),
(6, 9, 650, 672, 30, '2026-07-02'),
(8, 11, 140, 168, 25, '2026-07-01');

-- 搜索历史数据
INSERT INTO search_history (user_id, keyword) VALUES (1, '科幻小说');
INSERT INTO search_history (user_id, keyword) VALUES (1, '三体');
INSERT INTO search_history (user_id, keyword) VALUES (2, '历史书籍');
INSERT INTO search_history (user_id, keyword) VALUES (3, '儿童读物');
INSERT INTO search_history (user_id, keyword) VALUES (4, '投资理财');
INSERT INTO search_history (user_id, keyword) VALUES (NULL, '经典名著');
INSERT INTO search_history (user_id, keyword) VALUES (NULL, '百年孤独');