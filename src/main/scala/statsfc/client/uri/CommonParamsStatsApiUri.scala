package statsfc.client.uri

import core.statsapi.Competition

trait CommonParamsStatsApiUri extends StatsApiUri {
  def competition = Competition.PremierLeague
  def year = "2013/2014"
  uri = uri + s"&competition=$competition&year=$year"
}
