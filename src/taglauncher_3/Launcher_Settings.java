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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javafx.scene.Scene;

/**
 *
 * @author Mathew
 */
public class Launcher_Settings {
    
    static public final String serverIP = "mc.terraprimal.com";
    static public final String serverName = "Terra Primal";
    static public String launcherVersion = "1.30";
    static public String playerUsername = "";
    static public String playerVersion = "-1";
    static public boolean firstStart = true;
    static public boolean refreshVersionList = false;
    static public boolean bypassBlacklist = true;
    static public boolean keepLauncherOpen = false;
    static public boolean showDebugStatus = false;
    static public boolean fastStartUp = false;
    static public String selectedTheme = "";
    
    static public String resolutionWidth = "854";
    static public String resolutionHeight = "480";
    static public String ramAllocationMin = "1024";
    static public String ramAllocationMax = "1024";
    static public String javaPath = "";
    static public String jvmArguments = "";
    
    public enum Status {
      IDLE {
        @Override
        public String toString() {
          return "Idle";
        }
      },
      DOWNLOADING {
        @Override
        public String toString() {
          return "Checking & Downloading Files";
        }
      },
      DOWNLOADING_N {
        @Override
        public String toString() {
          return "Downloading File #";
        }
      },
      DOWNLOADING_M {
        @Override
        public String toString() {
          return "Downloading The Minecraft Client";
        }
      },
      DOWNLOADING_LM {
        @Override
        public String toString() {
          return "Downloading Version Data From Mojang";
        }
      },
      DOWNLOADING_L {
        @Override
        public String toString() {
          return "Downloading Required Libraries";
        }
      },
      DOWNLOAD_COMPLETE {
        @Override
        public String toString() {
          return "Downloading Completed";
        }
      },
      FINALIZING {
        @Override
        public String toString() {
          return "Finalizing The Installation";
        }
      },
      ERROR {
        @Override
        public String toString() {
          return "Error";
        }
      },
      CHECKING {
        @Override
        public String toString() {
          return "Checking Latest Versions Available";
        }
      },
      EXTRACTING {
        @Override
        public String toString() {
          return "Extracting & Installing Files";
        }
      },
      APILOG {
        @Override
        public String toString() {
        tagapi_3.API_Interface API = new tagapi_3.API_Interface();
          return API.getLog();
        }
      },
    }
    
