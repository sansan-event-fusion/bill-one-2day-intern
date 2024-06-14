import { readFileSync } from "fs"
import { jsPDF, jsPDFOptions } from "jspdf"
import autoTable from "jspdf-autotable"
import { splitStringByMaxWidth } from "../Util"
import { BILL_ONE_COLOR } from "./color"
import { RenderTableArgument } from "./table"
import {
  FONT_FAMILY,
  FONT_SIZE,
  RenderTextBackground,
  RenderTextPosition,
  RenderTextTextData,
  RenderTextTextDataOnBackground,
} from "./text"

export type PdfGeneratorOptions = Pick<jsPDFOptions, "orientation" | "unit" | "format">

export type FileExtension = "jpeg" | "jpg" | "png"

/**
 * PDfGenerator はPDFを操作するAPIを提供する
 * PDFを作成するライブラリをこの中に閉じ込めることにより、他のライブラリに移行しやすくする
 */
export class PdfGenerator {
  doc: jsPDF
  fileName: string

  public renderImage(
    position: { startX: number; startY: number },
    file: { img: Buffer; extension: FileExtension; width: number; height: number },
  ) {
    this.doc.addImage(file.img, file.extension, position.startX, position.startY, file.width, file.height)
  }

  public text({
    position: { startX, startY },
    text: {
      text,
      fontStyle = "normal",
      textAlign = "LEFT",
      fontSize = FONT_SIZE.NORMAL,
      textColor = BILL_ONE_COLOR.BLACK,
      maxWidth = this.getPdfWidth(),
    },
  }: {
    position: RenderTextPosition
    text: RenderTextTextData
  }): {
    endY: number
  } {
    const textArray = (() => {
      const arr = Array.isArray(text) ? text : [text]
      return arr.map((txt) => splitStringByMaxWidth(txt, fontSize, maxWidth)).flat()
    })()

    const textNumberOfLine = textArray.length
    const lineSpaceHeight = textNumberOfLine === 1 ? 0 : 1.5 * textNumberOfLine
    const textHeight = fontSize * textNumberOfLine + lineSpaceHeight

    this.doc.setTextColor(textColor.r, textColor.g, textColor.b)
    this.doc.setFontSize(fontSize)
    this.doc.setFont(FONT_FAMILY.GEN_EI_MONO_GOTHIC, fontStyle)

    switch (textAlign) {
      case "LEFT":
        this.doc.text(textArray, startX, startY, {
          baseline: "top", // 左上から計算していくので、文字の配置も文字の上を起点にする
        })
        break
      case "RIGHT":
        this.doc.text(textArray, this.getPdfWidth() + -1 * startX, startY, {
          baseline: "top",
          align: "right",
        })
        break
    }

    return {
      endY: startY + textHeight,
    }
  }

