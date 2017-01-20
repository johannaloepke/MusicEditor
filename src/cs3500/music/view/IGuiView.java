package cs3500.music.view;

import java.awt.event.KeyListener;

/**
 * Represents the interface for a GUI view.
 */
public interface IGuiView extends IMusicView {

  /**
   * Adds the given keyListener to this IGUIView.
   * @param l keyListener to be added.
   */
  void addKeyListener(KeyListener l);

  /**
   * Determines whether given beat is visible on-screen.
   * @param  beat represents the given beat.
   * @return whether given beat is visible on-screen.
   */
  boolean beatIsVisible(int beat);
}
