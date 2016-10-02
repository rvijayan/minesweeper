package org.minesweeper.game

import org.specs2.mutable.Specification
import org.specs2.ScalaCheck

import org.minesweeper.game.MineSweeper._

final class MineSweeperSpec extends Specification
  with ScalaCheck {

  import org.minesweeper.game.MineSweeperGen._

  "Minesweeper" should {
    "have no safe cells when the board full of mines" in prop { bounds: Int =>
      val mines = pointGenerator(bounds)
      val board = game(bounds, mines)
      board.values.forall(_ == Mine) should beTrue
    }

    "have no mines when all the cells on the board are safe" in prop { bounds: Int =>
      val board = game(bounds, Seq.empty[Pos])
      board.values.exists(_ == Mine) should beFalse
    }

    "the sum of all mine counters should not be less than the number of mines" in prop {
      (tb: TestBoard) =>

        val board = game(tb.bounds, tb.mines)
        val totalMinesCount =
          board.values.collect { case Safe(surroundingMines) => surroundingMines }.sum

        (board.values.forall(_ == Mine) || totalMinesCount >= tb.mines.size) must beTrue
    }


    "the neighbours for any mine should have a mine count of at least 1" in prop {
      (tb: TestBoard) =>

      val board = game(tb.bounds, tb.mines)

      if (board.size == tb.mines.size) {
        // this proposition does not hold for the case where the board is full of mines
        true must beTrue
      } else
        tb.mines.forall { mine =>
          val neighbours = mine.neighbours.filter(n => terrain(tb.bounds)(n))

          val totalMineCount = neighbours
            .flatMap(x => board.get(x))
            .collect { case Safe(surroundingMines) => surroundingMines }.sum

          totalMineCount >= 1 ||
            neighbours.forall(n => board.get(n) == Some(Mine))
        } must beTrue
    }

  }

}