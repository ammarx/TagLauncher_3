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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ammar
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("TagLauncher - v0.5-alpha");
        stage.setScene(scene);
        
        //-fx-background-color: transparent;
        //stage.initStyle(StageStyle.TRANSPARENT);
        //scene.setFill(Color.TRANSPARENT);
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
