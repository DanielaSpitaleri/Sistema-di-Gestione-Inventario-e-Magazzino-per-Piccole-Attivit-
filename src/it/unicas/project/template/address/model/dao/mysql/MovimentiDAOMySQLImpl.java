package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Movimenti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementazione MySQL del DAO per l'entità Movimenti.
 * <br>
 * Fornisce operazioni di base (insert, delete, update, select) sulla tabella
 * "movimenti" del database e mappatura dei risultati su oggetti {@link Movimenti}.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class MovimentiDAOMySQLImpl implements DAO<Movimenti> {

    /**
     * Costruttore privato per applicare il pattern Singleton.
     */
    private MovimentiDAOMySQLImpl() {}

    /**
     * Istanza singleton del DAO.
     *
     * @see #getInstance()
     */
    private static DAO dao = null;

    /**
     * Logger per registrare query ed eventi relativi al DAO.
     */
    private static Logger logger = null;

    /**
     * Restituisce l'istanza singleton del DAO per Movimenti.
     *
     * @return l'istanza singleton di {@code DAO<Movimenti>}
     */
    public static DAO getInstance() {
        if (dao == null) {
            dao = new MovimentiDAOMySQLImpl();
            logger = Logger.getLogger(MovimentiDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    /**
     * Inserisce un nuovo movimento nella tabella "movimenti".
     * <br>
     * Pre-condizioni: {@code m} non deve essere {@code null} e deve essere valido
     * secondo {@link #verifyObject(Movimenti)}.
     * <br>
     * Post-condizioni: viene eseguita una INSERT nel database; l'effetto
     * è l'aggiunta di una riga nella tabella movimenti corrispondente ai valori
     * di {@code m}.
     *
     * @param m oggetto {@link Movimenti} da inserire (idMovimento ignorato)
     * @return valore intero di convenzione (attualmente 0); in implementazioni future può restituire l'id generato
     * @throws DAOException se {@code m} non è valido o si verifica un errore SQL durante l'insert
     */
    @Override
    public int insert(Movimenti m) throws DAOException {
        verifyObject(m);

        String query = "INSERT INTO movimenti (idMovimento, idProdotto, tipo, quantita, data, descrizione) VALUES ("
                + "NULL, "
                + m.getIdProdotto() + ", '"
                + m.getTipo() + "', "
                + m.getQuantita() + ", '"
                + m.getData() + "', '"
                + m.getDescrizione() + "')";

        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException e) {
            System.out.println("SQL: " + query);
        }

        executeUpdate(query);
        return 0;
    }

    /**
     * Elimina il movimento specificato dalla tabella "movimenti".
     * <br>
     * Non implementato.
     */
    @Override
    public void delete(Movimenti m) throws DAOException {

    }

    /**
     * Aggiorna il movimento esistente nel database con i valori forniti in {@code m}.
     * <br>
     * Non implementato.
     */
    @Override
    public void update(Movimenti m) throws DAOException {

    }

    /**
     * Esegue una query di selezione sulla tabella "movimenti" e restituisce la lista dei risultati.
     * <br>
     * Il parametro {@code m} può essere utilizzato come filtro (opzionale): se {@code null}
     * vengono restituiti tutti i movimenti. Il parametro {@code c} è attualmente non utilizzato
     * ma mantiene la firma coerente con l'interfaccia {@link DAO}.
     *
     * @param m filtro opzionale di tipo {@link Movimenti}; se {@code null} nessun filtro è applicato
     * @param c flag opzionale la cui semantica dipende dall'implementazione (non usato qui)
     * @return lista di oggetti {@link Movimenti} ordinata per data decrescente
     * @throws DAOException se si verifica un errore durante l'esecuzione della query SQL
     */
    @Override
    public List<Movimenti> select(Movimenti m, boolean c) throws DAOException {
        StringBuilder query = new StringBuilder("SELECT * FROM movimenti WHERE 1=1 ");

        query.append(" ORDER BY data DESC;");

        try{
            logger.info("SQL: " + query);
        } catch(NullPointerException nullPointerException) {
            System.out.println("SQL: " + query);
        }

        return executeQuery(query.toString());
    }

    /**
     * Verifica che l'oggetto {@link Movimenti} sia valido.
     * <br>
     * Controlla che:
     * <ul>
     *     <li>l'oggetto non sia {@code null};</li>
     *     <li>l'idProdotto sia positivo;</li>
     *     <li>il tipo di movimento non sia {@code null} o vuoto;</li>
     *     <li>la quantità non sia negativa (a meno che la descrizione sia "Carico iniziale").</li>
     * </ul>
     *
     * @param m oggetto {@link Movimenti} da verificare
     * @throws DAOException se l'oggetto non è valido
     */
    private void verifyObject(Movimenti m) throws DAOException {
        if (m == null) {
            throw new DAOException("Il movimento è null.");
        }

        if (m.getIdProdotto() <= 0) {
            throw new DAOException("idProdotto non valido.");
        }

        if (m.getTipo() == null || m.getTipo().isEmpty()) {
            throw new DAOException("Il tipo di movimento non può essere vuoto.");
        }

        if (m.getQuantita() < 0 || (m.getQuantita() == 0 && !"Carico iniziale".equalsIgnoreCase(m.getDescrizione()))) {
            throw new DAOException("La quantità non può essere negativa.");
        }
    }

    /**
     * Esegue una query di aggiornamento (INSERT, UPDATE, DELETE) sul database.
     *
     * @param query stringa SQL della query da eseguire
     * @throws DAOException se si verifica un errore SQL durante l'esecuzione
     */
    private void executeUpdate(String query) throws DAOException {
        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);
            DAOMySQLSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException("In executeUpdate(): " + e.getMessage());
        }
    }

    /**
     * Esegue una query di selezione sul database e mappa i risultati in una lista di oggetti {@link Movimenti}.
     *
     * @param query stringa SQL della query da eseguire
     * @return lista di oggetti {@link Movimenti} risultanti dalla query
     * @throws DAOException se si verifica un errore SQL durante l'esecuzione
     */
    private List<Movimenti> executeQuery(String query) throws DAOException {
        List<Movimenti> lista = new ArrayList<>();

        try {
            Statement st = DAOMySQLSettings.getStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                lista.add(new Movimenti(
                        rs.getInt("idMovimento"),
                        rs.getInt("idProdotto"),
                        rs.getString("tipo"),
                        rs.getInt("quantita"),
                        rs.getDate("data").toLocalDate(),
                        rs.getString("descrizione")));
            }
            DAOMySQLSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException("In executeQuery(): " + e.getMessage());
        }

        return lista;
    }
}