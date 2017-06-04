import sbt._

object Dependencies {

  lazy val akkaVersion = "2.5.0"
  val nd4jVersion = "0.8.0"

  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion

  val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.5"
  val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.5"

  // Testing frameworks
  val scalactic = "org.scalactic" %% "scalactic" % "3.0.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  val scalaMock = "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0"

  // Not working, getting conflicting versions
  val gatling = "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.5"

  val cats = "org.typelevel" %% "cats" % "0.9.0"
  val monocleVersion = "1.4.0"
  val monocle = Seq(
    "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
    "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
    "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion
  )

  // Logging
  val logging = Seq(
    "ch.qos.logback"             %  "logback-classic" % "1.1.7",
    "com.typesafe.scala-logging" %% "scala-logging"   % "3.5.0"
  )

  //Scala version dl4j not avaliable atm in alpha
  val dl4j = "org.deeplearning4j" % "deeplearning4j-core" % "0.8.0"
  val nd4jPlatform = "org.nd4j" % "nd4j-native-platform" % nd4jVersion
  // Scala API for nd4j
  val nd4j = "org.nd4j" %% "nd4s" % nd4jVersion

  val circeVersion = "0.8.0"
  val circleJson = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)
  val jsonStream = List(
    "de.knutwalker" %% "akka-stream-circe" % "3.3.0",
    "de.knutwalker" %% "akka-http-circe" % "3.3.0"
  )
  val macWire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
  val macWireUtil = "com.softwaremill.macwire" %% "util" % "2.3.0" //For tagging support

  val commonDeps = Seq(scalaTest, macWireUtil, akkaHttpTestkit, scalaMock, macWire, akkaHttp, akkaStream, akkaStreamTestkit,cats) ++ monocle ++ logging ++ circleJson ++ jsonStream
}