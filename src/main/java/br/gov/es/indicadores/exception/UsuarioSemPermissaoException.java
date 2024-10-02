package br.gov.es.indicadores.exception;

public class UsuarioSemPermissaoException extends RuntimeException {

    public UsuarioSemPermissaoException() {
        super("Usuário sem permissão.");
    }
}
