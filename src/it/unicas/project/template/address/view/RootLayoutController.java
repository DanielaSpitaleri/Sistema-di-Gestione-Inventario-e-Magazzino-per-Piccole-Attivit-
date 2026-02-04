package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.dao.mysql.DAOMySQLSettings;
import it.unicas.project.template.address.util.Esportazione;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller per il layout radice dell'applicazione.
 * <br>
 * Fornisce i gestori delle voci di menu (settings, about, home, exit, esportazioni, statistiche)
 * e coordina azioni globali che coinvolgono la MainApp.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class RootLayoutController {

    /**
     * Riferimento all'istanza principale dell'applicazione.
     * Utilizzato per aprire le varie viste e per accedere ai dati condivisi.
     * @see #setMainApp(MainApp)
     */
    private MainApp mainApp;

    /**
     * Imposta il riferimento all'istanza principale dell'applicazione.
     *
     * @param mainApp l'istanza {@link MainApp} da associare a questo controller
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Gestore per l'azione di apertura della finestra di modifica delle impostazioni del database MySQL.
     * <br>
     * Se le impostazioni vengono modificate e salvate, aggiorna l'istanza corrente delle impostazioni.
     */
    @FXML
    private void handleSettings() {
        DAOMySQLSettings daoMySQLSettings = DAOMySQLSettings.getCurrentDAOMySQLSettings();
        if (mainApp.showSettingsEditDialog(daoMySQLSettings)){
            DAOMySQLSettings.setCurrentDAOMySQLSettings(daoMySQLSettings);
            mainApp.caricaDati();
        }
    }

    /**
     * Gestore per l'azione di apertura della finestra "About".
     * <br>
     * Mostra informazioni sugli autori e sul sito web del progetto.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Gestione Magazzino");
        alert.setHeaderText("About");
        alert.setContentText("Autori: C. Corsini, S. Feola, D. Spitaleri\nWebsite: http://www.unicas.it");

        alert.showAndWait();
    }

    /**
     * Gestore per l'azione di ritorno alla vista principale dei prodotti.
     * <br>
     * Ricarica i dati dei prodotti dalla fonte dati.
     */
    @FXML
    private void handleGoHome() {
        if (mainApp != null) {
            mainApp.showProdottiOverview();
            mainApp.caricaDati();
        }
    }

    /**
     * Gestore per l'azione di uscita dall'applicazione.
     */
    @FXML
    private void handleExit() {
        mainApp.handleExit();
    }

    /**
     * Mostra le statistiche sui movimenti.
     */
    @FXML
    private void handleShowMovimentiStatistics() {
        mainApp.showMovimentiStatistics();
    }

    /**
     * Mostra le statistiche sui prodotti.
     */
    @FXML
    private void handleShowProdottiStatistics() {
        mainApp.showProdottiStatistics();
    }

    /**
     * Esporta l'inventario prodotti in un file CSV.
     */
    @FXML
    private void handleExportProdotti() {
        Stage stage = mainApp.getPrimaryStage();

        boolean esportazione = Esportazione.exportProdottiToCSV(stage, mainApp.getProdottiData());

        if (esportazione) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("Esportazione Completata");
            alert.setHeaderText(null);
            alert.setContentText("L'inventario dei prodotti è stato esportato in formato CSV.");
            alert.showAndWait();
        }
    }

    /**
     * Esporta lo storico dei movimenti in un file CSV.
     */
    @FXML
    private void handleExportMovimenti() {
        Stage stage = mainApp.getPrimaryStage();

        boolean esportazione = Esportazione.exportMovimentiToCSV(stage, mainApp.getMovimentiData(), mainApp.getProdottiData());

        if (esportazione) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("Esportazione Completata");
            alert.setHeaderText(null);
            alert.setContentText("Lo storico dei movimenti è stato esportato in formato CSV.");
            alert.showAndWait();
        }
    }
}
