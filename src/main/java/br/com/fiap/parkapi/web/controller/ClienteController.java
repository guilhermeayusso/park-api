package br.com.fiap.parkapi.web.controller;

import br.com.fiap.parkapi.entity.Cliente;
import br.com.fiap.parkapi.jwt.JwtUserDetails;
import br.com.fiap.parkapi.service.ClienteService;
import br.com.fiap.parkapi.service.UsuarioService;
import br.com.fiap.parkapi.web.dto.mapper.ClienteMapper;
import br.com.fiap.parkapi.web.dto.request.ClienteCreateDto;
import br.com.fiap.parkapi.web.dto.response.ClienteResponseDto;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }
}
