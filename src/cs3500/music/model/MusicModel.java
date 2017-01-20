package cs3500.music.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import cs3500.music.util.CompositionBuilder;
import javafx.util.Pair;

/**
 * Creates a Model for handling {@code Music}.
 * CHANGELOG: Added a removeMelody() method that removes a melody.
 *            Made the beat parameter final.
 *            Changed ASound.durationLeft to use Note.finalNoteLength() rather than the Note's
 *              duration changed to a double.
 *            Notion of instrument has been added to Melody. Therefore, method added to allow for
 *              using this notion as per IMusicModel.
 *            Implemented methods of CompositionBuilder interface, as IMusicModel now extends that
 *              interface.
 *            Added method to get every Note paired to the integer of the Melody it exists at. This
 *              method gives the highest level of information from the model.
 *            Changed getAllNotesMap to run from getAllNotesAtMelodyPrivate, to lessen repeated
 *            code.
 * HW07     : Change the internal logic of addNote to allow for creating chords when possible.
 *            Made several parameter names more explicit to clearly indicate that they are adding
 *              notes to a particular beat, not a List's index.
 *            Added method getNoteList() that allows for more quickly getting lists of notes.
 *            Changed methods getLowestNote() and getHighestNote() to significantly increase
 *              run speed of all methods that use these.
 *            Changed method getAllNotesMap() to significantly improve run speed for larger pieces.
 */
public class MusicModel implements IMusicModel {

  /**
   * Allows for building a Composition without knowing of the MusicModel.
   */
  public static class Builder implements CompositionBuilder<MusicModel> {
    IMusicModel model = new MusicModel(4, 100);

    @Override
    public MusicModel build() {
      return (MusicModel)model;
    }

    @Override
    public CompositionBuilder<MusicModel> setTempo(int tempo) {
      model.setTempo(tempo);
      return this;
    }

    @Override
    public CompositionBuilder<MusicModel> addNote(int start, int end, int instrument,
                                                  int pitch, int volume) {
      model.addNote(start, end, instrument, pitch, volume);
      return this;
    }
  }

  /**
   * Represents the different melodies that make up this piece of music.
   */
  private List<Melody> melodies;

  /**
   * Represents how much to scale the volume by. Number is from 0 to 1.
   */
  private double volumeMult;

  /**
   * Represents number of beats per measure.
   */
  private int beat = 4;

  /**
   * Represents the tempo in microseconds per beat.
   */
  private int tempo;

  /**
   * Represents the events in this model.
   */
  private List<IFlag> events = new ArrayList<>();

  /**
   * Creates a MusicModel out of a {@code List} of {@code Melodies}.
   * @param melodies   represents the melodies to be added.
   * @param volumeMult represents the volume of the whole model
   */
  public MusicModel(List<Melody> melodies, int volumeMult) {
    if (volumeMult > 100) {
      throw new IllegalArgumentException("Max volume multiplier is 100");
    }

    this.melodies = new ArrayList<Melody>();
    for (Melody m : melodies) {
      melodies.add(m);
    }

    this.volumeMult = volumeMult / 100.0;

    for (Pitch p : Pitch.values()) {
      ASound.PITCHES.put(p.val, p);
    }
  }

  /**
   * Creates a MusicModel out of a {@code List} of {@code Melodies}.
   * @param melodies   represents the melodies to be added.
   * @param volumeMult represents the volume of the whole model
   */
  public MusicModel(int beat, int volumeMult, Melody... melodies) {
    if (volumeMult > 100) {
      throw new IllegalArgumentException("Max volume multiplier is 100");
    }

    this.melodies = new ArrayList<Melody>();
    for (Melody m : melodies) {
      this.melodies.add(m);
    }

    this.volumeMult = volumeMult / 100.0;

    this.beat = beat;

    for (Pitch p : Pitch.values()) {
      ASound.PITCHES.put(p.val, p);
    }
  }


  @Override
  public IMusicModel build() {
    return this;
  }

  @Override
  public CompositionBuilder<IMusicModel> setTempo(int tempo) {
    this.tempo = tempo;
    return this;
  }

