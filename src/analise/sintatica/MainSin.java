package analise.sintatica;

import analise.lexico.Lexico;
import analise.lexico.TiposToken;
import analise.lexico.Token;

import java.util.List;
import java.util.stream.Collectors;

public class MainSin {
    public static void main(String[] args) {
       String codigoFonte = """
        metodo soma(a: int, b: int): int;
        metodo ehpar(n: int): booleano;
        
        metodo principal(): int {
            x: int;
            y: int;
            j: int;
            cont: int;
            resultado: booleano;
        
            x << 10;
            y << soma(x, 5);
            i: dup;
            j << (antedegumeon + 5) * (x - 2) / 4;
            se (ehpar(y) e x < y ou ehpar(x) = ehpar(b)) {
                resultado << verdadeiro;
                cont << 0;
                para(i << 1.2; i <= 10; i << i + 0.2){
                    cont << cont + 1;
                }
            } senao {
                resultado << falso;
            }
            
            se(cont > 10){
                i << 0;
            }
        
            devolva 0;
        }
        
        metodo soma(a: int, b: int): int {
            r: int;
            r << a + b;
            devolva r;
        }
        
        metodo ehpar(n: int): booleano {
            mod: int;
            mod << n resto 2;
        
            se (mod = 0) {
                devolva verdadeiro;
            } senao {
                devolva falso;
            }
        }

        """;
        //a > c e b + a !> c ou 1 > 10]
        //a > d e 2 e b+1 !> a ou 1 + 10 < 2


        Lexico lexico = new Lexico();
        lexico.defCodFonte(codigoFonte);
        List<Token> tokens = lexico.anLex();

        List<TiposToken> tiposTokens = tokens.stream()
                .map(t -> t.tipo)
                .collect(Collectors.toList());
    
        Sintatico sin = new Sintatico(tiposTokens);        
        System.out.println(tiposTokens);
        try {
            //System.out.println("Token de Numero: " + (64) + "\nTkn: "+tiposTokens.get(64) + "\n\n\n");
            sin.Programa();
            System.out.println("Analise Sintatica Concluida com sucesso.");
            sin.estadoAnSin();
            System.out.println("Token de Numero: " + (tiposTokens.size()-1) + "\nTkn: "+tiposTokens.get(tiposTokens.size()-1));
            
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
