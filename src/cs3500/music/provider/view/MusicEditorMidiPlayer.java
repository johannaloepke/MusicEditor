package cs3500.music.provider.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import cs3500.music.provider.model.ANote;
import cs3500.music.provider.model.ASound;
import cs3500.music.provider.model.BeatState;
import cs3500.music.provider.model.IViewModel;
import cs3500.music.provider.model.Note;
import cs3500.music.provider.util.PitchNoteConverter;

/**
 * Class that implements the MIDI view for Music Editor.
 */
public class MusicEditorMidiPlayer implements ISyncView<ASound> {
  private final Sequencer sequencer;
  private final int tempo;
  IViewModel<ASound> viewModel;
  HashMap<Integer, List<MidiEvent>> startEvents;
  HashMap<Integer, List<MidiEvent>> stopEvents;

  /**
   * Constructs a MusicEditorMidiPlayer with the given IViewModel.
   * @param viewModel  the IViewModel with all the data.
   */
  public MusicEditorMidiPlayer(IViewModel<ASound> viewModel) {

    try {
      this.viewModel = Objects.requireNonNull(viewModel);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Model must not be null.");
    }

    this.startEvents = new HashMap<Integer, List<MidiEvent>>();
    this.stopEvents = new HashMap<Integer, List<MidiEvent>>();
    this.tempo = this.viewModel.getTempo();

    Sequencer localSeq = null;
    try {
      localSeq = MidiSystem.getSequencer();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }

    this.sequencer = localSeq;

    try {
      this.sequencer.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Constructs a MidiViewModel with given Synthesizer and Receiver that reads data from given
   * ViewModel.
   * @param viewModel  the IViewModel with all the data.
   * @param sequencer  the Sequencer to use.
   */

  public MusicEditorMidiPlayer(IViewModel<ASound> viewModel, Sequencer sequencer) {
    this.viewModel = viewModel;
    this.tempo = viewModel.getTempo();
    this.sequencer = sequencer;

    try {
      this.sequencer.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  /**
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   *  <li>{@link MidiSystem#getSynthesizer()}</li>
   *  <li>{@link Synthesizer}
   *    <ul>
   *      <li>{@link Synthesizer#open()}</li>
   *      <li>{@link Synthesizer#getReceiver()}</li>
   *      <li>{@link Synthesizer#getChannels()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link Receiver}
   *    <ul>
   *      <li>{@link Receiver#send(MidiMessage, long)}</li>
   *      <li>{@link Receiver#close()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link MidiMessage}</li>
   *  <li>{@link ShortMessage}</li>
   *  <li>{@link MidiChannel}
   *    <ul>
   *      <li>{@link MidiChannel#getProgram()}</li>
   *      <li>{@link MidiChannel#programChange(int)}</li>
   *    </ul>
   *  </li>
   * </ul>
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI">
   *   https://en.wikipedia.org/wiki/General_MIDI
   *   </a>
   */

  /**
   * Plays a note and tells when to stop.
   * @param track       the Track to which to add the notes.
   * @param note        the ANote to play.
   * @param startBeat   the bat at which to start playing the music.
   * @param duration    the duration in beats for which to play the note.
   * @param instrument  the instrument to play the note on.
   * @param volume      the volume of the note.
   * @throws InvalidMidiDataException if MIDI throws an exception.
   */
  private void playNote(Track track, ANote note, int startBeat, int duration, int instrument,
                        int volume)
          throws InvalidMidiDataException {

    int pitch = PitchNoteConverter.noteToPitch(note);
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, instrument, pitch, volume);
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, instrument, pitch, volume);

    MidiEvent startEvent = new MidiEvent(start, startBeat);
    MidiEvent stopEvent = new MidiEvent(stop, startBeat + duration);

    if (this.startEvents.containsKey(startBeat)) {
      this.startEvents.get(startBeat).add(startEvent);
    } else {
      this.startEvents.put(startBeat, new ArrayList<MidiEvent>());
      this.startEvents.get(startBeat).add(startEvent);
    }

    if (this.stopEvents.containsKey(startBeat + duration)) {
      this.stopEvents.get(startBeat + duration).add(stopEvent);
    } else {
      this.stopEvents.put(startBeat + duration, new ArrayList<MidiEvent>());
      this.stopEvents.get(startBeat + duration).add(stopEvent);
    }

    track.add(startEvent);
    track.add(stopEvent);
  }

  @Override
  public void activate() {
    Sequence localSeq = null;
    try {
      localSeq = new Sequence(Sequence.PPQ, 1);
    }
    catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }

    final Sequence SEQUENCE = localSeq;
    Track track = SEQUENCE.createTrack();

    this.playAllNotes(track);

    try {
      this.sequencer.setSequence(SEQUENCE);
      this.sequencer.setTempoInMPQ(this.tempo);
      this.sequencer.start();

    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds notes to play at to given Track.
   * @param track Track to which all the notes are added.
   */
  private void playAllNotes(Track track) {

    for (int i = 0; i < this.viewModel.getDurationInBeats(); i++) {

      for (ASound note : this.viewModel.getNotesAt(i)) {

        if (note.getState() == BeatState.START) {

          try {
            this.playNote(track, new Note(note.getTone(), note.getOctave()), i, note.getDuration(),
                    note.getInstrument() - 1, note.getVolume());

          } catch (InvalidMidiDataException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @Override
  public void deactivate() {
    // No need for deactivating views yet so there is nothing here.
    return;
  }

  @Override
  public void playPause() {
    // Do some stuff.
    if (this.sequencer.isRunning()) {
      this.sequencer.stop();
    }
    else {
      this.sequencer.start();
      this.sequencer.setTempoInMPQ(this.tempo);
    }
  }

  @Override
  public boolean isInteractive() {
    return false;
  }

  @Override
  public int getTime() {
    return (int)this.sequencer.getTickPosition();
  }

  @Override
  public void setBeatAt(int x) throws IllegalArgumentException {
    this.sequencer.setTickPosition(x);
    this.sequencer.setTempoInMPQ(this.tempo);
  }

  @Override
  public void removeNote(ASound note, int beat) {

    // Find start of the note.
    MidiEvent startEvent = null;
    MidiEvent stopEvent = null;

    int startBeat = beat;

    for (int i = startBeat; i >= 0; i--) {

      if (!this.startEvents.containsKey(i)) {
        continue;
      }

      for (MidiEvent e : this.startEvents.get(i)) {
        ShortMessage msg = (ShortMessage) e.getMessage();

        if (msg.getChannel() == note.getInstrument() - 1
                && msg.getData1() == PitchNoteConverter.noteToPitch(note)
                && msg.getData2() == note.getVolume()) {
          startEvent = e;
          this.startEvents.get(i).remove(e);
          break;
        }
      }

      if (startEvent != null) {
        startBeat = i;
        break;
      }
    }

    if (startEvent == null) {
      throw new IllegalStateException("Note " + note.toString() + " not found at beat " + beat);
    }

    for (MidiEvent e : this.stopEvents.get(startBeat + note.getDuration())) {
      ShortMessage msg = (ShortMessage) e.getMessage();

      if (msg.getChannel() == note.getInstrument() - 1
              && msg.getData1() == PitchNoteConverter.noteToPitch(note)
              && msg.getData2() == note.getVolume()) {
        stopEvent = e;
        this.stopEvents.get(startBeat + note.getDuration()).remove(e);
        break;
      }
    }

    boolean startRemoved =
            this.sequencer.getSequence().getTracks()[0].remove(startEvent);
    boolean stopRemoved =
            this.sequencer.getSequence().getTracks()[0].remove(stopEvent);

    if (!startRemoved) {
      System.out.println("Couldn't remove " + startEvent.toString() + " from start.");
    }
    if (!stopRemoved) {
      System.out.println("Couldn't remove " + stopEvent.toString() + " from stop.");
    }
  }

  @Override
  public void addNote(ASound note, int beat) {

    try {
      this.playNote(this.sequencer.getSequence().getTracks()[0], note, beat, note.getDuration(),
              note.getInstrument() - 1, note.getVolume());
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }
}