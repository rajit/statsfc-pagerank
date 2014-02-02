package api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import core.statsapi.StatsApiActor
import akka.util.Timeout
import spray.http.Uri
import statsfc.client.StatsApiJsonFormat
import statsfc.client.StatsApiDomain.{Result, Team}
import core.statsapi.StatsApiActor.{Results, Teams}

class TeamsService(statsApi: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with StatsApiJsonFormat {

  import akka.pattern.ask
  import scala.concurrent.duration._
  implicit val timeout = Timeout(20.seconds)

  val route = get {
    path("teams") {
      complete { (statsApi ? Teams()).mapTo[List[Team]] }
    } ~
    path("teams" / Segment) { competition =>
      complete { (statsApi ? Teams(competition)).mapTo[List[Team]] }
    } ~
    path("teams" / Segment / RestPath) { (competition, year: Uri.Path) =>
      complete { (statsApi ? Teams(competition, year.toString())).mapTo[List[Team]] }
    } ~
    path("results") {
      complete { (statsApi ? Results()).mapTo[List[Result]] }
    } ~
    path("results" / Segment) { competition =>
      complete { (statsApi ? Results(competition)).mapTo[List[Result]] }
    } ~
    path("results" / Segment / RestPath) { (competition, year: Uri.Path) =>
      complete { (statsApi ? Results(competition, year.toString())).mapTo[List[Result]] }
    }
  }
}
