/* CREATE Bejaardentehuis TABLE*/
CREATE TABLE Bejaardentehuizen (
	bejaardentehuis varchar(255) NOT NULL UNIQUE,
	adres varchar(255) NOT NULL,
  postcode varchar(255) NOT NULL,
  telefoon varchar(255) NOT NULL,
  PRIMARY KEY (bejaardentehuis)
);

/* CREATE Patienten TABLE*/
CREATE TABLE Patienten (
    bejaardentehuis varchar(255) NOT NULL,
    naam varchar(255) NOT NULL,
    geboortedatum date NOT NULL,
    verblijftSinds date DEFAULT CURRENT_DATE,
    kamer varchar(255) NOT NULL UNIQUE,
    telefoon varchar(255) NOT NULL,
    adres varchar(255) NOT NULL,
    postcode varchar(255) NOT NULL,
    PRIMARY KEY (kamer)
    /* FOREIGN KEY (bejaardentehuis) REFERENCES Bejaardentehuizen(bejaardentehuis) */
);

/* CREATE loopmotivatie TABLE*/
CREATE TABLE Loopmotivaties (
    kamer varchar(255) NOT NULL,
    stappen int,
    wandelingStart timestamp DEFAULT CURRENT_TIMESTAMP UNIQUE,
    wandelingStop timestamp,
    PRIMARY KEY (wandelingStart)
    /* FOREIGN KEY (kamer) REFERENCES Patienten(kamer) */
);

/* ADD FOREIGN KEY TO Patienten */
ALTER TABLE Patienten
ADD CONSTRAINT FK_Bejaardentehuis
FOREIGN KEY (bejaardentehuis) REFERENCES Bejaardentehuizen(bejaardentehuis) ON DELETE CASCADE;

/* ADD FOREIGN KEY TO Loopmotivaties */
ALTER TABLE Loopmotivaties
ADD CONSTRAINT FK_Loopmotivaties
FOREIGN KEY (kamer) REFERENCES Patienten(kamer) ON DELETE CASCADE;

/* CONSTRAINTS */

/* Patienten: UNIQUE CONSTRAIN FOR THE COMBINATION OF bejaardentehuis, naamm, geboortedatum AND kamer*/
ALTER TABLE Patienten
  ADD CONSTRAINT uq_Patient UNIQUE(bejaardentehuis, naam, geboortedatum, kamer);
  
/* Loopmotivaties: UNIQUE CONSTRAIN FOR THE COMBINATION OF bejaardentehuis, kamer, stappen, wandelingStart AND wandelingStop */
ALTER TABLE Loopmotivaties
  ADD CONSTRAINT uq_Loop UNIQUE(kamer, stappen, wandelingStart, wandelingStop);


