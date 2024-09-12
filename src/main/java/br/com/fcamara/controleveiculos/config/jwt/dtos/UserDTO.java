package br.com.fcamara.controleveiculos.config.jwt.dtos;

import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {
	private String username;
    private String password;
    private Set<String> roles;
}