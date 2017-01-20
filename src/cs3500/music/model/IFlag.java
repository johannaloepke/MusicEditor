package cs3500.music.model;

/**
 * Created by owner on 12/9/16.
 * Represents a class that alerts a musical piece to take an action, such as repeating or restarting
 *  a section of music.
 */
public interface IFlag {
  /**
   * Returns the relevant starting point for this Flag.
   * @return int representing the point this flag starts at.
   */
  int getStartBeat();

  /**
   * Returns the relevant ending point for this Flag.
   * @return int representing the beat where this Flag ends.
   */
  int getEndBeat();

  /**
   * Returns where the first start beat is, if there are multiples.
   * @return int representing where this flag's "original" start, meaning where it goes the first
   * time.
   */
  int getOriginalStartBeat();

  /**
   * Returns where the first end beat is, if there are multiples.
   * @return int representing this flag's "original" end, meaning where it stops the first time.
   */
  int getOriginalEndBeat();

  /**
   * Returns the start beat at a given repetition point.
   * @param repetition represents the number of times to go through the song before the returned
   *                   beat would be the start beat.
   * @return the start beat at the given repetition, or null if there is no such beat.
   */
  Integer getStartBeatAt(int repetition);

  /**
   * Returns the end beat at a given repetition point.
   * @param repetition represents the number of times to go through the song before the returned
   *                   beat would be the start beat.
   * @return the end beat at the given repetition, or null if there is no such beat.
   */
  Integer getEndBeatAt(int repetition);

  /**
   * Returns what beat to move the music to, given the current beat.
   * @param curBeat represents the current beat.
   * @return {@code int} representing the beat to set to.
   */
  int changeBeat(int curBeat);

  /**
   * Adds a beat to the list of starting beats, given the number of repetitions before that beat
   * is activated.
   * @param startBeat   represents the beat being added to the list of event beats.
   * @param repetitions represents the number of times the song needs to be played through before
   *                    this event is triggered.
   */
  void addToStarts(int startBeat, int repetitions);

  /**
   * Adds a beat to a list of ending beats, given the number of repetitions before that beat is
   * activated.
   * @param endBeat     represents the beat being added to the list of event beats.
   * @param repetitions represents the number of times the song needs to be played through before
   *                    the event is activated.
   */
  void addToEnds(int endBeat, int repetitions);

  /**
   * Adds a beat to a list of ending beats, given the number of repetitions before that beat is
   * activated.
   * @param endBeat     represents the beat being added to the list of event beats.
   */
  void addToEnds(int endBeat);

  /**
   * Sets the number of times we've hit the end beat.
   * @param repetitions represents the number of times we've hit the end beat.
   */
  void setNumberOfRepetitions(int repetitions);

  /**
   * Gives the earliest beat in this Flag.
   * @return int representing the earliest beat in this Flag.
   */
  int getEarliestBeat();

  /**
   * Gives the latest beat in this Flag.
   * @return int representing the latest beat in this Flag.
   */
  int getLatestBeat();

  /**
   * Gives the point which is skipped for any number of repetitions.
   * @return the point which is skipped for any number of repetitions.
   */
  Integer getSkipPoint();
}
