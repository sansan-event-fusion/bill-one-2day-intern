# summer kotlin lib

サマーインターン(2days)用のkotlin(ktor)ライブラリになります。
実際の実装と違う部分も多いですが、共通ライブラリを使用している雰囲気だけでも掴んでもらうためのライブラリ群です。

# 使い方

## ライブラリのビルド

各マイクロサービスで読み込むためにローカルのmavenリポジトリにこのライブラリをpushします。

```shell
./gradlew publishMainPublicationToMavenLocal
```

