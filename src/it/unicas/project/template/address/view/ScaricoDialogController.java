package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.Movimenti;
import it.unicas.project.template.address.model.Prodotti;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;

/**
 * Controller per la finestra di dialogo che gestisce i movimenti di carico/scarico di un prodotto.
 * <br>
 * Fornisce i collegamenti ai controlli FXML e i metodi per popolare e validare i dati dell'interfaccia.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class ScaricoDialogController {

    /**
     * Label che mostra il nome del prodotto selezionato.
     * @see #setProdotto(Prodotti)
     */
    @FXML
    private Label nomeProdottoLabel;

    /**
     * Label che mostra la quantità attuale disponibile del prodotto.
     * @see #updateQuantitaProdotto()
     */
    @FXML
    private Label quantitaAttualeLabel;

    /**
     * Area di testo per la descrizione del movimento.
     */
    @FXML
    private TextArea descrizioneTextArea;

    /**
     * Campo di testo per inserire la quantità da caricare/scaricare.
     */
    @FXML
    private TextField quantitaTextField;

    /**
     * DatePicker per selezionare la data del movimento.
     */
    @FXML
    private DatePicker datePicker;

    /**
     * ChoiceBox per selezionare il tipo di movimento (Carico/Scarico).
     */
    @FXML
    private ChoiceBox<String> movimentoChoiceBox;

    /**
     * Lo stage di dialogo associato a questo controller, usato per chiudere la finestra.
     * @see #handleOk()
     * @see #handleCancel()
     */
    private Stage dialogStage;

    /**
     * Il prodotto corrente su cui si opera.
     * <br>
     * Questo campo definisce lo stato della dialog e viene impostato da {@link #setProdotto(Prodotti)}.
     */
    private Prodotti prodotto;

    /**
     * L'oggetto movimento che verrà popolato con i dati inseriti nella dialog.
     */
    private Movimenti movimento;

    /**
     * Flag che indica se l'utente ha confermato l'operazione con OK.
     */
    private boolean okClicked = false;

    /**
     * Flag che indica se la verifica della lunghezza è attiva (usato dalla dialog dei prodotti).
     */
    private boolean verifyLen = true;

    /**
     * Inizializza la classe controller. Viene chiamato automaticamente
     * dopo che il file FXML è stato caricato. Imposta valori di default per i controlli.
     */
    @FXML
    private void initialize() {
        movimentoChoiceBox.setValue("SCARICO");
        datePicker.setValue(LocalDate.now());
    }

    /**
     * Imposta lo stage di questa finestra di dialogo.
     * Serve per poter chiudere la finestra.
     *
     * @param dialogStage lo Stage che rappresenta la finestra di dialogo
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Imposta il prodotto da modificare nella dialog.
     * Questo metodo popola anche i campi della UI con i dati del prodotto.
     * Post-condizione: {@link #nomeProdottoLabel} e {@link #quantitaAttualeLabel} sono aggiornati.
     *
     * @param prodotto Il prodotto selezionato nella tabella principale; non deve essere null.
     */
    public void setProdotto(Prodotti prodotto) {
        this.prodotto = prodotto;

        nomeProdottoLabel.setText(prodotto.getNome());
        quantitaAttualeLabel.setText(String.valueOf(prodotto.getQuantita()));

        quantitaTextField.setText("");
        descrizioneTextArea.setText("");
    }

    /**
     * Imposta l'oggetto movimento che verrà popolato quando l'utente conferma.
     *
     * @param movimento l'istanza di {@link Movimenti} da popolare; non deve essere null
     */
    public void setMovimento(Movimenti movimento) {
        this.movimento = movimento;
    }

    /**
     * Restituisce true se l'utente ha cliccato OK, false altrimenti.
     *
     * @return true se OK è stato premuto e l'operazione è stata confermata
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Chiamato quando l'utente clicca OK.
     * Se i dati sono validi, popola l'oggetto {@link #movimento} e aggiorna la quantità del prodotto.
     * <br>
     * Post-condizione: se ritorna con successo {@link #okClicked} è true e {@link #dialogStage} è chiuso.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            movimento.setIdProdotto(prodotto.getIdProdotto());
            movimento.setTipo(movimentoChoiceBox.getValue());
            movimento.setQuantita(Integer.parseInt(quantitaTextField.getText()));
            movimento.setData(datePicker.getValue());
            movimento.setDescrizione(descrizioneTextArea.getText());

            updateQuantitaProdotto();

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Chiamato quando l'utente clicca Cancel. Chiude semplicemente la dialog senza salvare.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    /**
     * Aggiorna la quantità del prodotto in base al tipo di movimento selezionato.
     * <br>
     * Legge la quantità dal campo di testo e modifica lo stato di {@link #prodotto}.
     */
    private void updateQuantitaProdotto() {
        int quantitaInput = Integer.parseInt(quantitaTextField.getText());

        if ("Carico".equals(movimentoChoiceBox.getValue())) {
            prodotto.setQuantita(prodotto.getQuantita() + quantitaInput);
        } else if ("Scarico".equals(movimentoChoiceBox.getValue())) {
            prodotto.setQuantita(prodotto.getQuantita() - quantitaInput);
        }
    }

    /**
     * Valida l'input dell'utente nei campi della dialog.
     *
     * @return true se l'input è valido; false altrimenti (in tal caso mostra un Alert con gli errori)
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (quantitaTextField.getText() == null || !quantitaTextField.getText().matches("\\d+")) {
            errorMessage += "Quantità non valida!\n";
        } else {
            int quantitaInput = Integer.parseInt(quantitaTextField.getText());

            if (quantitaInput <= 0) {
                errorMessage += "La quantità deve essere maggiore di 0.\n";
            } else if ("SCARICO".equals(movimentoChoiceBox.getValue()) && quantitaInput > prodotto.getQuantita()) {
                        errorMessage += "Non puoi scaricare più merce di quella disponibile (Attuale: " + prodotto.getQuantita() + ").\n";
            }
        }

        if (datePicker.getValue() == null) {
            errorMessage += "Data non valida!\n";
        }

        if (datePicker.getValue().isAfter(LocalDate.now())) {
            errorMessage += "La data non può essere nel futuro!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Campi non validi");
            alert.setHeaderText("Correggi i campi errati");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
}