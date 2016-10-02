package org.minesweeper.game

object MineSweeper {

  type Col = Int
  type Row = Int

  /** Data structure for accepting position of each Cell. */
  case class Pos(col: Col, row: Row) {
    private def moveCol(col: Col) = this.copy(col = this.col + col)
    private def moveRow(row: Row) = this.copy(row = this.row + row)

    private def left = moveCol(-1)
    private def right = moveCol(1)
    private def down = moveRow(1)
    private def up = moveRow(-1)

    lazy val neighbours: Seq[Pos] =
      Seq(
        left,
        left.up,
        left.down,
        right,
        right.up,
        right.down,
        up,
        down
      )
  }

  /** Custom type for a cell. A cell can be a Mine or a Safe cell
    * which contains a count of surrounding mines.
    * */
  sealed trait Cell extends Product with Serializable
  case object Mine extends Cell
  case class Safe(surroundingMines: Int) extends Cell

  /**
   * This method creates empty positions for the board.
   *
   * @param bounds Size of the board. Eg: A size of 4 can generate a board of 16 empty cells.
   * @return A sequence of positions
   */
  def pointGenerator(bounds: Int): Seq[Pos] = {
    require(bounds > 0, "Bounds must be a positive integer.")
    for {
      col <- 0 to bounds-1
      row <- 0 to bounds-1
    } yield Pos(col, row)
  }

  type Terrain = Pos => Boolean

  /** This is a partial method which checks all the boundary conditions of the board. */
  def terrain(bounds: Int): Terrain = (p: Pos) =>
    p.row >= 0 && p.row < bounds && p.col >= 0 && p.col < bounds

  /**
   * This method creates a board of all given mine cells(Mine) and safe cells(Safe).
   * A safe cell contains a count of neighbouring mines.
   *
   * @param bounds Size of the board.
   * @param mines Sequence of cells with mines
   * @return Map of position and Cell
   */
  def game(bounds: Int, mines: Seq[Pos]): Map[Pos, Cell] = {

    def surveyNeighbours(pos: Pos): Int =
      pos.neighbours.collect { case n
        if terrain(bounds)(n) && mines.contains(n) => 1
      }.sum

    def cell(pos: Pos): Cell = {
      require(terrain(bounds)(pos), s"Given position $pos is not on the board!")

      if (mines.contains(pos))
        Mine
      else
        Safe(surveyNeighbours(pos))
    }

    pointGenerator(bounds).map { p =>
      p -> cell(p)
    }.toMap
  }
}
