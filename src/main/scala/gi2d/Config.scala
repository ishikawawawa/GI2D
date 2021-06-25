package gi2d

import scopt.OParser

import java.io.File

case class Config(
    directory: File = new File("."),
    line: Int = 10,
    borderWeight: Int = 4,
    margin: Int = 16,
    cellSize: Int = 128,
    output: String = "output.png"
)

object Config {
  val builder = OParser.builder[Config]

  val parser = {
    import builder._

    OParser.sequence(
      programName("GI2D"),
      opt[File]('d', "dir")
        .required()
        .action((file, c) => c.copy(directory = file)),
      opt[Int]('l', "line")
        .action((line, c) => c.copy(line = line)),
      opt[Int]("border-weight")
        .action((borderWeight, c) => c.copy(borderWeight = borderWeight)),
      opt[Int]("margin")
        .action((margin, c) => c.copy(margin = margin)),
      opt[Int]("cell-size")
        .action((cellSize, c) => c.copy(cellSize = cellSize)),
      opt[String]('o', "output")
        .action((output, c) => c.copy(output = output))
    )
  }
}