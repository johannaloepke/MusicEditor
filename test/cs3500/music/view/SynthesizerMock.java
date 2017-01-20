package cs3500.music.view;

import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.VoiceStatus;

/**
 * Represents a mock synthesizer meant for testing.
 */
public class SynthesizerMock implements Synthesizer {
  Receiver receiver;

  /**
   * Constructs a mock synthesizer.
   * @param receiver given receiver
   */
  SynthesizerMock(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public int getMaxPolyphony() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public long getLatency() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public MidiChannel[] getChannels() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public VoiceStatus[] getVoiceStatus() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public boolean isSoundbankSupported(Soundbank soundbank) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public boolean loadInstrument(Instrument instrument) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public void unloadInstrument(Instrument instrument) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public boolean remapInstrument(Instrument from, Instrument to) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public Soundbank getDefaultSoundbank() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public Instrument[] getAvailableInstruments() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public Instrument[] getLoadedInstruments() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public boolean loadAllInstruments(Soundbank soundbank) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public void unloadAllInstruments(Soundbank soundbank) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public boolean loadInstruments(Soundbank soundbank, Patch[] patchList) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public void unloadInstruments(Soundbank soundbank, Patch[] patchList) {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public Info getDeviceInfo() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public void open() throws MidiUnavailableException {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public void close() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public boolean isOpen() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public long getMicrosecondPosition() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public int getMaxReceivers() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public int getMaxTransmitters() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public Receiver getReceiver() throws MidiUnavailableException {
    return this.receiver;
  }

  @Override
  public List<Receiver> getReceivers() {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public Transmitter getTransmitter() throws MidiUnavailableException {
    throw new UnsupportedOperationException("Not testing this.");
  }

  @Override
  public List<Transmitter> getTransmitters() {
    throw new UnsupportedOperationException("Not testing this.");
  }
}
