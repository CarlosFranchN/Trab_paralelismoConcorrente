//PROFESSOR NAO CONSEGUI USAR O JFREE ,_,
//ESTAMOS USANDO O MATPLOTLIB DO PYTHON

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraficoTempoTamanho extends JFrame {

    public GraficoTempoTamanho() {
        setTitle("Gráfico Tempo vs. Tamanho do Vetor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(lerDadosCSV("Serial_resultados.csv", "Serial"));
        dataset.addSeries(lerDadosCSV("Paralelo_2_Threads_resultados.csv", "Paralelo - 2 Threads"));
        dataset.addSeries(lerDadosCSV("Paralelo_3_Threads_resultados.csv", "Paralelo - 3 Threads"));
        dataset.addSeries(lerDadosCSV("Paralelo_4_Threads_resultados.csv", "Paralelo - 4 Threads"));

        JFreeChart chart = ChartFactory.createXYLineChart(
            "Tempo vs. Tamanho do Vetor",
            "Tamanho do Vetor",
            "Tempo (ns)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        // Personaliza o gráfico
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.GREEN);
        renderer.setSeriesPaint(3, Color.ORANGE);
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(780, 560));
        setContentPane(chartPanel);
    }

    private XYSeries lerDadosCSV(String arquivo, String nomeSerie) {
        XYSeries serie = new XYSeries(nomeSerie);
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            br.readLine();  // Ignora o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");
                int tamanhoVetor = Integer.parseInt(valores[0]);
                long tempo = Long.parseLong(valores[1]);
                serie.add(tamanhoVetor, tempo);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }
        return serie;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraficoTempoTamanho ex = new GraficoTempoTamanho();
            ex.setVisible(true);
        });
    }
}
