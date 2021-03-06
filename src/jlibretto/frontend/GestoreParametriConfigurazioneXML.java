////////////////////////
package jlibretto.frontend;
import jlibretto.CaricatoreValidatoreXML;

public class GestoreParametriConfigurazioneXML {
    private static ParametriConfigurazione c; //(1)
    public static ParametriConfigurazione ottieniParametriConfigurazione() {
        if(c == null) {
            c = caricaParametriConfigurazioneDaXML("../../conf/configurazioni.xml","../../conf/configurazioni.xsd");
        }
        return c;
    }
    private static ParametriConfigurazione caricaParametriConfigurazioneDaXML(String percorsoXML,String percorsoSchemaXML) {
        CaricatoreValidatoreXML cvx = new CaricatoreValidatoreXML(percorsoXML,percorsoSchemaXML);
        cvx.getStreamXML().alias("Configurazioni", jlibretto.frontend.ParametriConfigurazione.class);
        cvx.getStreamXML().useAttributeFor(jlibretto.frontend.ParametriConfigurazione.class, "TipoMedia");
        cvx.getStreamXML().useAttributeFor(jlibretto.frontend.ParametriConfigurazione.class, "ValoreLode");        
        return (ParametriConfigurazione)cvx.caricaOggettoDaXML();
    }

}

// (1): L'oggetto è privato in quanto si vuole gestirlo SOLAMENTE attraverso
//      l'interfaccia offerta dalla classe => ottieniParametriConfigurazione