package tankrotationexample.game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    private Clip sound;

    public Sound (Clip c) {
        this.sound = c;
    }

    public void stopSound() {
        if (this.sound.isRunning()) {
            this.stopSound();
        }
    }
    public void playSound() {
        this.sound.setFramePosition(0);
        this.sound.start();
    }
    public void setLooping(int loopCount) {
        this.sound.loop(loopCount);
    }
    public void setVolume(float level) {
        FloatControl volume = (FloatControl) this.sound.getControl(FloatControl.Type.MASTER_GAIN);
    }


}
