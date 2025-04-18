package analise.lexico;

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
    }

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
        List<Token> tokens = new ArrayList<>();

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
                    addToken(TiposToken.PAR_ESQ, tokens);
                    break;
                case ')':
                    addToken(TiposToken.PAR_DIR, tokens);
                    break;
                case '{':
                    addToken(TiposToken.PAR_ESQ, tokens);
                    break;
                case '}':
                    addToken(TiposToken.PAR_DIR, tokens);
                    break;
                case ',':
                    addToken(TiposToken.VIRG, tokens);
                    break;
                case '.':
                    addToken(TiposToken.PONTO, tokens);
                    break;
                case '-':
                    addToken(TiposToken.MENOS, tokens);
                    break;
                case '+':
                    addToken(TiposToken.MAIS, tokens);
                    break;
                case ';':
                    addToken(TiposToken.PONT_VIRG, tokens);
                    break;
                case '*':
                    addToken(TiposToken.MULTI, tokens);
                    break;
                case '#':
                    addToken(TiposToken.HASTAG, tokens);
                    break;
                case ':':
                    addToken(TiposToken.DOIS_PONTOS, tokens);
                    break;
                case '!':
                    addToken(verificar('=') ? TiposToken.DIFERENTE : TiposToken.NOT, tokens);
                    break;
                case '>':
                    addToken(verificar('=') ? TiposToken.MAIOR_IGUAL : TiposToken.MAIOR, tokens);
                    break;
                case '<':
                    if (verificar('<')) {
                        addToken(TiposToken.ATRIBUICAO, tokens);
                    } else if (verificar('=')) {
                        addToken(TiposToken.MENOR_IGUAL, tokens);
                    } else {
                        addToken(TiposToken.MENOR, tokens);
                    }
                    break;
                case '/':
                    addToken(verificar('*') ? TiposToken.BARRA_ASTERISCO : TiposToken.BARRA, tokens);
                    break;
                default:
                    erro(c);
                    break;
            }
        }
         atual--;
        addToken(TiposToken.EOF, "eof", tokens);
        return tokens;
    }


    private void addToken(TiposToken tipo, List<Token> tokens) {
        addToken(tipo, null, tokens);
    }

    private void addToken(TiposToken tipo, Object literal, List<Token> tokens) {
        String texto = fonte.substring(inicio, atual);
        tokens.add(new Token(tipo, texto, literal, linha));
    }

    private boolean verificar(char esperado) {
        if (terminou()) return false;
        if (fonte.charAt(atual) != esperado) return false;

        atual++;
        return true;
    }
}
