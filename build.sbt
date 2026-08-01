scalaVersion := "3.8.4"

lazy val root = rootProject
  .settings(
    name := "sbt_2_access_denied_exception")

// Appends a comment to the main source so that the next compile/packageBin
// legitimately re-runs while the previous test run's classloader may still
// hold the old jar open. Used by the CI repro workflows:
//   sbt --batch ";compile;test;mutate;test"
commands += Command.command("mutate") { state =>
  IO.append(file("src/main/scala/Test.scala"), "\n// mutated\n")
  state
}
