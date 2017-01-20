package cs3500.music.provider.view;

/**
 * Interface for a {@link IView} for MusicEditor that is time sensitive.
 */
public interface ISyncView<NoteRep> extends IView<NoteRep> {
  /**
   * Gets the current time of the object in beats.
   * @return the current time of the object in beats.
   */
  int getTime();

  /**
   * Sets the current beat of the {@link IInteractiveView} to x.
   * @param x the beat to set to
   * @throws IllegalArgumentException if x isn't a valid beat
   */
  void setBeatAt(int x) throws IllegalArgumentException;

  /**
   * Signals the view to remove a note at given beat.
   * @param note  the note to remove at given beat.
   * @param beat  the beat at which to remove the note.
   */
  void removeNote(NoteRep note, int beat);

  /**
   * Signals the view to add a note at given beat.
   * @param note  the note to add at given beat.
   * @param beat  the beat at which to add the note.
   */
  void addNote(NoteRep note, int beat);
}
