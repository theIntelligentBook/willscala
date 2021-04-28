enablePlugins(ScalaJSPlugin)

name := "Will Scala"
scalaVersion := "3.0.0-RC1"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

resolvers += "jitpack" at "https://jitpack.io"

updateOptions := updateOptions.value.withLatestSnapshots(false)

libraryDependencies ++= Seq(
  "com.github.wbillingsley.veautiful" %%% "veautiful" % "v0.2-SNAPSHOT",
  "com.github.wbillingsley.veautiful" %%% "doctacular" % "v0.2-SNAPSHOT",
  
  "com.github.wbillingsley" % "lavamaze" % "v0.2-SNAPSHOT", // Need to single-% as it's a top-level jitpack project
)

val deployScript = taskKey[Unit]("Copies the fullOptJS script to deployscripts/")

// Used by Travis-CI to get the script out from the .gitignored target directory
// Don't run it locally, or you'll find the script gets loaded twice in index.html!
deployScript := {
  val opt = (Compile / fullOptJS).value
  IO.copyFile(opt.data, new java.io.File("deployscripts/compiled.js"))
}