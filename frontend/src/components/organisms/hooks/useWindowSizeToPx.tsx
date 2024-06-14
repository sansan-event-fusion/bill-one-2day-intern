// 縦横のパーセンテージ(windowの大きさから見た)を渡すとpxに直してくれるやつ(embedにつかう)
// %不可: https://developer.mozilla.org/ja/docs/Web/HTML/Element/embed
// あまりこのような実装はしないが、テスト等のためにあえて作成した
export const useWindowSizeToPx = (height: number, width: number) => {
  // 0パーセト以下は異常値(css:hiddenで対応できる)ためthrowする。
  if (height <= 0) {
    throw new Error(`Height must be greater than 0, but got ${height}`);
  }
  if (width <= 0) {
    throw new Error(`Width must be greater than 0, but got ${width}`);
  }

  const getPxFromHeightPercent = (height: number): number =>
    (height / 100) * window.innerHeight;

  const getPxFromWidthPercent = (width: number): number =>
    (width / 100) * window.innerWidth;

  return {
    height: getPxFromHeightPercent(height),
    width: getPxFromWidthPercent(width),
  };
};
