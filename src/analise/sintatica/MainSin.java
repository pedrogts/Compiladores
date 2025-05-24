package analise.sintatica;

import analise.lexico.Lexico;
import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;
import java.util.stream.Collectors;

public class MainSin {
    public static void main(String[] args) {
        String expressao = "1 + 2.5 - 3"; 
        Lexico lexico = new Lexico();
        lexico.defCodFonte(expressao);
        List<Token> tokens = lexico.anLex();

        List<TiposToken> tiposTokens = tokens.stream()
                .map(t -> t.tipo)
                .collect(Collectors.toList());

        Sintatico sintatico = new Sintatico();

        try {
            sintatico.Lst(tiposTokens);
            System.out.println("Análise sintática concluída com sucesso.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
