package cs3500.music.model;

import java.util.List;
import java.util.Map;

import cs3500.music.util.CompositionBuilder;
import javafx.util.Pair;

/**
 * Contains the methods expected of a music model.
 * CHANGELOG: Added a removeMelody() method that removes a melody.
 *            Added a removeMeasure() method that removes a measure from the given melody.
 *            Removed removeMeasure() as there is no longer such notion in IMusicModel.
 *            Added a lengthOfSongInBeats() method that returns the total number of beats in a song.
 *            Notion of instrument has been added to Melody. Therefore, methods added to allow for
 *              using this notion.
 *            Now extends CompositionBuilder interface parameterized over IMusicModel.
 *            Added getHighestNote/getLowestNote methods.
 *            Added a getAllNotesWithMelodies method that is similar to getAllNotesMap() but the
 *            Map's value is a Pair with the Note keyed to the melody it exists in.
 * HW08:      Added a getEvents() method that allows for returning every flag in this piece.
 *            Added a getEvent() method that returns a flag at a specific beat.
 */
public interface IMusicModel extends CompositionBuilder<IMusicModel> {
  /**
   * Wrapper for {@code ASound}. Allows the Model to know whether a note has finished playing.
   */
  class ActiveSound {
    /**
     * Represents the Sound being played.
     */
    Note sound;

    /**
     * Represents the amount of time left for the note to play.
     */
    double durationLeft;

    ActiveSound(Note sound) {
      this.sound = sound;
      this.durationLeft = sound.finalNoteLength();
    }

    /**
     * Gets the duration remaining.
     * @return {@code double} representing duration remaining.
     */
    protected double getDuration() {
      return this.durationLeft;
    }

    /**
     * Gives the sound associated with the given object.
     * @return {@code Note} representing the Note wrapped in this.
     */
    protected Note getNote() {
      return this.sound;
    }

    /**
     * Decreases the duration by the given amount.
     * @param amount represents the amount to decrease the duration by.
     */
    protected void decreaseDuration(double amount) {
      this.durationLeft = this.durationLeft - amount;
    }

    /**
     * Determines if this {@code ASound} has any duration left.
     * @return {@code boolean} representing whether this ASound has any duration left.
     */
    protected boolean hasDurationLeft() {
      return this.durationLeft > 0;
    }

    @Override
    public String toString() {
      return "[" + this.getNote().toString() + ", " + this.durationLeft + "]";
    }
  }


  /**
   * Adds an {@code Melody} so that it will play from the particular beat onward.
   * @param other      represents the {@code Melody}'s index.
   * @param mergeIndex represents the index at which the other {@code Melody} should stop being new
   *                   {@code Rest}s and start playing the music.
   */
  void addTracks(Melody other, int mergeIndex);

  /**
   * Adds two tracks (see above) at index 0.
   * @param other represents the other Melody to be merged.
   */
  void addTracks(Melody other);

  /**
   * Adds a Melody that gets {@code Rest}s until the given beat.
   * @param other  represents the {@code MusicModel} being merged with this.
   * @param beat represents the beat to add to.
   */
  void mergeMusic(IMusicModel other, int beat);

  /**
   * Allows access of the melodies making up this IMusicModel.
   */
  List<Melody> getMelodies();

  /**
   * Adds a {@code Melody} to the end of another melody.
   * @param other represents the other {@code Melody} to be added.
   */
  void addToEnd(Melody other);

  /**
   * Removes a {@code Melody}.
   * @param melodyIndex represents the index of the {@code Melody} to be removed.
   */
  void removeMelody(int melodyIndex);

  /**
   * Adds a Melody in at a particular measure, by making the others become Rests for the duration
   * of the new Melody.
   * @param beat   represents the beat at which the Melody will be added.
   * @param melody represents the Melody to be added.
   */
  void spliceTracks(int beat, Melody melody);


  /**
   * Adds a Note to the end of the given Melody.
   * @param  note represents the {@code ASound} being added.
   * @param  melodyIndex represents the index of the melody added to.
   * @param  beat represents the beat to add the note at.
   * @throws IllegalArgumentException if any argument is invalid.
   */
  void addNote(ASound note, int melodyIndex, int beat);

