package analise.lexico;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    String sourceCode = "senao";
    Lexico scanner = new Lexico(sourceCode);
    List<Token> tokens = scanner.scanTokens();

    for (Token token : tokens) {
      System.out.println(token);
    }
  }
}
