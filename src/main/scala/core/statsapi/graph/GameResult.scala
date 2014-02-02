package core.statsapi.graph

import core.statsapi.graph.GameResult._
import statsfc.client.StatsApiDomain.Result
import core.statsapi.graph.GameResult.Team

object GameResult {
  trait MatchResult
  case object HomeWon extends MatchResult
  case object AwayWon extends MatchResult
  case object Draw extends MatchResult
  case class Team(name: String)
}

class GameResult(private val result: Result) {
  val home: Team = new Team(result.homepath)
  val away: Team = new Team(result.awaypath)
  val outcome: MatchResult = result.fulltime match {
    case h :: a :: Nil =>
      if (h > a) HomeWon
      else if (a > h) AwayWon
      else Draw
    case _ => throw new RuntimeException("Malformed goals list")
  }
}
