# Auf windows zum Laufen bekommen

Es wird davon ausgegangen, dass eine Laufende JavaEditor Konfiguration vor handen ist.
Getestet und entwickelt wurde mit der Java OpenJDK - 21 LTS

- getestet auf Lenovo Thinkpad L13 Zoga Intel i3 Windos 11 amd64

1. Projekt herunterladen und ggf. entpacken (Falls als zip heruntergeladen)
2. JavaEditor öffnen
3. Folgende datei im JE öffnen: { Projekt ordner }/src/main/java/game/App.java
4. Benötigte Einstellungen setzen
5. Compelieren
6. Ausführen


# Benötigte Einstellungen:

Die Einstellungen sind unter: *Window -> Configuration*; zu finden

Unter: *Java -> Interpreter -> Interpreter -> Parameter*
Folgendes den Root-Ordner des Projektes als *user.dir* angeben:

```
-Duser.dir=C:\Users\{Benutzer}\{Pfad zum Projekt}
```

Es muss der gesammte, abselute Pfad zu dem Projektordner angegeben werden.

Dann unter: *Java -> Interpreter -> Classpath user*

auf *Edit* klicken, in dem neuen kleinen Fenster nun auf *New folder* klicken
und dann in den ordner "lib" in dem Projektordner auswählen und hinzufügen.

Nach dem Hinzufügen sollte der Pfad in dem kleinen Fenster angezeigt werden und
die blaue Checkbox links daneben sollte ebenfallst ausgewählt sein.

!!!Wichtig: Es muss in dem Fenster auf den Pfad gecklickt werden, um unten
links en dem Fenster die Option "all jar files" zu aktivieren. Die ist
notwendig, damit *alle* ".jar" Datein mit eingebunden werden.
