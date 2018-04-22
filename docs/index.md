# CuroERP Module: Pipeline

**This Module do everything, what you need to transfer Business-Transfer-Objects (Bto's) between Server and Client!**

## What?!

Stell dir vor, du schreibst ein CuroERP-Core Modul. In unserem Beispiel nehmen wir ein MVVMC-Modell (Model, View, ViewModel, Controller). 

### Erklärung

**View**: Der *sichtbare* aufbau der Benutzeroberfläche inkl. sichtbarer, einfacher Logik (Eingabefeld rot, wenn keine Zahl eingegeben wurde) / **einzelnes Objekt**

**ViewModel**: Daten zu einer Entität, hierrauf greift die **View** zurück / **einzelnes Objekt**

**Controller**: Einfache logik, um *ViewModel&lt;=&gt;Model*-Kommunikation und *View-&gt;ViewModel*-Beziehung aufzubauen, dient meist als Einstiegspunkt in einem abgetrennten Modul / **Logik**

**Model/BusinessLayer**: Erhält ungefilterte Daten von der Datenquelle (meist ein SQL-Server oder eine Datei) einer **Entität** und ist für Berechtigung&Validierung zuständig / **Alle Objekte einer Entität**, **Logik**

### Aufbau mit Pipeline

Sobald wir eine Transportschicht in unser Modell bauen, sollten wir auch zentral die ankommenden Daten validieren.

Andernfalls könnte z.B. ein Dritter einen **Login Umgehen**, indem dieser das Ändern/Löschen/Auslesen der Daten am zentralen Punkt ohne Beschränkung ausnutzen könnte! 

#### Aufbau

In unserem Beispiel gibt es exakt 5 Schichten (Absteigend, aus Modul-Sicht):

1. ViewModel
2. <=Controller=>
3. Model
4. <=BusinessLayer=>
5. Datenquelle

Ich habe absichtlich die Kontroll-Schichten mit Pfeilen markiert, da diese keine Daten beinhalten, sondern nur Adapter zwischen den anderen Schichten darstellen.

Logischerweise gehört die Geschäftslogik an eine zentrale Stelle*, sodass wir die Transportschicht erst nach dem **BusinessLayer** implementieren können.

> Theoretisch müssten wir uns mindestens drei weitere Schicht bauen, die unsere Models in *dumme* Objekte (nur Werte, keine Logik) schreibt und von dort aus in die Pipeline schickt. Auf der anderen Seite kommen unsere Models wieder zum Vorschein und werden von dort aus von unseren Controllern weiterverarbeitet.

Da wir jedoch schon mit den Models Objekte haben, die nur eine Entität enthält und keine weitere Login, können wir diese getrost über unsere Pipeline schicken.

Dafür braucht es jedoch noch ein wenig Vorbereitung: Wir sollten uns für unser Projekt (falls nicht schon erledigt) ein Shared-Modul anlegen, welches unsere Client/Server-Module (Siehe Client/Server-Modul?) implementieren.

#### Client/Server-Modul?

Ab dem Moment müssen wir zwischen Client/Server-Modul unterscheiden. Wir haben also nun zwei verschiedene Schichten-Aufbauten:

Client:

1. ViewModel
2. <=Controller=>
3. Model

Server:

1. Model
2. <=BusinessLayer=>
3. Datenquelle

Die Aufteilung bleibt exakt, wie bei dem letzten Modell, nur wird das Model nun von zwei Modulen referenziert.

Theoretisch kennen beide Modelle zusätzlich noch die Pipeline, jedoch ist dies ein Speziallfall und trägt in unserem Modell nur für Verwirrung.

### Implementierung

Die Implementierung sollte uns nicht weiter stören oder gar einschränken. Aus dem Grund habe ich auf die standardmäßige implementierung (get/set/delete) verzichtet und mithilfe von Abstraktion (Shared-Projekt) des BusinessLayer dem Entwickler den nötigen Spielraum gegeben.



## Beispiele

\* Zur Geschäftslogik zählt z.B. ein Login, der schon aus Sicherheitsgründen in einer zentralen Stelle validiert werden sollte.