package cs3500.music.provider.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Composites and synchronizes a {@link IGuiView} with a {@link ISyncView}.
 */
public class CompositeView<NoteRep> implements IInteractiveView<NoteRep>, ISyncView<NoteRep> {
  protected IGuiView<NoteRep> guiView;
  protected ISyncView<NoteRep> syncView;

  /**
   * Creates a CompositeView with given IGuiView and ISyncView.
   * @param guiView         the IGuiView.
   * @param syncView        the ISyncView.
   */
  public CompositeView(IGuiView<NoteRep> guiView, ISyncView<NoteRep> syncView) {
    this.guiView = guiView;
    this.syncView = syncView;
  }

  @Override
  public void activate() {

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        guiView.setCurrentBeat(syncView.getTime());
      }
    };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(task, 0, 100);
    guiView.activate();
    syncView.activate();
  }

  @Override
  public void deactivate() {
    guiView.deactivate();
    syncView.deactivate();
  }

  @Override
  public void playPause() {
    guiView.playPause();
    syncView.playPause();
  }

  @Override
  public boolean isInteractive() {
    return true;
  }

  @Override
  public void addMouseListener(MouseListener m) {
    this.guiView.addMouseListener(m);
  }

  @Override
  public void addKeyListener(KeyListener k) {
    this.guiView.addKeyListener(k);
  }

  @Override
  public NoteRep getNoteAt(int x, int y) {
    return this.guiView.getNoteAt(x, y);
  }

  @Override
  public int getBeatAt(int x) {
    return this.guiView.getBeatAt(x);
  }

  @Override
  public int getPitchAt(int y) {
    return this.guiView.getPitchAt(y);
  }

  @Override
  public void setBeatAt(int x) throws IllegalArgumentException {
    this.syncView.setBeatAt(x);
  }

  @Override
  public int getCurBeat() {
    return this.syncView.getTime();
  }

  @Override
  public int getTime() {
    return this.syncView.getTime();
  }

  @Override
  public void removeNote(NoteRep note, int beat) {
    this.syncView.removeNote(note, beat);
    this.guiView.removeNote(note, beat);
  }

  @Override
  public void addNote(NoteRep note, int beat) {
    this.syncView.addNote(note, beat);
    this.guiView.addNote(note, beat);
  }
}
