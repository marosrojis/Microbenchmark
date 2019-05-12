# Frontend aplikace
Aplikace slouží pro jednoduché provedení mikrobenchmarku. Aplikace je implementovaná v Angularu 5.
Aplikace je dostupná na školním serveru [http://147.228.63.36/mbmark](http://147.228.63.36/mbmark).

## Účel aplikace
Aplikace vznikla jako ukázka, jak komunikovat s backend aplikací za účelem provedení mikrobenchmarku.
Pro provedení mikrobenchmarku je nutné provést 2 - 3 HTTP requesty, navázat websocketové spojení a zaslat zprávu skrze websocketové spojení. Backend aplikace posílá skrze websocketové spojení informační zprávy o aktuálně zpracovávané fázi mikrobenchmarku.

## Kompilace

### NodeJS
- Níže jsou napsány příkazy pro nainstalování nástroje NodeJS, aby fungoval příkaz `npm`.

	1. curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash -
	2. apt-get install -y nodejs
	3. apt-get install -y build-essential
	4. npm install -g json

### Stažení závislostí
- Nejprve se musí provést příkaz `npm install`, který stáhne všechny potřebné závislosti Angular aplikace.
- Všechny potřebné knihovny se automaticky stáhnou do složky *node_modules*.

### Kompilace
- Aplikaci lze zkompilovat příkazem `npm run build`.
- Po úspěšné kompilaci se vygeneruje složka *dist* obsahující zkompilovanou verzi frontend aplikace.

## Spuštění
- Aby bylo možné aplikaci spustit, musí se nejprve provést příkaz `npm install`, který stáhne všechny potřebné závislosti Angular aplikace.
- Aplikace se spustí příkazem `npm run start`
- Po úspěšném spuštění je aplikace dostupná na adrese [http://localhost:4200](http://localhost:4200)
