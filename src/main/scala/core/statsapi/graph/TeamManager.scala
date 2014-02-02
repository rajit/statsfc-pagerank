package core.statsapi.graph

import core.statsapi.graph.GameResult.Team
import akka.actor.{Props, ActorRef, Actor}

object TeamManager {
  case class TeamFinished(team: Team)
}

class TeamManager extends Actor {
  var teams: Map[String, ActorRef] = Map()
  def receive: Receive = {
    case team: Team =>
      val teamActor = context.actorOf(Props(classOf[TeamActor], team))
      teams += (team.name -> teamActor)
      sender ! team
  }
}

