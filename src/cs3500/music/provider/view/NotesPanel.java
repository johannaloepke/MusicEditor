package cs3500.music.provider.view;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JPanel;

import cs3500.music.provider.model.ANote;
import cs3500.music.provider.model.ASound;
import cs3500.music.provider.model.BeatState;
import cs3500.music.provider.model.IViewModel;
import cs3500.music.provider.model.Sound;
import cs3500.music.provider.util.PitchNoteConverter;

/**
 * Panel that draws the Notes and the Grid. Only visible inside cs3500.music.view package.
 */
class NotesPanel extends JPanel {

  private int height;
  private int beatSize;
  private int padX;
  private int padY;
  private int curBeat;
  private int startBeat;
  private final int numBeats;
  private IViewModel<ASound> viewModel;
  private List<List<ASound>> noteStarts;
  private List<List<ASound>> noteSustains;

  /**
   * Constructor for a NotesPanel which needs a IViewModel and a scale.
   * @param viewModel  the IViewModel with all the data to draw.
   * @param beatSize   the size of a beat in the panel.
   * @param width      the width of the notes panel in pixels.
   */
  public NotesPanel(IViewModel viewModel, int beatSize, int width) {
    super();
    this.beatSize = beatSize;
    this.padX = 40;
    this.padY = 40;
    this.numBeats = (width - this.padX) / this.beatSize;
    this.noteStarts = new ArrayList<List<ASound>>();
    this.noteSustains = new ArrayList<List<ASound>>();

    try {
      this.viewModel = Objects.requireNonNull(viewModel);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("viewModel provided was null.");
    }

    Map<ASound, Integer> durations = new HashMap<ASound, Integer>();

    for (int i = 0; i < this.viewModel.getDurationInBeats(); i++) {

      List<ASound> notesToKeep = new ArrayList<>();
      List<ASound> keys = new ArrayList<>(durations.keySet());
      for (ASound curNote : keys) {
        notesToKeep.add(curNote);
        durations.put(curNote, durations.get(curNote) - 1);
        if (durations.get(curNote) == 0) {
          durations.remove(curNote);
        }
      }

      this.noteSustains.add(notesToKeep);

      List<ASound> notesToAdd = new ArrayList<ASound>();
      for (ASound curNote : this.viewModel.getNotesAt(i)) {
        if (curNote.getState() == BeatState.START) {
          notesToAdd.add(curNote);
          if (curNote.getDuration() > 1) {
            durations.put(curNote, curNote.getDuration() - 1);
          }
        }
      }

      this.noteStarts.add(notesToAdd);
    }

    this.height = (PitchNoteConverter.noteToPitch(this.viewModel.getHighest())
            - PitchNoteConverter.noteToPitch(this.viewModel.getLowest()) + 1) * beatSize
            + 2 * this.padY;

    this.setPreferredSize(new Dimension((this.numBeats + 6) * this.beatSize, height));
    this.curBeat = this.viewModel.getDurationInBeats() - this.numBeats + 10;
    this.startBeat = 0;
  }

  @Override
  public void paintComponent(Graphics g) {
    // Handle the default painting
    super.paintComponent(g);

    this.height = (PitchNoteConverter.noteToPitch(this.viewModel.getHighest())
            - PitchNoteConverter.noteToPitch(this.viewModel.getLowest()) + 1) * beatSize
            + 2 * this.padY;

    // Draw all my stuff in this order.
    this.drawNotes(g);
    this.drawGrid(g);
    this.drawSeek(g);
    this.drawYAxis(g);
    this.drawXAxis(g);
  }

  /**
   * Draws the red "seek" line on the Graphics.
   * @param g  the Graphics object on which to draw the seek line.
   */
  private void drawSeek(Graphics g) {

    g.setColor(Color.RED);
    g.drawLine(this.beatSize * (this.curBeat - this.startBeat) + this.padX,
            this.padY,
            this.beatSize * (this.curBeat - this.startBeat) + this.padX,
            (this.height - this.padY));
  }

