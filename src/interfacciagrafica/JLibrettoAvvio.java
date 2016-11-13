package interfacciagrafica;

import clientlog.*;
import configurazione.*;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class JLibrettoAvvio extends Application implements Loggable {
    FormInserimentoEsame formEsami;
    static Font fontGrassetto = Font.font(Font.getDefault().getFamily(),FontWeight.BOLD,Font.getDefault().getSize());
    static Font fontGrande = Font.font(Font.getDefault().getFamily(),FontWeight.BOLD,Font.getDefault().getSize()+3);

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
        TabellaEsami tabellaEsami = new TabellaEsami();
        HBox hb = new HBox();
        formEsami = costruisciFormInserimentoEsame();
        formEsami.setVgap(5);
        formEsami.setHgap(5);
        formEsami.setAlignment(Pos.CENTER);

        GraficoMediaEsami graficoMediaMobile = creaGraficoEsami();
        HBox.setHgrow(formEsami,Priority.ALWAYS);
        HBox.setHgrow(graficoMediaMobile,Priority.ALWAYS);
        hb.getChildren().addAll(formEsami,graficoMediaMobile);
        VBox.setVgrow(hb, Priority.ALWAYS);
        VBox.setVgrow(tabellaEsami, Priority.ALWAYS);
        vb.getChildren().addAll(tabellaEsami,hb);
        return vb;
    }
    
    private GraficoMediaEsami creaGraficoEsami() {
        NumberAxis na = new NumberAxis();
        na.setLowerBound(18);
        na.setUpperBound(33);
        String tipoMedia = "";
        try {
            tipoMedia = GestoreConfigurazioniXML.getIstanza().getTipoMedia();
        } catch(ConfigurazioniNonDisponibiliException e) {
            System.out.println("Errore nel caricamento delle configurazioni");
            Platform.exit();
            System.exit(1);
        }
        switch(tipoMedia) {
            case "aritmetica": return new GraficoMediaAritmetica(na);
            case "ponderata": return new GraficoMediaPonderata(na);
            default: return new GraficoMediaAritmetica(na);
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
