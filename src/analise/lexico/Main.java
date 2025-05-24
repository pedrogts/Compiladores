package analise.lexico;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    String fonte = "400 + 300";
    Lexico lexico = new Lexico();
    lexico.defCodFonte(fonte);
    List<Token> tokens = lexico.anLex();

    for (Token token : tokens) {
      System.out.println(token);
    }
  }
}
