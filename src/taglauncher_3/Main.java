/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3;

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
        //
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        Scene scene = new Scene(root);
        //
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
    
    public void initShutdownCredit(Stage stage)        
    {
        System.out.println("Initializing shutdown credits.");
        
        stage.setOnCloseRequest((WindowEvent we) -> {
            for (int i = 0; i < 16; i++) {
                System.out.println("Created by Ammar Ahmad.");
                System.out.println("Designed by Chalkie.");
                System.out.println("© TagCraftMC.com & TerraPrimal.com");
            }
            stage.close();
            System.exit(0);
        });
    }
}