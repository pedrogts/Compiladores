package analise.sintatica;

import analise.lexico.TiposToken;
import java.util.List;

public class Sintatico {
    public int curPos;
    List<TiposToken> tokenLst;
    
    
    public Sintatico(List<TiposToken> tknList){
        tokenLst = tknList;
        curPos = 0;
    }
    
    public void Programa(){
        Header();
        FuncaoPrincipal();
        Bloco();
        Constructos_Funcoes();
    }
    
    public void FuncaoPrincipal(){
        /*"Metodo" "principal()" ":" <Tipo>*/
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);
        
        if(tknAtual == TiposToken.METODO && proxToken == TiposToken.PRINCIPAL){
            consumirDoisTokens();
            tknAtual = getTokenAtual();
            proxToken = lookAhead(curPos);
            if(tknAtual == TiposToken.PAR_ESQ && proxToken == TiposToken.PAR_DIR){
                consumirDoisTokens();
                tknAtual = getTokenAtual();
                if(tknAtual == TiposToken.DOIS_PONTOS){
                    consumirToken();
                    Tipo();
                } else{
                    erroSin(curPos);
                }
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
    }
    
    
    public void Header(){
        Cabecalho();
    }
    
    public void Cabecalho(){
        /*<Cabeçalho> ::= <Declaracoes_Funcao_Cabecalho> <Cabeçalho>
              | <Variavel_Global> <Cabeçalho>
              | ?*/
              
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);
        if(tknAtual == TiposToken.METODO && proxToken != TiposToken.PRINCIPAL){
            DeclaracaoFuncaoCabecalho();
            Cabecalho();
        } else if(proxToken != TiposToken.PRINCIPAL){
            erroSin(curPos);
        }
    }
    
    
    public void Tipo(){
        //<Tipo> ::= "INT" | "DUP" | "CARACTER" | "Booleano"
        TiposToken tknAtual = getTokenAtual();
        
        if(tknAtual == TiposToken.INT || tknAtual == TiposToken.DUP || tknAtual == TiposToken.CARACTER || tknAtual == TiposToken.BOOLEANO || tknAtual == TiposToken.VAZIO)
            consumirToken();
        else erroSin(curPos);
    }
    
    public void DeclaracaoVariavel(){
        //<Declaracao_Variavel> ::= <ID> ":" <Tipo> ";"
        TiposToken tknAtual = getTokenAtual();
        
        if(tknAtual == TiposToken.ID){
            consumirToken();
            tknAtual = getTokenAtual();
        }    
        else erroSin(curPos);
        
        if(tknAtual == TiposToken.DOIS_PONTOS){
            consumirToken();
            tknAtual = getTokenAtual();
        }    
        else erroSin(curPos);
        
        Tipo();
        tknAtual = getTokenAtual();
        
        if(tknAtual == TiposToken.PONT_VIRG){
            consumirToken();
        }    
        else erroSin(curPos);
    }
    
    public void Atribuicao(){
        //<Atrib> ::= <ID> <Operador_Atribuicao> <Exp> ";"
        TiposToken tknAtual = getTokenAtual();
         if(tknAtual == TiposToken.ID){
            consumirToken();
            tknAtual = getTokenAtual();
        }    
        else erroSin(curPos);
        
         if(tknAtual == TiposToken.ATRIBUICAO){
            consumirToken();
            tknAtual = getTokenAtual();
        }    
        else erroSin(curPos);
        
        Exp();
        //Valor();
        tknAtual = getTokenAtual();
        
        if(tknAtual == TiposToken.PONT_VIRG){
            consumirToken();
        } else if(tknAtual != TiposToken.PAR_DIR)    
                erroSin(curPos);
        
    }
    
    public void Valor(){
        //<Valor> ::= "Numero" | "Boolean" | "Carac" | <ID>
        TiposToken tknAtual = getTokenAtual();
        
        if (isNumero()) {
            Numero();
        } else if (isBooleano(tknAtual)) {
            Boolean();
        } else if (isCaracter(tknAtual)) {
            Caracter();
        } else if (tknAtual == TiposToken.ID) {
            consumirToken();
        } else {
            erroSin(curPos);
        }
    }
    
