package com.example.BackEnd.Services.FilterService;

import java.util.List;

public interface IFilter<T extends Comparable<T>> {
    public List<T> filter(Object criteria);
}
