enablePlugins(ScalaJSPlugin)

name := "Will Scala"
scalaVersion := "2.13.1"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

resolvers += "jitpack" at "https://jitpack.io"

updateOptions := updateOptions.value.withLatestSnapshots(false)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "1.0.0",
  "com.github.wbillingsley.veautiful" %%% "veautiful" % "master-SNAPSHOT",
  "com.github.wbillingsley.veautiful" %%% "veautiful-templates" % "master-SNAPSHOT",
	"com.github.wbillingsley.veautiful" %%% "scatter" % "master-SNAPSHOT",
	//"com.github.wbillingsley.veautiful" %%% "wren" % "master-SNAPSHOT"
)

val deployScript = taskKey[Unit]("Copies the fullOptJS script to deployscripts/")

// Used by Travis-CI to get the script out from the .gitignored target directory
// Don't run it locally, or you'll find the script gets loaded twice in index.html!
deployScript := {
  val opt = (Compile / fullOptJS).value
  IO.copyFile(opt.data, new java.io.File("deployscripts/compiled.js"))
}