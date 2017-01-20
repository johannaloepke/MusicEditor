package cs3500.music.provider.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cs3500.music.provider.model.ANote;
import cs3500.music.provider.model.ASound;
import cs3500.music.provider.model.BeatState;
import cs3500.music.provider.model.IViewModel;
import cs3500.music.provider.model.Note;
import cs3500.music.provider.model.Sound;
import cs3500.music.provider.model.Tone;

/**
 * Class to implement a Console View for the Music Editor.
 */
public class ConsoleView implements IView<ASound> {

  private Appendable output;
  private IViewModel<ASound> viewModel;
  private Map<ASound, Integer> notes;

  /**
   * Creates a ConsoleView with the given View-Model which prints output to given Appendable.
   * @param viewModel the viewModel for the View.
   * @param output    the appendable on which to produce console output.
   * @throws IllegalArgumentException if View-Model provided is null.
   */
  public ConsoleView(IViewModel<ASound> viewModel, Appendable output) {

    try {
      this.viewModel = Objects.requireNonNull(viewModel);
      this.output = output;
      this.notes = new HashMap<ASound, Integer>();

    } catch (NullPointerException e) {
      throw new IllegalArgumentException("View and output appendable cannot be null.");
    }
  }

  @Override
  public void activate() {
    try {
      output.append(this.getModelAsString());

    } catch (IOException e) {
      throw new RuntimeException("IO Failed.");
    }
  }

  @Override
  public void deactivate() {
    return;
  }

  @Override
  public void playPause() {
    // Does nothing since there is no concept of play and pause in a Console View.
    return;
  }

  @Override
  public boolean isInteractive() {
    return false;
  }

  /**
   * Returns the String representation of the View-Model.
   * @return a String representing the state of the View-Model.
   */
  private String getModelAsString() {
    StringBuilder stringRep = new StringBuilder("");

    stringRep.append(this.getHeader());

    for (int i = 0; i < this.viewModel.getDurationInBeats(); i++) {
      stringRep.append(String.format("% 3d", i));
      stringRep.append(this.getBeatAsString(i));
      stringRep.append("\n");
    }

    return new String(stringRep);
  }

  /**
   * Returns the head of the table with all notes as a String.
   * @return the head of the table of notes.
   */
  private String getHeader() {

    StringBuilder header = new StringBuilder("   ");

    ASound lowest = this.viewModel.getLowest();
    ASound highest = this.viewModel.getHighest();
    ArrayList<Tone> allTones = Tone.getAllTones();

    if (lowest.equals(Sound.MUTE)) {
      return "";
    }

    for (int o = lowest.getOctave(); o <= highest.getOctave(); o++) {

      for (int n = 0; n < allTones.size(); n++) {

        ANote curNote = new Note(allTones.get(n), o);

        if (curNote.compareTo(lowest) < 0) {
          continue;

        } else if (curNote.compareTo(highest) > 0) {
          break;

        } else {

          header.append(this.pad(curNote.toString()));
        }
      }
    }

    header.append("\n");
    return new String(header);
  }

  /**
   * Returns the String representation of all the notes played at a beat.
   * @param beat the beat to convert to String.
   * @return String representation of the notes at that beat.
   */
  private String getBeatAsString(int beat) {

    StringBuilder stringRep = new StringBuilder("");
    ASound lowest = this.viewModel.getLowest();
    ASound highest = this.viewModel.getHighest();
    List<ASound> notes = this.viewModel.getNotesAt(beat);
    ArrayList<Tone> allTones = Tone.getAllTones();

    if (lowest.equals(Sound.MUTE) || highest.equals(Sound.MUTE)) {
      return "";
    }

    for (int o = lowest.getOctave(); o <= highest.getOctave(); o++) {

      for (int n = 0; n < allTones.size(); n++) {

        ANote curNote = new Note(allTones.get(n), o);

        if (curNote.compareTo(lowest) < 0) {
          continue;

        } else if (curNote.compareTo(highest) > 0) {
          break;

        } else {

          ASound soundFound = this.findNote(curNote, notes);
          if (!soundFound.isMute()) {

            if (soundFound.getState().equals(BeatState.START)) {
              stringRep.append(this.pad("X"));

              if (soundFound.getDuration() > 1) {
                this.notes.put(soundFound, soundFound.getDuration() - 1);
              }

            } else if (soundFound.getState().equals(BeatState.SUSTAIN)) {
              stringRep.append(this.pad("|"));
              this.notes.put(soundFound, this.notes.get(soundFound) - 1);
            }

          } else {

            soundFound = this.findNote(curNote, this.notes.keySet());
            if (!soundFound.isMute() && this.notes.get(soundFound) > 0) {
              stringRep.append(this.pad("|"));
              this.notes.put(soundFound, this.notes.get(soundFound) - 1);

              if (this.notes.get(soundFound) == 0) {
                this.notes.remove(soundFound);
              }
            }
            else {
              stringRep.append(this.pad(""));
            }
          }

        }
      }
    }

    return new String(stringRep);
  }

  /**
   * Find an ASound which plays given ANote in a list of ASound.
   * @param curNote  the given ANote.
   * @param notes    the collection of ASounds.
   * @return the first ASound that matches given note in the List.
   */
  private ASound findNote(ANote curNote, Collection<ASound> notes) {

    ASound ret = Sound.MUTE;

    for (ASound curSound : notes) {

      if (curNote.compareTo(curSound) == 0) {
        if (ret.equals(Sound.MUTE) || curSound.getState().equals(BeatState.START)) {
          ret = curSound;
        }
      }
    }

    return ret;
  }

  /**
   * Pad String with spaces to size 5.
   * @param str the String to pad with spaces.
   * @return the String padded with spaces, somewhat centered.
   */
  private String pad(String str) {
    int spaces = 5 - str.length();

    for (int s = 0; s < (spaces / 2); s++) {
      str += " ";
    }

    while (str.length() < 5) {
      str = " " + str;
    }

    return str;
  }
}
