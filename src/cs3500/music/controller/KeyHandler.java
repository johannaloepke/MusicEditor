package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a handler of key inputs, by implementing KeyListener.
 */
public class KeyHandler implements KeyListener {
  /**
   * Represents the map of all type event codes to their functions.
   */
  private Map<Integer, Runnable> typed;

  /**
   * Represents the map of all key press event codes to their function.
   */
  private Map<Integer, Runnable> pressed;

  /**
   * Represents the map of all key release event codes to their function.
   */
  private Map<Integer, Runnable> released;


  /**
   * Constructs a KeyHandler.
   */
  public KeyHandler() {
    typed = new HashMap<>();
    pressed = new HashMap<>();
    released = new HashMap<>();
  }

  /**
   * Adds a key to the typed {@code Map} event.
   * @param key    represents the Integer key for the code that triggers the event.
   * @param object represents the runnable being added.
   */
  public void addToKeyTypedMap(Integer key, Runnable object) {
    typed.put(key, object);
  }

  /**
   * Adds a key to the released {@code Map} event.
   * @param key    represents the Integer key for the code that triggers the event.
   * @param object represents the runnable being added.
   */
  public void addToKeyReleasedMap(Integer key, Runnable object) {
    released.put(key, object);
  }

  /**
   * Adds a key to the pressed {@code Map} event.
   * @param key    represents the Integer key for the code that triggers the event.
   * @param object represents the runnable being added.
   */
  public void addToKeyPressedMap(Integer key, Runnable object) {
    pressed.put(key, object);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    generalKeyEvent(typed, e);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    generalKeyEvent(pressed, e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    generalKeyEvent(released, e);
  }

  /**
   * Given a Map and a keyEvent, runs the event or returns a Runnable that does nothing..
   * @param keyMap represents the map from key to runnable.
   * @param e      represents the keyEvent.
   */
  private void generalKeyEvent(Map<Integer, Runnable> keyMap, KeyEvent e) {
    keyMap.getOrDefault(e.getExtendedKeyCode(), new Runnable() {
      @Override
      public void run() {
        //Do nothing.
      }
    }).run();
  }

  @Override
  public String toString() {
    return typed.toString() + ", " + pressed.toString() + ", " + released.toString();
  }
}