  /**
   * PDFに文字列を表示し、文字の背景色に色をつけたり、文字の周りの線で囲ったりすることができる。
   *
   * 特定のサイズで自動改行などはまだできない
   *
   * @param
   * - position 文字を表示する初期位置を決める
   *   - startX PDFの左からの距離
   *   - startY PDFの上からの距離
   * - text 表示する文字に関する情報を決める
   *   - text 表示したい文字。意図したところで改行したい場合は配列で渡す
   *   - fontStyle フォントの太さ
   *   - textAlign 文字をどっち寄せにするか
   *   - fontSize 文字の大きさ
   *   - textColor 文字の色を決める
   * - background 文字の背景をどう表示するか決める
   *   - backgroundType 背景色で塗りつぶすか枠線だけにするか指定
   *   - backgroundWidth 指定することで背景がその横の長さになる。指定しないと文字の大きさに合わせて横の長さになる
   *   - backgroundFillColor 背景を塗りつぶす際の色を指定
   *   - backgroundStrokeColor 枠線の色を指定
   *   - padding 枠線と文字の間隔を指定
   *   - strokeWidth 枠線の長さを指定
   *   - rounded 枠線を丸くするかを指定。trueを渡すことで、丸くする
   * @returns
   * - endY 文字を描画した後のPDFの上からの距離
   * - backgroundWidth 背景を含めてブロックの横の長さ（PDFの横からではない）
   * - backgroundHeight 背景を含めてブロックの縦の長さ（PDFの上からではない）
   */
  public textWithBackground({
    position: { startX, startY },
    text: {
      text,
      fontStyle = "normal",
      textAlign = "LEFT",
      fontSize = FONT_SIZE.NORMAL,
      textColor = BILL_ONE_COLOR.BLACK,
    },
    background: {
      backgroundType,
      backgroundWidth,
      backgroundFillColor = BILL_ONE_COLOR.RED,
      backgroundStrokeColor = BILL_ONE_COLOR.BLACK,
      padding = 0,
      strokeWidth = 3,
      rounded = false,
    },
  }: {
    position: RenderTextPosition
    text: RenderTextTextDataOnBackground
    background: RenderTextBackground
  }): {
    backgroundWidth: number
    backgroundHeight: number
    endY: number
  } {
    const textWidth = fontSize * [...text].length // 絵文字などは入ってこないと思われるため、Segmenterは使わず簡易実装
    const usedBackgroundWidth = backgroundWidth ?? textWidth + padding * 2
    const numberOfLines = (() => {
      return Array.isArray(text) ? text.length : 1
    })()
    const backgroundHeight = fontSize * numberOfLines + padding * 2
    const actualStartY = (() => {
      // 描画することで、画面からはみ出そうな場合は改ページする
      if (this.getPdfHeight() <= startY + backgroundHeight) {
        this.nextPage()
        return this.getPdfHeight() - startY
      }

      return startY
    })()

    this.doc.setFontSize(fontSize)
    this.doc.setFont(FONT_FAMILY.GEN_EI_MONO_GOTHIC, fontStyle)

    switch (backgroundType) {
      case "FILL":
        this.doc.setFillColor(backgroundFillColor.r, backgroundFillColor.g, backgroundFillColor.b)
        this.doc.setLineWidth(strokeWidth)
        this.doc.rect(startX, actualStartY, usedBackgroundWidth, backgroundHeight, "FD")
        break
      case "STROKE":
        this.doc.setDrawColor(backgroundStrokeColor.r, backgroundStrokeColor.g, backgroundStrokeColor.b)
        this.doc.setLineWidth(strokeWidth)
        if (rounded) {
          this.doc.roundedRect(startX, actualStartY, usedBackgroundWidth, backgroundHeight, 5, 5)
        } else {
          this.doc.rect(startX, actualStartY, usedBackgroundWidth, backgroundHeight)
        }
        break
    }

    this.doc.setTextColor(textColor.r, textColor.g, textColor.b)
    switch (textAlign) {
      case "LEFT":
        this.doc.text(text, startX + padding, actualStartY + padding, {
          baseline: "top", // 左上から計算していくので、文字の配置も文字の上を起点にする
        })
        break
      case "RIGHT":
        this.doc.text(text, startX + usedBackgroundWidth - padding, actualStartY + padding, {
          baseline: "top", // 左上から計算していくので、文字の配置も文字の上を起点にする
          align: "right",
        })
        break
    }

    return {
      backgroundWidth: usedBackgroundWidth,
      backgroundHeight,
      endY: actualStartY + backgroundHeight,
    }
  }

  /**
   * 現在のPDF自体の横の長さを返す
   *
   * @returns PDFの横の長さ
   */
  public getPdfWidth(): number {
    return this.doc.internal.pageSize.width
  }

  /**
   * 現在のPDF自体の縦の長さを返す
   *
   * @returns PDFの縦の長さ
   */
  public getPdfHeight(): number {
    return this.doc.internal.pageSize.height
  }

