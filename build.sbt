import Dependencies._


lazy val commonSettings = Seq(
  organization := "com.bfg",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.2"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "bfg6"
  )
  .aggregate(domain, application, infrastructure)

lazy val domain = project
  .settings(
    commonSettings,
    libraryDependencies ++= domainDeps
  )

lazy val application = project
  .settings(
    commonSettings,
    libraryDependencies ++= applicationDeps
  )
  .dependsOn(domain, infrastructure)

lazy val infrastructure = project
  .settings(
    commonSettings,
    libraryDependencies ++= infrastructureDeps
  )
  .dependsOn(domain)

lazy val componenttest = project
  .settings(
    commonSettings,
    libraryDependencies ++= componenttestDeps
  )
  .dependsOn(application)

lazy val loadtest = project
  .settings(
    commonSettings,
    libraryDependencies ++= loadtestDeps
  )
  .dependsOn(application)


