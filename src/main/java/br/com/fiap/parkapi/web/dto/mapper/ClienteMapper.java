package br.com.fiap.parkapi.web.dto.mapper;


import br.com.fiap.parkapi.entity.Cliente;
import br.com.fiap.parkapi.web.dto.request.ClienteCreateDto;
import br.com.fiap.parkapi.web.dto.response.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }
}
