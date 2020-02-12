import gui.MainWindow;
//import http.HttpRequest;

class rnd {

  public static void main(final String[] args) throws Exception {

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createGUI();
      }
    });
  }

  private static void createGUI() {
    final MainWindow w = new MainWindow();
    w.addDebugMessage("Initialization complete");
  }
}
