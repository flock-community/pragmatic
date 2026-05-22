CREATE TABLE IF NOT EXISTS users
(
    id    uuid primary key,
    firstName varchar,
    lastName  varchar,
    birthDay  varchar
);

INSERT INTO users (id, firstName, lastName, birthDay)
VALUES ('cf8c1fe6-fb9e-436f-883f-cf5ffba90629', 'Default', 'User', '2010-01-01');
