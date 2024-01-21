package br.com.fiap.parkapi.web.controller;

import br.com.fiap.parkapi.entity.Usuario;
import br.com.fiap.parkapi.exception.ErrorMessage;
import br.com.fiap.parkapi.service.UsuarioService;
import br.com.fiap.parkapi.web.dto.mapper.UsuarioMapper;
import br.com.fiap.parkapi.web.dto.request.UsuarioCreateRequestDto;
import br.com.fiap.parkapi.web.dto.request.UsuarioSenhaRequestDto;
import br.com.fiap.parkapi.web.dto.response.UsuarioCreateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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


@Tag(name = "Usuarios", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo usuário", description = "Recurso para criar um novo usuário",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioCreateResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<UsuarioCreateResponseDto> create(@Valid @RequestBody UsuarioCreateRequestDto usuarioCreateRequestDto) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioCreateRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }



    @GetMapping("/{id}")    @Operation(summary = "Recuperar um usuário pelo id", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN|CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioCreateResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioCreateResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Atualizar senha", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN|CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> UpdatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaRequestDto passUser) {
        Usuario user = usuarioService.editarSenha(id, passUser.getSenhaAtual(),
                passUser.getNovaSenha(), passUser.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os usuários cadastrados", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista com todos os usuários cadastrados",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UsuarioCreateResponseDto.class)))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioCreateResponseDto>> getAll() {
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }
}
