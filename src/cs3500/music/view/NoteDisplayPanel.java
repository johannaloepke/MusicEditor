package cs3500.music.view;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import cs3500.music.model.IFlag;
import cs3500.music.model.Pitch;

/**
 * This panel represents the region where the notes must be drawn.
 * CHANGELOG: Display now shows notes from highest to lowest, not lowest to highest.
 *            Display now takes a List of an array of ints, rather than taking single ints.
 */
public class NoteDisplayPanel extends JPanel {
  /**
   * Represents the number of beats per measure.
   */
  private int measureLength;

  /**
   * Represents the lowest note in the piece.
   */
  private int minNote;

  /**
   * Represents the highest note in the piece.
   */
  private int maxNote;

  /**
   * Represents the highest visible beat.
   */
  private int maxVisibleBeat;

  /**
   * Represents the number of beats in the piece.
   */
  private int numBeats;

  /**
   * Represents the height of a note in the piece.
   */
  public static final int NOTE_HEIGHT = 10;

  /**
   * Represents the width of a note in the piece.
   */
  public static final int NOTE_WIDTH = 15;

  /**
   * Represents the current beat.
   */
  private int curBeat = 0;

  /**
   * Represents the list of notes in the piece.
   */
  private List<Integer[]> noteList = new ArrayList<>();

  /**
   * Represents the list of events in the piece.
   */
  private List<IFlag> events = new ArrayList<>();

  /**
   * Represents the Map from an Integer (specified by method) to an Int representing a COlor.
   */
  private Map<Integer, Integer> colorInt = new HashMap<>();

  /**
   * Creates a NoteDisplayPanel with the given information.
   * @param measureLength represents the length of the measure.
   * @param minNote       represents the minimum note frequency.
   * @param maxNote       represents the maximum note frequency.
   * @param numBeats      represents the number of beats in the piece.
   */
  public NoteDisplayPanel(int measureLength,
                          int minNote, int maxNote, int numBeats) {
    this.measureLength = measureLength;
    this.minNote = minNote;
    this.maxNote = maxNote;
    this.numBeats = numBeats;
  }

  /**
   * Overload constructor for NoteDisplayPanel.
   */
  public NoteDisplayPanel() {
    this(4, 0, 10, 50);
  }

  /**
   * Draws the original grid.
   * @param g the Graphics parameter.
   */
  private void drawGrid(Graphics g) {
    int gridHeight = Math.max(maxNote - minNote, 13);
    int gridWidth = Math.max(numBeats + 1, 20);
    for (int i = 1; i <= gridHeight; i++) {
      int val = maxNote - i;
      if (i < gridHeight) {
        g.setFont(new Font("Arial", Font.PLAIN, NOTE_HEIGHT / 2));
        g.drawString(Pitch.getPitchAtVal(val % 12).toString() + ((val - Pitch.C.val) / 12),
                0, i * NOTE_HEIGHT + NOTE_HEIGHT / 2 + g.getFont().getSize() / 2);
      }
      g.drawLine(NOTE_WIDTH, i * NOTE_HEIGHT, (gridWidth + 1) * NOTE_WIDTH, i * NOTE_HEIGHT);

    }

    for (int i = 1; i <= gridWidth + 1; i++) {

      if (i % measureLength == 1) {
        g.drawString(((Integer) (i - 1)).toString(), i * NOTE_WIDTH, g.getFont().getSize());
        g.drawLine(i * NOTE_WIDTH, NOTE_HEIGHT, i * NOTE_WIDTH, gridHeight * NOTE_HEIGHT);
      }
    }

    g.setColor(Color.RED);
    g.drawLine((this.curBeat + 1) * NOTE_WIDTH, 0,
            (this.curBeat + 1) * NOTE_WIDTH, gridHeight * NOTE_HEIGHT);
    g.setColor(Color.BLACK);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.drawGrid(g);

    for (Integer[] integers : this.noteList) {
      this.drawNote(g, integers[0], integers[2], integers[1], integers[3]);
    }

    for (IFlag flag : this.events) {
      this.drawFlag(g, flag);
    }
  }

  protected void drawFlag(Graphics g, IFlag flag) {
    class Triangle extends Polygon {
       Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        super(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
      }
    }

    int gridHeight = Math.max(maxNote - minNote, 13);
    int triRad = NOTE_WIDTH / 4;
    int triHalf = NOTE_HEIGHT / 4;
    int xPos = (flag.getOriginalStartBeat() + 1) * NOTE_WIDTH;

    g.setColor(Color.BLUE);
    for (int i = 1; i <= gridHeight; i++) {
      int yPos = i * NOTE_HEIGHT;
      g.drawPolygon(new Triangle(xPos - triRad,
              yPos - triHalf,
              xPos - triRad,
              yPos + triHalf,
              xPos + triRad,
              yPos + triHalf / 2));
    }

    g.setColor(Color.RED);

    int j = 0;
    while (flag.getEndBeatAt(j) != null) {
      if (j % 2 == 0 && flag.getSkipPoint() == null
              || (int)flag.getEndBeatAt(j) != (int)(flag.getSkipPoint())) {
        xPos = (flag.getEndBeatAt(j) + 1) * NOTE_WIDTH;
        for (int i = 1; i <= gridHeight; i++) {
          int yPos = i * NOTE_HEIGHT;
          g.drawPolygon(new Triangle(xPos - triRad,
                  yPos - triHalf / 2,
                  xPos + triRad,
                  yPos - triHalf,
                  xPos + triRad,
                  yPos + triHalf));
        }
      }
      j++;
    }

    g.setColor(Color.BLUE);

    if (flag.getSkipPoint() != null) {
      xPos = (flag.getSkipPoint() + 1) * NOTE_WIDTH - NOTE_HEIGHT / 2;
      for (int i = 1; i <= gridHeight; i++) {
        int yPos = i * NOTE_HEIGHT - NOTE_HEIGHT / 2;
        g.drawArc(xPos, yPos, NOTE_HEIGHT, NOTE_HEIGHT, 0, 360);
      }
    }

    g.setColor(Color.BLACK);
  }

