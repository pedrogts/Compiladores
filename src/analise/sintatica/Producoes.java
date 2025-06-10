package analise.sintatica;
import java.util.List;


public class Producoes
{
    private List<String> simbolos;
    
    public Producoes(List<String> simbolos){
        this.simbolos = simbolos;
    }
 
    public List<String> getProducoes(){
        return simbolos;
    }
    
    
    public String toString() {
        return String.join(" ", simbolos);
    }

}
