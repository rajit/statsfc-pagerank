package core.statsapi.graph

import statsfc.client.StatsApiDomain.Result
import akka.actor.{Actor, Props, ActorRef}
import akka.pattern.ask
import scala.concurrent.Future
import core.statsapi.graph.GameResult.{Team, Draw, AwayWon, HomeWon}
import core.statsapi.graph.TeamActor.{DrewAgainst, LostTo, WonAgainst}
import core.statsapi.graph.ResultAggregatorActor.Finished
import akka.util.Timeout
import scala.concurrent.duration._

object ResultActor {
  def props(result: Result, manager: ActorRef): Props =
    Props(classOf[ResultActor], manager, result)
}

class ResultActor(manager: ActorRef, result: Result) extends Actor {
  import context.dispatcher

  implicit val _ = Timeout(5 seconds)

  def getTeam(team: Team): Future[ActorRef] = (manager ? team).mapTo[ActorRef]

  def receive: Receive = {
    case result: Result =>
      val enquirer = sender
      val game = new GameResult(result)
      for {
        hometeam <- getTeam(game.home)
        awayteam <- getTeam(game.away)
      } yield {
        game.outcome match {
          case HomeWon =>
            hometeam ! WonAgainst(awayteam)
            awayteam ! LostTo(hometeam)
          case AwayWon =>
            hometeam ! LostTo(awayteam)
            awayteam ! WonAgainst(hometeam)
          case Draw =>
            hometeam ! DrewAgainst(awayteam)
            awayteam ! DrewAgainst(hometeam)
        }

        enquirer ! Finished(result)
      }
      context.stop(self)
    case msg => throw new RuntimeException(s"Unhandled message: $msg")
  }
}
