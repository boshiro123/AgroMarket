-- Создаем новую таблицу для подписчиков рассылки
CREATE TABLE newsletter_subscribers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    subscribed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Копируем существующих подписчиков в новую таблицу
INSERT INTO newsletter_subscribers (email)
SELECT email FROM users WHERE subscribe_to_newsletter = true;

-- Удаляем колонку subscribe_to_newsletter из таблицы users
ALTER TABLE users DROP COLUMN subscribe_to_newsletter; 