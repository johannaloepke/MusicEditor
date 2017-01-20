package cs3500.music.provider.model;

import java.util.ArrayList;

/**
 * Enumeration to represent the 12 basic notes in music.
 */
public enum Tone {
  C("C"),
  C_SHARP("C#"),
  D("D"),
  D_SHARP("D#"),
  E("E"),
  F("F"),
  F_SHARP("F#"),
  G("G"),
  G_SHARP("G#"),
  A("A"),
  A_SHARP("A#"),
  B("B"),
  MUTE("");

  private String stringForm;

  /**
   * Creates a Tone with given {@code String} representation.
   * @param stringForm  the {@code String} that represents the tone.
   */
  Tone(String stringForm) {

    this.stringForm = stringForm;
  }

  /**
   * Returns the list of all Tones.
   * @return an ArrayList containing all Tones.
   */
  public static ArrayList<Tone> getAllTones() {

    ArrayList<Tone> allTones = new ArrayList<Tone>();
    allTones.add(Tone.C);
    allTones.add(Tone.C_SHARP);
    allTones.add(Tone.D);
    allTones.add(Tone.D_SHARP);
    allTones.add(Tone.E);
    allTones.add(Tone.F);
    allTones.add(Tone.F_SHARP);
    allTones.add(Tone.G);
    allTones.add(Tone.G_SHARP);
    allTones.add(Tone.A);
    allTones.add(Tone.A_SHARP);
    allTones.add(Tone.B);

    return allTones;
  }

  @Override
  public String toString() {
    return this.stringForm;
  }
}