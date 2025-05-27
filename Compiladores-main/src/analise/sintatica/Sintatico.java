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
    
   public void S(List<TiposToken> tokenLst) {
    Programa(tokenLst);  // chama o método para a regra S -> Programa
    }    
    
    public void Programa(List<TiposToken> tokenLst) {
    //Header(tokenLst);           // Header
    Funcao_Principal(tokenLst); // Funcao_Principal
    Bloco(tokenLst);            // Bloco
    Constructo_Funcao(tokenLst); // Constructo_Funcao
    }

    
    
    public void Funcao_Principal(List<TiposToken> tokenLst) {
    TiposToken tokenAtual = tokenLst.get(curPos);
    if (curPos < tokenLst.size()) {
        if (tokenAtual == TiposToken.METODO) {
            curPos++;
            tokenAtual = tokenLst.get(curPos);
            if (tokenAtual == TiposToken.PRINCIPAL) {
                curPos++;
                tokenAtual = tokenLst.get(curPos);
                if (tokenAtual == TiposToken.PAR_ESQ) {
                    curPos++;
                    tokenAtual = tokenLst.get(curPos);
                    if (tokenAtual == TiposToken.PAR_DIR) {
                        curPos++;
                        tokenAtual = tokenLst.get(curPos);
                        if (tokenAtual == TiposToken.DOIS_PONTOS) {
                            curPos++;
                            tokenAtual = tokenLst.get(curPos);
                            Tipo(tokenLst); // assume que já tem método Tipo()
                        } else {
                            erroSin(tokenLst, curPos); //erro: esperado DOIS_PONTOS
                        }
                    } else {
                        erroSin(tokenLst, curPos);// erro: esperado PAR_DIR
                    }
                } else {
                    erroSin(tokenLst, curPos);// erro: esperado PAR_ESQ
                }
            } else {
                erroSin(tokenLst, curPos);// erro: esperado PRINCIPAL
            }
        } else {
            erroSin(tokenLst, curPos);// erro: esperado METODO
        }
    } else {
        erroSin(tokenLst, curPos);// erro: fim inesperado dos tokens
    }
    }

    
    
    public void Atrib(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if(tokenAtual == TiposToken.ID){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
        
        tokenAtual = tokenLst.get(curPos);
        
        if(tokenAtual == TiposToken.ATRIBUICAO){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }        
        
        Exp(tokenLst);
        
        tokenAtual = tokenLst.get(curPos);
         if(tokenAtual == TiposToken.PONT_VIRG){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }     
        
    }
    
    public void Condicao_Simples(List<TiposToken> tokenLst) {
        if (verificarInicioDeExpressaoRelacional(tokenLst)) {
            Expressao_Relacional(tokenLst);
        } else {
            Exp(tokenLst);
        }
    }
    
    public void Condicao_E_Cont(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.E) {
            curPos++; // consome 'e'
            Condicao_Simples(tokenLst);
            Condicao_E_Cont(tokenLst); // chamada recursiva
        }
        // senão: ? (não faz nada)
    }
    
    public void Condicao_E(List<TiposToken> tokenLst) {
        Condicao_Simples(tokenLst);
        Condicao_E_Cont(tokenLst);
    }

    
    public void Condicao_OU_Cont(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.OU) {
            curPos++; // consome 'ou'
            Condicao_E(tokenLst);
            Condicao_OU_Cont(tokenLst); // chamada recursiva
        }
        // senão: ? (não faz nada)
    }


    public void Condicao_OU(List<TiposToken> tokenLst) {
    Condicao_E(tokenLst);
    Condicao_OU_Cont(tokenLst);
    }

    
    public void Condicao(List<TiposToken> tokenLst) {
    Condicao_OU(tokenLst);
    }
    
    public void Instrucao(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        TiposToken prox = lookAhead(tokenLst, curPos);
        
        switch (tokenAtual) {
            case ID:
                if (prox == TiposToken.ATRIBUICAO) {
                    Atrib(tokenLst); // ID := ...
                } else if (prox == TiposToken.PAR_ESQ) {
                    Chamada_Funcao(tokenLst); // ID(...)
                } else {
                    Exp(tokenLst); // expressão simples
                }
                break;
    
            case SE:
                Instrucao_Se(tokenLst);
                break;
    
            case PARA:
                Instrucao_Para(tokenLst);
                break;
    
            case ENQUANTO:
                Instrucao_Enquanto(tokenLst);
                break;
    
            case REPITA:
                Instrucao_Repita(tokenLst);
                break;
    
            case DEVOLVA:
                Expressao_Retorno(tokenLst);
                break;
    
            default:
                // Se não for nenhuma instrução reconhecida, tenta como expressão
                Exp(tokenLst);
                break;
        }
    }

    
    
    public void Instrucao_Se(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.SE) {
            curPos++; // consome 'se'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        tokenAtual = tokenLst.get(curPos);
        
        if (tokenAtual == TiposToken.PAR_ESQ) {
            curPos++; // consome '('
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        Condicao(tokenLst); // avalia condição
    
        tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.PAR_DIR) {
            curPos++; // consome ')'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        Bloco(tokenLst); // executa o bloco do if
    
        Instrucao_Se_Cont(tokenLst); // trata o else (ou não)
    }
    
    public void Lista_Instrucoes(List<TiposToken> tokenLst) {
        if (curPos < tokenLst.size()) {
            TiposToken tokenAtual = tokenLst.get(curPos);
    
            // Tokens que iniciam uma instrução válida
            if (tokenAtual == TiposToken.ID ||
                tokenAtual == TiposToken.SE ||
                tokenAtual == TiposToken.PARA ||
                tokenAtual == TiposToken.ENQUANTO ||
                tokenAtual == TiposToken.REPITA ||
                tokenAtual == TiposToken.DEVOLVA) {
    
                Instrucao(tokenLst);
                Lista_Instrucoes(tokenLst); // chamada recursiva
            }
            // caso contrário, ? (vazio): não faz nada
        }
    }
    
    public void Bloco(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.CHAVE_ESQ) {
            curPos++; // consome '{'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        Lista_Instrucoes(tokenLst); // pode ser vazio ou uma lista
        tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.CHAVE_DIR) {
            curPos++; // consome '}'
        } else {
            erroSin(tokenLst, curPos);
        }
    }




    public void Instrucao_Se_Cont(List<TiposToken> tokenLst) {
        TiposToken  tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.SENAO) {
            curPos++; // consome 'senao'
            tokenAtual = tokenLst.get(curPos);
            if (tokenAtual == TiposToken.SE) {
                Instrucao_Se(tokenLst); // senao seguido de outro 'se' (else if)
            } else {
                Bloco(tokenLst); // senao seguido de bloco normal
            }
        }
        // senão: ? (nada a fazer)
    }
    
    public void Instrucao_Para(List<TiposToken> tokenLst) {
    TiposToken  tokenAtual = tokenLst.get(curPos);    
    if (tokenAtual == TiposToken.PARA) {
        curPos++; // consome 'para'
    } else {
        erroSin(tokenLst, curPos);
        return;
    }
    
    tokenAtual = tokenLst.get(curPos); 

    if (tokenAtual == TiposToken.PAR_ESQ) {
        curPos++; // consome '('
    } else {
        erroSin(tokenLst, curPos);
        return;
    }

    Atrib(tokenLst); // Atribuição inicial
    tokenAtual = tokenLst.get(curPos); 
    if (tokenAtual == TiposToken.PONT_VIRG) {
        curPos++; // consome ';'
    } else {
        erroSin(tokenLst, curPos);
        return;
    }

    Condicao(tokenLst); // Condição
    tokenAtual = tokenLst.get(curPos); 
    if (tokenAtual == TiposToken.PONT_VIRG) {
        curPos++; // consome ';'
    } else {
        erroSin(tokenLst, curPos);
        return;
    }

    Exp(tokenLst); // Expressão de incremento ou passo
    tokenAtual = tokenLst.get(curPos); 
    if (tokenAtual == TiposToken.PAR_DIR) {
        curPos++; // consome ')'
    } else {
        erroSin(tokenLst, curPos);
        return;
    }

    Bloco(tokenLst); // corpo do laço
    }
    
    public void Instrucao_Enquanto(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.ENQUANTO) {
            curPos++; // consome 'enquanto'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }

        tokenAtual = tokenLst.get(curPos);
        
        if (tokenAtual == TiposToken.PAR_ESQ) {
            curPos++; // consome '('
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        Condicao(tokenLst); // analisa a condição
        tokenAtual = tokenLst.get(curPos);        
        
        if (tokenAtual == TiposToken.PAR_DIR){
            curPos++; // consome ')'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
        
        tokenAtual = tokenLst.get(curPos);     
    
        if (tokenAtual== TiposToken.REPITA) {
            curPos++; // consome 'repita'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
        
        Bloco(tokenLst); // corpo do laço
    }
    
    public void Instrucao_Repita(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.REPITA) {
            curPos++; // consome 'repita'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        Bloco(tokenLst); // executa o bloco
        tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.ENQUANTO) {
            curPos++; // consome 'enquanto'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        tokenAtual = tokenLst.get(curPos);
        
        if (tokenAtual == TiposToken.PAR_ESQ) {
            curPos++; // consome '('
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        Condicao(tokenLst); // avalia a condição
        tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.PAR_DIR) {
            curPos++; // consome ')'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        tokenAtual = tokenLst.get(curPos);
        
        if (tokenAtual == TiposToken.PONT_VIRG) {
            curPos++; // consome ';'
        } else {
            erroSin(tokenLst, curPos);
        }
    }


    
    public void DeclaracaoVariavel(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if(tokenAtual == TiposToken.ID){
            curPos++;
        } else{
            erroSin(tokenLst, curPos);
        }
            
        tokenAtual = tokenLst.get(curPos);
        if(tokenAtual == TiposToken.DOIS_PONTOS){
            curPos++;
        } else {
            erroSin(tokenLst, curPos);
        }
        Tipo(tokenLst);
        tokenAtual = tokenLst.get(curPos);
        if(tokenAtual == TiposToken.PONT_VIRG){
        curPos++;
        } else{
             erroSin(tokenLst, curPos);
        }
    }
    
    public void Declaracao_Funcao(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.METODO) {
            curPos++; // consome 'metodo'
        } else {
            erroSin(tokenLst, curPos);
        }
        
        tokenAtual = tokenLst.get(curPos);
    
        if (tokenAtual == TiposToken.ID) {
            curPos++; // consome o nome da função (ID)
        } else {
            erroSin(tokenLst, curPos);
        }
    
        tokenAtual = tokenLst.get(curPos);
        
        if (tokenAtual == TiposToken.PAR_ESQ) {
            curPos++; // consome '('
        } else {
            erroSin(tokenLst, curPos);
        }
    
        Parametros(tokenLst); // pode ser vazio ou com parâmetros
        tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.PAR_DIR) {
            curPos++; // consome ')'
        } else {
            erroSin(tokenLst, curPos);
        }
        tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.DOIS_PONTOS) {
            curPos++; // consome ':'
        } else {
            erroSin(tokenLst, curPos);
        }
    
        Tipo(tokenLst); // consome o tipo de retorno
        tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.PONT_VIRG) {
            curPos++; // consome ';'
        } else {
            erroSin(tokenLst, curPos);
        }
     }
     
     public void Constructo_Funcao(List<TiposToken> tokenLst) {
     TiposToken tokenAtual = tokenLst.get(curPos);
        // Se o próximo token é 'METODO', temos uma função a ser declarada
        if (tokenAtual == TiposToken.METODO) {
            Declaracao_Funcao(tokenLst);
            Bloco(tokenLst);
            
            // Recursivamente aceita múltiplos constructos de função (opcional)
            Constructo_Funcao(tokenLst);
        }
        // Caso contrário, ? — não faz nada (nenhuma função)
    }

    public void Header(List<TiposToken> tokenLst) {
    Cabecalho(tokenLst);
    }
    
   public void Cabecalho(List<TiposToken> tokenLst) {
    if (curPos < tokenLst.size()) {
        TiposToken tokenAtual = tokenLst.get(curPos);

        if (tokenAtual == TiposToken.METODO) {
            Declaracao_Funcao(tokenLst);
            Cabecalho(tokenLst);  // chamada recursiva
        } 
        // Caso contrário, não faz nada (?)
    }
    // Se chegou aqui, é fim da lista ou token inesperado — método termina sem return explícito
    }

        
     public void Chamada_Funcao(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos); 
        if (tokenAtual == TiposToken.ID) {
            curPos++; // consome ID
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        tokenAtual = tokenLst.get(curPos); 
        
        if (tokenAtual == TiposToken.PAR_ESQ) {
            curPos++; // consome '('
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
    
        Argumentos(tokenLst); // pode ser vazio ou com argumentos
        tokenAtual = tokenLst.get(curPos); 
        if (tokenAtual == TiposToken.PAR_DIR) {
            curPos++; // consome ')'
        } else {
            erroSin(tokenLst, curPos);
            return;
        }
        
        tokenAtual = tokenLst.get(curPos); 
    
        if (tokenAtual== TiposToken.PONT_VIRG) {
            curPos++; // consome ';'
        } else {
            erroSin(tokenLst, curPos);
        }
    }

     
    
    public void Lst_Parametros(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.VIRG) {
            curPos++; // consome a vírgula
    
            Param(tokenLst); // consome o próximo parâmetro
    
            Lst_Parametros(tokenLst); // chama recursivamente (pode haver mais vírgulas)
        }
        // Caso contrário é ? (vazio) — simplesmente retorna sem fazer nada
    }
    
    
    public void Parametros(List<TiposToken> tokenLst){
        // Se o próximo token é um ID, então há parâmetros (Lista_Parametros)
        TiposToken tokenAtual = tokenLst.get(curPos);
        if (tokenAtual == TiposToken.ID) {
            Lista_Parametros(tokenLst);
        }
        // Caso contrário, é ? (lista vazia) — não faz nada
    }
    
    public void Argumentos(List<TiposToken> tokenLst) {
    // Verifica se começa com uma expressão (ID, número, booleano, etc.)
    TiposToken tokenAtual = tokenLst.get(curPos);
    
    if (tokenAtual == TiposToken.ID || tokenAtual == TiposToken.DIGITO_INT ||
        tokenAtual == TiposToken.DIGITO_DUP || tokenAtual == TiposToken.VERDADEIRO ||
        tokenAtual == TiposToken.FALSO || tokenAtual == TiposToken.CARACTER ||
        tokenAtual == TiposToken.PAR_ESQ) {
        
        Lista_Argumentos(tokenLst);  // há argumentos
    }
    // Caso contrário: ? (lista vazia)
    }


    
    public void Lista_Parametros(List<TiposToken> tokenLst) {
        Param(tokenLst);         // Primeiro parâmetro obrigatório
        Lst_Parametros(tokenLst); // Continua a lista (ou termina, se for vazio)
    }
    
    public void Lista_Argumentos(List<TiposToken> tokenLst) {
    Exp(tokenLst);               // consome o primeiro argumento
    Lst_Argumentos(tokenLst);    // trata os demais
    }

    
    
    public void Lst_Argumentos(List<TiposToken> tokenLst) {
    if (curPos < tokenLst.size() && tokenLst.get(curPos) == TiposToken.VIRG) {
        curPos++;  // consome a vírgula
        Exp(tokenLst);  // consome o próximo argumento
        Lst_Argumentos(tokenLst);  // chamada recursiva
    }
    // senão: ? — termina a lista
    }


    
    
    public void Expressao_Retorno(List<TiposToken> tokenLst){
        TiposToken tokenAtual = tokenLst.get(curPos);
        if(tokenAtual == TiposToken.DEVOLVA)
            curPos++;
        else
            erroSin(tokenLst, curPos);
            
        Exp(tokenLst);
        
        tokenAtual = tokenLst.get(curPos);
        
        if(tokenAtual == TiposToken.PONT_VIRG)
            curPos++;
        else
            erroSin(tokenLst, curPos);
    }
    
    
    
    public void Exp(List<TiposToken> tokenLst) {
        TiposToken tokenAtual = tokenLst.get(curPos);

        if (tokenAtual == TiposToken.ID) {
            // Lookahead para decidir se é uma expressão mais complexa
            TiposToken prox = lookAhead(tokenLst, curPos + 1);
    
            // Pode ser uma chamada de função ou variável direta
            // Pode ser também parte de uma Expressao_Booleana ou Expressao_Caracter
    
            // Exemplo: ID OU ID ? booleano
            if (prox == TiposToken.OU || prox == TiposToken.E || 
                prox == TiposToken.IGUAL || prox == TiposToken.DIFERENTE ||
                prox == TiposToken.MAIOR || prox == TiposToken.MENOR ||
                prox == TiposToken.MAIOR_IGUAL || prox == TiposToken.MENOR_IGUAL) {
                Expressao_Booleana(tokenLst);
            }
    
            // Exemplo: ID + ID ? numérica
            else if (prox == TiposToken.MAIS || prox == TiposToken.MENOS ||
                     prox == TiposToken.MULTI || prox == TiposToken.BARRA || prox == TiposToken.RESTO) {
                Expressao_Numerica(tokenLst);
            }
    
            // Pode ser apenas ID ou caractere lógico
            else {
                curPos++;
            }
        }
    
        else if (tokenAtual == TiposToken.DIGITO_INT || tokenAtual == TiposToken.DIGITO_DUP ||
                 tokenAtual == TiposToken.MAIS || tokenAtual == TiposToken.MENOS || tokenAtual == TiposToken.PAR_ESQ) {
            Expressao_Numerica(tokenLst);
        }
    
        else if (tokenAtual == TiposToken.CARACTER) {
            Expressao_Caracter(tokenLst);
        }
    
        else if (tokenAtual == TiposToken.VERDADEIRO || tokenAtual == TiposToken.FALSO) {
            Expressao_Booleana(tokenLst);
        }
    
        else {
            erroSin(tokenLst, curPos);  // Token inesperado
        }
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
        Exp(tokenLst);  // consome a primeira expressão

        if (curPos < tokenLst.size()) {
            TiposToken operador = tokenLst.get(curPos);
    
            if (verificarOperadorRelacional(operador)) {
                curPos++;  // consome o operador relacional
            } else {
                erroSin(tokenLst, curPos);  // operador relacional esperado
            }
        } else {
            erroSin(tokenLst, curPos);  // fim da lista sem operador
        }
        Exp(tokenLst);  // consome a segunda expressão     
          
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
    
    public boolean verificarOperadorLogico(TiposToken token) {
    return token == TiposToken.E || token == TiposToken.OU;
    }

    public boolean verificarOperadorRelacional(TiposToken token) {
    return token == TiposToken.IGUAL ||
           token == TiposToken.MAIOR ||
           token == TiposToken.MENOR ||
           token == TiposToken.MAIOR_IGUAL ||
           token == TiposToken.MENOR_IGUAL ||
           token == TiposToken.DIFERENTE;
    }
    
    
    public boolean verificarOperadorSoma(TiposToken token) {
    return token == TiposToken.MAIS ||
           token == TiposToken.MENOS;
    }
    
    public boolean verificarOperadorMult(TiposToken token) {
    return token == TiposToken.MULTI ||
           token == TiposToken.BARRA ||
           token == TiposToken.RESTO;
    }
    
    private boolean verificarInicioDeExpressaoRelacional(List<TiposToken> tokenLst) {    
        TiposToken atual = tokenLst.get(curPos);
        TiposToken proximo = tokenLst.get(curPos + 1);
    
        // Verifica se é uma Exp seguida de operador relacional
        boolean expPossivel = atual == TiposToken.ID ||
                              atual == TiposToken.DIGITO_INT ||
                              atual == TiposToken.DIGITO_DUP ||
                              atual == TiposToken.VERDADEIRO ||
                              atual == TiposToken.FALSO ||
                              atual == TiposToken.CARACTER ||
                              atual == TiposToken.MAIS ||
                              atual == TiposToken.MENOS ||
                              atual == TiposToken.PAR_ESQ;
    
        boolean operadorRelacional = proximo == TiposToken.IGUAL ||
                                     proximo == TiposToken.DIFERENTE ||
                                     proximo == TiposToken.MAIOR ||
                                     proximo == TiposToken.MENOR ||
                                     proximo == TiposToken.MAIOR_IGUAL ||
                                     proximo == TiposToken.MENOR_IGUAL;
    
        return expPossivel && operadorRelacional;
    }


      
    public TiposToken lookAhead(List<TiposToken> tokenLst, int curPos){
        TiposToken proxToken = TiposToken.EOF;
        
        if(curPos < tokenLst.size() && tokenLst.get(curPos) != TiposToken.EOF)
            proxToken = tokenLst.get(curPos+1);
            
        return proxToken;
    }
}
