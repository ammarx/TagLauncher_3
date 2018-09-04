/* 
 * The MIT License
 *
 * Copyright 2018 Ammar Ahmad.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package taglauncher_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ammar Ahmad
 */
public class Launcher_Main extends Application {
    private static Stage applicationMainStage;
    private double xOffset = 0;
    private double yOffset = 0;

    private void setApplicationMainStage(Stage stage) {
        Launcher_Main.applicationMainStage = stage;
    }

    static public Stage getApplicationMainStage() {
        return Launcher_Main.applicationMainStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
   
    @Override
    public void start(Stage stage) throws Exception {
        Launcher_Settings.userSettingsLoad();
        
        //Launcher_Main_Background.setBackgroundImages();
        
        Parent root = FXMLLoader.load(getClass().getResource("gui/main/Launcher_Main_GUI.fxml"));
        Scene scene = new Scene(root);
        initApplicationSettings(stage, scene);

        stage.setScene(scene);
        stage.show();
    }

    public void initApplicationSettings(Stage stage, Scene scene)
    {
        setApplicationMainStage(stage);
        
        stage.getIcons().add(new Image(Launcher_Main.class.getResourceAsStream("/taglauncher_3/css/images/app_icon_1.png" )));
        stage.setTitle("Minecraft Launcher");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMinWidth(450);        
        stage.setMinHeight(450);
        stage.setMaxWidth(450);        
        stage.setMaxHeight(450);
        stage.setResizable(false);
        Launcher_Settings.setTheme(scene); 
        

        scene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }
}