  /**
   * Adds a note with the given parameters to this model.
   * @param start The start time of the note, in beats.
   * @param end The end time of the note, in beats.
   * @param instrument The instrument number (to be interpreted by MIDI).
   * @param pitch The pitch (in the range [0, 127], where 60 represents C4, the middle-C on a
   *              piano).
   * @param volume The volume (in the range [0, 127]).
   * @return this with the note of given parameters added.
   */
  public CompositionBuilder<IMusicModel> addNote(int start, int end, int instrument,
                                                 int pitch, int volume) {
    if (end - start == 0) {
      return this;
    }

    Note newNote = new Note(Pitch.getPitchAtVal(pitch % 12),
            (pitch / 12), volume, end - start, instrument);

    Melody cur = null;
    for (Melody m : this.melodies) {
      if (m.getInstrument() == instrument
              && (m.numOfBeats() <= start
                  || (m.getIndexOfBeat(start) >= 0
                  && !m.noteStartsAtExactBeat(start)
                  && (!m.hasNotePlayingAt(start))
                    || m.noteStartsAtExactBeat(start)
                      && m.getNoteAtBeat(start).getDuration() == newNote.getDuration())
                  && !m.hasNotePlayingAt(end - 1))) {
        cur = m;
        break;
      }
    }
    if (cur == null) {
      cur = new Melody(50, instrument);
      this.melodies.add(cur);
    }

    addNoteToProperMelody(newNote, cur, start);

    return this;
  }


  @Override
  public void addNote(ASound note, int melodyIndex, int beat) {
    if (melodyIndex > melodies.size() - 1) {
      throw new IllegalArgumentException("The melody index is out of bounds.");
    }
    melodies.get(melodyIndex).addNoteToBeat(note, beat);
  }

  /**
   * Adds note to the given melody, or a new melody if the given melody isn't correct for adding.
   * @param newNote    represents the new note being added.
   * @param cur        represents the current melody.
   * @param start      represents the start index.
   */
  private void addNoteToProperMelody(ASound newNote, Melody cur, int start) {
    cur.addNoteToBeat(newNote, start);
  }

  @Override
  public void addTracks(Melody melody, int beat) {
    this.privateAddTracks(melody, beat);
  }

  @Override
  public void addTracks(Melody other) {
    this.privateAddTracks(other, 0);
  }

  @Override
  public void mergeMusic(IMusicModel other, int beat) {
    for (Melody m : other.getMelodies()) {
      this.privateAddTracks(m, beat);
    }
  }

  @Override
  public List<Melody> getMelodies() {
    return this.melodies;
  }

  /**
   * Helper for the Public {@code mergeTracks} methods, in a private method to prevent mismatching
   * of the different versions of mergeTracks.
   * @param melody represents the {@code Melody} being added.
   * @param beats represents the beat to add to.
   */
  private void privateAddTracks(Melody melody, int beats) {
    if (beats > 0) {
      melody.addNote(new Rest(beats), 0);
    }
    this.melodies.add(melody);
  }

  @Override
  public void addToEnd(Melody other) {
    this.privateAddTracks(other, longestTrack().numOfBeats());
  }

  @Override
  public void removeMelody(int melodyIndex) {
    this.melodies.remove(melodyIndex);
  }

  @Override
  public void spliceTracks(int beat, Melody melody) {

    for (Melody m : this.melodies) {
      m.addNote(new Rest(melody.numOfBeats()), beat);
    }

    melody.addNote(new Rest(beat), 0);

    this.melodies.add(melody);
  }

  @Override
  public void swapNote(ASound note, int melodyIndex, int beat) {
    melodies.get(melodyIndex).swapNote(note, beat);
  }

  @Override
  public void removeNote(Note removed, int beat) {
    for (Melody m : melodies) {
      if (m.getInstrument() == removed.getInstrument() &&
              m.noteStartsAtExactBeat(beat) && m.getNoteAtBeat(beat).equals(removed)) {
        m.swapNote(new Rest(removed.getDuration()), beat);
        return;
      }
    }
  }

