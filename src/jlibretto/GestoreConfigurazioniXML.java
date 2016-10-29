package jlibretto;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

public class GestoreConfigurazioniXML {
    public static Configurazioni parametriConfigurazione;
    private final String percorsoXML;
    private final String percorsoSchemaXML;
    
    public GestoreConfigurazioniXML(String xml,String xsd) {
        percorsoXML = xml;
        percorsoSchemaXML = xsd;
    }
    
    public static boolean validaConfigurazione(String xml,String xsd) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document documentoConfigurazione = db.parse(new File(xml));
            
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schemaConfigurazione = sf.newSchema(new StreamSource(new File(xsd)));
            
            schemaConfigurazione.newValidator().validate(new DOMSource(documentoConfigurazione));
            return true;
        } catch(SAXException e) {
            System.out.println("Errore di validazione: "+e.getMessage());
            return false;
        } catch(ParserConfigurationException | IOException e) {
            System.out.println("Errore: "+e.getMessage());
            return false;
        }
    }
    
    public boolean caricaConfigurazioni() {
        try {
            if(!validaConfigurazione(percorsoXML,percorsoSchemaXML))
                return false;
            XStream flussoXML = new XStream();
            String inputDaFileXML = new String(Files.readAllBytes(Paths.get(percorsoXML)));
            parametriConfigurazione = (Configurazioni) flussoXML.fromXML(inputDaFileXML);
            return true;
        } catch(Exception e) {
            System.out.println("Impossibile caricare la configurazione: "+e.getMessage());
            return false;
        }     
    }
}
