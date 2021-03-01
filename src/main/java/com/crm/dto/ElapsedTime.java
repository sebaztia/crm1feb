package com.crm.dto;

import lombok.Data;

public @Data
class ElapsedTime {
    private long day;
    private long hours;

    public ElapsedTime(long day, long hours) {
        this.day = day;
        this.hours = hours;
    }

    public ElapsedTime() { }
}
