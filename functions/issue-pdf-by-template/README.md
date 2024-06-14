 # issue-invoice-pdf

## イベントフロー

```mermaid
sequenceDiagram
	participant fr as frontend
	participant is as issuingサービス
	participant rs as recipientサービス
	participant f as PDF作成Functions<br>(issue-pdf-by-template)
	participant s as cloud storage
	
	fr ->>is: 請求書をFormから登録
	is ->>f: 請求書情報を送信(ドメインイベント)
	f ->>s: 作成した請求書PDFをアップロード
	f ->>is: 作成した結果を反映
	f ->>rs: 作成したPDFを送信
	
```

## ローカルで動作確認
Functionsを起動
```
npm install
npm run dev
```

### サンプルのリクエストを送信
curlでリクエストを送って`2024-summer-invoice.pdf`が作成されることを確認する
test/fixtures/sample-local-request.jsonの内容がリクエストされる。
(storageアップロードとイベント発行は動作確認時にはコメントアウトしている)
```
curl -X POST localhost:8400 -H "Content-Type:application/json" -d @test/fixtures/sample-local-request.json
```



##