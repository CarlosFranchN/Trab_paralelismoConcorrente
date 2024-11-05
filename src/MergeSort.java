import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MergeSort {

    public static void main(String[] args) {
        int[][] amostrasArray = {
            gerarArrayAleatorio(10),
            gerarArrayAleatorio(100),
            gerarArrayAleatorio(1000),
            gerarArrayAleatorio(10000),
            gerarArrayAleatorio(100000)
        };
        
        int[] amostrasNumeroThreads = {2, 3, 4};
        long[][] amostras = new long[4][5];
        
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 5; i++) {
                int[] array = amostrasArray[i];
                long tempo;
                
                if (j == 3) {
                    long inicioSerial = System.nanoTime();
                    mergeSortSerial(array);
                    long fimSerial = System.nanoTime();
                    tempo = fimSerial - inicioSerial;
                } else {
                    int numeroThreads = amostrasNumeroThreads[j];
                    long inicioParalelo = System.nanoTime();
                    mergeSortParalelo(array, numeroThreads);
                    long fimParalelo = System.nanoTime();
                    tempo = fimParalelo - inicioParalelo;
                }
                amostras[j][i] = tempo;
            }

            String metodo = (j == 3) ? "Serial" : "Paralelo_" + amostrasNumeroThreads[j] + "_Threads";
            salvarCSV(metodo, amostrasArray, amostras[j]);
        }
    }

    public static void salvarCSV(String metodo, int[][] tamanhosVetores, long[] tempos) {
        try (FileWriter writer = new FileWriter("mergeSort_" + metodo + "_resultados.csv")) {
            writer.write("Tamanho do Vetor,Tempo (ns)\n");
            for (int i = 0; i < tamanhosVetores.length; i++) {
                writer.write(tamanhosVetores[i].length + "," + tempos[i] + "\n");
            }
            System.out.println("Arquivo " + metodo + "_resultados.csv criado com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo CSV: " + e.getMessage());
        }
    }

    public static int[] gerarArrayAleatorio(int tamanho) {
        List<Integer> listaNumeros = new ArrayList<>();
        for (int i = 1; i <= tamanho; i++) {
            listaNumeros.add(i);
        }
        Collections.shuffle(listaNumeros);
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = listaNumeros.get(i);
        }
        return array;
    }

    public static void mergeSortSerial(int[] arr) {
        int[] arrayOrdenadoSerial = Arrays.copyOf(arr, arr.length);
        mergeSort(arrayOrdenadoSerial, 0, arrayOrdenadoSerial.length - 1);
    }

    public static void mergeSortParalelo(int[] arr, int numeroThreads) {
        int[][] subArrays = dividirArray(arr, numeroThreads);
        Thread[] threads = new Thread[numeroThreads];
        for (int i = 0; i < numeroThreads; i++) {
            final int[] subArray = subArrays[i];
            threads[i] = new Thread(() -> mergeSort(subArray, 0, subArray.length - 1));
            threads[i].start();
        }
        for (int i = 0; i < numeroThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fundirArrays(subArrays);
    }

    public static void mergeSort(int[] arr, int inicio, int fim) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            mergeSort(arr, inicio, meio);
            mergeSort(arr, meio + 1, fim);
            merge(arr, inicio, meio, fim);
        }
    }

    public static void merge(int[] arr, int inicio, int meio, int fim) {
        int[] temp = Arrays.copyOfRange(arr, inicio, fim + 1);
        int i = 0, j = meio - inicio + 1, k = inicio;
        while (i <= meio - inicio && j < temp.length) {
            if (temp[i] <= temp[j]) arr[k++] = temp[i++];
            else arr[k++] = temp[j++];
        }
        while (i <= meio - inicio) arr[k++] = temp[i++];
        while (j < temp.length) arr[k++] = temp[j++];
    }

    public static int[][] dividirArray(int[] array, int numeroThreads) {
        int tamanhoSubArray = array.length / numeroThreads;
        int[][] subArrays = new int[numeroThreads][];
        for (int i = 0; i < numeroThreads; i++) {
            int inicio = i * tamanhoSubArray;
            int fim = (i == numeroThreads - 1) ? array.length : (i + 1) * tamanhoSubArray;
            subArrays[i] = Arrays.copyOfRange(array, inicio, fim);
        }
        return subArrays;
    }

    public static int[] fundirArrays(int[][] subArrays) {
        int tamanhoTotal = 0;
        for (int[] subArray : subArrays) tamanhoTotal += subArray.length;
        int[] resultado = new int[tamanhoTotal];
        int[] indices = new int[subArrays.length];
        for (int i = 0; i < tamanhoTotal; i++) {
            int menorIndice = -1;
            int menorValor = Integer.MAX_VALUE;
            for (int j = 0; j < subArrays.length; j++) {
                if (indices[j] < subArrays[j].length && subArrays[j][indices[j]] < menorValor) {
                    menorIndice = j;
                    menorValor = subArrays[j][indices[j]];
                }
            }
            resultado[i] = menorValor;
            indices[menorIndice]++;
        }
        return resultado;
    }
}
