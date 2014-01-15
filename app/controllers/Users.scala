package controllers

import play.api.mvc.{Action, Controller}
import models.User

object Users extends Controller {
  def findById(id: Long) = Action {
    println(User.findOne(id))
    Ok("success")
  }
  def findAll = Action {
    println(User.findAll)
    Ok("success")
  }

  def addUser = Action {
    println(User.insert(User(0, "testUser")))
    Ok("success")
  }
}