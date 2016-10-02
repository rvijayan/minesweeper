package org.minesweeper.game

import org.minesweeper.game.MineSweeper.Pos
import org.scalacheck._, Gen._

object MineSweeperGen {

  /** This is for valid input which is positive integers only. **/
  implicit val arbIntGen: Arbitrary[Int] = Arbitrary(posNum[Int])
  implicit val arbBoardGen: Arbitrary[TestBoard] = Arbitrary(boardGen)

  /** This case class ensures a valid pair of dimension and mine count. **/
  case class TestBoard(bounds: Int, mines: Seq[Pos]) {
    val cellCount = bounds * bounds
    require(cellCount >= mines.size, "The number of mines cannot exceed the number of Cells")
  }

  def positionGen(bounds: Int): Gen[Pos] =
    for {
      col <- choose[Int](0, bounds-1)
      row <- choose[Int](0, bounds-1)
    } yield Pos(col, row)

  lazy val boardGen: Gen[TestBoard] =
    for {
      bounds <- posNum[Int]
      cellCount = bounds*bounds
      mines <- listOf(positionGen(bounds)).filter(_.size <= cellCount)
  } yield TestBoard(bounds, mines.distinct)
}
