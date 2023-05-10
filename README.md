# autostepic

Opis: Projekat za skladištenje guma rađen u Clojure programskom kodu

Generated using Luminus version "3.91"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run this in leiningen folder:

    lein repl 

To start application, type:

    user => (start)
    
## Kratki Q&A projekta
### 1. Koji je povod pravljenja aplikacije i šta ona obuvhata?
Ova aplikacija je zavrsni proizvod projekta iz predmeta Softverski Alati. Napisana je u Clojure programskom jeziku uz pomoc alata "Leiningen", Clojure Web frameworka "Luminus" i CSS biblioteke "Bulma"

Za izradu projekta su korisceni materijali sa predmeta softverski alati, kao i knjiga "Web Development with Clojure" koju su napisali Dimitri Sotnikov i Skot Brown

### 2. Koja je ideja aplikacije?
Aplikacija predstavlja prilagodjenu verziju baze podataka koja bi olaksala vulkanizerima i mehenicarima rukovodjenje hotelom guma

### 3. Sta je hotel guma?
Hotel guma predstavlja skladistenje guma po svim standardima , uzimajući u obzir način skladištenja, sortiranje i numerisanje guma. Nastao je kao potreba musterija da ostave svoje vansezonske gume kod vulkanizera sa 2 cilja:
• Kako ne bi gume nepotrebno zauzimale mesto u stanu ili podrumu musterije
• Kako bi olaksalo musteriji promenu guma

### 4. Koje su sve funkcionalnosti aplikacije?
Vulkanizer moze da kreira musterije i moze nad njima da vrsi osnovne CRUD operacije, isto vazi i za mesto. Korisnik moze u skladiste da unese gume korisnika, ali je potrebno da unese informacije o gumama, korisnika koji je ostavio gume na cuvanje, kao i mesto na kojem se gume cuvaju. Nakon rezervacije, uneto mesto je zauzeto i ne mogu se druge gume postaviti na to mesto. Ukoliko korisnik odluci da promeni mesto guma ili da gume izveze iz skladista, odabrano mesto treba da se oslobodi. Pre brisanja, mušterija ne smeju da imaju rezervacije u skladistu i mesta ne smeju da imaju gume

### 5. Koji su najveći izazovi koje sam imao prilikom izrade projekta?
Najveći izazov je bio sam programski jezik Clojure. Razlog je to što se do dosadašnjeg školovanja nisam susretao sa Lisp jezicima niti njihovim interaktivnim developmentom. Nakon učenja njegovih osnovnih funkcionalnosti u konzolnim aplikacijama, prešao sam na izradu web aplikacije prateći uputstva pomenute knjige. Knjiga vrlo detaljno objašnjava pokretanje web aplikacije preko Leiningen alata, kreiranja tabela i njenih upita, ali sam imao problema prilikom kreiranja CRUD operacija nad entitetima iz tabela.

Problemi kojima sam posvetio najviše vremena za rešavanje su:
• API metode
• prikupljanje podataka iz URL-a
• Problem sa Lock migracijama

Prvi problem: Svaki pokušaj pozivanja bilo koje API metode osim GET i POST je predstavljao problem. Metode PUT i DELETE (koje se univerzalno koriste za izmenu i brisanje entiteta iz baze) nisu radile. Kako bih rešio ovaj problem, za sve API pozive koje bi vršile izmenu nad tabelom u bazi podataka sam koristio POST metodu.

Drugi problem: Prikupljanje podataka iz URLa. Pozivi na izmenu i brisanje entiteta iz baze (dugmići edit i delete) su prvenstveno trebali da budu u svakom redu, svakog entiteta u bazi; Međutim, program nije funckionisao ukoliko se jedna metoda sa istim nazivom poziva više puta na jednoj stranici. Uspeo sam svakoj metodu da preimenujem tako da ima drugačiji naziv, ali je nakon toga problem predstavljao prosleđivanje id-a entiteta stranicama za izmenu i brisanje. Testirao sam metode koje su imale zadatak da prikupe id i prikažu podatke sa fiksnim vrednostim (npr: /edit-Musterije/id/3) i one su funkcionisale, ali svaki pokušaj prosleđivanja vrednosti iz URL-a u metodu je izbacio grešku sa natpiosom da id ne može biti null vrednost. Jedina alternativa je bila da se ovi dugmići ponovo definišu kao univerzalni, tako da korisnik mora da unese id entiteta kako bi vršio promene nad tim entitetom.

Treci problem: Lock migracije. Prilikom kreiranja migracija i njihvog izvrsenja, napravio sam grešku prilikom kreiranja tabele. Nakon višestrukog pokušaja za ponovno migriranje, pojavila se greška "Migration reserved by another instance". Svaki pokušaj promene migracija ili baze podataka je bio bezuspešan. Problem sam rešio tako što sam napravio novi projekat i ponovo izvršio migraciju svih tabela iz baze podataka


## License

Copyright © 2023 FIXME


