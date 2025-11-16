package ru.lkodos.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.lkodos.dao.CurrencyDao;
import ru.lkodos.dao.ExchangeRatesDao;
import ru.lkodos.dto.ExchangeRateDto;
import ru.lkodos.entity.Currency;
import ru.lkodos.entity.ExchangeRate;
import ru.lkodos.entity.FullExchangeRate;
import ru.lkodos.exception.CurrencyNotFoundException;
import ru.lkodos.mapper.MapperUtil;
import ru.lkodos.servlet_util.ResponceSender;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/exchangeRates"}, name = "ExchangeRatesServlet")
public class ExchangeRatesServlet extends HttpServlet {

    private static final ExchangeRatesDao exchangeRateDao = ExchangeRatesDao.getInstance();
    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<FullExchangeRate> allExchangeRates = exchangeRateDao.getAll();
        List<ExchangeRateDto> exchangeRateDto = MapperUtil.mapList(allExchangeRates, ExchangeRateDto.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        ResponceSender.send(resp, exchangeRateDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = (String) req.getAttribute("baseCurrencyCode");
        String targetCurrencyCode = (String) req.getAttribute("targetCurrencyCode");
        String rate = (String) req.getAttribute("rate");

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new IllegalArgumentException("Base code matches target code");
        }

        System.out.println("baseCurrencyCode: " + baseCurrencyCode);
        System.out.println("targetCurrencyCode: " + targetCurrencyCode);
        System.out.println("rate: " + rate);

        Optional<Currency> baseCurrency = currencyDao.get(baseCurrencyCode);
        Optional<Currency> targetCurrency = currencyDao.get(targetCurrencyCode);

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new CurrencyNotFoundException("One (or both) currencies from the currency pair do not exist in the database");
        }
        Integer baseCurrencyId = baseCurrency.get().getId();
        Integer targetCurrencyId = targetCurrency.get().getId();
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrencyId(baseCurrencyId)
                .targetCurrencyId(targetCurrencyId)
                .rate(BigDecimal.valueOf(Double.parseDouble(rate)))
                .build();
        ExchangeRate newExchangeRate = exchangeRateDao.save(exchangeRate);
        ResponceSender.send(resp, newExchangeRate);


    }
}
