package gi2d

import scopt.OParser

import java.io.File

case class Config(
    directory: File = new File(".")
)

object Config {
  val builder = OParser.builder[Config]

  val parser = {
    import builder._

    OParser.sequence(
      programName("GI2D"),
      opt[File]('d', "dir")
        .required()
        .action((f, c) => c.copy(directory = f))
    )
  }
}