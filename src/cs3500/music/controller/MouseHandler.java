package cs3500.music.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents a handler of mouse inputs, by implementing MouseListener.
 */
public class MouseHandler implements MouseListener {
  /**
   * Represents the last position a mouse event happened upon.
   */
  private Point lastClickPosition;

  /**
   * Represents the click method.
   */
  private Runnable clickMethod;

  /**
   * Represents the release method.
   */
  private Runnable releaseMethod;

  /**
   * Represents the press method.
   */
  private Runnable pressMethod;

  /**
   * Constructs a MouseHandler.
   * @param clickMethod method for clicking
   * @param releaseMethod method for releasing
   * @param pressMethod method for pressing
   */
  public MouseHandler(Runnable clickMethod, Runnable releaseMethod, Runnable pressMethod) {
    this.clickMethod = clickMethod;
    this.releaseMethod = releaseMethod;
    this.pressMethod = pressMethod;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.lastClickPosition = e.getPoint();
    clickMethod.run();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.lastClickPosition = e.getPoint();
    pressMethod.run();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    this.lastClickPosition = e.getPoint();
    releaseMethod.run();
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // no reaction to mouse entering the screen
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // no reaction to mouse exiting the screen
  }

  /**
   * Gives the information on the last click event.
   * @return {@code Point} representing where the last click event happened.
   */
  public Point getPoint() {
    return lastClickPosition;
  }
}
