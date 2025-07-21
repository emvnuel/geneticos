public class Cliente {
    int ponto;
    String nome;
    int demandaKg;
    int janelaInicioHoras;  // Janela de tempo início (em horas desde 6h da manhã)
    int janelaFimHoras;     // Janela de tempo fim
    int tempoAtendimentoMin; // Tempo necessário para atendimento (em minutos)

    public Cliente(int ponto, String nome, int demandaKg, int janelaInicioHoras,
                  int janelaFimHoras, int tempoAtendimentoMin) {
        this.ponto = ponto;
        this.nome = nome;
        this.demandaKg = demandaKg;
        this.janelaInicioHoras = janelaInicioHoras;
        this.janelaFimHoras = janelaFimHoras;
        this.tempoAtendimentoMin = tempoAtendimentoMin;
    }
}
