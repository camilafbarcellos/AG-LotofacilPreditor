package AlgoritmoGenetico;

/**
 *
 * Classe que implementa um algoritmo genético para encontrar a combinação ideal
 * de números para jogar na Lotofacil.
 *
 * Este programa utiliza um algoritmo genético para gerar uma população inicial
 * de possíveis combinações de números para jogar na Lotofacil, avalia o fitness
 * de cada indivíduo baseado em critérios como o número de dezenas que estão na
 * lista dos números mais sorteados, a distribuição teórica esperada de números
 * pares e ímpares, a quantidade de pares de números presentes nas diagonais,
 * etc. Seleciona os melhores indivíduos para reprodução por meio de crossover e
 * mutação, e repete o processo por diversas gerações até que se encontre um
 * indivíduo que satisfaça os critérios desejados.
 *
 * Autores: Bernardo Dirceu Tomasi e Camila Florão Barcellos
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class LotoNumeros51 {

    private static final int tamanhoPopulacao = 500; // Tamanho da população
    private static final int numeroGeracoes = 200; // Número máximo de gerações
    private static final double taxaMutacao = 0.05; // Taxa de mutação
    private static final double taxaReproducao = 1.0; // Taxa de crossover
    private static final int elitismo = 250; // Número de indivíduos que serão mantidos na próxima geração
    private static final int valorFitnessDesejado = 400; // Valor de fitness desejado
    private static int[] tabelaFrequencia = new int[25]; // Vetor para frequencia de aparições dos números
    private static Random random = new Random(); // Para gerar números aleátorios

    /**
     * Este método gera um indivíduo aleatório composto por 15 números inteiros
     * distintos, escolhidos a partir de um conjunto de 25 números inteiros.
     *
     * @return Um array de inteiros representando o indivíduo gerado.
     */
    private static int[] geradorIndividuos() {
        // Cria uma lista de números inteiros de 1 a 25
        ArrayList<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            numeros.add(i);
        }
        // Embaralha a lista de números inteiros.
        // shuffle() é uma função da classe Collections.
        // Ele é usado para embaralhar os elementos de uma lista de forma aleatória.
        // Quando aplicado a uma lista, ele reorganiza os elementos da lista em uma ordem aleatória
        Collections.shuffle(numeros);
        // Cria um array de inteiros para representar o indivíduo
        int[] individual = new int[15];
        // Preenche o array de inteiros com os 15 primeiros números da lista embaralhada
        for (int i = 0; i < 15; i++) {
            individual[i] = numeros.get(i);
        }
        // Retorna o array de inteiros representando o indivíduo gerado
        return individual;
    }

    /**
     * Este método conta a quantidade de números pares em um array de inteiros.
     *
     * @param array o array de inteiros a ser contado
     * @return a quantidade de números pares no array
     */
    private static int contagemPares(int[] array) {
        int count = 0;
        for (int i : array) {
            if (i % 2 == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Método que aplica a técnica da diagonal: calcula a quantidade de pares no
     * array individual que estão em células vizinhas na matriz 5x5.
     *
     * @param individual o array de inteiros a ser verificado
     * @return count a quantidade de pares encontrados
     */
    public static int contarParesDiagonal(int[] individual) {
        // cria uma matriz 5x5
        int[][] matriz = {{0, 1, 2, 3, 4},
        {5, 6, 7, 8, 9},
        {10, 11, 12, 13, 14},
        {15, 16, 17, 18, 19},
        {20, 21, 22, 23, 24}};

        int count = 0; // contador para armazenar a quantidade de pares encontrados

        // percorre todas as células da matriz
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                int num = matriz[i][j]; // número da célula atual

                // verifica se a célula atual tem uma célula vizinha à direita
                if (j + 1 < matriz[i].length && matriz[i][j + 1] < individual.length) {
                    int a = individual[num];
                    int b = individual[matriz[i][j + 1]];
                    if (a < b) {
                        count++;
                    }
                }

                // verifica se a célula atual tem uma célula vizinha à esquerda
                if (j - 1 >= 0 && matriz[i][j - 1] < individual.length) {
                    int a = individual[num];
                    int b = individual[matriz[i][j - 1]];
                    if (a < b) {
                        count++;
                    }
                }

                // verifica se a célula atual tem uma célula vizinha abaixo
                if (i + 1 < matriz.length && matriz[i + 1][j] < individual.length) {
                    int a = individual[num];
                    int b = individual[matriz[i + 1][j]];
                    if (a < b) {
                        count++;
                    }
                }

                // verifica se a célula atual tem uma célula vizinha na diagonal inferior direita
                if (i + 1 < matriz.length && j + 1 < matriz[i].length && matriz[i + 1][j + 1] < individual.length) {
                    int a = individual[num];
                    int b = individual[matriz[i + 1][j + 1]];
                    if (a < b) {
                        count++;
                    }
                }

                // verifica se a célula atual tem uma célula vizinha na diagonal inferior esquerda
                if (i + 1 < matriz.length && j - 1 >= 0 && matriz[i + 1][j - 1] < individual.length) {
                    int a = individual[num];
                    int b = individual[matriz[i + 1][j - 1]];
                    if (a < b) {
                        count++;
                    }
                }
            }
        }

        return count; // retorna a quantidade de pares encontrados
    }

    /**
     * Verifica se um número é primo.
     *
     * @param n O número a ser verificado.
     * @return true se o número for primo, false caso contrário.
     */
    private static boolean ePrimo(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retorna a quantidade de números primos em um array de inteiros.
     *
     * @param individual O array de inteiros a ser analisado.
     * @return A quantidade de números primos no array.
     */
    public static int contarNumerosPrimos(int[] individual) {
        int count = 0;
        for (int i = 0; i < individual.length; i++) {
            if (ePrimo(individual[i])) {
                count++;
            }
        }
        return count;
    }

    /**
     * Cria e imprime uma tabela de frequência das dezenas dos jogos lotofacil
     * dos ultimos 10 concursos
     */
    private static void criadorTabelaFreq() {
        // Array 2D que representa os números sorteados em cada jogo da loto
        int[][] sorteios = {
            {1, 2, 3, 5, 8, 9, 10, 11, 13, 15, 18, 20, 22, 24, 25},
            {2, 4, 5, 6, 8, 9, 12, 13, 14, 17, 18, 19, 23, 24, 25},
            {1, 2, 5, 7, 8, 9, 11, 13, 14, 15, 16, 18, 21, 22, 23},
            {1, 2, 4, 6, 8, 12, 15, 16, 17, 19, 20, 22, 23, 24, 25},
            {1, 2, 3, 5, 6, 8, 10, 11, 12, 13, 14, 20, 23, 24, 25},
            {1, 4, 5, 6, 8, 9, 12, 13, 15, 17, 18, 19, 20, 22, 23},
            {1, 2, 3, 4, 5, 6, 7, 10, 11, 13, 15, 16, 17, 19, 21},
            {1, 2, 3, 4, 5, 7, 9, 10, 14, 15, 17, 18, 20, 22, 25},
            {1, 2, 3, 5, 9, 12, 14, 15, 17, 18, 20, 22, 23, 24, 25},
            {1, 2, 3, 5, 6, 7, 8, 12, 13, 15, 16, 17, 18, 20, 23}
        };

        // Array que armazena as frequências das dezenas (índice 0 representa 
        //a dezena 1, índice 1 a dezena 2, e assim por diante)
        int[] frequencia = new int[25];

        // Percorre cada jogo da loto e atualiza a frequência de cada dezena
        for (int i = 0; i < sorteios.length; i++) {
            for (int j = 0; j < sorteios[i].length; j++) {
                int dezena = sorteios[i][j];
                frequencia[dezena - 1]++;
            }
        }

        // Imprime a tabela de frequência resultante
        for (int i = 0; i < frequencia.length; i++) {
            System.out.println("Dezena " + (i + 1) + ": " + frequencia[i]);
        }
    }

    /**
     * Calcula os valores de fitness para cada indivíduo da população. O fitness
     * é calculado com base em duas características: a presença de números
     * primos, a distribuição teórica de números pares e ímpares, a tabela de
     * frequencia dos últimos jogos, a técnica dos 70%, na difrença de números
     * altos e baixos, na distância de entre os indivíduos e na técnica da
     * diagonal.
     *
     * @param populacao a população de indivíduos para calcular o fitness
     * @return uma lista de valores de fitness correspondente a cada indivíduo
     * da população
     */
    private static ArrayList<Double> calculaFitness(ArrayList<int[]> populacao) {
        ArrayList<Double> valoresFitness = new ArrayList<>();
        // Para cada individuo avalia o fitness
        for (int[] individual : populacao) {
            // Valor inivial da nota é zero por padrão
            double fitness = 0;

            /**
             * Calcula o fitness de um indivíduo com base na tabela de
             * frequência das dezenas
             */
            // percorre o array individual
            for (int i = 0; i < individual.length; i++) {
                // obtém a dezena atual
                int dezena = individual[i];
                // adiciona à soma o valor da frequência da dezena multiplicado por 10
                // (valor arbitrário escolhido para aumentar a diferença entre indivíduos)
                fitness += 10 * tabelaFrequencia[dezena - 1];
            }

            /**
             * Calcula a diferença entre a quantidade de números pares e ímpares
             * presentes na combinação do indivíduo e a quantidade teórica
             * esperada (7 números pares e 8 números ímpares) Se essa diferença
             * for maior que zero, o valor de fitness é reduzido multiplicando a
             * diferença pelo peso definido na constante "pesoDiferencaParImpar"
             */
            int diferencaPares = Math.abs(contagemPares(individual) - 7);
            fitness -= diferencaPares * 5;

            /**
             * Avalia um indivíduo representado por um vetor de inteiros,
             * utilizando o critério de fitness de distribuição balanceada de
             * números altos e baixos.
             */
            int contadorAlto = 0;
            int contadorBaixo = 0;
            // Conta quantos números baixos e quantos números altos existem no indivíduo
            for (int i = 0; i < individual.length; i++) {
                if (individual[i] <= 12) {
                    contadorBaixo++;
                } else {
                    contadorAlto++;
                }
            }
            // Calcula o valor de fitness do indivíduo com base na distribuição de 
            // números altos e baixos
            fitness += Math.abs(contadorBaixo - contadorAlto);

            /**
             * Avalia um indivíduo representado por um vetor de inteiros,
             * utilizando o critério de fitness de distância mínima entre os
             * números sorteados
             */
            for (int i = 0; i < individual.length - 1; i++) {
                int dif = Math.abs(individual[i] - individual[i + 1]);
                fitness += dif;
            }

            //Calcula a soma dos números do indivíduo
            int soma = 0;
            for (int i = 0; i < individual.length; i++) {
                soma += individual[i];
            }
            //Valoriza o indivíduo se a soma estiver entre 190 e 200
            if (soma >= 190 && soma <= 260) {
                fitness += 50;
            } else {
                fitness = 0;
            }

            // Realiza a técnica da diagonal
            int pares = contarParesDiagonal(individual);
            // Desvaloriza indivíduos que possuam menos de 2 pares e mais de 17
            if (pares < 2 || pares > 17) {
                fitness = 0;
            } // Beneficia indivíduos dentro do espectro desejado
            else {
                fitness += (10 * (contarParesDiagonal(individual)));
            }

            // Verifica total de números primos
            int primos = contarNumerosPrimos(individual);
            // Beneficia indivíduos que possuam primos
            if (primos >= 2) {
                fitness += (5 * (contarNumerosPrimos(individual)));
            } // Desvaloriza indivíduos que não possuam primos
            else {
                fitness = 0;
            }

            // Zera o fitness dos indivíduos com números duplicados
            for (int i = 0; i < individual.length - 1; i++) {
                for (int j = i + 1; j < individual.length; j++) {
                    if (individual[i] == individual[j]) {
                        fitness = 0;
                        break;
                    }
                }
            }

            // Retorna uma lista de valores de fitness correspondentes a cada indivíduo da população
            valoresFitness.add(fitness);
        }
        //System.out.println(valoresFitness);
        return valoresFitness;
    }

    /**
     * Método que seleciona um indivíduo da população com base em sua aptidão
     * (fitness). Utiliza roleta de seleção. O método utiliza uma abordagem
     * linear para somar a aptidão de todos os indivíduos na população.
     *
     * @param populacao a população de indivíduos
     * @param valoresFitness uma lista de valores de fitness correspondentes aos
     * indivíduos da população
     * @return o indivíduo selecionado
     */
    private static int[] selecinaIndividuoFitness(ArrayList<int[]> populacao, ArrayList<Double> valoresFitness) {
        // Calcula o total de aptidão da população
        double totalFitness = 0;
        for (double fitnessValue : valoresFitness) {
            totalFitness += fitnessValue;
        }
        // Gera um número aleatório que representará a aptidão do indivíduo selecionado
        double randomFitness = random.nextDouble() * totalFitness;
        // Percorre a lista de indivíduos e seus respectivos valores de fitness até encontrar o indivíduo selecionado
        double currentFitness = 0;
        for (int i = 0; i < populacao.size(); i++) {
            currentFitness += valoresFitness.get(i);
            if (currentFitness > randomFitness) {
                return populacao.get(i);
            }
        }
        // Caso não seja possível encontrar um indivíduo com aptidão suficiente, retorna o último da lista
        return populacao.get(populacao.size() - 1);
    }

    /**
     * Realiza o crossover entre dois indivíduos do algoritmo genético, gerando
     * um novo indivíduo. O crossover é realizado com uma certa taxa de
     * reprodução definida pelo parâmetro taxaReproducao.
     *
     * @param pai1 o primeiro pai utilizado no crossover
     * @param pai2 o segundo pai utilizado no crossover
     * @return o novo indivíduo gerado a partir do crossover
     */
    private static int[] crossover(int[] pai1, int[] pai2) {
        // Array para armazenar os genes do filho gerado
        int[] filho = new int[15];
        // Verifica se o crossover será realizado com base na taxa de reprodução definida
        if (random.nextDouble() < taxaReproducao) {
            /**
             * Um ponto de crossover é escolhido aleatoriamente e os genes do
             * pai1 são copiados até esse ponto, enquanto os genes do pai2 são
             * copiados a partir desse ponto
             */
            int crossoverPoint = random.nextInt(pai1.length);
            // Copia os genes do pai1 até o ponto de crossover
            for (int i = 0; i < crossoverPoint; i++) {
                filho[i] = pai1[i];
            }
            // Copia os genes do pai2 a partir do ponto de crossover
            for (int i = crossoverPoint; i < pai2.length; i++) {
                filho[i] = pai2[i];
            }
        } else {
            // Caso o crossover não ocorra, o filho é igual ao pai1
            filho = pai1;
        }
        return filho;
    }

    /**
     * Aplica uma mutação em um indivíduo da população. A mutação consiste em
     * trocar a posição de dois genes aleatórios do indivíduo.
     *
     * @param individual o indivíduo a ser mutado
     * @return o indivíduo mutado
     */
    private static int[] mutacao(int[] individual) {
        // Cria uma cópia do indivíduo para ser mutada
        int[] mutante = individual.clone();
        // Seleciona dois índices aleatórios diferentes para trocar seus valores
        // Essa troca caracteriza a mutação
        int index1 = random.nextInt(mutante.length);
        int index2 = random.nextInt(mutante.length);
        do {
            index2 = random.nextInt(mutante.length);
        } while (index2 == index1);
        // Troca os valores nos dois índices selecionados
        int temp = mutante[index1];
        mutante[index1] = mutante[index2];
        mutante[index2] = temp;
        // Retorna o indivíduo mutado
        return mutante;
    }

    /**
     * Converte um array de inteiros em uma String com os valores separados por
     * espaços.
     *
     * @param array o array de inteiros a ser convertido em String.
     * @return uma String com os valores do array separados por espaços.
     */
    private static String arrayToString(int[] array) {
        String result = "";
        for (int i : array) {
            result += i + " ";
        }
        return result.trim();
    }

    /**
     * Método principal que executa o algoritmo genético para encontrar a melhor
     * combinação de números.
     *
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        //atribui os valores para a tabela frequencia
        criadorTabelaFreq();

        /**
         * Inicializa a população aleatoriamente Tamnho da população definido
         * por "tamanhoPopulacao" Armazena a população em array list (populacao)
         */
        ArrayList<int[]> populacao = new ArrayList<>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            populacao.add(geradorIndividuos());
        }

        /**
         * Executa as gerações Loop limitado por "numeroGeracoes"
         */
        int contadorGeracoes = 0;
        while (contadorGeracoes < numeroGeracoes) {
            /**
             * Executa o Fiteness Fitness: atribui uma nota associada ao
             * individuo que avalia quão boa é a solução representada pelo
             * individuo.
             */
            // ValoresFitness recebe o array com o resultado dos fitness da população
            ArrayList<Double> valoresFitness = calculaFitness(populacao);
            // Se encontrar um indivíduo com o fitness desejado(definido pela constante valorFitnessDesejado), 
            // retorna o número correspondente
            // O método indexOf() é um método da classe ArrayList,
            // é usado para encontrar a primeira ocorrência de um elemento especificado em uma lista
            int melhoresNumeros = valoresFitness.indexOf((double) valorFitnessDesejado);
            if (melhoresNumeros >= 0) {
                System.out.println("Melhor indivíduo encontrado na geração " + contadorGeracoes + ": " + arrayToString(populacao.get(melhoresNumeros)));
                return;
            }

            /**
             * Se nenhum indivíduo da população tiver o fitness desejado:
             * Seleciona os indivíduos mais aptos (elitismo), são aqueles com os
             * maiores valores de fitness (serão mantidos)
             */
            ArrayList<int[]> novaPopulacao = new ArrayList<>();
            ArrayList<Double> valoresFitOrdenados = new ArrayList<>(valoresFitness);
            Collections.sort(valoresFitOrdenados, Collections.reverseOrder());
            for (int i = 0; i < elitismo; i++) {
                int index = valoresFitness.indexOf(valoresFitOrdenados.get(i));
                novaPopulacao.add(populacao.get(index));
            }

            /**
             * Executa o reprodução (crossover) entre pares de indivíduos. Criar
             * novos indivíduos para a próxima geração Os filhos são adicionados
             * ao ArrayList "novaPopulacao"
             */
            while (novaPopulacao.size() < tamanhoPopulacao) {
                int[] pai1 = selecinaIndividuoFitness(populacao, valoresFitness);
                int[] pai2 = selecinaIndividuoFitness(populacao, valoresFitness);
                int[] filho = crossover(pai1, pai2);
                novaPopulacao.add(filho);
            }

            /**
             * Executa a mutação em alguns indivíduos Introduz variação genética
             * nos indivíduos Ajuda a explorar novas soluções
             */
            for (int i = 0; i < tamanhoPopulacao; i++) {
                if (random.nextDouble() < taxaMutacao) {
                    int[] mutante = mutacao(populacao.get(i));
                    novaPopulacao.set(i, mutante);
                }
            }

            // Atualiza a população
            populacao = novaPopulacao;
            // Incrementa o contador de gerações
            contadorGeracoes++;
        }

        // Se não encontrar um indivíduo com o fitness desejado após o número máximo de gerações, retorna o melhor indivíduo encontrado
        int melhoresNumeros = calculaFitness(populacao).indexOf(Collections.max(calculaFitness(populacao)));
        System.out.println("Melhor indivíduo encontrado após " + contadorGeracoes + " gerações: " + arrayToString(populacao.get(melhoresNumeros)));
    }
}
