package com.hygor.mscartoes.application;

import com.hygor.mscartoes.domain.CartaoCliente;
import com.hygor.mscartoes.infra.repositorie.ICartaoRepository;
import com.hygor.mscartoes.infra.repositorie.IClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoClienteService {

    private final IClienteCartaoRepository repository;

    public List<CartaoCliente> listCartoesByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

}
