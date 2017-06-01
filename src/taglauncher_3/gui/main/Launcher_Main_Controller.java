/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3.gui.main;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import taglauncher_3.Launcher_Main;
import taglauncher_3.Launcher_Settings;

/**
 *
 * @author ammar
 */
public class Launcher_Main_Controller implements Initializable {

    private static Stage applicationOptionStage;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private ImageView playerAvatarImage;
    @FXML
    private Tooltip tt_username;
    @FXML
    private Label launcherStatus;
    @FXML
    private Label debugLabel;

    private void setApplicationOptionStage(Stage stage) {
        Launcher_Main_Controller.applicationOptionStage = stage;
    }

    static public Stage getApplicationOptionStage() {
        return Launcher_Main_Controller.applicationOptionStage;
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
        setToolTips();
        new Thread(() -> loadPlayerAvatar()).start(); //#LOVE YOU JAVA 8
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();
        ExecutorService executor1 = Executors.newCachedThreadPool();
        executor1.submit(() -> {
            if (API.getUpdateStatus().equals("0")) {
                System.out.println("You are running the latest API version");
                Platform.runLater(() -> {
                    launcherStatus.setText("Status: Your launcher is up to date.");
                });

            } else {
                System.out.println("You are " + API.getUpdateStatus() + " versions behind");
                Platform.runLater(() -> {
                    launcherStatus.setText("Status: Your launcher is outdated!");
                });
            }
            return null;
        });
        executor1.shutdown();

        username.setText(Launcher_Settings.playerUsername);

        for (Object ob : API.getInstalledVersionsList()) {
            version.getItems().addAll(ob.toString());

        }
        for (Object ob : API.getInstalledVersionsList()) {
            if (ob.equals(Launcher_Settings.playerVersion)) {
                version.setValue(Launcher_Settings.playerVersion);
            }
        }
    }

