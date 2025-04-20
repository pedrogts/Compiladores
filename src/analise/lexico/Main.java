package analise.lexico;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    String fonte = " 1 + 2.4 - 3 = 34\n 5 * 6";  // Incluindo uma nova linha para testar a mudança de linha

    Lexico lexico = new Lexico();
    lexico.defCodFonte(fonte);
    List<Token> tokens = lexico.anLex();

    for (Token token : tokens) {
      System.out.println(token);  // Imprimindo os tokens para ver a linha de cada um
    }
  }
}
