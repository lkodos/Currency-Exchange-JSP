package ru.lkodos.mapper;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MapperUtil {

    private MapperUtil() {
    }

    public static <D, T> List<D> mapList(List<T> sourceList, Class<D> target) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(source -> MapperConfig.getMapper().map(source, target))
                .collect(toList());
    }

    public static <D, T> D map(T source, Class<D> destinationClass) {
        return MapperConfig.getMapper().map(source, destinationClass);
    }
}