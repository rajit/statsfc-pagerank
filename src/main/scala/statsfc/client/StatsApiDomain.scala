package statsfc.client

/**
 * Created by Rajit Singh on 21/12/13.
 */
object StatsApiDomain {
  import com.github.nscala_time.time.Imports._
  case class Team(id: Int, name: String, nameshort: String, path: String)
  case class Incident(id: Int, `type`: String, goaltype: String, team_id: Int, team: String, teamshort: String,
                      teampath: String, player_id: Int, player: String, playershort: String, minute: Int)
  case class Result(id: Int, date: DateTime, competition_id: Int, competition: String, group: String,
                    home_id: Int, home: String, homeshort: String, homepath: String, away_id: Int,
                    away: String, awayshort: String, awaypath: String, status: String, halftime: List[Int],
                    extratime: List[Int], fulltime: List[Int], penalties: List[Int], incidents: List[Incident])
}
