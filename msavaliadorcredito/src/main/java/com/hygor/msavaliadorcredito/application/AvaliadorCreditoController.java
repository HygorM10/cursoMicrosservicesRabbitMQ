package com.hygor.msavaliadorcredito.application;

import com.hygor.msavaliadorcredito.domain.model.DadosAvaliacao;
import com.hygor.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import com.hygor.msavaliadorcredito.domain.model.SituacaoCliete;
import com.hygor.msavaliadorcredito.ex.DadosClienteNotFoundException;
import com.hygor.msavaliadorcredito.ex.ErroComunicacaoMicrosservicesExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorClienteService avaliadorClienteService;

    @GetMapping
    public String status(){
        return "OK";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {

        try {
            SituacaoCliete situacaoCliete = avaliadorClienteService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliete);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicrosservicesExceptions e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao) {

        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorClienteService.realizarAvaliacao(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicrosservicesExceptions e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }

    }

}
