package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Rappresenta un prodotto gestito dall'applicazione, esponendo le sue proprietà
 * come JavaFX Property per supportare il binding con la UI.
 *
 * Questa classe contiene informazioni identificative, descrittive e quantitative
 * (giacenza, soglia minima) oltre ai prezzi di acquisto e vendita.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class Prodotti {

    /**
     * Identificatore unico del prodotto.
     *
     * @see #getIdProdotto()
     * @see #setIdProdotto(int)
     */
    private IntegerProperty idProdotto;

    /**
     * Nome del prodotto.
     *
     * @see #getNome()
     * @see #setNome(String)
     */
    private StringProperty nome;

    /**
     * Descrizione testuale del prodotto.
     *
     * @see #getDescrizione()
     * @see #setDescrizione(String)
     */
    private StringProperty descrizione;

    /**
     * Quantità corrente (giacenza) del prodotto.
     *
     * @see #getQuantita()
     * @see #setQuantita(int)
     */
    private IntegerProperty quantita;

    /**
     * Soglia minima di giacenza considerata accettabile.
     *
     * @see #getGiacenzaMin()
     * @see #setGiacenzaMin(int)
     */
    private IntegerProperty giacenzaMin;

    /**
     * Prezzo di acquisto del prodotto.
     *
     * @see #getPrezzoAcquisto()
     * @see #setPrezzoAcquisto(double)
     */
    private DoubleProperty prezzoAcquisto;

    /**
     * Prezzo di vendita del prodotto.
     *
     * @see #getPrezzoVendita()
     * @see #setPrezzoVendita(double)
     */
    private DoubleProperty prezzoVendita;

    /**
     * Costruttore di default che inizializza il prodotto con valori di fallback.
     *
     * Post-condizione: tutte le proprietà sono inizializzate con valori di default
     * (id = -1, stringhe vuote, numeri a 0).
     */
    public Prodotti() {

        this(-1, "", "", 0, 0, 0.0, 0.0);
    }

    /**
     * Costruisce un prodotto con i valori forniti.
     *
     * @param idProdotto identificatore del prodotto; se null viene usato -1
     * @param nome nome del prodotto
     * @param descrizione descrizione del prodotto
     * @param quantita quantità corrente (giacenza)
     * @param giacenzaMin soglia minima di giacenza
     * @param prezzoAcquisto prezzo di acquisto
     * @param prezzoVendita prezzo di vendita
     *
     * Post-condizione: tutte le proprietà interne saranno impostate sui valori forniti.
     */
    public Prodotti(Integer idProdotto, String nome, String descrizione, int quantita, int giacenzaMin, double prezzoAcquisto, double prezzoVendita) {

        int safeId = (idProdotto != null) ? idProdotto : -1;

        this.idProdotto = new SimpleIntegerProperty(safeId);
        this.nome = new SimpleStringProperty(nome);
        this.descrizione = new SimpleStringProperty(descrizione);
        this.quantita = new SimpleIntegerProperty(quantita);
        this.giacenzaMin = new SimpleIntegerProperty(giacenzaMin);
        this.prezzoAcquisto = new SimpleDoubleProperty(prezzoAcquisto);
        this.prezzoVendita = new SimpleDoubleProperty(prezzoVendita);
    }

    /**
     * Restituisce l'identificatore del prodotto.
     *
     * @return l'id del prodotto; valore intero (può essere -1 se non assegnato)
     */
    public Integer getIdProdotto() {
        return idProdotto.get();
    }

    /**
     * Imposta l'identificatore del prodotto.
     *
     * @param idProdotto nuovo identificatore da assegnare
     *
     * Post-condizione: la property {@code idProdotto} contiene il nuovo valore.
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto.set(idProdotto);
    }

    /**
     * Fornisce la property JavaFX per l'id del prodotto, utile per il binding.
     *
     * @return la property IntegerProperty che rappresenta l'id del prodotto
     */
    public IntegerProperty idProdottoProperty() {
        return idProdotto;
    }

    /**
     * Restituisce il nome del prodotto.
     *
     * @return nome del prodotto come stringa
     */
    public String getNome() {
        return nome.get();
    }

    /**
     * Imposta il nome del prodotto.
     *
     * @param nome nuovo nome del prodotto
     *
     * Post-condizione: la property {@code nome} contiene il nuovo valore.
     */
    public void setNome(String nome) {
        this.nome.set(nome);
    }

    /**
     * Fornisce la property JavaFX per il nome, utile per il binding.
     *
     * @return la property StringProperty che rappresenta il nome
     */
    public StringProperty nomeProperty() {
        return nome;
    }

    /**
     * Restituisce la descrizione del prodotto.
     *
     * @return descrizione come stringa
     */
    public String getDescrizione() {
        return descrizione.get();
    }

    /**
     * Imposta la descrizione del prodotto.
     *
     * @param descrizione nuova descrizione testuale
     *
     * Post-condizione: la property {@code descrizione} contiene il nuovo valore.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione.set(descrizione);
    }

    /**
     * Fornisce la property JavaFX per la descrizione.
     *
     * @return la property StringProperty che rappresenta la descrizione
     */
    public StringProperty descrizioneProperty() {
        return descrizione;
    }

    /**
     * Restituisce la quantità corrente (giacenza) del prodotto.
     *
     * @return quantità corrente come intero
     */
    public int getQuantita() {
        return quantita.get();
    }

    /**
     * Imposta la quantità corrente (giacenza) del prodotto.
     *
     * @param quantita nuovo valore della giacenza
     *
     * Post-condizione: la property {@code quantita} contiene il nuovo valore.
     */
    public void setQuantita(int quantita) {
        this.quantita.set(quantita);
    }

    /**
     * Fornisce la property JavaFX per la quantità.
     *
     * @return la property IntegerProperty che rappresenta la quantità
     */
    public IntegerProperty quantitaProperty() {
        return quantita;
    }

    /**
     * Restituisce la soglia minima di giacenza per il prodotto.
     *
     * @return soglia minima come intero
     */
    public int getGiacenzaMin() {
        return giacenzaMin.get();
    }

    /**
     * Imposta la soglia minima di giacenza per il prodotto.
     *
     * @param giacenzaMin nuovo valore della soglia minima
     *
     * Post-condizione: la property {@code giacenzaMin} contiene il nuovo valore.
     */
    public void setGiacenzaMin(int giacenzaMin) {
        this.giacenzaMin.set(giacenzaMin);
    }

    /**
     * Fornisce la property JavaFX per la soglia minima di giacenza.
     *
     * @return la property IntegerProperty che rappresenta la soglia minima
     */
    public IntegerProperty giacenzaMinProperty() {
        return giacenzaMin;
    }

    /**
     * Restituisce il prezzo di acquisto del prodotto.
     *
     * @return prezzo di acquisto come double
     */
    public double getPrezzoAcquisto() {
        return prezzoAcquisto.get();
    }

    /**
     * Imposta il prezzo di acquisto del prodotto.
     *
     * @param prezzoAcquisto nuovo prezzo di acquisto
     *
     * Post-condizione: la property {@code prezzoAcquisto} contiene il nuovo valore.
     */
    public void setPrezzoAcquisto(double prezzoAcquisto) {
        this.prezzoAcquisto.set(prezzoAcquisto);
    }

    /**
     * Fornisce la property JavaFX per il prezzo di acquisto.
     *
     * @return la property DoubleProperty che rappresenta il prezzo di acquisto
     */
    public DoubleProperty prezzoAcquistoProperty() {
        return prezzoAcquisto;
    }

    /**
     * Restituisce il prezzo di vendita del prodotto.
     *
     * @return prezzo di vendita come double
     */
    public double getPrezzoVendita() {
        return prezzoVendita.get();
    }

    /**
     * Imposta il prezzo di vendita del prodotto.
     *
     * @param prezzoVendita nuovo prezzo di vendita
     *
     * Post-condizione: la property {@code prezzoVendita} contiene il nuovo valore.
     */
    public void setPrezzoVendita(double prezzoVendita) {
        this.prezzoVendita.set(prezzoVendita);
    }

    /**
     * Fornisce la property JavaFX per il prezzo di vendita.
     *
     * @return la property DoubleProperty che rappresenta il prezzo di vendita
     */
    public DoubleProperty prezzoVenditaProperty() {
        return prezzoVendita;
    }

    /**
     * Verifica se il prodotto ha raggiunto o è al di sotto della giacenza minima.
     *
     * @return true se la quantità corrente è minore o uguale alla giacenza minima, false altrimenti
     *
     * Effetto esterno: nessuno (metodo di sola lettura dello stato).
     */
    public boolean vincoloQuantita() {
        return getQuantita() <= getGiacenzaMin();
    }

    /**
     * Restituisce una rappresentazione testuale del prodotto contenente le principali proprietà.
     *
     * @return stringa che rappresenta il prodotto con id, nome, descrizione, quantità, giacenza minima e prezzi
     */
    @Override
    public String toString() {
        return "Prodotto{" +
                "idProdotto=" + idProdotto.getValue() +
                ", nome=" + nome.getValue() +
                ", descrizione=" + descrizione.getValue() +
                ", quantita=" + quantita.getValue() +
                ", giacenzaMinima=" + giacenzaMin.getValue() +
                ", prezzoAcquisto=" + prezzoAcquisto.getValue() +
                ", prezzoVendita=" + prezzoVendita.getValue() +
                '}';
    }
}