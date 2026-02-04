package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.dao.mysql.DAOMySQLSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Controller per il dialog che permette di modificare le impostazioni di connessione
 * al database MySQL (host, username, password, schema).
 * <br>
 * Questa classe popola i campi della UI con i valori correnti delle impostazioni
 * e valida l'input dell'utente prima di applicare le modifiche.
 *
 *  @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 *  @since 19/11/2025
 */
public class SettingsEditDialogController {

    /**
     * Campo TextField per il nome del driver JDBC.
     * @see it.unicas.project.template.address.model.dao.mysql.DAOMySQLSettings#DRIVERNAME
     */
    @FXML
    private TextField driverNameField;

    /**
     * Campo TextField per l'host del database (es. hostname:port).
     * @see #setSettings(DAOMySQLSettings)
     */
    @FXML
    private TextField hostField;

    /**
     * Campo TextField per il nome utente del database.
     * @see #setSettings(DAOMySQLSettings)
     */
    @FXML
    private TextField usernameField;

    /**
     * Campo TextField per la password dell'utente del database.
     * @see #setSettings(DAOMySQLSettings)
     */
    @FXML
    private TextField passwordField;

    /**
     * Campo TextField per il nome dello schema/database.
     * @see #setSettings(DAOMySQLSettings)
     */
    @FXML
    private TextField schemaField;


    /**
     * Stage del dialog corrente, utilizzato come owner per eventuali Alert e per chiudere la finestra.
     * @see #setDialogStage(Stage)
     */
    private Stage dialogStage;

    /**
     * Istanza delle impostazioni che viene modificata dal dialog.
     * @see #setSettings(DAOMySQLSettings)
     */
    private DAOMySQLSettings settings;

    /**
     * Flag che indica se l'utente ha confermato le modifiche premendo OK.
     */
    private boolean okClicked = false;

    /**
     * Inizializza il controller e imposta i valori di default dei campi.
     * Viene chiamato automaticamente dopo che il file FXML è stato caricato.
     */
    @FXML
    private void initialize() {
        driverNameField.setText(DAOMySQLSettings.DRIVERNAME);
        hostField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        schemaField.setText("");
    }

    /**
     * Imposta lo Stage di questo dialog.
     * Viene usato per associare icone e per centrare/posizionare eventuali Alert.
     *
     * @param dialogStage lo {@link Stage} del dialog
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        // Set the dialog icon.
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    /**
     * Popola i campi del dialog con i valori presi dall'oggetto settings fornito.
     *
     * @param settings l'istanza di {@link DAOMySQLSettings} da editare; non null
     */
    public void setSettings(DAOMySQLSettings settings) {
        this.settings = settings;
        hostField.setText(settings.getHost());
        usernameField.setText(settings.getUserName());
        passwordField.setText(settings.getPwd());
        schemaField.setText(settings.getSchema());
    }

    /**
     * Restituisce true se l'utente ha confermato le modifiche con OK.
     *
     * @return true se OK è stato premuto e le modifiche sono state applicate in memoria
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Gestore chiamato quando l'utente clicca OK.
     * Se l'input è valido, aggiorna l'oggetto {@link #settings} con i valori inseriti
     * e chiude il dialog.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            settings.setHost(hostField.getText());
            settings.setUserName(usernameField.getText());
            settings.setPwd(passwordField.getText());
            settings.setSchema(schemaField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Gestore chiamato quando l'utente clicca Cancel.
     * Chiude il dialog senza applicare alcuna modifica.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Valida l'input inserito dall'utente nei campi del dialog.
     * Mostra un Alert in caso di errori.
     *
     * @return true se tutti i campi necessari sono validi; false altrimenti
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (hostField.getText() == null || hostField.getText().length() == 0) {
            errorMessage += "No valid hostname!\n";
        }
        if (usernameField.getText() == null || usernameField.getText().length() == 0) {
            errorMessage += "No valid username!\n";
        }

        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "No valid password!\n";
        }
        if (schemaField.getText() == null || schemaField.getText().length() == 0){
            errorMessage += "No valid schema!\n";
        }



        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
