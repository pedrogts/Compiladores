package analise.lexico;

public class Token {
  final TiposToken tipo;
  final String lexema;
  final Object literal;
  final int linha;

  public Token(TiposToken tipo, String lexema, Object literal, int linha) {
    this.tipo = tipo;
    this.lexema = lexema;
    this.literal = literal;
    this.linha = linha;
  }

  @Override
  public String toString() {
    return "["
            + "linha: " + linha + ", "
            + "lexema: \"" + lexema + "\", "
            + "tipo: <" + tipo + ">, "
             + "]";
  }

}
