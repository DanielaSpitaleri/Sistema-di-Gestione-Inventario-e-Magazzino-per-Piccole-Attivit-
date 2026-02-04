import it.unicas.project.template.address.model.Movimenti;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di unità per la classe Movimenti.
 * <br>
 * Contiene casi di test per costruttori, setter/getter e logiche di business basilari.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
class MovimentiTest {
    /**
     * Verifica che il costruttore di default inizializzi i campi ai valori attesi.
     * <br>
     * Controlla i valori di default (id, tipo, quantità, descrizione) e che la data non sia nulla e sia impostata a oggi.
     */
    @Test
    @DisplayName("Test Costruttore di Default")
    void testCostruttoreDefault() {
        Movimenti m = new Movimenti();

        assertEquals(-1, m.getIdMovimento());
        assertEquals(-1, m.getIdProdotto());
        assertEquals("", m.getTipo());
        assertEquals(0, m.getQuantita());
        assertEquals("", m.getDescrizione());

        assertNotNull(m.getData());
        assertEquals(LocalDate.now(), m.getData());
    }

    /**
     * Verifica il costruttore parametrizzato.
     * <br>
     * Assicura che tutti i campi passati al costruttore siano correttamente assegnati.
     */
    @Test
    @DisplayName("Test Costruttore Parametrizzato")
    void testCostruttoreParametrizzato() {
        // ARRANGE
        Integer idMov = 100;
        Integer idProd = 5;
        String tipo = "CARICO";
        int qta = 50;
        LocalDate dataMov = LocalDate.of(2023, 12, 25); // Natale 2023
        String desc = "Carico Natalizio";

        // ACT
        Movimenti m = new Movimenti(idMov, idProd, tipo, qta, dataMov, desc);

        // ASSERT
        assertEquals(100, m.getIdMovimento());
        assertEquals(5, m.getIdProdotto());
        assertEquals("CARICO", m.getTipo());
        assertEquals(50, m.getQuantita());
        assertEquals(dataMov, m.getData());
        assertEquals("Carico Natalizio", m.getDescrizione());
    }

    /**
     * Verifica la logica di "safe id" presente nel costruttore che converte null in -1.
     * <br>
     * Questo test passa null come idMovimento e si aspetta -1 come valore interno.
     */
    @Test
    @DisplayName("Test Logica Safe ID (Null Handling)")
    void testSafeIdNelCostruttore() {
        Movimenti m = new Movimenti(null, 1, "SCARICO", 10, LocalDate.now(), "Test Null");

        assertEquals(-1, m.getIdMovimento());
    }

    /**
     * Verifica il funzionamento dei setter e la sincronizzazione con le property JavaFX.
     * <br>
     * Post-condizione: le proprietà e i getter devono riflettere i nuovi valori impostati.
     */
    @Test
    @DisplayName("Test Setter e Property Binding")
    void testSetterEProperties() {
        Movimenti m = new Movimenti();

        m.setTipo("SCARICO");
        m.setQuantita(20);

        assertEquals("SCARICO", m.getTipo());
        assertEquals(20, m.getQuantita());

        assertEquals("SCARICO", m.tipoProperty().get());
        assertEquals(20, m.quantitaProperty().get());
    }

    /**
     * Controlla che le date passate siano gestite correttamente e che la logica temporale sia valida.
     * <br>
     * Crea un movimento con data 10 giorni fa e verifica che sia effettivamente antecedente a oggi.
     */
    @Test
    @DisplayName("Test Date nel Passato (Per Grafico Ultimi 30gg)")
    void testDatePassate() {
        LocalDate dieciGiorniFa = LocalDate.now().minusDays(10);

        Movimenti m = new Movimenti(1, 1, "CARICO", 5, dieciGiorniFa, "");

        assertEquals(dieciGiorniFa, m.getData());

        assertTrue(m.getData().isBefore(LocalDate.now()));
    }
}