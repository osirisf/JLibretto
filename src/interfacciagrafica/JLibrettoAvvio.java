package interfacciagrafica;

import clientlog.*;
import configurazione.*;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class JLibrettoAvvio extends Application implements Loggable {
    FormInserimentoEsame formEsami;
    GraficoMediaEsami graficoMediaMobileEsami;
    TabellaEsami tabellaEsami;

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPanel = new BorderPane();
        VBox examsContentPanel = costruisciPannelloEsamiPrincipale();
        mainPanel.setCenter(examsContentPanel);
        StackPane root = new StackPane();
        root.getChildren().add(mainPanel);
        Scene scene = new Scene(root, 800, 600);
        impostaAzioniChiusuraApplicazione(primaryStage);
        primaryStage.setTitle("JLibretto");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        
        System.out.println("Caricamento contenuto form da cache");
        FormCache.caricaDaCache(formEsami);
        (new ClientLogAttivitaXML(this,TipoAttivita.AVVIO_APPLICAZIONE)).start();
    }
    
    private void impostaAzioniChiusuraApplicazione(Stage stage) {
        stage.setOnCloseRequest((WindowEvent we) -> {
           System.out.println("In fase di chiusura, salvataggio in cache del form.");
           FormCache.salvaInCache(formEsami);
           System.out.println("Salvataggio completato.");
            (new ClientLogAttivitaXML(this,TipoAttivita.CHIUSURA_APPLICAZIONE)).start();
        });
    }

    private VBox costruisciPannelloEsamiPrincipale() {
        VBox vb = new VBox();
        tabellaEsami = new TabellaEsami();
        HBox hb = new HBox();
        formEsami = costruisciFormInserimentoEsame();
        formEsami.setVgap(5);
        formEsami.setHgap(5);
        formEsami.setAlignment(Pos.CENTER);

        graficoMediaMobileEsami = creaGraficoEsami();
        HBox.setHgrow(formEsami,Priority.ALWAYS);
        HBox.setHgrow(graficoMediaMobileEsami,Priority.ALWAYS);
        hb.getChildren().addAll(formEsami,graficoMediaMobileEsami);
        VBox.setVgrow(hb, Priority.ALWAYS);
        VBox.setVgrow(tabellaEsami, Priority.ALWAYS);
        vb.getChildren().addAll(tabellaEsami,hb);
        return vb;
    }
    
    private GraficoMediaEsami creaGraficoEsami() {
        String tipoMedia = "";
        try {
            tipoMedia = GestoreConfigurazioniXML.getIstanza().getTipoMedia();
        } catch(ConfigurazioniNonDisponibiliException e) {
            System.out.println("Errore nel caricamento delle configurazioni");
            Platform.exit();
            System.exit(1);
        }
        switch(tipoMedia) {
            case "aritmetica": return new GraficoMediaAritmetica();
            case "ponderata": return new GraficoMediaPonderata();
            default: return new GraficoMediaAritmetica();
        }
    }
    
    private FormInserimentoEsame costruisciFormInserimentoEsame() {
        FormInserimentoEsame gp = new FormInserimentoEsame();
        return gp;
    }

    public static void main(String[] args) {
        System.out.println("Avvio applicazione...");
        launch(args);
        
    }

    @Override
    public AttivitaXML produciAttivita(TipoAttivita tipo) {
        AttivitaXML attivita = new AttivitaXML("JLibretto",tipo,null,"");
        return attivita;
    }

}