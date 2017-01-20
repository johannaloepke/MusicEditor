package cs3500.music.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the TextView class.
 */
public class TextViewTest {

  /** Tests the getCurrentState method. */
  @Test
  public void testGetCurrentState() {
    IMusicView view = ViewFactory.createView(ViewFactory.TEXT_VIEW);
    assertEquals(view.getCurrentState(), "");
    view.changeCurrentState("C#3");
    assertEquals(view.getCurrentState(), "C#3");
  }

  /** Tests the display method. */
  @Test
  public void testDisplay() throws Exception {
    IMusicView view = ViewFactory.createView(ViewFactory.TEXT_VIEW);
    assertEquals(view.getCurrentState(), "");

    view.changeCurrentState("      E3   F3   F#3  G3   G#3  A4   A#4  B4   C4   C#4  D4   D#4" +
            "  E4   F4   F#4  G4  \n" +
            "    0                 X                                            X              " +
            "    \n" +
            "    1                 |                                            |              " +
            "    \n" +
            "    2                 |                                  X                        " +
            "    \n" +
            "    3                 |                                  |                         " +
            "   \n" +
            "    4                 |                        X                                   " +
            "   \n" +
            "    5                 |                        |                                   " +
            "   \n" +
            "    6                 |                                  X                         " +
            "   \n" +
            "    7                                                    |                          " +
            "  \n" +
            "    8                 X                                            X                " +
            "  \n" +
            "    9                 |                                            |                " +
            "  \n" +
            "   10                 |                                            X              " +
            "    \n" +
            "   11                 |                                            |              " +
            "    \n" +
            "   12                 |                                            X              " +
            "    \n" +
            "   13                 |                                            |              " +
            "    \n" +
            "   14                 |                                            |             " +
            "     \n" +
            "   15                                                                           " +
            "      \n" +
            "   16                 X                                  X                      " +
            "      \n" +
            "   17                 |                                  |                       " +
            "     \n" +
            "   18                 |                                  X                       " +
            "     \n" +
            "   19                 |                                  |                        " +
            "    \n" +
            "   20                 |                                  X                        " +
            "    \n" +
            "   21                 |                                  |                       " +
            "     \n" +
            "   22                 |                                  |                        " +
            "    \n" +
            "   23                 |                                  |                       " +
            "     \n" +
            "   24                 X                                            X             " +
            "" +
            "     \n" +
            "   25                 |                                            |             " +
            "     \n" +
            "   26                                                                           " +
            "  X   \n" +
            "   27                                                                            " +
            " |   \n" +
            "   28                                                                           " +
            "  X   \n" +
            "   29                                                                           " +
            "  |   \n" +
            "   30                                                                           " +
            "  |   \n" +
            "   31                                                                            " +
            " |   \n" +
            "   32                 X                                            X             " +
            "     \n" +
            "   33                 |                                            |             " +
            "     \n" +
            "   34                 |                                  X                        " +
            "    \n" +
            "   35                 |                                  |                       " +
            "     \n" +
            "   36                 |                        X                                 " +
            "     \n" +
            "   37                 |                        |                                  " +
            "    \n" +
            "   38                 |                                  X                       " +
            "     \n" +
            "   39                 |                                  |                       " +
            "     \n" +
            "   40                 X                                            X              " +
            "    \n" +
            "   41                 |                                            |             " +
            "     \n" +
            "   42                 |                                            X              " +
            "    \n" +
            "   43                 |                                            |            " +
            "      \n" +
            "   44                 |                                            X              " +
            "    \n" +
            "   45                 |                                            |             " +
            "     \n" +
            "   46                 |                                            X              " +
            "    \n" +
            "   47                 |                                            |              " +
            "    \n" +
            "   48                 X                                  X                       " +
            "     \n" +
            "   49                 |                                  |                        " +
            "    \n" +
            "   50                 |                                  X                       " +
            "     \n" +
            "   51                 |                                  |                       " +
            "     \n" +
            "   52                 |                                            X             " +
            "     \n" +
            "   53                 |                                            |             " +
            "     \n" +
            "   54                 |                                  X                       " +
            "     \n" +
            "   55                 |                                  |                        " +
            "    \n" +
            "   56  X                                       X                                 " +
            "     \n" +
            "   57  |                                       |                                " +
            "      \n" +
            "   58  |                                       |                                 " +
            "     \n" +
            "   59  |                                       |                                   " +
            "   \n" +
            "   60  |                                       |                                 " +
            "     \n" +
            "   61  |                                       |                                 " +
            "     \n" +
            "   62  |                                       |                                 " +
            "     \n" +
            "   63  |                                       |                                " +
            "      \n");

    // console should display Mary Had A Little Lamb
    view.display();
  }

  /*@Test
  public void testHandNotes() {
    IMusicView view = ViewFactory.createView(ViewFactory.TEXT_VIEW);
    Map<Integer, List<Pair<Integer, Note>>> map = new TreeMap<Integer, List<Pair<Integer, Note>>>();
    List<Pair<Integer, Note>> list = new ArrayList<Pair<Integer, Note>>();
    Pair<Integer, Note> pair = new Pair<>(5, new Note(Pitch.C, 4, 4, 4, 4));
    Pair<Integer, Note> pair2 = new Pair<>(5, new Note(Pitch.A, 5, 5, 5, 5));
    list.add(pair);
    list.add(pair2);
    map.put(0, list);
    view.handNotes(map, 0, 10);
    assertEquals(view.getCurrentState(), "");
  }*/

}
