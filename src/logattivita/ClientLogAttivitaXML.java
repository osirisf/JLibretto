package logattivita;
import java.io.DataOutputStream;
import java.net.Socket;
import configurazione.GestoreConfigurazioniXML;

public class ClientLogAttivitaXML extends Thread {
    private final AttivitaXML attivita;
    private ClientLogAttivitaXML(AttivitaXML a) {
        attivita = a;
    }
    @Override
    public void run() {
        String indirizzoServerLog;
        Integer portaServerLog;
        try {
            indirizzoServerLog = GestoreConfigurazioniXML.ottieni().IPServerLog;
            portaServerLog = GestoreConfigurazioniXML.ottieni().PortaServerLog;
            attivita.indirizzoIPClient = GestoreConfigurazioniXML.ottieni().IPClient;
        } catch(Exception e) {
            System.out.println("Impossibile configurare il client.");
            return;
        }
        
        try (
            Socket socketInvioXML = new Socket(indirizzoServerLog,portaServerLog);
            DataOutputStream streamUscitaAlServer = new DataOutputStream(socketInvioXML.getOutputStream());
        ) {
            System.out.println("Connesso, invio XML.");
            attivita.indirizzoIPClient = socketInvioXML.getLocalAddress().toString();
            streamUscitaAlServer.writeUTF(attivita.serializzaInXML());
            System.out.println("Invio completato.");
        } catch(Exception e) {
            System.out.println("Errore di connessione al server di log");
        }
    }
    
    private static void inviaLogClickComponente(String nomeApplicazione,String nomeComponente,TipoAttivita t) {
        AttivitaXML a = new AttivitaXML(nomeApplicazione,t,nomeComponente,"");
        (new ClientLogAttivitaXML(a)).start();
    }
    
    public static void inviaLogClickBottone(String nomeApplicazione,String nomeComponente) {
        inviaLogClickComponente(nomeApplicazione,nomeComponente,TipoAttivita.CLICK_BOTTONE);
    }
    
    public static void inviaLogClickTabella(String nomeApplicazione,String nomeComponente) {
        inviaLogClickComponente(nomeApplicazione,nomeComponente,TipoAttivita.CLICK_TABELLA);
    }
    
    public static void inviaLogEventoApplicazione(String nomeApplicazione,int chiusura) {
        TipoAttivita t = (chiusura==1)?TipoAttivita.CHIUSURA_APPLICAZIONE:TipoAttivita.AVVIO_APPLICAZIONE;
        AttivitaXML a = new AttivitaXML(nomeApplicazione,t,null,"");
        (new ClientLogAttivitaXML(a)).start();
    }
}
