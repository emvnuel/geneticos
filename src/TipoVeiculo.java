public class TipoVeiculo {

    String nome;
    int capacidadeKg;
    double custoKm;
    int tempoMaximoHoras;
    double velocidadeMediaKmH;
        
    public TipoVeiculo(String nome, int capacidadeKg, double custoKm,
                           int tempoMaximoHoras, double velocidadeMediaKmH) {
            this.nome = nome;
            this.capacidadeKg = capacidadeKg;
            this.custoKm = custoKm;
            this.tempoMaximoHoras = tempoMaximoHoras;
            this.velocidadeMediaKmH = velocidadeMediaKmH;
    }
}
