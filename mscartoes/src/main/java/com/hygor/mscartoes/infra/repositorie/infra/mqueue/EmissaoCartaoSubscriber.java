package com.hygor.mscartoes.infra.repositorie.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hygor.mscartoes.domain.Cartao;
import com.hygor.mscartoes.domain.CartaoCliente;
import com.hygor.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import com.hygor.mscartoes.infra.repositorie.ICartaoRepository;
import com.hygor.mscartoes.infra.repositorie.IClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmissaoCartaoSubscriber {

    private final ICartaoRepository cartaoRepository;
    private final IClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload){
        try{

            var mapper = new ObjectMapper();
            DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            Cartao cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();
            CartaoCliente cartaoCliente = new CartaoCliente();
            cartaoCliente.setCartao(cartao);
            cartaoCliente.setCpf(dados.getCpf());
            cartaoCliente.setLimite(dados.getLimiteLiberado());

            clienteCartaoRepository.save(cartaoCliente);

        } catch (Exception e) {
            log.error("Erro ao receber solicitacao de emissao de cartao: {}", e.getMessage());
        }

    }
}