  @Override
  public void setInstrument(int instrument, int melodyIndex) {
    this.melodies.get(melodyIndex).changeInstrument(instrument);
  }

  @Override
  public void changeVolume(int changeAmount) {
    this.volumeMult += changeAmount / 100.0;
    if (volumeMult > 1) {
      volumeMult = 1;
    }
    if (volumeMult < 0) {
      volumeMult = 0;
    }
  }

  @Override
  public void setVolume(int newVolume) {
    if (newVolume > 100 || newVolume < 0) {
      throw new IllegalArgumentException("This volume is outside acceptable range.");
    }
    this.volumeMult = newVolume / 100.0;
  }

  @Override
  public void mergeTracks(int trackToMerge,
                          int positionToMerge,
                          Melody melody) {
    if (trackToMerge >= this.melodies.size()) {
      throw new IllegalArgumentException("Index out of bounds.");
    }
    this.melodies.get(trackToMerge).merge(positionToMerge, melody);
  }

  @Override
  public ASound getNote(int melodyIndex, int noteIndex) {
    try {
      return melodies.get(melodyIndex).getNote(noteIndex);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("This was given out-of-bounds indeces.");
    }
  }

  @Override
  public Map<Integer, List<ASound>> getAllSounds() {
    return this.getAllSoundsPrivate();
  }

  private Map<Integer, List<ASound>> getAllSoundsPrivate() {
    Map<Integer, List<ASound>> allNotes = new TreeMap<Integer, List<ASound>>();
    int beatCount = 0;
    while (beatCount <= this.longestTrack().numOfBeats()) {
      List<ASound> sounds = new ArrayList<>();
      for (Melody current : this.melodies) {
        try {
          for (ASound s : current.getNoteAtBeat(beatCount).getSoundsIn()) {
            sounds.add(s);
          }
        } catch (IllegalArgumentException e) {
          //do nothing.
        }
      }

      if (allNotes.get(beatCount) == null && !sounds.isEmpty()) {
        allNotes.put(beatCount, sounds);
      }
      else if (!sounds.isEmpty()) {
        allNotes.get(beatCount).addAll(sounds);
      }

      beatCount++;
    }

    return allNotes;
  }

  @Override
  public synchronized Map<Integer, List<Note>> getAllNotesMap() {
    return this.getAllNotesPrivate();
  }

  /**
   * Gets every note, without the melody it exists in.
   * @return a {@code Map} of {@code Integer}s to {@code List}s of every note at the given integer.
   */
  private Map<Integer, List<Note>> getAllNotesPrivate() {
    Map<Integer, List<Note>> allNotes = new TreeMap<Integer, List<Note>>();
    int beatCount = 0;
    int length = this.longestTrack().numOfBeats();
    for (Melody current : this.melodies) {
      int beat = 0;
      for (ASound s : current.getMelodySounds()) {
        allNotes.putIfAbsent(beat, new ArrayList<>());
        allNotes.get(beat).addAll(s.getNotesIn());
        beat += s.getDuration();
      }
    }
    return allNotes;
  }

  @Override
  public Map<Integer, List<Pair<Integer, Note>>> getAllNotesWithMelodies() {
    return this.getAllNotesPrivateWithMelody();
  }

  @Override
  public List<Pair<Integer, Note>> getNoteList() {
    List<Pair<Integer, Note>> noteList = new ArrayList<>();
    for (Melody m : this.melodies) {
      int beat = 0;
      for (ASound s : m.getMelodySounds()) {
        for (Note n : s.getNotesIn()) {
          int i = 1;
          while (noteList.size() > i && noteList.get(i - 1).getKey() < beat) {
            i++;
          }
          noteList.add(i - 1, new Pair<>(beat, n));
        }
        beat += s.getDuration();
      }
    }
    return noteList;
  }

