package gi2d

import java.awt.{ Color, Font }
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Main extends App {
  val cellSize = 128
  val column = 11
  val row = 12
  val borderWeight = 4
  val margin = 16

  // (セルサイズ * 列数) + (左右のマージン) + (線の太さ * (列数 + 1))
  val width = (cellSize * column) + (margin * 2) + (borderWeight * (column + 1))
  // (セルサイズ * 行数) + (上下のマージン) + (線の太さ * (行数 + 1))
  val height = (cellSize * row) + (margin * 2) + (borderWeight * (row + 1))

  val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
  val graphics = image.getGraphics

  // 背景色
  graphics.setColor(Color.white)
  graphics.fillRect(0, 0, width, height)

  // 枠線
  graphics.setColor(Color.black)

  // 縦線
  {
    val borderLength = (cellSize * row) + (borderWeight * (row + 1))

    (0 to column).foreach { n =>
      val topLeftX = image.getMinX + margin
      val offset = (cellSize + borderWeight) * n
      val x = topLeftX + offset
      val y = image.getMinY + margin

      graphics.fillRect(x, y, borderWeight, borderLength)
    }
  }

  // 横線
  {
    val borderLength = (cellSize * column) + (borderWeight * (column + 1))

    (0 to row).foreach { n =>
      val topLeftY = image.getMinY + margin
      val offset = (cellSize + borderWeight) * n
      val x = image.getMinX + margin
      val y = topLeftY + offset

      graphics.fillRect(x, y, borderLength, borderWeight)
    }
  }

  // 番号
  {
    val fontSize = 72
    val font = new Font("Arial", Font.BOLD, fontSize);
    val fontMetrics = graphics.getFontMetrics(font)
    val textHeight = fontMetrics.getHeight
    val textAscent = fontMetrics.getAscent
    graphics.setFont(font)

    // 1の位
    (0 to column - 2).foreach { n =>
      val text = s"x${n}"
      val textWidth = fontMetrics.stringWidth(text)

      val topLeftX = image.getMinX + margin + (cellSize * (n + 1)) + (borderWeight * (n + 2))
      val topLeftY = image.getMinY + margin + borderWeight
      val x = topLeftX + (cellSize / 2) - (textWidth / 2)
      val y = topLeftY + (cellSize / 2) - (textHeight / 2) + textAscent

      graphics.drawChars(text.toCharArray, 0, text.length, x, y)
    }

    // 10の位
    (0 to row - 2).foreach { n =>
      val text = s"${n}x"
      val textWidth = fontMetrics.stringWidth(text)

      val topLeftX = image.getMinX + margin + borderWeight
      val topLeftY = image.getMinY + margin + (cellSize * (n + 1)) + (borderWeight * (n + 2))
      val x = topLeftX + (cellSize / 2) - (textWidth / 2)
      val y = topLeftY + (cellSize / 2) - (textHeight / 2) + textAscent

      graphics.drawChars(text.toCharArray, 0, text.length, x, y)
    }
  }

  graphics.dispose()

  val file = new File("example.png")
  ImageIO.write(image, "png", file)
}