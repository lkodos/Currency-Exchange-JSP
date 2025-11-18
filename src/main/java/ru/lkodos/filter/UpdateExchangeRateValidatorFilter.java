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
        if ("PATCH".equalsIgnoreCase((request).getMethod())) {
            String rate;
            String fullCode;
            try {
                fullCode = request.getPathInfo().substring(1).toUpperCase();
                rate = request.getParameter("rate");
            } catch (Exception e) {
                throw new IllegalArgumentException("Required form field is missing");
            }

            if (rate == null || rate.isEmpty()) {
                throw new IllegalArgumentException("Required form field is missing");
            }

            if (fullCode.length() != 6 || !(requestValidator.isLetter(fullCode))) {
                throw new IllegalArgumentException("Invalid currency code. Currency code must consist of three Latin letters!");
            }
            String baseCurrencyCode = fullCode.substring(0, 3);
            String targetCurrencyCode = fullCode.substring(3);
            request.setAttribute("baseCurrencyCode", baseCurrencyCode);
            request.setAttribute("targetCurrencyCode", targetCurrencyCode);
            request.setAttribute("rate", rate);
        }
        filterChain.doFilter(request, servletResponse);
    }
}