  /**
   * 次のページに移動する。もし次のページがなければ、新しくページを追加する
   *
   */
  public nextPage() {
    const current = this.doc.getCurrentPageInfo().pageNumber
    this.doc.setPage(current + 1)

    const newCurrent = this.doc.getCurrentPageInfo().pageNumber
    if (current === newCurrent) {
      this.doc.addPage()
    }
  }

  /**
   * PDFを保存する
   */
  public save() {
    this.doc.save(this.fileName)
  }

  /**
   * PDFデータをArrayBuffer形式で取得する
   */
  public getArrayBuffer(): ArrayBuffer {
    return this.doc.output("arraybuffer")
  }

  /**
   * tableを描画する
   */
  public table({
    startY,
    header,
    bodies,
    styles: {
      tableMargin = { top: 0, right: 0, bottom: 0, left: 0 },
      tableBorderWidth,
      tableBorderColor = BILL_ONE_COLOR.BLACK,
      tableWidth,
      halign = "center",
      valign = "middle",
      font = FONT_FAMILY.GEN_EI_MONO_GOTHIC,
      fontStyle = "normal",
      fontSize = 8,
      customMinCellWidth,
    },
    headerStyles: {
      fillColor: headerFillColor = BILL_ONE_COLOR.WHITE,
      borderColor: headerBorderColor = BILL_ONE_COLOR.WHITE,
      borderWidth: headerBorderWidth = { top: 1, right: 1, bottom: 1, left: 1 },
      padding: headerPadding = { top: 0, right: 0, bottom: 0, left: 0 },
      fontStyle: headerFontStyle = "bold",
    },
    bodiesStyles: {
      fillColor: bodiesFillColor = BILL_ONE_COLOR.WHITE,
      borderColor: bodiesBorderColor = BILL_ONE_COLOR.WHITE,
      borderWidth: bodiesBorderWidth = { top: 1, right: 1, bottom: 1, left: 1 },
      padding: bodiesPadding = { top: 0, right: 0, bottom: 0, left: 0 },
      customHalign: bodiesCustomHalign,
      customPadding: bodiesCustomPadding,
      customFillColors: bodiesCustomFillColors,
      customFontColors: bodiesCustomFontColors,
      customFontStyles: bodiesCustomFontStyles,
      alternateRowFillColor = BILL_ONE_COLOR.WHITE,
    },
  }: RenderTableArgument): {
    endY: number
  } {
    if (bodies[0] === undefined) throw Error("bodiesは空配列を許しません")

    const lastColumnIndex = bodies[0].length - 1
    const isHeaderExist = header !== undefined && header.length > 0
    const lastRowIndex = bodies.length - 1

    autoTable(this.doc, {
      startY,
      theme: "plain",
      head: header ? [header] : undefined,
      body: bodies,
      tableWidth: tableWidth,
      margin: {
        top: tableMargin?.top,
        right: tableMargin?.right,
        bottom: tableMargin?.bottom,
        left: tableMargin?.left,
      },
      styles: {
        halign,
        valign,
        font,
        fontStyle,
        fontSize,
      },
      headStyles: {
        fillColor: [headerFillColor.r, headerFillColor.g, headerFillColor.b],
        lineColor: [headerBorderColor.r, headerBorderColor.g, headerBorderColor.b],
        lineWidth: {
          top: headerBorderWidth.top,
          right: headerBorderWidth.right,
          bottom: headerBorderWidth.bottom,
          left: headerBorderWidth.left,
        },
        cellPadding: {
          top: headerPadding.top,
          right: headerPadding.right,
          bottom: headerPadding.bottom,
          left: headerPadding.left,
        },
        fontStyle: headerFontStyle,
      },
      bodyStyles: {
        fillColor: [bodiesFillColor.r, bodiesFillColor.g, bodiesFillColor.b],
        lineColor: [bodiesBorderColor.r, bodiesBorderColor.g, bodiesBorderColor.b],
        lineWidth: {
          top: bodiesBorderWidth.top,
          right: bodiesBorderWidth.right,
          bottom: bodiesBorderWidth.bottom,
          left: bodiesBorderWidth.left,
        },
        cellPadding: {
          top: bodiesPadding.top,
          right: bodiesPadding.right,
          bottom: bodiesPadding.bottom,
          left: bodiesPadding.left,
        },
      },
      alternateRowStyles: {
        fillColor: [alternateRowFillColor.r, alternateRowFillColor.g, alternateRowFillColor.b],
      },
      //このフックは、列幅やその他の機能が計算される直前に呼び出されます。
      // didParseCell は列幅やその他の機能が計算される直前に呼び出され、特定のせるのテキストやスタイルをカスタマイズするために使用する
      didParseCell: (data) => {
        // tableLineWidth だと一律に全部描画してしまうため、自作で処理を書く
        if (tableBorderWidth !== undefined) {
          // 一番左の列
          if (data.row.section === "body" && data.column.index === 0) {
            data.cell.styles.lineWidth = {
              ...bodiesBorderWidth,
              left: tableBorderWidth.left,
            }

            // 左下のセル
            if (data.row.index === lastRowIndex) {
              data.cell.styles.lineWidth = {
                ...bodiesBorderWidth,
                left: tableBorderWidth.left,
                bottom: tableBorderWidth.bottom,
              }
            }
          }

          // 一番右の列
          if (data.row.section === "body" && data.column.index === lastColumnIndex) {
            data.cell.styles.lineWidth = {
              ...bodiesBorderWidth,
              right: tableBorderWidth.right,
            }

            if (data.row.index === lastRowIndex) {
              data.cell.styles.lineWidth = {
                ...bodiesBorderWidth,
                right: tableBorderWidth.left,
                bottom: tableBorderWidth.bottom,
              }
            }
          }

          // 1行目
          if (isHeaderExist) {
            // ヘッダーがある場合の1行目
            if (data.row.section === "head" && data.row.index === 0) {
              data.cell.styles.lineColor = [tableBorderColor.r, tableBorderColor.g, tableBorderColor.b]
              data.cell.styles.lineWidth = {
                ...headerBorderWidth,
                top: tableBorderWidth.top,
              }

              // 左上のセル
              if (data.column.index === 0) {
                data.cell.styles.lineWidth = {
                  ...headerBorderWidth,
                  top: tableBorderWidth.top,
                  left: tableBorderWidth.left,
                }
              }

              // 右上のセル
              if (data.column.index === lastColumnIndex) {
                data.cell.styles.lineWidth = {
                  ...headerBorderWidth,
                  top: tableBorderWidth.top,
                  right: tableBorderWidth.right,
                }
              }
            }
          } else {
            if (data.row.section === "body" && data.row.index === 0) {
              data.cell.styles.lineColor = [tableBorderColor.r, tableBorderColor.g, tableBorderColor.b]
              data.cell.styles.lineWidth = {
                ...bodiesBorderWidth,
                top: tableBorderWidth.top,
              }

              // 左上のセル
              if (data.column.index === 0) {
                data.cell.styles.lineWidth = {
                  ...bodiesBorderWidth,
                  top: tableBorderWidth.top,
                  left: tableBorderWidth.left,
                }
              }

              // 右上のセル
              if (data.column.index === lastColumnIndex) {
                data.cell.styles.lineWidth = {
                  ...bodiesBorderWidth,
                  top: tableBorderWidth.top,
                  right: tableBorderWidth.right,
                }
              }
            }

            // 一番下の行
            if (data.row.section === "body" && data.row.index === bodies.length - 1) {
              data.cell.styles.lineWidth = {
                ...bodiesBorderWidth,
                bottom: tableBorderWidth.bottom,
              }
            }
          }
        }

        // それぞれのStyleでminCellWidthの設定はできるが、全ての列に適応されるため、自作で処理を書く
        if (customMinCellWidth !== undefined) {
          const minCellWidth = customMinCellWidth[data.column.index]
          if (minCellWidth !== undefined) {
            data.cell.styles.minCellWidth = minCellWidth
          }
        }

        // それぞれのStyleでhalignの設定はできるが、全ての列に適応されるため、自作で処理を書く
        if (bodiesCustomHalign !== undefined && data.row.section === "body") {
          const customHalign = bodiesCustomHalign[data.column.index]
          if (customHalign !== undefined) {
            data.cell.styles.halign = customHalign
          }
        }

        // それぞれのStyleでpaddingの設定はできるが、全ての列に適応されるため、自作で処理を書く
        if (bodiesCustomPadding !== undefined && data.row.section === "body") {
          const customPadding = bodiesCustomPadding[data.column.index]
          if (customPadding !== undefined) {
            data.cell.styles.cellPadding = customPadding
          }
        }

        // それぞれのStyleでfillColorの設定はできるが、全てのセルに適応されてるため、自作で処理を書く
        if (bodiesCustomFillColors !== undefined && data.row.section === "body") {
          const customFillColor = bodiesCustomFillColors[data.row.index][data.column.index]
          if (customFillColor !== undefined) {
            data.cell.styles.fillColor = [customFillColor.r, customFillColor?.g, customFillColor.b]
          }
        }

        // それぞれのStyleでtextColorの設定はできるが、全てのセルに適応されるため、自作で処理を書く
        if (bodiesCustomFontColors !== undefined && data.row.section === "body") {
          const customFontColor = bodiesCustomFontColors[data.row.index][data.column.index]
          if (customFontColor !== undefined && data.row.section === "body") {
            data.cell.styles.textColor = [customFontColor.r, customFontColor.g, customFontColor.b]
          }
        }

        // それぞれのStyleでfontStyleの設定はできるが、全てのセルに適応されるため、自作で処理を書く
        if (bodiesCustomFontStyles !== undefined && data.row.section === "body") {
          const customFontStyle = bodiesCustomFontStyles[data.row.index][data.column.index]
          if (customFontStyle !== undefined && data.row.section === "body") {
            data.cell.styles.fontStyle = customFontStyle
          }
        }
      },
    })

    return {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      endY: (this.doc as any).lastAutoTable.finalY,
    }
  }

