package br.com.fiap.parkapi.web.dto.mapper;

import br.com.fiap.parkapi.entity.Vaga;
import br.com.fiap.parkapi.web.dto.request.VagaCreateDto;
import br.com.fiap.parkapi.web.dto.response.VagaResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDto toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
