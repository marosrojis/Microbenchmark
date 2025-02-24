# Backend aplikace
Aplikace slouží pro jednoduché porovnání fragmentů kódu v programovacím jazyce Java.

## Testovací server
Aplikace běží na školním serveru 147.228.63.36 na portu 8080. Server je dostupný pouze ze ZČU sítě.
Dále běží testovací frontend aplikace na adrese [http://147.228.63.36/mbmark](http://147.228.63.36/mbmark).

V aplikaci jsou vytvořené 3 testovací účty:
- účet s ADMIN rolí - email: admin@mbmark.cz, heslo: admin
- účet s USER rolí - email: user@mbmark.cz, heslo: user
- účet s DEMO rolí - email: demo@mbmark.cz, heslo: demo

## Technologie
- Aplikace je vyvíjena v programovacím jazyce Java 1.8. V této verzi Javy jsou spouštěny vygenerované mikrobenchmarky pomocí frameworku JMH v Docker kontejneru. Framework Spring Boot je použit ve verzi 2.0.6.
- Aplikace potřebuje ke svému správnému běhu několik externích nástrojů. Jako databáze je použit PostgreSQL 9.6.10. Docker je nainstalován ve verzi 18.09.0. Pro kompilaci aplikace a benchmarků je použit nástroj Máven v3.6.0. JMH je použit ve verzi 1.21.
- Správné fungování aplikace je otestováno v operačním systému Windows 10 a Linux Debian 4.9.110-3+deb9u2. Všechny použité nástroje lze nainstalovat na oba uvedené operační systémy.

## Potřebné nástroje
### Databáze
- PostgreSQL databáze musí běžet na stejném počítači jako aplikace.
- Před spuštěním aplikace je potřeba vytvořit v PostgreSQL databázi s názvem `mbmark`.
- Dále vytvořit uživatele `mbmark` s heslem `mbmark`.
- Nakonec je nutné nastavit vytvořenému uživateli `mbmark` práva na vytvořenou databázi `mbmark`.
- Připojení k databázi lze změnit v souboru *microbenchmark-web/src/main/resources/application.properties*. Po změně hodnot v tomto souboru je nutné znovu zkompilovat aplikaci.

### Docker
- Docker musí běžet na stejném počítači jako aplikace.
- Před spuštěním aplikace je potřeba vytvořit Docker image s názvem `docker-microbenchmark`
- Příkaz pro build image: `docker build -f Dockerfile -t docker-microbenchmark .`

## Kompilace aplikace
- Pro zkompilování aplikace je potřeba ve složce 'backend' otevřít příkazovou řádku a provést příkaz `mvn clean install`. 
- V průběhu kompilace jsou provedeny všechny JUnit testy. 
- Pokud doběhne kompilace úspěšně, ve složce *microbenchmark-web/target* je vygenerován soubor `MBMark_application.jar`.

## Spuštění
- Aplikaci lze spustit pouze pokud jsou v operačním systému nainstalovány všechny důležité nástroje a je systém správně nastaven. Správné nastavení operačního systému Linux Debian 4.9.110-3+deb9u2 je popsáno níže.
- Spring Boot aplikace v sobě obsahuje Tomcat server. Díky této vlastnosti stačí spustit vygenerovaný JAR soubor a backend aplikace se spustí. Aplikaci je nutné spustit pod root uživatelem.

### Skript pro spuštění
- Pro spuštění aplikace slouží bashový skript run.sh ve složce backend. Skript lze spustit pouze v operačním systému Linux.
- Ve skriptu se nachází příkaz java s parametry pro správné spuštění aplikace.
- Pro spuštění lze využít dva různé maven profily. Profil se nastaví pomocí příkazu `-Dspring.profiles.active=`. Pokud chce uživatel spustit aplikaci s výchozí konfigurací pro vývoj, použije profil *dev*. Pokud ale chce spustit aplikaci s konfigurací pro produkci, musí použít parametr *prod*. Maven profily definují například cestu pro ukládání vygenerovaných projektů. Konfigurační soubory se nachází v adresáři `backend/microbenchmark-web/src/main/resources`.
- Příkaz obsahuje parametr pro nastavení portu. a maven profilu. Dále se zde nachází parametr pro zabezpečení aplikace. Jako předposlední parametr je uvedena cesta k JAR souboru. Jako poslední je znak & pomocí kterého je zajištěno, aby aplikace běžela i po ukončení terminálu. Před provedením příkazu java je provedena kontrola, jestli je v systému nainstalován Docker a PostgreSQL databáze a následné zkopírování JAR souboru z *target* složky.
- Pokud aplikace úspěšně naběhne, je dostupná na portu **8080**.

### Admin uživatel
- Po prvním spuštění je automaticky vytvořen uživatel s rolí *ADMIN*. Uživatel má nastavený email **`admin@rojik.cz`** a heslo `78X2Dup4O9nuoCEg`.
- Uživatel je vytvořen pomocí Liquibase skriptu v adresáři *microbenchmark-web/src/main/resources/db/changelog/insert_data.xml*.
- Pokud chcete změnit výchozího uživatele, stačí editovat tento soubor a zkompilovat aplikaci. Heslo musí být zahashováno funkcí BCrypt.

## REST API
- REST API je zdokumentováno pomocí nástroje Swagger.
- Swagger UI je dostupné na adrese [http://147.228.63.36:8080/swagger-ui.html](http://147.228.63.36:8080/swagger-ui.html).
- Nicméně jeden endpoint Swagger nezdokumentoval. Jedná se o endpoint sloužící pro přihlášení uživatele.
- Pro přihlášení je potřeba poslat HTTP požadavek typu POST na adresu `/api/login`. V těle požadavku je uveden email a heslo uživatele. Níže je příklad těla požadavku pro přihlášení:
```
 {
	"email" : "user@mbmark.cz",
	"passowrd: "user"
}
```
- Po úspěšném přihlášení je v odpovědi zaslán token pomocí kterého uživatel komunikuje se serverem.

## Websocket
- Ve Swaggeru dále chybí informace o navázání websocketového spojení, adresa pro zaslání zprávy a adresa, na kterou server zasílá informační zprávy.
- Pro navázání spojení slouží adresa `/socket/websocket`.
- Spuštění benchmarku se zahájí posláním zprávy na adresu `/app/benchmark/run` s parametrem ID projektu. Pokud je uživatel přihlášen, součástí hlavičky zprávy by měl být i autorizační token.
- Průběžné informace o aktuální prováděné fázi benchmarku lze získávat odposloucháváním adresy `/user/benchmark/result/step`.
- Po úspěšném či neúspěšném provedení benchmarku jsou výsledky dostupné na adrese `/user/benchmark/result`.

## Frontend aplikace
- Pro otestování a pohodlnější provádění testovacích benchmarků je vytvořena jednoduchá frontend aplikace ve frameworku Angular 5. Aplikace je přiložena v adresáři `frontend`. 
- V implementaci aplikaci lze případně najít, jaké všechny HTTP požadavky nebo websocketové zprávy je nutné poslat pro správné provedení benchmarku.

## Jak aktualizovat verzi Javy
- Benchmarky jsou generovány ve verzi Javy 1.8. Pokud by v budoucnu bylo potřeba použít novější verzi Javy, je potřeba provést několik kroků.
- Jako první je potřeba změnit konfigurační hodnotu javaVersion skrze REST API `PUT /api/property`. V těle požadavku se uvede jako klíč `javaVersion` a hodnota nová verze Javy.
- Dále je nutné vytvořit novou mapu tříd, která se používá pro automatický import. K tomu slouží endpoind `PUT /api/library`. V těle požadavku je uveden jediný atribut. Jedná se o uvedení absolutní cesty k adresáři, ve kterém se nacházejí JAR soubory k proskenování a nalezení všech dostupných tříd Javy.
- Pro správnou kompilaci benchmarku nástrojem Maven je potřeba mít správně nastavenou systémovou proměnnou `JAVA_HOME`. Proměnná musí obsahovat cestu do adresáře nové verze Javy.
- Jako poslední je nezbytné upravit současný Dockerfile. V definici Dockerfile je uvedena Java, která má být ve spuštěném kontejneru nainstalována (apk add openjdk8). V tomto příkazu se potřeba vyměnit balíček `openjdk8` za novější. Upravený Dockerfile se následně musí zbuildit, aby nahradil současnou verzi obrazu `docker-microbenchmark`.
- Všechny tyto kroky je nutné provést pro použití novější verze Javy. Tyto kroky nevyžadují restartování běžící backend aplikace.

## Nastavení systému
- V této kapitole je popsán podrobný návod, jak nastavit linuxový server, aby na něm mohla aplikace bezproblémově běžet. Návod obsahuje přímo linuxové příkazy, které je nutné provádět v uvedeném pořadí. 
- Všechny následující příkazy by se měli provést před kompilací aplikace.
- Všechny příkazy je nutné provádět s root uživatelem.

### Obecné příkazy
- Následující příkazy pouze aktualizují systém a nainstalují základní sadu nástrojů.

	1. sudo -i
	2. apt-get update
		(a) pokud nastane v průběhu update chyba, pravděpodobně tyto příkazy vyřeší
			problém
		(b) apt-get install dirmngr
		\(c\) apt-key adv –keyserver keyserver.ubuntu.com –recv-keys C2518248EEA14886
	3. apt-get install net-tools

### Java 1.8
- Níže jsou napsány příkazy pro nainstalování Java 1.8.

	1. add-apt-repository ppa:openjdk-r/ppa
	2. apt-get update
	3. apt-get install openjdk-8-jdk
	4. export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64/
	5. export PATH=$PATH:$JAVA_HOME/bin

### Databáze:
- Níže jsou napsány příkazy pro nainstalování PostgreSQL databáze.

	1. apt-get install postgresql postgresql-contrib
	2. sudo -u postgres psql
	3. DB dotazy pro vytvoření databáze a uživatele
	(a) CREATE DATABASE mbmark;
	(b) CREATE USER mbmark WITH ENCRYPTED PASSWORD 'mbmark';
	\(c\) GRANT ALL PRIVILEGES ON DATABASE mbmark TO mbmark;

### Docker
- Níže jsou napsány příkazy pro nainstalování Dockeru.

	1. apt-get update
	2. apt-get install apt-transport-https ca-certificates curl gnupg2 software-properties-common
	3. curl -fsSL https://download.docker.com/linux/debian/gpg | sudo apt-key add -
	4. sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
	5. apt-get update
	6. apt-get install docker-ce
	7. vytvoření Dockefile
		(a) mkdir /var/docker
		(b) cd /var/docker
		\(c\) nano Dockerfile
			i. Dockerfile se nachází ve složce Docker
		(d) docker build -f Dockerfile -t docker-microbenchmark .

### Maven
- Níže jsou napsány příkazy pro nainstalování nástroje Maven.

	1. cd /tmp
	2. apt-get install wget
	3. wget https://bit.ly/2VDHsKZ -O apache-maven-3.6.0-bin.tar.gz
	4. tar xzvf apache-maven-3.6.0-bin.tar.gz
	5. mv apache-maven-3.6.0 /opt/apache-maven-3.6.0
	6. export PATH=/opt/apache-maven-3.6.0/bin:$PATH
	7. export M2_HOME=/opt/apache-maven-3.6.0

### Apache
- Níže jsou napsány příkazy pro nainstalování nástroje Apache.

	1. apt-get install apache2
	2. nano /etc/apache2/ports.conf
		(a) Listen 147.228.63.36:80 (jedná se o IP adresu serveru)
	3. nano /etc/apache2/sites-available/000-default.conf
		(a) <VirtualHost *:80>
	4. /etc/init.d/apache2 restart
	5. mkdir -p /var/mbmark/projects
	6. ln -s /var/mbmark/projects /var/www/html/projects
	7. chmod 755 /var/mbmark/projects/

### Firewall
- Níže jsou napsány příkazy pro povolení portů ve firewallu. Jedná se o port 80 (Apache) a 8080 (backend aplikace).

	1. /sbin/iptables-save > /etc/network/iptables
	2. nano /etc/network/iptables
		- přidat následující pravidla
		- -A INPUT -m tcp -p tcp --dport 80 -s 147.228.0.0/16 -j ACCEPT
		- -A INPUT -m tcp -p tcp --dport 80 -j DROP
		- -A INPUT -m tcp -p tcp --dport 8080 -s 147.228.0.0/16 -j ACCEPT
		- -A INPUT -m tcp -p tcp --dport 8080 -j DROP
	3. /sbin/iptables-restore < /etc/network/iptables

### Vytvoření adresářů
- Aby měla aplikace kam ukládat logy a získané výsledky, je nezbytné vytvořit složky na disku.

	1. mkdir -p /var/mbmark/logs
	2. mkdir -p /var/mbmark/results
		
## Konfigurace aplikace
- V jednotlivých kapitolách je popsáno, jakým způsobem lze konfigurovat aplikaci.

## Databáze a email
- Ke spouštění backend aplikace je nutné nastavit správné údaje pro připojení k databázi. Databáze musí běžet před spuštěním backend aplikace.
- Aby bylo možné novému zaregistrovanému uživateli poslat informační email, je potřeba nakonfigurovat připojení k SMTP serveru.
- Soubor *microbenchmark-web/src/main/resources/application.properties* obsahuje právě konfiguraci pro připojení k databázi a SMTP serveru.
- Hodnoty s prefixem `spring.datasource` slouží k nastavení připojení k databázi.
- Hodnoty s `spring.mail` nastavují připojení k SMTP serveru.
- Pokud dojde ke změně těchto konfigurací, je nutné aplikaci znovu zkompilovat a spustit.

### Konfigurace pomocí REST API

- Některé konfigurace aplikace se nachází přímo v databázi. Jedná se o konfigurační hodnoty, které se nastavují skrze REST API `/api/property`.
- Provedením požadavku GET `/api/property` administrátor aplikace zjistí, jaké konfigurační hodnoty jsou uloženy v databázi. Pokud konfigurace nabývá hodnoty null, znamená to že konfigurační hodnota není manuálně nastavena aplikace používá výchozí hodnotu.
- Konfigurace `libraries` a `ignoreClasses` se používají pro automatický import knihoven v rámci fáze generování benchmarku. Výchozí hodnoty čte aplikace ze souborů.
- Konfigurace `jmhVersion` a `javaVersion` značí verze JMH a Javy, které se používají pro benchmark. Výchozí hodnota pro JMH je 1.21 a pro Javu 1.8.
- Konfigurace `blindCopyEmail` slouží pro definování emailu, na který mají chodit skryté kopie všech poslaných emailů. Aplikace nemá nastavený žádný výchozí email.
- Poslední `maxMemory` definuje maximální velikost paměti, kterou může běžící benchmark využít. Výchozí hodnota je nastavena na 512 MB.