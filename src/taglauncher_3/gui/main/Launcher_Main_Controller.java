/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3.gui.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import taglauncher_3.Launcher_Main;
import taglauncher_3.Launcher_Settings;

/**
 *
 * @author ammar
 */
public class Launcher_Main_Controller implements Initializable {

    private static Stage applicationOptionStage;
    ArrayList<String> backgroundList = new ArrayList<String>();

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private ImageView playerAvatarImage;
    @FXML
    private Tooltip tt_username;
    @FXML
    private Label launcherStatus;
    @FXML
    private AnchorPane mainBackground;
    @FXML
    private Tooltip tt_version;
    @FXML
    private Tooltip tt_play;
    @FXML
    private Tooltip tt_options;

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
        setTextBoxMax();
        runBackground();

        new Thread(() -> loadPlayerAvatar()).start(); //#LOVE YOU JAVA 8
        new Thread(() -> checkLatestVersion()).start();
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();

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
        if (username.getText().equals("")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Minecraft Launcher - Error");
            alert.setHeaderText("A man needs a name...");
            alert.setContentText("Please create a username prior to starting Minecraft.");
            alert.initStyle(StageStyle.UTILITY);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("taglauncher_3/css/purple.css");
            alert.show();
            return;
        }

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
            //add server
            List ip = new ArrayList(API.getServersIPList());
            if (!ip.contains(Launcher_Settings.serverIP) || ip.isEmpty()) {
                API.addServerToServersDat(Launcher_Settings.serverName, Launcher_Settings.serverIP);
            }

            API.downloadProfile((String) username.getText());
            API.syncVersions();

            if (!Launcher_Settings.fastStartUp) { //NOT faststartup
                API.downloadMinecraft((String) version.getValue(), false);
            }

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

            if (Launcher_Settings.fastStartUp) {
                API.runMinecraft(username.getText(), (String) version.getValue(), true);
            } else {
                API.runMinecraft(username.getText(), (String) version.getValue(), false);
            }

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
                            new Thread(() -> checkLatestVersion()).start();
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
                            alert.initStyle(StageStyle.UTILITY);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add("taglauncher_3/css/purple.css");
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
        if (version.getValue() != null) {
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
            stage.getIcons().add(new Image(Launcher_Main.class.getResourceAsStream("/taglauncher_3/css/images/app_icon_1.png")));
            stage.setTitle("Minecraft Launcher - Options");
            Scene sceneOptions = new Scene(optionsGUI);
            stage.setMinWidth(400);
            stage.setMinHeight(500);
            stage.setMaxWidth(400);
            stage.setMaxHeight(500);
            stage.setResizable(false);

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
                //if (Launcher_Settings.refreshVersionList == true) { //Just refesh it anyway.
                tagapi_3.API_Interface API = new tagapi_3.API_Interface();

                version.getItems().removeAll(version.getItems());
                for (Object ob : API.getInstalledVersionsList()) {
                    version.getItems().addAll(ob.toString());
                }

                if (!Launcher_Settings.playerVersion.equals("-1")) {
                    version.setValue(Launcher_Settings.playerVersion);
                }
                //}
            });
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Launcher_Main_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadPlayerAvatar() {
        Image image = new Image("http://minotar.net/avatar/" + Launcher_Settings.playerUsername + "/100");
        if (!image.isError()) //incase someone adds random shit or changes URL. Will show steve not a blank area.
        {
            playerAvatarImage.setImage(image);
        }
    }

    private void setToolTips() {
        Image infoIMG = new Image(getClass().getResourceAsStream("/taglauncher_3/css/images/m_info.png"));

        tt_username.setText(
                ""
                + "Your Username\n"
                + "The name must be between 4 and 16 characters long and can only contain letters, numbers and underscores.\n"
        );
        tt_username.setGraphic(new ImageView(infoIMG));

        tt_version.setText(
                ""
                + "Minecraft Version\n"
                + "Select what version of minecraft you wish to play.\n"
                + "You can download new versions via the options menu.\n"
        );
        tt_version.setGraphic(new ImageView(infoIMG));

        tt_options.setText(
                ""
                + "The Options Menu\n"
                + "A menu that allows you to tweak various settings for the launcher and Minecraft.\n"
        );
        tt_options.setGraphic(new ImageView(infoIMG));
        tt_play.setText(
                ""
                + "Play Minecraft\n"
                + "Launches Minecraft with the version and settings you have chosen.\n"
        );
        tt_play.setGraphic(new ImageView(infoIMG));
    }

    @FXML
    private void kt_username(KeyEvent event) {
        if (!event.getCharacter().matches("[A-Za-z0-9\b_]")) {
            //Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    private void runBackground() {
        for (int i = 1; i < 11; i++) {
            backgroundList.add("background_" + i);
        }

        int randomBG = ThreadLocalRandom.current().nextInt(0, backgroundList.size());
        mainBackground.setStyle("-fx-background-image: url('/taglauncher_3/css/images/" + backgroundList.get(randomBG) + ".jpg')");

        Timeline rotateBackground = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int randomBG = ThreadLocalRandom.current().nextInt(0, backgroundList.size());

                mainBackground.setStyle("-fx-background-image: url('/taglauncher_3/css/images/" + backgroundList.get(randomBG) + ".jpg')");
            }
        }));
        rotateBackground.setCycleCount(Timeline.INDEFINITE);
        rotateBackground.play();
    }

    private void checkLatestVersion() {
        try {
            URL versionLastesturl = new URL("https://raw.githubusercontent.com/ammarx/TagLauncher_3/master/_html_/latestVersion");
            URLConnection con = versionLastesturl.openConnection();
            con.setUseCaches(false); //had to as it was caching it.

            BufferedReader in = new BufferedReader(new InputStreamReader(versionLastesturl.openStream()));
            String line;

            while ((line = in.readLine()) != null) {
                if (Launcher_Settings.launcherVersion.equals(line)) {
                    Platform.runLater(() -> {
                        launcherStatus.setText("Status: Your launcher is up to date!");
                    });
                } else {
                    Platform.runLater(() -> {
                        launcherStatus.setText("Status: Your launcher is outdated!");
                    });
                }
            }
            in.close();

        } catch (MalformedURLException e) {
            Platform.runLater(() -> {
                launcherStatus.setText("Status: Unable to check for latest version!");
            });
        } catch (IOException e) {
            Platform.runLater(() -> {
                launcherStatus.setText("Status: Unable to check for latest version!");
            });

        }
    }

    private void setTextBoxMax() {
        username.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (username.getText().length() > 16) {
                        username.setText(username.getText().substring(0, 16));
                        //Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });
    }
}