  /**
   * Draw all the notes on the screen.
   * @param g  the Graphics on which to draw the notes.
   */
  private void drawNotes(Graphics g) {

    int numBeatsToShow = this.numBeats;
    if (this.numBeats > (this.viewModel.getDurationInBeats() - this.startBeat)) {
      numBeatsToShow = (this.viewModel.getDurationInBeats() - this.startBeat);
    }

    int highestPitch = PitchNoteConverter.noteToPitch(this.viewModel.getHighest());

    for (int i = 0; i < numBeatsToShow; i++) {

      for (ASound note : this.noteStarts.get(i + this.startBeat)) {
        int y = highestPitch - PitchNoteConverter.noteToPitch(note);

        g.setColor(Color.BLACK);
        g.fillRect(this.beatSize * i + this.padX,
                (this.beatSize * y + this.padY), this.beatSize, this.beatSize);
      }

      for (ASound note : this.noteSustains.get(i + this.startBeat)) {
        int y = highestPitch - PitchNoteConverter.noteToPitch(note);

        g.setColor(Color.GREEN);
        g.fillRect(this.beatSize * i + this.padX,
                (this.beatSize * y + this.padY), this.beatSize, this.beatSize);
      }
    }
  }

  /**
   * Draws all the Notes used in the song at the left side.
   * @param g  the Graphics on which to draw the Y axis.
   */
  private void drawYAxis(Graphics g) {

    g.setColor(Color.BLACK);
    int highestPitch = PitchNoteConverter.noteToPitch(this.viewModel.getHighest());
    int lowestPitch = PitchNoteConverter.noteToPitch(this.viewModel.getLowest());

    for (int i = highestPitch; i >= lowestPitch; i--) {
      int y = highestPitch - i;
      ANote note = PitchNoteConverter.pitchToNote(i);
      g.drawString(note.toString(),
              0,
              (this.beatSize * y + this.padY + (this.beatSize * 2) / 3));
    }
  }

  /**
   * Draws all the beats in the song at the top.
   * @param g  the Graphics on which to draw the X axis.
   */
  private void drawXAxis(Graphics g) {

    g.setColor(Color.BLACK);
    int numBeatsToShow = this.numBeats;
    if (this.numBeats > (this.viewModel.getDurationInBeats() - this.startBeat)) {
      numBeatsToShow = (this.viewModel.getDurationInBeats() - this.startBeat);
    }
    int curX = this.padX;
    int curY = this.padY - 2;

    for (int i = 0; i < numBeatsToShow; i++) {

      if ((i + this.startBeat) % 16 == 0) {
        g.drawString(Integer.toString(i + this.startBeat), curX, curY);
      }

      curX += this.beatSize;
    }

    if (this.viewModel.getDurationInBeats() <= this.startBeat + this.numBeats) {
      g.drawString(Integer.toString(this.viewModel.getDurationInBeats()), curX, curY);
    }
  }

  /**
   * Draws a grid on given Graphics.
   * @param g  the Graphics on which to draw.
   */
  private void drawGrid(Graphics g) {

    g.setColor(Color.BLACK);
    // Draw vertical lines
    int numBeatsToShow = this.numBeats;
    if (this.numBeats > (this.viewModel.getDurationInBeats() - this.startBeat)) {
      numBeatsToShow = (this.viewModel.getDurationInBeats() - this.startBeat);
    }

    for (int i = 0; i < numBeatsToShow; i++) {

      if ((i + this.startBeat) % 4 == 0 || i == 0) {
        g.drawLine((this.beatSize * i + this.padX),
                this.padY,
                (this.beatSize * i + this.padX),
                (this.height - this.padY));
      }
    }
    g.drawLine((this.beatSize * numBeatsToShow + this.padX),
            this.padY,
            (this.beatSize * numBeatsToShow + this.padX),
            (this.height - this.padY));

    // Draw Horizontal lines

    int highestPitch = PitchNoteConverter.noteToPitch(this.viewModel.getHighest());
    int lowestPitch = PitchNoteConverter.noteToPitch(this.viewModel.getLowest());

    for (int i = highestPitch + 1; i >= lowestPitch; i--) {
      int y = highestPitch - i + 1;
      g.drawLine(this.padX,
              (this.beatSize * y + this.padY),
              (this.beatSize * numBeatsToShow + this.padX),
              (this.beatSize * y + this.padY));
    }
  }

