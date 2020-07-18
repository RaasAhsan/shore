
val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := "2.13.3"
)

lazy val shore = project.in(file("."))
  .settings(commonSettings)
  .settings(
    name := "shore",
    libraryDependencies ++= Seq(
      "org.ow2.asm" % "asm" % "8.0.1"
    )
  )
