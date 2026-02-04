-- =================================================================
-- 2. SCRIPT DI INSERIMENTO DATI DEMO (DML)
-- Lo schema deve essere già stato creato da 'magazzino_schema.sql'
-- =================================================================

USE magazzino;

-- 1. INSERIMENTO CATALOGO PRODOTTI (9 Articoli)
INSERT INTO prodotti (nome, descrizione, quantita, giacenzaMin, prezzoAcquisto, prezzoVendita)
VALUES ('Notebook HP', 'Notebook HP ProBook 450 G9 - Laptop professionale con processore Intel Core i5, 16GB RAM e SSD da 512GB. Schermo 15.6" antiriflesso, Windows 11 Pro installato.',
0, 3, 450.00, 699.00),
('Monitor Samsung', 'Monitor Samsung 24" LED IPS - Schermo Full HD (1920x1080) con design borderless su tre lati. Tecnologia Flicker Free e porte HDMI/VGA.',
 0, 4, 90.00, 149.00),
('SSD SanDisk', 'SSD Esterno SanDisk 1TB - Unità a stato solido portatile, resistente a cadute e acqua (IP55). Velocità di lettura fino a 1050MB/s tramite USB-C.',
0, 2, 20.00, 55.00),
('Mouse Logitech', 'Mouse Wireless Logitech M185 - Mouse ottico senza fili grigio/nero. Include ricevitore nano USB "Plug & Play". Batteria a lunga durata (fino a 12 mesi).',
0, 5, 8.50, 18.90),
('Tastiera Logitech', 'Tastiera Meccanica Logitech G Pro - Tastiera da gaming e programmazione. Switch meccanici tattili, layout QWERTY italiano e retroilluminazione RGB personalizzabile.',
0, 3, 75.00, 129.90),
('Stampante Epson', 'Stampante Multifunzione Epson EcoTank - Stampante a getto di inchiostro A4 (stampa, copia, scanner). Sistema EcoTank senza cartucce per costi di gestione ultrabassi.',
0, 2, 180.00, 249.00),
('Router Wi-Fi TP-Link', 'Router Wi-Fi Mesh TP-Link Deco M4 (2 Pack) - Sistema Wi-Fi Mesh per coprire fino a 260 mq. Velocità fino a 1167 Mbps, ideale per streaming 4K e smart home.',
0, 3, 110.00, 159.90),
('Webcam Logitech', 'Webcam Full HD Logitech C920 - Webcam per videocall e streaming con risoluzione 1080p a 30fps. Ottica Full HD in vetro e correzione automatica della luce.',
0, 6, 45.00, 79.90),
('Cavo HDMI', 'Cavo HDMI 2.1 Alta Velocità 2m - Cavo Ultra High Speed per supportare risoluzioni 4K@120Hz e 8K@60Hz. Ideale per monitor e console di ultima generazione.',
0, 10, 5.50, 14.50);

-- 2. INSERIMENTO MOVIMENTI (Generazione Storico)
INSERT INTO movimenti (idProdotto, tipo, quantita, data, descrizione) VALUES
(1, 'CARICO', 100, '2025-10-01', 'Maxi fornitura trimestrale'),
(1, 'SCARICO', 40, '2025-10-20', 'Vendita grossista'),
(1, 'SCARICO', 10, '2025-11-05', 'Vendita al banco'),
(2, 'CARICO', 50, '2025-10-05', 'Carico iniziale'),
(2, 'SCARICO', 40, '2025-11-15', 'Promozione svuota tutto'),
(3, 'CARICO', 100, '2025-10-01', 'Produzione propria'),
(4, 'CARICO', 20, '2025-11-01', 'Arrivo settimanale'),
(4, 'SCARICO', 15, '2025-11-25', 'Festa privata cliente'),
(5, 'CARICO', 30, '2025-11-10', 'Rifornimento bar'),
(6, 'CARICO', 100, '2025-10-01', 'Scorta pasticceria'),
(6, 'SCARICO', 80, '2025-11-20', 'Produzione panettoni'),
(7, 'CARICO', 24, '2025-10-15', 'Cantina sociale'),
(7, 'SCARICO', 11, '2025-11-28', 'Cene aziendali'),
(8, 'CARICO', 20, '2025-11-01', 'Fornitura Pavesi'),
(8, 'SCARICO', 5, '2025-11-12', 'Colazioni'),
(9, 'CARICO', 20, '2025-11-01', 'Fornitura Centrale'),
(9, 'SCARICO', 20, '2025-11-03', 'Scadenza breve - smaltito tutto');


-- 3. ALLINEAMENTO FINALE TABELLA PRODOTTI
-- (Questi UPDATE non sono DML essenziali, ma completano la logica del tuo script originale)
UPDATE prodotti SET quantita = 50 WHERE idProdotto = 1; 
UPDATE prodotti SET quantita = 10 WHERE idProdotto = 2; 
UPDATE prodotti SET quantita = 100 WHERE idProdotto = 3;
UPDATE prodotti SET quantita = 5 WHERE idProdotto = 4; 
UPDATE prodotti SET quantita = 30 WHERE idProdotto = 5; 
UPDATE prodotti SET quantita = 20 WHERE idProdotto = 6;  
UPDATE prodotti SET quantita = 13 WHERE idProdotto = 7; 
UPDATE prodotti SET quantita = 15 WHERE idProdotto = 8; 
UPDATE prodotti SET quantita = 0 WHERE idProdotto = 9;

-- 4. VERIFICHE FINALI
SELECT * FROM prodotti;
SELECT * FROM prodotti WHERE quantita <= giacenzaMin;