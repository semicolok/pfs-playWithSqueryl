package controllers

import play.api.mvc.{Action, Controller}
import models.Board

object Boards extends Controller {
  def findAll = Action {
    println(Board.findAll)
    Ok("success")
  }

  def findByUserName(name: String) = Action {
    println(Board.findByUserNameLike(name))
    Ok("success")
  }

  def addBoard = Action {
    println(Board.insert(Board(7L, "testTitle7", "testContent7", 2L)))
    Ok("success")
  }
}