    public static void userSettingsSave()
    {
        if (Launcher_Settings.resolutionWidth.equals("") || Launcher_Settings.resolutionWidth.equals("0"))
        {
            Launcher_Settings.resolutionWidth = "854";
        }
        if (Launcher_Settings.resolutionHeight.equals("") || Launcher_Settings.resolutionHeight.equals("0"))
        {
            Launcher_Settings.resolutionHeight = "480";
        }
        if (Launcher_Settings.ramAllocationMin.equals("") || Launcher_Settings.ramAllocationMin.equals("0"))
        {
            Launcher_Settings.ramAllocationMin = "1024";
        }
        if (Launcher_Settings.ramAllocationMax.equals("") || Launcher_Settings.ramAllocationMax.equals("0"))
        {
            Launcher_Settings.ramAllocationMax = "1024";
        }
        if (Launcher_Settings.playerUsername.equals(""))
        {
            Launcher_Settings.playerUsername = "Username";
        }
        
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            prop.setProperty("username", Launcher_Settings.playerUsername);
            prop.setProperty("version", Launcher_Settings.playerVersion);
            prop.setProperty("theme", Launcher_Settings.selectedTheme);
            prop.setProperty("firststart", String.valueOf(Launcher_Settings.firstStart));
            prop.setProperty("faststartup", String.valueOf(Launcher_Settings.fastStartUp));
            prop.setProperty("bypassblacklist", String.valueOf(Launcher_Settings.bypassBlacklist));
            prop.setProperty("keeplauncheropen", String.valueOf(Launcher_Settings.keepLauncherOpen));
            prop.setProperty("resolutionwidth", Launcher_Settings.resolutionWidth);
            prop.setProperty("resolutionheight", Launcher_Settings.resolutionHeight);
            prop.setProperty("ramAllocationmin", Launcher_Settings.ramAllocationMin);
            prop.setProperty("ramAllocationmax", Launcher_Settings.ramAllocationMax);
            prop.setProperty("javapath", Launcher_Settings.javaPath);
            prop.setProperty("jvmarguments", Launcher_Settings.jvmArguments);
            prop.setProperty("debugMode", String.valueOf(Launcher_Settings.showDebugStatus));
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
    
    public static void userSettingsLoad()
    {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            
            if (prop.getProperty("username") != null)
            {
                Launcher_Settings.playerUsername = prop.getProperty("username");  
            }
            
            if (prop.getProperty("version") != null)
            {
                Launcher_Settings.playerVersion = prop.getProperty("version");  
            }
            
            if (prop.getProperty("firststart") != null)
            {
                Launcher_Settings.firstStart = Boolean.parseBoolean(prop.getProperty("firststart"));
            }
            
            if (prop.getProperty("faststartup") != null)
            {
                Launcher_Settings.fastStartUp = Boolean.parseBoolean(prop.getProperty("faststartup"));
            }
            
            if (prop.getProperty("bypassblacklist") != null)
            {
                Launcher_Settings.bypassBlacklist = Boolean.parseBoolean(prop.getProperty("bypassblacklist"));
            }
            
            if (prop.getProperty("keeplauncheropen") != null)
            {
                Launcher_Settings.keepLauncherOpen = Boolean.parseBoolean(prop.getProperty("keeplauncheropen"));
            }

            if (prop.getProperty("resolutionwidth") != null)
            {
                Launcher_Settings.resolutionWidth = prop.getProperty("resolutionwidth");
            }
            
            if (prop.getProperty("resolutionheight") != null)
            {
               Launcher_Settings.resolutionHeight = prop.getProperty("resolutionheight");
            }
            
            if (prop.getProperty("ramAllocationmin") != null)
            {
                Launcher_Settings.ramAllocationMin = prop.getProperty("ramAllocationmin");
            }
            
            if (prop.getProperty("ramAllocationmax") != null)
            {
                Launcher_Settings.ramAllocationMax = prop.getProperty("ramAllocationmax");
            }
            
            if (prop.getProperty("javapath") != null)
            {
                Launcher_Settings.javaPath = prop.getProperty("javapath");
            }
            
            if (prop.getProperty("jvmarguments") != null)
            {
              Launcher_Settings.jvmArguments = prop.getProperty("jvmarguments");  
            }
            
            if (prop.getProperty("debugMode") != null)
            {
                Launcher_Settings.showDebugStatus = Boolean.parseBoolean(prop.getProperty("debugMode"));
            }
            
            if (prop.getProperty("theme") != null)
            {
              Launcher_Settings.selectedTheme = prop.getProperty("theme");  
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
    }
    
    public static void setTheme(Scene sceneOptions) {
           sceneOptions.getStylesheets().clear();
        if (Launcher_Settings.selectedTheme.equals("purple") || Launcher_Settings.selectedTheme.equals("")) {
            sceneOptions.getStylesheets().add("taglauncher_3/css/purple.css");
        }
        if (Launcher_Settings.selectedTheme.equals("red")) {
            sceneOptions.getStylesheets().add("taglauncher_3/css/red.css");
        }
        if (Launcher_Settings.selectedTheme.equals("green")) {
            sceneOptions.getStylesheets().add("taglauncher_3/css/green.css");
        }
        if (Launcher_Settings.selectedTheme.equals("blue")) {
            sceneOptions.getStylesheets().add("taglauncher_3/css/blue.css");
        }
        if (Launcher_Settings.selectedTheme.equals("gray")) {
            sceneOptions.getStylesheets().add("taglauncher_3/css/gray.css");
        }
        if (Launcher_Settings.selectedTheme.equals("white")) {
            sceneOptions.getStylesheets().add("taglauncher_3/css/white.css");
        }
    }
     
}
