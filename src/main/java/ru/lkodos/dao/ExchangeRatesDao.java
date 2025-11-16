package ru.lkodos.dao;

import ru.lkodos.db_util.ConnectionManager;
import ru.lkodos.entity.FullExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements Dao<String, FullExchangeRate> {

    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    private static final String GET_ALL_EXCHANGE_RATES_SQL = """
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
                        """;
    private static final String GET_SPEC_EXCHANGE_RATE_SQL = """
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
                        WHERE base_code = ? AND target_code = ?
                        """;

    private ExchangeRatesDao() {
    }

    @Override
    public List<FullExchangeRate> getAll() {
        try (var connection = ConnectionManager.getConnection();
             var ps = connection.prepareStatement(GET_ALL_EXCHANGE_RATES_SQL)) {

            List<FullExchangeRate> fullExchangeRates = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fullExchangeRates.add(buildFullExchangeRate(rs));
            }
            return fullExchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<FullExchangeRate> getFullExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) {
        try (var connection = ConnectionManager.getConnection();
             var ps = connection.prepareStatement(GET_SPEC_EXCHANGE_RATE_SQL)) {

            ps.setString(1, baseCurrencyCode);
            ps.setString(2, targetCurrencyCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(buildFullExchangeRate(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FullExchangeRate> get(String key) {
        return Optional.empty();
    }

    @Override
    public FullExchangeRate save(FullExchangeRate entity) {
        return null;
    }

    private FullExchangeRate buildFullExchangeRate(ResultSet resultSet) {
        try {
            return FullExchangeRate.builder()
                    .id(resultSet.getInt("id"))
                    .baseCurrencyId(resultSet.getInt("base_id"))
                    .baseCurrencyCode(resultSet.getString("base_code"))
                    .baseCurrencyName(resultSet.getString("base_name"))
                    .baseCurrencySign(resultSet.getString("base_sign"))
                    .targetCurrencyId(resultSet.getInt("target_id"))
                    .targetCurrencyCode(resultSet.getString("target_code"))
                    .targetCurrencyName(resultSet.getString("target_name"))
                    .targetCurrencySign(resultSet.getString("target_sign"))
                    .rate(resultSet.getBigDecimal("rate"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExchangeRatesDao getInstance() {
        return INSTANCE;
    }
}
