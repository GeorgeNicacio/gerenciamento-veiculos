package br.com.fcamara.controleveiculos.exceptions;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import reactor.core.publisher.Mono;

@Component
public class CustomGraphQLErrorHandler extends DataFetcherExceptionResolverAdapter {

    // Remova o @Override
    protected Mono<GraphQLError> resolveException(Throwable ex, DataFetcherExceptionHandlerParameters params) {
        // Retorna a mensagem de erro personalizada
        return Mono.just(
            GraphqlErrorBuilder.newError()
                .message(ex.getMessage())  // Exibe a mensagem real do erro
                .path(params.getPath())    // Inclui o caminho onde o erro ocorreu
                .location(params.getSourceLocation()) // Inclui a localização da fonte do erro
                .build()
        );
    }
}