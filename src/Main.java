import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

public class Main {

    private static final int POPULACAO_TAMANHO = 50;
    private static final int GERACOES = 100000;
    private static final double TAXA_MUTACAO = 0.15;

    private static final Random random = new Random();
    public static final double[][] DISTANCIAS = MatrizDistanciasSalvador.DISTANCIAS;

    public static final TipoVeiculo[] FROTA = {
            new TipoVeiculo("Moto", 30, 0.5, 6, 60.0),
            new TipoVeiculo("Van", 800, 1.5, 10, 30.0),
            new TipoVeiculo("Caminhão", 1200, 2.0, 12, 25.0)
    };

    public static final Cliente[] CLIENTES = {
        new Cliente(1, "Escritório Advocacia Pelourinho", 8, 6, 12, 45),
        new Cliente(2, "Hotel Barra - Suprimentos", 180, 6, 10, 45),
        new Cliente(3, "Farmácia Rio Vermelho", 12, 10, 14, 25),
        new Cliente(4, "Shopping Pituba - Estoque", 320, 9, 18, 60),
        new Cliente(5, "Clínica Ondina", 15, 6, 16, 50),
        new Cliente(6, "Universidade Canela", 280, 8, 17, 75),
        new Cliente(7, "Empresa Aeroporto - Carga", 750, 10, 17, 90),
        new Cliente(8, "Livraria Rodoviária", 6, 13, 17, 20),
        new Cliente(9, "Pousada Itapuã", 160, 6, 15, 35),
        new Cliente(10, "Floricultura Bonfim", 18, 6, 18, 30),
        new Cliente(11, "Hotel Stella Maris", 140, 9, 16, 30),
        new Cliente(12, "Distribuidora Flamengo", 420, 8, 17, 80),
        new Cliente(13, "Ótica Piatã", 3, 10, 20, 55),
        new Cliente(14, "Fábrica Lauro de Freitas", 950, 6, 16, 180),
        new Cliente(15, "Laboratório Brotas", 14, 8, 18, 45),
        new Cliente(16, "Supermercado Federação", 240, 8, 20, 60),
        new Cliente(17, "Escritório Contábil Graça", 5, 9, 17, 30),
        new Cliente(18, "Consultório Médico Vitória", 9, 7, 17, 35),
        new Cliente(19, "Hotel Corredor da Vitória", 130, 6, 20, 40),
        new Cliente(20, "Universidade Campo Grande", 380, 8, 18, 90),
        new Cliente(21, "Hospital Nazaré", 200, 6, 22, 45),
        new Cliente(22, "Farmácia Barris", 11, 7, 19, 20),
        new Cliente(23, "Loja de Eletrônicos Tororó", 16, 9, 19, 50),
        new Cliente(24, "Shopping Iguatemi", 520, 10, 22, 75),
        new Cliente(25, "Distribuidora Paralela", 680, 8, 18, 120),
        new Cliente(26, "Centro Logístico Cajazeiras", 780, 6, 17, 90),
        new Cliente(27, "Papelaria Cabula", 7, 8, 17, 60),
        new Cliente(28, "Mercado Liberdade", 150, 7, 19, 40),
        new Cliente(29, "Indústria Subúrbio Ferroviário", 1100, 6, 16, 150)
    };


    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE OTIMIZAÇÃO DE ROTAS - ENTREGA RÁPIDA ===\n");

        // Exibir informações dos clientes
        System.out.println("CLIENTES A SEREM ATENDIDOS:");
        for (Cliente cliente : CLIENTES) {
            System.out.printf("Cliente %d: %s - Demanda: %dkg - Janela: %dh-%dh - Atendimento: %dmin\n",
                    cliente.ponto, cliente.nome, cliente.demandaKg,
                    cliente.janelaInicioHoras, cliente.janelaFimHoras, cliente.tempoAtendimentoMin);
        }

        System.out.println("\nFROTA DISPONÍVEL:");
        for (int i = 0; i < FROTA.length; i++) {
            TipoVeiculo veiculo = FROTA[i];
            System.out.printf("%d. %s - Capacidade: %dkg - Custo: R$%.2f/km - Tempo máx: %dh - Velocidade: %.1fkm/h\n",
                    i, veiculo.nome, veiculo.capacidadeKg, veiculo.custoKm,
                    veiculo.tempoMaximoHoras, veiculo.velocidadeMediaKmH);
        }

