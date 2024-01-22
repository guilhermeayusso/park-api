package br.com.fiap.parkapi.service;

import br.com.fiap.parkapi.entity.Cliente;
import br.com.fiap.parkapi.exception.CpfUniqueViolationException;
import br.com.fiap.parkapi.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar (Cliente cliente){
        try {
            return clienteRepository.save(cliente);
        }catch (DataIntegrityViolationException ex){
            throw new CpfUniqueViolationException(String.format("CPF '%s' não pode ser cadastrado, já existe no sistema", cliente.getCpf()));
        }
    }
}
