/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientlog;


import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author feder
 */
public class BottoneLog extends Button implements Loggable {
    public BottoneLog(String contenuto) {
        super(contenuto);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent e) -> (new ClientLogAttivitaXML(this)).start());
    }

    @Override
    public AttivitaXML produciAttivita(TipoAttivita tipo) {
        AttivitaXML a = new AttivitaXML("JLibretto",TipoAttivita.CLICK_BOTTONE,this.getText(),"");
        return a;
    }

}