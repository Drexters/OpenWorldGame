package se.liu.samgu876_mellu161.main;

import javax.swing.*;

import static se.liu.samgu876_mellu161.utilz.Constants.TARGET_FPS;

/**
 * Sets up the game window and contains the main game loop.
 * Responsible for starting and running the game's update and render cycle.
 */

public class GameContainer implements Runnable {
    private GameManager gameManager;
    private JFrame frame = new JFrame();

    public GameContainer(GameManager gm){
        this.gameManager = gm;
    }

    public void setUpFrame() {
        frame.setTitle("Game");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameManager);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void startGame() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double timePerFrame = 1_000_000_000.0d/TARGET_FPS;
        long previousTime = System.nanoTime();
        double delta = 0;

        while (true){

            long currentTime = System.nanoTime();
            delta += (currentTime-previousTime)/timePerFrame;
            previousTime = currentTime;

            if (delta >= 1){
                // gameManager.updateGame();
		gameManager.repaint();
                delta--;
            }
        }
    }
}
