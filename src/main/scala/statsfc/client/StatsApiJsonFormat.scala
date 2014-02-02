package statsfc.client

import api.DefaultJsonFormats
import org.joda.time.format.DateTimeFormat
import statsfc.client.StatsApiDomain.{Result, Incident, Team}
import spray.json._

object StatsApiJsonFormat extends StatsApiJsonFormat

trait StatsApiJsonFormat extends DefaultJsonFormats {
  val isoFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  implicit val teamFormat = jsonFormat4(Team)
  implicit val incidentFormat = jsonFormat11(Incident)

  implicit object ResultJsonFormat extends RootJsonFormat[Result] {
    def write(r: Result) = JsObject(
      "id" -> JsNumber(r.id),
      "date" -> JsString(isoFormatter.print(r.date)),
      "competition_id" -> JsNumber(r.competition_id),
      "competition" -> JsString(r.competition),
      "group" -> JsString(r.group),
      "home_id" -> JsNumber(r.home_id),
      "home" -> JsString(r.home),
      "homeshort" -> JsString(r.homeshort),
      "homepath" -> JsString(r.homepath),
      "away_id" -> JsNumber(r.away_id),
      "away" -> JsString(r.away),
      "awayshort" -> JsString(r.awayshort),
      "awaypath" -> JsString(r.awaypath),
      "status" -> JsString(r.status),
      "halftime" -> JsArray(r.halftime.toJson),
      "extratime" -> JsArray(r.extratime.toJson),
      "fulltime" -> JsArray(r.fulltime.toJson),
      "penalties" -> JsArray(r.penalties.toJson),
      "incidents" -> JsArray(r.incidents.toJson))

    def read(value: JsValue) = value.asJsObject.getFields("id", "date", "competition_id", "competition", "group",
      "home_id", "home", "homeshort", "homepath", "away_id", "away", "awayshort", "awaypath", "status", "halftime",
      "extratime", "fulltime", "penalties", "incidents") match {
      case Seq(JsNumber(id), JsString(date), JsNumber(competition_id), JsString(competition),
      JsString(group), JsNumber(home_id), JsString(home), JsString(homeshort), JsString(homepath),
      JsNumber(away_id), JsString(away), JsString(awayshort), JsString(awaypath), JsString(status),
      halftime, extratime, fulltime, penalties, incidents)
      => Result(id.toInt, isoFormatter.parseDateTime(date), competition_id.toInt, competition, group, home_id.toInt,
        home, homeshort, homepath, away_id.toInt, away, awayshort, awaypath, status,
        halftime.convertTo[List[Int]], extratime.convertTo[List[Int]], fulltime.convertTo[List[Int]],
        penalties.convertTo[List[Int]], incidents.convertTo[List[Incident]])
      case json => deserializationError(s"Result expected: $json")
    }
  }
}