  /**
   * Gives every note that exists at a given beat paired to the melody it exists at. Essentially
   * contains the highest level of information relevant to the model possible, without filtering.
   * @return {@code Map} of Integers to Lists of Pairs of  Integers to ASounds.
   */
  private Map<Integer, List<Pair<Integer, Note>>> getAllNotesPrivateWithMelody() {
    Map<Integer, List<Pair<Integer, Note>>> allNotes
            = new TreeMap<Integer, List<Pair<Integer, Note>>>();
    int melodyCount = 0;
    for (Melody melody : this.melodies) {
      int beatCount = 0;
      for (ASound s : melody.getMelodySounds()) {
        for (Note n : s.getNotesIn()) {
          allNotes.putIfAbsent(beatCount, new ArrayList<Pair<Integer, Note>>());
          allNotes.get(beatCount).add(new Pair<Integer, Note>(melodyCount, n));
        }
        beatCount += s.getDuration();
      }
      melodyCount++;
    }

    return allNotes;
  }

  @Override
  public int lengthOfSongInBeats() {
    return this.longestTrack().numOfBeats();
  }

  @Override
  public Note getHighestNote() {
    Note highestNote = null;
    for (Melody m : this.melodies) {
      for (ASound s : m.getMelodySounds()) {
        for (Note n : s.getNotesIn()) {
          if (highestNote == null || n.higherThan(highestNote)) {
            highestNote = n;
          }
        }
      }
    }
    return highestNote;
  }

  @Override
  public Note getLowestNote() {
    Note lowestNote = null;
    for (Melody m : this.melodies) {
      for (ASound s : m.getMelodySounds()) {
        for (Note n : s.getNotesIn()) {
          if (lowestNote == null || n.lowerThan(lowestNote)) {
            lowestNote = n;
          }
        }
      }
    }
    return lowestNote;
  }

  @Override
  public long microsecondsPerBeat() {
    int tempo = this.tempo;
    return tempo;
  }

  @Override
  public List<IFlag> getEvents() {
    return events;
  }

  @Override
  public IFlag getEvent(int originalStart) {
    for (IFlag flag : this.events) {
      if (originalStart == flag.getOriginalStartBeat()) {
        return flag;
      }
    }
    return null;
  }

  @Override
  public IFlag addEvent(IFlag added) {
    for (int i = 0; i < this.events.size(); i++) {
      IFlag flag = events.get(i);
      if (flag.getEarliestBeat() > added.getEarliestBeat()) {
        if (added.getLatestBeat() > flag.getEarliestBeat()) {
          throw new IllegalArgumentException("Cannot add a new Event here, between "
                  + added.getEarliestBeat() + " and " + added.getLatestBeat()
                  + " as there is an event between " + flag.getEarliestBeat() + " and "
                  + flag.getLatestBeat() + ".");
        }
        else if (i > 0
                && added.getLatestBeat() > events.get(i - 1).getEarliestBeat()) {
          flag = events.get(i - 1);
          throw new IllegalArgumentException("Cannot add a new Event here, between "
                  + added.getEarliestBeat() + " and " + added.getLatestBeat()
                  + " as there is an event between " + flag.getEarliestBeat() + " and "
                  + flag.getLatestBeat() + ".");
        }
        events.add(i, added);
        System.out.println("Added");
        return added;
      }
    }

    IFlag flag;
    if (events.size() > 0 && events.get(events.size() - 1).getLatestBeat()
            > added.getEarliestBeat()) {
      flag = events.get(events.size() - 1);
      throw new IllegalArgumentException("Cannot add a new Event here, between "
              + added.getEarliestBeat() + " and " + added.getLatestBeat()
              + " as there is an event between " + flag.getEarliestBeat() + " and "
              + flag.getLatestBeat() + ".");
    }
    this.events.add(added);

    return added;
  }

  /**
   * Determines the longest Melody in this Model.
   * @return {@code Melody} that is the longest track in this piece.
   * @throws IllegalStateException if the ist of Melody's is empty.
   */
  private Melody longestTrack() {
    if (this.melodies.isEmpty()) {
      throw new IllegalStateException("There are no melodies, so there is no longest melody");
    }
    Melody longestMelody = this.melodies.get(0);
    for (Melody m : melodies) {
      if (m.numOfBeats() > longestMelody.numOfBeats()) {
        longestMelody = m;
      }
    }
    return longestMelody;
  }