        System.out.println("\n=== EXECUTANDO ALGORITMO GENÉTICO ===\n");

        // Gerar população inicial
        ArrayList<Solucao> populacao = gerarPopulacao();

        // Evolução
        for (int geracao = 0; geracao < GERACOES; geracao++) {
            // Avaliar população
            for (Solucao solucao : populacao) {
                solucao.fitness = avaliar(solucao);
            }

            // Ordenar por fitness (melhor fitness = menor custo)
            Collections.sort(populacao, Comparator.comparingDouble(s -> s.fitness));

            if (geracao % 20 == 0) {
                System.out.printf("Geração %d - Melhor fitness: %.2f\n", geracao, populacao.get(0).fitness);
            }

            // Criar nova população
            ArrayList<Solucao> novaPopulacao = new ArrayList<>();

            // Elitismo - manter os 20% melhores
            int elites = POPULACAO_TAMANHO / 5;
            for (int i = 0; i < elites; i++) {
                novaPopulacao.add(new Solucao(populacao.get(i).rotas));
            }

            // Crossover para completar a população
            while (novaPopulacao.size() < POPULACAO_TAMANHO) {
                int pai1Idx = selecionarPorTorneio(populacao);
                int pai2Idx = selecionarPorTorneio(populacao);

                Solucao filho = crossover(populacao.get(pai1Idx), populacao.get(pai2Idx));
                novaPopulacao.add(filho);
            }

            // Mutação
            for (int i = elites; i < novaPopulacao.size(); i++) {
                if (random.nextDouble() < TAXA_MUTACAO) {
                    novaPopulacao.set(i, mutacao(novaPopulacao.get(i)));
                }
            }

            populacao = novaPopulacao;
        }

        // Avaliar população final
        for (Solucao solucao : populacao) {
            solucao.fitness = avaliar(solucao);
        }
        Collections.sort(populacao, Comparator.comparingDouble(s -> s.fitness));

        // Exibir melhor solução
        Solucao melhor = populacao.get(0);
        System.out.println("\n=== MELHOR SOLUÇÃO ENCONTRADA ===");
        System.out.printf("Fitness (custo total): %.2f\n\n", melhor.fitness);

