package ru.lkodos.dao;

import java.util.List;

public interface Dao<K, T> {

    List<T> getAll();
}