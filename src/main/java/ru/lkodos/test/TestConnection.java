package ru.lkodos.test;

import ru.lkodos.db_util.ConnectionManager;
import ru.lkodos.exception.DbAccessException;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try {
            Connection connection = ConnectionManager.getConnection();
            System.out.println("CONNECTION SUCCESS");
        } catch (DbAccessException e) {
            System.out.println("CONNECTION FAILURE");
            System.out.println(e.getMessage());
        }
    }
}