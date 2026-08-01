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

// Referenced from the CI workflows via `set Compile / packageBin / packageOptions += reproManifestOption`.
// A bare val so the set expression needs no parentheses: parens inside the
// quoted command string break sbt.bat's internal argument re-parsing on Windows.
val reproManifestOption = Pkg.JarManifest(new java.util.jar.Manifest)
