package com.bfg.domain.model

import java.time.Instant

/**
  * Created by henke on 2017-05-12.
  */
sealed trait BetfairEvent {
  /* The operation type */
  def op: ResponseType
  /* Client generated unique id to link request with response (like json rpc) */
}

case class ConnectionMessage (
                               op: ResponseType,
                               //id: Int,
                               /* The connection id */
                               connectionId: String
                             ) extends BetfairEvent

case class StatusMessage (
                           op: ResponseType,
                           id: Option[Int],
                           /* Additional message in case of a failure */
                           errorMessage: Option[String],
                           /* The type of error in case of a failure */
                           errorCode: Option[String],
                           /* The connection id */
                           connectionId: Option[String],
                           /* Is the connection now closed */
                           connectionClosed: Boolean,
                           /* The status of the last request */
                           statusCode: String
                         ) extends BetfairEvent

case class MarketChangeMessage (
                                 /* The operation type */
                                 op: String,
                                 /* Client generated unique id to link request with response (like json rpc) */
                                 id: Int,
                                 /* Change Type - set to indicate the type of change - if null this is a delta) */
                                 ct: Option[String],
                                 /* Token value (non-null) should be stored and passed in a MarketSubscriptionMessage to resume subscription (in case of disconnect) */
                                 clk: String,
                                 /* Heartbeat Milliseconds - the heartbeat rate (may differ from requested: bounds are 500 to 30000) */
                                 heartbeatMs: Option[Long],
                                 /* Publish Time (in millis since epoch) that the changes were generated */
                                 pt: Long,
                                 /* Token value (non-null) should be stored and passed in a MarketSubscriptionMessage to resume subscription (in case of disconnect) */
                                 initialClk: Option[String],
                                 /* MarketChanges - the modifications to markets (will be null on a heartbeat */
                                 mc: Option[List[MarketChange]],
                                 /* Conflate Milliseconds - the conflation rate (may differ from that requested if subscription is delayed) */
                                 conflateMs: Option[Long],
                                 /* Segment Type - if the change is split into multiple segments, this denotes the beginning and end of a change, and segments in between. Will be null if data is not segmented */
                                 segmentType: Option[String]
                               ) extends BetfairEvent

case class MarketChange (
                          /* Runner Changes - a list of changes to runners (or null if un-changed) */
                          rc: Option[List[RunnerChange]],
                          /* Image - replace existing prices / data with the data supplied: it is not a delta (or null if delta) */
                          img: Option[Boolean],
                          /* The total amount matched across the market. This value is truncated at 2dp (or null if un-changed) */
                          tv: Option[Double],
                          /* Conflated - have more than a single change been combined (or null if not conflated) */
                          con: Option[Boolean],
                          marketDefinition: Option[MarketDefinition],
                          /* Market Id - the id of the market */
                          id: String
                        )
object MarketChange {
  val empty = MarketChange(None, None, None, None, None, "")
}

case class RunnerChange (
                          /* The total amount matched. This value is truncated at 2dp. */
                          tv: Option[Double] = None,
                          /* Best Available To Back - LevelPriceVol triple delta of price changes, keyed by level (0 vol is remove) */
                          batb: Option[List[LevelPriceVolume]] = None,
                          /* Starting Price Back - PriceVol tuple delta of price changes (0 vol is remove) */
                          spb: Option[List[PriceVolume]] = None,
                          /* Best Display Available To Lay (includes virtual prices)- LevelPriceVol triple delta of price changes, keyed by level (0 vol is remove) */
                          bdatl: Option[List[LevelPriceVolume]] = None,
                          /* Traded - PriceVol tuple delta of price changes (0 vol is remove) */
                          trd: Option[List[PriceVolume]] = None,
                          /* Starting Price Far - The far starting price (or null if un-changed) */
                          spf: Option[Double] = None,
                          /* Last Traded Price - The last traded price (or null if un-changed) */
                          ltp: Option[Double] = None,
                          /* Available To Back - PriceVol tuple delta of price changes (0 vol is remove) */
                          atb: Option[List[PriceVolume]] = None,
                          /* Starting Price Lay - PriceVol tuple delta of price changes (0 vol is remove) */
                          spl: Option[List[PriceVolume]] = None,
                          /* Starting Price Near - The far starting price (or null if un-changed) */
                          spn: Option[Double] = None,
                          /* Available To Lay - PriceVol tuple delta of price changes (0 vol is remove) */
                          atl: Option[List[PriceVolume]] = None,
                          /* Best Available To Lay - LevelPriceVol triple delta of price changes, keyed by level (0 vol is remove) */
                          batl: Option[List[LevelPriceVolume]] = None,
                          /* Selection Id - the id of the runner (selection) */
                          id: Option[Long] = None,
                          /* Handicap - the handicap of the runner (selection) (null if not applicable) */
                          hc: Option[Double] = None,
                          /* Best Display Available To Back (includes virtual prices)- LevelPriceVol triple delta of price changes, keyed by level (0 vol is remove) */
                          bdatb: Option[List[LevelPriceVolume]] = None
                        )
object RunnerChange {
  val empty = RunnerChange()
}

case class MarketDefinition (
                              venue: Option[String],
                              settledTime: Option[Instant],
                              timezone: Option[String],
                              eachWayDivisor: Option[Double],
                              /* The market regulators. */
                              regulators: Option[List[String]],
                              marketType: Option[String],
                              marketBaseRate: Option[Double],
                              numberOfWinners: Option[Int],
                              countryCode: Option[String],
                              /* For Line markets, maximum value for the outcome, in market units for this market (eg 100 runs). */
                              lineMaxUnit: Option[Double],
                              inPlay: Option[Boolean],
                              betDelay: Option[Int],
                              bspMarket: Option[Boolean],
                              bettingType: Option[String],
                              numberOfActiveRunners: Option[Int],
                              /* For Line markets, minimum value for the outcome, in market units for this market (eg 0 runs). */
                              lineMinUnit: Option[Double],
                              eventId: Option[String],
                              crossMatching: Option[Boolean],
                              runnersVoidable: Option[Boolean],
                              turnInPlayEnabled: Option[Boolean],
                              suspendTime: Option[Instant],
                              discountAllowed: Option[Boolean],
                              persistenceEnabled: Option[Boolean],
                              runners: Option[List[RunnerDefinition]],
                              version: Option[Long],
                              /* The Event Type the market is contained within. */
                              eventTypeId: Option[String],
                              complete: Option[Boolean],
                              openDate: Option[Instant],
                              marketTime: Option[Instant],
                              bspReconciled: Option[Boolean],
                              /* For Line markets, the odds ladder on this market will be between the range of lineMinUnit and lineMaxUnit, in increments of the interval value.e.g. If lineMinUnit=10 runs, lineMaxUnit=20 runs, lineInterval=0.5 runs, then valid odds include 10, 10.5, 11, 11.5 up to 20 runs. */
                              lineInterval: Option[Double],
                              status: Option[String]
                            )

case class RunnerDefinition (
                              sortPriority: Option[Int],
                              removalDate: Option[Instant],
                              /* Selection Id - the id of the runner (selection) */
                              id: Option[Long],
                              /* Handicap - the handicap of the runner (selection) (null if not applicable) */
                              hc: Option[Double],
                              adjustmentFactor: Option[Double],
                              bsp: Option[Double],
                              status: Option[String]
                            )
