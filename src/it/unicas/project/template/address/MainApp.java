package it.unicas.project.template.address;

import it.unicas.project.template.address.model.Movimenti;
import it.unicas.project.template.address.model.Prodotti;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.DAOMySQLSettings;
import it.unicas.project.template.address.model.dao.mysql.MovimentiDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.ProdottiDAOMySQLImpl;
import it.unicas.project.template.address.view.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Classe principale dell'applicazione JavaFX per la gestione del magazzino.
 * Si occupa dell'inizializzazione della GUI, del caricamento dei dati e dell'apertura delle finestre di dialogo.
 *
 *  @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 *  @since 19/11/2025
 */
public class MainApp extends Application {

    /**
     * Stage principale dell'applicazione.
     * @see #getPrimaryStage()
     */
    private Stage primaryStage;

    /**
     * Root layout (BorderPane) dell'applicazione.
     */
    private BorderPane rootLayout;

    /**
     * Lista osservabile dei prodotti critici (ad esempio con quantità bassa).
     */
    private ObservableList<Prodotti> prodottiCritici = FXCollections.observableArrayList();

    /**
     * Lista osservabile di tutti i prodotti.
     */
    private ObservableList<Prodotti> prodottiData = FXCollections.observableArrayList();

    /**
     * Lista osservabile dei movimenti registrati.
     */
    private ObservableList<Movimenti> movimentiData = FXCollections.observableArrayList();


    /**
     * Costruttore della classe MainApp.
     * Inizializza i dati caricandoli dal database.
     */
    public MainApp() {
        caricaDati();
    }

    /**
     * Carica i dati dei prodotti e dei movimenti dal database.
     * <br>
     * Pre-condizione: la connessione al DB deve essere disponibile; eventuali errori sono catturati e stampati su stderr.
     * <br>
     * Post-condizione: le liste osservabili {@link #prodottiData}, {@link #prodottiCritici} e {@link #movimentiData}
     * sono popolate con i dati prelevati dal database.
     */
    public void caricaDati(){
        prodottiData.clear();
        prodottiCritici.clear();
        movimentiData.clear();
        try {
            prodottiData.addAll(ProdottiDAOMySQLImpl.getInstance().select(null, false));
            prodottiCritici.addAll(ProdottiDAOMySQLImpl.getInstance().select(null, true));
            movimentiData.addAll(MovimentiDAOMySQLImpl.getInstance().select(null, false));
        } catch (DAOException e) {
            System.err.println("Errore caricamento dati dal DB: " + e.getMessage());
        }
    }

    /**
     * Restituisce la lista osservabile dei prodotti.
     *
     * @return la {@link ObservableList} di {@link Prodotti} visibile alla UI
     */
    public ObservableList<Prodotti> getProdottiData() {
        return prodottiData;
    }

    /**
     * Restituisce la lista osservabile dei prodotti critici.
     *
     * @return la {@link ObservableList} di prodotti critici
     */
    public ObservableList<Prodotti> getProdottiCritici() {
        return prodottiCritici;
    }

    /**
     * Restituisce la lista osservabile dei movimenti.
     *
     * @return la {@link ObservableList} di {@link Movimenti}
     */
    public ObservableList<Movimenti> getMovimentiData() {
        return movimentiData;
    }

