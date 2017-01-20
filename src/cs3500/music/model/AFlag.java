package cs3500.music.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an abstract class for general IFlags.
 */
public abstract class AFlag implements IFlag {
  /**
   * Maps the number of repetitions (key) to the beat (value).
   */
  protected Map<Integer, Integer> startBeats = new HashMap<>();

  /**
   * Number of times through this Flag. Starts at 0.
   */
  protected int repetitions = 0;

  /**
   * Maps the number of repetitions (key) to the beat (value).
   */
  protected Map<Integer, Integer> endPositions = new HashMap<>();

  /**
   * The beat at which the a skip happens, if this Event revolves around a skip.
   */
  private Integer skipPoint = null;

  /**
   * Creates a Flag with given startbeat and end position.
   * @param startBeat   represents where the flag starts.
   * @param endPosition represents where the flag ends.
   */
  protected AFlag(int startBeat, int endPosition) {
    this.startBeats.put(0, startBeat);
    this.endPositions.put(0, endPosition);
  }

  /**
   * Creates a Flag with given startBeat, end positions, and a skip beat.
   * @param startBeat      represents where the Flag starts.
   * @param firstLoopStart represents where the Flag will skip on every repetition but the first.
   * @param endBeats       represents the different ending beats.
   */
  protected AFlag(int startBeat, int firstLoopStart, int... endBeats) {
    this.startBeats.put(0, startBeat);
    int reps = 0;
    for (int i : endBeats) {
      if (reps <= endBeats.length * 2) {
        this.endPositions.put(reps, i);
        reps++;
        this.startBeats.put(reps, i);
      }
      reps++;
    }

    for (int i = 0; i <= endBeats.length; i++) {
      if (i % 2 == 0) this.startBeats.put(i, startBeat);
      else this.endPositions.put(i, firstLoopStart);
    }

    if (this.endPositions.size() < this.startBeats.size()) {
      this.endPositions.put(endPositions.size(), firstLoopStart);
    }

    this.skipPoint = firstLoopStart;
  }

  @Override
  public int getStartBeat() {
    return this.startBeats.get(this.repetitions);
  }

  @Override
  public int getEndBeat() {
    return this.endPositions.get(this.repetitions);
  }

  @Override
  public Integer getStartBeatAt(int repetition) {
    return this.startBeats.getOrDefault(repetition, null);
  }

  @Override
  public Integer getEndBeatAt(int repetition) {
    return this.endPositions.getOrDefault(repetition, null);
  }

  @Override
  public int getOriginalStartBeat() {
    return this.startBeats.get(0);
  }

  @Override
  public int getOriginalEndBeat() {
    return this.endPositions.get(0);
  }

  @Override
  public int changeBeat(int curBeat) {
    if (this.endPositions.get(repetitions) != null && endPositions.get(repetitions) <= curBeat) {
      int beat = this.startBeats.getOrDefault(repetitions, curBeat);
      this.repetitions++;
      return beat;
    }
    return curBeat;
  }

  @Override
  public void addToStarts(int startBeat, int repetitions) {
    this.startBeats.putIfAbsent(repetitions, startBeat);
  }

  @Override
  public void addToEnds(int endBeat, int repetitions) {
    this.startBeats.putIfAbsent(repetitions, endBeat);
  }

  @Override
  public void addToEnds(int endBeat) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNumberOfRepetitions(int repetitions) {
    this.repetitions = repetitions;
  }

  @Override
  public int getEarliestBeat() {
    return this.getStartBeatAt(0);
  }

  @Override
  public int getLatestBeat() {
    int size = startBeats.entrySet().size();
    if (size == 1) {
      return this.getEndBeatAt(0);
    }
    return this.getStartBeatAt(size - 1);
  }

  @Override
  public Integer getSkipPoint() {
    return this.skipPoint;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " with starts at " + this.startBeats + " and ends at "
            + this.endPositions;
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof AFlag)
            && ((AFlag) other).startBeats.equals(this.startBeats)
            && ((AFlag) other).endPositions.equals(this.endPositions);

  }

  @Override
  public int hashCode() {
    return this.startBeats.hashCode() * this.endPositions.hashCode()
            - this.endPositions.hashCode() / 2;
  }
}
