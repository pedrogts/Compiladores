package analise.sintatica;

import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;

public class Grammar extends Sintatico {

    public int curPos = 0;
    public NoArvore arvore;

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

    public NoArvore aritmetica(List<Token> tokens) {
        curPos = 0;
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
                    break;
            }
        }

        return arvore;
    }

    public NoArvore fator(List<Token> tokens) {
        return num(tokens);
    }

    public NoArvore termo(List<Token> tokens) {
        NoArvore no = fator(tokens);

        while (curPos < tokens.size()) {
            TiposToken tipo = tokens.get(curPos).tipo;
            if (tipo == TiposToken.MULTI || tipo == TiposToken.BARRA) {
                curPos++;
                NoArvore direito = fator(tokens);
                if (tipo == TiposToken.MULTI)
                    no = gerarArvMulti(no, direito);
                else
                    no = gerarArvDiv(no, direito);
            } else {
                break;
            }
        }

        return no;
    }

    public NoArvore expressao(List<Token> tokens) {
        NoArvore no = termo(tokens);

        while (curPos < tokens.size()) {
            TiposToken tipo = tokens.get(curPos).tipo;
            if (tipo == TiposToken.MAIS || tipo == TiposToken.MENOS) {
                curPos++;
                NoArvore direito = termo(tokens);
                if (tipo == TiposToken.MAIS)
                    no = gerarArvMais(no, direito);
                else
                    no = gerarArvMenos(no, direito);
            } else {
                break;
            }
        }

        return no;
    }

    public NoArvore S(List<Token> tokens) {
        curPos = 0;
        arvore = expressao(tokens);
        return arvore;
    }

}