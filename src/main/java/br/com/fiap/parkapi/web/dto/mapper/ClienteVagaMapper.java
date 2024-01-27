package br.com.fiap.parkapi.web.dto.mapper;

import br.com.fiap.parkapi.entity.ClienteVaga;
import br.com.fiap.parkapi.web.dto.request.EstacionamentoCreateDto;
import br.com.fiap.parkapi.web.dto.response.EstacionamentoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toDto(ClienteVaga clienteVaga) {
        return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
    }
}
