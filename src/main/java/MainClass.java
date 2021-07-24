import processing.core.PApplet;
import window.MainWindow;
import window.SecondaryWindow;

public class MainClass {
    public static void main(String... args) {
        SecondaryWindow secondaryWindow = new SecondaryWindow();
        PApplet.runSketch(new String[]{""}, secondaryWindow);

        MainWindow mainWindow = new MainWindow();
        PApplet.runSketch(new String[]{""}, mainWindow);
    }
}
