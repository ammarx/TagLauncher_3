/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3.gui.options;

import java.awt.Toolkit;
import taglauncher_3.gui.main.Launcher_Main_Controller;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import taglauncher_3.Launcher_Main;
import taglauncher_3.Launcher_Settings;

/**
 * FXML Controller class
 *
 * @author Mathew
 */
public class Launcher_Options_Controller implements Initializable {

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
    @FXML
    private ComboBox themeType;
    @FXML
    private RadioButton useThemeType;
    @FXML
    private Tooltip tt_keepLauncherOpen;
    @FXML
    private Tooltip tt_customTheme;
    @FXML
    private Tooltip tt_resolution;
    @FXML
    private Tooltip tt_ramAllocation;
    @FXML
    private Tooltip tt_bypassBlacklist;
    @FXML
    private Tooltip tt_selectVersion;
    @FXML
    private Tooltip tt_selectVersionInstall;
    @FXML
    private Tooltip tt_forceDownload;
    @FXML
    private Tooltip tt_javaVersion;
    @FXML
    private Tooltip tt_jvmArgs;
    @FXML
    private Label launcherVersion;
    @FXML
    private Tooltip tt_launcherVersion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setToolTips();
        setTextBoxMax();
        themeType.getItems().addAll("Purple", "Gray", "Red", "Green", "Blue", "White");

        loadOptionsData();

