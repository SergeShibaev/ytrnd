import gui.MainWindow;

class rnd {

  public static void main(final String[] args) throws Exception {

    javax.swing.SwingUtilities.invokeLater(() -> createGUI());
  }

  private static void createGUI() {
    MainWindow w = new MainWindow();
    w.addDebugMessage("Initialization complete");
  }
}
