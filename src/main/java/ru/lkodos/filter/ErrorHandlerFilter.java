package ru.lkodos.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.lkodos.exception.CurrencyAlreadyExistsException;
import ru.lkodos.exception.CurrencyNotFoundException;
import ru.lkodos.exception.DbAccessException;

import java.io.IOException;

@WebFilter("/*")
public class ErrorHandlerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch (DbAccessException e) {
            handleError(servletRequest, servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
        }
        catch (IllegalArgumentException e) {
            handleError(servletRequest, servletResponse, HttpServletResponse.SC_BAD_REQUEST, e);
        }
        catch (CurrencyNotFoundException e) {
            handleError(servletRequest, servletResponse, HttpServletResponse.SC_NOT_FOUND, e);
        }
        catch (CurrencyAlreadyExistsException e) {
            handleError(servletRequest, servletResponse, HttpServletResponse.SC_CONFLICT, e);
        }

    }

    private void handleError(ServletRequest servletRequest, ServletResponse servletResponse, int status, Exception e) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        httpResponse.setStatus(status);

        httpRequest.setAttribute("errorMessage", e.getMessage());

        String errorPage = getErrorPage(status);
        RequestDispatcher requestDispatcher = httpRequest.getRequestDispatcher(errorPage);
        if (requestDispatcher != null) {
            requestDispatcher.forward(httpRequest, httpResponse);
        } else {
            servletResponse.setContentType("text/html; charset=UTF-8");
            servletResponse.getWriter().println(e.getMessage());
        }
    }

    private String getErrorPage(int status) {
        return switch (status) {
            case 400 -> "/WEB-INF/jsp/400.jsp";
            case 404 -> "/WEB-INF/jsp/404.jsp";
            case 409 -> "/WEB-INF/jsp/409.jsp";
            case 500 -> "/WEB-INF/jsp/500.jsp";
            default -> "/WEB-INF/jsp/defaultErrorPage.jsp";
        };
    }
}