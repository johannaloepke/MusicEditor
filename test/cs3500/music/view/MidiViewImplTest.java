package cs3500.music.view;

import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import cs3500.music.controller.IMusicController;
import cs3500.music.controller.MusicController;
import cs3500.music.model.IMusicModel;
import cs3500.music.model.Melody;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Pitch;

import static org.junit.Assert.assertEquals;

/**
 * Tests MidiViewImpl using mock Receivers and Synthesizers.
 */
public class MidiViewImplTest {
  /**
   * Tests that playNote() correctly sends the relevant info to MIDI.
   */
  @Test
  public void testPlayNote() {
    IMusicView view = ViewFactory.createView(ViewFactory.MIDI_VIEW);

    StringBuilder msg = new StringBuilder();

    Receiver receiver =  new ReceiverMock(msg);
    Synthesizer synthesizer = new SynthesizerMock(receiver);

    Note n1 = new Note(Pitch.G, 6, 5, 10, 1);
    Note n2 = new Note(Pitch.E, 6, 5, 2, 1);

    Melody melody = new Melody();
    melody.addNote(n1, 0);
    msg.append("Note G6 of length 10 and instrument 1");
    melody.addNote(n2, 1);
    msg.append("Note E6 of length 2 and instrument 1");

    try {
      view.playNote(0, 10000, n1.getInstrument(), n1.getPitch().val, 5000);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }

    IMusicModel model = new MusicModel(10, 50, melody);
    IMusicController controller = new MusicController(model, view);
    try {
      view.playNote(10,
              10 + n2.getDuration(),
              n2.getInstrument(),
              ((n2.getOctave() * 12) + n2.getPitch().val + 8),
              model.microsecondsPerBeat());
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }


    Receiver test = new ReceiverMock(msg);

    try {
      test.send(new ShortMessage(ShortMessage.NOTE_ON, 1,
              (n2.getOctave() * 12) + n2.getPitch().val + 8, 1), 64);
      test.send(new ShortMessage(ShortMessage.NOTE_OFF, 1,
              (n2.getOctave() * 12) + n2.getPitch().val + 8, 1), 64);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }

    assertEquals(receiver.toString(), test.toString());
  }

  /**
   * Additional test that playNote() correctly sends the relevant info to MIDI.
   */
  @Test
  public void testPlayNoteTwo() {
    IMusicView view = ViewFactory.createView(ViewFactory.MIDI_VIEW);

    StringBuilder msg = new StringBuilder();

    Receiver receiver =  new ReceiverMock(msg);
    Synthesizer synthesizer = new SynthesizerMock(receiver);

    Note n1 = new Note(Pitch.A, 6, 5, 10, 1);

    Melody melody = new Melody();
    melody.addNote(n1, 0);
    msg.append("Note G6 of length 10 and instrument 1");

    try {
      view.playNote(0, 10000, n1.getInstrument(), n1.getPitch().val, 5000);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }

    IMusicModel model = new MusicModel(10, 50, melody);

    try {
      view.playNote(10,
              n1.getDuration(),
              n1.getInstrument(),
              ((n1.getOctave() * 12) + n1.getPitch().val + 8),
              model.microsecondsPerBeat());
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }


    Receiver test = new ReceiverMock(msg);

    try {
      test.send(new ShortMessage(ShortMessage.NOTE_ON, 1,
              (n1.getOctave() * 12) + n1.getPitch().val + 8, 1), 64);
      test.send(new ShortMessage(ShortMessage.NOTE_OFF, 1,
              (n1.getOctave() * 12) + n1.getPitch().val + 8, 1), 64);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }

    assertEquals(receiver.toString(), test.toString());
  }
}
