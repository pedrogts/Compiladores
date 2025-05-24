package analise.sintatica;

import analise.lexico.TiposToken;
import java.util.List;

public class Sintatico {
    public int curPos = 0;

    public void estadoAnSin(List<TiposToken> tokenLst){
        System.out.println("[curPos = " + curPos + ", tokenLst = " + tokenLst + "]");
    }

    public void erroSin(List<TiposToken> tokenLst, int curPos){
        String msg = String.format(
                "Erro sintático no token de posição %d: encontrado '%s'\nTokens: %s",
                curPos + 1,
                tokenLst.get(curPos),
                tokenLst
        );
        throw new RuntimeException(msg);
    }

    public void num(List<TiposToken> tokenLst){
        if (tokenLst.get(curPos) == TiposToken.DIGITO_INT ||
                tokenLst.get(curPos) == TiposToken.DIGITO_DUP) {
            curPos++;
        } else {
            erroSin(tokenLst, curPos);
        }
    }

    public void Lst(List<TiposToken> tokenLst) {
        num(tokenLst);

        while (curPos < tokenLst.size() && tokenLst.get(curPos) != TiposToken.EOF) {
            TiposToken tokenAtual = tokenLst.get(curPos);

            if (tokenAtual == TiposToken.MAIS || tokenAtual == TiposToken.MENOS) {
                curPos++;
                num(tokenLst);
            } else {
                erroSin(tokenLst, curPos);
            }
        }
    }
}
