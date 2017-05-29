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
import java.util.Properties;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author ammar
 */
public class Main extends Application {
    private static Stage applicationMainStage;
    private double xOffset = 0;
    private double yOffset = 0;

    private void setApplicationMainStage(Stage stage) {
        Main.applicationMainStage = stage;
    }

    static public Stage getApplicationMainStage() {
        return Main.applicationMainStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
        
    @Override
    public void start(Stage stage) throws Exception {
        initLoadUserSettings(); //Has to load prior, otherwise the main GUI loads before and uses the default variable data.
        
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        Scene scene = new Scene(root);
        setApplicationMainStage(stage);
        initApplicationSettings(stage, scene);
        initShutdownCredit(stage);

        stage.setScene(scene);
        stage.show();
    }

    public void initApplicationSettings(Stage stage, Scene scene)
    {
        System.out.println("Initializing application settings");
        //
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/taglauncher_3/css/images/icon.png" )));
        stage.setTitle("The Primal Launcher - v0.8-alpha");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        //
        scene.getStylesheets().add("taglauncher_3/css/dark.css");
        //
        scene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        //
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }
 
    public static void initSaveUserSettings()
    {
        if (LauncherOptions.resolutionWidth.equals(""))
        {
            LauncherOptions.resolutionWidth = "854";
        }
        if (LauncherOptions.resolutionHeight.equals(""))
        {
            LauncherOptions.resolutionHeight = "480";
        }
        if (LauncherOptions.ramAllocationMin.equals(""))
        {
            LauncherOptions.ramAllocationMin = "1024";
        }
        if (LauncherOptions.ramAllocationMax.equals(""))
        {
            LauncherOptions.ramAllocationMax = "1024";
        }
        
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            prop.setProperty("username", LauncherOptions.playerUsername);
            prop.setProperty("version", LauncherOptions.playerVersion);
            prop.setProperty("bypassblacklist", String.valueOf(LauncherOptions.bypassBlacklist));
            prop.setProperty("keeplauncheropen", String.valueOf(LauncherOptions.keepLauncherOpen));
            prop.setProperty("resolutionwidth", LauncherOptions.resolutionWidth);
            prop.setProperty("resolutionheight", LauncherOptions.resolutionHeight);
            prop.setProperty("ramAllocationmin", LauncherOptions.ramAllocationMin);
            prop.setProperty("ramAllocationmax", LauncherOptions.ramAllocationMax);
            prop.setProperty("javapath", LauncherOptions.javaPath);
            prop.setProperty("jvmarguments", LauncherOptions.jvmArguments);
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } 
    }
    
    public static void initLoadUserSettings()
    {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            
            if (prop.getProperty("username") != null)
            {
                LauncherOptions.playerUsername = prop.getProperty("username");  
            }
            
            if (prop.getProperty("version") != null)
            {
                LauncherOptions.playerVersion = prop.getProperty("version");  
            }
            
            if (prop.getProperty("bypassblacklist") != null)
            {
                LauncherOptions.bypassBlacklist = Boolean.parseBoolean(prop.getProperty("bypassblacklist"));
            }
            
            if (prop.getProperty("keepLauncherppen") != null)
            {
                LauncherOptions.keepLauncherOpen = Boolean.parseBoolean(prop.getProperty("keepLauncherppen"));
            }

            if (prop.getProperty("resolutionwidth") != null)
            {
                LauncherOptions.resolutionWidth = prop.getProperty("resolutionwidth");
            }
            
            if (prop.getProperty("resolutionheight") != null)
            {
               LauncherOptions.resolutionHeight = prop.getProperty("resolutionheight");
            }
            
            if (prop.getProperty("ramAllocationmin") != null)
            {
                LauncherOptions.ramAllocationMin = prop.getProperty("ramAllocationmin");
            }
            
            if (prop.getProperty("ramAllocationmax") != null)
            {
                LauncherOptions.ramAllocationMax = prop.getProperty("ramAllocationmax");
            }
            
            if (prop.getProperty("javapath") != null)
            {
                LauncherOptions.javaPath = prop.getProperty("javapath");
            }
            
            if (prop.getProperty("jvmarguments") != null)
            {
              LauncherOptions.jvmArguments = prop.getProperty("jvmarguments");  
            }

        } catch (IOException ex) {
            System.out.println("File not found" + ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("File not found" + e);
                }
            }
        }
        
//        System.out.print("DB PO: " + LauncherOptions.playerUsername + "\n");
//        System.out.print("DB PO: " + LauncherOptions.playerVersion + "\n");
//        System.out.print("DB PO: " + LauncherOptions.bypassBlacklist + "\n");
//        System.out.print("DB PO: " + LauncherOptions.keepLauncherOpen + "\n");
//        System.out.print("DB PO: " + LauncherOptions.resolutionWidth + "\n");
//        System.out.print("DB PO: " + LauncherOptions.resolutionHeight + "\n");
//        System.out.print("DB PO: " + LauncherOptions.ramAllocationMin + "\n");
//        System.out.print("DB PO: " + LauncherOptions.ramAllocationMax + "\n");
//        System.out.print("DB PO: " + LauncherOptions.javaPath + "\n");
//        System.out.print("DB PO: " + LauncherOptions.jvmArguments + "\n");
//        System.out.print("DB PO: LOADING \n");
    }
    
    public void initShutdownCredit(Stage stage)        
    {
        System.out.println("Initializing shutdown credits.");
        
        stage.setOnCloseRequest((WindowEvent we) -> {
            for (int i = 0; i < 16; i++) {
                System.out.println("Created by Ammar Ahmad.");
                System.out.println("UI by Chalkie.");
                System.out.println("© TagCraftMC.com & TerraPrimal.com");
            }
            stage.close();
            System.exit(0);
        });
    }
}