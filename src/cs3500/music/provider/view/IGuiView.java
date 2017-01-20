package cs3500.music.provider.view;

/**
 * Interface to represent a GUI View for the Music Editor.
 */
public interface IGuiView<NoteRep> extends IInteractiveView<NoteRep> {

  /**
   * Set the current beat in the view to the given beat and update the "seek" line accordingly.
   * @param beat  the beat that is the new current beat.
   */
  void setCurrentBeat(int beat);
}