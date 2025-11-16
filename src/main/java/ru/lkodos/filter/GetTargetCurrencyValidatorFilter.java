package ru.lkodos.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import ru.lkodos.servlet_util.RequestValidator;

import java.io.IOException;

@WebFilter(servletNames = {"GetTargetCurrencyServlet"})
public class GetTargetCurrencyValidatorFilter implements Filter {

    private static final RequestValidator requestValidator = RequestValidator.getInstance();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String code;
        try {
            code = request.getPathInfo().substring(1).toUpperCase();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid specific currency code");
        }
        System.out.println("GetTargetCurrencyServlet code: " + code);
        if ((code.length() != 3) || !(requestValidator.isLetter(code))) {
            throw new IllegalArgumentException("Invalid specific currency code");
        }
        request.setAttribute("code", code);
        filterChain.doFilter(request, servletResponse);
    }
}