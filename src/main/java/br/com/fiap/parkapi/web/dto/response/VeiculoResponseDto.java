package br.com.fiap.parkapi.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VeiculoResponseDto {
    private String placa;
    private String marca;
    private String modelo;
    private String cor;
    private String clienteCpf;
}