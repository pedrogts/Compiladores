package analise.sintatica;

import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;

public class Sintatico {
    public int curPos = 0;
    public NoArvore arvore;

    public void estadoAnSin(List<Token> tokens) {
        List<TiposToken> tokenLst = tokens.stream().map(t -> t.tipo).toList();
        System.out.println("[curPos = " + curPos + ", tokenLst = " + tokenLst + "]");
    }

    public void erroSin(List<TiposToken> tokenLst, int curPos) {
        String msg = String.format("Erro sintático no token de posição %d: encontrado '%s'\nTokens: %s",
                curPos + 1, tokenLst.get(curPos), tokenLst);
        throw new RuntimeException(msg);
    }

    public void anSin(List<Token> tokens) {
        Grammar gramatica = new Grammar();
        this.arvore = gramatica.S(tokens);
    }

}

