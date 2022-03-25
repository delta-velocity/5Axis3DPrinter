package app;

import engine.Application;
import engine.ApplicationInterface;

public class TheWindow {

   public static void display(String model_path) {
      try {
         boolean vSync = true;
         ApplicationInterface gameLogic = new DummyGame(model_path);
         Application app = new Application("app", 600, 480, vSync, gameLogic);
         app.run();
      } catch (Exception excp) {
         excp.printStackTrace();
         System.exit(-1);
      }
   }
}