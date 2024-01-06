package br.com.fiap.parkapi.web.dto.response;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCreateResponseDto {

    private Long id;
    private String username;
    private String role;
}
