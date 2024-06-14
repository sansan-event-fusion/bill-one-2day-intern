import { Color } from "./color"

export type FontWeight = "normal" | "bold"

export type TextAlign = "LEFT" | "RIGHT"

/**
 * フォントサイズを保持する
 * 基本的には、この定義を利用するようにして、後でデザイン調整をしやすくする
 */
export const FONT_SIZE = {
  HEADER_1: 24,
  HEADER_4: 10,
  NORMAL: 8,
}

/**
 * フォントを保持する
 * ライブラリにフォントを追加した際には、ここに定数を追加する
 */
export const FONT_FAMILY = {
  GEN_EI_MONO_GOTHIC: "GenEiMonoGothic",
}

export type RenderTextPosition = {
  startX: number
  startY: number
}

export type RenderTextTextData = {
  text: string | string[]
  fontStyle?: FontWeight
  textAlign?: TextAlign
  fontSize?: number
  textColor?: Color
  maxWidth?: number
}

export type RenderTextTextDataOnBackground = Exclude<RenderTextTextData, "maxWidth">

export type RenderTextBackground = {
  backgroundType: "FILL" | "STROKE"
  backgroundWidth?: number
  backgroundFillColor?: Color
  backgroundStrokeColor?: Color
  padding?: number
  strokeWidth?: number
  rounded?: boolean
}
