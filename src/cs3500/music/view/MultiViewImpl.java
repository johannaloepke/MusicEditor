package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IFlag;

/**
 * A combination view of GUI and MIDI simultaneously.
 */
public class MultiViewImpl implements IMusicView {
  private IGuiView gui = new GuiViewFrame();
  private IMusicView midi;

  private Timer timer = null;

  private boolean autoScroll = true;

  /**
   * Constructs a MultiViewImpl.
   */
  public MultiViewImpl() {
    midi = new MidiViewImpl();
  }


  @Override
  public void playNote(long startTime, long endTime, int instrument, int pitch, long tempo)
          throws InvalidMidiDataException {


    if (timer == null) {
      timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          int oldBeat = gui.getCurrentBeat();
          gui.setCurrentBeat(midi.getCurrentBeat());
          int curBeat = gui.getCurrentBeat();
          if (autoScroll) {
            while (!gui.beatIsVisible(curBeat)) {
              gui.incrementCurrentDisplayPosition(true, oldBeat < curBeat);
            }
          }
        }
      };
      timer.scheduleAtFixedRate(task, 0,
              10);
    }
    if (midi == null) {
      midi = new MidiViewImpl();
    }

    //gui.playNote(startTime, endTime, instrument, pitch, tempo);
    midi.playNote(startTime, endTime, instrument, pitch, tempo);
    //gui.handNotes((int)startTime, (int)(endTime - startTime), pitch, instrument);
  }

  @Override
  public String getCurrentState() {
    return midi.getCurrentState();
  }

  @Override
  public Integer intCommands(String question) {
    return this.gui.intCommands(question);
  }

  @Override
  public void setCommandListener(ActionListener listener) {
    this.gui.setCommandListener(listener);

  }

  @Override
  public void changeCurrentState(String command) {
    //Not currently implemented.
  }

  @Override
  public int getCurrentBeat() {
    return this.gui.getCurrentBeat();
  }

  @Override
  public void setCurrentBeat(int beat) {
    this.gui.setCurrentBeat(beat);
    this.midi.setCurrentBeat(beat);
  }

  @Override
  public void setCurrentDisplayPosition(Integer x, Integer y) {
    this.gui.setCurrentDisplayPosition(x, y);
  }

  @Override
  public void incrementCurrentDisplayPosition(boolean horizontal, boolean positive) {
    gui.incrementCurrentDisplayPosition(horizontal, positive);
  }

  @Override
  public void setScrollPolicy(boolean autoScroll) {
    this.autoScroll = autoScroll;
  }

  @Override
  public void display() {
    this.gui.display();
  }

  @Override
  public void setFlagEvents(List<IFlag> events) {
    this.gui.setFlagEvents(events);
    this.midi.setFlagEvents(events);
  }

  @Override
  public void close() {
    if (this.midi == null) {
      return;
    }
    this.midi.close();
    this.midi = null;
  }

  @Override
  public void switchPlayback(boolean on) {
    this.midi.switchPlayback(on);
  }

  @Override
  public void changeDisplay(int lowestNote, int highestNote, int lengthOfSong,
                            List<Integer[]> noteList) {
    this.gui.changeDisplay(lowestNote, highestNote, lengthOfSong, noteList);
  }

  @Override
  public void handNotes(int start, int duration, int pitch, int melodyOrInstrument) {
    this.gui.handNotes(start, duration, pitch, melodyOrInstrument);
  }

  @Override
  public void showWarning(String warning) {
    this.gui.showWarning(warning);
  }

  @Override
  public void addKeyListener(KeyListener l) {
    gui.addKeyListener(l);
  }

  @Override
  public void addMouseListener(MouseListener l) {
    gui.addMouseListener(l);
  }
}
