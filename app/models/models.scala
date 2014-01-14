package models

import org.squeryl.KeyedEntity
import org.squeryl.Schema
import org.squeryl.Table
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Query
import collection.Iterable

case class User(id: Long, name: String) extends KeyedEntity[Long]

case class Board(id: Long, title: String, content: String, user_id: Long) extends KeyedEntity[Long]

object Board {
  def allQ: Query[Board] = from(Database.boardsTable) {
    board => select(board) orderBy(board.id asc)
  }
  def findByUserNameLikeQ(name: String): Query[Board] = {
    join(Database.boardsTable, Database.usersTable)((board, user) =>
      where(user.name like "%"+name+"%").select(board).on(board.user_id === user.id)
      )
  }

  def findByUserNameLike(name: String): Iterable[Board] = inTransaction {
    findByUserNameLikeQ(name).toList
  }

  def findAll: Iterable[Board] = inTransaction {
    allQ.toList
  }

  def insert(board: Board): Board = inTransaction{
    Database.boardsTable.insert(board)
  }

  def update(board: Board) {
    inTransaction{ Database.boardsTable.update(board) }
  }
}

object User {
  def allQ: Query[User] = from(Database.usersTable) {
    user => select(user) orderBy(user.id asc)
  }

  def findAll: Iterable[User] = inTransaction {
    allQ.toList
  }

  def insert(user: User): User = inTransaction{
    Database.usersTable.insert(user)
  }

  def update(user: User) {
    inTransaction{ Database.usersTable.update(user) }
  }
}

object Database extends Schema {
  val usersTable: Table[User] = table[User]("User")
  val boardsTable: Table[Board] = table[Board]("Board")

  on(usersTable) { u => declare {
    u.id is(autoIncremented)
    }}

  on(boardsTable) { b => declare {
    b.id is(autoIncremented)
    }}
}