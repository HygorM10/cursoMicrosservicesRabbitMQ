package com.hygor.msavaliadorcredito.ex;

import lombok.Getter;
public class ErroComunicacaoMicrosservicesExceptions extends Exception {

    @Getter
    private Integer status;

    public ErroComunicacaoMicrosservicesExceptions(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
