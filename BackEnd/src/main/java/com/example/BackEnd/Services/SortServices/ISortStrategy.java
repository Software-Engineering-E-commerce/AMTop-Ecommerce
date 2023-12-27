package com.example.BackEnd.Services.SortServices;

import java.util.List;

public interface ISortStrategy<T extends Comparable<T>> {
    public List<T> sort(String sortBy, boolean sortOrder);
}
