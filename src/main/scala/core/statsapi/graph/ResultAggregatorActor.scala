package core.statsapi.graph

import akka.actor.{ActorRef, Props, Actor}
import statsfc.client.StatsApiDomain.Result
import akka.pattern.ask
import scala.concurrent.Future
import scala.collection.mutable.ListBuffer
import core.statsapi.graph.TeamActor.{DrewAgainst, LostTo, WonAgainst}
import core.statsapi.graph.ResultAggregatorActor.{Finished, GraphStarted}
import core.statsapi.graph.GameResult._
import statsfc.client.StatsApiDomain.Result
import core.statsapi.graph.TeamActor.LostTo
import core.statsapi.graph.TeamActor.DrewAgainst
import core.statsapi.graph.ResultAggregatorActor.Finished
import core.statsapi.graph.TeamActor.WonAgainst

object ResultAggregatorActor {
  case object GraphStarted
  case class Finished(result: Result)
}

class ResultAggregatorActor extends Actor {
  val manager = context.actorOf(Props[TeamManager])
  var finished = 0
  var total = 0

  def receive: Receive = startGraph

  def startGraph: Receive = {
    case results: List[Result] =>
      finished = 0
      total = results.size
      results map { r: Result => context.actorOf(ResultActor.props(r, manager))}
      sender ! GraphStarted
      context.become(waitForGraph)
  }

  def waitForGraph: Receive = {
    // TODO: Timeout if it takes too long?
    case results: List[Result] => sender ! GraphStarted
    case Finished(result) =>
      finished = finished + 1
      if (finished == total) {
        context.system.eventStream.publish _
        context.become(graphReady)
      }
  }

  def graphReady: Receive = {
    case msg => throw new RuntimeException(s"Not implemented! ($msg)")
  }
}
