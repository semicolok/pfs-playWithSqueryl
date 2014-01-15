package models

import org.squeryl.{KeyedEntity, Query, Schema, Table}
import org.squeryl.dsl.{OneToMany, ManyToOne}
import org.squeryl.PrimitiveTypeMode._
import collection.Iterable

object Database extends Schema {
  val usersTable: Table[User] = table[User]("User")
  val boardsTable: Table[Board] = table[Board]("Board")

  val userToBoard = oneToManyRelation(usersTable, boardsTable)
    .via((u, b) => u.id === b.user_id)

  on(usersTable) { u => declare {
    u.id is(autoIncremented)
    }}

  on(boardsTable) { b => declare {
    b.id is(autoIncremented)
    }}
}

case class User(id: Long, name: String) extends KeyedEntity[Long] {
  lazy val boards: OneToMany[Board] = Database.userToBoard.left(this)
}

case class Board(id: Long, title: String, content: String, user_id: Long) extends KeyedEntity[Long] {
  lazy val user: ManyToOne[User] = Database.userToBoard.right(this)
}

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

  def findByUserId(id: Long): Iterable[Board] = inTransaction {
    // from(Database.boardsTable)(board => where(board.user_id === id).select(board).orderBy(board.id asc)).toList
    User.findOne(id).boards.toList
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

  def findOne(id: Long): User = inTransaction {
    from(Database.usersTable)(user => where(user.id === id) select(user)).single
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

  def addBoard(user: User, board: Board) = {

    // user.boards.assign(board)
    // transaction {
    //   Database.boardsTable.insert(board)
    // }

    // only work with extending KeyedEntity classes
    transaction {
      user.boards.associate(board)
    }

  }
}