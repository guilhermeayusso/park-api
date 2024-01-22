package br.com.fiap.parkapi.repository;


import br.com.fiap.parkapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /*@Query("select c from Cliente c")
    #Page<ClienteProjection> findAllPageable(Pageable pageable);*/

    Cliente findByUsuarioId(Long id);

    Optional<Cliente> findByCpf(String cpf);
}