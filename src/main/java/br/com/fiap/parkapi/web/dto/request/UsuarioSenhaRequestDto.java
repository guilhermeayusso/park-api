package br.com.fiap.parkapi.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaRequestDto {

    @NotBlank
    @Size(min = 6, max = 12, message = "Verifique o tamanho da senha.")
    private String senhaAtual;
    @NotBlank
    @Size(min = 6, max = 12, message = "Verifique o tamanho da senha.")
    private String novaSenha;
    @NotBlank
    @Size(min = 6, max = 12, message = "Verifique o tamanho da senha.")
    private String confirmaSenha;

}
