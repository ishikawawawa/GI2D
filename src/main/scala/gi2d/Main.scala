package gi2d

import scopt.OParser

import java.awt.{ Color, Font }
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Main extends App {
  OParser.parse(Config.parser, args, Config()) match {
    case Some(config) => {
      val cellSize = 128
      val columnNum = 11
      val rowNum = 12
      val borderWeight = 4
      val margin = 16

      // (セルサイズ * 列数) + (左右のマージン) + (線の太さ * (列数 + 1))
      val width = (cellSize * columnNum) + (margin * 2) + (borderWeight * (columnNum + 1))
      // (セルサイズ * 行数) + (上下のマージン) + (線の太さ * (行数 + 1))
      val height = (cellSize * rowNum) + (margin * 2) + (borderWeight * (rowNum + 1))

      val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
      val graphics = image.getGraphics

/***
       * n個目のマスの左上の座標を求める
       * @param n 0始まり
       * @return (x座標, y座標)
       */
      def topLeftPoint(n: Int): (Int, Int) = {
        // n列目
        val column = n % columnNum
        // n行目
        val row = n / columnNum

        // ボーダーの本数
        val xBorderNum = column + 1
        val yBorderNum = row + 1

        // 0 + マージン + (ボーダーの太さ * ボーダーの本数) + (セルサイズ * 列数)
        val x = image.getMinX + margin + (borderWeight * xBorderNum) + (cellSize * column)
        // 0 + マージン + (ボーダーの太さ * ボーダーの本数) + (セルサイズ * 行数)
        val y = image.getMinY + margin + (borderWeight * yBorderNum) + (cellSize * row)

        (x, y)
      }

      // 背景色
      graphics.setColor(Color.white)
      graphics.fillRect(0, 0, width, height)

      // 枠線
      graphics.setColor(Color.black)

      // 縦線
      {
        val borderLength = (cellSize * rowNum) + (borderWeight * (rowNum + 1))

        (0 to columnNum).foreach { n =>
          val topLeftX = image.getMinX + margin
          val offset = (cellSize + borderWeight) * n
          val x = topLeftX + offset
          val y = image.getMinY + margin

          graphics.fillRect(x, y, borderWeight, borderLength)
        }
      }

      // 横線
      {
        val borderLength = (cellSize * columnNum) + (borderWeight * (columnNum + 1))

        (0 to rowNum).foreach { n =>
          val topLeftY = image.getMinY + margin
          val offset = (cellSize + borderWeight) * n
          val x = image.getMinX + margin
          val y = topLeftY + offset

          graphics.fillRect(x, y, borderLength, borderWeight)
        }
      }

      // 番号
      {
        // todo 文字のセンタリングが微妙にうまくいっていないため直す
        val fontSize = 72
        val font = new Font("Arial", Font.BOLD, fontSize);
        val fontMetrics = graphics.getFontMetrics(font)
        val textHeight = fontMetrics.getHeight
        val textAscent = fontMetrics.getAscent
        graphics.setFont(font)

        // 1の位
        (0 to columnNum - 2).foreach { n =>
          val text = s"x${n}"
          val textWidth = fontMetrics.stringWidth(text)

          val (topLeftX, topLeftY) = topLeftPoint(n + 1)
          val x = topLeftX + (cellSize / 2) - (textWidth / 2)
          val y = topLeftY + (cellSize / 2) - (textHeight / 2) + textAscent
          graphics.drawChars(text.toCharArray, 0, text.length, x, y)
        }

        // 10の位
        (0 to rowNum - 2).foreach { n =>
          val text = s"${n}x"
          val textWidth = fontMetrics.stringWidth(text)

          val (topLeftX, topLeftY) = topLeftPoint((n + 1) * columnNum)
          val x = topLeftX + (cellSize / 2) - (textWidth / 2)
          val y = topLeftY + (cellSize / 2) - (textHeight / 2) + textAscent

          graphics.drawChars(text.toCharArray, 0, text.length, x, y)
        }
      }

      config.directory.listFiles.foreach { file =>

        val numberText :: _ = file.getName.split("_", 2).toList

        numberText.toIntOption match {
          case Some(number) => {
            // 列
            val column = (number % (columnNum - 1)) + 1
            // 行
            val row = (number / (columnNum - 1)) + 1
            val n = (columnNum * row) + column
            val (x, y) = topLeftPoint(n)

            graphics.drawImage(ImageIO.read(file), x, y, null)
          }
          case _ => {
            // todo ファイル名がおかしい旨をログに出力
          }
        }
      }

      val file = new File("example.png")
      ImageIO.write(image, "png", file)
    }
    case _ => {
      // todo オプションの読み込みに失敗した旨をログに出力
    }
  }
}