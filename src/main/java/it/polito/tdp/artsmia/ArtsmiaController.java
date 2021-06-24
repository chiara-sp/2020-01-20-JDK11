package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	if(!model.grafoCreato()) {
    		txtResult.appendText("prima creare il grafo");
    	}
    	String ruolo= boxRuolo.getValue();
    	if(ruolo==null) {
    		txtResult.appendText("selezioanre un ruolo");
    		return;
    	}
    	txtResult.appendText("Calcola artisti connessi\n\n");
    	List<Adiacenza> adiacenze= model.getAdiacenze(ruolo);
    	for(Adiacenza a: adiacenze) {
    		txtResult.appendText(a.getA1()+ " - "+a.getA2()+ " "+a.getPeso()+ "\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String ruolo= boxRuolo.getValue();
    	if(ruolo==null) {
    		txtResult.appendText("selezioanre un ruolo");
    		return;
    	}
    	txtResult.appendText("Creato grafo\n");
    	
    	model.creaGrafo(ruolo);
    	txtResult.appendText("#vertici: "+model.numVertici()+"\n");
    	txtResult.appendText("#archi: "+model.numArchi()+"\n");
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
