package core.statsapi

import akka.actor.Actor
import scala.concurrent.Future
import akka.pattern.pipe
import spray.can.Http
import akka.io.IO
import akka.pattern.ask
import Competition.Competition
import spray.httpx.RequestBuilding.Get
import akka.util.Timeout
import statsfc.client._
import statsfc.client.StatsApiDomain.Result
import spray.http.HttpResponse
import StatsApiActor.Results
import StatsApiActor.Teams
import statsfc.client.StatsApiDomain.Team
import statsfc.client.uri.{ApiKey, StatsApiUri, TeamsStatsApiUri, ResultsStatsApiUri}

object StatsApiActor {
  case class Teams(competition: String = "premier-league", year: String = "2013/2014")
  case class Results(competition: String = "premier-league", year: String = "2013/2014")
}

class StatsApiActor(key: ApiKey) extends Actor {
  import scala.concurrent.duration._
  import context.dispatcher
  import spray.json._
  import StatsApiJsonFormat._

  implicit val timeout = Timeout(20.seconds)
  implicit val system = context.system
  implicit val apiKey = key

  implicit def stringToCompetition(s: String): Competition = Competition.withName(s)

  def receive: Receive = {
    case Teams(competition, year)
      => apiCall(TeamsStatsApiUri(competition, year)) map { _.convertTo[List[Team]]} pipeTo sender
    case Results(competition, year)
      => apiCall(ResultsStatsApiUri(competition, year)) map { _.convertTo[List[Result]]} pipeTo sender
  }

  def apiCall(uri: StatsApiUri): Future[JsValue] = {
    for {
      response <- IO(Http).ask(Get(uri.toString)).mapTo[HttpResponse]
    } yield response.entity.asString.replaceAll("null", "\"\"").asJson
  }
}
