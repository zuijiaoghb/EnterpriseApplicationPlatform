package com.enterprise.platform.inventorymanagement.model.dto;

import java.util.List;

/**
 * 分页结果DTO
 */
public class PageResultDTO<T> {
    private long total;          // 总记录数
    private List<T> records;     // 当前页数据列表

    public PageResultDTO() {
    }

    public PageResultDTO(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    // Getters and Setters
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}