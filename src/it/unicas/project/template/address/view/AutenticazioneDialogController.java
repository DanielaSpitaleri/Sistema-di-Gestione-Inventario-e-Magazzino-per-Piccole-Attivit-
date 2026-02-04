package it.unicas.project.template.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Controller per il dialog di autenticazione che gestisce l'inserimento della password
 * e la logica di accesso alla finestra.
 * <br>
 * Questo controller legge la password inserita dall'utente, la confronta con la
 * costante interna e imposta lo stato di accesso chiudendo la finestra in caso
 * di successo o mostrando un messaggio di errore in caso contrario.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class AutenticazioneDialogController {

    /**
     * Costante che rappresenta la password attesa per l'accesso.
     * @see #handleInserimento()
     */
    private static final String PASSWORD = "123";

    /**
     * Campo di input per la password (mascherato).
     * @see #handleInserimento()
     */
    @FXML
    private PasswordField campoInserimento;

    /**
     * Label utilizzata per visualizzare messaggi di errore relativi all'autenticazione.
     * @see #handleInserimento()
     */
    @FXML
    private Label messaggiErrore;

    /**
     * Riferimento allo stage della finestra di dialogo.
     * <br>
     * Viene utilizzato per chiudere la finestra dopo un accesso riuscito.
     * @see #setDialogStage(Stage)
     */
    private Stage dialogStage;

    /**
     * Flag che indica se l'accesso è stato autorizzato.
     * {@code true} se la password inserita è corretta; {@code false} altrimenti.
     * @see #isAccesso()
     */
    private boolean accesso = false;

    /**
     * Imposta lo stage associato a questo dialog.
     * <br>
     * Post-condizione: il dialogStage interno viene valorizzato con il parametro fornito.
     *
     * @param dialogStage lo stage della finestra di dialogo da associare al controller
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Restituisce lo stato di accesso corrente.
     *
     * @return {@code true} se l'utente ha effettuato l'accesso con successo, {@code false} altrimenti
     */
    public boolean isAccesso() {
        return accesso;
    }

    /**
     * Gestore dell'evento di inserimento della password.
     * <br>
     * Confronta il valore inserito con la costante {@link #PASSWORD}. Se la password
     * è corretta, imposta {@link #accesso} a {@code true} e chiude il {@link #dialogStage}.
     * In caso contrario, mostra un messaggio di errore nella {@link #messaggiErrore}
     * e resetta il campo {@link #campoInserimento}.
     * <br>
     * Pre-condizione: {@link #campoInserimento} e {@link #messaggiErrore} devono essere inizializzati
     * tramite il meccanismo FXML; {@link #dialogStage} dovrebbe essere impostato tramite {@link #setDialogStage(Stage)}
     * per consentire la chiusura della finestra al successo.
     */
    @FXML
    private void handleInserimento() {
        String inputPassword = campoInserimento.getText();

        if (inputPassword != null && inputPassword.equals(PASSWORD)) {
            accesso = true;
            dialogStage.close();
        } else {
            messaggiErrore.setText("Password errata. Riprova.");
            campoInserimento.setText("");
        }
    }
}
