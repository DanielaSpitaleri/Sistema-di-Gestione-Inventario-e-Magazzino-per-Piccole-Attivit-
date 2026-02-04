package it.unicas.project.template.address.util;

import it.unicas.project.template.address.model.Movimenti;
import it.unicas.project.template.address.model.Prodotti;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility per l'esportazione dei dati dell'applicazione in formato CSV.
 * Fornisce metodi per aprire un FileChooser e scrivere le collezioni di
 * Prodotti e Movimenti su file CSV utilizzando un separatore definito.
 *
 * Questa classe è composta esclusivamente da metodi statici e non deve essere istanziata.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class Esportazione {

    /**
     * Logger per la classe Esportazione.
     *
     * @see java.util.logging.Logger
     */
    private static final Logger LOGGER = Logger.getLogger(Esportazione.class.getName());

    /**
     * Separatore utilizzato per i file CSV generati da questa utility.
     *
     * @see #writeProdottiToCSV(File, ObservableList)
     * @see #writeMovimentiToCSV(File, ObservableList, ObservableList)
     */
    private static final String CSV_SEPARATOR = ";";

    /**
     * Apre un FileChooser e scrive la lista di Prodotti nel file selezionato.
     * Effettua la scrittura chiamando internamente {@link #writeProdottiToCSV(File, ObservableList)}.
     *
     * @param stage    Lo Stage genitore per la finestra di dialogo.
     * @param prodotti La lista di prodotti da esportare.
     * @return true se l'esportazione è stata completata con successo; false se l'utente ha annullato o si è verificato un errore.
     */
    public static boolean exportProdottiToCSV(Stage stage, ObservableList<Prodotti> prodotti) {
        FileChooser fileChooser = setupFileChooser("Esporta Inventario Prodotti", "inventario_prodotti.csv");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                writeProdottiToCSV(file, prodotti);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Apre un FileChooser e scrive la lista di Movimenti nel file selezionato.
     * Effettua la scrittura chiamando internamente {@link #writeMovimentiToCSV(File, ObservableList, ObservableList)}.
     *
     * @param stage     Lo Stage genitore per la finestra di dialogo.
     * @param movimenti La lista di movimenti da esportare.
     * @param prodotti  La lista dei prodotti utilizzata per risolvere i nomi dei prodotti riferiti dai movimenti.
     */
    public static boolean exportMovimentiToCSV(Stage stage, ObservableList<Movimenti> movimenti, ObservableList<Prodotti> prodotti) {
        FileChooser fileChooser = setupFileChooser("Esporta Storico Movimenti", "storico_movimenti.csv");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                writeMovimentiToCSV(file, movimenti, prodotti);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Configura il FileChooser con i filtri e il nome di default.
     *
     * @param title           Titolo della finestra FileChooser.
     * @param defaultFileName Nome file iniziale suggerito nella finestra di salvataggio.
     * @return FileChooser configurato con filtro CSV e nome di default.
     */
    private static FileChooser setupFileChooser(String title, String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(defaultFileName);
        // Aggiunge un filtro per i file CSV
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv")
        );
        return fileChooser;
    }

    /**
     * Logica di scrittura effettiva per i Prodotti.
     * Scrive l'intestazione e successivamente ogni riga corrispondente a un prodotto nella lista.
     * Eventuali IOException vengono gestite internamente e loggate.
     *
     * @param file     File di destinazione dove scrivere il CSV.
     * @param prodotti Lista di prodotti da esportare.
     */
    private static void writeProdottiToCSV(File file, ObservableList<Prodotti> prodotti) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

            writer.println("ID_Prodotto" + CSV_SEPARATOR + "Nome" + CSV_SEPARATOR + "Descrizione" + CSV_SEPARATOR +
                    "Quantita_Attuale" + CSV_SEPARATOR + "Giacenza_Minima" + CSV_SEPARATOR +
                    "Prezzo_Acquisto" + CSV_SEPARATOR + "Prezzo_Vendita");

            for (Prodotti p : prodotti) {
                writer.println(
                        p.getIdProdotto() + CSV_SEPARATOR +
                                p.getNome() + CSV_SEPARATOR +
                                p.getDescrizione() + CSV_SEPARATOR +
                                p.getQuantita() + CSV_SEPARATOR +
                                p.getGiacenzaMin() + CSV_SEPARATOR +
                                p.getPrezzoAcquisto() + CSV_SEPARATOR +
                                p.getPrezzoVendita()
                );
            }
            LOGGER.log(Level.INFO, "Inventario prodotti esportato con successo in: " + file.getAbsolutePath());

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'esportazione dei Prodotti in CSV: " + e.getMessage());
        }
    }

    /**
     * Logica di scrittura effettiva per i Movimenti.
     * Costruisce una mappa id->nome prodotto per migliorare l'efficienza durante la scrittura.
     * La data viene formattata usando il pattern yyyy-MM-dd; la descrizione viene sanificata
     * rimuovendo il separatore CSV per evitare rotture del formato.
     *
     * @param file      File di destinazione dove scrivere il CSV.
     * @param movimenti Lista di movimenti da esportare.
     * @param prodotti  Lista di prodotti utilizzata per risolvere i nomi dei prodotti nei movimenti.
     */
    private static void writeMovimentiToCSV(File file, ObservableList<Movimenti> movimenti, ObservableList<Prodotti> prodotti) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

            writer.println("ID_Movimento" + CSV_SEPARATOR + "Nome prodotto" + CSV_SEPARATOR + "Tipo" + CSV_SEPARATOR +
                    "Quantita" + CSV_SEPARATOR + "Data" + CSV_SEPARATOR + "Descrizione");

            // Formattatore per la data del movimento
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Map<Integer, String> prodottoMap = new HashMap<>();
            for (Prodotti p : prodotti) {
                prodottoMap.put(p.getIdProdotto(), p.getNome());
            }

            for (Movimenti m : movimenti) {
                String dataFormatted = (m.getData() != null) ? m.getData().format(dateFormatter) : "";

                writer.println(
                        m.getIdMovimento() + CSV_SEPARATOR +
                                prodottoMap.get(m.getIdProdotto()) + CSV_SEPARATOR +
                                m.getTipo() + CSV_SEPARATOR +
                                m.getQuantita() + CSV_SEPARATOR +
                                dataFormatted + CSV_SEPARATOR +
                                m.getDescrizione().replace(CSV_SEPARATOR, "") // Rimuovi il separatore dalla descrizione per sicurezza
                );
            }
            LOGGER.log(Level.INFO, "Storico movimenti esportato con successo in: " + file.getAbsolutePath());

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'esportazione dei Movimenti in CSV: " + e.getMessage());
        }
    }
}