        exibirSolucao(melhor);
    }

    private static double calcularTempoRota(int[] rota, double[][] distancias,
                                            Cliente[] clientes, double velocidadeMediaKmH) {
        if (rota.length == 0) return 0.0;

        double tempoTotal = 0.0;

        double distanciaTotal = MatrizDistanciasSalvador.calcularDistanciaRota(rota, distancias, 0);
        tempoTotal += distanciaTotal / velocidadeMediaKmH;

        for (int clienteIdx : rota) {
            Cliente cliente = encontrarCliente(clienteIdx, clientes);
            if (cliente != null) {
                tempoTotal += cliente.tempoAtendimentoMin / 60.0;
            }
        }

        return tempoTotal;
    }

    private static double calcularPenalidadeJanelaTempo(int[] rota, Cliente[] clientes) {
        double penalidade = 0.0;
        double tempoAtual = 6.0; // Começar às 6h da manhã

        for (int clienteIdx : rota) {
            Cliente cliente = encontrarCliente(clienteIdx, clientes);
            if (cliente != null) {
                // Se chegou antes da janela, esperar
                if (tempoAtual < cliente.janelaInicioHoras) {
                    tempoAtual = cliente.janelaInicioHoras;
                }

                // Se chegou depois da janela, penalizar
                if (tempoAtual > cliente.janelaFimHoras) {
                    penalidade += (tempoAtual - cliente.janelaFimHoras) * 100.0;
                }

                // Adicionar tempo de atendimento
                tempoAtual += cliente.tempoAtendimentoMin / 60.0;
            }
        }

        return penalidade;
    }

    private static ArrayList<Solucao> gerarPopulacao() {
        ArrayList<Solucao> populacao = new ArrayList<>();

        for (int i = 0; i < POPULACAO_TAMANHO; i++) {
            Solucao solucao = new Solucao();

            // Lista de clientes não atendidos
            ArrayList<Integer> clientesRestantes = new ArrayList<>();
            for (Cliente cliente : CLIENTES) {
                clientesRestantes.add(cliente.ponto);
            }

            // Criar rotas aleatórias
            while (!clientesRestantes.isEmpty()) {
                int tipoVeiculo = random.nextInt(FROTA.length);
                Rota rota = new Rota(tipoVeiculo);

                TipoVeiculo veiculo = FROTA[tipoVeiculo];
                int capacidadeRestante = veiculo.capacidadeKg;

                // Adicionar clientes à rota respeitando capacidade
                ArrayList<Integer> clientesParaRemover = new ArrayList<>();
                Collections.shuffle(clientesRestantes);

                for (Integer clienteIdx : clientesRestantes) {
                    Cliente cliente = encontrarCliente(clienteIdx, CLIENTES);
                    if (cliente != null && cliente.demandaKg <= capacidadeRestante) {
                        rota.clientes.add(clienteIdx);
                        capacidadeRestante -= cliente.demandaKg;
                        clientesParaRemover.add(clienteIdx);
                    }
                }

                clientesRestantes.removeAll(clientesParaRemover);

                if (!rota.clientes.isEmpty()) {
                    solucao.rotas.add(rota);
                }

                // Evitar loop infinito
                if (solucao.rotas.size() > 10) break;
            }

            populacao.add(solucao);
        }

        return populacao;
    }

    private static double avaliar(Solucao solucao) {
        double custoTotal = 0.0;
        double penalidade = 0.0;

        // Verificar se todos os clientes foram atendidos
        boolean[] clientesAtendidos = new boolean[CLIENTES.length + 1];

        for (Rota rota : solucao.rotas) {
            if (rota.clientes.isEmpty()) continue;

            TipoVeiculo veiculo = FROTA[rota.tipoVeiculo];

            // Verificar capacidade
            int demandaTotal = 0;
            for (Integer clienteIdx : rota.clientes) {
                Cliente cliente = encontrarCliente(clienteIdx, CLIENTES);
                if (cliente != null) {
                    demandaTotal += cliente.demandaKg;
                    clientesAtendidos[clienteIdx] = true;
                }
            }

            // Penalidade por excesso de capacidade
            if (demandaTotal > veiculo.capacidadeKg) {
                penalidade += (demandaTotal - veiculo.capacidadeKg) * 10.0;
            }

            // Calcular custo da rota
            int[] rotaArray = rota.clientes.stream().mapToInt(idx -> idx).toArray();
            double distancia = MatrizDistanciasSalvador.calcularDistanciaRota(rotaArray, DISTANCIAS, 0);
            custoTotal += distancia * veiculo.custoKm;

            // Verificar tempo máximo
            double tempoRota = calcularTempoRota(rotaArray, DISTANCIAS, CLIENTES, veiculo.velocidadeMediaKmH);
            if (tempoRota > veiculo.tempoMaximoHoras) {
                penalidade += (tempoRota - veiculo.tempoMaximoHoras) * 50.0;
            }

            // Penalidade por janela de tempo
            penalidade += calcularPenalidadeJanelaTempo(rotaArray, CLIENTES);
        }

        // Penalidade por clientes não atendidos
        for (int i = 1; i < clientesAtendidos.length; i++) {
            if (!clientesAtendidos[i]) {
                penalidade += 1000.0;
            }
        }

        return custoTotal + penalidade;
    }

    private static Solucao crossover(Solucao pai1, Solucao pai2) {
        Solucao filho = new Solucao();

        // Combinar rotas dos pais
        ArrayList<Rota> todasRotas = new ArrayList<>();
        todasRotas.addAll(pai1.rotas);
        todasRotas.addAll(pai2.rotas);

        // Coletar todos os clientes
        ArrayList<Integer> todosClientes = new ArrayList<>();
        for (Cliente cliente : CLIENTES) {
            todosClientes.add(cliente.ponto);
        }

        Collections.shuffle(todosClientes);

        // Criar novas rotas
        ArrayList<Integer> clientesRestantes = new ArrayList<>(todosClientes);

        while (!clientesRestantes.isEmpty()) {
            int tipoVeiculo = random.nextInt(FROTA.length);
            Rota novaRota = new Rota(tipoVeiculo);

            TipoVeiculo veiculo = FROTA[tipoVeiculo];
            int capacidadeRestante = veiculo.capacidadeKg;

            ArrayList<Integer> clientesParaRemover = new ArrayList<>();

            for (Integer clienteIdx : clientesRestantes) {
                Cliente cliente = encontrarCliente(clienteIdx, CLIENTES);
                if (cliente != null && cliente.demandaKg <= capacidadeRestante) {
                    novaRota.clientes.add(clienteIdx);
                    capacidadeRestante -= cliente.demandaKg;
                    clientesParaRemover.add(clienteIdx);

                    if (novaRota.clientes.size() >= 4) break;
                }
            }

            clientesRestantes.removeAll(clientesParaRemover);

            if (!novaRota.clientes.isEmpty()) {
                filho.rotas.add(novaRota);
            }

            if (filho.rotas.size() > 8) break;
        }

        return filho;
    }

    private static Solucao mutacao(Solucao solucao) {
        Solucao mutante = new Solucao();

        // Copiar rotas
        for (Rota rota : solucao.rotas) {
            mutante.rotas.add(new Rota(new ArrayList<>(rota.clientes), rota.tipoVeiculo));
        }

        if (mutante.rotas.isEmpty()) return mutante;

        int tipoMutacao = random.nextInt(3);

        switch (tipoMutacao) {
            case 0: // Trocar ordem de clientes em uma rota
                if (!mutante.rotas.isEmpty()) {
                    Rota rota = mutante.rotas.get(random.nextInt(mutante.rotas.size()));
                    if (rota.clientes.size() > 1) {
                        Collections.shuffle(rota.clientes);
                    }
                }
                break;

            case 1: // Trocar tipo de veículo
                if (!mutante.rotas.isEmpty()) {
                    Rota rota = mutante.rotas.get(random.nextInt(mutante.rotas.size()));
                    rota.tipoVeiculo = random.nextInt(FROTA.length);
                }
                break;

            case 2: // Mover cliente entre rotas
                if (mutante.rotas.size() > 1) {
                    int rota1Idx = random.nextInt(mutante.rotas.size());
                    int rota2Idx = random.nextInt(mutante.rotas.size());

                    Rota rota1 = mutante.rotas.get(rota1Idx);
                    Rota rota2 = mutante.rotas.get(rota2Idx);

                    if (!rota1.clientes.isEmpty()) {
                        Integer cliente = rota1.clientes.remove(random.nextInt(rota1.clientes.size()));
                        rota2.clientes.add(cliente);
                    }
                }
                break;
        }

        return mutante;
    }

    private static Cliente encontrarCliente(int ponto, Cliente[] clientes) {
        for (Cliente cliente : clientes) {
            if (cliente.ponto == ponto) {
                return cliente;
            }
        }
        return null;
    }

    private static int selecionarPorTorneio(ArrayList<Solucao> populacao) {
        int tamanhoTorneio = 3;
        int melhorIdx = random.nextInt(populacao.size());

        for (int i = 1; i < tamanhoTorneio; i++) {
            int candidatoIdx = random.nextInt(populacao.size());
            if (populacao.get(candidatoIdx).fitness < populacao.get(melhorIdx).fitness) {
                melhorIdx = candidatoIdx;
            }
        }

        return melhorIdx;
    }

    private static void exibirSolucao(Solucao solucao) {
        double custoTotal = 0.0;

        for (int i = 0; i < solucao.rotas.size(); i++) {
            Rota rota = solucao.rotas.get(i);
            TipoVeiculo veiculo = FROTA[rota.tipoVeiculo];

            System.out.printf("ROTA %d - Veículo: %s\n", i + 1, veiculo.nome);
            System.out.print("Sequência: Depósito");

            int demandaTotal = 0;
            for (Integer clienteIdx : rota.clientes) {
                Cliente cliente = encontrarCliente(clienteIdx, CLIENTES);
                if (cliente != null) {
                    System.out.printf(" → %s", cliente.nome);
                    demandaTotal += cliente.demandaKg;
                }
            }
            System.out.print(" → Depósito\n");

            int[] rotaArray = rota.clientes.stream().mapToInt(idx -> idx).toArray();
            double distancia = MatrizDistanciasSalvador.calcularDistanciaRota(rotaArray, DISTANCIAS, 0);
            double custo = distancia * veiculo.custoKm;
            double tempo = calcularTempoRota(rotaArray, DISTANCIAS, CLIENTES, veiculo.velocidadeMediaKmH);

            System.out.printf("Demanda total: %dkg/%dkg\n", demandaTotal, veiculo.capacidadeKg);
            System.out.printf("Distância: %.1fkm - Tempo: %.1fh - Custo: R$%.2f\n\n",
                    distancia, tempo, custo);

            custoTotal += custo;
        }

        System.out.printf("CUSTO TOTAL: R$%.2f\n", custoTotal);
    }
}
