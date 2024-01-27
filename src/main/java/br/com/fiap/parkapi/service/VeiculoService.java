package br.com.fiap.parkapi.service;

import br.com.fiap.parkapi.entity.Cliente;
import br.com.fiap.parkapi.entity.Veiculo;
import br.com.fiap.parkapi.exception.ClientNotFoundException;
import br.com.fiap.parkapi.exception.CodigoUniqueViolationException;
import br.com.fiap.parkapi.exception.VeiculoNotFoundException;
import br.com.fiap.parkapi.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteService clienteService;

    @Transactional
    public Veiculo salvar(Veiculo veiculo) {

        Cliente cliente = clienteService.buscarPorCpf(veiculo.getCliente().getCpf());
        veiculo.setCliente(cliente);
        try {
            return veiculoRepository.save(veiculo);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(
                    String.format("Veiculo com a placa '%s' já cadastrada", veiculo.getPlaca())
           );
        }
    }

    @Transactional(readOnly = true)
    public List<Veiculo> buscarTodosVeiculosPorCliente(String cpf){
        Cliente cliente = clienteService.buscarPorCpf(cpf);
        return veiculoRepository.findByIdCliente(cliente);
    }

    @Transactional(readOnly = true)
    public Veiculo buscarPorPlaca(String placa) {
        return veiculoRepository.findByPlaca(placa).orElseThrow(
                () -> new VeiculoNotFoundException(String.format("Veiculo com placa=%s não encontrado no sistema", placa))
        );
    }

    @Transactional
    public void deletarVeiculo(String placa){
        Veiculo veiculo = buscarPorPlaca(placa);
        veiculoRepository.delete(veiculo);
    }

}
