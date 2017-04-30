package model.tradeSystem.decision

/**
  * Created by henke on 2017-04-29.
  */
trait DecisionService {
  def back(decision: Decision) = true
  def lay(decision: Decision) = false
}

// Concrete instance of the Service using the object keyword
object DecisionService extends DecisionService