  /**
   * Sets the current beat to the given beat.
   * @param beat  the new value for current beat.
   */
  public void setCurrentBeat(int beat) {
    this.curBeat = beat;
    this.startBeat = this.curBeat - this.curBeat % this.numBeats;
  }

  /**
   * Returns the Note at Y graphical co-ordinate as an ASound.
   * @param x  the X graphical co-ordinate.
   * @param y  the Y graphical co-ordinate.
   * @return the Note at Y graphical co-ordinate or the Mute Sound if none is present.
   */
  public ASound getNoteAt(int x, int y) {

    ANote note = PitchNoteConverter.pitchToNote(this.getPitchAt(y));
    int beat = this.getBeatAt(x);

    for (ASound curNote : this.viewModel.getNotesAt(beat)) {
      if (note.equals(curNote)) {
        return curNote;
      }
    }

    return Sound.MUTE;
  }

  /**
   * Returns the beat represented at given X graphical Co-ordinate.
   * @param x  the X graphical co-ordinate.
   * @return the beat at given X co-ordinate.
   */
  public int getBeatAt(int x) {

    int beat = (x - this.padX) / this.beatSize + this.startBeat;

    if (beat < this.viewModel.getDurationInBeats()) {
      return beat;
    } else {
      return this.viewModel.getDurationInBeats() - 1;
    }
  }

  /**
   * Returns the pitch represented by the given Y graphical Coordinate.
   * @param y  the Y graphical coordinate.
   * @return the pitch at given Y coordinate.
   */
  public int getPitchAt(int y) {
    int highestPitch = PitchNoteConverter.noteToPitch(this.viewModel.getHighest());
    int pitchNumber = (y - this.padY) / this.beatSize;
    return highestPitch - pitchNumber;
  }

  /**
   * Sets the current beat to the given one.
   * @param beat the target beat to make the current
   * @throws IllegalArgumentException if the given beat is negative or too large.
   */
  public void setBeat(int beat) throws IllegalArgumentException {
    if (beat < 0 || beat >= this.viewModel.getDurationInBeats()) {
      throw new IllegalArgumentException("Invalid beat provided.");
    }
    this.curBeat = beat;
  }

  /**
   * Returns the current beat displayed by this NotesPanel.
   * @return the current beat being displayed.
   */
  public int getCurBeat() {
    return this.curBeat;
  }

  /**
   * Removes a note at given beat from the display.
   * @param note  the note to remove at given beat.
   * @param beat  the beat at which to remove the note.
   */
  public void removeNote(ASound note, int beat) {

    int start = -1;
    int duration = -1;

    // Find start and duration
    for (int i = beat; i >= 0; i--) {

      int index = -1;

      for (int j = 0; j < this.noteStarts.get(i).size(); j++) {
        ASound curNote = this.noteStarts.get(i).get(j);

        if (note.equals(curNote)) {
          start = i;
          duration = curNote.getDuration();
          index = j;
          break;
        }
      }

      if (start != -1 && duration != -1 && index != -1) {
        this.noteStarts.get(start).remove(index);
        break;
      }
    }

    if (start == -1 || duration == -1) {
      throw new IllegalStateException("Model and View are out of sync, no note " + note
              + " at beat " + beat);
    }

    // Remove note from Sustains.
    for (int i = start + 1; i < start + duration; i++) {
      // Identify index
      int index = -1;
      for (int j = 0; j < this.noteSustains.get(i).size(); j++) {
        ASound curNote = this.noteSustains.get(i).get(j);

        if (note.equals(curNote)) {
          index = j;
          break;
        }
      }

      this.noteSustains.get(i).remove(index);
    }
  }

  /**
   * Adds a note at given beat from the display.
   * @param note  the note to remove at given beat.
   * @param beat  the beat at which to remove the note.
   */
  public void addNote(ASound note, int beat) {

    this.noteStarts.get(beat).add(note);

    for (int i = beat + 1; i < beat + note.getDuration(); i++) {
      this.noteSustains.get(i).add(note);
    }
  }
}
