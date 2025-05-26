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
                "Erro Sintatico no token de posicao %d: encontrado '%s'\nTokens: %s",
                curPos + 1,
                tokenLst.get(curPos),
                tokenLst
        );
        throw new RuntimeException(msg);
    }
    
    public void S(List<TiposToken> tokenLst){
        //Programa();
    }
    
    public void Atrib(List<TiposToken> tokenLst){
        
    }
    
    public void Param(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if(tokenAtual == TiposToken.ID){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
        
        tokenAtual = tokenLst.get(curPos);
        
        if(tokenAtual == TiposToken.DOIS_PONTOS){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
        
        Tipo(tokenLst);
    }
    
    public void Tipo(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if(tokenAtual == TiposToken.INT){
            curPos++;
        } else if (tokenAtual == TiposToken.DUP){
            curPos++;
        } else if (tokenAtual == TiposToken.CARACTER){
            curPos++;
        }else if (tokenAtual == TiposToken.BOOLEANO){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
    }
    
    public void Inteiro(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        TiposToken proxToken = lookAhead(tokenLst,curPos);
        if(tokenAtual == TiposToken.DIGITO_INT){
            curPos++;
        } else if (tokenAtual == TiposToken.MAIS && proxToken == TiposToken.DIGITO_INT){
            curPos += 2;
        } else if (tokenAtual == TiposToken.MENOS && proxToken == TiposToken.DIGITO_INT){
            curPos += 2;
        } else{
            erroSin(tokenLst, curPos);
        }
    }

    
    public void DuplaPrecisao(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        TiposToken proxToken = lookAhead(tokenLst,curPos);
        if(tokenAtual == TiposToken.DIGITO_DUP){
            curPos++;
        } else if (tokenAtual == TiposToken.MAIS && proxToken == TiposToken.DIGITO_DUP){
            curPos += 2;
        } else if (tokenAtual == TiposToken.MENOS && proxToken == TiposToken.DIGITO_DUP){
            curPos += 2;
        } else{
            erroSin(tokenLst, curPos);
        }
    }
    
    public void Numero(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.DIGITO_INT){
            Inteiro(tokenLst);
        } else if(tokenLst.get(curPos) == TiposToken.DIGITO_DUP){
            DuplaPrecisao(tokenLst);
        } else{
            erroSin(tokenLst, curPos);
        }
    }
    
    public void Caracter(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.CARACTER){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.ID){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
    }
    
    public void Boolean(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.VERDADEIRO){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.FALSO){
            curPos++; 
        } else if(tokenLst.get(curPos) == TiposToken.ID){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
    }
    
    public void Valor(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.DIGITO_INT || tokenLst.get(curPos) == TiposToken.DIGITO_DUP){
            Numero(tokenLst);
        } else if(tokenLst.get(curPos) == TiposToken.VERDADEIRO || tokenLst.get(curPos) == TiposToken.FALSO){
            Boolean(tokenLst); 
        } else if(tokenLst.get(curPos) == TiposToken.CARACTER){
            Caracter(tokenLst);
        } else if(tokenLst.get(curPos) == TiposToken.ID){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
    }
    
     public void OperadorRelacional(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.IGUAL){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.MAIOR){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.MENOR){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.MAIOR_IGUAL){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.MENOR_IGUAL){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.DIFERENTE){
            curPos++;
        }else{
            erroSin(tokenLst, curPos);
        }
     }
     
      public void OperadorLogico(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.E){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.OU){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
      }
      
      public void OperadorSoma(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.MAIS){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.MENOS){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
      }
      
      public void OperadorMult(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenLst.get(curPos) == TiposToken.MULTI){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.BARRA){
            curPos++;
        } else if(tokenLst.get(curPos) == TiposToken.RESTO){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
      }
      
      public void Expressao_Booleana(List<TiposToken> tokenLst) {
        Boolean(tokenLst); // reconhece o primeiro booleano (Boolean ? VERDADEIRO, FALSO, ID)
    
        if (curPos < tokenLst.size()) {
            TiposToken token = tokenLst.get(curPos);
    
            if (verificarOperadorLogico(token)) {
                OperadorLogico(tokenLst);
                Boolean(tokenLst); // segundo booleano
            } else if (verificarOperadorRelacional(token)) {
                OperadorRelacional(tokenLst);
                Boolean(tokenLst); // segundo booleano
            }
            // Caso contrário: é apenas Boolean sozinho (produção: Expressao_Booleana ? Boolean)
        }
      }
      
       public void Expressao_Caracter(List<TiposToken> tokenLst) {
        Caracter(tokenLst); 
    
        if (curPos < tokenLst.size()) {
            TiposToken token = tokenLst.get(curPos);
    
            if (verificarOperadorLogico(token)) {
                OperadorLogico(tokenLst);
                Caracter(tokenLst); 
            } else if (verificarOperadorRelacional(token)) {
                OperadorRelacional(tokenLst);
                Caracter(tokenLst); 
            }
            
        }
      }
      
       public void Expressao_Relacional(List<TiposToken> tokenLst) {
      
      }
      
      public void Expressao_Numerica(List<TiposToken> tokenLst){
          Termo(tokenLst);
          Expressao_Numerica_Cont(tokenLst);
      }
      
      public void Expressao_Numerica_Cont(List<TiposToken> tokenLst) {
            if (curPos < tokenLst.size() && verificarOperadorSoma(tokenLst.get(curPos))) {
                OperadorSoma(tokenLst);
                Termo(tokenLst);
                Expressao_Numerica_Cont(tokenLst); // recursão
            }
            // ? (vazio) — não faz nada
      }
      
      public void Termo(List<TiposToken> tokenLst) {
        Fator(tokenLst);
        Termo_Cont(tokenLst); // opcional
      }

      public void Termo_Cont(List<TiposToken> tokenLst) {
        if (curPos < tokenLst.size() && verificarOperadorMult(tokenLst.get(curPos))) {
            OperadorMult(tokenLst);
            Fator(tokenLst);
            Termo_Cont(tokenLst); // recursão
        }
        // ? (vazio)
    }
    
     public void Fator(List<TiposToken> tokenLst) {
        if (curPos < tokenLst.size() && tokenLst.get(curPos) == TiposToken.PAR_ESQ) {
            curPos++;  // avança após '('
    
            Expressao_Numerica(tokenLst);
    
            if (curPos < tokenLst.size() && tokenLst.get(curPos) == TiposToken.PAR_DIR) {
                curPos++;  // avança após ')'
            } else {
                erroSin(tokenLst, curPos);  // erro se não fecha parênteses
            }
        } else {
            Numero(tokenLst);  // se não tem parênteses, tenta analisar número
        }
      }

  
      

    public void Lst(List<TiposToken> tokenLst) {
        Numero(tokenLst);

        while (curPos < tokenLst.size() && tokenLst.get(curPos) != TiposToken.EOF) {
            TiposToken tokenAtual = tokenLst.get(curPos);

            if (tokenAtual == TiposToken.MAIS || tokenAtual == TiposToken.MENOS) {
                curPos++;
                Numero(tokenLst);
            } else {
                erroSin(tokenLst, curPos);
            }
        }
    }
    
    private boolean verificarOperadorLogico(TiposToken token) {
    return token == TiposToken.E || token == TiposToken.OU;
    }

    private boolean verificarOperadorRelacional(TiposToken token) {
    return token == TiposToken.IGUAL ||
           token == TiposToken.MAIOR ||
           token == TiposToken.MENOR ||
           token == TiposToken.MAIOR_IGUAL ||
           token == TiposToken.MENOR_IGUAL ||
           token == TiposToken.DIFERENTE;
    }
    
    
    private boolean verificarOperadorSoma(TiposToken token) {
    return token == TiposToken.MAIS ||
           token == TiposToken.MENOS;
    }
    
    private boolean verificarOperadorMult(TiposToken token) {
    return token == TiposToken.MULTI ||
           token == TiposToken.BARRA ||
           token == TiposToken.RESTO;
    }

      
    public TiposToken lookAhead(List<TiposToken> tokenLst, int curPos){
        TiposToken proxToken = TiposToken.EOF;
        
        if(curPos < tokenLst.size() && tokenLst.get(curPos) != TiposToken.EOF)
            proxToken = tokenLst.get(curPos+1);
            
        return proxToken;
    }
}
