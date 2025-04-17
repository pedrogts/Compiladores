package src.analise.lexico;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    String sourceCode = "( ) + var name = 42;";
    Scanner scanner = new Scanner(sourceCode);
    List<Token> tokens = scanner.scanTokens();

    for (Token token : tokens) {
      System.out.println(token);
    }
  }
}
