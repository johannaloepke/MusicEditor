package cs3500.music.controller;

import java.awt.Point;
import java.awt.event.KeyEvent;

import cs3500.music.provider.model.ASound;
import cs3500.music.model.Pitch;
import cs3500.music.provider.model.IViewModel;
import cs3500.music.model.Note;
import cs3500.music.provider.view.IInteractiveView;
import cs3500.music.provider.view.IView;

/**
 * Extension of {@code MusicController} used for running Music Editors, and works on views given
 * in latest assignment.
 */
public class OtherMusicController implements IMusicController {
  private IViewModel<ASound> viewModel;
  private IView<ASound> view;
  private MouseHandler mouseHandler;
  private ASound clickedNote;
  private Integer selectedBeat;
  private Point pressed;

  /**
   * Constructs an OtherMusicController.
   *
   * @param viewModel the model this controller manipulates
   * @param view  the view this controller gets input from
   */
  public OtherMusicController(IViewModel<ASound> viewModel, IView<ASound> view) {
    this.viewModel = viewModel;
    this.view = view;
    KeyHandler keyHandler = new KeyHandler();
    if (view.isInteractive()) {
      ((IInteractiveView<ASound>)view).addKeyListener(keyHandler);
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
    }


    if (view.isInteractive()) {
      ((IInteractiveView<ASound>) view).addMouseListener(mouseHandler);
    }

    // plays the midi music
    keyHandler.addToKeyPressedMap(KeyEvent.VK_SPACE, new Runnable() {
      @Override
      public void run() {
        view.playPause();
      }
    });
    // takes red line to the beginning of song
    keyHandler.addToKeyPressedMap(KeyEvent.VK_HOME, new Runnable() {
      @Override
      public void run() {
        ((IInteractiveView)view).setBeatAt(0);
      }
    });
    // takes red line to the end of the song
    keyHandler.addToKeyPressedMap(KeyEvent.VK_END, new Runnable() {
      @Override
      public void run() {
        ((IInteractiveView)view).setBeatAt(viewModel.getDurationInBeats());
      }
    });

    // moves red line to the right
    keyHandler.addToKeyPressedMap(KeyEvent.VK_RIGHT, new Runnable() {
      @Override
      public void run() {
        ((IInteractiveView)view).setBeatAt(((IInteractiveView)view).getCurBeat() + 1);
      }
    });

    // moves red line to the left
    keyHandler.addToKeyPressedMap(KeyEvent.VK_LEFT, new Runnable() {
      @Override
      public void run() {
        ((IInteractiveView)view).setBeatAt(((IInteractiveView)view).getCurBeat() - 1);
      }
    });
    // moves display up
    keyHandler.addToKeyPressedMap(KeyEvent.VK_UP, new Runnable() {
      @Override
      public void run() {
        // their view does not provide capabilities to do this.
      }
    });

    // moves display down
    keyHandler.addToKeyPressedMap(KeyEvent.VK_DOWN, new Runnable() {
      @Override
      public void run() {
        // their view does not provide capabilities to do this.
      }
    });


    keyHandler.addToKeyPressedMap(KeyEvent.VK_BACK_SPACE, new Runnable() {
      @Override
      public void run() {
        if (clickedNote != null) {
          ((IInteractiveView<ASound>)view).removeNote(clickedNote, selectedBeat);
          clickedNote = null;
          selectedBeat = null;
        }
      }
    });
  }

  @Override
  public void goController() {
    view.activate();
  }

  /**
   * Changes mouse info based on where the mouse was pressed.
   * @param pressed represents the Point pressed.
   */
  private void sendPressedInfo(Point pressed) {
    this.pressed = pressed;
    int xPosn = (int)pressed.getX();
    int yPosn = (int)pressed.getY();
    this.selectedBeat = ((IInteractiveView<ASound>)view).getBeatAt(xPosn);


    clickedNote = ((IInteractiveView<ASound>)view).getNoteAt(xPosn, yPosn);
  }

  /**
   * Changes model and view based on where mouse clicks happened.
   * @param click represents the point clicked.
   */
  private void sendClickInfo(Point click) {
    int xPosn = (int)click.getX();
    int yPosn = (int)click.getY();
  }

  /**
   * Changes model and view based on mouse release events.
   * @param released represents the Point released from.
   */
  private void sendReleasedInfo(Point released) {
    if (clickedNote != null && pressed.equals(released)) {
      return;
    }


    int end = ((IInteractiveView<ASound>)view).getBeatAt(((int)released.getX()));
    int pitch = ((IInteractiveView<ASound>)view).getPitchAt(((int)released.getY()));

    ((IInteractiveView<ASound>)view).addNote(new Note(Pitch.getPitchAtVal(pitch % 12), pitch / 12,
            Math.max(end - selectedBeat + 1, selectedBeat + 1 - end)), Math.min(selectedBeat, end));

    selectedBeat = null;
    clickedNote = null;
  }
}
