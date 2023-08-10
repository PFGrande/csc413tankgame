package tankrotationexample.game;

public interface PowerUp {
    //public void activatePowerUp(Tank tank); // powerup activation will be handled by tank collision method
    boolean expired(); // used in GameWorld
    boolean isActive(int currentLives); // used in tank
    void setActivationHealth(int activationHealth);
}
