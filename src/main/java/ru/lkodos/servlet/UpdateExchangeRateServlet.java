package ru.lkodos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.lkodos.dao.CurrencyDao;
import ru.lkodos.dao.ExchangeRatesDao;
import ru.lkodos.dto.ExchangeRateDto;
import ru.lkodos.entity.Currency;
import ru.lkodos.entity.FullExchangeRate;
import ru.lkodos.exception.CurrencyNotFoundException;
import ru.lkodos.mapper.MapperUtil;
import ru.lkodos.servlet_util.ResponceSender;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(urlPatterns = {"/exchangeRateq/*"}, name = "UpdateExchangeRateServlet")
public class UpdateExchangeRateServlet extends HttpServlet {

    private static final ExchangeRatesDao exchangeRateDao = ExchangeRatesDao.getInstance();
    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = (String) req.getAttribute("baseCurrencyCode");
        String targetCurrencyCode = (String) req.getAttribute("targetCurrencyCode");
        String rate = (String) req.getAttribute("rate");

        Optional<Currency> baseCurrency = currencyDao.get(baseCurrencyCode);
        Optional<Currency> targetCurrency = currencyDao.get(targetCurrencyCode);

        if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
            Integer baseCurrencyId = baseCurrency.get().getId();
            Integer targetCurrencyId = targetCurrency.get().getId();
            exchangeRateDao.update(BigDecimal.valueOf(Double.parseDouble(rate)), baseCurrencyId, targetCurrencyId);
            Optional<FullExchangeRate> exchangeRate = exchangeRateDao.getFullExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate.isPresent()) {
                ExchangeRateDto exchangeRateDto = MapperUtil.map(exchangeRate.get(), ExchangeRateDto.class);
                resp.setStatus(HttpServletResponse.SC_OK);
                ResponceSender.send(resp, exchangeRateDto);
            }
        }
        else {
            throw new CurrencyNotFoundException("One (or both) currencies from the currency pair do not exist in the database");
        }
    }
}
