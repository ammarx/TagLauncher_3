/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taglauncher_3;

/**
 *
 * @author Mathew
 */
public class LauncherOptions {

    
    static String playerUsername = "Username";
    static String playerVersion = "-1";
    static boolean refreshVersionList = false;
    static boolean bypassBlacklist = true;
    static boolean keepLauncherOpen = false;
    static boolean showDebugStatus = false;
    
    static String resolutionWidth = "854";
    static String resolutionHeight = "480";
    static String ramAllocationMin = "1024";
    static String ramAllocationMax = "1024";
    static String javaPath = "";
    static String jvmArguments = "";
    
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
}
