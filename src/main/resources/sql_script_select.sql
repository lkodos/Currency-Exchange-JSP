SELECT e.id AS id,
       e.base_currency_id AS base_id,
       c.code AS base_code,
       c.full_name AS base_name,
       c.sign AS base_sign,
       e.target_currency_id AS target_id,
       c2.code AS target_code,
       c2.full_name AS target_name,
       c2.sign AS target_sign,
       e.rate AS rate
FROM exchange_rates e
JOIN currency c ON c.id = e.base_currency_id
JOIN currency c2 ON c2.id = e.target_currency_id
WHERE base_code = 'RUB' AND target_code = 'USD';