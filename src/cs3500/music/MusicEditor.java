package cs3500.music;

import cs3500.music.adaptor.ModelAdaptor;
import cs3500.music.controller.IMusicController;
import cs3500.music.controller.MusicController;
import cs3500.music.controller.OtherMusicController;
import cs3500.music.model.IMusicModel;
import cs3500.music.model.MusicModel;
import cs3500.music.provider.model.ASound;
import cs3500.music.provider.model.IViewModel;
import cs3500.music.provider.view.IView;
import cs3500.music.util.MusicReader;
import cs3500.music.view.IMusicView;
import cs3500.music.view.ViewFactory;

import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;


public class MusicEditor {
  /**
   * Main method that runs this MusicEditor.
   * @param args array of String arguments to be executed.
   * @throws IOException in theory.
   * @throws InvalidMidiDataException if the data is invalid for MIDI.
   *         CHANGELOG: If you give the specification of "midi," "gui," "console." or "multi" as a
   *         second argument then our old views will display. Otherwise, if left blank, the
   *         CompositeView of our providers will appear.
   */
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    IMusicController controller;
    if (args.length == 2) {
      IMusicModel model = MusicReader.parseFile(new FileReader(args[0]),
              new MusicModel.Builder());
      IMusicView view = ViewFactory.createView(args[1]);

      controller = new MusicController(model, view);
    }
    else {
      IViewModel<ASound> viewModel = MusicReader.parseFile(new FileReader(args[0]),
              new ModelAdaptor.Builder());
      IView<ASound> view = ViewFactory.createView(ViewFactory.MULTI_VIEW, viewModel);
      controller = new OtherMusicController(viewModel, view);
    }
    controller.goController();

  }
}
