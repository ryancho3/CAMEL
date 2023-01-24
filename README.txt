=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: ryancho
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays

  I used 2D Arrays to represent the state of the Chess game and board. The exact type of this 2D array is Piece[][].
  Each piece on the board is represented by a Piece object in the 2D array. Empty spaces are represented by null.
  I believe that this is an appropriate use of the concept because a game of chess is played on a two-dimensional board,
  which can be adequately represented by a 2D array. This is especially the case because the size of the chess board
  does not change.

  2. Inheritance and Subtyping

  I made an abstract class called Piece. All of the classes I made to represent the different pieces in chess are
  subtypes of this abstract Piece class. All pieces in chess need the same functionality, such as the ability to be
  drawn, store position, store how valuable the piece is, ability to calculate and return moves available to it, etc.
  Because code for many of these functionalities are the same, using an abstract class and subtyping from it is an
  appropriate choice. Also, by making all of the pieces a subtype of Piece, the entire gamestate can be represented by
  the 2D array mentioned above by declaring it as a Piece[][].

  3. Complex Game Logic

  Chess requires complex game logic as one needs to implement check, checkmate, castling, en passant, etc.
  A player's move should not put the player's king in check.
  Castling can only occur without obstructing pieces.
  En passant should only occur immediately after the offending move.
  Logic such as the above is complex enough to be considered complex game logic.
  I went beyond the rubric to include promotion of pawns as well.

  4. Advanced topic (or Recursion?)

  I implemented a Minimax algorithm that looks 3 moves ahead to select its move. Each piece is given an importance value
  to denote how valuable it is, and the ai adds these values up to determine the strength of a given game state.
  This ai implements heuristics and look-ahead to determine its best move.
  This ai was able to beat me in multiple matches.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  -Chess
  This class implements the main game logic. It stores the game state, checks if given moves are legal, executes moves
  and saves the results to its game state, checks for checks and checkmates, etc.
  This implementation is entirely independent from the GUI.

  -Board
  This class extends JPanel and represents the Chess object above in the GUI. It also takes mouse inputs from the user
  and applies the intended move to the Chess object.
  It also provides the current game state to Camel (the ai) and also prompts Camel to select its move. It then applies
  Camel's selected move to the Chess object.
  Board also updates and displays the aiDialogue (which conveys game state such as turn, check, checkmate).

  -RunChess

  This class initializes and displays the instruction window, the Board object,
  and the reset button that resets the game.
  'Runs' Chess as the name suggests.

  -Piece

  The abstract class that all piece objects extend. It includes common functionality such as draw, get and set methods,
  valid horizontal moves (for Rook and Queen), valid diagonal moves (for Bishop and Queen), etc. It stores whether
  the piece has moved (for castling, pawn initial movement), whether the piece is the player's piece, its importance
  value, file path to its image, etc. It also has abstract functions for availableMoves() and copy() to assert their
  requirement in any class that extend it.

  -Pawn, Bishop, Knight, Rook, Queen, King

  These classes extend the Piece class. They have unique availableMoves() that reflect their unique movement
  characteristics.

  -Move

  This class represents a chess move. If the user or the ai requests for a move to be applied, it is represented by a
  Move object. Move objects store the start position, end position, and whether the move is a special move (castling,
  en passant, promotion) and its associated effects (a king's castle move affects the Rook, en passant removes the
  opponent pawn from the board, etc.)

  -Camel

  Camel (Chess ai using Minimax EvaLuation) is class that represents the ai.
  Functionality includes:
   Getting all possible moves it can make given a certain Chess state,
   Evaluating a chess game state given the pieces left on the board.
   Recursively searching through possible moves (Minimax) to determine its best move up to depth 3






- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  Surprisingly implementing Check and Checkmate was one of the most difficult roadblocks that I encountered throughout
  the development process. Checkmate was especially difficult, because it required me to look at the game state one
  move in the future to determine whether the king was doomed. Determining the legality of a move based on whether
  the king would be put in check was also difficult.

  Design wise, I ran into the difficulty of implementing something in a subtype just to realize it is common code that
  should be implemented in the supertype. For example, the draw() function had to be removed from the piece subtypes and
  rewritten in the abstract Piece class. Conversely I implemented copy() in the abstract Piece class before realizing
  that they need to be unique to each subtype.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  I think I did a pretty good job in terms of designing my program to have separation of functionality.
  The Chess class is completely independent from Board and the rest of the GUI. Camel (ai) can also run independently
  from the GUI. I am also happy with my private state encapsulation.

  One thing I am slightly unhappy about is the interdependence of my Piece class (and associated subclasses) with the
  Chess class. The Chess class has to filter through moves generated by availableMoves() from the pieces to eliminate
  illegal moves. Also, I need to provide the pieces with the 2D array game state for them to determine their available
  moves. Further, position information is stored in duplicate: once in the index of the 2D array and once in the Piece
  class itself. If I were to refactor this program, I would eliminate these issues by relegating all game logic either
  to Chess or to Piece.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  Chess piece images: https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
  Information on Minimax: https://en.wikipedia.org/wiki/Minimax

