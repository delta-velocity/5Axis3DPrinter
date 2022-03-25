package engine;

public class Application {
   public static final int TARGET_FPS = 75;

   public static final int TARGET_UPS = 30;

   private final Window window;

   private final Timer timer;

   private final ApplicationInterface app;

   public Application(String windowTitle, int width, int height, boolean vSync, ApplicationInterface app) throws Exception {
      window = new Window(windowTitle, width, height, vSync);
      this.app = app;
      timer = new Timer();
   }

   public void run() {
      try {
         init(window);
         loop();
      } catch (Exception excp) {
         excp.printStackTrace();
      } finally {
         cleanup();
      }
   }

   private void cleanup() {
   }

   protected void init(Window window) throws Exception {
      window.init();
      timer.init();
      app.init(window);
   }

   protected void loop() {
      float elapsedTime;
      float accumulator = 0f;
      float interval = 1f / TARGET_UPS;
   
      boolean running = true;
      while (running && !window.windowShouldClose()) {
         elapsedTime = timer.getElapsedTime();
         accumulator += elapsedTime;
      
         input();
      
         while (accumulator >= interval) {
            update(interval);
            accumulator -= interval;
         }
      
         render();
      
         if (!window.isvSync()) {
            sync();
         }
      }
   }

   private void sync() {
      float loopSlot = 1f / TARGET_FPS;
      double endTime = timer.getLastLoopTime() + loopSlot;
      while (timer.getTime() < endTime) {
         try {
            Thread.sleep(1);
         } catch (InterruptedException ie) {
         }
      }
   }

   protected void input() {
      app.input(window);
   }

   protected void update(float interval) {
      app.update(interval);
   }

   protected void render() {
      app.render(window);
      window.update();
   }
}