  /**
   * Swaps a note in for another.
   * @param note         represents the {@code ASound} being added.
   * @param melodyIndex  represents the index of the melody the swap will happen within.
   * @param beat    represents the beat the swap will happen within.
   */
  void swapNote(ASound note, int melodyIndex, int beat);

  /**
   * Replaces a Note with a Rest at a given beat.
   * @param removed the note to be removed.
   * @param beat the beat it is being removed at.
   */
  void removeNote(Note removed, int beat);

  /**
   * Sets given melody to an instrument.
   * @param instrument  represents the instrument being set to.
   * @param melodyIndex represents the index of the melody that's having its instrument changed.
   */
  void setInstrument(int instrument, int melodyIndex);


  /**
   * Changes the volume by the given amount.
   * @param changeAmount represents the amount to change the volume by.
   */
  void changeVolume(int changeAmount);

  /**
   * Sets the volume to the given amount.
   * @param newVolume represents the new volume.
   */
  void setVolume(int newVolume);

  /**
   * Merges two tracks so that they become one.
   * @param trackToMerge    represents the index of the track being merged with.
   * @param positionToMerge represents the first position the new {@code Melody} will be played.
   * @param melody          represents the {@code Melody} being added.
   * @throws IllegalArgumentException if any of the parameters are invalid.
   */
  void mergeTracks(int trackToMerge,
                   int positionToMerge,
                   Melody melody);

  /**
   * Returns the name of the {@code ASound} at a given position.
   * @param melodyIndex  represents the index of the melody the note exists at.
   * @param noteIndex    represents the index of the note at the beat the note exists at.
   * @return             {@code ASound} at given info.
   * @throws             IllegalArgumentException if any argument is invalid.
   */
  ASound getNote(int melodyIndex, int noteIndex);

  /**
   * Gives every Sound in this that falls on a standard beat.
   * @return {@code Map} of {@code Integer}'s to {@code List} of {@code ASound}'s, representing
   *         every sound at a given beat.
   */
  Map<Integer, List<ASound>> getAllSounds();

  /**
   * Contains a List of every Note that falls on a given beat.
   * @return {@code Map} of {@code Integer}'s to {@code List} of Notes, representing every note at
   *         a given beat.
   */
  Map<Integer, List<Note>> getAllNotesMap();

  /**
   * Creates a Map keyed with Integer representing given beats, where the Value is a List of a Pair
   * of every Note that falls on a given beat keyed to its melody; highest level of information
   * available about what notes play, where they play, and in what melody they exist in.
   * @return Map keyed with an Integer tied to a List of Pair of Integers to Notes.
   */
  Map<Integer, List<Pair<Integer, Note>>> getAllNotesWithMelodies();

  /**
   * Creates a List of Pairs of every Note keyed to the beat it starts at.
   * @return {@code List} of {@code Pair<Integer, Note>}
   */
  List<Pair<Integer, Note>> getNoteList();

  /**
   * Returns the current game state. Only finds notes that rest on beats, and doesn't display every
   * note.
   * @return {@code String} representing the current game state.
   */
  String getGameState();

  /**
   * Represents the total number of beats in a song.
   */
  int lengthOfSongInBeats();

  /**
   * Represents the highest Note in this.
   */
  Note getHighestNote();

  /**
   * Represents the lowest Note in this.
   */
  Note getLowestNote();

  /**
   * Represents the number of microseconds per beat.
   */
  long microsecondsPerBeat();

  /**
   * Returns the list of Flags associated with this IMusicModel.
   * @return a List of IFlags representing the events existing on this IMusicModel.
   */
  List<IFlag> getEvents();

  /**
   * Returns the Flag that exists in this event at given beat, or null if there is no such Flag.
   * @param originalStart represents the first possible starting point for this event.
   * @return              the original starting position for this event.
   */
  IFlag getEvent(int originalStart);

  /**
   * Adds a Flag to this model's list of events, then returns the Flag.
   * @param  added represents the Flag being added.
   * @return {@param added}.
   */
  IFlag addEvent(IFlag added);
}
