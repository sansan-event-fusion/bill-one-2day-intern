# bill-one-2024-summer

# リポジトリ構成

```
.
|
|-- frontend # フロントエンド 
|-- functions # google cloud functions
|-- ktor-app-issuing # ktorアプリケーション(発行)
|-- ktor-app-recipient # ktorアプリケーション(受取)
|-- summer-kotlin-lib # サマーインターン(2days)用のkotlin(ktor)ライブラリ
|-- local-dev # ローカル開発用の色々
```

## 主な技術構成

* frontend
    * react
    * typescript
    * mantine (ui library)
    * npm (package manager)
* functions
    * typescript
    * functions-framework (google cloud functions)
    * npm (package manager)
* kotlin関連
    * kotlin
    * ktor
    * gradle (package manager & build tool)
* その他
    * docker
    * gcloud
    * postgres15

# ローカル開発環境の構築

## ライブラリを使えるようにする

各マイクロサービスで読み込むためにローカルのmavenリポジトリにこのライブラリをpushします。

```shell
# at summer-kotlin-lib
./gradlew publishMavenPublicationToMavenLocal
```

## ログを見やすくする

pino-prettyでログを見やすく管理しているので、インストールします。

```shell
# at everywhere
npm install -g pino-pretty
```

##  Dockerを起動する前に

```shell
# at repo root
chmod -R 755 ./local-dev/dockerfiles
```
権限でエラーが出る場合があるため、念の為実行してください。

## docker周りの起動

仮想環境でデータベースやその他emulatorを起動します。

```shell
# at local-dev
docker-compose up -d --build 
```

## テーブルの作成と初期データの挿入

見出しの通りです

```shell
# at ktor-app-issuing & ktor-app-recipient
./operations/migrate.sh --local && ./operations/dbsetup.sh --local
```

## 各microserviceの起動

```shell
# at ktor-app-issuing & ktor-app-recipient
./gradlew run
```

## frontendの起動

```shell
# at frontend
# 初回のみ
npm install
# 起動
npm run dev 
```

## functionsの起動

```shell
# at functions/issue-pdf-by-template
# 初回のみ
npm install
# 起動
npm run dev
```
