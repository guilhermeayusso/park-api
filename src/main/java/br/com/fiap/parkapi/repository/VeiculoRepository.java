package br.com.fiap.parkapi.repository;

import br.com.fiap.parkapi.entity.Cliente;
import br.com.fiap.parkapi.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("select v from Veiculo v where v.cliente = :cliente")
    List<Veiculo> findByIdCliente(Cliente cliente);

    Optional<Veiculo> findByPlaca(String placa);
}
