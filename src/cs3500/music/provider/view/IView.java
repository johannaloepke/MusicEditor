package cs3500.music.provider.view;

import cs3500.music.provider.model.ANote;

/**
 * General interface for a view for {@link ANote}.
 */
public interface IView<NoteRep> {

  /**
   * Activates the current view.
   */
  void activate();

  /**
   * Deactivates the current view.
   */
  void deactivate();

  /**
   * Plays or Pauses the view depending on whether its playing or paused.
   */
  void playPause();

  /**
   * Is this view interactive?
   * @return true if it is interactive, false otherwise.
   */
  boolean isInteractive();
}
