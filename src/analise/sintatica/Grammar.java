package analise.sintatica;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Grammar
{
      private final Map<String, List<Producoes>> regras = new HashMap<>();
      
      public void adicionarProducao(String naoTerminal, Producoes producao){
          regras.computeIfAbsent(naoTerminal, k -> new ArrayList<>()).add(producao);
      }
      
      public Map<String, List<Producoes>> getProducoes(){
            return regras;
      }
      
       // Método para configurar a gramática
    public void setGrammar() {
        adicionarProducao("S", new Producoes(Arrays.asList("Programa")));
        
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Atrib")));
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Instrucao_Se")));
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Instrucao_Para")));
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Instrucao_Enquanto")));
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Instrucao_Repita")));
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Chamada_Funcao")));
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Exp")));
        adicionarProducao("Instrucao", new Producoes(Arrays.asList("Expressao_Retorno")));
        
        adicionarProducao("Programa", new Producoes(Arrays.asList("Header", "Funcao_Principal", "Bloco", "Constructo_Funcao")));
        adicionarProducao("Funcao_Principal", new Producoes(Arrays.asList("METODO", "Principal", "PAR_ESQ", "PAR_DIR", "DOIS_PONTOS", "Tipo")));
        
        adicionarProducao("Lista_Instrucoes", new Producoes(Arrays.asList("Instrucao", "Lista_Instrucoes")));
        adicionarProducao("Lista_Instrucoes", new Producoes(Collections.emptyList()));
        
        adicionarProducao("Bloco", new Producoes(Arrays.asList("CHAVE_ESQ", "Lista_Instrucoes", "CHAVE_DIR")));
        
        adicionarProducao("Header", new Producoes(Arrays.asList("Cabecalho")));
        
        adicionarProducao("Cabecalho", new Producoes(Arrays.asList("Declaracao_Funcao", "Cabecalho")));
        adicionarProducao("Cabecalho", new Producoes(Arrays.asList("Variavel_Global", "Cabecalho")));
        adicionarProducao("Cabecalho", new Producoes(Collections.emptyList()));
        
        adicionarProducao("Exp", new Producoes(Arrays.asList("ID")));
        adicionarProducao("Exp", new Producoes(Arrays.asList("Expressao_Numerica")));
        adicionarProducao("Exp", new Producoes(Arrays.asList("Expressao_Caracter")));
        adicionarProducao("Exp", new Producoes(Arrays.asList("Expressao_Booleana")));
        
        adicionarProducao("Condicao", new Producoes(Arrays.asList("Condicao_OU")));
        adicionarProducao("Condicao_OU", new Producoes(Arrays.asList("Condicao_E", "Condicao_OU_Cont")));
        adicionarProducao("Condicao_OU_Cont", new Producoes(Arrays.asList("OU", "Condicao_E", "Condicao_OU_Cont")));
        adicionarProducao("Condicao_OU_Cont", new Producoes(Collections.emptyList())); 
        adicionarProducao("Condicao_E", new Producoes(Arrays.asList("Condicao_Simples", "Condicao_E_Cont")));
        adicionarProducao("Condicao_E_Cont", new Producoes(Arrays.asList("E", "Condicao_Simples", "Condicao_E_Cont")));
        adicionarProducao("Condicao_E_Cont", new Producoes(Collections.emptyList()));
        adicionarProducao("Condicao_Simples", new Producoes(Arrays.asList("Expressao_Relacional")));
        adicionarProducao("Condicao_Simples", new Producoes(Arrays.asList("Exp")));
        
        adicionarProducao("Instrucao_Se", new Producoes(Arrays.asList("SE", "PAR_ESQ", "Condicao", "PAR_DIR", "Bloco", "Instrucao_Se_Cont")));
        adicionarProducao("Instrucao_Se_Cont", new Producoes(Arrays.asList("SENAO", "Bloco")));
        adicionarProducao("Instrucao_Se_Cont", new Producoes(Arrays.asList("SENAO", "Instrucao_Se")));
        adicionarProducao("Instrucao_Se_Cont", new Producoes(Collections.emptyList()));
        
        adicionarProducao("Instrucao_Para", new Producoes(Arrays.asList("PARA", "PAR_ESQ", "Atrib", "PONT_VIRG", "Condicao", "PONT_VIRG", "Exp", "PAR_DIR", "Bloco")));
        adicionarProducao("Instrucao_Enquanto", new Producoes(Arrays.asList("ENQUANTO", "PAR_ESQ", "Condicao", "PAR_DIR", "REPITA", "Bloco")));
        adicionarProducao("Instrucao_Repita", new Producoes(Arrays.asList("REPITA", "Bloco", "ENQUANTO", "PAR_ESQ", "Condicao", "PAR_DIR", "PONT_VIRG")));
        
        adicionarProducao("Instrucao_Switch", new Producoes(Arrays.asList("ESCOLHA", "PAR_ESQ", "Exp", "PAR_DIR", "DOIS_PONTOS", "Casos_Escolha", "Padrao")));
        adicionarProducao("Casos_Escolha", new Producoes(Arrays.asList("CASO", "PAR_ESQ", "Exp", "PAR_DIR", "SAIA", "Casos_Escolha")));
        adicionarProducao("Casos_Escolha", new Producoes(Collections.emptyList()));
        adicionarProducao("Padrao", new Producoes(Arrays.asList("CONTINUE", "Bloco")));
        
        adicionarProducao("Declaracao_Variavel", new Producoes(Arrays.asList("ID", "DOIS_PONTOS", "Tipo")));
        adicionarProducao("Variavel_Global", new Producoes(Arrays.asList("HASTAG", "GLOBAL", "ID", "Valor")));
        adicionarProducao("Atrib", new Producoes(Arrays.asList("ID", "Operador_Atribuicao", "Exp", "PONT_VIRG")));
        
        adicionarProducao("Declaracao_Funcao", new Producoes(Arrays.asList("METODO", "ID", "PAR_ESQ", "Parametros", "PAR_DIR", "DOIS_PONTOS", "Tipo", "PONT_VIRG")));
        adicionarProducao("Chamada_Funcao", new Producoes(Arrays.asList("ID", "PAR_ESQ", "Argumentos", "PAR_DIR", "PONT_VIRG")));
        adicionarProducao("Constructo_Funcao", new Producoes(Arrays.asList("Declaracao_Funcao", "Bloco")));
        adicionarProducao("Constructo_Funcao", new Producoes(Collections.emptyList()));
        adicionarProducao("Expressao_Retorno", new Producoes(Arrays.asList("DEVOLVA", "Exp")));        
        
        adicionarProducao("Parametros", new Producoes(Arrays.asList("Lista_Parametros")));
        adicionarProducao("Parametros", new Producoes(Collections.emptyList()));
        adicionarProducao("Lista_Parametros", new Producoes(Arrays.asList("Param", "Lst_Parametros")));
        adicionarProducao("Lst_Parametros", new Producoes(Arrays.asList("VIRG", "Param", "Lst_Parametros")));
        adicionarProducao("Lst_Parametros", new Producoes(Collections.emptyList()));
        adicionarProducao("Param", new Producoes(Arrays.asList("ID", "DOIS_PONTOS", "Tipo")));
        
        adicionarProducao("Argumentos", new Producoes(Arrays.asList("Lista_Argumentos")));
        adicionarProducao("Argumentos", new Producoes(Collections.emptyList()));
        adicionarProducao("Lista_Parametros", new Producoes(Arrays.asList("Exp", "Lst_Argumentos")));
        adicionarProducao("Lst_Parametros", new Producoes(Arrays.asList("VIRG", "Exp", "Lst_Argumentos")));
        adicionarProducao("Lst_Argumentos", new Producoes(Collections.emptyList()));
        
        adicionarProducao("Tipo", new Producoes(Arrays.asList("INT")));
        adicionarProducao("Tipo", new Producoes(Arrays.asList("DUP")));
        adicionarProducao("Tipo", new Producoes(Arrays.asList("CARACTER")));
        adicionarProducao("Tipo", new Producoes(Arrays.asList("BOOLEANO")));
        
        adicionarProducao("Inteiro", new Producoes(Arrays.asList("DIGITO_INT")));
        adicionarProducao("Inteiro", new Producoes(Arrays.asList("MENOS","DIGITO_INT")));
        adicionarProducao("Inteiro", new Producoes(Arrays.asList("MAIS","DIGITO_INT")));
        
        adicionarProducao("Dupla_Precisao", new Producoes(Arrays.asList("DIGITO_DUP")));
        adicionarProducao("Dupla_Precisao", new Producoes(Arrays.asList("MENOS","DIGITO_DUP")));
        adicionarProducao("Dupla_Precisao", new Producoes(Arrays.asList("MAIS","DIGITO_DUP")));
        
        adicionarProducao("Numero", new Producoes(Arrays.asList("Inteiro")));
        adicionarProducao("Numero", new Producoes(Arrays.asList("Dupla_Precisao")));
        
        adicionarProducao("Caracter", new Producoes(Arrays.asList("CARACTER")));
        adicionarProducao("Caracter", new Producoes(Arrays.asList("ID")));
        
        adicionarProducao("Boolean", new Producoes(Arrays.asList("VERDADEIRO")));
        adicionarProducao("Boolean", new Producoes(Arrays.asList("FALSO")));
        adicionarProducao("Boolean", new Producoes(Arrays.asList("ID")));
        
        adicionarProducao("Operador_Relacional", new Producoes(Arrays.asList("IGUAL")));
        adicionarProducao("Operador_Relacional", new Producoes(Arrays.asList("MAIOR")));
        adicionarProducao("Operador_Relacional", new Producoes(Arrays.asList("MENOR")));
        adicionarProducao("Operador_Relacional", new Producoes(Arrays.asList("MAIOR_IGUAL")));
        adicionarProducao("Operador_Relacional", new Producoes(Arrays.asList("MENOR_IGUAL")));
        adicionarProducao("Operador_Relacional", new Producoes(Arrays.asList("DIFERENTE")));
        
        adicionarProducao("Operador_Logico", new Producoes(Arrays.asList("E")));
        adicionarProducao("Operador_Logico", new Producoes(Arrays.asList("OU")));
        
        adicionarProducao("Operador_Soma", new Producoes(Arrays.asList("MAIS")));
        adicionarProducao("Operador_Soma", new Producoes(Arrays.asList("MENOS")));
        
        adicionarProducao("Operador_Mult", new Producoes(Arrays.asList("MULTI")));
        adicionarProducao("Operador_Mult", new Producoes(Arrays.asList("BARRA")));
        adicionarProducao("Operador_Mult", new Producoes(Arrays.asList("RESTO")));
        
        adicionarProducao("Operador_Atribuicao", new Producoes(Arrays.asList("ATRIBUICAO")));
        
        adicionarProducao("Expressao_Booleana", new Producoes(Arrays.asList("Boolean", "Operador_Logico", "Boolean")));
        adicionarProducao("Expressao_Booleana", new Producoes(Arrays.asList("Boolean", "Operador_Relacional", "Boolean")));
        adicionarProducao("Expressao_Booleana", new Producoes(Arrays.asList("Boolean")));
        
        adicionarProducao("Expressao_Relacional", new Producoes(Arrays.asList("Exp", "Operador_Relacional", "Exp")));
 
        adicionarProducao("Expressao_Caracter", new Producoes(Arrays.asList("Caracter", "Operador_Logico", "Caracter")));
        adicionarProducao("Expressao_Caracter", new Producoes(Arrays.asList("Caracter", "Operador_Relacional", "Caracter")));
        adicionarProducao("Expressao_Caracter", new Producoes(Arrays.asList("Caracter")));
        
        adicionarProducao("Expressao_Numerica", new Producoes(Arrays.asList("Termo")));
        adicionarProducao("Expressao_Numerica", new Producoes(Arrays.asList("Termo", "Expressao_Numerica_Cont")));
        adicionarProducao("Expressao_Numerica_Cont", new Producoes(Arrays.asList("Operador_Soma", "Termo", "Expressao_Numerica_Cont")));
        adicionarProducao("Expressao_Numerica_Cont", new Producoes(Collections.emptyList()));
        
        adicionarProducao("Termo", new Producoes(Arrays.asList("Fator", "Termo_Cont")));
        adicionarProducao("Termo_Cont", new Producoes(Arrays.asList("Operador_Mult", "Fator", "Termo_Cont")));
        adicionarProducao("Termo_Cont", new Producoes(Collections.emptyList()));
         
        adicionarProducao("Fator", new Producoes(Arrays.asList("Numero")));
        adicionarProducao("Fator", new Producoes(Arrays.asList("PAR_ESQ","Expressao_Numerica", "PAR_DIR")));
    
        adicionarProducao("Valor", new Producoes(Arrays.asList("Numero")));
        adicionarProducao("Valor", new Producoes(Arrays.asList("Boolean")));
        adicionarProducao("Valor", new Producoes(Arrays.asList("Caracter")));
        adicionarProducao("Valor", new Producoes(Arrays.asList("ID")));
        
    }
      
       public void printGrammar() {
        for (String nonTerminal : regras.keySet()) {
            for (Producoes p : regras.get(nonTerminal)) {
                System.out.println(nonTerminal + " -> " + p);
            }
        }
    }
}
