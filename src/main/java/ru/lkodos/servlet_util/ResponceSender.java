package ru.lkodos.servlet_util;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponceSender {

    public static void send(HttpServletResponse resp, Object obj) throws IOException {
        String answer = new Gson().toJson(obj);
        resp.getWriter().write(answer);
    }
}