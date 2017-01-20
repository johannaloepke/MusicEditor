package cs3500.music.view;

import cs3500.music.provider.model.IViewModel;
import cs3500.music.provider.view.CompositeView;
import cs3500.music.provider.view.ConsoleView;
import cs3500.music.provider.view.IView;
import cs3500.music.provider.model.ASound;
import cs3500.music.provider.view.MusicEditorGuiView;
import cs3500.music.provider.view.MusicEditorMidiPlayer;

/**
 * Contains a single static method that allows for creation of views.
 * CHANGELOG: added codes for the other views given to us.
 */
public class ViewFactory {

  /**
   * Represents the GUIViewFrame String code.
   */
  public static final String GUI_VIEW = "gui";

  /**
   * Represents the TextView String code.
   */
  public static final String TEXT_VIEW = "console";

  /**
   * Represents the MidiViewImpl String code.
   */
  public static final String MIDI_VIEW = "midi";

  /**
   * Represents the MultiViewImpl String code.
   */
  public static final String MULTI_VIEW = "multi";

  /**
   * Creates a view depending on given values.
   * @param args represents the arguments given.
   * @return relevant IMusicView.
   * @throws IllegalArgumentException if the String isn't recognized.
   */
  public static IMusicView createView(String args) {
    switch (args) {
      case GUI_VIEW :
        return new GuiViewFrame();
      case TEXT_VIEW :
        return new TextView();
      case MIDI_VIEW :
        return new MidiViewImpl();
      case MULTI_VIEW :
        return new MultiViewImpl();

      default :
        throw new IllegalArgumentException("Unrecognized String command for frame: " + args);
    }
  }

  /**
   * Creates the other person's views.
   * @param args arguments to specify a song
   * @param model IViewModel for this view
   * @return the appropriate view.
   */
  public static IView<ASound> createView(String args, IViewModel<ASound> model) {
    switch (args) {
      case GUI_VIEW :
        return new MusicEditorGuiView(model);
      case TEXT_VIEW :
        return new ConsoleView(model, System.out);
      case MIDI_VIEW :
        return new MusicEditorMidiPlayer(model);
      case MULTI_VIEW :
        return new CompositeView<>(new MusicEditorGuiView(model), new MusicEditorMidiPlayer(model));
      default :
        throw new IllegalArgumentException("Unrecognized String command for frame.");
    }
  }
}
