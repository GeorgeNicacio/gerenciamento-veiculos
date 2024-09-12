package br.com.fcamara.controleveiculos.exceptions;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public GraphQLError handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("Exceção capturada: {}", ex.getMessage());  // Log para verificar se o método está sendo chamado
        // Cria um GraphQLError com a mensagem original da exceção
        return GraphqlErrorBuilder.newError()
            .message(ex.getMessage()) // Exibe a mensagem real do erro
            .extensions(Map.of("classification", "INTERNAL_ERROR")) // Tipo de erro como string
            .build();
    }
}