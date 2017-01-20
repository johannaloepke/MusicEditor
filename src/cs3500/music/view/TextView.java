package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IFlag;

/**
 * Represents a textual view for a MusicEditor.
 */
public class TextView implements IMusicView {
  /**
   * Represents the information within this view.
   */
  private StringBuilder info;

  public TextView() {
    this.info = new StringBuilder();
  }

  @Override
  public void playNote(long startTime, long endTime, int instrument, int pitch, long tempo)
          throws InvalidMidiDataException {
    // Text cannot be played out loud.
  }

  @Override
  public String getCurrentState() {
    return this.info.toString();
  }

  @Override
  public Integer intCommands(String question) {
    return 0;
  }

  @Override
  public void setCommandListener(ActionListener listener) {
    // No commands on the console.
  }

  @Override
  public void changeCurrentState(String command) {
    info = new StringBuilder(command);
  }

  @Override
  public int getCurrentBeat() {
    return 0;
  }

  @Override
  public void setCurrentBeat(int beat) {
    // No current beat to set.
  }

  @Override
  public void setCurrentDisplayPosition(Integer x, Integer y) {
    // No display position to set in console.
  }

  @Override
  public void incrementCurrentDisplayPosition(boolean horizontal, boolean positive) {
    // No display to increment position.
  }

  @Override
  public void setScrollPolicy(boolean autoScroll) {
    //No scrolling for this view type.
  }

  @Override
  public void display() {
    System.out.println(this.info.toString());
  }

  @Override
  public void setFlagEvents(List<IFlag> events) {
    //Does nothing, as TextView doesn't allow for looping.
  }

  @Override
  public void close() {
    // Console is not closed.
  }

  @Override
  public void switchPlayback(boolean on) {
    // No playback on text.
  }

  @Override
  public void changeDisplay(int lowestNote, int highestNote, int lengthOfSong,
                            List<Integer[]> noteList) {
    // Do nothing.
  }

  @Override
  public void handNotes(int start, int duration, int pitch, int melodyOrInstrument) {
    // No handing note information to console.
  }

  @Override
  public void showWarning(String warning) {
    info.append("/n").append(warning);
  }

  @Override
  public void addKeyListener(KeyListener l) {
    // No KeyListener in a console.
  }

  @Override
  public void addMouseListener(MouseListener l) {
    // No MouseListener in a console.
  }
}