    /**
     * Entry point JavaFX: imposta lo stage primario, mostra il dialogo di autenticazione
     * e, se l'accesso va a buon fine, inizializza e mostra l'interfaccia principale.
     *
     * @param primaryStage lo stage principale fornito dal runtime JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestione Magazzino");

        primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));

        boolean autenticazione = showAutenticazioneDialog();

        if (autenticazione) {
            initRootLayout();
            showProdottiOverview();
            primaryStage.setMaximized(true);
            primaryStage.show();
        } else {
            System.exit(0);
        }
    }

    /**
     * Mostra la finestra di dialogo di login e attende l'autenticazione.
     *
     * @return true se l'utente ha effettuato l'accesso con successo, false altrimenti
     */
    public boolean showAutenticazioneDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AutenticazioneDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Autenticazione");
            dialogStage.getIcons().add(new Image("file:resources/images/key.png"));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setResizable(false);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AutenticazioneDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            return controller.isAccesso();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inizializza il layout principale (root layout) caricando il file FXML e impostando il controller.
     * <br>
     * Post-condizione: {@link #rootLayout} è inizializzato e collegato allo stage principale.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(windowEvent ->
            {
                windowEvent.consume();
                handleExit();
            });

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra un dialog di conferma per l'uscita dall'applicazione e, se confermato,
     * termina il processo.
     * <br>
     * Post-condizione: l'applicazione termina se l'utente conferma l'uscita.
     */
    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Uscita");
        alert.setHeaderText("Vuoi davvero uscire dall'applicazione?");
        alert.setContentText("I dati sono già stati salvati automaticamente nel database.");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image("file:resources/images/exit.png"));
        ButtonType exitButton = new ButtonType("Esci", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(exitButton, cancelButton);

        alert.getDialogPane().setPrefWidth(380);
        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(button -> {
            if (button == exitButton) {
                System.exit(0);
            }
        });
    }



    /**
     * Mostra la vista principale dei prodotti all'interno del root layout.
     */
    public void showProdottiOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ProdottiOverview.fxml"));

            rootLayout.setCenter(loader.load());

            ProdottiOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra il dialog per modificare le impostazioni di accesso al DB.
     *
     * @param daoMySQLSettings istanza di {@link DAOMySQLSettings} da modificare
     * @return true se l'utente ha confermato le modifiche, false altrimenti
     */
    public boolean showSettingsEditDialog(DAOMySQLSettings daoMySQLSettings){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SettingsEditDialog.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.setTitle("DAO settings");
            dialogStage.initModality((Modality.WINDOW_MODAL));
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            SettingsEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSettings(daoMySQLSettings);

            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Apre il dialog per l'editing di un prodotto.
     *
     * @param Prodotti l'oggetto {@link Prodotti} da modificare
     * @param verifyLen flag che indica se applicare la verifica delle lunghezze
     * @param titolo il titolo della finestra di dialogo
     * @param immagine percorso dell'immagine icona per il dialogo
     * @return true se l'utente ha confermato le modifiche, false altrimenti
     */
    public boolean showProdottiDialog(Prodotti Prodotti, boolean verifyLen, String titolo, String immagine) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ProdottiEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titolo);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(immagine));

            ProdottiEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage, verifyLen);
            controller.setProdotto(Prodotti);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Apre il dialog per registrare un movimento su un prodotto (carico/scarico).
     *
     * @param prodotto il prodotto oggetto del movimento
     * @param movimento l'istanza di {@link Movimenti} che verrà popolata
     * @return true se l'utente ha confermato l'operazione, false altrimenti
     */
    public boolean showMovimentoDialog(Prodotti prodotto, Movimenti movimento) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ScaricoDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Movimento prodotto");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ScaricoDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProdotto(prodotto);
            controller.setMovimento(movimento);

            dialogStage.getIcons().add(new Image("file:resources/images/move.png"));

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mostra le statistiche relative ai movimenti.
     * La finestra viene mostrata in modalità modale non bloccante (maximizzata).
     */
    public void showMovimentiStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MovimentiStatistics.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Statistiche movimenti");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(loader.load()));
            dialogStage.getIcons().add(new Image("file:resources/images/statistics.png"));
            dialogStage.setMaximized(true);

            MovimentiStatisticsController controller = loader.getController();
            controller.setMovimentiData(movimentiData, null);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra le statistiche relative ai prodotti.
     */
    public void showProdottiStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MovimentiStatistics.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Statistiche prodotti");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(loader.load()));
            dialogStage.getIcons().add(new Image("file:resources/images/statistics.png"));
            dialogStage.setMaximized(true);

            MovimentiStatisticsController controller = loader.getController();
            controller.setProdottiData(prodottiData, movimentiData);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce lo stage principale dell'applicazione.
     *
     * @return lo {@link Stage} principale
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Avvio dell'applicazione.
     *
     * @param args argomenti della riga di comando
     */
    public static void main(String[] args) {
        MainApp.launch(args);
        //System.out.println("Finito");
    }
}