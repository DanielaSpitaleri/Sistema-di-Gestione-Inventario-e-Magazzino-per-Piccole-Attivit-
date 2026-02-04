package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.Prodotti;
import it.unicas.project.template.address.model.Movimenti;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Controller per la visualizzazione delle statistiche dei movimenti.
 * <br>
 * Questa classe costruisce e popola istogrammi (BarChart) per rappresentare:
 * <ul>
 *   <li>i carichi e gli scarichi giornalieri di un mese</li>
 *   <li>i totali di carichi e scarichi per prodotto</li>
 * </ul>
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class MovimentiStatisticsController {

    /**
     * Grafico a barre usato per mostrare le statistiche.
     *
     * @see #xAxis
     * @see #yAxis
     */
    @FXML
    private BarChart<String, Number> barChart;

    /**
     * Asse delle categorie (etichette sull'asse X), usato per giorni o nomi prodotti.
     *
     * @see #barChart
     */
    @FXML
    private CategoryAxis xAxis;

    /**
     * Asse numerico (asse Y) che mostra le quantità movimentate.
     *
     * @see #barChart
     */
    @FXML
    private NumberAxis yAxis;

    /**
     * Lista di etichette contenente i giorni del mese da visualizzare sull'asse X.
     * <br>
     * Viene popolata da {@link #setMovimentiData(List, LocalDate)}.
     *
     * @see #xAxis
     */
    private ObservableList<String> giorniMese = FXCollections.observableArrayList();

    /**
     * Lista di etichette contenente i nomi dei prodotti da visualizzare sull'asse X.
     * <br>
     * Viene popolata da {@link #setProdottiData(List, List)}.
     *
     * @see #xAxis
     */
    private ObservableList<String> nomeProdotti = FXCollections.observableArrayList();

    /**
     * Inizializza il controller JavaFX.
     * <br>
     * Configura etichetta e unità di tick per l'asse Y. Questo metodo è chiamato
     * automaticamente dal framework dopo il caricamento dell'FXML.
     */
    @FXML
    private void initialize() {
        yAxis.setLabel("Quantità movimentate");
        yAxis.setTickUnit(1);
    }

    /**
     * Costruisce e popola un grafico giornaliero dei carichi e scarichi per il mese precedente alla data fornita.
     * <br>
     * Se {@code data} è {@code null} viene utilizzata la data corrente.
     * <br>
     * Il metodo:
     * <ul>
     *     <li>calcola il mese precedente rispetto a {@code data}</li>
     *     <li>riempie l'asse X con i giorni del mese</li>
     *     <li>accumula le quantità giornaliere di carico e scarico dai movimenti forniti</li>
     *     <li>aggiorna il {@link #barChart} con due serie: "Carichi" e "Scarichi"</li>
     * </ul>
     * Pre-condizioni: {@code movimenti} non deve essere {@code null} (se è {@code null} il comportamento è non definito).
     * <br>
     * Post-condizioni: il grafico viene aggiornato con i dati del mese calcolato e {@link #giorniMese} contiene le etichette dei giorni del mese.
     *
     * @param movimenti lista dei movimenti da analizzare; ogni elemento fornisce tipo (CARICO/SCARICO),
     *                  data e quantità.
     * @param data data di riferimento; se {@code null} viene usata la data odierna. Il mese visualizzato
     *             è il mese precedente a questa data.
     */
    public void setMovimentiData(List<Movimenti> movimenti, LocalDate data) {
        if (data == null) {
            data = LocalDate.now();
        }

        LocalDate mesePrecedente = data.minusMonths(1);
        int numeroGiorni = mesePrecedente.lengthOfMonth();
        String nomeMese = mesePrecedente.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN);

        barChart.setTitle("Carichi e scarichi di " + nomeMese);
        xAxis.setLabel("Giorni del mese");
        barChart.getData().clear();

        for (int i = 1; i <= numeroGiorni; i++) {
            giorniMese.add(String.valueOf(i));
        }

        xAxis.setCategories(giorniMese);

        int[] carichiDay = new int[numeroGiorni];
        int[] scarichiDay = new int[numeroGiorni];

        int day;
        for (Movimenti m : movimenti) {
            if (m.getData().getMonth() == mesePrecedente.getMonth() && m.getData().getYear() == mesePrecedente.getYear()) {
                day = m.getData().getDayOfMonth() - 1;

                if ("CARICO".equalsIgnoreCase(m.getTipo())) {
                    carichiDay[day]+=m.getQuantita();
                } else if ("SCARICO".equalsIgnoreCase(m.getTipo())) {
                    scarichiDay[day]+=m.getQuantita();
                }
            }
        }

        XYChart.Series<String, Number> carichi = new XYChart.Series<>();
        carichi.setName("Carichi");

        XYChart.Series<String, Number> scarichi = new XYChart.Series<>();
        scarichi.setName("Scarichi");

        for (int i = 0; i < carichiDay.length && i < giorniMese.size(); i++) {
            String giorno = giorniMese.get(i);
            carichi.getData().add(new XYChart.Data<>(giorno, carichiDay[i]));
            scarichi.getData().add(new XYChart.Data<>(giorno, scarichiDay[i]));
        }

        barChart.getData().clear();
        barChart.getData().addAll(carichi, scarichi);
    }

    /**
     * Costruisce e popola un grafico che mostra, per ogni prodotto, il totale dei carichi e degli scarichi.
     * <br>
     * Il metodo:
     * <ul>
     *     <li>usa la lista {@code prodotti} per ottenere i nomi dei prodotti e l'ordine</li>
     *     <li>scorre la lista {@code movimenti} per sommare quantità di tipo CARICO e SCARICO associate a ciascun prodotto</li>
     *     <li>aggiorna il {@link #barChart} con due serie: "Carichi" e "Scarichi"</li>
     * </ul>
     * Pre-condizioni: {@code prodotti} e {@code movimenti} non devono essere {@code null}.
     * <br>
     * Post-condizioni: {@link #nomeProdotti} contiene i nomi dei prodotti visualizzati e il grafico è aggiornato con i totali per prodotto.
     *
     * @param prodotti lista dei prodotti da includere nel grafico; l'ordine nella lista determina l'ordine delle barre.
     * @param movimenti lista dei movimenti usata per calcolare i totali per ogni prodotto.
     */
    public void setProdottiData(List<Prodotti> prodotti, List<Movimenti> movimenti) {
        barChart.setTitle("Prodotti più movimentati");
        xAxis.setLabel("Prodotti");

        int numeroProdotti = prodotti.size();
        int[] totaleCarichi = new int[numeroProdotti];
        int[] totaleScarichi = new int[numeroProdotti];

        int index = 0;

        for (Prodotti p : prodotti) {
            nomeProdotti.add(p.getNome());
            totaleCarichi[index] = 0;
            totaleScarichi[index] = 0;

            for (Movimenti m : movimenti) {
                if (m.getIdProdotto() == p.getIdProdotto()) {
                    if ("CARICO".equalsIgnoreCase(m.getTipo())) {
                        totaleCarichi[index] += m.getQuantita();
                    } else if ("SCARICO".equalsIgnoreCase(m.getTipo())) {
                        totaleScarichi[index] += m.getQuantita();
                    }
                }
            }
            index++;
        }
        xAxis.setCategories(nomeProdotti);

        XYChart.Series<String, Number> carichi = new XYChart.Series<>();
        carichi.setName("Carichi");

        XYChart.Series<String, Number> scarichi = new XYChart.Series<>();
        scarichi.setName("Scarichi");

        for (int i = 0; i < totaleCarichi.length && i < nomeProdotti.size(); i++) {
            carichi.getData().add(new XYChart.Data<>(nomeProdotti.get(i), totaleCarichi[i]));
            scarichi.getData().add(new XYChart.Data<>(nomeProdotti.get(i), totaleScarichi[i]));
        }

        barChart.getData().clear();
        barChart.getData().addAll(carichi, scarichi);
    }
}
