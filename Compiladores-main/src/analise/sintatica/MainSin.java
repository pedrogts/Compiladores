package analise.sintatica;

import analise.lexico.Lexico;
import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;
import java.util.stream.Collectors;

public class MainSin {
    public static void main(String[] args) {
        String codigoFonte = "metodo principal(): int {  x: int;    x = 5; se (x > 0) {        devolva x;    } senao {        devolva 0;    }} metodo soma(a: int, b: int): int {  devolva a + b;}";


        Lexico lexico = new Lexico();
        lexico.defCodFonte(codigoFonte);
        List<Token> tokens = lexico.anLex();

        List<TiposToken> tiposTokens = tokens.stream()
                .map(t -> t.tipo)
                .collect(Collectors.toList());

        Sintatico sintatico = new Sintatico();

        try {
            sintatico.S(tiposTokens);
            System.out.println("Analise Sintatica Concluida com sucesso.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
