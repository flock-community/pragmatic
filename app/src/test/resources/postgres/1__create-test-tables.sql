CREATE TABLE IF NOT EXISTS users
(
    user_id    uuid primary key,
    first_name varchar,
    last_name  varchar,
    birth_day  varchar
);

INSERT INTO users (user_id, first_name, last_name, birth_day)
VALUES ('cf8c1fe6-fb9e-436f-883f-cf5ffba90629', 'Default', 'User', '2010-01-01');
