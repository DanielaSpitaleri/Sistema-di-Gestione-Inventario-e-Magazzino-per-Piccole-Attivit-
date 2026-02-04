package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.Prodotti;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller per il dialog di creazione/modifica/ricerca di un {@link Prodotti}.
 * <br>
 * Gestisce i campi della UI, la validazione dell'input e la scrittura dei valori
 * nell'oggetto {@code Prodotti} passato dall'esterno.
 * <br>
 * Supporta due modalità operative:
 * <ul>
 *   <li>Modalità edit/new: tutti i campi obbligatori devono essere compilati correttamente</li>
 *   <li>Modalità search: i campi possono essere lasciati vuoti e vengono trattati con valori sentinella</li>
 * </ul>
 * La modalità è determinata dal flag {@code verifyLen}.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class ProdottiEditDialogController {

    /**
     * Campo di input per il nome del prodotto (FXML).
     * @see #setProdotto(Prodotti)
     */
    @FXML
    private TextField nomeField;
    /**
     * Campo di input per la descrizione del prodotto (FXML).
     * @see #setProdotto(Prodotti)
     */
    @FXML
    private TextField descrizioneField;
    /**
     * Campo di input per la quantità del prodotto (FXML).
     * @see #isInputValid()
     */
    @FXML
    private TextField quantitaField;
    /**
     * Campo di input per la giacenza minima del prodotto (FXML).
     * @see #isInputValid()
     */
    @FXML
    private TextField giacenzaMinField;
    /**
     * Campo di input per il prezzo d'acquisto del prodotto (FXML).
     * @see #isInputValid()
     */
    @FXML
    private TextField prezzoAcquistoField;
    /**
     * Campo di input per il prezzo di vendita del prodotto (FXML).
     * @see #isInputValid()
     */
    @FXML
    private TextField prezzoVenditaField;

    /**
     * Stage della finestra di dialogo associata a questo controller.
     * <br>
     * Utilizzato per chiudere il dialog dopo l'OK o Cancel.
     * @see #handleOk()
     * @see #handleCancel()
     */
    private Stage dialogStage;

    /**
     * Riferimento all'oggetto {@link Prodotti} manipolato dal dialog.
     * <br>
     * I valori dei campi vengono letti/scritti in questo oggetto.
     * @see #setProdotto(Prodotti)
     */
    private Prodotti prodotto;

    /**
     * Flag che indica se l'utente ha confermato l'operazione con OK.
     * {@code true} se OK premuto e validazione passata.
     * @see #isOkClicked()
     */
    private boolean okClicked = false;

    /**
     * Modalità di funzionamento:
     * {@code true} = validazione rigorosa (edit/new),
     * {@code false} = modalità ricerca (campi opzionali, uso di sentinelle).
     * @see #setDialogStage(Stage, boolean)
     */
    private boolean verifyLen = true;

    /**
     * Inizializza il controller dopo che i campi FXML sono stati caricati.
     * <br>
     * Non implementato.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Imposta lo stage del dialog e la modalità di validazione.
     *
     * @param dialogStage lo stage della finestra di dialogo associata
     * @param verifyLen   {@code true} per modalità edit/new (validazione obbligatoria), {@code false} per modalità ricerca
     */
    public void setDialogStage(Stage dialogStage, boolean verifyLen) {
        this.dialogStage = dialogStage;
        this.verifyLen = verifyLen;
    }

    /**
     * Imposta l'oggetto {@link Prodotti} da mostrare/modificare nel dialog.
     * <br>
     * Aggiorna i campi della UI in base allo stato dell'oggetto e alla {@code verifyLen}.
     *
     * @param prodotto l'istanza di {@link Prodotti} da associare al dialog (non null)
     */
    public void setProdotto(Prodotti prodotto) {
        this.prodotto = prodotto;

        // Nome/descrizione sempre sicuri
        nomeField.setText(prodotto.getNome() == null ? "" : prodotto.getNome());
        descrizioneField.setText(prodotto.getDescrizione() == null ? "" : prodotto.getDescrizione());

        // Rileviamo se il prodotto è "nuovo"
        boolean newOrUnsaved = (prodotto.getIdProdotto() == null) || (prodotto.getIdProdotto() <= 0);

        if (verifyLen) {
            // Modalità edit/new: per prodotti nuovi non mostrare valori numerici di default,
            // per prodotti esistenti mostrare i valori reali (anche 0 se valido).
            if (newOrUnsaved) {
                quantitaField.setText("");
                giacenzaMinField.setText("");
                prezzoAcquistoField.setText("");
                prezzoVenditaField.setText("");
            } else {
                quantitaField.setText(String.valueOf(prodotto.getQuantita()));
                giacenzaMinField.setText(String.valueOf(prodotto.getGiacenzaMin()));
                prezzoAcquistoField.setText(String.valueOf(prodotto.getPrezzoAcquisto()));
                prezzoVenditaField.setText(String.valueOf(prodotto.getPrezzoVendita()));
            }
        } else {
            // Modalità ricerca: mostra solo valori significativi/forniti dall'utente.
            quantitaField.setText(prodotto.getQuantita() > -1 ? String.valueOf(prodotto.getQuantita()) : "");
            giacenzaMinField.setText(prodotto.getGiacenzaMin() > -1 ? String.valueOf(prodotto.getGiacenzaMin()) : "");
            prezzoAcquistoField.setText(prodotto.getPrezzoAcquisto() > 0.0 ? String.valueOf(prodotto.getPrezzoAcquisto()) : "");
            prezzoVenditaField.setText(prodotto.getPrezzoVendita() > 0.0 ? String.valueOf(prodotto.getPrezzoVendita()) : "");
        }
    }

    /**
     * Indica se l'utente ha confermato l'azione tramite il dialog (OK).
     *
     * @return {@code true} se OK è stato premuto e la validazione è stata superata; {@code false} altrimenti
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Gestisce l'azione di conferma del dialog.
     * <br>
     * Valida l'input e, se valido, aggiorna l'oggetto {@link #prodotto} con i valori dei campi.
     * Chiude il dialog se l'input è valido.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            // In modalità ricerca alcuni campi possono restare vuoti: impostiamo valori sentinella nel prodotto
            String nome = nomeField.getText() == null || nomeField.getText().trim().isEmpty() ? null : nomeField.getText().trim();
            String descr = descrizioneField.getText() == null || descrizioneField.getText().trim().isEmpty() ? null : descrizioneField.getText().trim();

            prodotto.setNome(nome);
            prodotto.setDescrizione(descr);

            // Quantità: se vuoto in ricerca -> -1 (sentinella), altrimenti parse
            if (quantitaField.getText() == null || quantitaField.getText().trim().isEmpty()) {
                prodotto.setQuantita(verifyLen ? 0 : -1);
            } else {
                prodotto.setQuantita(Integer.parseInt(quantitaField.getText().trim()));
            }

            // Giacenza minima: se vuoto in ricerca -> -1 (non considerare), altrimenti parse
            if (giacenzaMinField.getText() == null || giacenzaMinField.getText().trim().isEmpty()) {
                prodotto.setGiacenzaMin(-1);
            } else {
                prodotto.setGiacenzaMin(Integer.parseInt(giacenzaMinField.getText().trim()));
            }

            // Prezzi: se vuoti in ricerca -> 0.0 (non considerare), altrimenti parse
            if (prezzoAcquistoField.getText() == null || prezzoAcquistoField.getText().trim().isEmpty()) {
                prodotto.setPrezzoAcquisto(0.0);
            } else {
                prodotto.setPrezzoAcquisto(Double.parseDouble(prezzoAcquistoField.getText().trim()));
            }

            if (prezzoVenditaField.getText() == null || prezzoVenditaField.getText().trim().isEmpty()) {
                prodotto.setPrezzoVendita(0.0);
            } else {
                prodotto.setPrezzoVendita(Double.parseDouble(prezzoVenditaField.getText().trim()));
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Gestisce l'azione di cancellazione del dialog.
     * <br>
     * Chiude semplicemente la finestra senza salvare modifiche.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Valida l'input dell'utente nei campi del dialog.
     * <br>
     * In modalità edit/new (verifyLen = true) tutti i campi obbligatori devono essere compilati correttamente.
     * <br>
     * In modalità ricerca (verifyLen = false) i campi possono essere lasciati vuoti e vengono trattati con valori sentinella.
     *
     * @return {@code true} se l'input è valido; {@code false} altrimenti
     */
    private boolean isInputValid() {
        String errorMessage = "";

        // Nome obbligatorio solo in modalità edit/new (verifyLen == true)
        if (verifyLen && (nomeField.getText() == null || nomeField.getText().trim().isEmpty())) {
            errorMessage += "Nome non valido!\n";
        }

        // Descrizione obbligatoria solo in modalità edit/new (verifyLen == true)
        if (verifyLen && (descrizioneField.getText() == null || descrizioneField.getText().trim().length() == 0)) {
            errorMessage += "Descrizione non valida!\n";
        }

        // Quantità: se vuota in modalità search -> ok; altrimenti deve essere numero >= 0
        if (quantitaField.getText() != null && !quantitaField.getText().trim().isEmpty()) {
            if (!quantitaField.getText().trim().matches("\\d+")) {
                errorMessage += "Quantità non valida (deve essere un numero)!\n";
            } else if (Integer.parseInt(quantitaField.getText().trim()) < 0) {
                errorMessage += "La quantità non può essere negativa!\n";
            }
        } else if (verifyLen) {
            // in modalità edit/new campo obbligatorio
            errorMessage += "Quantità non valida (campo obbligatorio)!\n";
        }

        // Giacenza minima: stessa logica (vuota in search ok; in edit obbligatorio)
        if (giacenzaMinField.getText() != null && !giacenzaMinField.getText().trim().isEmpty()) {
            if (!giacenzaMinField.getText().trim().matches("\\d+")) {
                errorMessage += "Giacenza minima non valida (deve essere un numero)!\n";
            } else if (Integer.parseInt(giacenzaMinField.getText().trim()) < 0) {
                errorMessage += "La giacenza minima non può essere negativa!\n";
            }
        } else if (verifyLen) {
            errorMessage += "Giacenza minima non valida (campo obbligatorio)!\n";
        }

        // Prezzi: se vuoti in search -> ok; altrimenti devono essere numerici positivi
        if (prezzoAcquistoField.getText() != null && !prezzoAcquistoField.getText().trim().isEmpty()) {
            if (!prezzoAcquistoField.getText().trim().matches("\\d+(\\.\\d+)?")) {
                errorMessage += "Prezzo acquisto non valido!\n";
            } else if (Double.parseDouble(prezzoAcquistoField.getText().trim()) <= 0) {
                errorMessage += "Il prezzo di acquisto deve essere maggiore di 0!\n";
            }
        } else if (verifyLen) {
            errorMessage += "Prezzo acquisto non valido (campo obbligatorio)!\n";
        }

        if (prezzoVenditaField.getText() != null && !prezzoVenditaField.getText().trim().isEmpty()) {
            if (!prezzoVenditaField.getText().trim().matches("\\d+(\\.\\d+)?")) {
                errorMessage += "Prezzo vendita non valido!\n";
            } else if (Double.parseDouble(prezzoVenditaField.getText().trim()) <= 0) {
                errorMessage += "Il prezzo di vendita deve essere maggiore di 0!\n";
            }
        } else if (verifyLen) {
            errorMessage += "Prezzo vendita non valido (campo obbligatorio)!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Errore nei campi");
            alert.setHeaderText("Correggi i campi non validi");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}