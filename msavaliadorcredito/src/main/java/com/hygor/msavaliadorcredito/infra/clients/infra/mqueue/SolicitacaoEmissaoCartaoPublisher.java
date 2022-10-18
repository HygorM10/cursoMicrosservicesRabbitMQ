package com.hygor.msavaliadorcredito.infra.clients.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hygor.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartoes;

    public void solicitarCartao(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException{
        var json = convertIntoToJson(dados);
        rabbitTemplate.convertAndSend(queueEmissaoCartoes.getName(), json);
    }

    private String convertIntoToJson(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dados);
    }
}
