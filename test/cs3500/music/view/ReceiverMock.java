package cs3500.music.view;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * Represents a mock receiver meant for testing.
 */
public class ReceiverMock implements Receiver {

  public StringBuilder msg;

  /**
   * Constructs a ReceiverMock.
   * @param msg messages received
   */
  ReceiverMock(StringBuilder msg) {
    this.msg = msg;
  }

  @Override
  public void send(MidiMessage message, long timeStamp) {
    msg.append(" " + message.toString());
  }

  @Override
  public void close() {
    System.out.println(msg.toString());
  }

  @Override
  public int hashCode() {
    return this.msg.hashCode() * 10;
  }

  @Override
  public boolean equals(Object other) {
    if (! (other instanceof ReceiverMock)) {
      return false;
    }
    else {
      return ((ReceiverMock) other).msg.equals(this.msg);
    }
  }
}
