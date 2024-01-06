package br.com.fiap.parkapi.service;

import br.com.fiap.parkapi.entity.Usuario;
import br.com.fiap.parkapi.exception.PasswordNotCheckException;
import br.com.fiap.parkapi.exception.UserNotFoundException;
import br.com.fiap.parkapi.exception.UsernameUniqueViolationException;
import br.com.fiap.parkapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario){
        try {
            return usuarioRepository.save(usuario);
        }catch (DataIntegrityViolationException ex){
            throw new UsernameUniqueViolationException(
                    String.format("Username {%s} já cadastrado",usuario.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Usuário id=%s não encontrado",id)));
    }
    @Transactional
    public Usuario editarSenha (Long id, String senhaAtual, String novaSenha, String confirmaSenha){
        if(!novaSenha.equals(confirmaSenha)){
            throw new PasswordNotCheckException("Nova senha não confere com a confirmação de senha.");
        }

        Usuario user = buscarPorId(id);

        if(!user.getPassword().equals(senhaAtual)){
            throw new PasswordNotCheckException("Sua senha não confere com a senha cadastrada.");
        }

        user.setPassword(novaSenha);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }

}
