package statsfc.client.uri

import core.statsapi.Competition.Competition

case class TeamsStatsApiUri(override val competition: Competition, override val year: String)(implicit val key: ApiKey) extends CommonParamsStatsApiUri {
  override def apiKey = key
  override lazy val jsonCall = "teams.json"
}
