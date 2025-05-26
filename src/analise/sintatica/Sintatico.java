package analise.sintatica;

import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;

public class Sintatico {
    public int curPos = 0;
    public NoArvore arvore;

    public void erroSin(List<TiposToken> tokenLst, int curPos) {
        String msg = String.format(
                "Erro sintático no token de posição %d: encontrado '%s'\nTokens: %s",
                curPos + 1,
                tokenLst.get(curPos),
                tokenLst
        );
        throw new RuntimeException(msg);
    }

    public NoArvore gerarArvMais(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("+");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvMenos(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("-");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvMulti(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("*");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvDiv(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("/");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore num(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        if (tokenAtual.tipo == TiposToken.DIGITO_INT || tokenAtual.tipo == TiposToken.DIGITO_DUP) {
            NoArvore no = new NoArvore(tokenAtual.lexema);
            curPos++;
            return no;
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public void Lst(List<Token> tokens) {
        arvore = num(tokens);

        while (curPos < tokens.size() && tokens.get(curPos).tipo != TiposToken.EOF) {
            TiposToken tokenAtual = tokens.get(curPos).tipo;
            curPos++;
            NoArvore noDireito = num(tokens);

            switch (tokenAtual) {
                case MAIS:
                    arvore = gerarArvMais(arvore, noDireito);
                    break;
                case MENOS:
                    arvore = gerarArvMenos(arvore, noDireito);
                    break;
                case MULTI:
                    arvore = gerarArvMulti(arvore, noDireito);
                    break;
                case BARRA:
                    arvore = gerarArvDiv(arvore, noDireito);
                    break;
                default:
                    erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            }
        }
    }
}
