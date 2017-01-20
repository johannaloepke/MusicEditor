package cs3500.music.controller;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.IFlag;
import cs3500.music.model.IMusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Repeat;
import cs3500.music.view.IMusicView;
import cs3500.music.view.NoteDisplayPanel;
import javafx.util.Pair;

/**
 * Implementation of {@code IMusicController} used for running Music Editors.
 * CHANGELOG: HW09: Fixed a bug in getReleasedInfo that prevented users from adding notes to the top
 *                    line in the piece.
 */
public class MusicController implements IMusicController, ActionListener {
  /**
   * Represents the view for this controller.
   */
  private IMusicView view;

  /**
   * Represents the model for this controller.
   */
  private IMusicModel model;

  /**
   * Mouse event handler for this controller.
   */
  private MouseHandler mouseHandler;

  /**
   * Represents the info of a note that was clicked on.
   */
  private Note clickedNote;

  /**
   * Represents the beat a note button was selected on.
   */
  private Integer selectedBeat;

  /**
   * Represents every note.
   */
  private Map<Integer, List<Note>> allNotes;

  /**
   * Represents the last point pressed.
   */
  private Point pressed;

  /**
   * Represents whether music should be playing or not.
   */
  private boolean offSignal = true;

  /**
   * Constructs a Music Controller.
   * @param model the model this controller manipulates
   * @param view the view this controller gets input from
   */
  public MusicController(IMusicModel model, IMusicView view) {
    this.model = model;
    this.view = view;
    KeyHandler keyHandler = new KeyHandler();
    view.addKeyListener(keyHandler);

    mouseHandler = new MouseHandler(new Runnable() {
      @Override
      public void run() {
        Point click = mouseHandler.getPoint();
        sendClickInfo(click);
      }
    }, new Runnable() {
      @Override
      public void run() {
        Point hold = mouseHandler.getPoint();
        sendReleasedInfo(hold);
      }
    }, new Runnable() {
      @Override
      public void run() {
          sendPressedInfo(mouseHandler.getPoint());
      }
    });

    view.addMouseListener(mouseHandler);

    // plays the midi music
    keyHandler.addToKeyPressedMap(KeyEvent.VK_SPACE, new Runnable() {
      @Override
      public void run() {
        offSignal = !offSignal;
        view.switchPlayback(!offSignal);
        if (!offSignal) {
          view.setScrollPolicy(true);
        }
      }
    });
    // takes display to the beginning of song
    keyHandler.addToKeyPressedMap(KeyEvent.VK_HOME, new Runnable() {
      @Override
      public void run() {
        view.setScrollPolicy(false);
        view.setCurrentDisplayPosition(0, 0);
      }
    });

    // takes display to the end of the song
    keyHandler.addToKeyPressedMap(KeyEvent.VK_END, new Runnable() {
      @Override
      public void run() {
        view.setScrollPolicy(false);
        view.setCurrentDisplayPosition(model.lengthOfSongInBeats() *
                NoteDisplayPanel.NOTE_WIDTH, 0);
      }
    });

    // moves display to the right
    keyHandler.addToKeyPressedMap(KeyEvent.VK_RIGHT, new Runnable() {
      @Override
      public void run() {
        view.setScrollPolicy(false);
        view.incrementCurrentDisplayPosition(true, true);
      }
    });

    // moves display to the left
    keyHandler.addToKeyPressedMap(KeyEvent.VK_LEFT, new Runnable() {
      @Override
      public void run() {
        view.setScrollPolicy(false);
        view.incrementCurrentDisplayPosition(true, false);
      }
    });

    // moves display up
    keyHandler.addToKeyPressedMap(KeyEvent.VK_UP, new Runnable() {
      @Override
      public void run() {
        view.incrementCurrentDisplayPosition(false, false);
      }
    });

    // moves display down
    keyHandler.addToKeyPressedMap(KeyEvent.VK_DOWN, new Runnable() {
      @Override
      public void run() {
        view.incrementCurrentDisplayPosition(false, true);
      }
    });


    keyHandler.addToKeyPressedMap(KeyEvent.VK_BACK_SPACE, new Runnable() {
      @Override
      public void run() {
        if (clickedNote != null) {
          model.removeNote(clickedNote, selectedBeat);
          clickedNote = null;
          selectedBeat = null;
          goController();
        }
      }
    });
  }

  @Override
  public void goController() {
    List<Pair<Integer, Note>> noteList = this.model.getNoteList();
    allNotes = this.model.getAllNotesMap();

    List<Integer[]> noteInfo = new ArrayList<>();


    for (Pair<Integer, Note> pair : noteList) {
      int start = pair.getKey();
      Note note = pair.getValue();
      view.handNotes(start, note.getDuration(), note.noteValue(),
              note.getInstrument());
      noteInfo.add(new Integer[]{start,
              note.getDuration(),
              note.noteValue(),
              note.getInstrument()});
    }

    play();

    view.changeDisplay(model.getLowestNote().noteValue() - 1,
            model.getHighestNote().noteValue() + 1, model.lengthOfSongInBeats(), noteInfo);
    view.display();
  }

