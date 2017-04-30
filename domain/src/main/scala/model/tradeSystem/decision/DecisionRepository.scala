package model.tradeSystem.decision

/**
  * Created by henke on 2017-04-29.
  */
trait DecisionRepository {
  """
    | What an aggregate offer for in memory storage
    | a repository do the same for persistent storage.
  """.stripMargin
  def query(decisionId: String): Option[Decision]
  def write(decision: Decision): Boolean
  def delete(decision: Decision): Boolean
}
