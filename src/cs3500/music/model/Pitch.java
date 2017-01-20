package cs3500.music.model;


/**
 * A {@code Pitch} represents one of the classic musical notes.
 * CHANGELOG: Added new method getPitchAtValue that determines the correct pitch given an int.
 *            Changed getPitchAtVal() so that param 0 would equal param 12.
 */
public enum Pitch {
  A(9, "A"),
  A_SHARP(10, "A#", "Bb"),
  B(11, "B", "Cb"),
  C(0, "C", "C", "B#"),
  C_SHARP(1, "C#", "Db"),
  D(2, "D"),
  D_SHARP(3, "D#", "Eb"),
  E(4, "E", "Fb"),
  F(5, "F", "F", "E#"),
  F_SHARP(6, "F#", "Gb"),
  G(7, "G"),
  G_SHARP(8, "G#", "Ab");


  /**
   * Represents the value of this NoteName for comparisons.
   */
  public final int val;

  /**
   * Represents the standard name of this NoteName.
   */
  private final String name;

  /**
   * Represents the name of this NoteName on a flat scale.
   */
  private final String flatName;

  /**
   * Represents the name of this NoteName on a sharp scale.
   */
  private final String sharpName;

  /**
   * Creates a NoteName.
   * @param val       represents the value of the note (for comparisons).
   * @param name      represents the standard name of the note.
   * @param flatName  represents the name of the note if using a scale of flat notes.
   * @param sharpName represents the name of the note if using a scale of sharp notes.
   */
  Pitch(int val, String name, String flatName, String sharpName) {
    this.val = val;
    this.name = name;
    this.flatName = flatName;
    this.sharpName = sharpName;
  }


  /**
   * Creates a NoteName, with the sharpName equaling the normal name.
   * @param val      represents the value of the note.
   * @param name     represents the standard name of the note and the sharpName of the note.
   * @param flatName represents the name of the note if using a scale of flat notes.
   */
  Pitch(int val, String name, String flatName) {
    this(val, name, flatName, name);
  }

  /**
   * Creates a NoteName, with the  sharpName and flatName equaling the normal name.
   * @param val  represents the value of the note.
   * @param name represents the name of the note normally or in any scale.
   */
  Pitch(int val, String name) {
    this(val, name, name, name);
  }

  /**
   * Creates a {@code String} representation of this {@code NoteName} in flat notation.
   * @return a {@code String} representing this flat {@code NoteName}.
   */
  public String flatString() {
    return this.flatName;
  }

  /**
   * Creates a {@code String} representation of this {@code NoteName} in sharp notation.
   * @return a {@code String} representing this sharp {@code NoteName}.
   */
  public String sharpString() {
    return this.sharpName;
  }

  /**
   * Creates a {@code String} representation of this {@code Object}.
   * @return a {@code String} representation of this {@code Object}.
   */
  public String toString() {
    return this.name;
  }

  /**
   * Gives the {@code Pitch} following this one.
   * @return {@code Pitch} representing the next {@code Pitch} after this one.
   */
  public Pitch getNextPitch() {
    int keyVal = this.val + 1;
    if (keyVal > Pitch.values().length) {
      keyVal = 1;
    }
    return ASound.PITCHES.get(keyVal);
  }

  /**
   * Returns the correct pitch at a given value.
   * @param value represents the value being looked for.
   * @return {@code Pitch} representing the pitch at the given value.
   * @throws IllegalArgumentException if the given value doesn't exist for {@code Pitch}es.
   */
  public static Pitch getPitchAtVal(int value) {
    if (value > 12 || value < 0) {
      throw new IllegalArgumentException("This pitch doesn't exist at this value " + value + ".");
    }
    if (value == 12) {
      value = 0;
    }

    for (Pitch p : Pitch.values()) {
      if (p.val == value) {
        return p;
      }
    }
    throw new IllegalArgumentException("This pitch doesn't exist at this value" + value + ".");
  }

  /**
   * Determines which Pitch is greater. Should be used in place of compareTo();
   * @param other represents the other Pitch.
   * @return {@code int} representing which is greater. 1 means this is, -1 means other is, and 0
   *         means they are equivalent.
   */
  public int compare(Pitch other) {
    if (this.val > other.val) {
      return 1;
    }
    else if (this.val == other.val) {
      return 0;
    }
    else {
      return -1;
    }
  }
}
