package ru.lkodos.servlet_util;

public class RequestValidator {

    private static final RequestValidator INSTANCE = new RequestValidator();

    private RequestValidator() {
    }

    public boolean isLetter(String str) {

        char[] charArray = str.toUpperCase().toCharArray();
        for (char ch : charArray) {
            if (!(ch >= 'A' && ch <= 'Z')) {
                return false;
            }
        }
        return true;
    }

    public static RequestValidator getInstance() {
        return INSTANCE;
    }
}