    @FXML
    private void launchMineCraft(ActionEvent event) {
        Launcher_Settings.playerUsername = username.getText();
        Launcher_Settings.playerVersion = version.getValue().toString();
        Launcher_Settings.userSettingsSave();

        options.setDisable(true);
        launch.setDisable(true);
        version.setDisable(true);
        username.setDisable(true);
        new Thread(() -> loadPlayerAvatar()).start();

        tagapi_3.API_Interface API = new tagapi_3.API_Interface();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            API.downloadProfile((String) username.getText());
            API.syncVersions();
            API.downloadMinecraft((String) version.getValue(), false); //force download flag

            API.setMinMemory(Integer.parseInt(Launcher_Settings.ramAllocationMin));
            API.setMemory(Integer.parseInt(Launcher_Settings.ramAllocationMax));
            API.setHeight(Integer.parseInt(Launcher_Settings.resolutionHeight));
            API.setWidth(Integer.parseInt(Launcher_Settings.resolutionWidth));
            if (Launcher_Settings.bypassBlacklist == true) {
                API.injectNetty();
            }
            if (!Launcher_Settings.javaPath.equals("")) {
                API.setJavaPath(Launcher_Settings.javaPath);
            }
            if (!Launcher_Settings.jvmArguments.equals("")) {
                API.setJVMArgument(Launcher_Settings.jvmArguments);
            }
            if (!Launcher_Settings.playerUsername.equals("")) {
                API.setVersionData("Hi " + Launcher_Settings.playerUsername + "!");
            } else {
                API.setVersionData("#AmmarBless");
            }

            API.runMinecraft(username.getText(), (String) version.getValue());
            return null;
        });
        executor.shutdown();

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    if (API.getLog().startsWith("[dl] DOWNLOADING...")) {
                        Platform.runLater(() -> {
                            launcherStatus.setText("Status: Checking installed " + Launcher_Settings.playerVersion + " files.");
                        });
                    }
                    if (API.getLog().startsWith("[rl] KEY:")) {
                        Platform.runLater(() -> {
                            launcherStatus.setText("Status: Preparing to start Minecraft.");
                        });
                    }
                    if (API.getLog().startsWith("[rl] Starting")) {
                        Platform.runLater(() -> {
                            launcherStatus.setText("Status: Starting Minecraft " + Launcher_Settings.playerVersion + ".");
                        });
                    }

                    Thread.sleep(10);

                    if (API.getLog().equals("[rl] Minecraft Initialized!")) {
                        Launcher_Settings.playerUsername = username.getText();
                        Launcher_Settings.playerVersion = version.getValue().toString();
                        Launcher_Settings.userSettingsSave();

                        if (Launcher_Settings.keepLauncherOpen == false) {
                            Platform.runLater(() -> {
                                launcherStatus.setText("Status: Minecraft started, now closing launcher. Have fun!");
                            });
                            API.dumpLogs();
                            System.exit(0);
                        } else {
                            Platform.runLater(() -> {
                                if (API.getUpdateStatus().equals("0")) {
                                    System.out.println("You are running the latest API version");
                                    Platform.runLater(() -> {
                                        launcherStatus.setText("Status: Your launcher is up to date.");
                                        username.setDisable(false);
                                        options.setDisable(false);
                                        launch.setDisable(false);
                                        version.setDisable(false);
                                    });

                                } else {
                                    System.out.println("You are " + API.getUpdateStatus() + " versions behind");
                                    Platform.runLater(() -> {
                                        launcherStatus.setText("Status: Your launcher is outdated!");
                                        username.setDisable(false);
                                        options.setDisable(false);
                                        launch.setDisable(false);
                                        version.setDisable(false);
                                    });
                                }
                            });
                        }
                        return;

                    } else if (API.getLog().equals("[el] Minecraft Corruption found!")) {
                        Platform.runLater(() -> {
                            //STATUS status.setText(API.getLog());
                            launcherStatus.setText("Status: Error. Minecraft file corruption detected!");
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Unable to start Minecraft!");
                            alert.setHeaderText("Version: " + (String) version.getValue() + " failed to initialize!");
                            alert.setContentText("The game failed to initialize as data corruption \nwas found! Press re-Download game with \n*Force Download* checked in the options menu.");
                            alert.showAndWait();
                            API.dumpLogs();
                            username.setDisable(false);
                            options.setDisable(false);
                            launch.setDisable(false);
                            version.setDisable(false);
                        });
                        return;
                    }
                } catch (Exception e) {
                }
            }
        });
        t.start();

    }

    @FXML
    private void launchMinimize(ActionEvent event) {
        Stage stage = Launcher_Main.getApplicationMainStage();
        stage.setIconified(true);
    }

    @FXML
    private void launchExit(ActionEvent event) {
        Launcher_Settings.playerUsername = username.getText();
        if (version.getValue() != null)
        {
            Launcher_Settings.playerVersion = version.getValue().toString();
        }
        Launcher_Settings.userSettingsSave();
        Stage stage = Launcher_Main.getApplicationMainStage();
        stage.close();
    }

    @FXML
    private void launchOptions(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/taglauncher_3/gui/options/Launcher_Options_GUI.fxml"));
            Parent optionsGUI = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("The Options Menu");
            Scene sceneOptions = new Scene(optionsGUI);

            stage.setScene(sceneOptions);
            Launcher_Settings.setTheme(sceneOptions);
            setApplicationOptionStage(stage);

            sceneOptions.setOnMousePressed(event_ -> {
                xOffset = stage.getX() - event_.getScreenX();
                yOffset = stage.getY() - event_.getScreenY();
            });

            sceneOptions.setOnMouseDragged(event_ -> {
                stage.setX(event_.getScreenX() + xOffset);
                stage.setY(event_.getScreenY() + yOffset);
            });
            stage.setOnHiding(event_ -> {
                if (Launcher_Settings.refreshVersionList == true) {
                    tagapi_3.API_Interface API = new tagapi_3.API_Interface();

                    version.getItems().removeAll(version.getItems());
                    for (Object ob : API.getInstalledVersionsList()) {
                        version.getItems().addAll(ob.toString());
                    }
                    version.setValue(Launcher_Settings.playerVersion);
                }
            });
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Launcher_Main_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadPlayerAvatar() {
         debugLabel.setText("METHOD LOADED");
        Image image = new Image("https://mcapi.ca/avatar/" + Launcher_Settings.playerUsername);
        if (!image.isError()) //incase someone adds random shit or changes URL. Will show steve not a blank area.
        {
            playerAvatarImage.setImage(image);
        }
        else
        {
           debugLabel.setText(image.getException().toString());
           System.out.print(image.getException());
        }

    }

    private void setToolTips() {
        //Image warnIMG = new Image(getClass().getResourceAsStream("/taglauncher_3/css/images/warning.png"));
        Image infoIMG = new Image(getClass().getResourceAsStream("/taglauncher_3/css/images/m_info.png"));
        //Image helpIMG = new Image(getClass().getResourceAsStream("/taglauncher_3/css/images/help.png"));
        //Image critIMG = new Image(getClass().getResourceAsStream("/taglauncher_3/css/images/critical.png"));
        //#.setGraphic(new ImageView(image));

        tt_username.setText(
                ""
                + "You must pick a username thats between\n"
                + "1 and 16 characters long.\n"
        );
        tt_username.setGraphic(new ImageView(infoIMG));
    }

    @FXML
    private void kt_username(KeyEvent event) {
        if (username.getText().length() > 16) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();
        }
    }

}
