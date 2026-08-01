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

// Appends an extra packageOption so the next packageBin re-runs organically
// without any recompile, while the previous test run's classloader may still
// pin the old jar. A command (not a `set` expression) because parens break
// sbt.bat argument re-parsing and `set` cannot see build.sbt vals.
commands += Command.command("addManifestOption") { state =>
  Project
    .extract(state)
    .appendWithSession(
      Seq(Compile / packageBin / packageOptions += Pkg.JarManifest(new java.util.jar.Manifest)),
      state
    )
}
