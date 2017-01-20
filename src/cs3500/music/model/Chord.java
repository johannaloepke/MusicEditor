package cs3500.music.model;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import cs3500.music.provider.model.Tone;

/**
 * A Chord is a List of Notes that have the same duration.
 * CHANGELOG: Added implementation of makeChord as per ASound's changelog.
 */
public class Chord extends ASound implements Iterable<Note> {
  /**
   * Represents the list of notes in this Chord.
   */
  private List<Note> notes;

  /**
   * Creates a Chord.
   * @param duration represents the duration of the Chord, as a {@code Fraction}.
   * @param volume   represents the volume of the Chord, as an {@code int}.
   * @param names    represents the names of every Note added, mapped to an Octave.
   */
  public Chord(int duration, int volume, Map<Pitch, Integer> names) {
    if (duration <= 0) {
      throw new IllegalArgumentException("Attempted to give duration: " + duration);
    }
    notes = new ArrayList<Note>();
    for (Map.Entry<Pitch, Integer> entry : names.entrySet()) {
      Note note = new Note(entry.getKey(), volume, entry.getValue(), duration);
      note.setInstrument(getInstrument());
      notes.add(note);
    }
    this.duration = duration;
  }

  /**
   * Creates a Chord.
   * @param duration represents the duration every note in the Chord will take.
   * @param volume   represents the volume.
   * @param notes    represents the new Notes being added to the Chord.
   */
  public Chord(int duration, int volume, List<Note> notes) {
    if (duration <= 0) {
      throw new IllegalArgumentException("Attempted to give duration: " + duration);
    }
    this.notes = notes;
    for (Note note : this.notes) {
      note.setVolume(volume);
      note.setDuration(duration);
      note.setInstrument(getInstrument());
    }
    this.duration = duration;
  }

  /**
   * Creates a Chord.
   * @param duration represents the duration every note in the Chord will take.
   * @param oldChord represents the old Chord.
   * @param notes    represents the new Notes being added to the Chord.
   */
  public Chord(int duration, Chord oldChord, Note... notes) {
    if (duration <= 0) {
      throw new IllegalArgumentException("Attempted to give duration: " + duration);
    }
    this.notes = new ArrayList<Note>();

    for (Note note : notes) {
      this.notes.add(note);
      note.setDuration(duration);
      note.setInstrument(getInstrument());
    }
    for (Note note : oldChord) {
      this.notes.add(note);
      note.setDuration(duration);
      note.setInstrument(getInstrument());
    }
    this.duration = duration;
  }

  /**
   * Creates a Chord.
   * @param duration represents the duration every note in the Chord will take.
   * @param notes    represents the new Notes being added to the Chord.
   */
  public Chord(int duration, Note... notes) {
    if (duration <= 0) {
      throw new IllegalArgumentException("Attempted to give duration: " + duration);
    }
    this.notes = new ArrayList<Note>();

    for (Note note : notes) {
      this.notes.add(note);
      note.setDuration(duration);
      note.setInstrument(getInstrument());
    }
    this.duration = duration;
  }

  /**
   * Changes the Octave of every Note in this Chord.
   * @param octaveDifference represents what to add to the old Octave.
   */
  public void changeOctave(int octaveDifference) {
    for (Note note : notes) {
      note.changeOctave(octaveDifference);
    }
  }

  /**
   * Allows for use in forEach loops.
   * @return the Iterator that allows for use in forEach loops.
   */
  public Iterator<Note> iterator() {
    return this.notes.iterator();
  }

  public boolean isRest() {
    return false;
  }

  @Override
  public List<String> getName() {
    List<String> list = new ArrayList<>();
    for (Note note : this) {
      list.add(note.toString());
    }
    return list;
  }

  @Override
  public boolean higherThan(ASound other) {
    return this.getHighestNote().higherThan(other);
  }

  @Override
  public boolean lowerThan(ASound other) {
    return this.getLowestNote().lowerThan(other);
  }

  @Override
  public Note getHighestNote() {
    if (this.notes.size() == 0) {
      throw new IllegalArgumentException("There are no notes");
    }
    Note note = this.notes.get(0);
    for (Note n : this) {
      if (n.higherThan(note)) {
        note = n;
      }
    }
    return note;
  }

  @Override
  public Note getLowestNote() {
    if (this.notes.size() == 0) {
      throw new IllegalArgumentException("There are no notes");
    }
    Note note = this.notes.get(0);
    for (Note n : this.notes) {
      if (n.lowerThan(note)) {
        note = n;
      }
    }
    return note;
  }

  @Override
  public Chord makeChord(Note note) {
    return new Chord(this.duration, this, note);
  }

  @Override
  public List<ASound> getSoundsIn() {
    List<ASound> sounds = new ArrayList<ASound>();
    for (Note note : this.notes) {
      sounds.add(note);
    }
    return sounds;
  }

  @Override
  public int getOctave() {
    throw new UnsupportedOperationException("Chords have no octaves.");
  }

  @Override
  public Tone getTone() {
    throw new UnsupportedOperationException("Chords have no tones.");
  }

  @Override
  public int hashCode() {
    return this.notes.hashCode() * this.duration * this.volume;
  }

  @Override
  public boolean equals(Object other) {
    if (! (other instanceof Chord)) {
      return false;
    }
    else {
      return ((Chord) other).notes.equals(this.notes)
              && ((Chord) other).duration == this.duration
              && ((Chord) other).volume == this.volume;
    }
  }

  @Override
  public String toString() {
    return "Chord with notes " + this.notes;
  }
}
