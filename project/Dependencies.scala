import sbt._

object Dependencies {

  lazy val akkaVersion = "2.5.0"
  val nd4jVersion = "0.8.0"

  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion

  val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.5"

  // Testing frameworks
  val scalactic = "org.scalactic" %% "scalactic" % "3.0.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  val mockito = "org.mockito" % "mockito-all" % "1.10.19"


  // Not working, getting conflicting versions
  val gatling = "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.5"

  val scalaz = "org.scalaz" %% "scalaz-core" % "7.2.11"

  //Scala version dl4j not avaliable atm in alpha
  val dl4j = "org.deeplearning4j" % "deeplearning4j-core" % "0.8.0"
  val nd4jPlatform = "org.nd4j" % "nd4j-native-platform" % nd4jVersion
  // Scala API for nd4j
  val nd4j = "org.nd4j" %% "nd4s" % nd4jVersion

  val domainDeps = Seq(scalaTest, scalactic, mockito)
  val applicationDeps = Seq(dl4j)
  val infrastructureDeps = Seq(akkaHttp, akkaStream)
  val loadtestDeps = Seq()
  val componenttestDeps = Seq(scalaTest % Test, scalactic)
}