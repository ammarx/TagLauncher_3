/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3;

import java.net.URL;
import java.util.Hashtable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author ammar
 */
public class Controller implements Initializable {

    private Label label;
    @FXML
    private TextField username;
    @FXML
    private Button launch;
    @FXML
    private ComboBox version;
    @FXML
    private Label status;
    @FXML
    private ComboBox installversionList;

    Hashtable<String, String> VersionHashTable = new Hashtable<String, String>();

    @FXML
    private Label versionType;
    @FXML
    private Button installGame;
    @FXML
    private CheckBox forceInst;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();
        for (Object ob : API.getInstalledVersionsList()) {
            version.getItems().addAll(ob.toString());

        }
        //step 1 load files from mojang servers
        //load data on different thread.
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            API.downloadVersionManifest();
            return null;
        });
        executor.shutdown();
        installversionList.setDisable(true);
        for (Object ob : API.getInstallableVersionsList()) {
            String[] prsntAry = ob.toString().split(" % ");
            installversionList.getItems().addAll(prsntAry[0]);
            VersionHashTable.put(prsntAry[0], prsntAry[1]);

        }
        //step 2 read files from local system
        for (Object ob_ : API.getInstalledVersionsList()) {
            if (!VersionHashTable.containsKey((String) ob_)) {
                installversionList.getItems().addAll(ob_);
                VersionHashTable.put((String) ob_, "Unknown");
            }
        }

        installversionList.setDisable(false);

    }

    @FXML
    private void lunachMineCraft(ActionEvent event) {
        //txt.getText()
        //(String) cmbox.getValue()
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            API.downloadMinecraft((String) version.getValue(), false); //force download flag
            API.runMinecraft(username.getText(), (String) version.getValue());
            return null;
        });
        executor.shutdown();

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> {
                        status.setText(API.getLog());
                    });

                    Thread.sleep(10);

                    if (API.getLog().equals("[rl] Minecraft Initialized!")) {
                        API.dumpLogs();
                        System.exit(0);
                        return;
                    } else if (API.getLog().equals("[rl] Minecraft Corruption found!")) {
                        Platform.runLater(() -> {
                            status.setText(API.getLog());
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Unable to start Minecraft!");
                            alert.setHeaderText("Version: " + (String) version.getValue() + " failed to initialize!");
                            alert.setContentText("The game failed to initialize as data corruption \nwas found! Press re-Download game with \n*Force Download* checked");
                            alert.showAndWait();
                            API.dumpLogs();
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
    private void updateType(ActionEvent event) {
        try {
            versionType.setText(VersionHashTable.get((String) installversionList.getValue()));
            //System.out.println(VersionHashTable.get((String) installversionList.getValue()));
        } catch (Exception e) {
            //System.out.println("abc");
            versionType.setText("Unknown");

        }
    }

    @FXML
    private void installGameBtn(ActionEvent event) {
        //step 1 check if forceinstall is enabled or not
        if (forceInst.isSelected()) {
            System.out.println("Selected!");
        } else {
            System.out.println("NOT Selected!");

        }
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            API.downloadMinecraft((String) installversionList.getValue(), forceInst.isSelected());
            return null;
        });
        executor.shutdown();

        Thread t = new Thread(() -> {
            //boolean stop = true;
            while (true) {
                try {

                    Platform.runLater(() -> {

                        status.setText(API.getLog());

                        //System.out.println(API.getRunLogs());
                    });

                    Thread.sleep(10);

                    if (status.getText().equals("[dl] Download Complete!")) {
                        //stop = false;
                        //msgbox to user
                        Platform.runLater(() -> {
                            status.setText(API.getLog());
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Download Complete!");
                            alert.setHeaderText(null);
                            alert.setContentText("Version: " + (String) installversionList.getValue() + " has been installed!");

                            alert.showAndWait();
                            API.dumpLogs();
                            //refresh versions list
                            version.getItems().removeAll(version.getItems());

                            for (Object ob : API.getInstalledVersionsList()) {
                                version.getItems().addAll(ob.toString());

                            }

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

}
