import { HAlignType } from "jspdf-autotable"
import { Color } from "./color"
import { Space } from "./space"
import { FontWeight } from "./text"

export type TableStyle = {
  tableMargin?: { top?: number; right?: number; bottom?: number; left?: number }
  tableBorderWidth?: Space
  tableBorderColor?: Color
  tableWidth?: number
  halign?: Extract<HAlignType, "left" | "right" | "center">
  valign?: "middle"
  font?: string
  fontStyle?: FontWeight
  fontSize?: number
  customMinCellWidth?: number[]
}

export type TableHeaderStyle = {
  fillColor?: Color
  borderColor?: Color
  borderWidth?: Space
  padding?: Space
  fontStyle?: FontWeight
}

export type TableBodyStyle = {
  fillColor?: Color
  borderColor?: Color
  borderWidth?: Space
  padding?: Space
  customHalign?: Extract<HAlignType, "left" | "right" | "center">[]
  customPadding?: (Space | undefined)[]
  customFillColors?: (Color | undefined)[][]
  customFontColors?: (Color | undefined)[][]
  customFontStyles?: (FontWeight | undefined)[][]
  alternateRowFillColor?: Color
}

export type RenderTableArgument = {
  startY: number
  header?: string[]
  bodies: string[][]
  styles: TableStyle
  headerStyles: TableHeaderStyle
  bodiesStyles: TableBodyStyle
}
