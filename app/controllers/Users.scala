package controllers

import play.api.Play.current
import play.api.mvc.{Action, Controller}
import play.api.cache.Cache
import models.User

object Users extends Controller {
  
  def findById(id: Long) = Action {

    val user: User = Cache.getOrElse[User]("user-"+id) {
      println("reGet!")
      User.findOne(id)
    }
    println(user)
    Ok("success")
  }

  def findAll = Action {
    println(User.findAll)
    Ok("success")
  }

  def addUser = Action {
    val insertedUser = User.insert(User(0, "testUser22"))
    Cache.set("user-"+insertedUser.id, insertedUser)
    Ok("success")
  }
}