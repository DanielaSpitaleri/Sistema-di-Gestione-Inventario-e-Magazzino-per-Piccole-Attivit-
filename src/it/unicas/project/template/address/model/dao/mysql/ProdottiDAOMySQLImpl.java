package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Prodotti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementazione MySQL del DAO per l'entità Prodotti.
 * <br>
 * Fornisce operazioni CRUD (create, read, update, delete) utilizzando query SQL
 * costruite dinamicamente. Utilizza un singleton per l'istanza del DAO.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class ProdottiDAOMySQLImpl implements DAO<Prodotti> {

    /**
     * Costruttore privato per applicare il pattern Singleton.
     */
    private ProdottiDAOMySQLImpl() {}

    /**
     * Riferimento all'istanza singleton del DAO.
     *
     * @see ProdottiDAOMySQLImpl#getInstance()
     */
    private static DAO dao = null;

    /**
     * Logger per registrare le query e gli eventi rilevanti.
     *
     * @see java.util.logging.Logger
     */
    private static Logger logger = null;

    /**
     * Restituisce l'istanza singleton del DAO per Prodotti.
     *
     * @return l'istanza singleton di {@code DAO} per Prodotti
     */
    public static DAO getInstance() {
        if (dao == null) {
            dao = new ProdottiDAOMySQLImpl();
            logger = Logger.getLogger(ProdottiDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    /**
     * Esegue una query di selezione sulla tabella prodotti utilizzando i campi
     * dell'oggetto filtro fornito. Se il parametro {@code critico} è true,
     * vengono selezionati solo i prodotti la cui quantità è minore o uguale alla giacenza minima.
     *
     * @param a       oggetto {@code Prodotti} usato come filtro; se {@code null}
     *                viene usato un filtro vuoto che seleziona tutti i record
     * @param critico true per filtrare i prodotti con quantità critica (&lt;= giacenzaMin)
     * @return lista di oggetti {@code Prodotti} che soddisfano i criteri
     * @throws DAOException se si verifica un errore durante l'esecuzione della query SQL
     */
    @Override
    public List<Prodotti> select(Prodotti a, boolean critico) throws DAOException {
        if (a == null) {
            a = new Prodotti(null, "", "", -1, -1, 0.0, 0.0);
        }

        StringBuilder query = new StringBuilder("SELECT * FROM prodotti WHERE 1=1");

        if (a.getNome() != null && !a.getNome().trim().isEmpty()) {
            query.append(" AND nome LIKE '").append(a.getNome()).append("%'");
        }

        if (a.getDescrizione() != null && !a.getDescrizione().trim().isEmpty()) {
            query.append(" AND descrizione LIKE '").append(a.getDescrizione()).append("%'");
        }

        if (a.getQuantita() > -1) {
            query.append(" AND quantita = ").append(a.getQuantita());
        }

        if (critico) {
            query.append(" AND quantita <= giacenzaMin");
        }

        if (a.getGiacenzaMin() > -1) {
                query.append(" AND giacenzaMin = ").append(a.getGiacenzaMin());
        }

        if (a.getPrezzoAcquisto() > 0.0) {
            query.append(" AND prezzoAcquisto = ").append(a.getPrezzoAcquisto());
        }

        if (a.getPrezzoVendita() > 0.0) {
            query.append(" AND prezzoVendita = ").append(a.getPrezzoVendita());
        }

        try{
            logger.info("SQL: " + query);
        } catch(NullPointerException nullPointerException) {
            System.out.println("SQL: " + query);
        }

        return executeQuery(query.toString());
    }

    /**
     * Aggiorna un record esistente nella tabella prodotti corrispondente
     * all'ID presente nell'oggetto {@code a}. Prima di eseguire l'aggiornamento
     * viene verificata la validità dell'oggetto mediante {@link #verifyObject(Prodotti)}.
     * <br>
     * Pre-condizioni: {@code a} non deve essere {@code null} e deve avere
     * {@code idProdotto} valido.
     * <br>
     * Post-condizioni: il record nel database corrispondente a {@code idProdotto}
     * viene modificato per riflettere i valori di {@code a}.
     *
     * @param a oggetto {@code Prodotti} contenente i nuovi valori e l'id del prodotto da aggiornare
     * @throws DAOException se l'oggetto non è valido o si verifica un errore SQL
     */
    @Override
    public void update(Prodotti a) throws DAOException {
        verifyObject(a);

        String query = "UPDATE prodotti SET nome = '" + a.getNome() + "', descrizione = '" + a.getDescrizione()
                + "', quantita = '" + a.getQuantita()
                + "', giacenzaMin = '" + a.getGiacenzaMin()
                + "', prezzoAcquisto = '" + a.getPrezzoAcquisto()
                + "', prezzoVendita = '" + a.getPrezzoVendita()
                + "' WHERE idProdotto = " + a.getIdProdotto() + ";";

        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL: " + query);
        }

        int n = executeUpdate(query);
    }

    /**
     * Inserisce un nuovo prodotto nella tabella prodotti. Verifica la correttezza
     * dell'oggetto con {@link #verifyObject(Prodotti)} prima dell'inserimento.
     * <br>
     * Pre-condizioni: {@code a} non deve essere {@code null} e deve essere valido
     * secondo {@link #verifyObject(Prodotti)}.
     * <br>
     * Post-condizioni: viene creato un nuovo record nella tabella prodotti.
     *
     * @param a oggetto {@code Prodotti} da inserire (i campi devono essere validi)
     * @return l'id generato dal database per il nuovo prodotto (o -1 se non presente)
     * @throws DAOException se l'oggetto non è valido o si verifica un errore SQL
     */
    @Override
    public int insert(Prodotti a) throws DAOException {
        verifyObject(a);

        String query = "INSERT INTO prodotti (idProdotto, nome, descrizione, quantita, giacenzaMin, prezzoAcquisto, prezzoVendita) VALUES  (NULL, '"
                + a.getNome() + "', '"
                + a.getDescrizione() + "', "
                + a.getQuantita() + ", "
                + a.getGiacenzaMin() + ", "
                + a.getPrezzoAcquisto() + ", "
                + a.getPrezzoVendita() + ");";
        try {
            logger.info("SQL (Prodotti): " + query);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL (Prodotti): " + query);
        }

        return executeUpdate(query);
    }

    /**
     * Elimina il prodotto specificato (e le relative righe nella tabella movimenti)
     * identificato da {@code a.getIdProdotto()}.
     * <br>
     * Pre-condizioni: {@code a} non deve essere {@code null} e {@code a.getIdProdotto()}
     * non deve essere {@code null}.
     * <br>
     * Post-condizioni: le righe corrispondenti in {@code movimenti} e {@code prodotti}
     * vengono rimosse dal database.
     *
     * @param a oggetto {@code Prodotti} che identifica il prodotto da cancellare
     * @throws DAOException se {@code a} è {@code null} o se si verifica un errore SQL
     */
    @Override
    public void delete(Prodotti a) throws DAOException {
        if (a == null || a.getIdProdotto() == null) {
            throw new DAOException("In delete: il campo idProdotto non può essere null");
        }

        String queryMovimento = "DELETE FROM movimenti WHERE idProdotto = '" + a.getIdProdotto() + "';";
        String queryProdotto = "DELETE FROM prodotti WHERE idProdotto = '" + a.getIdProdotto() + "';";

        try {
            logger.info("SQL (Movimenti): " + queryMovimento);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL (Movimenti): " + queryMovimento);
        }

        int n = executeUpdate(queryMovimento);

        try {
            logger.info("SQL (Prodotto): " + queryProdotto);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL (Prodotto): " + queryProdotto);
        }

        int m = executeUpdate(queryProdotto);
    }

    /**
     * Verifica la validità semantica dei campi numerici e dei campi obbligatori
     * dell'oggetto {@code Prodotti}.
     * <ul>
     *     <li>I campi numerici (quantità, giacenza minima, prezzo di acquisto, prezzo di vendita) non possono essere negativi.</li>
     *     <li>I campi nome e descrizione non possono essere {@code null}.</li>
     * </ul>
     *
     * @param a oggetto {@code Prodotti} da verificare
     * @throws DAOException se uno dei campi numerici è negativo o se nome/descrizione sono null
     */
    private void verifyObject(Prodotti a) throws DAOException {
        if (a.getQuantita() < 0 || a.getGiacenzaMin() < 0 || a.getPrezzoAcquisto() < 0 || a.getPrezzoVendita() < 0) {
            throw new DAOException("I campi numerici (quantità, giacenza e prezzi) non possono essere negativi.");
        }

        if (a == null || a.getNome() == null || a.getDescrizione() == null) {
            throw new DAOException("I campi Nome e Descrizione del Prodotto non possono essere null.");
        }
    }

    /**
     * Esegue una query di aggiornamento/insert/delete e restituisce l'ID generato
     * dal database (se disponibile).
     *
     * @param query stringa SQL da eseguire
     * @return id generato dal database o -1 se non disponibile
     * @throws DAOException se si verifica un errore durante l'esecuzione SQL
     */
    private int executeUpdate(String query) throws DAOException {
        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            int generatedId = -1;
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
            DAOMySQLSettings.closeStatement(st);

            return generatedId;
        } catch (SQLException e) {
            throw new DAOException("In executeUpdateID(): " + e.getMessage());
        }
    }

    /**
     * Esegue una query di selezione e mappa i risultati in una lista di {@code Prodotti}.
     *
     * @param query stringa SQL di selezione da eseguire
     * @return lista di oggetti {@code Prodotti} risultanti dalla query
     * @throws DAOException se si verifica un errore durante l'esecuzione SQL
     */
    private List<Prodotti> executeQuery(String query) throws DAOException {
        List<Prodotti> lista = new ArrayList<>();

        try {
            Statement st = DAOMySQLSettings.getStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                lista.add(new Prodotti(
                        rs.getInt("idProdotto"),
                        rs.getString("nome"),
                        rs.getString("descrizione"),
                        rs.getInt("quantita"),
                        rs.getInt("giacenzaMin"),
                        rs.getDouble("prezzoAcquisto"),
                        rs.getDouble("prezzoVendita")));
            }
            DAOMySQLSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException("In executeQuery(): " + e.getMessage());
        }

        return lista;
    }
}