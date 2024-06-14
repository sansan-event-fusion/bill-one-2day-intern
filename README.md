# bill-one-2days-internship

## 前提条件

### Docker Desktop

[公式ドキュメント](https://docs.docker.com/get-docker/)を参考に最新の Docker Desktop をインストールしてください。

### JDK11

#### macOS

[Homebrew](https://brew.sh/index_ja)を用いてインストールできます。
インストール時に出力されるメッセージを参考に PATH を通してください。

```shell
brew install openjdk@11
```

#### Windows

winget を用いてインストールできます。

参考: [Microsoft Build of OpenJDK をインストールする](https://docs.microsoft.com/ja-jp/java/openjdk/install#install-with-the-windows-package-manager)

```shell
winget install Microsoft.OpenJDK.11
```

### PostgreSQL

`psql` コマンドを用いてサンプルデータをインポートするため、PostgreSQL をインストールします。

#### macOS

```shell
brew install postgresql
```

#### Windows

```shell
winget install PostgreSQL.PostgreSQL
```

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
