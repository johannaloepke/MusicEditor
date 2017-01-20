package cs3500.music.provider.view;

import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import cs3500.music.provider.model.ASound;
import cs3500.music.provider.model.IViewModel;

/**
 * The GUI View for a Music Editor.
 */
public class MusicEditorGuiView extends JFrame implements IGuiView<ASound> {
  private IViewModel<ASound> viewModel;
  private final int width;
  private final int height;
  private final int beatSize;
  private NotesPanel notesPanel;
  private JScrollPane scrollPanel;

  /**
   * Creates a new non-scrollable MusicEditorGuiView.
   * @param viewModel         the ViewModel provided with data.
   */
  public MusicEditorGuiView(IViewModel<ASound> viewModel, int width, int height) {

    try {
      this.viewModel = Objects.requireNonNull(viewModel);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("ViewModel provided was null.");
    }

    this.beatSize = 15;
    this.width = width;
    this.height = height;

    this.notesPanel = new NotesPanel(this.viewModel, this.beatSize, this.width);
    this.scrollPanel = new JScrollPane(this.notesPanel);
    this.scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.scrollPanel.getVerticalScrollBar().setUnitIncrement(beatSize * 4);

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.getContentPane().add(this.scrollPanel);
    this.pack();
  }

  /**
   * Creates a GUI View that displays all notes at once.
   * @param viewModel  the ViewModel provided with data.
   */
  public MusicEditorGuiView(IViewModel<ASound> viewModel) {
    this(viewModel, (viewModel.getDurationInBeats() + 5) * 15, 700);
    this.scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  @Override
  public void activate() {
    this.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(this.width, this.height);
  }

  @Override
  public void deactivate() {
    this.setVisible(false);
  }

  @Override
  public void playPause() {
    // Does nothing because there is no concept of play/pause in a graphical view.
    return;
  }

  @Override
  public boolean isInteractive() {
    return true;
  }

  @Override
  public void setCurrentBeat(int beat) {
    this.notesPanel.setCurrentBeat(beat);
    this.getContentPane().repaint();
  }

  @Override
  public ASound getNoteAt(int x, int y) {
    return this.notesPanel.getNoteAt(x, y);
  }

  @Override
  public int getBeatAt(int x) {
    return this.notesPanel.getBeatAt(x);
  }

  @Override
  public int getPitchAt(int y) {
    return this.notesPanel.getPitchAt(y);
  }

  @Override
  public void setBeatAt(int x) throws IllegalArgumentException {
    this.notesPanel.setBeat(x);
    this.scrollPanel.setAutoscrolls(true);
  }

  @Override
  public int getCurBeat() {
    return this.notesPanel.getCurBeat();
  }

  @Override
  public void removeNote(ASound note, int beat) {
    this.notesPanel.removeNote(note, beat);
  }

  @Override
  public void addNote(ASound note, int beat) {
    this.notesPanel.addNote(note, beat);
  }

  @Override
  public void addMouseListener(MouseListener m) {
    this.notesPanel.addMouseListener(m);
  }
}
