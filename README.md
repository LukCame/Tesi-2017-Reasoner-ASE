# Tesi-2017-Reasoner-ASE

<h2>Installazione dell’applicazione</h2>

<b>Pre-Condizione:</b> avere installato nel proprio pc Java Platform(JDK).<br><br>
Le attività da svolgere per poter installare e utilizzare l’applicazione sull’ambiente di collaudo sono:
<ol>
  <li>	Soluzione mediante l’utilizzo dell’IDE Netbeans:</li><br>
  <ul>
    <li>Installare NetBeansIDE versione Java EE al seguente indirizzo http://netbeans.org/downloads/index.html.</li>
    <li>Seguire la procedura guidata di installazione selezionando Tomcat come application server.</li>
    <li>Se non già fatto in precedenza installare l’Application Server “Tomcat” tramite NetBeansIDE: “Strumenti -> Servers -> Add Server..”</li>
    <li>Scaricare il progetto reasoner ASE al seguente indirizzo http://github.com/LukCame/Tesi-2017-Reasoner-ASE selezionato la voce “Clone or dowload”.</li>
    <li>Aprire il progetto tramite NetBeanIDE: “File  Open Project…”</li>
    <li>Eseguire una build del progetto cliccando con il tasto destro sul progetto” ASE” e selezionando la voce “build”.</li>
    <li>Cliccare con il tasto destro sul progetto “ASE” e selezionare la voce “Run”</li><br>
  </ul>
<li>Soluzione mediante uso del file .war (Windows):</li><br>
  <ul>
    <li>Scaricare l’application server Tomcat versione 8 al seguente indirizzo http://tomcat.apache.org/download-80.cgi selezionando l’estensione zip per la propria versione del sistema operativo(32-64-bit).</li>
    <li>Estrarre il file contenente Tomcat e inserirlo in una locazione all’interno del file system.</li>
    <li>Impostare le variabili JAVA_HOME e CATALINA_HOME come variabili d’ambiente.</li>
    <li>Scaricare il file .war nominato “ASE” al seguente indirizzo http://github.com/LukCame/Tesi-2017-Reasoner-ASE.</li>
    <li>Inserire il file .war all’interno della cartella “webapps” di Tomcat.</li>
    <li>Aprire il prompt dei comandi e posizionarsi all’interno della cartella bin di Tomcat.</li>
    <li>Avviare Tomcat digitando nel prompt la stringa “startup.bat”</li>
   <li>Aprire il browser e scrivere il seguente url nella barra degli indirizzi localhost:8080/ASE.</li>
  </ul>
</ol>
<h2>Prova dell'applicazione</h2>

<ul>
  <li> Una volta avviata l'applicazione cliccare sul pulsante Get Started Now Reasoning</li>
  <li> Caricare l'ontologia "ontologia.ttl" situata al seguente indirizzo https://github.com/LukCame/Tesi-2017-Reasoner-ASE/tree/master/Test </li>
  <li> Caricare il file delle regole di inferenza "regoleOnt.txt" situato al seguente indirizzo https://github.com/LukCame/Tesi-2017-Reasoner-ASE/tree/master/Test </li>
  <li> Spuntare le checkbox "unset iteration" e "use all rules" </li>
</ul>
