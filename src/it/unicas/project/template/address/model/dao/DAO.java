package it.unicas.project.template.address.model.dao;

import java.util.List;

/**
 * Interfaccia DAO generica che definisce le operazioni CRUD di base per entità di tipo {@code T}.
 * Implementazioni concrete incapsulano l'accesso alla sorgente di dati (es. database) per leggere,
 * inserire, aggiornare e cancellare istanze di {@code T}.
 *
 * @param <T> il tipo di entità gestita dal DAO
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public interface DAO <T>{

    /**
     * Recupera una lista di entità che corrispondono al prototipo fornito.
     * L'oggetto {@code a} viene usato come filtro/prototipo: campi non null possono essere interpretati
     * come criteri di ricerca. Il flag {@code c} controlla la modalità di ricerca (es. ricerca completa
     * vs. parziale o approfondita); il significato preciso dipende dall'implementazione concreta.
     *
     * @param a il prototipo o filtro usato per la selezione; non necessariamente completamente popolato
     * @param c flag che controlla la modalità di selezione (interpretazione dipende dall'implementazione)
     * @return una lista (eventualmente vuota) di istanze di {@code T} che soddisfano i criteri
     * @throws DAOException se si verifica un errore di accesso alla sorgente dati
     */
    List<T> select(T a, boolean c) throws DAOException;

    /**
     * Aggiorna lo stato persistente dell'entità fornita.
     * <br>
     * Pre-condizione: l'entità {@code a} deve contenere l'identificatore necessario per individuare il record
     * da aggiornare.
     * <br>
     * Post-condizione: lo stato persistente corrispondente viene modificato per riflettere i valori di {@code a}.
     *
     * @param a l'entità con i valori aggiornati; deve contenere l'identificatore per la persistenza
     * @throws DAOException se si verifica un errore durante l'operazione di aggiornamento
     */
    void update(T a) throws DAOException;

    /**
     * Inserisce una nuova entità nella sorgente dati
     * <br>
     * Pre-condizione: l'entità {@code a} deve avere i campi necessari per l'inserimento dei valori.
     * <br>
     * Post-condizione: se l'operazione ha successo, la nuova entità è persistita nella sorgente dati.
     *
     * @param a l'entità da inserire; i campi necessari per l'inserimento devono essere valorizzati
     * @return un intero che rappresenta il risultato dell'inserimento (es. chiave generata o numero di record inseriti);
     *         il significato esatto dipende dall'implementazione concreta
     * @throws DAOException se si verifica un errore durante l'inserimento
     */
    int insert(T a) throws DAOException;

    /**
     * Rimuove l'entità corrispondente dalla sorgente dati.
     * <br>
     * Pre-condizione: l'entità {@code a} deve identificare un record esistente (es. avere un id impostato).
     * <br>
     * Post-condizione: il record identificato viene cancellato dalla sorgente dati.
     *
     * @param a l'entità da cancellare; deve contenere le informazioni necessarie per identificare il record
     * @throws DAOException se si verifica un errore durante l'operazione di cancellazione
     */
    void delete(T a) throws DAOException;
}
