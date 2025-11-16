package ru.lkodos.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.lkodos.dao.CurrencyDao;
import ru.lkodos.dto.CurrencyDto;
import ru.lkodos.entity.Currency;
import ru.lkodos.mapper.MapperUtil;
import ru.lkodos.servlet_util.ResponceSender;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/currencies"}, name = "CurrencyServlet")
public class CurrencyServlet extends HttpServlet {

    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Currency> currencies = currencyDao.getAll();
        List<CurrencyDto> currencyDtoList = MapperUtil.mapList(currencies, CurrencyDto.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        ResponceSender.send(resp, currencyDtoList);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = (String) req.getAttribute("name");
        String code = (String) req.getAttribute("code");
        String sign = (String) req.getAttribute("sign");

        Currency currency = Currency.builder()
                .code(code)
                .fullName(name)
                .sign(sign)
                .build();

        Currency savedCurrency = currencyDao.save(currency);
        CurrencyDto currencyDto = MapperUtil.map(savedCurrency, CurrencyDto.class);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        ResponceSender.send(resp, currencyDto);
    }
}