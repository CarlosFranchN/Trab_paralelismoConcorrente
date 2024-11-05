import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuickSort {

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
                    quickSortSerial(array, 0, array.length - 1);
                    long fimSerial = System.nanoTime();
                    tempo = fimSerial - inicioSerial;
                } else {
                    int numeroThreads = amostrasNumeroThreads[j];
                    long inicioParalelo = System.nanoTime();
                    quickSortParalelo(array, numeroThreads);
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
        try (FileWriter writer = new FileWriter("quickSort_" + metodo + "_resultados.csv")) {
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

    public static void quickSortSerial(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSortSerial(arr, low, pi - 1);
            quickSortSerial(arr, pi + 1, high);
        }
    }

    public static void quickSortParalelo(int[] arr, int numeroThreads) {
        if (numeroThreads <= 1) {
            quickSortSerial(arr, 0, arr.length - 1);
        } else {
            int pi = partition(arr, 0, arr.length - 1);
            Thread leftThread = new Thread(() -> quickSortParalelo(Arrays.copyOfRange(arr, 0, pi), numeroThreads / 2));
            Thread rightThread = new Thread(() -> quickSortParalelo(Arrays.copyOfRange(arr, pi + 1, arr.length), numeroThreads / 2));
            leftThread.start();
            rightThread.start();
            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}