  @Override
  public String getGameState() {

    Map<Integer, List<ASound>> active;

    try {
      active = this.getAllSounds();
    } catch (IllegalStateException e) {
      return "\n";
    }

    Note lowestNote = this.getLowestNote();

    Note highestNote = this.getHighestNote();

    if (lowestNote == null || highestNote == null) {
      return "\n";
    }

    for (Pitch p : Pitch.values()) {
      ASound.PITCHES.put(p.val, p);
    }

    int lowestNoteVal = lowestNote.noteValue();

    int lowestOctave = lowestNote.getOctave();

    int curOctave = lowestNote.getOctave();

    Pitch lowestPitch = lowestNote.getPitch();

    Pitch curPitch = lowestNote.getPitch();

    Pitch highestPitch = highestNote.getPitch();

    int highestOctave = highestNote.getOctave();

    int numOfPitches = 0;

    StringBuilder gameState = new StringBuilder("     ");

    while (curOctave < highestOctave
            || curOctave == highestOctave && curPitch.compare(highestPitch) <= 0) {
      int pitchSize = 5;

      String name = curPitch.toString() + curOctave;

      int offsetPosition = (pitchSize - name.length()) / 2;

      String pitch = "";
      for (int i = 0; i < offsetPosition; i++) {
        pitch = pitch + " ";
      }

      pitch = pitch + name;

      while (pitch.length() < 5) {
        pitch = pitch + " ";
      }

      gameState.append(pitch);

      curPitch = curPitch.getNextPitch();
      if (curPitch == Pitch.A) {
        curOctave++;
      }

      numOfPitches++;
    }

    int totalNumberOfBeats = 0;
    for (int i : active.keySet()) {
      if (i > totalNumberOfBeats) {
        totalNumberOfBeats = i;
      }
    }

    List<ActiveSound> activeSoundList = new ArrayList<>();
    Integer currentBeat = 0;

    while (currentBeat <= totalNumberOfBeats || !activeSoundList.isEmpty()) {

      gameState.append("\n     ");
      gameState.replace(gameState.length() - currentBeat.toString().length(),
              gameState.length() - 1,
              currentBeat.toString());
      StringBuilder lineState = new StringBuilder("");
      for (int i = 0; i < numOfPitches; i++) {
        lineState.append("     ");
      }
      for (ActiveSound note : activeSoundList) {
        int noteVal = (note.getNote().getOctave() * 12) + note.getNote().getPitch().val
                - (lowestPitch.val + (12 * lowestOctave));

        lineState.replace(noteVal * 5 + 1, noteVal * 5 + 2, "|");
      }

      for (int i = 0; i < activeSoundList.size();) {
        ActiveSound note = activeSoundList.get(i);
        note.decreaseDuration(1);
        if (!note.hasDurationLeft()) {
          activeSoundList.remove(note);
        }
        else {
          i++;
        }
      }


      if (active.get(currentBeat) != null) {
        List<Note> aSoundList = ASound.getNotesIn(active.get(currentBeat));
        for (Note note : aSoundList) {
          ActiveSound sound = new ActiveSound(note);
          sound.decreaseDuration(1);
          if (sound.hasDurationLeft()) {
            activeSoundList.add(sound);
          }

          int noteVal = note.noteValue() - lowestNoteVal;

          lineState.replace(noteVal * 5 + 1, noteVal * 5 + 2, "X");
        }
      }



      gameState.append(lineState);
      currentBeat++;
    }
    gameState.append("\n");
    return gameState.toString();
  }

  @Override
  public int hashCode() {
    return (int)(this.melodies.hashCode() * this.beat * this.volumeMult);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof  MusicModel)) {
      return false;
    }
    else {
      return ((MusicModel) other).melodies.equals(this.melodies)
              && ((MusicModel) other).beat == this.beat
              && ((MusicModel) other).volumeMult == this.volumeMult;
    }
  }

  @Override
  public String toString() {
    return "Tracks: " + this.melodies.toString()
            + ", Beat: " + this.beat
            + ", Volume: " + this.volumeMult;
  }
}