  /**
   * Hands allthe notes to the List.
   */
  private void play() {

    List<IFlag> events = this.model.getEvents();
    view.setFlagEvents(events);
    for (IFlag flag : events) {
      flag.setNumberOfRepetitions(0);
    }

    for (Pair<Integer, Note> pair : this.model.getNoteList()) {
      int start = pair.getKey();
      Note note = pair.getValue();
      try {
        view.playNote(start, start + note.getDuration(), note.getInstrument(),
                note.noteValue(), this.model.microsecondsPerBeat());
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
      //Do nothing, as we don't currently use ActionEvents.
  }

  /**
   * Changes mouse info based on where the mouse was pressed.
   * @param pressed represents the Point pressed.
   */
  private void sendPressedInfo(Point pressed) {
    this.pressed = pressed;
    int xPosn = (int)pressed.getX();
    int yPosn = (int)pressed.getY();

    while (allNotes == null) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    int pitchVal = model.getHighestNote().noteValue() - (yPosn / NoteDisplayPanel.NOTE_HEIGHT) + 1;

    int beatVal = xPosn / NoteDisplayPanel.NOTE_WIDTH - 1;

    List<Note> beatList = allNotes.getOrDefault(beatVal, new ArrayList<>());


    for (Note n : beatList) {
      if (n.noteValue() == pitchVal) {
        this.clickedNote = n;
        this.selectedBeat = beatVal;
        break;
      }
    }
  }

  /**
   * Changes model and view based on where mouse clicks happened.
   * @param click represents the point clicked.
   */
  private void sendClickInfo(Point click) {
    int xPosn = (int)click.getX();
    int yPosn = (int)click.getY();

    while (allNotes == null) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    int pitchVal = model.getHighestNote().noteValue() - (yPosn / NoteDisplayPanel.NOTE_HEIGHT) + 1;

    int beatVal = xPosn / NoteDisplayPanel.NOTE_WIDTH - 1;

    List<Note> beatList = allNotes.getOrDefault(beatVal, new ArrayList<>());


    for (Note n : beatList) {
      if (n.noteValue() == pitchVal) {
        this.clickedNote = n;
        this.selectedBeat = beatVal;
        break;
      }
    }
  }

  /**
   * Changes model and view based on mouse release events.
   * @param released represents the Point released from.
   */
  private void sendReleasedInfo(Point released) {
    if (clickedNote != null && pressed.equals(released)) {
      return;
    }
    int highestNote = model.getHighestNote().noteValue();
    int start = Math.min((int)this.pressed.getX() /  NoteDisplayPanel.NOTE_WIDTH - 1,
            (int)released.getX() /  NoteDisplayPanel.NOTE_WIDTH - 1);
    int end = Math.max((int)this.pressed.getX() /  NoteDisplayPanel.NOTE_WIDTH,
            (int)released.getX() /  NoteDisplayPanel.NOTE_WIDTH);
    int pitch = highestNote -
            (int)pressed.getY() / NoteDisplayPanel.NOTE_HEIGHT + 1;
    pressed = null;
    int instrument;
    int volume;
    if (pitch <= highestNote && start >= 0) {
      while (true) {
        try {
          instrument = this.view.intCommands("What is the MIDI code for the instrument you want?");
          volume = this.view.intCommands("What volume would you like this note as.");
          this.model.addNote(start, end, instrument / 8 + 1, pitch, volume);
          this.goController();
          return;
        } catch (NullPointerException e) {
          this.clickedNote = null;
          this.pressed = null;
          return;
        } catch (NumberFormatException e) {
          this.view.showWarning("Please input valid number representing instrument or volume.");
          this.clickedNote = null;
          this.pressed = null;
        } catch (IllegalArgumentException e) {
          this.view.showWarning(e.getMessage());
          this.clickedNote = null;
          this.pressed = null;
        } catch (Exception e) {
          e.printStackTrace();
          this.view.showWarning(e.getMessage());
          this.clickedNote = null;
          this.pressed = null;
          return;
        }
      }
    }
    else if (pitch > highestNote && start >= 0) {
      try {
        int numOfEndings = this.view.intCommands("How many endings do you want?");
        if (numOfEndings == 0) {
          this.clickedNote = null;
          this.pressed = null;
          return;
        }
        else if (numOfEndings == 1) {
          int endBeat = this.view.intCommands("At what beat would you like the repeat?");
          this.model.addEvent(new Repeat(start, endBeat));
          this.clickedNote = null;
          this.pressed = null;
          this.goController();
        }
        else if (numOfEndings >= 1) {
          if (numOfEndings > 7) {
            this.view.showWarning("Are you sure you want " + numOfEndings + " endings? If not, "
                    + "close the next box to cancel.");
          }
          int skipPoint = this.view.intCommands("At what beat would you like the "
                  + "start of the first time bar?");
          int[] ints = new int[numOfEndings - 1];
          for (int i = 0; i < ints.length; i++) {
            ints[i] = this.view.intCommands("At what beat would you like to start time bar "
                    + (i + 2));
          }
          this.clickedNote = null;
          this.pressed = null;
          if (ints.length <= 0) {
            return;
          }
          this.model.addEvent(new Repeat(start, skipPoint, ints));
          this.goController();
        }
      } catch (IllegalArgumentException e) {
        view.showWarning(e.getMessage());
      } catch (NullPointerException e) {
        //Do nothing.
      }
    }
  }
}
