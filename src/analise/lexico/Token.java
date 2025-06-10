package analise.lexico;

public class Token {
    public final TiposToken tipo;
    public final String lexema;
    public final Object literal;
    public final int linha;

    public Token(TiposToken tipo, String lexema, Object literal, int linha) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.linha = linha;
    }

    @Override
    public String toString() {
        return "[" + linha + ", " + "\"" + lexema + "\"" + ", " + "<" + tipo + ">" + "]";
    }

}
