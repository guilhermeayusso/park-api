package br.com.fiap.parkapi.web.controller;


import br.com.fiap.parkapi.entity.Veiculo;
import br.com.fiap.parkapi.exception.ErrorMessage;
import br.com.fiap.parkapi.service.VeiculoService;
import br.com.fiap.parkapi.web.dto.mapper.UsuarioMapper;
import br.com.fiap.parkapi.web.dto.mapper.VeiculoMapper;
import br.com.fiap.parkapi.web.dto.request.EstacionamentoCreateDto;
import br.com.fiap.parkapi.web.dto.request.VeiculoCreateDto;
import br.com.fiap.parkapi.web.dto.response.ClienteResponseDto;
import br.com.fiap.parkapi.web.dto.response.UsuarioCreateResponseDto;
import br.com.fiap.parkapi.web.dto.response.VeiculoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Veículos", description = "Operações de registro de Veículos.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Operation(summary = "Criar um novo cliente",
            description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente CPF já possui cadastro no sistema",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<VeiculoResponseDto> create (@RequestBody @Valid VeiculoCreateDto dto){
        Veiculo veiculo = veiculoService.salvar(VeiculoMapper.toVeiculo(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(VeiculoMapper.toDto(veiculo));
    }

    @Operation(summary = "Localizar um Veiculo", description = "Recurso para localizar um cliente pelo ID. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = VeiculoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{cpf}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<VeiculoResponseDto>> getAllByCpf(@PathVariable String cpf){
        List<Veiculo> veiculos = veiculoService.buscarTodosVeiculosPorCliente(cpf);
        return ResponseEntity.ok(VeiculoMapper.toListDto(veiculos));
    }

    @Operation(summary = "Deletar veículos cadastrados",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Recurso recuperado com sucesso"),
                    @ApiResponse(responseCode = "409", description = "Veiculo não encontrado",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @DeleteMapping("/{placa}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String placa){
        veiculoService.deletarVeiculo(placa);
        return ResponseEntity.noContent().build();
    }
}
