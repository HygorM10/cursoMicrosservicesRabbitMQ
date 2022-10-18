package com.hygor.msavaliadorcredito.application;

import com.hygor.msavaliadorcredito.domain.model.Cartao;
import com.hygor.msavaliadorcredito.domain.model.CartaoAprovado;
import com.hygor.msavaliadorcredito.domain.model.CartaoCliente;
import com.hygor.msavaliadorcredito.domain.model.DadosCliente;
import com.hygor.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import com.hygor.msavaliadorcredito.domain.model.SituacaoCliete;
import com.hygor.msavaliadorcredito.ex.DadosClienteNotFoundException;
import com.hygor.msavaliadorcredito.ex.ErroComunicacaoMicrosservicesExceptions;
import com.hygor.msavaliadorcredito.infra.clients.ICartoesResourceClient;
import com.hygor.msavaliadorcredito.infra.clients.IClienteResourceClient;
import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorClienteService {

    private final IClienteResourceClient clientesClient;

    private final ICartoesResourceClient cartoesCliente;

    public SituacaoCliete obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicrosservicesExceptions{

        try {

            ResponseEntity<DadosCliente> dadosClienteRespose = clientesClient.dadosDoCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesCliente.getCartoesByCliente(cpf);

            return SituacaoCliete
                    .builder()
                    .cliente(dadosClienteRespose.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();

        }catch (FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicrosservicesExceptions(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicrosservicesExceptions{
       try{
           ResponseEntity<DadosCliente> dadosClienteRespose = clientesClient.dadosDoCliente(cpf);
           ResponseEntity<List<Cartao>> cartoesResponse = cartoesCliente.getCartoesRendaAteh(renda);

           List<Cartao> cartoes = cartoesResponse.getBody();
           var listCartoesAprovados = cartoes.stream().map(cartao -> {

               DadosCliente dadosCliente = dadosClienteRespose.getBody();

               BigDecimal limiteBasico = cartao.getLimiteBasico();
               BigDecimal rendaBD = BigDecimal.valueOf(renda);
               BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
               var fator = idadeBD.divide(BigDecimal.valueOf(10));
               BigDecimal limiteAprovado = fator.multiply(limiteBasico);

               CartaoAprovado aprovado = new CartaoAprovado();
               aprovado.setCartao(cartao.getNome());
               aprovado.setBandeira(cartao.getBandeira());
               aprovado.setValorAprovado(limiteAprovado);

               return aprovado;
           }).collect(Collectors.toList());

           return new RetornoAvaliacaoCliente(listCartoesAprovados);

       }catch (FeignClientException e){
           int status = e.status();
           if(HttpStatus.NOT_FOUND.value() == status){
               throw new DadosClienteNotFoundException();
           }
           throw new ErroComunicacaoMicrosservicesExceptions(e.getMessage(), status);
       }

    }

}
