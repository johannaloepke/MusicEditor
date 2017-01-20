CONTROLS

To add a note, click the mouse at the desired pitch and position, dragging until you reach the
duration you want.
To delete a note, click the note head (black square) and then press the backspace key.
Press the space bar to play and pause the music.
Use the arrow keys to scroll.
Press "Home" to visually return to the beginning of a piece and "End" to jump to the end.
To add a repeat, click the desired start of a repeat above the staff line. A prompt will ask how
many endings you wish to have, type 0 to cancel, 1 for a regular repeat, or >1 for a multi-end
repeat. This will lead to more queries about where those repeats should start.

MODEL

IMusicModel is the overarching interface for the MusicModel. It contains the methods implemented
by MusicModel.

MusicModel contains a List of Melodies (see below) representing a stream of music, an integer
representing the beat of the music, and a double volume multiplier.

A Melody represents, in essence, a single stream of music. It is made up of a List of beats and a
dynamic.

An ASound represents a type of Sound that can exist. It is one of class Note, Rest, and Chord. It
has a duration represented by a Fraction, and an integer volume, both protected access.

A Note is a standard sound. It also has a Pitch and an integer octave.

A Rest is a standard silence.

A Chord has a list of Notes. Every Note's duration is set to the Chord's duration. Chords all play
at the same time.

A Pitch is an enum representing one of the possible musical pitches. It has a String name, String
sharpName, String flatName, and integer value.

A Repeat is a repetition in music that extends AFlag.

AFlag is an abstract class that implements IFlag. It has a HashMap of startBeats and a HashMap
of endPosiitions, as well as keeping track of number of repetitions and the skip point.

IFlag is an interface that represents a signal to take action at that part of the musical piece.

MusicModel also has a private class ActiveSound, which wraps a Note with a double representing how
long it has left to be active. This allows for methods to determine how long Notes have to play
without altering the Notes' durations.

VIEW

IMusicView is the overarching interface for all views. Implementations of this interface take the
form of text, midi music, and a GUI.

A ViewFactory creates the following implementations of IMusicView based on given Strings:

TextView displays a composition using the console. The columns indicate pitch and octave while the
rows are beat numbers. X's are placed at a Note's starting point and |'s are shown below for the
length of the note's duration.

MidiViewImpl has fields of Synthesizer and Receiver. To play the notes, the class sends start and
stop MidiMessages, making sure to place notes on their appropriate channel and with the correct
instrument.

GuiViewFrame displays a pop-up musical staff takes a NoteDisplayPanel, our customized JPanel, to
display the graphics.

A NoteDisplayPanel is specialized for the GuiViewFrame, creating a grid with the columns as beat
numbers and the rows as pitch and octave. Notes are black at their starting point and have varying
colors based on their instrument for the duration.

ConcreteGuiViewPanel is a dummy view that was given to us and may be helpful in the future.

MultiViewImpl is a view that has both a MidiViewImpl and a GuiViewFrame. It selectively calls the
relevant methods from one or both of its MidiViewImpl and GuiViewFrame in its interface methods.

CONTROLLER

IMusicController is the controller interface. It only has one methods, goController(go was too
short a name) to start the program.

MusicController implements IMusicController and has the fields of a model and a view. It then
changes the model and displays the view, acting as an intermediary. It also creates an instance of
a KeyHandler and has a MouseHandler, both of which help it process events. It keeps information
related to certain mouse events in order to run its programs.

KeyHandler has three maps from Integer to a Runnable representing what will happen on key presses,
clicks, and releases respectively.

MouseHandler takes three Runnables.

UTIL

MusicReader interprets a file and parses it so it can be read and played.

A CompositionBuilder is a generic builder that builds a model.


