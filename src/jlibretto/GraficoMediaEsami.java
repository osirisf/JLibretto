package jlibretto;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

class GraficoMediaEsami extends LineChart {
    
    public GraficoMediaEsami(NumberAxis n) {
        super(new CategoryAxis(),n);
        setLegendVisible(false);
        setTitle("Average Plot");
        RisorsaListaEsami.getInstance().getExams().addListener((ListChangeListener.Change<? extends Esame> c) -> {
            Double avg = update((ObservableList<Esame>) c.getList());
            setTitle("Current average "+avg);
            
        });
        setAnimated(false);
    }
    
    public double update(ObservableList<Esame> exams) {
        Double sum = 0.0;
        Integer count = 0;
        Double tmp = 0.0;
        Series<String,Double> mobileAvg;        
        mobileAvg = new Series<>();
        for(Esame e:exams) {
            sum+=e.getValutazione();
            count++;
            tmp = sum/count;
            mobileAvg.getData().add(new XYChart.Data(e.getNome(),tmp));
        }
        setData(FXCollections.observableArrayList(mobileAvg));
        return tmp;
    }
 }
