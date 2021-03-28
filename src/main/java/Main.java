import processing.core.PApplet;

public class Main extends PApplet {

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        background(25);
        frameRate(60);
        strokeCap(ROUND);
    }

    public void draw(){
        background(25);
        rect(mouseX, mouseY, 20, 20);
    }

    public static void main(String... args){
        PApplet.main("Main");
    }
}
