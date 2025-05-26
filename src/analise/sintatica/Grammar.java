package analise.sintatica;

import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;

public class Grammar extends Sintatico {

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

    public NoArvore Expressao_Numerica(List<Token> tokens) {
        NoArvore termo = Termo(tokens);
        return Expressao_Numerica_Cont(tokens, termo);
    }

    public NoArvore Expressao_Numerica_Cont(List<Token> tokens, NoArvore esquerda) {
        if (curPos < tokens.size() && verificarOperadorSoma(tokens.get(curPos).tipo)) {
            Token operador = tokens.get(curPos);
            curPos++;
            NoArvore termo = Termo(tokens);
            NoArvore novaArvore = (operador.tipo == TiposToken.MAIS)
                    ? gerarArvMais(esquerda, termo)
                    : gerarArvMenos(esquerda, termo);
            return Expressao_Numerica_Cont(tokens, novaArvore);
        }
        return esquerda;
    }

    public NoArvore Termo(List<Token> tokens) {
        NoArvore fator = Fator(tokens);
        return Termo_Cont(tokens, fator);
    }

    public NoArvore Termo_Cont(List<Token> tokens, NoArvore esquerda) {
        if (curPos < tokens.size() && verificarOperadorMult(tokens.get(curPos).tipo)) {
            Token operador = tokens.get(curPos);
            curPos++;
            NoArvore fator = Fator(tokens);
            NoArvore novaArvore;
            if (operador.tipo == TiposToken.MULTI)
                novaArvore = gerarArvMulti(esquerda, fator);
            else if (operador.tipo == TiposToken.BARRA)
                novaArvore = gerarArvDiv(esquerda, fator);
            else
                novaArvore = gerarArvResto(esquerda, fator);
            return Termo_Cont(tokens, novaArvore);
        }
        return esquerda;
    }

    public NoArvore Fator(List<Token> tokens) {
        if (curPos < tokens.size() && tokens.get(curPos).tipo == TiposToken.PAR_ESQ) {
            curPos++;
            NoArvore expressao = Expressao_Numerica(tokens);
            if (curPos < tokens.size() && tokens.get(curPos).tipo == TiposToken.PAR_DIR) {
                curPos++;
                return expressao;
            } else {
                erroSin(tokens.stream().map(t -> t.tipo).toList(), curPos);
            }
        }
        return num(tokens);
    }

    public NoArvore Expressao_Booleana(List<Token> tokens) {
        NoArvore esquerdo = booleano(tokens);
        if (curPos < tokens.size()) {
            TiposToken tipo = tokens.get(curPos).tipo;
            if (verificarOperadorLogico(tipo)) {
                NoArvore operador = OperadorLogico(tokens);
                NoArvore direito = booleano(tokens);
                return (tipo == TiposToken.E)
                        ? gerarArvE(esquerdo, direito)
                        : gerarArvOu(esquerdo, direito);
            } else if (verificarOperadorRelacional(tipo)) {
                NoArvore operador = OperadorRelacional(tokens);
                NoArvore direito = booleano(tokens);
                switch (tipo) {
                    case MAIOR: return gerarArvMaior(esquerdo, direito);
                    case MENOR: return gerarArvMenor(esquerdo, direito);
                    case MAIOR_IGUAL: return gerarArvMaiorIgual(esquerdo, direito);
                    case MENOR_IGUAL: return gerarArvMenorIgual(esquerdo, direito);
                    case DIFERENTE: return gerarArvDiferente(esquerdo, direito);
                    default: return null;
                }
            }
        }
        return esquerdo;
    }

    public NoArvore Expressao_Caracter(List<Token> tokens) {
        NoArvore esquerdo = carac(tokens);
        if (curPos < tokens.size()) {
            TiposToken tipo = tokens.get(curPos).tipo;
            if (verificarOperadorLogico(tipo)) {
                NoArvore operador = OperadorLogico(tokens);
                NoArvore direito = carac(tokens);
                return (tipo == TiposToken.E)
                        ? gerarArvE(esquerdo, direito)
                        : gerarArvOu(esquerdo, direito);
            } else if (verificarOperadorRelacional(tipo)) {
                NoArvore operador = OperadorRelacional(tokens);
                NoArvore direito = carac(tokens);
                switch (tipo) {
                    case MAIOR: return gerarArvMaior(esquerdo, direito);
                    case MENOR: return gerarArvMenor(esquerdo, direito);
                    case MAIOR_IGUAL: return gerarArvMaiorIgual(esquerdo, direito);
                    case MENOR_IGUAL: return gerarArvMenorIgual(esquerdo, direito);
                    case DIFERENTE: return gerarArvDiferente(esquerdo, direito);
                    default: return null;
                }
            }
        }
        return esquerdo;
    }

    public NoArvore OperadorLogico(List<Token> tokens) {
        Token operador = tokens.get(curPos);
        NoArvore no = new NoArvore(operador.lexema);
        curPos++;
        return no;
    }

    public NoArvore OperadorRelacional(List<Token> tokens) {
        Token operador = tokens.get(curPos);
        NoArvore no = new NoArvore(operador.lexema);
        curPos++;
        return no;
    }

    public boolean verificarOperadorLogico(TiposToken token) {
        return token == TiposToken.E || token == TiposToken.OU;
    }

    public boolean verificarOperadorRelacional(TiposToken token) {
        return token == TiposToken.IGUAL ||
                token == TiposToken.MAIOR ||
                token == TiposToken.MENOR ||
                token == TiposToken.MAIOR_IGUAL ||
                token == TiposToken.MENOR_IGUAL ||
                token == TiposToken.DIFERENTE;
    }

    public boolean verificarOperadorSoma(TiposToken token) {
        return token == TiposToken.MAIS || token == TiposToken.MENOS;
    }

    public boolean verificarOperadorMult(TiposToken token) {
        return token == TiposToken.MULTI ||
                token == TiposToken.BARRA ||
                token == TiposToken.RESTO;
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

    public NoArvore S(List<Token> tokens) {
        return null;
    }
}
