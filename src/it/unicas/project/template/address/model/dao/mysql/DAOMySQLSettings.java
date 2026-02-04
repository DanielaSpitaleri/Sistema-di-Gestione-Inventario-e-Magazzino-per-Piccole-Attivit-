package it.unicas.project.template.address.model.dao.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility per la configurazione e la gestione delle impostazioni di
 * connessione MySQL usate dai DAO dell'applicazione.
 * <br>
 * Fornisce costanti di default, accesso al singleton {@code DAOMySQLSettings}
 * corrente e metodi helper per creare e chiudere {@link Statement}.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class DAOMySQLSettings {

    /**
     * Nome del driver JDBC MySQL.
     *
     * @see #getStatement()
     */
    public final static String DRIVERNAME = "com.mysql.cj.jdbc.Driver";

    /**
     * Host di default del database.
     *
     * @see #getDefaultDAOSettings()
     */
    public final static String HOST = "localhost";

    /**
     * Nome utente prelevato dalle variabili d'ambiente.
     *
     * @see #getDefaultDAOSettings()
     */
    public final static String USERNAME = System.getenv("DB_USER");

    /**
     * Password prelevata dalle variabili d'ambiente.
     *
     * @see #getDefaultDAOSettings()
     */
    public final static String PWD = System.getenv("DB_PASSWORD");

    /**
     * Schema (database) di default utilizzato dall'applicazione.
     *
     * @see #getDefaultDAOSettings()
     */
    public final static String SCHEMA = System.getenv("DB_SCHEMA");//"magazzino";

    /**
     * Parametri di connessione JDBC da appendere all'URL.
     *
     * @see #getStatement()
     */
    public final static String PARAMETERS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /**
     * Host corrente da usare per la connessione (istanza).
     *
     * @see #getHost()
     */
    private String host = "localhost";

    /**
     * Nome utente corrente da usare per la connessione (istanza).
     *
     * @see #getUserName()
     */
    private String userName = System.getenv("DB_USER");

    /**
     * Password corrente da usare per la connessione (istanza).
     *
     * @see #getPwd()
     */
    private String pwd = System.getenv("DB_PASSWORD");

    /**
     * Schema corrente (database) da usare per la connessione (istanza).
     *
     * @see #getSchema()
     */
    private String schema = System.getenv("DB_SCHEMA");//"magazzino";

    /**
     * Restituisce l'host configurato per questa istanza.
     *
     * @return la stringa dell'host (es. "localhost")
     */
    public String getHost() {
        return host;
    }

    /**
     * Restituisce il nome utente configurato per questa istanza.
     *
     * @return il nome utente per la connessione al DB
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Restituisce la password configurata per questa istanza.
     *
     * @return la password per la connessione al DB
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Restituisce lo schema (database) configurato per questa istanza.
     *
     * @return il nome dello schema/database
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Imposta l'host per questa istanza di configurazione.
     *
     * @param host nuova stringa host da usare (non valida se null)
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Imposta il nome utente per questa istanza di configurazione.
     *
     * @param userName nuovo nome utente da usare (non valido se null)
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Imposta la password per questa istanza di configurazione.
     *
     * @param pwd nuova password da usare (non valida se null)
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Imposta lo schema (database) per questa istanza di configurazione.
     *
     * @param schema nuovo schema/database da utilizzare
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    static{
        try {
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Istanza singleton corrente delle impostazioni DAO.
     *
     * @see #getCurrentDAOMySQLSettings()
     */
    private static DAOMySQLSettings currentDAOMySQLSettings = null;

    /**
     * Restituisce l'istanza corrente di {@code DAOMySQLSettings}.
     * <br>
     * Se non è stata ancora inizializzata, viene creata usando i valori di default.
     *
     * @return l'istanza singleton corrente di {@code DAOMySQLSettings}
     */
    public static DAOMySQLSettings getCurrentDAOMySQLSettings(){
        if (currentDAOMySQLSettings == null){
            currentDAOMySQLSettings = getDefaultDAOSettings();
        }
        return currentDAOMySQLSettings;
    }

    /**
     * Crea e restituisce una nuova istanza di {@code DAOMySQLSettings} popolata con i valori di default
     * (costanti statiche {@link #HOST}, {@link #USERNAME}, {@link #PWD} e {@link #SCHEMA}).
     *
     * @return una nuova istanza configurata con i valori di default
     */
    public static DAOMySQLSettings getDefaultDAOSettings(){
        DAOMySQLSettings daoMySQLSettings = new DAOMySQLSettings();
        daoMySQLSettings.host = HOST;
        daoMySQLSettings.userName = USERNAME;
        daoMySQLSettings.schema = SCHEMA;
        daoMySQLSettings.pwd = PWD;
        return daoMySQLSettings;
    }

    /**
     * Sovrascrive l'istanza singleton corrente con quella fornita.
     * <br>
     * Pre-condizione: il parametro non deve essere {@code null} se si desidera una
     * configurazione valida; passare {@code null} ripristina il comportamento lazy (creazione da default).
     *
     * @param daoMySQLSettings nuova istanza da usare come valore corrente (può essere {@code null})
     */
    public static void setCurrentDAOMySQLSettings(DAOMySQLSettings daoMySQLSettings){
        currentDAOMySQLSettings = daoMySQLSettings;
    }

    /**
     * Crea e restituisce un {@link Statement} utilizzando la configurazione corrente.
     * <br>
     * Se non esiste un'istanza corrente, viene creata con i valori di default.
     *
     * @return un oggetto {@link Statement} pronto per eseguire query sul database
     * @throws SQLException se non è possibile aprire la connessione o creare lo statement
     * @see #getCurrentDAOMySQLSettings()
     */
    public static Statement getStatement() throws SQLException{
        if (currentDAOMySQLSettings == null){
            currentDAOMySQLSettings = getDefaultDAOSettings();
        }
        return DriverManager.getConnection("jdbc:mysql://" + currentDAOMySQLSettings.host  + "/" + currentDAOMySQLSettings.schema + PARAMETERS, currentDAOMySQLSettings.userName, currentDAOMySQLSettings.pwd).createStatement();
    }

    /**
     * Chiude lo {@link Statement} fornito e la connessione sottostante.
     * <br>
     * Pre-condizioni: {@code st} non deve essere {@code null} e deve essere aperto.
     *
     * @param st lo {@link Statement} da chiudere; la connessione associata verrà chiusa anch'essa
     * @throws SQLException se si verifica un errore durante la chiusura della risorsa
     */
    public static void closeStatement(Statement st) throws SQLException{
        st.getConnection().close();
        st.close();
    }
}
