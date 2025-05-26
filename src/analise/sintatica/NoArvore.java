package analise.sintatica;

public class NoArvore {
    public String valor;
    public NoArvore esquerdo;
    public NoArvore direito;

    public NoArvore(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        if (esquerdo == null && direito == null) {
            return valor;
        }
        return "[" + esquerdo + " " + valor + " " + direito + "]";
    }
}