  constructor(
    pdfOption: PdfGeneratorOptions = {
      orientation: "portrait", // 縦のPDFにしたいため
      unit: "pt", // pdf-libがptしか使えず、移行させやすいように。また、ptを指定することでフォントサイズで文字を描画した際の高さや横の長さを計算しやすくしているので、変更する場合は注意が必要。
      format: "a4", // A4サイズにしたいため,
    },
  ) {
    this.fileName = "2024_summer_invoice.pdf"
    this.doc = new jsPDF(pdfOption)
    // インスタンス生成時に、フォント情報を設定してしまう
    this.initFont()
  }

  /**
   * ライブラリにフォント情報を設定する
   */
  private initFont() {
    const resourcePath = `./resource`
    const docRef = this.doc

    setGenEiMonoGothicBold()
    setGenEiMonoGothicRegular()

    function setGenEiMonoGothicBold() {
      const ttf = `${FONT_FAMILY.GEN_EI_MONO_GOTHIC}-Bold.ttf`
      const font = readFileSync(`${resourcePath}/${ttf}`, { encoding: "base64" })

      docRef.addFileToVFS(ttf, font)
      docRef.addFont(ttf, FONT_FAMILY.GEN_EI_MONO_GOTHIC, "bold")
    }

    function setGenEiMonoGothicRegular() {
      const ttf = `${FONT_FAMILY.GEN_EI_MONO_GOTHIC}-Regular.ttf`
      const font = readFileSync(`${resourcePath}/${ttf}`, { encoding: "base64" })

      docRef.addFileToVFS(ttf, font)
      docRef.addFont(ttf, FONT_FAMILY.GEN_EI_MONO_GOTHIC, "normal")
    }
  }
}
