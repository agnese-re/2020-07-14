/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Team;
import it.polito.tdp.PremierLeague.model.TeamDiff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {
    	this.txtResult.clear();
    	Team teamScelto = this.cmbSquadra.getValue();
    	if(teamScelto != null) {
    		/* *********** */
    		this.txtResult.appendText("SQUADRE MIGLIORI:\n");
    		List<TeamDiff> migliori = this.model.getMigliori(teamScelto);
    		for(TeamDiff td: migliori)
    			txtResult.appendText(td.toString() + "\n");
    		/* ********** */
    		this.txtResult.appendText("\nSQUADRE PEGGIORI:\n");
    		List<TeamDiff> peggiori = this.model.getPeggiori(teamScelto);
    		for(TeamDiff td: peggiori)
    			txtResult.appendText(td.toString() + "\n");
    	} else {
    		txtResult.setText("Seleziona una squadra (s) dal menu' a tendina");
    		return;
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String msg = this.model.creaGrafo();
    	this.cmbSquadra.getItems().addAll(this.model.getAllTeams());
    	this.txtResult.setText(msg);
    	
    	this.txtResult.appendText("\n\n# Componenti connesse: " + this.model.getNComponentiConnesse());
    }

    @FXML
    void doSimula(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