    public void Numero(){
        //<Numero> ::= <Inteiro> | <Dupla_Precisao>
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);
        if(tknAtual == TiposToken.DIGITO_INT){
            Inteiro();
        } else if(tknAtual == TiposToken.MENOS && proxToken == TiposToken.DIGITO_INT)    {
            Inteiro();
        } else if(tknAtual == TiposToken.MAIS && proxToken == TiposToken.DIGITO_INT){
            Inteiro();
        } else if(tknAtual == TiposToken.DIGITO_DUP){
            Dupla_Precisao();
        } else if(tknAtual == TiposToken.MENOS && proxToken == TiposToken.DIGITO_DUP){
            Dupla_Precisao();
        } else if(tknAtual == TiposToken.MAIS && proxToken == TiposToken.DIGITO_DUP){
            Dupla_Precisao();
        } else erroSin(curPos);
    }
    
    public void Inteiro(){
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;
        if(tknAtual == TiposToken.DIGITO_INT){
            consumirToken();
        } else{
            proxToken = lookAhead(curPos);
            if(tknAtual == TiposToken.MENOS && proxToken == TiposToken.DIGITO_INT)
                consumirDoisTokens();
            else if(tknAtual == TiposToken.MAIS && proxToken == TiposToken.DIGITO_INT)
                    consumirDoisTokens();
            else erroSin(curPos);        
        }
    }
    
    public void Dupla_Precisao(){
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;
        if(tknAtual == TiposToken.DIGITO_DUP){
            consumirToken();
        } else{
            proxToken = lookAhead(curPos);
            if(tknAtual == TiposToken.MENOS && proxToken == TiposToken.DIGITO_DUP)
                consumirDoisTokens();
            else if(tknAtual == TiposToken.MAIS && proxToken == TiposToken.DIGITO_DUP)
                    consumirDoisTokens();
            else erroSin(curPos);        
        }
    }
    
    public void Bloco(){

        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;
        if(tknAtual == TiposToken.CHAVE_ESQ){
            consumirToken();
            tknAtual = getTokenAtual();
            proxToken = lookAhead(curPos);
            while(tknAtual != TiposToken.CHAVE_DIR && tknAtual != TiposToken.EOF){
                if(tknAtual == TiposToken.ID && proxToken == TiposToken.DOIS_PONTOS){
                    DeclaracaoVariavel();
                } else{
                    Instrucao();
                }
                tknAtual = getTokenAtual();
                proxToken = lookAhead(curPos);
            }
            if(tknAtual == TiposToken.EOF)
                erroSin(curPos);
            else
                consumirToken();
        }
    }
    
    public void Lista_Instrucoes(){
        /*<Lista_Instruções> ::= <Instrução> <Lista_Instruções> | ?*/
        TiposToken tknAtual = getTokenAtual();
        if(ehInicioInstrucao()){
            Instrucao();
            Lista_Instrucoes();
        }
    }
    
    public void Instrucao(){
        /*<Instrução> ::= <Atrib>
              | <Instrucao_Se>
              | <Instrucao_Para>
              | <Instrucao_Enquanto>
              | <Instrucao_Repita>
              | <Chamada_Funcao>
              | <Exp>
              | <Expressao_Retorno>*/
              
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);
        
        if(tknAtual == TiposToken.ID && proxToken == TiposToken.ATRIBUICAO){
            Atribuicao();
        } else if(tknAtual == TiposToken.SE){
            InstrucaoSe();
        }else if(tknAtual == TiposToken.PARA){
            Instrucao_Para();
        } else if(tknAtual == TiposToken.ENQUANTO){
            Instrucao_Enquanto();
        } else if(tknAtual == TiposToken.REPITA){
            Instrucao_Repita();
        } else if(tknAtual == TiposToken.DEVOLVA){
            ExpressaoRetorno();
        } else if(tknAtual == TiposToken.ID && proxToken == TiposToken.PAR_ESQ){
            System.out.println("\nChamou Funcao\n");
            ChamadaFuncao();
            tknAtual = getTokenAtual();
            if(tknAtual == TiposToken.PONT_VIRG){
                consumirToken();
            } else{
                erroSin(curPos);
            }
        } else if(ehInicioExpressao(tknAtual)){
            Exp();
        } else{
            erroSin(curPos);
        }
        
    }
    
    
    public void InstrucaoSe(){
        /*<Instrucao_Se> ::= "se" "(" <Condicao> ")" <Bloco> <Instrucao_Se_Cont>*/
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);
        
        if(tknAtual == TiposToken.SE && proxToken == TiposToken.PAR_ESQ){
            consumirDoisTokens();
            Condicao();
            tknAtual = getTokenAtual();
            if(tknAtual == TiposToken.PAR_DIR){
                consumirToken();
                Bloco();
                InstrucaoSeCont();
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos+1);
        }
        
    }
    
    public void InstrucaoSeCont(){
        /*<Instrucao_Se_Cont> ::= "senao" <Bloco>
                      | "senao" <Instrucao_Se>
                      | ?
        */
         
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);
        
        if(tknAtual == TiposToken.SENAO && proxToken == TiposToken.CHAVE_ESQ){
            consumirToken();
            Bloco();
        } else if(tknAtual == TiposToken.SENAO && proxToken == TiposToken.SE){
            consumirToken();
            InstrucaoSe();
        } else if(tknAtual == TiposToken.SENAO && (proxToken != TiposToken.SE && proxToken != TiposToken.CHAVE_ESQ)){
            erroSin(curPos);
        }
       
    }
    
    public void Instrucao_Repita(){
        /*<Instrucao_Repita> ::= "repita" <Bloco> "enquanto" "(" <Condicao> ")" ";"*/
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;
        if(tknAtual == TiposToken.REPITA){
            consumirToken();
            Bloco();
            tknAtual = getTokenAtual();
            proxToken = lookAhead(curPos);
            if(tknAtual == TiposToken.ENQUANTO && proxToken == TiposToken.PAR_ESQ){
                consumirDoisTokens();
                Condicao();
                tknAtual = getTokenAtual();
                proxToken = lookAhead(curPos);
                if(tknAtual == TiposToken.PAR_DIR && proxToken == TiposToken.PONT_VIRG){
                    consumirDoisTokens();
                } else{
                    erroSin(curPos);
                }
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
    }
    
    public void Instrucao_Enquanto(){
        /*<Instrucao_Enquanto> ::= "enquanto" "(" <Condicao> ")" "repita" <Bloco>*/
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;
        if(tknAtual == TiposToken.ENQUANTO){
            consumirToken();
            tknAtual = getTokenAtual();
            if(tknAtual == TiposToken.PAR_ESQ){
                consumirToken();
                tknAtual = getTokenAtual();
                Condicao();
                tknAtual = getTokenAtual();
                proxToken = lookAhead(curPos);
                if(tknAtual == TiposToken.PAR_DIR && proxToken == TiposToken.REPITA){
                    consumirDoisTokens();
                    Bloco();
                } else{
                    erroSin(curPos);
                }
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
    }
    
    public void Instrucao_Para(){
        /*<Instrucao_Para> ::= "para" "(" <Atrib> ";" <Condicao> ";" <Exp> ")" <Bloco>*/
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;
        if(tknAtual == TiposToken.PARA){
            consumirToken();
            tknAtual = getTokenAtual();
            if(tknAtual == TiposToken.PAR_ESQ){
                consumirToken();
                tknAtual = getTokenAtual();
                Atribuicao(); //Verifica primeiro ;
                tknAtual = getTokenAtual();
                Condicao();
                tknAtual = getTokenAtual();
                if(tknAtual == TiposToken.PONT_VIRG){
                    consumirToken();
                    proxToken = lookAhead(curPos);
                     System.out.println("antes atrib ou exp");    
                    if(proxToken == TiposToken.ATRIBUICAO)
                        Atribuicao();
                    else    
                        Exp();
                    System.out.println("pos atrib ou exp");    
                    tknAtual = getTokenAtual();
                    if(tknAtual == TiposToken.PAR_DIR){
                        consumirToken();
                        Bloco();
                    } else{
                        erroSin(curPos);
                    }
                } else{
                    erroSin(curPos);
                }
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
               
        
    }
    
    public void ChamadaFuncao(){
        /*<Chamada_Funcao> ::= <ID> "(" <Argumentos> ")" ";"*/
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;   
        
        if(tknAtual == TiposToken.ID){
            consumirToken();
            tknAtual = getTokenAtual();
            proxToken = lookAhead(curPos);
            if(tknAtual == TiposToken.PAR_ESQ && proxToken == TiposToken.PAR_DIR){
                consumirDoisTokens();
                tknAtual = getTokenAtual();
            } else if(proxToken != TiposToken.PAR_DIR){
                consumirToken();
                Argumentos();
                tknAtual = getTokenAtual();
                if(tknAtual == TiposToken.PAR_DIR){
                    consumirToken();
                    tknAtual = getTokenAtual();
                } else{
                    erroSin(curPos);
                }
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
    }
    
    public void Constructos_Funcoes(){
         TiposToken tknAtual = getTokenAtual();
         while(tknAtual == TiposToken.METODO){
            ConstructoFuncao();
            tknAtual = getTokenAtual();
         }
         
         if(tknAtual != TiposToken.EOF){
             erroSin(curPos);
         }
    }
    
    public void ConstructoFuncao(){
        /*<Constructo_Funcao> ::= <Declaracao_Funcao> <Bloco> | ?*/
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.METODO){
            DeclaracaoFuncaoConstructo();
            Bloco();
        }
    }
    
    public void DeclaracoesFuncaoCabecalho(){
        TiposToken tknAtual = getTokenAtual();
        while(tknAtual == TiposToken.METODO){
            DeclaracaoFuncaoCabecalho();
            tknAtual = getTokenAtual();
        }
        
        if(tknAtual != TiposToken.EOF){
            erroSin(curPos);
        }
    }
    
    
    public void DeclaracaoFuncaoCabecalho(){
        /*<Declaracao_Funcao> ::= "Metodo" <ID> "(" <Parametros> ")" ":" <Tipo> ";"*/
         DeclaracaoFuncaoConstructo();
         if(getTokenAtual() == TiposToken.PONT_VIRG){
            consumirToken();
          } else {
            erroSin(curPos);
        }
    }
    
     public void DeclaracaoFuncaoConstructo(){
        /*<Declaracao_Funcao> ::= "Metodo" <ID> "(" <Parametros> ")" ":" <Tipo> ";"*/
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken;
        if(tknAtual == TiposToken.METODO){
            consumirToken();
            tknAtual = getTokenAtual();
            if(tknAtual == TiposToken.ID){
                consumirToken();
                tknAtual = getTokenAtual();
                proxToken = lookAhead(curPos);
                if(tknAtual == TiposToken.PAR_ESQ && proxToken == TiposToken.PAR_DIR){
                    consumirDoisTokens();
                    tknAtual = getTokenAtual();
                    if(tknAtual == TiposToken.DOIS_PONTOS){
                        consumirToken();
                        tknAtual = getTokenAtual();
                        Tipo();
                    } else{
                        erroSin(curPos);
                    }
                } else if(proxToken != TiposToken.PAR_DIR){
                    consumirToken();
                    Parametros();
                    tknAtual = getTokenAtual();
                    if(tknAtual == TiposToken.PAR_DIR){
                        consumirToken();
                        tknAtual = getTokenAtual();
                    if(tknAtual == TiposToken.DOIS_PONTOS){
                        consumirToken();
                        tknAtual = getTokenAtual();
                        Tipo();
                    } else{
                        erroSin(curPos);
                    }
                    }else{
                        erroSin(curPos);
                    }
                } else{
                    erroSin(curPos);
                }
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
    }
    
    
    
    public void ExpressaoRetorno(){
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.DEVOLVA){
            consumirToken();
            tknAtual = getTokenAtual();
            Exp();
            tknAtual = getTokenAtual();
            if(tknAtual == TiposToken.PONT_VIRG){
                consumirToken();
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
    }
    
    public void Parametros(){
        /*<Parametros> ::= ? | <Lista_Parametros>*/
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.ID)
            Lista_Parametros();
        else{
            erroSin(curPos);
        }
    }
    
    public void Lista_Parametros(){
        /*<Lista_Parametros> ::= <Param> <Lst_Parametros>*/
        Param();
        Lst_Parametros();
    }
    
    public void Lst_Parametros(){
        /*<Lst_Parametros> ::= "," <Param> <Lst_Parametros>
                   | ?
        */
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.VIRG){
            consumirToken();
            Param();
            Lst_Parametros();
        }
       
    }
    
    public void Param(){
        /*<Param> ::= <ID> ":" <Tipo>*/
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.ID){
            consumirToken();
            tknAtual = getTokenAtual();
            if(tknAtual == TiposToken.DOIS_PONTOS){
                consumirToken();
                Tipo();
            } else{
                erroSin(curPos);
            }
        } else{
            erroSin(curPos);
        }
    }
    
    public void Argumentos(){
        /*<Argumentos> ::= ? | <Lista_Argumentos>*/
        TiposToken tknAtual = getTokenAtual();
        if(ehInicioExpressao(tknAtual))
            Lista_Argumentos();
        else
            erroSin(curPos);
    }
    
    public void Lista_Argumentos(){
    /*<Lista_Argumentos> ::= <Exp> <Lst_Argumentos>*/
    Exp();
    Lst_Argumentos();
    
    }
    
    
    public void Lst_Argumentos(){
        /*<Lst_Argumentos> ::= "," <Exp> <Lst_Argumentos>
                   | ?
        */
          
       TiposToken tknAtual = getTokenAtual();
       if(tknAtual == TiposToken.VIRG){
           consumirToken();
           Exp();
           Lst_Argumentos();
       }
       
    }
    
    public void Condicao(){
        System.out.println("Condicao");
        CondicaoOU();
    }
    
    public void CondicaoOU(){
        CondicaoE();
        CondicaoOUcont();
    }
    
    public void CondicaoOUcont(){
        TiposToken tknAtual = getTokenAtual();
        
        if(tknAtual == TiposToken.OU){
            consumirToken();
            CondicaoE();
            CondicaoOUcont();
        }
    }
    
    public void CondicaoE(){
        CondicaoSimples();
        CondicaoEcont();
    }
    
    public void CondicaoEcont(){
        /*<Condicao_E_Cont> ::= "e" <Condicao_Simples> <Condicao_E_Cont> | ?*/
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.E){
            System.out.println("Consumiu E");
            consumirToken();
            CondicaoSimples();
            CondicaoEcont();
        } 
    }
    
    public void CondicaoSimples(){
        /*<Condicao_Simples> ::= <Expressao_Relacional> | <Exp>*/
        TiposToken tknAtual;
        Exp();
        tknAtual = getTokenAtual();
        if(ehOperadorRelacional(tknAtual)){
            OperadorRelacional();
            tknAtual = getTokenAtual();
            Exp();
        } 
        
    }
    
    public void Exp(){
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);  // olhar o próximo token
        TiposToken proxTokenBoolean = lookAhead(curPos+1);
        System.out.println("    Exp     ");
        if(ehExpressaoAritmetica() || (tknAtual == TiposToken.PAR_ESQ) || (tknAtual == TiposToken.ID && ehOperadorAritmetico(proxToken))){
            System.out.println("Exp:Chamou ExpNumerica\nTokentAtual: " +tknAtual+ " ProxToken: " +proxToken + "\n");
            ExpressaoNumerica();
        } 
        else if(isCaracter(tknAtual)){
            System.out.println("Exp:Chamou ExpCarac");
            ExpressaoCaracter();
        } 
        else if(isBooleano(tknAtual) || ehExpressaoRelacional()){
            System.out.println("Exp:Chamou ExpBool");
            ExpressaoBooleana();
        } 
        else if(tknAtual == TiposToken.ID && proxToken == TiposToken.PAR_ESQ){
           System.out.println("FUNCTION CALL");
            ChamadaFuncao();
        } else if(tknAtual == TiposToken.ID){ 
            System.out.println("Exp:Consumiu ID de numero: " + curPos);
            consumirToken();
        }else{
            System.out.println("Lendario");
            erroSin(curPos);
        }
    }
    
    public void ExpressaoRelacional(){
        /*<Expressao_Relacional> ::= <Exp> <Operador_Relacional> <Exp>*/
        Exp();
        OperadorRelacional();
        Exp();
    }
    
    public void ExpressaoBooleana(){
        TiposToken tknAtual;
        System.out.println("Token a ser consumido expBool: "+getTokenAtual());
        Boolean();
        tknAtual = getTokenAtual();
        if(ehOperadorLogico(tknAtual)){
            OperadorLogico();
            tknAtual = getTokenAtual();
            Boolean();
        } else if(ehOperadorRelacional(tknAtual)){
            OperadorRelacional();
            tknAtual = getTokenAtual();
            Boolean();
        } 
    }
    
     public void ExpressaoCaracter(){
        TiposToken tknAtual;
        Caracter();
        tknAtual = getTokenAtual();
        if(ehOperadorLogico(tknAtual)){
            OperadorLogico();
            tknAtual = getTokenAtual();
            Caracter();
        } else if(ehOperadorRelacional(tknAtual)){
            OperadorRelacional();
            tknAtual = getTokenAtual();
            Caracter();
        } 
    }
    
    
    public void ExpressaoNumerica(){
        /*<Expressao_Numerica> ::= <Termo>
                       | <Termo> <Expressao_Numerica_Cont>
        */
        Termo();
        ExpressaoNumericaCont();
    }
    
    public void ExpressaoNumericaCont(){
        /*<Expressao_Numerica_Cont> ::= <Operador_Soma> <Termo> <Expressao_Numerica_Cont>
                            | ?
        */
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.MAIS || tknAtual == TiposToken.MENOS){
            consumirToken();
            tknAtual = getTokenAtual();
            
            Termo();
            tknAtual = getTokenAtual();
            
            ExpressaoNumericaCont();
        } 
    }
    
    public void Termo(){
        Fator();
        TermoCont();
    }
    
    public void TermoCont(){
        /*<Termo_Cont> ::= <Operador_Mult> <Fator> <Termo_Cont>
               | ?
        */
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.MULTI || tknAtual == TiposToken.BARRA || tknAtual == TiposToken.RESTO){
            consumirToken();
            tknAtual = getTokenAtual();
            
            Fator();
            
            tknAtual = getTokenAtual();
            
            TermoCont();
        }
    }
    
    public void Fator(){
       /*<Fator> ::= <Numero>
          | "(" <Expressao_Numerica> ")"
       */
       TiposToken tknAtual = getTokenAtual();
       if(isNumero()){
           Numero();
       } else if(tknAtual == TiposToken.ID){
           System.out.println("Fator Consumiu ID");
           consumirToken();
       }else if(tknAtual == TiposToken.PAR_ESQ){
           consumirToken();
           tknAtual = getTokenAtual();
           ExpressaoNumerica();
           tknAtual = getTokenAtual();
           if(tknAtual == TiposToken.PAR_DIR){
               consumirToken();
           } else erroSin(curPos);
       } else erroSin(curPos);
    }
    
    public void OperadorSoma(){
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.MAIS || tknAtual == TiposToken.MENOS)
            consumirToken();
        else erroSin(curPos);    
    }
    
    public void OperadorMult(){
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.MULTI || tknAtual == TiposToken.BARRA || tknAtual == TiposToken.RESTO)
            consumirToken();
        else erroSin(curPos);
    }
    
    public void OperadorLogico(){
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.E || tknAtual == TiposToken.OU)
            consumirToken();
        else erroSin(curPos);
    }
    
    public void OperadorRelacional(){
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.IGUAL || tknAtual == TiposToken.MAIOR || tknAtual == TiposToken.MENOR || tknAtual == TiposToken.MAIOR_IGUAL || tknAtual == TiposToken.MENOR_IGUAL || tknAtual == TiposToken.DIFERENTE)
            consumirToken();
        else erroSin(curPos);
    }
    
    public void Boolean(){
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.VERDADEIRO || tknAtual == TiposToken.FALSO || tknAtual == TiposToken.ID || isNumero()){
            consumirToken();
        }    
        else erroSin(curPos);
    }
    
    public void Caracter(){
         TiposToken tknAtual = getTokenAtual();
        if(tknAtual.toString().matches("^.$")){
            consumirToken();
        }    
        else erroSin(curPos);
    }
    
   
    public TiposToken lookAhead(int curPos){
        TiposToken proxToken = TiposToken.EOF;
        
        if(curPos < tokenLst.size() && tokenLst.get(curPos) != TiposToken.EOF)
            proxToken = tokenLst.get(curPos+1);
            
        return proxToken;
    }
    
    
    public TiposToken getTokenAtual(){
        TiposToken tokenAtual;
        tokenAtual = tokenLst.get(curPos);
        return tokenAtual;
    }
    
    public void consumirToken(){
        curPos++;
    }
    
    public void consumirDoisTokens(){
        curPos += 2;
    }
    
    public void avancarEnquantoNaoTerminou(){
        TiposToken tknAtual = getTokenAtual();
        while(tknAtual != TiposToken.EOF){
             if(tknAtual == TiposToken.INT || tknAtual == TiposToken.DUP || tknAtual == TiposToken.CARACTER || tknAtual == TiposToken.BOOLEANO)
                consumirToken();
            else erroSin(curPos);
            tknAtual = getTokenAtual(); 
        }
    }
    
     public void estadoAnSin(){
        System.out.println("[curPos = " + curPos + ", tokenLst = " + tokenLst + "]");
    }

    public void erroSin(int curPos){
        String msg = String.format(
                "Erro Sintatico no token de posicao %d: encontrado '%s'\nTokens: %s",
                curPos,
                tokenLst.get(curPos),
                tokenLst
        );
        throw new RuntimeException(msg);
    }
    
    
    private boolean isNumero() {
    TiposToken tknAtual = getTokenAtual();
    boolean ok = false;
    if(tknAtual == TiposToken.MENOS || tknAtual == TiposToken.MAIS || tknAtual == TiposToken.DIGITO_INT || tknAtual == TiposToken.DIGITO_DUP)
        ok = true;
    return ok;    
    }

    private boolean isBooleano(TiposToken token) {
        return token == TiposToken.VERDADEIRO || token == TiposToken.FALSO;
    }
    
    private boolean isCaracter(TiposToken token) {
        return token == TiposToken.CARACTER;
    }
    
    private boolean verificarCaracter(TiposToken token) {
        return token.toString().matches("^.$");
    }
    
    
    private boolean ehOperadorLogico(TiposToken token) {
        boolean ok = false;
        if(token == TiposToken.E || token == TiposToken.OU)
            ok = true;
        return ok;    
    }
    
    private boolean ehOperadorRelacional(TiposToken token) {
        boolean ok = false;
        if(token == TiposToken.IGUAL || token == TiposToken.MAIOR || token == TiposToken.MENOR || token == TiposToken.MAIOR_IGUAL || token == TiposToken.MENOR_IGUAL || token == TiposToken.DIFERENTE)
            ok = true;
        return ok;    
    }
    
    private boolean ehOperadorAritmetico(TiposToken token) {
        boolean ok = false;
        if(token == TiposToken.MAIS || token == TiposToken.MENOS || token == TiposToken.BARRA || token == TiposToken.MULTI || token == TiposToken.RESTO)
            ok = true;
        return ok;    
    }
    
    private boolean ehOperadorAceito(TiposToken token){
        boolean ok = false;
        if(token == TiposToken.PAR_DIR || token == TiposToken.PONT_VIRG || token == TiposToken.VIRG || token == TiposToken.CHAVE_ESQ)
            ok = true;
        return ok;
    }
    
    public boolean verificarBoolean(TiposToken tkn){
        boolean ok = false;
        if(tkn == TiposToken.VERDADEIRO || tkn == TiposToken.FALSO || tkn == TiposToken.ID){
            ok = true;
        }    
        return ok;
    }
    
    
    
    private boolean ehInicioExpressao(TiposToken token){
        boolean ok = false;
        if(isNumero())
            ok = true;
        else if(verificarBoolean(token))
            ok = true;
        return ok;
    }
    
    private boolean ehExpressaoAritmetica(){
        boolean ok = false;
        TiposToken proxToken = lookAhead(curPos);
        if(isNumero() && (!ehOperadorRelacional(proxToken)) && (!ehOperadorLogico(proxToken)))
            ok = true;
        return ok;
    }
    
    
    private boolean ehExpressaoRelacional(){
        boolean ok = false;
        TiposToken tknAtual = getTokenAtual();
        TiposToken proxToken = lookAhead(curPos);
        if(verificarBoolean(tknAtual) && ((ehOperadorRelacional(proxToken)) || (ehOperadorLogico(proxToken))))
            ok = true;
        else if(isNumero() && ((ehOperadorRelacional(proxToken)) || (ehOperadorLogico(proxToken))))  
                ok = true;
        return ok;
    }
    
    private boolean ehInicioInstrucao(){
        boolean ok = false;
        TiposToken tknAtual = getTokenAtual();
        if(tknAtual == TiposToken.ID || tknAtual == TiposToken.SE || tknAtual == TiposToken.PARA ||tknAtual == TiposToken.ENQUANTO
        ||tknAtual == TiposToken.REPITA ||tknAtual == TiposToken.DEVOLVA || ehInicioExpressao(tknAtual))
            ok = true;
        
        return ok;
    }

}
