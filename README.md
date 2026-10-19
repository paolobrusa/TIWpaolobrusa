# Aste online — versione server-side (JSP)

Politecnico di Milano · Tecnologie Informatiche per il Web · **Voto: 27/30**

Applicazione web per **aste online** realizzata con servlet Java e JSP, con struttura MVC e pattern DAO. Gli utenti possono mettere all'asta i propri articoli e fare offerte sulle aste degli altri.

> Esiste anche una versione single-page in JavaScript dello stesso progetto: [TIWpaolobrusaJS](https://github.com/paolobrusa/TIWpaolobrusaJS).

## Funzionalità

**Vendo**
- Creazione di articoli con nome, descrizione, prezzo e caricamento dell'immagine
- Creazione di un'asta scegliendo gli articoli, il prezzo iniziale, il rialzo minimo e la scadenza
- Elenco delle aste aperte e chiuse, con le offerte ricevute, e chiusura dell'asta dopo la scadenza

**Acquisto**
- Ricerca per parola chiave tra le aste attive degli altri utenti (cerca nel nome e nella descrizione degli articoli)
- Pagina di dettaglio dell'asta e modulo per fare un'offerta
- L'offerta è accettata solo se è almeno pari all'*offerta più alta (o prezzo iniziale) + rialzo minimo*: la regola è verificata in SQL, non solo nell'interfaccia
- Elenco delle aste aggiudicate

**In generale**
- Login e logout con gestione della sessione; un filtro sulle servlet protegge tutte le pagine e manda al login chi non è autenticato

## Architettura

```
beans/         Utente, Articolo, Asta, Offerta, State
controllers/   servlet: Login, Homepage, Vendo, Acquisto, DettaglioAsta, Offerte
  filterAndUtils/  LoginFilter, AddArticolo, CreateAsta, ImageHandler, TimeLeft, Logout
dao/           UtenteDAO, ArticoloDAO, AstaDAO, OffertaDAO  (JDBC, prepared statement)
webapp/WEB-INF/*.jsp   viste
```

Le regole di business sono controllate lato server (proprietà degli articoli, stato dell'asta, importo dell'offerta) e tutte le query usano prepared statement.

## Tecnologie

Java · Jakarta Servlet 6 · JSP · JDBC · MariaDB · Maven (wrapper incluso) · Apache Tomcat 10+

## Come avviarlo

1. Crea un database MariaDB chiamato `tiw` (tabelle usate: `utente`, `articolo`, `asta`, `articolilista`, `offerta`).
2. Imposta le credenziali del database in `src/main/webapp/WEB-INF/web.xml` (`user`, `pwd`, `urlDb`).
3. Compila il WAR e fai il deploy su Tomcat 10+:

```bash
./mvnw clean package
```

4. Apri `http://localhost:8080/<context-path>/`: arrivi alla pagina di login.

<!-- TODO: aggiungere uno schema.sql con la definizione delle tabelle e qualche utente di prova. -->
