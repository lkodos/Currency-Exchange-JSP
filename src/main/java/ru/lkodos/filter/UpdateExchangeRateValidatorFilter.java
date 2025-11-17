package ru.lkodos.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import ru.lkodos.servlet_util.RequestValidator;

import java.io.IOException;

@WebFilter(servletNames = {"GetTargetExchangeRatesServlet"})
public class UpdateExchangeRateValidatorFilter implements Filter {

    private static final RequestValidator requestValidator = RequestValidator.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if ("POST".equalsIgnoreCase((request).getMethod())) {
            String baseCurrencyCode;
            String targetCurrencyCode;
            String rate;
            try {
                baseCurrencyCode = request.getParameter("baseCurrencyCode");
                targetCurrencyCode = request.getParameter("targetCurrencyCode");
                rate = request.getParameter("rate");
            } catch (Exception e) {
                throw new IllegalArgumentException("Required form field is missing");
            }

            if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null || baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || rate.isEmpty()) {
                throw new IllegalArgumentException("Required form field is missing");
            }

            if (baseCurrencyCode.length() != 3 || !(requestValidator.isLetter(baseCurrencyCode)) || targetCurrencyCode.length() != 3 || !(requestValidator.isLetter(targetCurrencyCode))) {
                throw new IllegalArgumentException("Invalid currency code. Currency code must consist of three Latin letters!");
            }
            request.setAttribute("baseCurrencyCode", baseCurrencyCode);
            request.setAttribute("targetCurrencyCode", targetCurrencyCode);
            request.setAttribute("rate", rate);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
