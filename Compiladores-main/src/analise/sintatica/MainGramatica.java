package analise.sintatica;

import java.util.Arrays;

public class MainGramatica {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.setGrammar();
        /*// Adiciona a produção Tipo -> Primitivo
        grammar.adicionarProducao("Tipo", new Producoes(Arrays.asList("Primitivo")));

        // Para exemplo: Primitivo -> INT e Primitivo -> DUP
        grammar.adicionarProducao("Primitivo", new Producoes(Arrays.asList("INT")));
        grammar.adicionarProducao("Primitivo", new Producoes(Arrays.asList("DUP")));*/

        // Imprime a gramática
        grammar.printGrammar();
    }
}
