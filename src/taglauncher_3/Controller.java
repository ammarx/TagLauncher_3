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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 * @author ammar
 */
public class Controller implements Initializable {
    
    private static Stage applicationOptionStage;
    private double xOffset = 0;
    private double yOffset = 0;
    
    private void setApplicationOptionStage(Stage stage) {
        Controller.applicationOptionStage = stage;
    }

    static public Stage getApplicationOptionStage() {
        return Controller.applicationOptionStage;
    }
    
    private Label label;
    @FXML
    private TextField username;
    @FXML
    private Button launch;
    @FXML
    private ComboBox version;
    @FXML
    private Button minimize;
    @FXML
    private Button exit;
    @FXML
    private Button options;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
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
            version.getItems().addAll(ob.toString());

        }
        
        username.setText(LauncherOptions.playerUsername);

        for (Object ob : API.getInstalledVersionsList()) {
            if (ob.equals(LauncherOptions.playerVersion)) {
                version.setValue(LauncherOptions.playerVersion);
            }
        }
        //load my settings
       // Properties prop = new Properties();
        //InputStream input = null;

        //try {

            //input = new FileInputStream("config.properties");

            // load a properties file
           // prop.load(input);

            // get the property value and print it out


//        } catch (IOException ex) {
//            System.out.println("File not found" + ex);
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    System.out.println("File not found" + e);
//                }
//            }
//        }

    }

    @FXML
    private void lunachMineCraft(ActionEvent event) {
        //txt.getText()
        //(String) cmbox.getValue()
        launch.setDisable(true);
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            API.downloadProfile((String) username.getText());
            API.syncVersions();
            API.downloadMinecraft((String) version.getValue(), false); //force download flag
            //API.setMemory("2G");
            //API.setVersionData(username.getText());
            API.injectNetty();
            API.runMinecraft(username.getText(), (String) version.getValue());
            return null;
        });
        executor.shutdown();

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> {
                        //STATUS status.setText(API.getLog());
                    });

                    Thread.sleep(10);

                    if (API.getLog().equals("[rl] Minecraft Initialized!")) {
                        //write settings to system
                        
                        LauncherOptions.playerUsername = username.getText(); 
                        LauncherOptions.playerVersion = version.getValue().toString();
                        Main.initSaveUserSettings();
//                        Properties prop = new Properties();
//                        OutputStream output = null;
//
//                        try {
//
//                            output = new FileOutputStream("config.properties");
//
//                            // set the properties value
//                            prop.setProperty("username", LauncherOptions.playerUsername);
//                            prop.setProperty("version", LauncherOptions.playerVersion);
//                           
//                            // save properties to project root folder
//                            prop.store(output, null);
//
//                        } catch (IOException io) {
//                            io.printStackTrace();
//                        } finally {
//                            if (output != null) {
//                                try {
//                                    output.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
                        API.dumpLogs();
                        System.exit(0);
                        return;
                    } else if (API.getLog().equals("[el] Minecraft Corruption found!")) {
                        Platform.runLater(() -> {
                            //STATUS status.setText(API.getLog());
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Unable to start Minecraft!");
                            alert.setHeaderText("Version: " + (String) version.getValue() + " failed to initialize!");
                            alert.setContentText("The game failed to initialize as data corruption \nwas found! Press re-Download game with \n*Force Download* checked");
                            alert.showAndWait();
                            API.dumpLogs();
                            launch.setDisable(false);
                            //refresh versions list

                            //System.out.println(API.getRunLogs());
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
    private void launchMinimize(ActionEvent event) {
        Stage stage = Main.getApplicationMainStage();
        stage.setIconified(true);
    }

    @FXML
    private void launchExit(ActionEvent event) {    
        Stage stage = Main.getApplicationMainStage();
        stage.close();
    }
    
    @FXML
    private void launchOptions(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserOptions.fxml"));
            Parent optionsGUI = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("The Options Menu");
            Scene sceneOptions = new Scene(optionsGUI);
            
            stage.setScene(sceneOptions);
            sceneOptions.getStylesheets().add("taglauncher_3/css/dark_options.css");
            setApplicationOptionStage(stage);
            
            sceneOptions.setOnMousePressed(event_ -> {
                xOffset = stage.getX() - event_.getScreenX();
                yOffset = stage.getY() - event_.getScreenY();
            });
            //
            sceneOptions.setOnMouseDragged(event_ -> {
                stage.setX(event_.getScreenX() + xOffset);
                stage.setY(event_.getScreenY() + yOffset);
            });            
            stage.setOnHiding(event_ -> {
                //check boolean, if true refresh version list.
                if (LauncherOptions.refreshVersionList == true)
                {
                    tagapi_3.API_Interface API = new tagapi_3.API_Interface();
                    
                    version.getItems().removeAll(version.getItems());
                    for (Object ob : API.getInstalledVersionsList()) {
                        version.getItems().addAll(ob.toString());
                    }  
                } 
            });
            stage.show();
        } catch (IOException ex) 
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
