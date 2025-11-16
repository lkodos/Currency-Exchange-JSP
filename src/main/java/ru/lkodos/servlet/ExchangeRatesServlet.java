package ru.lkodos.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.lkodos.dao.ExchangeRatesDao;
import ru.lkodos.dto.ExchangeRateDto;
import ru.lkodos.entity.FullExchangeRate;
import ru.lkodos.mapper.MapperUtil;
import ru.lkodos.servlet_util.ResponceSender;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/exchangeRates"}, name = "ExchangeRatesServlet")
public class ExchangeRatesServlet extends HttpServlet {

    private static final ExchangeRatesDao exchangeRateDao = ExchangeRatesDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<FullExchangeRate> allExchangeRates = exchangeRateDao.getAll();
        List<ExchangeRateDto> exchangeRateDto = MapperUtil.mapList(allExchangeRates, ExchangeRateDto.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        ResponceSender.send(resp, exchangeRateDto);
    }
}
