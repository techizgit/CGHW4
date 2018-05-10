package c2g2.game;

import c2g2.engine.GameEngine;
import c2g2.engine.IGameLogic;
 
public class Main {
 
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            String inputFilename = args[0];
            IGameLogic gameLogic = new DummyGame(inputFilename);
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}