package controllers

import play.api.mvc.{Action, Controller}
import models.{Board, User}

object Boards extends Controller {
  def findAll = Action {
    println(Board.findAll)
    Ok("success")
  }

  def findMyBoards(id: Long) = Action {
    println(s"findMyBoards by ${id}")
    println(Board.findByUserId(id))
    Ok("success")
  }

  def findByUserName(name: String) = Action {
    println(Board.findByUserNameLike(name))
    Ok("success")
  }

  def addBoard = Action {
    println(Board.insert(Board(0, "testTitle7", "testContent7", 2L)))
    Ok("success")
  }
}