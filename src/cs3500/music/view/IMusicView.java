package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IFlag;

/**
 * Represents the interface of a music view.
 * Can create implementations of this interface with the ViewFactory.
 */
public interface IMusicView {
  /**
   * Play's the specified note in this view.
   * @param startTime start time of the note
   * @param endTime end time of the note
   * @param instrument instrument of the note
   * @param pitch pitch of the note
   * @param tempo of the piece
   */
  void playNote(long startTime, long endTime, int instrument, int pitch, long tempo)
          throws InvalidMidiDataException;

  /**
   * Gives a String of the current information of the View.
   * @return {@code String} representing the current info of the view.
   */
  String getCurrentState();

  /**
   * Returns an int, generally based on user input.
   * @param question represents the String representing the question being posed to the user.
   * @return Integer representing user response.
   */
  Integer intCommands(String question) throws NumberFormatException;

  /**
   * Gives a Listener that can process events from this View.
   * @param listener represents the ActionListener that processes events in this View.
   */
  void setCommandListener(ActionListener listener);

  /**
   * Changes the current state of the view.
   * @param command the command this view should change based on
   */
  void changeCurrentState(String command);

  /**
   * Gets the current beat from the view.
   * @return the current beat.
   */
  int getCurrentBeat();

  /**
   * Sets the view's beat to an integer.
   * @param beat represents the beat to set the view to.
   */
  void setCurrentBeat(int beat);

  /**
   * Closes this view.
   */
  void close();

  /**
   * Toggles playback according to given boolean flag.
   * @param on boolean to determine if playback is on.
   */
  void switchPlayback(boolean on);

  /**
   * Alters the display according to the parameters.
   * @param lowestNote lowest note to be shown in the view.
   * @param highestNote highest note to be shown in the view.
   * @param lengthOfSong length of the song.
   * @param notes the notes to be shown in this view.
   */
  void changeDisplay(int lowestNote, int highestNote, int lengthOfSong, List<Integer[]> notes);

  /**
   * Passes appropriate note information along.
   * @param start start time of a note to be displayed.
   * @param duration duration of that note.
   * @param pitch pitch of that note.
   * @param melodyOrInstrument melody or instrument of that note.
   */
  void handNotes(int start, int duration, int pitch, int melodyOrInstrument);

  /**
   * Show error message with given String.
   * @param warning warning message.
   */
  void showWarning(String warning);

  /**
   * Adds given KeyListener to this view.
   * @param l KeyListener to be added.
   */
  void addKeyListener(KeyListener l);

  /**
   * Adds given MouseListener to this view.
   * @param l MouseListener to be added.
   */
  void addMouseListener(MouseListener l);

  /**
   * Sets the position of a display.
   * @param x represents the x position of the display. Null represents no change to the x position.
   * @param y represents the y position of the display. Null represents no change to the y position.
   */
  void setCurrentDisplayPosition(Integer x, Integer y);

  /**
   * Increments the display position by a specific amount.
   * @param horizontal represents whether the change is horizontal. False is vertical.
   * @param positive   represents whether the change is positive.
   */
  void incrementCurrentDisplayPosition(boolean horizontal, boolean positive);

  /**
   * Changes the scroll policy between automatic and manual.
   * @param autoScroll is a boolean representing the scroll policy; true means auto-scroll is on.
   */
  void setScrollPolicy(boolean autoScroll);

  /**
   * Displays this view.
   */
  void display();

  /**
   * Changes the Flag events to the given list.
   * @param events represents the Flags that are being added.
   */
  void setFlagEvents(List<IFlag> events);
}
