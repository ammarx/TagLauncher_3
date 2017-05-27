/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mathew
 */
public class UserOptionsController implements Initializable {

    @FXML
    private Button optionsExit;
    @FXML
    private Button optionsClose;
    @FXML
    private RadioButton optionsKeepLauncherOpen;
    @FXML
    private RadioButton optionDisableAutoUpdates;
    @FXML
    private RadioButton optionsResolution;
    @FXML
    private RadioButton optionsRamAllocation;
    @FXML
    private RadioButton optionsBypassBlacklist;
    @FXML
    private TextField optionsResolutionMin;
    @FXML
    private TextField optionsRamAllocationMin;
    @FXML
    private TextField optionsResolutionMax;
    @FXML
    private TextField optionsRamAllocationMax;
    @FXML
    private ComboBox<?> optionsSelectVersion;
    @FXML
    private Button optionsSelectVersionInstall;
    @FXML
    private RadioButton optionsSelectVersionForce;
    @FXML
    private RadioButton optionsJavaVersion;
    @FXML
    private RadioButton optionsJVMArguments;
    @FXML
    private TextField optionsJavaVersionInput;
    @FXML
    private TextField optionsJVMArgumentsInput;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void _optionsClose(ActionEvent event) {
        Stage stage = Controller.getApplicationOptionStage();
        stage.close();
    }

    @FXML
    private void _optionsExit(ActionEvent event) {
        Stage stage = Controller.getApplicationOptionStage();
        stage.close();
    }

    @FXML
    private void _optionsResolution(ActionEvent event) {
        if (optionsResolution.isSelected())
        {
            optionsResolutionMin.setDisable(false);
            optionsResolutionMax.setDisable(false);
        }
        else
        {
            optionsResolutionMin.setDisable(true);
            optionsResolutionMax.setDisable(true);
        }
    }

    @FXML
    private void _optionsRamAllocation(ActionEvent event) {
        if (optionsRamAllocation.isSelected())
        {
            optionsRamAllocationMin.setDisable(false);
            optionsRamAllocationMax.setDisable(false);
        }
        else
        {
            optionsRamAllocationMin.setDisable(true);
            optionsRamAllocationMax.setDisable(true);
        }
    }
    
    @FXML
    private void _optionsJavaVersion(ActionEvent event) {   
        if (optionsJavaVersion.isSelected())
        {
            optionsJavaVersionInput.setDisable(false);
        }
        else
        {
            optionsJavaVersionInput.setDisable(true);
        }
    }

    @FXML
    private void _optionsJVMArguments(ActionEvent event) {
        if (optionsJVMArguments.isSelected())
        {
            optionsJVMArgumentsInput.setDisable(false);
        }
        else
        {
            optionsJVMArgumentsInput.setDisable(true);
        }
    }


    
}
