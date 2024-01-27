package br.com.fiap.parkapi.web.dto.mapper;


import br.com.fiap.parkapi.entity.Usuario;
import br.com.fiap.parkapi.entity.Veiculo;
import br.com.fiap.parkapi.web.dto.request.VeiculoCreateDto;
import br.com.fiap.parkapi.web.dto.response.UsuarioCreateResponseDto;
import br.com.fiap.parkapi.web.dto.response.VeiculoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VeiculoMapper {

    public static Veiculo toVeiculo(VeiculoCreateDto dto) {
        return new ModelMapper().map(dto, Veiculo.class);
    }

    public static VeiculoResponseDto toDto(Veiculo veiculo) {
        return new ModelMapper().map(veiculo, VeiculoResponseDto.class);
    }

    public static List<VeiculoResponseDto> toListDto(List<Veiculo> veiculo){
        return veiculo.stream().map(v -> toDto(v)).collect(Collectors.toList());
    }
}
