package cs3500.music.adaptor;

import java.util.ArrayList;
import java.util.List;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.Melody;
import cs3500.music.model.MusicModel;
import cs3500.music.provider.model.ASound;
import cs3500.music.provider.model.IViewModel;
import cs3500.music.model.Note;
import cs3500.music.util.CompositionBuilder;

/**
 * Adaptor to translate between the given view-model and our model. Extends our music model,
 * implements given view-model to promise that functionality.
 */
public class ModelAdaptor extends MusicModel implements IViewModel<ASound> {

  /**
   * Allows for building a Composition without knowing of the MusicModel.
   */
  public static class Builder implements CompositionBuilder<IViewModel<ASound>> {
    IMusicModel model = new ModelAdaptor(4, 100);

    @Override
    public ModelAdaptor build() {
      return (ModelAdaptor)model;
    }

    @Override
    public CompositionBuilder<IViewModel<ASound>> setTempo(int tempo) {
      model.setTempo(tempo);
      return this;
    }

    @Override
    public CompositionBuilder<IViewModel<ASound>> addNote(int start, int end, int instrument,
                                                  int pitch, int volume) {
      model.addNote(start, end, instrument, pitch, volume);
      return this;
    }
  }

  /**
   * Constructs a ModelAdaptor.
   * @param melodies melodies in the model.
   * @param volumeMult increases volume of the piece by this multiple.
   */
  public ModelAdaptor(List<Melody> melodies, int volumeMult) {
    super(melodies, volumeMult);
  }

  /**
   * Constructs a ModelAdaptor.
   * @param beat how many beats in a standard measure.
   * @param volumeMult increases volume of the piece by this multiple.
   * @param melodies melodies in this model.
   */
  public ModelAdaptor(int beat, int volumeMult, Melody... melodies) {
    super(beat, volumeMult, melodies);
  }

  @Override
  public int getDurationInBeats() {
    return this.lengthOfSongInBeats();
  }

  @Override
  public List getNotesAt(int beat) throws IllegalArgumentException {

    List notes  = this.getAllNotesMap().get(beat);
    if (notes == null) {
      return new ArrayList();
    }
    return notes;
  }

  @Override
  public List<List<ASound>> getAllNotes() throws IllegalArgumentException {
    return null;
  }

  @Override
  public Note getHighest() {
    return this.getHighestNote();
  }

  @Override
  public Note getLowest() {
    return this.getLowestNote();
  }

  @Override
  public int getTempo() {
    return (int) this.microsecondsPerBeat();
  }
}
