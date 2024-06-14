/**
 * 指定した文字数で文字列を分割する
 *
 * @param text 分割したい文字列
 * @param maxLength 分割したい文字数
 * @returns 分割した文字列の配列
 */
export const splitStringByMaxLength = (text: string, maxLength: number): string[] => {
  const arr = []

  for (let i = 0; i < text.length; i += maxLength) {
    arr.push(text.substring(i, i + maxLength))
  }

  return arr
}

/**
 * 指定した横の長さで分割する
 *
 * @param text 分割したい文字列
 * @param fontSize 分割したい文字列のフォントサイズ
 * @param maxWidth どの横の長さで分割したいか
 * @returns 分割された文字列
 */
export const splitStringByMaxWidth = (text: string, fontSize: number, maxWidth: number): string[] => {
  let maxLength = 0
  for (let i = 0; i < text.length; i += 1) {
    if (i * fontSize > maxWidth) {
      maxLength = i - 1
      break
    }
  }

  return maxLength === 0 ? [text] : splitStringByMaxLength(text, maxLength)
}

/**
 * 日付をユーザーへの表示用にフォーマットする。
 *
 * @param date 日付を表すDate
 * @param format 出力フォーマット形式
 */
export const formatDate = (date: Date, format: string) =>
  format
    .replace(/yyyy/g, date.getFullYear().toString())
    .replace(/MM/g, ("0" + (date.getMonth() + 1)).slice(-2))
    .replace(/dd/g, ("0" + date.getDate()).slice(-2))
    .replace(/HH/g, ("0" + date.getHours()).slice(-2))
    .replace(/mm/g, ("0" + date.getMinutes()).slice(-2))
    .replace(/ss/g, ("0" + date.getSeconds()).slice(-2))
    .replace(/SSS/g, ("00" + date.getMilliseconds()).slice(-3))
