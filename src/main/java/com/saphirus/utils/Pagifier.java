package com.saphirus.utils;


import java.util.ArrayList;
import java.util.List;

public class Pagifier<T>
{
    private int pageSize;
    private List<List<T>> pages;

    public Pagifier(final int pageSize) {
        this.pageSize = pageSize;
        (this.pages = new ArrayList<List<T>>()).add(new ArrayList<T>());
    }

    public void addItem(final T item) {
        final int pageNum = this.pages.size() - 1;
        List<T> currentPage = this.pages.get(pageNum);
        if (currentPage.size() >= this.pageSize) {
            currentPage = new ArrayList<T>();
            this.pages.add(currentPage);
        }
        currentPage.add(item);
    }

    public List<T> getPage(final int pageNum) {
        if (this.pages.size() == 0 || this.pages.get(pageNum) == null) {
            return null;
        }
        return this.pages.get(pageNum);
    }

    public List<List<T>> getPages() {
        return this.pages;
    }

    public int getPageSize() {
        return this.pageSize;
    }
}

