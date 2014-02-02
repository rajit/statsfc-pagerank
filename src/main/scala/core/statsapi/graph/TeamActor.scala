package core.statsapi.graph

import akka.actor.{Actor, ActorRef}
import core.statsapi.graph.GameResult.Team
import scala.collection.mutable.ListBuffer
import core.statsapi.graph.TeamActor.{StartPageRank, DrewAgainst, LostTo, WonAgainst}

object TeamActor {
  case class WonAgainst(team: ActorRef)
  case class LostTo(team: ActorRef)
  case class DrewAgainst(team: ActorRef)
  case object StartPageRank
}

class TeamActor(team: Team) extends Actor {
  var rank = 1
  var wonAgainst: ListBuffer[ActorRef] = ListBuffer()
  var lostTo: ListBuffer[ActorRef] = ListBuffer()
  var drewAgainst: ListBuffer[ActorRef] = ListBuffer()
  def receive: Receive = buildGraph

  def buildGraph: Receive = {
    case WonAgainst(teamActor: ActorRef) => wonAgainst += teamActor
    case LostTo(teamActor: ActorRef) => lostTo += teamActor
    case DrewAgainst(teamActor: ActorRef) => drewAgainst += teamActor
    case StartPageRank => context.become(pageRanking)
  }

  def pageRanking: Receive = {
    case msg => throw new RuntimeException(s"Not implemented! ($msg)")
  }
}
