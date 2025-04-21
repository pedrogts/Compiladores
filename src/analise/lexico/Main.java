package src.analise.lexico;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    String fonte = "int dup l << 2; para se entao \n\n\n\nPRAIA TORRES ILHA BELA se entao senao para faca enquanto repita ate que int 1 2 3 123 abc 12.12";
    Lexico lexico = new Lexico();
    lexico.defCodFonte(fonte);
    List<Token> tokens = lexico.anLex();

    for (Token token : tokens) {
      System.out.println(token);
    }
  }
}
