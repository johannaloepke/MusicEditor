package cs3500.music.provider.util;

import java.util.List;

import cs3500.music.provider.model.ANote;
import cs3500.music.provider.model.Note;
import cs3500.music.provider.model.Tone;

/**
 * Class that converts MIDI pitch to Notes and vice versa.
 */
public class PitchNoteConverter {
  /**
   * Given an ANote, returns the MIDI number corresponding to it.
   * @param note  the given ANote.
   * @return a number representing the ANote in MIDI.
   */
  public static int noteToPitch(ANote note) {

    int ret = note.getOctave() * 12;
    List<Tone> allTones = Tone.getAllTones();

    for (int i = 0; i < allTones.size(); i++) {
      if (allTones.get(i).equals(note.getTone())) {
        break;
      }
      ret++;
    }

    return ret;
  }

  /**
   * Given a pitch, returns the Note corresponding to it.
   * @param pitch  the given pitch.
   * @return the ANote representing that pitch.
   */
  public static ANote pitchToNote(int pitch) {

    int octave = pitch / 12;
    int note = pitch % 12;
    List<Tone> allTones = Tone.getAllTones();

    return new Note(allTones.get(note), octave);
  }
}
