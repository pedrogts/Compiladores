package src.analise.lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexico {

    private String fonte;
    private int inicio = 0;
    private int atual = 0;
    private int linha = 1;

    private static final Map<String, TiposToken> palavra_reservada;

    static {
        palavra_reservada = new HashMap<>();
        palavra_reservada.put("e", TiposToken.E);
        palavra_reservada.put("senao", TiposToken.SENAO);
        palavra_reservada.put("falso", TiposToken.FALSO);
        palavra_reservada.put("para", TiposToken.PARA);
        palavra_reservada.put("se", TiposToken.SE);
        palavra_reservada.put("ou", TiposToken.OU);
        palavra_reservada.put("metodo", TiposToken.METODO);
        palavra_reservada.put("vazio", TiposToken.VAZIO);
        palavra_reservada.put("apresente", TiposToken.APRESENTE);
        palavra_reservada.put("devolva", TiposToken.DEVOLVA);
        palavra_reservada.put("verdadeiro", TiposToken.VERDADEIRO);
        palavra_reservada.put("enquanto", TiposToken.ENQUANTO);
        palavra_reservada.put("resto", TiposToken.RESTO);
        palavra_reservada.put("entao", TiposToken.ENTAO);
        palavra_reservada.put("escolha", TiposToken.ESCOLHA);
        palavra_reservada.put("caso", TiposToken.CASO);
        palavra_reservada.put("saia", TiposToken.SAIA);
        palavra_reservada.put("continue", TiposToken.CONTINUE);
        palavra_reservada.put("faca", TiposToken.FACA);
        palavra_reservada.put("repita", TiposToken.REPITA);
        palavra_reservada.put("int", TiposToken.INT);
        palavra_reservada.put("dup", TiposToken.DUP);
        palavra_reservada.put("simbol", TiposToken.CARACTER);
        palavra_reservada.put("booleano", TiposToken.BOOLEANO);
    }

    private List<Token> tokens = new ArrayList<>();

    public void defCodFonte(String fonte) {
        this.fonte = fonte;
        this.inicio = 0;
        this.atual = 0;
        this.linha = 1;
    }

    private boolean terminou() {
        return atual >= fonte.length();
    }

    private char avancar() {
        return fonte.charAt(atual++);
    }

    private char olhar() {
        if (terminou()) {
            return '\0';
        }
        return fonte.charAt(atual);
    }

    void erro(char c) {
        var msg = String.format("Erro ao analisar o token: <%c>", c);
        throw new RuntimeException(msg);
    }

    public List<Token> anLex() {
        while (!terminou()) {
            inicio = atual;
            char c = avancar();

            if (c == ' ' || c == '\r' || c == '\t') {
                continue;
            }
            if (c == '\n') {
                linha++;
                continue;
            }

            switch (c) {
                case '(':
                    addToken(TiposToken.PAR_ESQ);
                    break;
                case ')':
                    addToken(TiposToken.PAR_DIR);
                    break;
                case '{':
                    addToken(TiposToken.CHAVE_ESQ);
                    break;
                case '}':
                    addToken(TiposToken.CHAVE_DIR);
                    break;
                case ',':
                    addToken(TiposToken.VIRG);
                    break;
                case '.':
                    addToken(TiposToken.PONTO);
                    break;
                case '-':
                    addToken(TiposToken.MENOS);
                    break;
                case '+':
                    addToken(TiposToken.MAIS);
                    break;
                case ';':
                    addToken(TiposToken.PONT_VIRG);
                    break;
                case '*':
                    addToken(TiposToken.MULTI);
                    break;
                case '=':
                    addToken(TiposToken.IGUAL);
                    break;
                case '#':
                    addToken(TiposToken.HASTAG);
                    break;
                case ':':
                    addToken(TiposToken.DOIS_PONTOS);
                    break;
                case '!':
                    addToken(verificar('=') ? TiposToken.DIFERENTE : TiposToken.NOT);
                    break;
                case '>':
                    addToken(verificar('=') ? TiposToken.MAIOR_IGUAL : TiposToken.MAIOR);
                    break;
                case '<':
                    if (verificar('<')) {
                        addToken(TiposToken.ATRIBUICAO);
                    } else if (verificar('=')) {
                        addToken(TiposToken.MENOR_IGUAL);
                    } else {
                        addToken(TiposToken.MENOR);
                    }
                    break;
                case '/':
                    addToken(verificar('*') ? TiposToken.BARRA_ASTERISCO : TiposToken.BARRA);
                    break;
                default:
                    if (Character.isDigit(c)) {
                        numero();
                    } else if (id(c)) {
                        identificador();
                    } else {
                        erro(c);
                    }
            }
        }
        tokens.add(new Token(TiposToken.EOF, "eof", "EOF", linha));
        return tokens;
    }

    private boolean digito(char c) {
        return Character.toString(c).matches("\\d");
    }

    private boolean id(char c) {
        return Character.toString(c).matches("[A-Za-z_][A-Za-z0-9_]*");
    }

    private void numero() {
        while (digito(olhar())) {
            avancar();
        }

        if (olhar() == '.' && digito(olharProx())) {
            avancar();

            while (digito(olhar())) {
                avancar();
            }

            addToken(TiposToken.DIGITODUP, Double.parseDouble(fonte.substring(inicio, atual)));
        } else {
            addToken(TiposToken.DIGITOINT, Integer.parseInt(fonte.substring(inicio, atual)));
        }
    }


    private char olharProx() {
        if (atual + 1 >= fonte.length()) return '\0';
        return fonte.charAt(atual + 1);
    }

    private void addToken(TiposToken tipo) {
        addToken(tipo, null);
    }

    private void addToken(TiposToken tipo, Object literal) {
        String texto = fonte.substring(inicio, atual);
        tokens.add(new Token(tipo, texto, literal, linha));
    }

    private boolean verificar(char esperado) {
        if (terminou()) return false;
        if (fonte.charAt(atual) != esperado) return false;

        atual++;
        return true;
    }

    private void identificador() {
        while (id(olhar())) {
            avancar();
        }
        String s = fonte.substring(inicio, atual);
        TiposToken tipo;
        tipo = palavra_reservada.get(s);
        if (tipo == null) {
            addToken(TiposToken.ID, s);
        } else {
            addToken(tipo, s);
        }

    }

}