use pragmatic;

CREATE TABLE IF NOT EXISTS users
(
    user_id    UUID PRIMARY KEY,
    first_name varchar,
    last_name  varchar,
    birth_day  varchar,
) WITH gc_grace_seconds = 86400;
