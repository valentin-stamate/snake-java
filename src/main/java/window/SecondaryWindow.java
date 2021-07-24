package window;

import processing.core.PApplet;
import util.Config;

public class SecondaryWindow extends PApplet {

    @Override
    public void settings() {
        size(Config.CANVAS_WIDTH, Config.CANVAS_HEIGHT);
    }

    @Override
    public void setup() {
        surface.setLocation(displayWidth / 2 - width, displayHeight / 2 - height / 2);
        surface.setTitle("Neural Network");

        background(25);
    }

    @Override
    public void draw() {
        background(25);
    }

}
