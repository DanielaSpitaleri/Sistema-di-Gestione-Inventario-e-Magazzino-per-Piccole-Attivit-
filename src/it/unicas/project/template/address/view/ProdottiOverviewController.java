package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.Movimenti;
import it.unicas.project.template.address.model.Prodotti;
import it.unicas.project.template.address.model.dao.mysql.MovimentiDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.ProdottiDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller per la vista principale di gestione dei prodotti.
 * <br>
 * Coordina la visualizzazione delle tabelle (prodotti, prodotti critici, movimenti),
 * gestisce le azioni utente (nuovo, modifica, elimina, sposta, ricerca) e aggiorna i dati
 * nell'interfaccia in seguito alle operazioni sul database.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class ProdottiOverviewController {

    /**
     * Tabella principale dei prodotti (FXML).
     * @see #setMainApp(MainApp)
     */
    @FXML
    private TableView<Prodotti> prodottiTableView;

    /**
     * Colonna per il nome del prodotto (FXML).
     */
    @FXML
    private TableColumn<Prodotti, String> nomeColumn;

    /**
     * Colonna per la descrizione del prodotto (FXML).
     */
    @FXML
    private TableColumn<Prodotti, String> descrizioneColumn;

    /**
     * Tabella dei prodotti critici (sotto-scorta) (FXML).
     */
    @FXML
    private TableView<Prodotti> criticitaTableView;

    /**
     * Colonna nome per i prodotti critici (FXML).
     */
    @FXML
    private TableColumn<Prodotti, String> nomeCriticoColumn;

    /**
     * Colonna per la quantità critica (differenza tra giacenza minima e quantità).
     */
    @FXML
    private TableColumn<Prodotti, Integer> quantitaCriticaColumn;

    /**
     * Tabella dei movimenti (FXML).
     */
    @FXML
    private TableView<Movimenti> movimentiTableView;

    /**
     * Colonna del tipo di movimento (CARICO/SCARICO) (FXML).
     */
    @FXML
    private TableColumn<Movimenti, String> tipoMovimentoColumn;

    /**
     * Colonna della quantità del movimento (FXML).
     */
    @FXML
    private TableColumn<Movimenti, Integer> quantitaMovimentoColumn;

    /**
     * Colonna della data del movimento (FXML).
     */
    @FXML
    private TableColumn<Movimenti, LocalDate> dataMovimentoColumn;

    /**
     * Colonna per il nome del prodotto associato al movimento (FXML).
     * @see #setMainApp(MainApp)
     */
    @FXML
    private TableColumn<Movimenti, String> nomeProdottoMovimentoColumn;

    /**
     * Label per il nome prodotto nei dettagli (FXML).
     */
    @FXML
    private Label nomeLabel;

    /**
     * Label per la descrizione prodotto nei dettagli (FXML).
     */
    @FXML
    private Label descrizioneLabel;

    /**
     * Label per la quantità prodotto nei dettagli (FXML).
     */
    @FXML
    private Label quantitaLabel;

    /**
     * Label per la giacenza minima nei dettagli (FXML).
     */
    @FXML
    private Label giacenzaMinimaLabel;

    /**
     * Label per il prezzo d'acquisto nei dettagli (FXML).
     */
    @FXML
    private Label prezzoAcquistoLabel;

    /**
     * Label per il prezzo di vendita nei dettagli (FXML).
     */
    @FXML
    private Label prezzoVenditaLabel;

    /**
     * Contenitore (HBox) che mostra lo stato di disponibilità (FXML).
     * <br>
     * Usato per nascondere/mostrare dinamicamente lo status.
     */
    @FXML
    private HBox statusContainer;

    /**
     * Regione quadrata colorata che indica lo stato (FXML).
     */
    @FXML
    private Region statusSquare;  // Il quadrato colorato

    /**
     * Label che descrive lo stato (es. "Disponibile", "Da riordinare") (FXML).
     */
    @FXML
    private Label statusLabel;

    /**
     * Campo di ricerca (FXML). Utilizzato dal metodo {@link #resetFilters()}.
     */
    @FXML
    private TextField searchField;

    /**
     * Riferimento all'istanza principale dell'applicazione.
     * <br>
     * Utilizzato per accedere ai dati condivisi e per mostrare dialog/modal.
     * @see #setMainApp(MainApp)
     */
    private MainApp mainApp;

    /**
     * Costruttore vuoto richiesto da JavaFX.
     */
    public ProdottiOverviewController() {
    }

    /**
     * Inizializza il controller. Questo metodo viene chiamato automaticamente
     * dopo che il file FXML è stato caricato.
     * <br>
     * Configura le colonne delle tabelle, i listener di selezione e lo stato iniziale
     * dei dettagli del prodotto.
     */
    @FXML
    private void initialize() {
        // Collegamento colonne
        nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        descrizioneColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

        nomeCriticoColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        quantitaCriticaColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() ->
                        cellData.getValue().getGiacenzaMin() - cellData.getValue().getQuantita(),
                cellData.getValue().giacenzaMinProperty(),
                cellData.getValue().quantitaProperty()
        ));
        tipoMovimentoColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        quantitaMovimentoColumn.setCellValueFactory(cellData -> cellData.getValue().quantitaProperty().asObject());
        dataMovimentoColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());

        // --- GESTIONE DINAMICA DELLO SPAZIO (Status Container) ---
        // Usiamo 'statusContainer' (l'HBox che contiene Quadrato + Scritta).
        // Bind: Se visible=false -> managed=false (Spazio liberato, pulsanti scendono).
        //       Se visible=true  -> managed=true  (Spazio occupato, pulsanti salgono).
        if (statusContainer != null) {
            statusContainer.managedProperty().bind(statusContainer.visibleProperty());
        }
        // ---------------------------------------------------------

        showProdottoDetails(null);
        prodottiTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProdottoDetails(newValue)
        );
    }

    /**
     * Informa il controller dell'istanza principale dell'applicazione e inizializza
     * le liste/tableView con i dati condivisi dall'app.
     *
     * @param mainApp l'istanza principale {@link MainApp} dell'applicazione
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        prodottiTableView.setItems(mainApp.getProdottiData());
        criticitaTableView.setItems(mainApp.getProdottiCritici());
        movimentiTableView.setItems(mainApp.getMovimentiData());

        nomeProdottoMovimentoColumn.setCellValueFactory(cellData -> {
            int pid = cellData.getValue().getIdProdotto();
            Prodotti prod = mainApp.getProdottiData()
                    .stream()
                    .filter(p -> p.getIdProdotto() == pid)
                    .findFirst()
                    .orElse(null);
            String display = (prod != null) ? prod.getNome() : String.valueOf(pid);
            return new javafx.beans.property.SimpleStringProperty(display);
        });
    }

    /**
     * Resetta i filtri di ricerca presenti nella vista.
     * Se il campo di ricerca esiste, il suo contenuto viene svuotato.
     */
    public void resetFilters() {
        if (searchField != null) {
            searchField.setText("");
        }
    }

    /**
     * Mostra i dettagli del prodotto selezionato nell'area dedicata.
     * <br>
     * Aggiorna le label con le informazioni del prodotto e
     * modifica lo stato visivo in base alla disponibilità.
     *
     * @param p il prodotto selezionato; se null, i dettagli vengono puliti
     */
    private void showProdottoDetails(Prodotti p) {
        if (p != null) {
            nomeLabel.setText(p.getNome());
            descrizioneLabel.setText(p.getDescrizione());
            quantitaLabel.setText(String.valueOf(p.getQuantita()));
            giacenzaMinimaLabel.setText(String.valueOf(p.getGiacenzaMin()));
            prezzoAcquistoLabel.setText(String.valueOf(p.getPrezzoAcquisto()));
            prezzoVenditaLabel.setText(String.valueOf(p.getPrezzoVendita()));

            if (p.vincoloQuantita()) {
                quantitaLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

                if (statusSquare != null) {
                    statusSquare.setStyle("-fx-background-color: red; -fx-background-radius: 2; -fx-border-color: #444444;");
                }

                if (statusLabel != null) {
                    statusLabel.setText("Da riordinare");
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                }

            } else {
                quantitaLabel.setStyle("-fx-text-fill: #106524; -fx-font-weight: bold;");

                if (statusSquare != null) {
                    statusSquare.setStyle("-fx-background-color: #106524; -fx-background-radius: 2; -fx-border-color: #444444;");
                }

                if (statusLabel != null) {
                    statusLabel.setText("Disponibile");
                    statusLabel.setStyle("-fx-text-fill: #106524; -fx-font-weight: bold;");
                }
            }

            if (statusContainer != null) statusContainer.setVisible(true);

        } else {
            nomeLabel.setText("");
            descrizioneLabel.setText("");
            quantitaLabel.setText("");
            giacenzaMinimaLabel.setText("");
            prezzoAcquistoLabel.setText("");
            prezzoVenditaLabel.setText("");

            if (statusContainer != null) statusContainer.setVisible(false);
        }
    }

    /**
     * Gestisce l'azione di eliminazione del prodotto selezionato.
     * <br>
     * Mostra una conferma, elimina il prodotto dal database e aggiorna
     * le liste/tableView nell'interfaccia utente.
     */
    @FXML
    private void handleDeleteProdotto() {
        Prodotti selectedProdotto = prodottiTableView.getSelectionModel().getSelectedItem();
        if (selectedProdotto != null){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Attenzione");
            alert.setHeaderText("Eliminare il Prodotto Selezionato?");
            alert.setContentText("Sei sicuro di voler eliminare: " + selectedProdotto.getNome() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    ProdottiDAOMySQLImpl.getInstance().delete(selectedProdotto);

                    mainApp.caricaDati();
                } catch (DAOException e) {
                    showError("Errore di Eliminazione", e.getMessage());
                }
            }
        } else {
            showWarning("Nessuna Selezione", "Seleziona un prodotto da eliminare.");
        }
    }

    /**
     * Gestisce l'azione di creazione di un nuovo prodotto.
     * <br>
     * Mostra una dialog per l'inserimento dei dati, salva il nuovo prodotto
     * nel database e aggiorna le liste/tableView nell'interfaccia utente.
     */
    @FXML
    private void handleNewProdotto() {
        Prodotti temp = new Prodotti();
        boolean okClicked = mainApp.showProdottiDialog(temp, true, "Nuovo prodotto", "file:resources/images/new.png");
        if (okClicked) {
            try {
                int id = ProdottiDAOMySQLImpl.getInstance().insert(temp);
                temp.setIdProdotto(id);

                Movimenti movimento = new Movimenti(null, id, "CARICO", temp.getQuantita(), LocalDate.now(), "Carico iniziale");
                MovimentiDAOMySQLImpl.getInstance().insert(movimento);

                mainApp.caricaDati();
                prodottiTableView.getSelectionModel().select(temp);
                prodottiTableView.scrollTo(temp);
                showProdottoDetails(temp);
            } catch (DAOException e) {

                String msg = e.getMessage();
                if (msg != null && msg.contains("Duplicate entry") && msg.contains("prodotti.nome_UNIQUE")) {
                    showError("Attenzione", "Il nome '" + temp.getNome() + "' è già presente in archivio.");
                }
                else showError("Errore Database", e.getMessage());
            }
        }
    }

    /**
     * Gestisce l'azione di ricerca prodotti.
     * <br>
     * Mostra una dialog per l'inserimento dei criteri di ricerca,
     * esegue la query sul database e aggiorna la lista dei prodotti
     * visualizzati nell'interfaccia utente.
     */
    @FXML
    private void handleSearchProdotti() {
        Prodotti tempProdotto = new Prodotti(null, "", "", -1, -1, 0.0, 0.0);
        boolean okClicked = mainApp.showProdottiDialog(tempProdotto, false, "Cerca prodotti", "file:resources/images/search.png");
        if (okClicked) {
            try {
                List<Prodotti> list = ProdottiDAOMySQLImpl.getInstance().select(tempProdotto, false);

                mainApp.getProdottiData().clear();
                mainApp.getProdottiData().addAll(list);
            } catch (DAOException e) {
                showError("Errore Ricerca", e.getMessage());
            }
        }
    }

    /**
     * Gestisce l'azione di modifica del prodotto selezionato.
     * <br>
     * Mostra una dialog per l'editing dei dati, aggiorna il prodotto
     * nel database e riflette le modifiche nell'interfaccia utente.
     */
    @FXML
    private void handleEditProdotti() {
        Prodotti selectedProdotto = prodottiTableView.getSelectionModel().getSelectedItem();
        if (selectedProdotto != null) {
            boolean okClicked = mainApp.showProdottiDialog(selectedProdotto, true, "Modifica prodotto", "file:resources/images/edit.png");
            if (okClicked) {
                try {
                    ProdottiDAOMySQLImpl.getInstance().update(selectedProdotto);

                    mainApp.caricaDati();
                    showProdottoDetails(selectedProdotto);
                    prodottiTableView.getSelectionModel().select(selectedProdotto);
                    prodottiTableView.scrollTo(selectedProdotto);
                } catch (DAOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showWarning("Nessuna selezione", "Seleziona un prodotto nel catalogo");
        }
    }

    /**
     * Gestisce l'azione di movimento (carico/scarico) del prodotto selezionato.
     * <br>
     * Mostra una dialog per l'inserimento del movimento, aggiorna il database
     * e riflette le modifiche nell'interfaccia utente.
     */
    @FXML
    private void handleMoveProdotto() {
        Movimenti movimento = new Movimenti();
        Prodotti selectedProdotto = prodottiTableView.getSelectionModel().getSelectedItem();
        if (selectedProdotto != null) {
            boolean okClicked = mainApp.showMovimentoDialog(selectedProdotto, movimento);
            if (okClicked) {
                try {
                    MovimentiDAOMySQLImpl.getInstance().insert(movimento);

                    int nuovaQuantita = selectedProdotto.getQuantita();
                    if (movimento.getTipo().equals("CARICO")) {
                        nuovaQuantita += movimento.getQuantita();
                    } else if (movimento.getTipo().equals("SCARICO")) {
                        nuovaQuantita -= movimento.getQuantita();
                    }
                    selectedProdotto.setQuantita(nuovaQuantita);
                    ProdottiDAOMySQLImpl.getInstance().update(selectedProdotto);

                    mainApp.caricaDati();
                    showProdottoDetails(selectedProdotto);
                    prodottiTableView.getSelectionModel().select(selectedProdotto);
                    prodottiTableView.scrollTo(selectedProdotto);
                } catch (DAOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showWarning("Nessuna selezione", "Seleziona un prodotto nel catalogo");
        }
    }

    /**
     * Mostra un messaggio di errore in una finestra di dialog.
     *
     * @param header l'intestazione del messaggio
     * @param msg    il contenuto del messaggio
     */
    private void showError(String header, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio di avviso in una finestra di dialog.
     *
     * @param header l'intestazione del messaggio
     * @param msg    il contenuto del messaggio
     */
    private void showWarning(String header, String msg) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Attenzione");
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
