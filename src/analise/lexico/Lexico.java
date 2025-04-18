package analise.lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexico {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();

  private int start = 0;
  private int current = 0;
  private int line = 1;

  private static final Map<String, TiposToken> palavra_reservada;

  static {
    palavra_reservada = new HashMap<>();
    palavra_reservada.put("e",    TiposToken.E);
    palavra_reservada.put("senao",   TiposToken.SENAO);
    palavra_reservada.put("falso",  TiposToken.FALSO);
    palavra_reservada.put("para",    TiposToken.PARA);
    palavra_reservada.put("se",     TiposToken.SE);
    palavra_reservada.put("ou",     TiposToken.OU );
    palavra_reservada.put("metodo", TiposToken.METODO);
    palavra_reservada.put("vazio", TiposToken.VAZIO);
    palavra_reservada.put("apresente",  TiposToken.APRESENTE);
    palavra_reservada.put("devolva", TiposToken.DEVOLVA);
    palavra_reservada.put("verdadeiro",   TiposToken.VERDADEIRO);
    palavra_reservada.put("enquanto",  TiposToken.ENQUANTO);
    palavra_reservada.put("resto",     TiposToken.RESTO);
    palavra_reservada.put("entao",     TiposToken.ENTAO);
    palavra_reservada.put("escolha",    TiposToken.ESCOLHA);
    palavra_reservada.put("caso",     TiposToken.CASO);
    palavra_reservada.put("saia",     TiposToken.SAIA);
    palavra_reservada.put("continue",    TiposToken.CONTINUE);
    palavra_reservada.put("faca",     TiposToken.FACA);
    palavra_reservada.put("repita",    TiposToken.REPITA);
    
    
  }

  public Lexico(String source) {
    this.source = source;
  }

  public List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(TiposToken.EOF, "", null, line));
    return tokens;
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(': addToken(TiposToken.PAR_ESQ); break;
      case ')': addToken(TiposToken.PAR_DIR); break;
      case '{': addToken(TiposToken.PAR_ESQ); break;
      case '}': addToken(TiposToken.PAR_DIR); break;
      case ',': addToken(TiposToken.VIRG); break;
      case '.': addToken(TiposToken.PONTO); break;
      case '-': addToken(TiposToken.MENOS); break;
      case '+': addToken(TiposToken.MAIS); break;
      case ';': addToken(TiposToken.PONT_VIRG); break;
      case '*': addToken(TiposToken.MULTI); break;
      case '=': addToken(TiposToken.IGUAL); break;
      case '#': addToken(TiposToken.HASTAG); break;
      case ':': addToken(TiposToken.DOIS_PONTOS); break;
      default:
        System.out.println("Caracter nao reconhecido: " + c);
        break;
    }
  }

  private char advance() {
    return source.charAt(current++);
  }

  private void addToken(TiposToken type) {
    addToken(type, null);
  }

  private void addToken(TiposToken type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
  void erro(char c) {
    var msg = String.format("Erro ao analisar o token: <%c>", c);
    throw new RuntimeException(msg);
  }

  ArrayList<TiposToken> anLex(char[] fonte) {
    ArrayList<TiposToken> tokenLst = new ArrayList<>();
    for (var c : fonte) {
      if (Character.toString(c).matches("\\d")) {
        tokenLst.add(TiposToken.DIG);
      } else if (Character.toString(c).matches("+")) {
        tokenLst.add(TiposToken.MAIS);
      } else if (Character.toString(c).matches("-")) {
        tokenLst.add(TiposToken.MENOS);
      } else if (Character.toString(c).matches("\\s")) {
        // ignorado...
      } else {
        erro(c);
      }
    }
    tokenLst.add(TiposToken.EOF);
    return tokenLst;
  }


}
