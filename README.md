<h1 align="center">Guru Discord Bot</h1>
<div align="center">
    <strong>
        Szerveren lévő jogosultságok/skillek kezelő botja a <a href="https://discord.gg/A5yC5Hhxrs">Step into Community</a> Discord szerveren.
    </strong>
</div>
<br/>
<div align="center">

[![Discord](https://img.shields.io/discord/848942109191962665.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/A5yC5Hhxrs)


</div>
<br/>

## Funkcionalitás

- Csatlakozó felhasználók üdvözlése, ami után reakciók segítségével tudják magukhoz rendelni a szerepköröket
- A bot által nyújtott parancsok/súgó lekérése (`/help`)
- Szerepkörök/skillek listázása a szerveren (`/list-skills`)
- Szerepkörök felhaszálóhoz rendelése (`/i-know`)
- Szerepkörök törlése/visszautasítása (`/i-dont-know`)

## Jellemzők

- **Nincs perzisztencia** - A bot nem tartja számon, hogy melyik felhasználó melyik szerepkörrel rendelkezik, ezek lekérésére a Discord API biztosít metódusokat, de jövőben elképzelhető, hogy egy perzisztens réteg is be lesz iktatva.
- **Slash parancsok natív támogatásának hiánya** - A Javacord könyvtár !!egyelőre!! nem támogatja a natív `slash-command`-okat, de az új verziók valamelyikében elképzelhető, hogy elérhető lesz, emiatt a parancsok nem jelenekk meg egy `/` billentyű leütése után.

## Fordítás és futtatás

A projekt [Quarkus](https://quarkus.io) keretrendszerre épül és a [Javacord](https://javacord.org/) könyvtárat használja a Discord API által nyújtott funkciók kihasználására.

A fordításhoz a következőkre van szükség:

  * Java 11 vagy afölötti verzió.

Ezután a tényleges fordítás (JAR összeállítása):

~~~~
$ ./mvnw package
~~~~

Majd pedig az elkészült JAR futtatása:

~~~~
$ java -jar ./target/quarkus-app/quarkus-run.jar
~~~~

## Konfiguráció

> [BotConfiguration.java](src/main/java/hu/stepintomeetups/configuration/BotConfiguration.java)

Az alkalmazást az application.properties fájlban lévő kulcsok segítségével konfigurálható be, viszont van módunk rá, hogy környezeti változókkal tegyük meg ezt:

  * `GURU_BOT_TOKEN`
    * A bot számára generált token.

### Skillek/szerepkörök konfigurálása

A bot képes dinamikusan bővíteni a skilljeinek/szerepköreinek a listáját, ahhoz, hogy egy új szerepkört fel tudjunk venni, a következő paramétereket kell beállítanunk neki:
- `name` - a skill neve
- `role-association` - a telepítendő szerveren a szerepkör/jogosultság neve
- `emoji` - ha a szerveren található az adott skill-hez egyedi (vagy akár sima beépített) emoji akkor itt beállítható (pl.: `:java:853025519188181002`)

Példa kettő darab skill/szerepkör konfigurációjához:  
A konfiguráció felépítése a következő:
- `guru-bot.skills.<skill_neve>.<attribútum>`
> application.properties  
```properties
guru-bot.skills.java.name=Java
guru-bot.skills.java.role-association=java
guru-bot.skills.java.emoji=:java:853025519188181002
guru-bot.skills.javascript.name=Javascript
guru-bot.skills.javascript.role-association=javascript
guru-bot.skills.javascript.emoji=:javascript:853025658284539944
```

Bármennyi skill/szerepkör objektum konfigurálható így.  
Sajnos a szerepkörök nem konfigurálhatóak környezeti változókkal.

###### YAML alapú konfigurációra is van lehetőség, viszont a Quarkus jelenlegi verziójánál előfordulhatnak inkompatibilitási hibák.

### Parancsok konfigurálása

### Üzenetek konfigurálása
A bot által küldött üzenetek törzse dinamikusan konfigurálható.
Ehhez az `application.properties/.env/$pwd/config/application.properties` fájlok valamelyikében kell a következő konifurációkat megtenni:  
A konfiguráció felépítése a következő (formázott szövegek esetén):
- `guru-bot.embed-messages.<üzenet_kulcsa>.<attribútum>`
> application.properties  
```properties
guru-bot.embed-messages.welcome.title=Üdvözlünk a Step Into Meetup szerverén  
guru-bot.embed-messages.welcome.description=Ahhoz, hogy a maximumot tudd kihozni a szereveren eltöltött idődből, kérlek olvasd végig a következőket és teljesítsd az első kihívásodat.  
guru-bot.embed-messages.welcome.image=https://avatars.githubusercontent.com/u/43297388?s=200&v=4  
guru-bot.embed-messages.welcome.fields[0].name=Programozási nyelv ismeretek  
guru-bot.embed-messages.welcome.fields[0].message=Kérlek az emoticonok segítségével válaszd ki az általad ismert vagy ismerni kívánt nyelveket, hogy felruházhassunk a hozzájuk tartozó szerepkörökkel  
guru-bot.embed-messages.welcome.footer=https://stepintomeetups.hu
```  

A konfiguráció felépítése a következő (sime szövegek esetén):
- `guru-bot.messages.<üzenet_kulcsa>.<attribútum>`
> application.properties  
```properties
guru-bot.messages.unknown-role=Ismerelten **{0}** skill a szerveren, kérlek tudasd a hibát a moderátorokkal, hogy minél hamarabb javíthassák a hibát.  
```  

### Jogosultságok  

A bot működéséhez szükséges jogok:
- Szerepkörök karbantartása (`MANAGE_ROLES`)
- Csatornák karbantartása (`MANAGE_CHANNELS`)
- Emojik karbantartása (`MANAGE_EMOJIS`)
- Üzenetek küldése (`SEND_MESSAGES`)
- Üzenetek karbantartása (`MANAGE_MESSAGES`)
- Linkek csatolása (`EMBED_LINKS`)
- Fájlok csatolása (`ATTACH_FILE`)
- Külső emojik használata (`USE_EXTERNAL_EMOJIS`)
- Reakciók hozzáadása üzenetekhez (`ADD_REACTIONS`)

Ezen jogosultságok kódja: `1342502992`

## Licenc
Az alkalmazásra az [MIT License](LICENSE) vonatkozik.
