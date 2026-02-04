package it.unicas.project.template.address.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Rappresenta un movimento di magazzino (carico o scarico).
 * Questa classe incapsula lo stato di un singolo movimento, inclusi
 * identificatore, prodotto associato, tipo (ad esempio "CARICO"/"SCARICO"),
 * quantità, data del movimento e una descrizione opzionale.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class Movimenti {

    /**
     * Identificatore del movimento.
     *
     * @see #getIdMovimento()
     * @see #setIdMovimento(int)
     */
    private IntegerProperty idMovimento;

    /**
     * Identificatore del prodotto associato a questo movimento.
     *
     * @see #getIdProdotto()
     * @see #setIdProdotto(int)
     */
    private IntegerProperty idProdotto;

    /**
     * Tipo di movimento (es. "CARICO" o "SCARICO").
     *
     * @see #getTipo()
     * @see #setTipo(String)
     */
    private StringProperty tipo;

    /**
     * Quantità movimentata.
     *
     * @see #getQuantita()
     * @see #setQuantita(int)
     */
    private IntegerProperty quantita;

    /**
     * Data associata al movimento.
     *
     * @see #getData()
     * @see #setData(LocalDate)
     */
    private ObjectProperty<LocalDate> data;

    /**
     * Descrizione testuale del movimento.
     *
     * @see #getDescrizione()
     * @see #setDescrizione(String)
     */
    private StringProperty descrizione;

    /**
     * Costruttore di default.
     * Inizializza i campi con valori predefiniti:
     * idMovimento = -1, idProdotto = -1, tipo e descrizione vuote,
     * quantita = 0, data = LocalDate.now().
     * Postcondizione: l'istanza è valida e pronta per essere popolata.
     */
    public Movimenti() {

        this(-1, -1, "", 0, LocalDate.now(), "");
    }

    /**
     * Costruttore con parametri.
     *
     * @param idMovimento identificatore del movimento; se {@code null} viene usato -1.
     * @param idProdotto  identificatore del prodotto associato al movimento.
     * @param tipo        tipo di movimento (es. "CARICO" o "SCARICO").
     * @param quantita    quantità movimentata.
     * @param data        data del movimento.
     * @param descrizione descrizione testuale del movimento.
     * Postcondizione: tutti i campi sono inizializzati con i valori forniti.
     */
    public Movimenti(Integer idMovimento, Integer idProdotto, String tipo, int quantita,
                     LocalDate data, String descrizione) {

        int safeId = (idMovimento != null) ? idMovimento : -1;

        this.idMovimento = new SimpleIntegerProperty(safeId);
        this.idProdotto = new SimpleIntegerProperty(idProdotto);
        this.tipo = new SimpleStringProperty(tipo);
        this.quantita = new SimpleIntegerProperty(quantita);
        this.data = new SimpleObjectProperty<>(data);
        this.descrizione = new SimpleStringProperty(descrizione);
    }

    /**
     * Restituisce l'identificatore del movimento.
     *
     * @return l'id del movimento come int.
     */
    public int getIdMovimento() {
        return idMovimento.get();
    }

    /**
     * Imposta l'identificatore del movimento.
     *
     * @param idMovimento nuovo identificatore del movimento.
     * Postcondizione: {@link #getIdMovimento()} restituirà il valore impostato.
     */
    public void setIdMovimento(int idMovimento) {
        this.idMovimento.set(idMovimento);
    }

    /**
     * Fornisce la property JavaFX per l'id del movimento.
     *
     * @return {@link IntegerProperty} che rappresenta l'id del movimento.
     */
    public IntegerProperty idMovimentoProperty() {
        return idMovimento;
    }

    /**
     * Restituisce l'identificatore del prodotto associato.
     *
     * @return l'id del prodotto come int.
     */
    public int getIdProdotto() {
        return idProdotto.get();
    }

    /**
     * Imposta l'identificatore del prodotto associato.
     *
     * @param idProdotto nuovo identificatore del prodotto.
     * Postcondizione: {@link #getIdProdotto()} restituirà il valore impostato.
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto.set(idProdotto);
    }

    /**
     * Fornisce la property JavaFX per l'id del prodotto.
     *
     * @return {@link IntegerProperty} che rappresenta l'id del prodotto.
     */
    public IntegerProperty idProdottoProperty() {
        return idProdotto;
    }

    /**
     * Restituisce il tipo di movimento.
     *
     * @return il tipo di movimento come {@link String}.
     */
    public String getTipo() {
        return tipo.get();
    }

    /**
     * Imposta il tipo di movimento.
     *
     * @param tipo nuovo tipo di movimento (es. "CARICO", "SCARICO").
     * Postcondizione: {@link #getTipo()} restituirà la stringa impostata.
     */
    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    /**
     * Fornisce la property JavaFX per il tipo di movimento.
     *
     * @return {@link StringProperty} che rappresenta il tipo di movimento.
     */
    public StringProperty tipoProperty() {
        return tipo;
    }

    /**
     * Restituisce la quantità movimentata.
     *
     * @return la quantità come int.
     */
    public int getQuantita() {
        return quantita.get();
    }

    /**
     * Imposta la quantità movimentata.
     *
     * @param quantita nuova quantità (valore intero).
     * Postcondizione: {@link #getQuantita()} restituirà il valore impostato.
     */
    public void setQuantita(int quantita) {
        this.quantita.set(quantita);
    }

    /**
     * Fornisce la property JavaFX per la quantità.
     *
     * @return {@link IntegerProperty} che rappresenta la quantità.
     */
    public IntegerProperty quantitaProperty() {
        return quantita;
    }

    /**
     * Restituisce la data del movimento.
     *
     * @return la data come {@link LocalDate}.
     */
    public LocalDate getData() {
        return data.get();
    }

    /**
     * Imposta la data del movimento.
     *
     * @param data nuova data del movimento.
     * Postcondizione: {@link #getData()} restituirà la data impostata.
     */
    public void setData(LocalDate data) {
        this.data.set(data);
    }

    /**
     * Fornisce la property JavaFX per la data del movimento.
     *
     * @return {@link ObjectProperty} di tipo {@link LocalDate} che rappresenta la data.
     */
    public ObjectProperty<LocalDate> dataProperty() {
        return data;
    }

    /**
     * Restituisce la descrizione del movimento.
     *
     * @return la descrizione come {@link String}.
     */
    public String getDescrizione() {
        return descrizione.get();
    }

    /**
     * Imposta la descrizione del movimento.
     *
     * @param descrizione nuova descrizione testuale.
     * Postcondizione: {@link #getDescrizione()} restituirà la stringa impostata.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione.set(descrizione);
    }

    /**
     * Fornisce la property JavaFX per la descrizione del movimento.
     *
     * @return {@link StringProperty} che rappresenta la descrizione.
     */
    public StringProperty descrizioneProperty() {
        return descrizione;
    }

    /**
     * Restituisce una rappresentazione testuale dell'istanza, utile per logging e debug.
     *
     * @return stringa contenente i valori correnti di tutti i campi.
     */
    @Override
    public String toString() {
        return "Movimenti{" +
                "idMovimento=" + idMovimento.get() +
                ", idProdotto=" + idProdotto.get() +
                ", tipo='" + tipo.get() + '\'' +
                ", quantita=" + quantita.get() +
                ", data=" + data.get() +
                ", descrizione='" + descrizione.get() + '\'' +
                '}';
    }
}