package controllers

import play.api.mvc.{Action, Controller}
import models.User

object Users extends Controller {
  def findAll = Action {
    println(User.findAll)
    Ok("success")
  }

  def addUser = Action {
    println(User.insert(User(2L, "testUser")))
    Ok("success")
  }
}