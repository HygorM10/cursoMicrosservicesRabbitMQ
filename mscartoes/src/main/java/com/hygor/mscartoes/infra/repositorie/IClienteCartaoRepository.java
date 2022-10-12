package com.hygor.mscartoes.infra.repositorie;

import com.hygor.mscartoes.domain.CartaoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClienteCartaoRepository extends JpaRepository<CartaoCliente, Long> {
    List<CartaoCliente> findByCpf(String cpf);
}