        tagapi_3.API_Interface API = new tagapi_3.API_Interface();
        ExecutorService executor1 = Executors.newCachedThreadPool();
        executor1.submit(() -> {
            if (API.getUpdateStatus().equals("0")) {
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

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                optionStatus.setText("Status: Getting latest versions");
                optionsSelectVersion.setDisable(true);
                optionsSelectVersionInstall.setDisable(true);
                API.downloadVersionManifest();

                for (Object ob : API.getInstallableVersionsList()) {
                    String[] prsntAry = ob.toString().split(" % ");
                    optionsSelectVersion.getItems().addAll(prsntAry[0]);
                    VersionHashTable.put(prsntAry[0], prsntAry[1]);

                }
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
                try {
                    Platform.runLater(() -> {
                        optionStatus.setText("Status: Idle");
                    });
                } catch (Exception e) {
                }
                return null;
            }
        });
        executor.shutdown();
    }

    @FXML
    private void _optionsClose(ActionEvent event) {
        saveOptionsData();
        Stage stage = Launcher_Main_Controller.getApplicationOptionStage();
        stage.close();
    }

    @FXML
    private void _optionsExit(ActionEvent event) {
        saveOptionsData();
        Stage stage = Launcher_Main_Controller.getApplicationOptionStage();
        stage.close();
    }

    @FXML
    private void _optionsResolution(ActionEvent event) {
        if (optionsResolution.isSelected()) {
            optionsResolutionMin.setDisable(false);
            optionsResolutionMax.setDisable(false);

        } else {
            optionsResolutionMin.setDisable(true);
            optionsResolutionMax.setDisable(true);
            optionsResolutionMin.setText("854");
            optionsResolutionMax.setText("480");
        }
    }

    @FXML
    private void _optionsRamAllocation(ActionEvent event) {
        if (optionsRamAllocation.isSelected()) {
            optionsRamAllocationMin.setDisable(false);
            optionsRamAllocationMax.setDisable(false);
        } else {
            optionsRamAllocationMin.setDisable(true);
            optionsRamAllocationMax.setDisable(true);
            optionsRamAllocationMin.setText("1024");
            optionsRamAllocationMax.setText("1024");
        }
    }

    @FXML
    private void _optionsJavaVersion(ActionEvent event) {
        if (optionsJavaVersion.isSelected()) {
            optionsJavaVersionInput.setDisable(false);
        } else {
            optionsJavaVersionInput.setDisable(true);
        }
    }

    @FXML
    private void _optionsJVMArguments(ActionEvent event) {
        if (optionsJVMArguments.isSelected()) {
            optionsJVMArgumentsInput.setDisable(false);
        } else {
            optionsJVMArgumentsInput.setDisable(true);
        }
    }

    @FXML
    private void _optionsKeepLauncherOpen(ActionEvent event) {
        if (optionsKeepLauncherOpen.isSelected()) {
            Launcher_Settings.keepLauncherOpen = true;
        } else {
            Launcher_Settings.keepLauncherOpen = false;
        }

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
            while (true) {
                try {

                    Platform.runLater(() -> {

                        if (Launcher_Settings.showDebugStatus == true) {
                            optionStatus.setText(API.getLog());
                        } else {
                            if (API.getLog().startsWith("[dl] URL: https://launchermeta")) {
                                optionStatus.setText("Status: " + Launcher_Settings.Status.DOWNLOADING_LM);
                            }
                            if (API.getLog().startsWith("[dl] DOWNLOADING...HASH:")) {
                                optionStatus.setText("Status: " + Launcher_Settings.Status.DOWNLOADING);
                            }
                            if (API.getLog().startsWith("[dl] DOWNLOADING MINECRAFT JAR")) {
                                optionStatus.setText("Status: " + Launcher_Settings.Status.DOWNLOADING_M);
                            }
                            if (API.getLog().startsWith("[dl] Downloading: https://libraries")) {
                                optionStatus.setText("Status: " + Launcher_Settings.Status.DOWNLOADING_L);
                            }
                            if (API.getLog().startsWith("[dl] Getting NATIVES URL")) {
                                optionStatus.setText("Status: " + Launcher_Settings.Status.FINALIZING);
                            }
                        }

                    });

                    Thread.sleep(10);

                    if (API.getLog().equals("[dl] Download Complete!")) {
                        Platform.runLater(() -> {
                            optionStatus.setText("Status: " + Launcher_Settings.Status.DOWNLOAD_COMPLETE);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Download Complete!");
                            alert.setHeaderText(null);
                            alert.setContentText("Version: " + (String) optionsSelectVersion.getValue() + " has been downloaded & installed!");
                            ;
                            optionStatus.setText("Status: " + Launcher_Settings.Status.IDLE);
                            optionsSelectVersionInstall.setDisable(false);
                            optionsExit.setDisable(false);
                            optionsClose.setDisable(false);
                            optionsSelectVersion.setDisable(false);
                            Launcher_Settings.refreshVersionList = true;
                            alert.showAndWait();
                            API.dumpLogs();

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
    private void _optionsSelectVersionForce(ActionEvent event) {
    }

    @FXML
    private void _optionsDebugMode(ActionEvent event) {
        if (Launcher_Settings.showDebugStatus == true) {
            Launcher_Settings.showDebugStatus = false;
        } else {
            Launcher_Settings.showDebugStatus = true;
        }

    }

    private void saveOptionsData() {
        Launcher_Settings.bypassBlacklist = optionsBypassBlacklist.isSelected();
        Launcher_Settings.keepLauncherOpen = optionsKeepLauncherOpen.isSelected();
        Launcher_Settings.resolutionWidth = optionsResolutionMin.getText();
        Launcher_Settings.resolutionHeight = optionsResolutionMax.getText();
        Launcher_Settings.ramAllocationMin = optionsRamAllocationMin.getText();
        Launcher_Settings.ramAllocationMax = optionsRamAllocationMax.getText();
        Launcher_Settings.javaPath = optionsJavaVersionInput.getText();
        Launcher_Settings.jvmArguments = optionsJVMArgumentsInput.getText();

        Launcher_Settings.userSettingsSave();
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private void loadOptionsData() {

        launcherVersion.setText("Version: " + Launcher_Settings.launcherVersion);

        optionsResolutionMin.setText(Launcher_Settings.resolutionWidth);
        optionsResolutionMax.setText(Launcher_Settings.resolutionHeight);
        if (!Launcher_Settings.resolutionWidth.equals("854") || !Launcher_Settings.resolutionHeight.equals("480")) {
            optionsResolution.setSelected(true);
            optionsResolutionMin.setDisable(false);
            optionsResolutionMax.setDisable(false);
        }

        optionsRamAllocationMin.setText(Launcher_Settings.ramAllocationMin);
        optionsRamAllocationMax.setText(Launcher_Settings.ramAllocationMax);
        if (!Launcher_Settings.ramAllocationMin.equals("1024") || !Launcher_Settings.ramAllocationMax.equals("1024")) {
            optionsRamAllocation.setSelected(true);
            optionsRamAllocationMin.setDisable(false);
            optionsRamAllocationMax.setDisable(false);
        }

        if (Launcher_Settings.bypassBlacklist == true) {
            optionsBypassBlacklist.setSelected(true);
        }

        optionsJavaVersionInput.setText(Launcher_Settings.javaPath);
        if (!Launcher_Settings.javaPath.equals("")) {
            optionsJavaVersion.setSelected(true);
            optionsJavaVersionInput.setDisable(false);
        }

        optionsJVMArgumentsInput.setText(Launcher_Settings.jvmArguments);
        if (!Launcher_Settings.jvmArguments.equals("")) {
            optionsJVMArguments.setSelected(true);
            optionsJVMArgumentsInput.setDisable(false);
        }

        if (!Launcher_Settings.selectedTheme.equals("")) {
            useThemeType.setSelected(true);
            themeType.setDisable(false);
            themeType.setValue(capitalize(Launcher_Settings.selectedTheme));
        }

        if (Launcher_Settings.keepLauncherOpen == true) {
            optionsKeepLauncherOpen.setSelected(true);
        }
    }

    @FXML
    private void kt_optionsResolutionMin(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void kt_optionsRamAllocationMin(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void kt_optionsResolutionMax(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void kt_optionsRamAllocationMax(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void _themeType(ActionEvent event) {
        Launcher_Settings.selectedTheme = themeType.getValue().toString().toLowerCase();
        Launcher_Settings.userSettingsSave();

        Stage gui_options = Launcher_Main_Controller.getApplicationOptionStage();
        Stage gui_main = Launcher_Main.getApplicationMainStage();

        Launcher_Settings.setTheme(gui_options.getScene());
        Launcher_Settings.setTheme(gui_main.getScene());
    }

    @FXML
    private void _useThemeType(ActionEvent event) {
        if (useThemeType.isSelected()) {
            themeType.setDisable(false);
            if (themeType.getValue() != null) {
                Launcher_Settings.selectedTheme = themeType.getValue().toString().toLowerCase();
                Launcher_Settings.userSettingsSave();
            }
        } else {
            themeType.setDisable(true);
            Launcher_Settings.selectedTheme = "";
        }
        Stage gui_options = Launcher_Main_Controller.getApplicationOptionStage();
        Stage gui_main = Launcher_Main.getApplicationMainStage();

        Launcher_Settings.setTheme(gui_options.getScene());
        Launcher_Settings.setTheme(gui_main.getScene());
    }

    private void setToolTips() {
        Image infoIMG = new Image(getClass().getResourceAsStream("/taglauncher_3/css/images/m_info.png"));

        tt_keepLauncherOpen.setText(
                ""
                + "Keep The Launcher Open\n"
                + "Keeps the launcher open after Minecraft has started.\n"
        );
        tt_keepLauncherOpen.setGraphic(new ImageView(infoIMG));

        tt_customTheme.setText(
                ""
                + "Choose A Custom Theme\n"
                + "Change the launchers theme to a diffrent color.\n"
        );
        tt_customTheme.setGraphic(new ImageView(infoIMG));

        tt_resolution.setText(
                ""
                + "Set Minecraft's Resolution\n"
                + "Set the height and width of the Minecraft client.\n"
        );
        tt_resolution.setGraphic(new ImageView(infoIMG));

        tt_ramAllocation.setText(
                ""
                + "Set Minecraft's Ram Usage\n"
                + "Set the minimum and maximum ram that the Minecraft client will use.\n"
                + "WARNING: It's best to leave this option alone unless you know what you're doing. Altering this beyond what your computer can handle will cause Minecraft to not open.\n"
        );
        tt_ramAllocation.setGraphic(new ImageView(infoIMG));

        tt_bypassBlacklist.setText(
                ""
                + "Bypass The Blacklist\n"
                + "Let you bypass the EULA blacklist when enabled.\n"
        );
        tt_bypassBlacklist.setGraphic(new ImageView(infoIMG));

        tt_selectVersion.setText(
                ""
                + "Select A Version\n"
                + "Choose a version of Minecraft to download and install.\n"
        );
        tt_selectVersion.setGraphic(new ImageView(infoIMG));

        tt_selectVersionInstall.setText(
                ""
                + "Install and Download\n"
                + "Install and download the version of Minecraft selected.\n"
        );
        tt_selectVersionInstall.setGraphic(new ImageView(infoIMG));

        tt_forceDownload.setText(
                ""
                + "Force Download\n"
                + "When enabled, this tells the launcher to re-download all files for the selected version.\n"
                + "This can fix issues with Minecraft not starting due to corruption or missing files.\n"
        );
        tt_forceDownload.setGraphic(new ImageView(infoIMG));

        tt_javaVersion.setText(
                ""
                + "Java Location\n"
                + "When enabled, this changes the location of where the launcher gets Java from.\n"
                + "WARNING: It's best to leave this option alone unless you know what you're doing. Altering this may cause Minecraft to not open.\n"
        );
        tt_javaVersion.setGraphic(new ImageView(infoIMG));

        tt_jvmArgs.setText(
                ""
                + "Extra JVM Arguments\n"
                + "When enabled, this allows you to insert extra JVM arguments when Minecraft launches.\n"
                + "WARNING: It's best to leave this option alone unless you know what you're doing. Altering this may cause Minecraft to not open.\n"
        );
        tt_jvmArgs.setGraphic(new ImageView(infoIMG));

        tt_launcherVersion.setText(
                ""
                + "Current Launcher Version\n"
                + "This is the current launcher version.\n"
                + "Click to view the launcher credits.\n"
        );
        tt_launcherVersion.setGraphic(new ImageView(infoIMG));
    }

    @FXML
    private void mc_launcherVersion(MouseEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Minecraft Launcher - Credits");
        alert.setHeaderText("So long, and thanks for all the fish.");
        alert.setContentText(""
                + "Minotar.net: API used for the Avatars.\n"
                + "Mojang: A little thing called Minecraft.\n"
                + "Ammar_Ahmad: The brains behind the code.\n"
                + "Chalkie: Pressed 'compile' that one time.\n\n"
                + "© TagCraftMC.com & TerraPrimal.com\n");
        alert.show();
    }

    private void setTextBoxMax() {
        optionsResolutionMin.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (optionsResolutionMin.getText().length() > 4) {
                        optionsResolutionMin.setText(optionsResolutionMin.getText().substring(0, 4));
                        //Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });

        optionsResolutionMax.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (optionsResolutionMax.getText().length() > 4) {
                        optionsResolutionMax.setText(optionsResolutionMax.getText().substring(0, 4));
                        //Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });

        optionsRamAllocationMin.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (optionsRamAllocationMin.getText().length() > 4) {
                        optionsRamAllocationMin.setText(optionsRamAllocationMin.getText().substring(0, 4));
                        //Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });

        optionsRamAllocationMax.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (optionsRamAllocationMax.getText().length() > 4) {
                        optionsRamAllocationMax.setText(optionsRamAllocationMax.getText().substring(0, 4));
                        //Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });
    }
}
