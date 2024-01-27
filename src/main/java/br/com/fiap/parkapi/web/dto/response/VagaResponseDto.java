package br.com.fiap.parkapi.web.dto.response;

import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VagaResponseDto {
    private Long id;
    private String codigo;
    private String status;
}
