/**
 * 色を表す
 */
export type Color = {
  r: number
  g: number
  b: number
}

export const BILL_ONE_COLOR = {
  BLACK: { r: 0, g: 0, b: 0 },
  RED: { r: 255, g: 0, b: 0 },
  WHITE: { r: 255, g: 255, b: 255 },
} satisfies { [key: string]: Color }
