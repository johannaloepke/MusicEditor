package cs3500.music.model;

/**
 * Class represents a repetition of music.
 */
public class Repeat extends AFlag {
  /**
   * Creates a Repeat with given startbeat and end position.
   * @param startBeat   represents where the flag starts.
   * @param endPosition represents where the flag ends.
   */
  public Repeat(int startBeat, int endPosition) {
    super(startBeat, endPosition);
    if (endPosition < startBeat) {
      throw new IllegalArgumentException("End comes before the start");
    }
  }


  public Repeat(int startBeat, int firstEndBeat, int... endBeats) {
    super(startBeat, firstEndBeat, endBeats);
    if (endBeats[0] < startBeat) {
      throw new IllegalArgumentException("End comes before the start");
    }
    for (int i = 1; i < endBeats.length; i++) {
      if (endBeats[i - 1] > endBeats[i]) {
        throw new IllegalArgumentException("Second beat is inaccessible.");
      }
    }
  }

  @Override
  public boolean equals(Object other) {
    return super.equals(other) && (other instanceof Repeat);
  }
}
