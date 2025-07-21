import java.util.ArrayList;

class Rota {
    ArrayList<Integer> clientes;
    int tipoVeiculo; // índice do tipo de veículo

    public Rota(int tipoVeiculo) {
        this.clientes = new ArrayList<>();
        this.tipoVeiculo = tipoVeiculo;
    }

    public Rota(ArrayList<Integer> clientes, int tipoVeiculo) {
        this.clientes = new ArrayList<>(clientes);
        this.tipoVeiculo = tipoVeiculo;
    }
}
