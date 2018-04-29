# Pipeline business protocol

Short: **pbp**

## why

Über das **pbp** soll die vollständige Pipeline (alle Aufrufe der Business-Schnittstellen und Rückgabe-Typen) mit dem textbasierten TCP-Protokoll über die Java-Sockets lösen.

Überall, wo wir Daten als Text senden, macht es Sin für die einfache Lesbarkeit und mitlerweile schnellen Verarbeitung (z.B. Jackson) auf JSON zu setzen. Hiermit beschäftigt sich jedoch erst die nächste Instanz (Pipeline-Layer)!

Das **pbp** ist absichtlich ist sehr Schmal gehalten, da es hier eher um die Synchronität und das gleichmäßige Verarbeiten von Daten geht.

## Aufbau

```
#START;<ID>;<TYPE>;<HASH OF CONTENT>;
<CONTENT (ML)>
#END;<ID>;<TYPE>;<HASH OF CONTENT>;
```

Das **pbp** unterstüzt keine asynchrone verarbeitung von Anfragen und Ergebnissen, das heißt, es kann nur eine Anfrage oder Ergebnis gleichzeitig über eine Verbindung empfangen und gesendet werden, wie es auch bei anderen Textbasierten Protokollen (wie z.B. das HTTP) üblich ist.

Üblicherweise schließt der Server oder Client die **pbp**-Verbindung nur explizit und kann nach belieben aufrecht erhalten werden. Bei der implementation on der CuroERP-Pipeline werden die Verbindungen 30 Sekunden nach dem letzten Kontakt geschlossen. Das die Verbindung bei einem Timeout einer Anfrage abgebrochen wird, macht keinen Sinn, da üblicherweise die Anfragen vom Client kommen und der Server so nicht wissen kann, ob eine Anfrage zu lange in der Queue hängt.

 

 