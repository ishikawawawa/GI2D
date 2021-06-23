package gi2d

import java.awt.{ Color, Font }
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Main extends App {
  // todo 線の太さを考慮
  // todo 縦マスの数を可変にする
  // todo マスの大きさを可変にする

  // 1マスの大きさ 128x128
  // 縦11枚・横11枚
  // 1408 x 1408
  // 上下左右に16pxマージン
  // 1440 x 1440
  val width = 1440
  val height = 1440
  val margin = 16

  val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
  val graphics = image.getGraphics

  // 背景色
  graphics.setColor(Color.white)
  graphics.fillRect(0, 0, width, height)

  // 縦線
  {
    graphics.setColor(Color.black)
    val startX = 0 + margin
    val endX = width - margin
    val startY = 0 + margin
    val endY = height - margin

    // 縦
    (startX to endX by 128).foreach { x =>
      graphics.drawLine(x, startY, x, endY)
    }

    // 横
    (startY to endY by 128).foreach { y =>
      graphics.drawLine(startX, y, endX, y)
    }
  }

  // 番号
  {
    val fontSize = 96
    val font = new Font("Arial", Font.BOLD, fontSize);
    val fontMetrics = graphics.getFontMetrics(font)
    graphics.setFont(font)

    val onesPlace = (0 to 9).map(n => (s"x${n}", n))
    onesPlace.foreach {
      case (char, n) =>
        val strWidth = fontMetrics.stringWidth(char)
        val strHeight = fontMetrics.getHeight
        val ascent = fontMetrics.getAscent

        val offsetX = 128 * (n + 1)
        // (0 + マージン) + 1枚の中心X座標 - (文字列の幅 / 2) + n枚目の始点X
        val x = (image.getMinX + margin) + (128 / 2) - (strWidth / 2) + offsetX
        val y = (image.getMinY + margin) + (128 / 2) - (strHeight / 2) + ascent

        graphics.drawChars(char.toCharArray, 0, char.length, x, y)
    }

    val tensPlace = (0 to 9).map(n => (s"${n}x", n))
    tensPlace.foreach {
      case (char, n) =>
        val strWidth = fontMetrics.stringWidth(char)
        val strHeight = fontMetrics.getHeight
        val ascent = fontMetrics.getAscent

        val offsetY = 128 * (n + 1)
        val x = (image.getMinX + margin) + (128 / 2) - (strWidth / 2)
        val y = (image.getMinY + margin) + (128 / 2) - (strHeight / 2) + ascent + offsetY

        graphics.drawChars(char.toCharArray, 0, char.length, x, y)
    }
  }

  graphics.dispose()

  val file = new File("example.png")
  ImageIO.write(image, "png", file)
}