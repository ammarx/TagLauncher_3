/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.event.DocumentEvent;

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
    private ComboBox optionsSelectVersion;
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

    Hashtable<String, String> VersionHashTable = new Hashtable<String, String>();
    @FXML
    private Label optionStatus;
    @FXML
    private RadioButton optionsDebugMode;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadOptionsData();
        
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();
        ExecutorService executor1 = Executors.newCachedThreadPool();
        executor1.submit(() -> {
            if (API.getUpdateStatus().equals("0"))
            {
                System.out.println("You are running the latest API version");
            } else {
                System.out.println("You are " + API.getUpdateStatus() + " versions behind");
            }
            return null;
        });
        executor1.shutdown();
        
        
        for (Object ob : API.getInstalledVersionsList()) {
            //AMMAR version.getItems().addAll(ob.toString());

        }
        //step 1 load files from mojang servers
        //load data on different thread.
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                optionStatus.setText("Status: Getting latest versions");
                optionsSelectVersion.setDisable(true);
                optionsSelectVersionInstall.setDisable(true);
                optionsExit.setDisable(true);
                optionsClose.setDisable(true);
                //installGame.setDisable(true);
                //forceInst.setDisable(true);
                API.downloadVersionManifest();
                
                for (Object ob : API.getInstallableVersionsList()) {
                    String[] prsntAry = ob.toString().split(" % ");
                    optionsSelectVersion.getItems().addAll(prsntAry[0]);
                    VersionHashTable.put(prsntAry[0], prsntAry[1]);
                    
                }
                //step 2 read files from local system
                if (!API.getInstalledVersionsList().isEmpty()) {
                    
                    for (Object ob_ : API.getInstalledVersionsList()) {
                        if (!VersionHashTable.containsKey((String) ob_)) {
                            optionsSelectVersion.getItems().addAll(ob_);
                            VersionHashTable.put((String) ob_, "Unknown");
                        }
                    }
                    
                }

                optionsSelectVersion.setDisable(false);
                optionsSelectVersionInstall.setDisable(false);
                optionsExit.setDisable(false);
                optionsClose.setDisable(false);
                try {
                    Platform.runLater(() -> {
                        optionStatus.setText("Status: Idle");
                    });
                }catch (Exception e) {}
                
                
                //installGame.setDisable(false);
                //forceInst.setDisable(false);
                
                return null;
            }
        });       
        executor.shutdown();
    }    

    @FXML
    private void _optionsClose(ActionEvent event) {
        saveOptionsData();
        Stage stage = Controller.getApplicationOptionStage();
        stage.close();   
    }

    @FXML
    private void _optionsExit(ActionEvent event) {
        saveOptionsData();
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

    @FXML
    private void _optionsKeepLauncherOpen(ActionEvent event) {
    }

    @FXML
    private void _optionDisableAutoUpdates(ActionEvent event) {
    }

    @FXML
    private void _optionsBypassBlacklist(ActionEvent event) {
    }

    @FXML
    private void _optionsSelectVersion(ActionEvent event) {
        try {
            //versionType.setText(VersionHashTable.get((String) installversionList.getValue()));
            //System.out.println(VersionHashTable.get((String) installversionList.getValue()));
        } catch (Exception e) {
            //System.out.println("abc");
            //versionType.setText("Unknown");

        }
    }

    @FXML
    private void _optionsSelectVersionInstall(ActionEvent event) {
        //step 1 check if forceinstall is enabled or not
        //launch.setDisable(true);
        //installGame.setDisable(true);
        
        //Can't access the main GUI when options is open. So only need to disable the close buttons + install.
        optionsSelectVersionInstall.setDisable(true);
        optionsExit.setDisable(true);
        optionsClose.setDisable(true); 
        optionsSelectVersion.setDisable(true);

        if (optionsSelectVersionForce.isSelected()) {
            System.out.println("Selected!");
        } else {
            System.out.println("NOT Selected!");

        }
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            API.downloadMinecraft((String) optionsSelectVersion.getValue(), optionsSelectVersionForce.isSelected());
            return null;
        });
        executor.shutdown();

        Thread t = new Thread(() -> {
            //boolean stop = true;
            while (true) {
                try {

                    Platform.runLater(() -> {
                        
                        if (LauncherOptions.showDebugStatus == true)
                        {
                            optionStatus.setText(API.getLog());
                        }
                        else
                        {
                            if (API.getLog().startsWith("[dl] URL: https://launchermeta"))
                            {
                                optionStatus.setText("Status: " + LauncherOptions.Status.DOWNLOADING_LM);
                            }
                            if (API.getLog().startsWith("[dl] DOWNLOADING...HASH:"))
                            {
                                optionStatus.setText("Status: " + LauncherOptions.Status.DOWNLOADING);
                            }
                            if (API.getLog().startsWith("[dl] DOWNLOADING MINECRAFT JAR"))
                            {
                                optionStatus.setText("Status: " + LauncherOptions.Status.DOWNLOADING_M);
                            }
                            if (API.getLog().startsWith("[dl] Downloading: https://libraries"))
                            {
                                optionStatus.setText("Status: " + LauncherOptions.Status.DOWNLOADING_L);
                            }
                            if (API.getLog().startsWith("[dl] Getting NATIVES URL"))
                            {
                                optionStatus.setText("Status: " + LauncherOptions.Status.FINALIZING);
                            }
                        }
                        
                    });

                    Thread.sleep(10);

                    if (API.getLog().equals("[dl] Download Complete!")) {
                        //optionStatus.setText("Status: Download & Install Complete");
                        //stop = false;
                        //msgbox to user
                        Platform.runLater(() -> {
                            optionStatus.setText("Status: " + LauncherOptions.Status.DOWNLOAD_COMPLETE);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Download Complete!");
                            alert.setHeaderText(null);
                            alert.setContentText("Version: " + (String) optionsSelectVersion.getValue() + " has been installed!");

                            //launch.setDisable(false);
                            //installGame.setDisable(false);
                            optionStatus.setText("Status: " + LauncherOptions.Status.IDLE);
                            optionsSelectVersionInstall.setDisable(false);
                            optionsExit.setDisable(false);
                            optionsClose.setDisable(false);
                            optionsSelectVersion.setDisable(false);
                            LauncherOptions.refreshVersionList = true;
                            alert.showAndWait();
                            API.dumpLogs();
                            
                        });
                        return;
                    }
                    //Thread.currentThread().stop();
                } catch (Exception e) {
                }
            }

        });
        t.start();    
    }

    @FXML
    private void _optionsSelectVersionForce(ActionEvent event) {
    }

    @FXML
    private void _optionsDebugMode(ActionEvent event) {
        if (LauncherOptions.showDebugStatus == true)
        {
            LauncherOptions.showDebugStatus = false;
        }
        else
        {
            LauncherOptions.showDebugStatus = true;
        }
        
    }

    private void saveOptionsData() {
        LauncherOptions.bypassBlacklist = optionsBypassBlacklist.isSelected();  
        LauncherOptions.keepLauncherOpen = optionsKeepLauncherOpen.isSelected(); 
        LauncherOptions.resolutionWidth = optionsResolutionMin.getText();
        LauncherOptions.resolutionHeight = optionsResolutionMax.getText(); 
        LauncherOptions.ramAllocationMin = optionsRamAllocationMin.getText(); 
        LauncherOptions.ramAllocationMax = optionsRamAllocationMax.getText(); 
        LauncherOptions.javaPath = optionsJavaVersionInput.getText(); 
        LauncherOptions.jvmArguments = optionsJVMArgumentsInput.getText();
        
        Main.initSaveUserSettings();
    }
    
    private void loadOptionsData() {
       
        optionsResolutionMin.setText(LauncherOptions.resolutionWidth);
        optionsResolutionMax.setText(LauncherOptions.resolutionHeight);
        if (!LauncherOptions.resolutionWidth.equals("854") || !LauncherOptions.resolutionHeight.equals("480"))
        {
            optionsResolution.setSelected(true);
            optionsResolutionMin.setDisable(false);
            optionsResolutionMax.setDisable(false);
        }
       
        optionsRamAllocationMin.setText(LauncherOptions.ramAllocationMin);
        optionsRamAllocationMax.setText(LauncherOptions.ramAllocationMax);
        if (!LauncherOptions.ramAllocationMin.equals("1024") || !LauncherOptions.ramAllocationMax.equals("1024"))
        {
            optionsRamAllocation.setSelected(true);
            optionsRamAllocationMin.setDisable(false);
            optionsRamAllocationMax.setDisable(false);
        }
       
        if (LauncherOptions.bypassBlacklist == true)
        {
           optionsBypassBlacklist.setSelected(true); 
        }

        optionsJavaVersionInput.setText(LauncherOptions.javaPath);
        if (!LauncherOptions.javaPath.equals(""))
        {
            optionsJavaVersion.setSelected(true);
            optionsJavaVersionInput.setDisable(false);
        }
       
        optionsJVMArgumentsInput.setText(LauncherOptions.jvmArguments);
        if (!LauncherOptions.jvmArguments.equals(""))
        {
            optionsJVMArguments.setSelected(true);
            optionsJVMArgumentsInput.setDisable(false);
        }
    }

    @FXML
    private void kt_optionsResolutionMin(KeyEvent event) { 
        System.out.print("LN " + optionsResolutionMin.getText().length());
        if (!"0123456789".contains(event.getCharacter()) || optionsResolutionMin.getText().length() > 3)
        {
            event.consume();    
        }
    }

    @FXML
    private void kt_optionsRamAllocationMin(KeyEvent event) {
        System.out.print("LN " + optionsResolutionMin.getText().length());
        if (!"0123456789".contains(event.getCharacter()) || optionsResolutionMin.getText().length() > 3)
        {
            event.consume();    
        }
    }

    @FXML
    private void kt_optionsResolutionMax(KeyEvent event) {
        System.out.print("LN " + optionsResolutionMin.getText().length());
        if (!"0123456789".contains(event.getCharacter()) || optionsResolutionMin.getText().length() > 3)
        {
            event.consume();    
        }
    }

    @FXML
    private void kt_optionsRamAllocationMax(KeyEvent event) {
        System.out.print("LN " + optionsResolutionMin.getText().length());
        if (!"0123456789".contains(event.getCharacter()) || optionsResolutionMin.getText().length() > 3)
        {
            event.consume();    
        }
    }
}
