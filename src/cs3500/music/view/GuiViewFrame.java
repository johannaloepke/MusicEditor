package cs3500.music.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
//import java.awt.event.MouseListener  Possibly of interest for handling mouse events
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Objects;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;

import cs3500.music.model.IFlag;

/**
 * A skeleton Frame (i.e., a window) in Swing.
 */
public class GuiViewFrame extends javax.swing.JFrame implements IMusicView, IGuiView {

  private final NoteDisplayPanel displayPanel;

  private final JScrollPane scrollPane;

  private JButton exitButton;

  /**
   * Represents the information relevant to the model.
   */
  private StringBuilder info = new StringBuilder();

  /**
   * Creates new GuiView.
   */
  public GuiViewFrame() {
    this.displayPanel = new NoteDisplayPanel();
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    //displayPanel.setPreferredSize(new Dimension(1000, 2000));

    JScrollPane scrollPane = new JScrollPane(displayPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.getContentPane().add(scrollPane);

    this.scrollPane = scrollPane;

    this.exitButton = new JButton("Exit", null);

    this.pack();
  }


  public void initialize() {
    this.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(4000, 1000);
  }

  /**
   * Returns a String representing a command, given options.
   * @param  options represents the String of possible options.
   * @return {@code String} representing the chosen response.
   */
  public String commandName(String[] options) {
    Objects.requireNonNull(options);
    if (options.length == 0) {
      throw new IllegalArgumentException("No options given; requires non-empty array parameter.");
    }
    return (String)JOptionPane.showInputDialog(
            this,
            "What would you like to do here?",
            "Editor",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
  }

  @Override
  public Integer intCommands(String question) {
    String response = JOptionPane.showInputDialog(this, null, question,
            JOptionPane.QUESTION_MESSAGE);
    if (response.equals("")) {
      return null;
    }
    return Integer.parseInt(response);
  }

  @Override
  public void playNote(long startTime, long endTime, int instrument, int pitch, long tempo)
          throws InvalidMidiDataException {
    // currently the gui does not play any notes
  }

  @Override
  public String getCurrentState() {
    return this.info.toString();
  }

  @Override
  public void setCommandListener(ActionListener listener) {
    this.exitButton.addActionListener(listener);
    // this.listener = listener;
  }

  @Override
  public void changeCurrentState(String command) {
    // currently the gui's state is not being changed
  }

  @Override
  public int getCurrentBeat() {
    return this.displayPanel.getBeat();
  }

  @Override
  public void setCurrentBeat(int beat) {
    this.displayPanel.setBeat(beat);
    this.displayPanel.repaint();
  }

  @Override
  public void setCurrentDisplayPosition(Integer xPosition, Integer yPosition) {
    JScrollBar horizontal = this.scrollPane.getHorizontalScrollBar();
    JScrollBar vertical = this.scrollPane.getVerticalScrollBar();
    if (xPosition != null) {
      xPosition = Math.min(xPosition, horizontal.getMaximum());
      xPosition = Math.max(xPosition, horizontal.getMinimum());
      horizontal.setValue(xPosition);
    }

    if (yPosition != null) {
      yPosition = Math.min(yPosition, vertical.getMaximum());
      yPosition = Math.max(yPosition, vertical.getMinimum());
      vertical.setValue(yPosition);
    }
  }

  @Override
  public void incrementCurrentDisplayPosition(boolean horizontal, boolean positive) {
    JScrollBar bar;
    int size;
    if (horizontal) {
      bar = this.scrollPane.getHorizontalScrollBar();
      size = this.scrollPane.getWidth();
    }
    else {
      bar = this.scrollPane.getVerticalScrollBar();
      size = this.scrollPane.getHeight();
    }

    if (!positive) {
      size = -size;
    }

    int val = bar.getValue() + size;

    bar.setValue(val);
  }

  @Override
  public void setScrollPolicy(boolean autoScroll) {
    //Not valid for GUI views.
  }

  @Override
  public void display() {
    this.setVisible(true);
    this.setFocusable(true);
    this.displayPanel.revalidate();
    this.displayPanel.repaint();
  }

  @Override
  public void setFlagEvents(List<IFlag> events) {
    displayPanel.setFlagEvents(events);
  }

  @Override
  public void changeDisplay(int lowest, int highest, int songLength, List<Integer[]> beats) {
    this.displayPanel.setNotes(beats);
    this.displayPanel.setLowestNote(lowest);
    this.displayPanel.setHighestNote(highest);
    this.displayPanel.setCorrectDimensions(songLength, highest - lowest);
  }

  @Override
  public void handNotes(int start, int duration, int pitch,
                        int melodyOrInstrument) {
    this.displayPanel.drawNote(start, pitch, duration, melodyOrInstrument);
  }

  @Override
  public void showWarning(String warning) {
    JOptionPane.showMessageDialog(this, warning, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void close() {
    // currently the gui is not being closed
    this.setVisible(false);
  }

  @Override
  public void switchPlayback(boolean on) {
    //No playback to switch.
  }

  @Override
  public void addKeyListener(KeyListener l) {
    super.addKeyListener(l);
  }

  @Override
  public boolean beatIsVisible(int beat) {
    beat = (beat + 1) * NoteDisplayPanel.NOTE_WIDTH;
    boolean visible = (beat >= scrollPane.getHorizontalScrollBar().getValue())
            && (beat < scrollPane.getHorizontalScrollBar().getValue() + scrollPane.getWidth());
    return visible;
  }

  @Override
  public void addMouseListener(MouseListener l) {
    displayPanel.addMouseListener(l);
  }
}
