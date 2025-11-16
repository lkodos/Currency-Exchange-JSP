package ru.lkodos.dao;

import ru.lkodos.db_util.ConnectionManager;
import ru.lkodos.entity.Currency;
import ru.lkodos.exception.CurrencyAlreadyExistsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<String, Currency> {

    private static final   CurrencyDao INSTANCE = new CurrencyDao();

    private static final String GET_ALL_SQL = "SELECT id, code, full_name, sign FROM currency";
    private static final String GET_BY_CODE_SQL = "SELECT id, code, full_name, sign FROM currency WHERE code = ?";
    private static final String SAVE_NEW_CURRENCY_SQL = "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)";

    private CurrencyDao() {
    }

    @Override
    public Currency save(Currency entity) {
        try (var connection = ConnectionManager.getConnection();
             var ps = connection.prepareStatement(SAVE_NEW_CURRENCY_SQL)) {

            ps.setString(1, entity.getCode());
            ps.setString(2, entity.getFullName());
            ps.setString(3, entity.getSign());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            entity.setId(generatedKeys.getInt(1));
            return entity;
        } catch (SQLException e) {
            throw new CurrencyAlreadyExistsException("Currency already exists!", e);
        }
    }

    @Override
    public Optional<Currency> get(String code) {
        try (var connection = ConnectionManager.getConnection();
             var ps = connection.prepareStatement(GET_BY_CODE_SQL)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(buildCurrency(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> getAll() {
        try (var connection = ConnectionManager.getConnection();
             var ps = connection.prepareStatement(GET_ALL_SQL)) {

            List<Currency> currencies = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currencies.add(buildCurrency(rs));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Currency entity) {

    }

    public static CurrencyDao getInstance(){
        return INSTANCE;
    }

    private Currency buildCurrency(ResultSet rs) {
        try {
            return Currency.builder()
                    .id(rs.getInt("id"))
                    .code(rs.getString("code"))
                    .fullName(rs.getString("full_name"))
                    .sign(rs.getString("sign"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}