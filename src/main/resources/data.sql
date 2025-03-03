-- Добавление ролей
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;

-- Добавление администратора (пароль: admin123)
INSERT INTO users (username, email, password, first_name, last_name, subscribe_to_newsletter, created_at, updated_at)
VALUES ('admin', 'admin@beltrufsmak.com', '123123', 'Администратор', 'Системы', true, NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

-- Связь администратора с ролью админа
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

-- Добавление категорий
INSERT INTO categories (name, description) VALUES ('Тракторы', 'Сельскохозяйственные тракторы различной мощности') ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, description) VALUES ('Комбайны', 'Зерноуборочные и кормоуборочные комбайны') ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, description) VALUES ('Сеялки', 'Посевное оборудование для различных культур') ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, description) VALUES ('Плуги', 'Оборудование для вспашки почвы') ON CONFLICT (name) DO NOTHING;
INSERT INTO categories (name, description) VALUES ('Культиваторы', 'Оборудование для обработки почвы') ON CONFLICT (name) DO NOTHING;


-- Добавление продуктов
INSERT INTO products (name, description, price, quantity, image_url, category_id, created_at, updated_at)
SELECT 'Трактор МТЗ-82.1', 'Универсальный колесный трактор тягового класса 1,4 с двигателем мощностью 81 л.с.', 2500000, 5, '/images/mtz-82.png', c.id, NOW(), NOW()
FROM categories c WHERE c.name = 'Тракторы'
ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image_url, category_id, created_at, updated_at)
SELECT 'Комбайн ПАЛЕССЕ GS12', 'Зерноуборочный комбайн с мощностью двигателя 330 л.с.', 7800000, 2, '/images/palesse-gs12.png', c.id, NOW(), NOW()
FROM categories c WHERE c.name = 'Комбайны'
ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image_url, category_id, created_at, updated_at)
SELECT 'Сеялка СЗ-5,4', 'Зерновая сеялка с шириной захвата 5,4 м', 950000, 10, '/images/sz-5-4.png', c.id, NOW(), NOW()
FROM categories c WHERE c.name = 'Сеялки'
ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image_url, category_id, created_at, updated_at)
SELECT 'Плуг ПЛН-3-35', 'Трехкорпусный навесной плуг для вспашки почвы', 120000, 15, '/images/pln-3-35.png', c.id, NOW(), NOW()
FROM categories c WHERE c.name = 'Плуги'
ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image_url, category_id, created_at, updated_at)
SELECT 'Культиватор КПС-4', 'Культиватор для сплошной обработки почвы с шириной захвата 4 м', 280000, 8, '/images/kps-4.png', c.id, NOW(), NOW()
FROM categories c WHERE c.name = 'Культиваторы'
ON CONFLICT DO NOTHING;