# 💻 Progetto 3: Sistema di Gestione Magazzino (JavaFX & MySQL)

### Descrizione del Progetto

Applicazione desktop sviluppata in **JavaFX** per la gestione di un inventario di magazzino.
Il sistema permette di tenere traccia della giacenza, registrare movimenti (carico/scarico) e monitorare la disponibilità dei prodotti, segnalando quelli sotto la soglia minima di sicurezza.

---

### 🚀 Stack Tecnologico

* **Linguaggio:** Java (JDK 17+)
* **Interfaccia Utente:** JavaFX
* **Database:** MySQL
* **Architettura:** MVC (Model-View-Controller) / Pattern DAO (Data Access Object)

---

### ⚙️ Istruzioni di Setup Locale

Per eseguire il progetto, assicurarsi di avere i seguenti componenti installati e configurati.

#### 1. Prerequisiti

* **Java Development Kit (JDK) 17 o superiore.**
* **MySQL Server** installato e in esecuzione.
* **Driver JDBC:** Il progetto utilizza il connettore MySQL/J. Assicurarsi che il file `.jar` sia incluso nelle librerie (`lib/`) del progetto.
* **JavaFX SDK:** Configurare l'SDK di JavaFX nell'IDE utilizzato (es. come Moduli per IntelliJ IDEA).

#### 2. Configurazione Database

La cartella `db/` contiene gli script SQL necessari per l'inizializzazione.

1.  **Creazione DB:** Accedere al MySQL Server ed eseguire il comando per creare il database:
    ```sql
    CREATE DATABASE IF NOT EXISTS magazzino;
    ```
2.  **Schema (Struttura):** Eseguire lo script **`db/magazzino_schema.sql`** per creare le tabelle `prodotti` e `movimenti`.
3.  **Dati Demo:** Eseguire lo script **`db/magazzino_demo_data.sql`** per popolare il database con prodotti e movimenti di esempio.

#### 3. Avvio Applicazione

1.  Clonare la repository in locale.
2.  Aprire il progetto nell'IDE (es. importando il file `.iml`).
3.  Verificare e, se necessario, aggiornare le credenziali di connessione al database all'interno della classe DAO.
4.  Eseguire la classe **`MainApp.java`**.

> Per accedere alla dashboard principale dopo l'avvio, utilizzare la seguente credenziale: \
> **Password di accesso (Demo): `123`**


