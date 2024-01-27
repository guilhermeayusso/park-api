package br.com.fiap.parkapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "veiculos")
@EntityListeners(AuditingEntityListener.class)
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "placa", nullable = false, unique = true ,length = 8)
    private String placa;
    @Column(name = "marca", nullable = false, length = 45)
    private String marca;
    @Column(name = "modelo", nullable = false, length = 45)
    private String modelo;
    @Column(name = "cor", nullable = false, length = 45)
    private String cor;
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @CreatedDate
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @CreatedBy
    @Column(name = "criado_por")
    private  String criadoPor;

    @LastModifiedBy
    @Column(name = "modificado_por")
    private String modificadoPor;
}