  /**
   * Sets notes to the given information.
   * @param noteInfo note information.
   */
  protected void setNotes(List<Integer[]> noteInfo) {
    noteList = noteInfo;
  }

  /**
   * Draws a note.
   * @param beatIndex beat to draw note at.
   * @param pitchIndex which pitch column to draw in.
   * @param noteDuration how long the note is.
   * @param colorPicker what color the note should be.
   */
  protected void drawNote(int beatIndex, int pitchIndex, int noteDuration, int colorPicker) {
    Graphics g = this.getGraphics();
    beatIndex = beatIndex + 1;
    Color thisColor;
    if (colorInt.containsKey(colorPicker)) {
      thisColor = this.colorWheel(colorInt.get(colorPicker));
    }
    else {
      int newColor = 1;
      newColor += colorInt.keySet().size();
      colorInt.put(colorPicker, newColor);
      thisColor = this.colorWheel(newColor);
    }

    g.setColor(thisColor);
    g.fillRect(beatIndex * NOTE_WIDTH, (this.maxNote - pitchIndex) * NOTE_HEIGHT,
            noteDuration * NOTE_WIDTH, NOTE_HEIGHT);
    g.setColor(Color.black);
    g.fillRect(beatIndex * NOTE_WIDTH, (this.maxNote - pitchIndex) * NOTE_HEIGHT, NOTE_WIDTH,
            NOTE_HEIGHT);
  }

  /**
   * Draws a note on a given panel.
   * @param g            Graphics object.
   * @param beatIndex    the beat the note starts at.
   * @param pitchIndex   value representing the starting pitch.
   * @param noteDuration length of the note.
   * @param colorPicker  the integer of the stat that changes the color.
   */
  public void drawNote(Graphics g, int beatIndex, int pitchIndex, int noteDuration,
                          int colorPicker) {
    beatIndex = beatIndex + 1;
    Color thisColor;
    if (colorInt.containsKey(colorPicker)) {
      thisColor = this.colorWheel(colorInt.get(colorPicker));
    }
    else {
      int newColor = 1;
      newColor += colorInt.keySet().size();
      colorInt.put(colorPicker, newColor);
      thisColor = this.colorWheel(newColor);
    }

    g.setColor(thisColor);
    g.fillRect(beatIndex * NOTE_WIDTH, (this.maxNote - pitchIndex) * NOTE_HEIGHT,
            noteDuration * NOTE_WIDTH, this.NOTE_HEIGHT);
    g.setColor(Color.black);
    g.fillRect(beatIndex * NOTE_WIDTH, (this.maxNote - pitchIndex) * NOTE_HEIGHT, NOTE_WIDTH,
            NOTE_HEIGHT);
  }

  public void setFlagEvents(List<IFlag> events) {
    this.events = events;
  }

  /**
   * Sets the lowest note.
   * @param lowest represents the int value of the lowest note.
   */
  public void setLowestNote(int lowest) {
    this.minNote = lowest;
  }

  /**
   * Sests the highest note.
   * @param highest represents the int value of the highest note.
   */
  public void setHighestNote(int highest) {
    this.maxNote = highest;
  }

  /**
   * Sets the appropriate dimensions of this panel based on length of song.
   * @param lengthOfSong length of song in beats.
   * @param numOfBoxes number of boxes // TODO: ??
   */
  public void setCorrectDimensions(int lengthOfSong, int numOfBoxes) {
    this.numBeats = lengthOfSong + 10;
    this.setPreferredSize(new Dimension(numBeats * NOTE_WIDTH, numOfBoxes * NOTE_HEIGHT));
  }

  /**
   * Sets the beat to the given one.
   * @param beat beat to be set to.
   */
  public void setBeat(int beat) {
    this.curBeat = Math.max(beat, 0);
  }


  /**
   * Returns the current beat according to this.
   * @return int representing the current beat.
   */
  public int getBeat() {
    return this.curBeat;
  }

  /**
   * Returns the color based on an int.
   * @param colorNum represents the int which will be converted to a color.
   * @return Color.
   */
  private Color colorWheel(int colorNum) {
    switch (colorNum) {
      case 1 :
        return Color.GREEN;
      case 2 :
        return Color.BLUE;
      case 3 :
        return Color.RED;
      case 4 :
        return Color.YELLOW;
      case 5 :
        return Color.ORANGE;
      case 6 :
        return Color.CYAN;
      case 7 :
        return Color.MAGENTA;
      case 8 :
        return Color.PINK;
      case 9 :
        return Color.LIGHT_GRAY;
      default :
        return colorWheel(colorNum % 9 + 1);
    }
  }
}
