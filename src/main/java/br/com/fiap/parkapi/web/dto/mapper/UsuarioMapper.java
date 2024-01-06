package br.com.fiap.parkapi.web.dto.mapper;

import br.com.fiap.parkapi.entity.Usuario;
import br.com.fiap.parkapi.web.dto.request.UsuarioCreateRequestDto;
import br.com.fiap.parkapi.web.dto.response.UsuarioCreateResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;


import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    public static Usuario toUsuario (UsuarioCreateRequestDto dto){
        return new ModelMapper().map(dto, Usuario.class);
    }

    public static UsuarioCreateResponseDto toDto (Usuario usuario){
       String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuario, UsuarioCreateResponseDto> props = new PropertyMap<Usuario, UsuarioCreateResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario,UsuarioCreateResponseDto.class);
    }

    public static List<UsuarioCreateResponseDto> toListDto(List<Usuario> usuarios){
        return usuarios.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}
