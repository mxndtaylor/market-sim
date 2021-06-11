DROP DATABASE IF EXISTS StockSimDBtest;
CREATE DATABASE StockSimDBtest;

USE StockSimDBtest;

# renamed db to accomodate this table's name
CREATE TABLE Stocks (
	# moved to new table since UNIQUE kw in closings would only allow one closing entry per ticker
	Ticker VARCHAR(5) PRIMARY KEY,
    # nullable for now as it's unclear if we need it, as it should be the earliest value of Closings.Date
    IPODate DATE
);


CREATE TABLE Closings (
	`Date` DATE NOT NULL,
    Ticker VARCHAR(5) NOT NULL,
    Price DOUBLE NOT NULL,
    PRIMARY KEY (`Date`, Ticker),
		FOREIGN KEY fk_Closings_Stocks (Ticker) REFERENCES Stocks(Ticker)
);

CREATE TABLE Portfolios (
	PortfolioId INT PRIMARY KEY AUTO_INCREMENT,
    `Date` DATE NOT NULL,
    Cash DOUBLE NOT NULL,
    # perhaps unnecessary, but it's the only thing the UI passes to the backend so we might as well save it.
    StartDate DATE NOT NULL,
    StartCash DOUBLE NOT NULL
);

CREATE TABLE Holdings (
	PortfolioId INT,
    Ticker VARCHAR(5) NOT NULL,
    # added PurchaseDate instead of BoughtPrice, since we can grab price by date from Closings table
    # plus in the case of multiple buys of the same stock, it would be ambiguous what to put in the BoughtPrice field
	PurchaseDate DATE NOT NULL,
    Quantity INT NOT NULL,
	PRIMARY KEY pk_Holdings (PortfolioId, Ticker, PurchaseDate),
		FOREIGN KEY fk_Holdings_Portfolios (PortfolioId) REFERENCES Portfolios(PortfolioId),
		FOREIGN KEY fk_Holdings_Closings (Ticker, PurchaseDate) REFERENCES Closings(Ticker, `Date`)
);