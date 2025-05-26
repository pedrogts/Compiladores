package analise.sintatica;

import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;

public class Grammar extends Sintatico{

    private int curPos = 0;
    private NoArvore arvore;


    public NoArvore gerarArvResto(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("%");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvMaior(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore(">");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvMenor(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("<");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvMaiorIgual(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore(">=");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvMenorIgual(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("<=");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvDiferente(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("!=");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvE(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("&&");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
    }

    public NoArvore gerarArvOu(NoArvore esquerda, NoArvore direita) {
        NoArvore no = new NoArvore("||");
        no.esquerdo = esquerda;
        no.direito = direita;
        return no;
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

    public NoArvore tipo(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        if (tokenAtual.tipo == TiposToken.INT || tokenAtual.tipo == TiposToken.DUP ||
                tokenAtual.tipo == TiposToken.BOOLEANO || tokenAtual.tipo == TiposToken.CARACTER) {
            NoArvore no = new NoArvore(tokenAtual.lexema);
            curPos++;
            return no;
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public NoArvore carac(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        NoArvore no = new NoArvore(tokenAtual.lexema);
        if (tokenAtual.tipo == TiposToken.CARACTER || tokenAtual.tipo == TiposToken.ID) {
            curPos++;
            return no;
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public NoArvore booleano(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        NoArvore no = new NoArvore(tokenAtual.lexema);
        if (tokenAtual.tipo == TiposToken.VERDADEIRO || tokenAtual.tipo == TiposToken.FALSO ||
                tokenAtual.tipo == TiposToken.ID) {
            curPos++;
            return no;
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public NoArvore valor(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);

        if (tokenAtual.tipo == TiposToken.DIGITO_INT || tokenAtual.tipo == TiposToken.DIGITO_DUP) {
            return num(tokens);
        } else if (tokenAtual.tipo == TiposToken.VERDADEIRO || tokenAtual.tipo == TiposToken.FALSO) {
            return booleano(tokens);
        } else if (tokenAtual.tipo == TiposToken.CARACTER) {
            return carac(tokens);
        } else if (tokenAtual.tipo == TiposToken.ID) {
            curPos++;
            return new NoArvore(tokenAtual.lexema);
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public NoArvore OperadorRelacional(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        NoArvore no = new NoArvore(tokenAtual.lexema);
        switch (tokenAtual.tipo) {
            case MAIOR, MENOR, MAIOR_IGUAL, MENOR_IGUAL, DIFERENTE -> {
                curPos++;
                return no;
            }
            default -> {
                erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
                return null;
            }
        }
    }

    public NoArvore OperadorLogico(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        NoArvore no = new NoArvore(tokenAtual.lexema);
        if (tokenAtual.tipo == TiposToken.E || tokenAtual.tipo == TiposToken.OU) {
            curPos++;
            return no;
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public NoArvore OperadorSoma(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        NoArvore no = new NoArvore(tokenAtual.lexema);
        if (tokenAtual.tipo == TiposToken.MAIS || tokenAtual.tipo == TiposToken.MENOS) {
            curPos++;
            return no;
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public NoArvore OperadorMult(List<Token> tokens) {
        if (curPos >= tokens.size()) {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
        }
        Token tokenAtual = tokens.get(curPos);
        NoArvore no = new NoArvore(tokenAtual.lexema);
        if (tokenAtual.tipo == TiposToken.MULTI || tokenAtual.tipo == TiposToken.BARRA || tokenAtual.tipo == TiposToken.RESTO) {
            curPos++;
            return no;
        } else {
            erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            return null;
        }
    }

    public NoArvore Lst(List<Token> tokens) {
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
}
