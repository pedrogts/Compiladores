package analise.lexico;

public enum TiposToken {

    //Tokens com um caracter
    PAR_ESQ, PAR_DIR, CHAVE_ESQ, CHAVE_DIR,
    VIRG, PONTO, MENOS, MAIS, PONT_VIRG, MULTI, IGUAL, HASTAG, DOIS_PONTOS,

    //Tokens com um ou dois caracteres
    NOT, DIFERENTE,
    MAIOR, MAIOR_IGUAL,
    MENOR, MENOR_IGUAL, ATRIBUICAO,
    BARRA, BARRA_ASTERISCO,

    //Constantes
    ID, DIGITO_INT, DIGITO_DUP, INT, DUP, CARACTER, BOOLEANO,

    //Palavras-reservadas
    E, SENAO, FALSO, PARA, SE, OU, METODO, VAZIO,
    APRESENTE, DEVOLVA, VERDADEIRO, ENQUANTO, RESTO,
    ENTAO, ESCOLHA, CASO, SAIA, CONTINUE, FACA, REPITA, PRINCIPAL,

    //Fim do lexema
    EOF
}

