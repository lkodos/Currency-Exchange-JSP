PRAGMA foreign_keys = on;

DROP TABLE IF EXISTS exchange_rates;
DROP TABLE IF EXISTS currency;

CREATE TABLE IF NOT EXISTS currency
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT UNIQUE NOT NULL,
    full_name TEXT        NOT NULL,
    sign TEXT
);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id INTEGER NOT NULL REFERENCES currency (id) ON DELETE CASCADE,
    target_currency_id INTEGER NOT NULL REFERENCES currency (id) ON DELETE CASCADE,
    rate NUMERIC,
    UNIQUE (base_currency_id, target_currency_id)
);

INSERT INTO currency(code, full_name, sign)
VALUES ('RUB', 'Russian Ruble', '₽'),
       ('EUR', 'Euro', '€'),
       ('USD', 'US Dollar', '$');

INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
VALUES (1, 2, 2),
       (1, 3, 0.5),
       (2, 3, 1.4);