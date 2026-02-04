package it.unicas.project.template.address.model.dao;

/**
 * Eccezione checked lanciata dal livello DAO per segnalare errori di accesso o operazioni sulla sorgente dati.
 * Utilizzata per incapsulare dettagli dell'errore provenienti dall'infrastruttura di persistenza.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
public class DAOException extends Exception{
    /**
     * Costruisce una nuova {@code DAOException} con un messaggio descrittivo.
     *
     * @param message descrizione dell'errore che ha causato l'eccezione
     */
    public DAOException(String message){
        super(message);
    }
}