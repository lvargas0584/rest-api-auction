package com.auction.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AdminUserDto {
    @NonNull
    @NotBlank
    private String user;
    @NonNull
    @NotBlank
    private String pass;
}
