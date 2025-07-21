import java.util.ArrayList;

class Solucao {
    ArrayList<Rota> rotas;
    double fitness;

    public Solucao() {
        this.rotas = new ArrayList<>();
        this.fitness = 0.0;
    }

    public Solucao(ArrayList<Rota> rotas) {
        this.rotas = new ArrayList<>(rotas);
        this.fitness = 0.0;
    }
}
