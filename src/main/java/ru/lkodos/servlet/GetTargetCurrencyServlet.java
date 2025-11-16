package ru.lkodos.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.lkodos.dao.CurrencyDao;
import ru.lkodos.dto.CurrencyDto;
import ru.lkodos.entity.Currency;
import ru.lkodos.exception.CurrencyNotFoundException;
import ru.lkodos.mapper.MapperUtil;
import ru.lkodos.servlet_util.ResponceSender;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/currency/*"}, name = "GetTargetCurrencyServlet")
public class GetTargetCurrencyServlet extends HttpServlet {

    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = (String) req.getAttribute("code");
        Optional<Currency> currency = currencyDao.get(code);
        if (currency.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            CurrencyDto currencyDto = MapperUtil.map(currency.get(), CurrencyDto.class);
            ResponceSender.send(resp, currencyDto);
        } else {
            throw new CurrencyNotFoundException("Currency not found!");
        }
    }
}
