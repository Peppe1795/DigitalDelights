Digital Delights
Benvenuti nel repository del progetto Digital Delights. Questa piattaforma è un e-commerce full-stack sviluppato con Java e Spring Boot per il Back-End, Angular e bootStrap per il Front-End, utilizza PostgreSQL come sistema di memorizzazione dei dati e Stripe per gestire i pagamenti.

Funzionalità

- Autenticazione e registrazione degli utenti
- Visualizzazione, ricerca e filtraggio dei prodotti
- Wishlist
- Carrello e processo di checkout
- Gestione degli ordini e dello storico degli acquisti
- Dashboard di amministrazione per la gestione dei prodotti e degli ordini

Tecnologie utilizzate
FRONTEND

- Angular 14 - Framework per la creazione dell'interfaccia utente
- BootStrap - Libreria per la gestione della parte responsive dell'interfaccia utente

BACKEND

- Java - Server e logica di backend
- Spring Boot - Framework basato su Java per creare microservizi e app Web.
- PostgreSQL - Per la memorizzazione dei dati

  Altre tecnologie

- Stripe - sistema per la gestione dei pagamenti

Istruzione per L'utilizzo

1. Crea un database chiamato 'Digital Delights'

2. Configura le variabili d'ambiente in un file .env come:

- PG_USERNAME=tuoUsernameSuPostgre
- PG_PASSWORD=tuaPasswordSuPostgre
- PG_DB=nomeDb
- PORT=3001
- JWT_SECRET=laTuaJwtSecret
- stripe.key.secret=laTuaChiaveSegretaFornitaDaStripe
- EMAIL_USERNAME=emailDaCuiVerrannoInviateLeMailGenerateInAutomatico
- EMAIL_PASSWORD=passwordEmail

Per il backend:

- utilizzando un editor che supporta lo sviluppo in Java importa il mio progetto maven e le dipendenze sono gia tutte installate.
- avvia l'app.

Per il frontend:

- installa nodeJS dal sito ufficiale
- installa Angular 14 da terminale con il comando [npm install -g @angular/cli]
- utlizza un editor che supporta Angular
- installa i node package nella cartella del progetto [npm i]
- installa bootStrap con il comando [npm install bootstrap] e [npm install @popperjs/core]
- installa gsap per alcune animazioni con il comdando [npm install gsap]
- installa Stripe con il comando [npm install stripe]
- avvia il progetto con il comando [npm s -o](ricordati di utilizzare la porta 4200 per impostazione del cors)

Autore
Giuseppe Petrucci - https://github.com/Peppe1795 - https://www.linkedin.com/in/giuseppe-petrucci-7b817327a/
