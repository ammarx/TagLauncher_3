/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author ammar
 */
public class Main extends Application {
    private static Stage launcherMainWindow;

    private void setLauncherMainWindow(Stage stage) {
        Main.launcherMainWindow = stage;
    }

    static public Stage getLauncherMainWindow() {
        return Main.launcherMainWindow;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/taglauncher_3/css/images/icon.png" )));
        stage.setTitle("The Primal Launcher - v0.8-alpha");
        setLauncherMainWindow(stage);
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("taglauncher_3/css/dark.css");
        stage.initStyle(StageStyle.UNDECORATED);
        
        stage.setResizable(false);
        stage.setScene(scene);
        //-fx-background-color: transparent;
        //stage.initStyle(StageStyle.TRANSPARENT);
        //scene.setFill(Color.TRANSPARENT);
        stage.show();
        stage.setOnCloseRequest((WindowEvent we) -> {
            for (int i = 0; i < 16; i++) {
                System.out.println("Created by Ammar Ahmad @ TagCraftMC");
            }
            stage.close();
            System.exit(0);
        });
        
        
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
