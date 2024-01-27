package br.com.fiap.parkapi.service;

import br.com.fiap.parkapi.entity.Cliente;
import br.com.fiap.parkapi.entity.ClienteVaga;
import br.com.fiap.parkapi.entity.Vaga;
import br.com.fiap.parkapi.entity.Veiculo;
import br.com.fiap.parkapi.exception.EntityNotFoundException;
import br.com.fiap.parkapi.exception.ParkingNotAllowedException;
import br.com.fiap.parkapi.util.EstacionamentoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;
    private final VeiculoService veiculoService;

    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga) {
        Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf());
        Veiculo veiculo = veiculoService.buscarPorPlaca(clienteVaga.getPlaca());

        if(veiculo.getCliente().getCpf() != cliente.getCpf()){
            throw new ParkingNotAllowedException("Veículo não pertence ao Cliente");
        }

        clienteVaga.setCliente(cliente);

        clienteVaga.setCor(veiculo.getCor());
        clienteVaga.setMarca(veiculo.getMarca());
        clienteVaga.setPlaca(veiculo.getPlaca());
        clienteVaga.setModelo(veiculo.getModelo());

        if(clienteVaga.getDataSaida() != null){
            if(clienteVaga.getDataSaida().isBefore(LocalDateTime.now())){
                throw new ParkingNotAllowedException("Para horário fixo a data de saída precisa ser maior que a data de entrada");
            }

            if(cliente.getTipoPagamento().toString() != "PIX"){
                throw new ParkingNotAllowedException("Para horário fixo somenta pagamento com PIX");
            }
        }

        Vaga vaga = vagaService.buscarPorVagaLivre();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());

        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());
        clienteVaga.setCheckout(false);

        return clienteVagaService.salvar(clienteVaga);
    }

    @Transactional
    public ClienteVaga checkOut(String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        LocalDateTime dataSaida = null;
        if(clienteVaga.isCheckout() == true){
            throw new EntityNotFoundException(String.format("Recibo '%s' checkout já realizado", recibo));
        }

        if(clienteVaga.getDataSaida() ==null){
             dataSaida = LocalDateTime.now();
        }else{
            dataSaida = clienteVaga.getDataSaida();
        }

        BigDecimal valor = EstacionamentoUtils.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);
        clienteVaga.setValor(valor);

        long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf());

        BigDecimal desconto = EstacionamentoUtils.calcularDesconto(valor, totalDeVezes);
        clienteVaga.setDesconto(desconto);

        clienteVaga.setDataSaida(dataSaida);
        clienteVaga.getVaga().setStatus(Vaga.StatusVaga.LIVRE);
        clienteVaga.setCheckout(true);
        return clienteVagaService.salvar(clienteVaga);
    }
}
