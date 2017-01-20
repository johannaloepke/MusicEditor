package cs3500.music.provider.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Interface to represent a user-interactive view for Music Editor.
 */
public interface IInteractiveView<NoteRep> extends IView<NoteRep> {

  /**
   * Sets the {@link IView}'s MouseListener to m.
   * @param m the MouseListener to use.
   */
  void addMouseListener(MouseListener m);

  /**
   * Sets the {@link IView}'s KeyListener to k.
   * @param k the KeyListener to use.
   */
  void addKeyListener(KeyListener k);

  /**
   * Returns the Note represented at given X and Y graphical co-ordinate.
   * @param x  the X graphical co-ordinate.
   * @param y  the Y graphical co-ordinate.
   * @return   the Note at given X, Y graphical co-ordinate.
   */
  NoteRep getNoteAt(int x, int y);

  /**
   * Returns the beat represented at given X graphical Co-ordinate.
   * @param x  the X graphical co-ordinate.
   * @return the beat at give X co-ordinate.
   */
  int getBeatAt(int x);

  /**
   * Returns the beat represented at given Y graphical Co-ordinate.
   * @param y  the Y graphical co-ordinate.
   * @return the pitch at give Y co-ordinate.
   */
  int getPitchAt(int y);

  /**
   * Sets the current beat of the {@link IInteractiveView} to x.
   * @param x the beat to set to
   * @throws IllegalArgumentException if x isn't a valid beat
   */
  void setBeatAt(int x) throws IllegalArgumentException;

  /**
   * Returns the current beat displayed by the view.
   * @return the current beat for the view.
   */
  int getCurBeat();

  /**
   * Signals the view to remove a note at given beat.
   * @param note  the note to remove at given beat.
   * @param beat  the beat at which to remove the note.
   */
  void removeNote(NoteRep note, int beat);

  /**
   * Signals the view to add a note at given beat.
   * @param note  the note to play at given beat.
   * @param beat  the beat at which to play the note.
   */
  void addNote(NoteRep note, int beat);
}
