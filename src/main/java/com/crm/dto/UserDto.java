package com.crm.dto;

import lombok.Data;

public @Data class UserDto {
    private boolean roleUser;
    private boolean admin;
    private boolean wills;
    private boolean leads;
}
