import java.io.File

import Dependencies._

val scalaV = "2.12.2"

name := "bfg6"
// Needed or else root is defaulted to 2.10
scalaVersion := scalaV

lazy val commonSettings = Seq(
  version := "0.1.SNAPSHOT",
  organization := "com.bfg",
  scalaVersion := scalaV
)
enablePlugins(DockerComposePlugin)

docker <<= (docker in bfg) map {(image) => image}

//Set the image creation Task to be the one used by sbt-docker
dockerImageCreationTask := docker.value

lazy val bfg = project
  .settings(
    name := "bfg",
    Defaults.itSettings,
    commonSettings,
    libraryDependencies ++= commonDeps,
    libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0" % "it",
    // Used to run dockerComposeUp from within subproject
    composeFile :=  s"${baseDirectory.value.absolutePath}/../docker/docker-compose.yml",
    dockerImageCreationTask := docker.value,
    //---
    //To use 'dockerComposeTest' to run tests in the 'IntegrationTest' scope instead of the default 'Test' scope:
    // 1) Package the tests that exist in the IntegrationTest scope
    testCasesPackageTask := (sbt.Keys.packageBin in IntegrationTest).value,
    // 2) Specify the path to the IntegrationTest jar produced in Step 1
    testCasesJar := artifactPath.in(IntegrationTest, packageBin).value.getAbsolutePath,
    // 3) Include any IntegrationTest scoped resources on the classpath if they are used in the tests
    testDependenciesClasspath := {
      val fullClasspathCompile = (fullClasspath in Compile).value
      val classpathTestManaged = (managedClasspath in IntegrationTest).value
      val classpathTestUnmanaged = (unmanagedClasspath in IntegrationTest).value
      val testResources = (resources in IntegrationTest).value
      (fullClasspathCompile.files ++ classpathTestManaged.files ++ classpathTestUnmanaged.files ++ testResources).map(_.getAbsoluteFile).mkString(File.pathSeparator)
    },
    dockerfile in docker := {
        new Dockerfile {
        val dockerAppPath = "/app/"
        val mainClassString = (mainClass in Compile).value.get
        val classpath = (fullClasspath in Compile).value
        from("java")
        add(classpath.files, dockerAppPath)
        entryPoint("java", "-cp", s"$dockerAppPath:$dockerAppPath/*", s"$mainClassString")
      }
    },
    imageNames in docker := Seq(ImageName(
      repository = name.value.toLowerCase,
      tag = Some(version.value))
    )
  )
  .configs(IntegrationTest)
  .enablePlugins(DockerPlugin, DockerComposePlugin)

