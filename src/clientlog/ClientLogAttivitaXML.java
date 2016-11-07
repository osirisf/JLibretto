/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlog;
import configurazione.ConfigurazioniNonDisponibiliException;
import java.io.DataOutputStream;
import java.net.Socket;
import configurazione.GestoreConfigurazioniXML;

/**
 *
 * @author feder
 */
public class ClientLogAttivitaXML extends Thread {
    AttivitaXML attivita;
    Socket socketInvioXML;
    public ClientLogAttivitaXML(Loggable l,TipoAttivita a) {
        attivita = l.produciAttivita(a);
    }
    public ClientLogAttivitaXML(Loggable l) {
        attivita = l.produciAttivita(null);
    }
    @Override
    public void run() {
        
        try {
            String indirizzoServerLog;
            Integer portaServerLog;
            
            indirizzoServerLog = GestoreConfigurazioniXML.getIstanza().getIPServerLog();
            portaServerLog = GestoreConfigurazioniXML.getIstanza().getPortaServerLog();
            System.out.println("Tentativo di connessione: "+indirizzoServerLog+" "+portaServerLog);  
            
            socketInvioXML = new Socket(indirizzoServerLog,portaServerLog);
            System.out.println("Socket connesso: "+socketInvioXML.getInetAddress()+" "+socketInvioXML.getPort());
            
            DataOutputStream streamUscitaAlServer = new DataOutputStream(socketInvioXML.getOutputStream());
            attivita.indirizzoIPClient = socketInvioXML.getLocalAddress().toString();
            streamUscitaAlServer.writeUTF(attivita.serializzaInXML());
        } catch(ConfigurazioniNonDisponibiliException exx) {
            System.out.println("Errore nel caricamento della configurazione");
        } 
        catch(Exception e) {
            System.out.println("Errore di connessione al server di log: "+e.getMessage());
        }
    }
    
}
