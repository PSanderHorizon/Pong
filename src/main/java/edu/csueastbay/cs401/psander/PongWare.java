package edu.csueastbay.cs401.psander;

import edu.csueastbay.cs401.psander.engine.input.InputManager;
import edu.csueastbay.cs401.psander.engine.scenes.SceneManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Main game class. Responsible for starting the game loop
 * and initiating initial program state. Implemented
 * as a singleton to avoid reallocations in the class's
 * launcher program.
 */
public class PongWare {
    private final double _fieldWidth = 1280;
    private final double _fieldHeight = 720;

    private GraphicsContext _graphicsContext = null;
    private Timeline _timeline = null;

    // Timing
    private long _previousNano;

    // Systems
    private static PongWare _instance = null;
    private InputManager _inputManager = null;
    private SceneManager _sceneManager = null;

    private PongWare() {
        _inputManager = InputManager.getInstance();
        _sceneManager = SceneManager.getInstance();
    }

    public static PongWare getInstance() {
        if (_instance == null)
            _instance = new PongWare();

        return _instance;
    }

    public static double getFieldWidth() { return getInstance()._fieldWidth; }

    public static double getFieldHeight() { return getInstance()._fieldHeight; }

    /**
     * Reinitialize the PongWare object.
     *
     * Because this object is constructed as a singleton
     * (to cut down on excess/repeated memory allocations),
     * this method is required to ensure the game has a handle
     * to the updated graphics context.
     * @param gc The current graphics context from the game's Canvas element.
     */
    public void init(GraphicsContext gc)
    {
        _graphicsContext = gc;

        _inputManager.init();
        _sceneManager.init();

        var game = this;

        _timeline = new Timeline(new KeyFrame(Duration.millis(16.6), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.tick();
            }
        }));

        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();

        _previousNano = System.nanoTime();
    }

    /**
     * Accepts incoming keyboard input to dispatch
     * it to the proper handler.
     * @param code The current key being pressed.
     */
    public void keyDown(KeyCode code) {
        _inputManager.keyDown(code);
    }

    /**
     * Accepts incoming keyboard input to dispatch
     * it to the proper handler.
     * @param code The current key being pressed.
     */
    public void keyUp(KeyCode code) {
        _inputManager.getInstance().keyUp(code);
    }

    private void tick()
    {
        // Update the time delta.
        var currentNano = System.nanoTime();
        var elapsedNano = currentNano - _previousNano;
        var delta = elapsedNano / 1000.0 / 1000.0 / 1000.0; // Convert ns to sec

        _graphicsContext.setFill(Color.BLACK);
        _graphicsContext.fillRect(0, 0, _fieldWidth, _fieldHeight);
        
        _inputManager.update(delta);
        _sceneManager.update(delta);

        _previousNano = currentNano;
    }

    public GraphicsContext getGraphicsContext() {
        return _graphicsContext;
    }
}