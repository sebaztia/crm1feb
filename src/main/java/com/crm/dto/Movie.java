package com.crm.dto;

import lombok.Data;

public @Data class Movie {
    private Long id;
    private String title;
    private String info;

    public Movie() { }
    public Movie(String info, String title) { this.info = info; this.title = title;}
}
