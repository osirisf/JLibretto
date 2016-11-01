package jlibretto;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
public class TabellaEsami extends TableView {
    
        TableColumn colonnaNome = new TableColumn("Nome esame");
        TableColumn colonnaCrediti = new TableColumn("Crediti esame");
        TableColumn colonnaVoti = new TableColumn("Valutazione esame");
        TableColumn colonnaData = new TableColumn("Data esame");
        TableColumn colonnaElimina = new TableColumn("Azione");
        public TabellaEsami() {
            setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colonnaCrediti.setCellValueFactory(new PropertyValueFactory<>("crediti"));
            colonnaVoti.setCellValueFactory(new PropertyValueFactory<>("valutazione"));
            colonnaData.setCellValueFactory(new PropertyValueFactory<>("data"));

            impostaFormatoModificaCelle();
            impostaCompletamentoModificaCelle();
            impostaEliminazione();
            colonnaNome.setSortable(false);
            colonnaCrediti.setSortable(false);
            colonnaVoti.setSortable(false);            
            colonnaData.setSortable(false);
            setItems(RisorsaListaEsami.getIstanza().getListaEsami());
            setEditable(true);
            getColumns().addAll(colonnaNome,colonnaCrediti,colonnaVoti,colonnaData,colonnaElimina);
        }

        private void impostaFormatoModificaCelle() {
            colonnaNome.setCellFactory(TextFieldTableCell.forTableColumn());
            colonnaCrediti.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));            
            colonnaVoti.setCellFactory(ComboBoxTableCell.forTableColumn(Esame.listaVotiStandard));            
            colonnaData.setCellFactory(TextFieldTableCell.forTableColumn());
            colonnaElimina.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList("Elimina")));
        }
        
        private void completaModifica(Esame e,int rowIndex) {
            boolean result = GestoreArchiviazioneEsami.getIstanza().modificaEsame(e);
            if(result)
                RisorsaListaEsami.getIstanza().notificaCambiamentoEsame(rowIndex);
        }
        
        private void impostaEliminazione() {
            colonnaElimina.setOnEditCommit(new EventHandler<CellEditEvent<Esame, String>>() {
                    @Override
                    public void handle(CellEditEvent<Esame, String> t) {
                        Esame daRimuovere = ottieniElementoModificato(t);
                        boolean risultato = GestoreArchiviazioneEsami.getIstanza().rimuoviEsame(daRimuovere.getId());
                        if(risultato) {
                            RisorsaListaEsami.getIstanza().getListaEsami().remove(ottieniRigaCella(t));
                        }
                    }
            });
        }
        
        private int ottieniRigaCella(CellEditEvent<Esame, ?> cella) {
            return cella.getTablePosition().getRow();
        }
        
        private Esame ottieniElementoModificato(CellEditEvent<Esame, ?> cellaModificata) {
            int indice = ottieniRigaCella(cellaModificata);
            return RisorsaListaEsami.getIstanza().prelevaEsame(indice);
        }

        private void impostaCompletamentoModificaCelle() {
            colonnaNome.setOnEditCommit(new EventHandler<CellEditEvent<Esame, String>>() {
                    @Override
                    public void handle(CellEditEvent<Esame, String> t) {
                        Esame edited = ottieniElementoModificato(t);
                        edited.setNome(t.getNewValue());
                        completaModifica(edited,ottieniRigaCella(t));
                    }
                }
            );
            colonnaVoti.setOnEditCommit(new EventHandler<CellEditEvent<Esame, Integer>>() {
                    @Override
                    public void handle(CellEditEvent<Esame, Integer> t) {
                        Esame edited = ottieniElementoModificato(t);
                        edited.setValutazione(t.getNewValue());
                        completaModifica(edited,ottieniRigaCella(t));
                    }
                }
            );
            colonnaCrediti.setOnEditCommit(new EventHandler<CellEditEvent<Esame, Long>>() {
                    @Override
                    public void handle(CellEditEvent<Esame, Long> t) {
                        Esame edited = ottieniElementoModificato(t);
                        edited.setCrediti(t.getNewValue().intValue());                        
                        completaModifica(edited,ottieniRigaCella(t));
                    }
                }
            );
            colonnaData.setOnEditCommit(new EventHandler<CellEditEvent<Esame, String>>() {
                    @Override
                    public void handle(CellEditEvent<Esame, String> t) {
                        Esame edited = ottieniElementoModificato(t);                                              
                        edited.setData(t.getNewValue());   
                        completaModifica(edited,ottieniRigaCella(t));
                    }
                }
            ); 

        }
}
