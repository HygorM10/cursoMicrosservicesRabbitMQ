package com.hygor.mscartoes.application;

import com.hygor.mscartoes.application.representation.CartoesPorClieteResponse;
import com.hygor.mscartoes.domain.Cartao;
import com.hygor.mscartoes.domain.CartaoCliente;
import com.hygor.mscartoes.representation.CartaoSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesController {

    private final CartaoService cartaoService;
    private final CartaoClienteService clienteService;

    @GetMapping
    public String status(){
        return "OK";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request){
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda){
        List<Cartao> list = cartaoService.getCartaoRendaMenor(renda);
        return ResponseEntity.ok(list);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClieteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf){
        List<CartaoCliente> lista = clienteService.listCartoesByCpf(cpf);
        List<CartoesPorClieteResponse> resultList = lista.stream().map(CartoesPorClieteResponse::fromModel)
                                                        .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }

}
