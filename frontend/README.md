# Frontend aplikace
Aplikace slouží pro jednoduché provedení mikrobenchmarku. Aplikace je implementovaná v Angularu 5.
Aplikace je dostupná na školním serveru [http://147.228.63.36/mbmark](http://147.228.63.36/mbmark).

## Účel aplikace
Aplikace vznikla jako ukázka, jak komunikovat s backend aplikací za účelem provedení mikrobenchmarku.
Pro provedení mikrobenchmarku je nutné provést 2 - 3 HTTP requesty, navázat websocketové spojení a zaslat zprávu skrze websocketové spojení. Backend aplikace posílá skrze websocketové spojení informační zprávy o aktuálně zpracovávané fázi mikrobenchmarku.

## Spuštění
- Pro spuštění aplikace je nutné mít nainstalovaný NodeJS - [https://nodejs.org/en/download/](https://nodejs.org/en/download/).
- Aby bylo možné aplikaci spustit, musí se nejprve provést příkaz `npm install`, který stáhne všechny potřebné závislosti Angular aplikace.
- Aplikace se spustí příkazem `npm run start`
- Po úspěšném spuštění je aplikace dostupná na adrese [http://localhost:4200](http://localhost:4200)
