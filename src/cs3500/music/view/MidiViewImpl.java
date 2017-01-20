package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Instrument;
import javax.sound.midi.Track;

import cs3500.music.model.IFlag;

/**
 * A skeleton for MIDI playback.
 * Changelog (HW08) : Fixed bug in getCurrentBeat that allows for correct returning of current beat.
 */
public class MidiViewImpl implements IMusicView {
  /**
   * Represents the synthesizer object.
   */
  private final Synthesizer synth;

  /**
   * Represents the Sequencer.
   */
  private final Sequencer sequencer;

  /**
   * Determines whether the MidiDevice is playing.
   */
  private boolean isOn = false;

  /**
   * Represents the tempo for playback in microseconds per beat.
   */
  private Integer tempo = 100000;

  /**
   * Represents the events that will change the playback info.
   */
  private List<IFlag> events = new ArrayList<>();

  /**
   * Constructs a MidiViewImpl.
   */
  public MidiViewImpl() {
    try {
      this.synth = MidiSystem.getSynthesizer();
      synth.open();
      //this.receiver = synth.getReceiver();
      this.sequencer = MidiSystem.getSequencer();
      Sequence sequence = new Sequence(Sequence.PPQ, 1);
      Track track = sequence.createTrack();
      sequencer.setSequence(sequence);
      this.sequencer.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
      //Added so synth and receiver couldn't be finalized to null.
      throw new IllegalStateException("Unable to create MidiSystem");
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
      throw new IllegalStateException("Created invalid sequencer");
    }
  }

  /**
   * Creates a MidiView with a given receiver and sequencer.
   * @param sequencer represents the sequencer being added.
   * @param receiver  represents the receiver being added.
   */
  public MidiViewImpl(Sequencer sequencer, Receiver receiver) {
    if (sequencer == null) {
      throw new IllegalArgumentException();
    }
    try {
      this.sequencer = sequencer;
      this.synth = MidiSystem.getSynthesizer();
      synth.open();
      Sequence sequence = new Sequence(Sequence.PPQ, 1);
      Track track = sequence.createTrack();
      sequencer.setSequence(sequence);
      this.sequencer.getTransmitter().setReceiver(receiver);
      this.sequencer.open();
    } catch (MidiUnavailableException | InvalidMidiDataException e) {
      e.printStackTrace();
      throw new IllegalArgumentException();
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
  @Override
  public void playNote(long startTime, long endTime, int instrument, int pitch, long tempo)
          throws InvalidMidiDataException {
    this.tempo = (int) tempo;
    sequencer.setTempoInMPQ(this.tempo);


    int channel = 0;
    this.synth.loadInstrument(this.synth.getDefaultSoundbank().getInstruments()[instrument]);
    Instrument[] instruments = this.synth.getDefaultSoundbank().getInstruments();
    this.synth.getChannels()[0].programChange(0, instruments[instrument].getPatch().getProgram());

    if (instrument < 17 && instrument >= 9) {
      channel = 9;
    }
    if (instrument >= 17 && instrument < 26) {
      channel = 17;
    }

    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, channel, pitch, 64);
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, channel, pitch, 64);

    Track curTrack = this.sequencer.getSequence().getTracks()[0];
    curTrack.add(new MidiEvent(start, startTime));
    curTrack.add(new MidiEvent(stop, endTime));
  }

  @Override
  public void close() {
    this.isOn = false;
    this.synth.close();
    this.sequencer.close();
  }

  @Override
  public void switchPlayback(boolean on) {
    this.isOn = on;
    if (this.isOn) {
      if (sequencer.getMicrosecondPosition() >= sequencer.getMicrosecondLength()) {
        sequencer.setMicrosecondPosition(1);
        for (IFlag event : this.events) {
          event.setNumberOfRepetitions(0);
        }
      }
      sequencer.start();
      sequencer.setTempoInMPQ(tempo);
    }
    else {
      this.sequencer.stop();
    }
  }

  @Override
  public void changeDisplay(int lowestNote, int highestNote, int lengthOfSong,
                            List<Integer[]> notes) {
    // Not necessary for MIDI.
  }

  @Override
  public void handNotes(int start, int duration, int pitch, int melodyOrInstrument) {
    // Not necessary for MIDI.
  }

  @Override
  public void showWarning(String warning) {
    //No warning displays by sound.
  }

  @Override
  public void addKeyListener(KeyListener l) {
    // Not necessary for MIDI.
  }

  @Override
  public void addMouseListener(MouseListener l) {
    // Not necessary for MIDI.
  }

  @Override
  public String getCurrentState() {
    return "beat " + Long.toString(this.sequencer.getMicrosecondPosition() / tempo);
  }

  @Override
  public Integer intCommands(String question) {
    return 0;
  }

  @Override
  public void setCommandListener(ActionListener listener) {
    // currently no commands on this view.
  }

  @Override
  public void changeCurrentState(String command) {
    // state is currently not changing
  }

  @Override
  public int getCurrentBeat() {
    int beat = (int)(sequencer.getMicrosecondPosition()
            * sequencer.getTickLength()
            / Math.max(sequencer.getMicrosecondLength(), 1));
    if (sequencer.isRunning()) {
      for (IFlag flag : events) {
        int newBeat = flag.changeBeat(beat);
        if (newBeat != beat) {
          this.setCurrentBeat(newBeat);
          sequencer.setTempoInMPQ(tempo);
          return newBeat;
        }
      }
    }
    if (this.sequencer.getMicrosecondPosition() >= this.sequencer.getMicrosecondLength()) {
      this.sequencer.setMicrosecondPosition(this.sequencer.getMicrosecondLength());
      this.sequencer.stop();
    }
    return beat;
  }

  @Override
  public void setCurrentBeat(int beat) {
    sequencer.setMicrosecondPosition(
            beat
            * Math.max(1, sequencer.getMicrosecondLength())
            / sequencer.getTickLength());
  }

  @Override
  public void setCurrentDisplayPosition(Integer x, Integer y) {
    // Not necessary for MIDI.
  }

  @Override
  public void incrementCurrentDisplayPosition(boolean horizontal, boolean positive) {
    // Not necessary for MIDI.
  }

  @Override
  public void setScrollPolicy(boolean autoScroll) {
    //No autoscroll in MIDI.
  }

  /**
   * Represents whether this is on.
   * @return boolean representing whether this is currently playing.
   */
  protected boolean isOn() {
    return this.isOn;
  }

  @Override
  public void display() {
    // audio cannot be displayed
  }

  @Override
  public void setFlagEvents(List<IFlag> events) {
    this.events = events;
  }
}
