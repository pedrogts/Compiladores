package analise.sintatica;

import analise.lexico.Lexico;
import analise.lexico.Token;

import java.util.List;

public class MainSin {
    public static void main(String[] args) {
        String expressao = "1 + 2.5 - 3 / 34 * 4";

        Lexico lexico = new Lexico();
        lexico.defCodFonte(expressao);
        List<Token> tokens = lexico.anLex();

        Sintatico sintatico = new Sintatico();

        try {
            sintatico.anSin(tokens);
            System.out.println("Análise sintática concluída com sucesso.");
            System.out.println("Árvore sintática: " + sintatico.arvore);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
