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
}