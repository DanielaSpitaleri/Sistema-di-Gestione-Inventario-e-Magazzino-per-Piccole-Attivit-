-- =================================================================
-- 1. SCRIPT DI CREAZIONE STRUTTURA (DDL) DATABASE MAGAZZINO
-- =================================================================

-- CREAZIONE E SELEZIONE DATABASE
CREATE DATABASE IF NOT EXISTS magazzino;
USE magazzino;

-- Disabilita i controlli delle chiavi esterne per pulizia
SET FOREIGN_KEY_CHECKS = 0;

-- PULIZIA TOTALE (OPZIONALE, utile per l'esecuzione ripetuta)
DROP TABLE IF EXISTS movimenti;
DROP TABLE IF EXISTS prodotti;

-- CREAZIONE TABELLA PRODOTTI
CREATE TABLE prodotti (
    `idProdotto` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `descrizione` MEDIUMTEXT NULL DEFAULT NULL,
  `quantita` INT NULL DEFAULT NULL,
  `giacenzaMin` INT NULL DEFAULT NULL,
  `prezzoAcquisto` DECIMAL(5,2) NULL DEFAULT NULL,
  `prezzoVendita` DECIMAL(5,2) NULL DEFAULT NULL,
  PRIMARY KEY (`idProdotto`),
  UNIQUE INDEX `idprodotto_UNIQUE` (`idProdotto` ASC) VISIBLE,
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC) VISIBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CREAZIONE TABELLA MOVIMENTI
CREATE TABLE IF NOT EXISTS movimenti (
    `idMovimento` INT NOT NULL AUTO_INCREMENT,
  `idProdotto` INT NOT NULL,
  `tipo` ENUM('CARICO', 'SCARICO') NOT NULL,
  `quantita` INT NOT NULL,
  `data` DATE NOT NULL,
  `descrizione` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`idMovimento`),
  INDEX `idProdotto` (`idProdotto` ASC) VISIBLE,
  CONSTRAINT `movimenti_ibfk_1`
    FOREIGN KEY (`idProdotto`)
    REFERENCES `prodotti` (`idProdotto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Riabilita i controlli
SET FOREIGN_KEY_CHECKS = 1;