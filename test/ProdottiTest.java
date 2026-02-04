import it.unicas.project.template.address.model.Prodotti;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di unità per la classe Prodotti.
 * <br>
 * Comprende verifiche sui costruttori, sui setter/getter e sulla logica di vincolo quantità.
 *
 * @author Cristian Corsini, Simone Feola, Daniela Spitaleri
 * @since 19/11/2025
 */
class ProdottiTest {

    /**
     * Verifica che il costruttore di default imposti valori sensati e coerenti.
     * <br>
     * Controlla id, nome, quantità e prezzo di acquisto.
     */
    @Test
    @DisplayName("Test Costruttore di Default")
    void testCostruttoreDefault() {
        Prodotti p = new Prodotti();

        assertEquals(-1, p.getIdProdotto());
        assertEquals("", p.getNome());
        assertEquals(0, p.getQuantita());
        assertEquals(0.0, p.getPrezzoAcquisto());
    }

    /**
     * Verifica il costruttore parametrizzato e i getter corrispondenti.
     * <br>
     * Assicura che i valori passati al costruttore siano assegnati correttamente.
     */
    @Test
    @DisplayName("Test Costruttore Parametrizzato e Getter")
    void testCostruttoreParametrizzato() {
        // ARRANGE
        int id = 10;
        String nome = "Trapano Elettrico";
        String desc = "Trapano 500W professionale";
        int quantita = 50;
        int giacenzaMin = 10;
        double pAcquisto = 45.50;
        double pVendita = 89.99;

        // ACT
        Prodotti p = new Prodotti(id, nome, desc, quantita, giacenzaMin, pAcquisto, pVendita);

        // ASSERT
        assertEquals(10, p.getIdProdotto());
        assertEquals("Trapano Elettrico", p.getNome());
        assertEquals(50, p.getQuantita());
        assertEquals(10, p.getGiacenzaMin());

        // Per i double si usa un delta di tolleranza (es. 0.001)
        assertEquals(45.50, p.getPrezzoAcquisto(), 0.001);
        assertEquals(89.99, p.getPrezzoVendita(), 0.001);
    }

    /**
     * Verifica che i setter aggiornino correttamente lo stato dell'oggetto e che le property JavaFX siano sincronizzate.
     * <br>
     * Post-condizione: nome e quantita e la relativa property devono riflettere i nuovi valori.
     */
    @Test
    @DisplayName("Test Setter e Property Binding")
    void testSetter() {
        Prodotti p = new Prodotti();

        p.setNome("Martello");
        p.setQuantita(100);

        assertEquals("Martello", p.getNome());
        assertEquals(100, p.getQuantita());

        assertEquals("Martello", p.nomeProperty().get());
    }

    /**
     * Test del metodo vincoloQuantita nel caso in cui la quantità sia maggiore della giacenza minima.
     * Ci si aspetta false perché non è sottoscorta.
     */
    @Test
    @DisplayName("Test Logica Vincolo Quantità: Caso OK (Quantità > Minima)")
    void testVincoloQuantitaOk() {
        Prodotti p = new Prodotti(1, "Viti", "Desc", 100, 20, 1.0, 2.0);

        boolean risultato = p.vincoloQuantita();

        assertFalse(risultato, "Il vincolo non dovrebbe scattare se Quantità (100) > Minima (20)");
    }

    /**
     * Test del metodo vincoloQuantita nel caso critico in cui la quantità sia minore della giacenza minima.
     * Ci si aspetta true perché l'articolo è in sottoscorta.
     */
    @Test
    @DisplayName("Test Logica Vincolo Quantità: Caso Critico (Quantità < Minima)")
    void testVincoloQuantitaCritico() {
        Prodotti p = new Prodotti(1, "Viti", "Desc", 5, 20, 1.0, 2.0);

        boolean risultato = p.vincoloQuantita();

        assertTrue(risultato, "Il vincolo deve essere TRUE se Quantità (5) < Minima (20)");
    }

    /**
     * Test del metodo vincoloQuantita nel caso limite in cui la quantità sia esattamente uguale alla giacenza minima.
     * Basandosi sull'implementazione (minore o uguale), ci si aspetta true.
     */
    @Test
    @DisplayName("Test Logica Vincolo Quantità: Caso Limite (Quantità == Minima)")
    void testVincoloQuantitaLimite() {
        Prodotti p = new Prodotti(1, "Viti", "Desc", 20, 20, 1.0, 2.0);

        boolean risultato = p.vincoloQuantita();

        assertTrue(risultato, "Il vincolo deve essere TRUE se Quantità è uguale alla Minima");
    }
}

