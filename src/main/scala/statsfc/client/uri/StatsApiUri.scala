package statsfc.client.uri

import spray.http.Uri

case class ApiKey(key: String)

trait StatsApiUri {
  def apiKey: ApiKey
  lazy val jsonCall = "competitions.json"
  lazy val uri = Uri(s"http://api.statsfc.com/$jsonCall?key=${apiKey.key}")
  override def toString: String = uri.toString()
}
