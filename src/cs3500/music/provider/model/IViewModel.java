package cs3500.music.provider.model;

import java.util.List;

/**
 * Created by amoghlaptop on 3/11/16.
 */
public interface IViewModel<NoteRep> {

  /**
   * Returns the duration of this sound track in number of beats.
   * @return the duration of sound track in number of beats.
   */
  public int getDurationInBeats();

  /**
   * Returns a list of {@code Note} played at given beat.
   * @param beat  the time in number of beats since start of sound track.
   * @return All the {@code Note} being played at given beat as a List.
   * @throws IllegalArgumentException if beat given is beyond the scope of music piece.
   */
  public List<NoteRep> getNotesAt(int beat) throws IllegalArgumentException;

  /**
   * Returns a list of {@code Note} played at given beat.
   * @return All the {@code Note} being played at given beat as a List.
   * @throws IllegalArgumentException if beat given is beyond the scope of music piece.
   */
  public List<List<NoteRep>> getAllNotes() throws IllegalArgumentException;

  /**
   * Returns the highest note in the model.
   * @return the highest note.
   */
  public NoteRep getHighest();

  /**
   * Returns the lowest note in the model.
   * @return the lowest note.
   */
  public NoteRep getLowest();

  /**
   * Returns the length of one beat in microseconds.
   * @return the duration of a beat.
   */
  public int getTempo();
}
