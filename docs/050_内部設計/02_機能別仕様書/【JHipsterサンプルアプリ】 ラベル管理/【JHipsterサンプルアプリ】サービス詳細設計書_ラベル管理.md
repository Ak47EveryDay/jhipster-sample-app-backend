# 【JHipsterサンプルアプリ】サービス詳細設計書_ラベル管理

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 |  | 検索・登録・更新・削除 |

目次

| 項番 | シート名 |
| --- | --- |
| 1 | 処理概要 |
| 2 | 1　対象クラス |
| 3 | 2　API定義 |
| 4 | 3　単項目チェック |
| 5 | 4　相関項目チェック |
| 6 | 5　依存クラス |
| 7 | 6　処理詳細（POST /api/labels） |
| 8 | 7　処理詳細（PUT /api/labels/{id}） |
| 9 | 8　処理詳細（PATCH /api/labels/{id}） |
| 10 | 9　処理詳細（GET /api/labels） |
| 11 | 10　処理詳細（GET /api/labels/{id}） |
| 12 | 11　処理詳細（DELETE /api/labels/{id}） |
| 13 | 12　エラー一覧 |

---

## 処理概要

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 |  | 検索・登録・更新・削除 |

### 処理概要

| ・ | ラベル（ラベル名）を検索、登録、更新、削除する。 |
| --- | --- |

コール元に対する補足説明

フロントエンド（別リポジトリ）が HTTP で呼び出す。認証済みであれば権限を問わず呼び出せる。本機能にはサービスクラスがなく、リソースクラスがリポジトリを直接使う。リクエスト・応答の本文には DTO を使わず、エンティティ `Label` を直接使う。ラベルは取引と多対多で関連付く。

---

## 1　対象クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | 対象クラス | 検索・登録・更新・削除 |

### 処理詳細

| No | クラス | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- | --- |
| 1 | `LabelResource` | `src/main/java/io/github/jhipster/sample/web/rest/LabelResource.java` | ラベル管理のエンドポイント |
| 2 | `Label` | `src/main/java/io/github/jhipster/sample/domain/Label.java` | ラベルのエンティティ。リクエスト・応答の本文としても使う |
| 3 | `LabelRepository` | `src/main/java/io/github/jhipster/sample/repository/LabelRepository.java` | ラベルテーブルへのアクセス |
| 4 | `BadRequestAlertException` | `src/main/java/io/github/jhipster/sample/web/rest/errors/BadRequestAlertException.java` | ID の指定エラー |

---

## 2　API定義

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | API定義 | 検索・登録・更新・削除 |

### 処理詳細

| No | HTTPメソッド | URL | リソースクラス | メソッド | 認可 | リクエスト | 応答本文 | 正常時ステータス | トランザクション |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/labels` | LabelResource | `createLabel` | 認証済み | 本文 `Label`（JSON） | `Label` | 201 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 2 | PUT | `/api/labels/{id}` | LabelResource | `updateLabel` | 認証済み | パス変数 `id`、本文 `Label`（JSON） | `Label` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 3 | PATCH | `/api/labels/{id}` | LabelResource | `partialUpdateLabel` | 認証済み | パス変数 `id`、本文 `Label`（JSON または `application/merge-patch+json`） | `Label` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 4 | GET | `/api/labels` | LabelResource | `getAllLabels` | 認証済み | なし | `Label` の配列 | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 5 | GET | `/api/labels/{id}` | LabelResource | `getLabel` | 認証済み | パス変数 `id` | `Label` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 6 | DELETE | `/api/labels/{id}` | LabelResource | `deleteLabel` | 認証済み | パス変数 `id` | なし | 204 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |

リソースクラスに `@Transactional(rollbackFor = Exception.class)` を付与しており、全メソッドがトランザクションの中で実行される。口座管理、取引管理と違い、`eagerload` パラメータとページングはない。

---

## 3　単項目チェック

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | 単項目チェック | 検索・登録・更新・削除 |

### 処理概要

リクエストデータ部の単項目チェックを実施する。

### 処理詳細

`POST` と `PUT` が対象である。チェックは `Label` エンティティの Bean Validation アノテーションで行う。

| No | 項目 | 型 | 必須 | 桁数 | アノテーション | 備考 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | Long | － | － | なし | 登録時は null とする（「4　相関項目チェック」） |
| 2 | `label` | String | ○ | 3 以上 | `@NotNull`、`@Size(min = 3)` | ラベル名 |
| 3 | `operations` | `Operation` の集合 | － | － | なし | 取引（関連） |

`PATCH` は本文に `@Valid` を付けていないため、項目のチェックは行わない（パラメータには `@NotNull` のみ付与している）。

チェックエラーの場合、業務エラーと返す。

---

## 4　相関項目チェック

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | 相関項目チェック | 検索・登録・更新・削除 |

### 処理概要

ID の指定と存在をチェックする。

### 処理詳細

チェックはリソースクラス内のコードで行う。エンティティ名（`ENTITY_NAME`）は `label` である。

#### １）登録時（POST）

| No | チェック | 発生条件 | デフォルトメッセージ | エラーキー |
| --- | --- | --- | --- | --- |
| 1 | ID の指定 | リクエスト本文．`id` が null 以外 | `A new label cannot already have an ID` | `idexists` |

#### ２）更新時（PUT、PATCH）

| No | チェック | 発生条件 | デフォルトメッセージ | エラーキー |
| --- | --- | --- | --- | --- |
| 1 | ID の有無 | リクエスト本文．`id` が null | `Invalid id` | `idnull` |
| 2 | ID の一致 | パス変数．`id` とリクエスト本文．`id` が不一致 | `Invalid ID` | `idinvalid` |
| 3 | ラベルの存在 | `LabelRepository#existsById(パス変数．id)` が false | `Entity not found` | `idnotfound` |

いずれの場合も `BadRequestAlertException` をスローし、業務エラー（400）とする。パス変数の `id` は `required = false` で宣言しているが、`/api/labels` に `id` を付けずに `PUT`、`PATCH` を呼び出した場合は該当するマッピングがなく、405 となる。ラベル名の一意性チェックはない。

---

## 5　依存クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | 依存クラス | 検索・登録・更新・削除 |

### 処理概要

コンストラクタインジェクションで、本クラスが使う次の Bean を注入する。

### 処理詳細

| No | クラス | フィールド | 用途 |
| --- | --- | --- | --- |
| 1 | `LabelRepository` | `labelRepository` | ラベルの検索、保存、削除 |

`@Value("${jhipster.clientApp.name:jhipsterSampleApplication}")` で読み込む `applicationName` は、アラートヘッダの名前に使う。

`LabelRepository` の `save`、`findById`、`findAll`、`existsById`、`deleteById` は、`JpaRepository` から継承したメソッドである。リポジトリのインターフェースには宣言がない。

---

## 6　処理詳細（POST /api/labels）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | createLabel | 登録 |

### 処理概要

ラベルを新規登録する。

### 処理詳細

対象メソッドは `LabelResource#createLabel(@Valid @RequestBody Label label)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）保存する。

`LabelRepository#save(label)` で保存する。`id` はシーケンス `sequence_generator` で採番する。

#### ３）応答を返す。

| 項目 | 内容 |
| --- | --- |
| ステータス | 201 |
| ヘッダ | `Location: /api/labels/{id}`、`X-jhipsterSampleApplicationApp-alert: label.created`、`X-jhipsterSampleApplicationApp-params: {id}` |
| 応答本文 | 登録した `Label` の JSON |

---

## 7　処理詳細（PUT /api/labels/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | updateLabel | 更新 |

### 処理概要

ラベルを全項目更新する。

### 処理詳細

対象メソッドは `LabelResource#updateLabel(@PathVariable(value = "id", required = false) Long id, @Valid @RequestBody Label label)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）保存する。

`LabelRepository#save(label)` で保存する。リクエスト本文のエンティティをそのまま保存するため、本文に含めない項目は上書きされる（部分更新ではない）。

#### ３）応答を返す。

| 項目 | 内容 |
| --- | --- |
| ステータス | 200 |
| ヘッダ | `X-jhipsterSampleApplicationApp-alert: label.updated`、`X-jhipsterSampleApplicationApp-params: {id}` |
| 応答本文 | 更新した `Label` の JSON |

---

## 8　処理詳細（PATCH /api/labels/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | partialUpdateLabel | 更新 |

### 処理概要

ラベルを部分更新する。

### 処理詳細

対象メソッドは `LabelResource#partialUpdateLabel(@PathVariable(value = "id", required = false) Long id, @NotNull @RequestBody Label label)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）既存のラベルを取得し、指定のあった項目だけを更新する。

`LabelRepository#findById(リクエスト本文．id)` で既存のラベルを取得し、リクエスト本文で null 以外の項目を設定する。

| No | 編集項目 | 編集仕様 | 備考 |
| --- | --- | --- | --- |
| 1 | `label` | リクエスト本文．`label` | null の場合は更新しない |

`operations` は部分更新の対象外である。

#### ３）保存して応答を返す。

`LabelRepository#save` で保存し、`ResponseUtil#wrapOrNotFound` で応答に変換する。

| 条件 | ステータス | ヘッダ | 応答本文 |
| --- | --- | --- | --- |
| 更新できた | 200 | `X-jhipsterSampleApplicationApp-alert: label.updated`、`X-jhipsterSampleApplicationApp-params: {id}` | 更新した `Label` |
| 既存のラベルが取得できない | 404 | なし | Problem Details |

---

## 9　処理詳細（GET /api/labels）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | getAllLabels | 検索 |

### 処理概要

全ラベルを検索する。

### 処理詳細

対象メソッドは `LabelResource#getAllLabels()` である。`LabelRepository#findAll()` の結果をそのまま返す。ページング、抽出条件、関連の一括読み込みはない。

| 項目 | 内容 |
| --- | --- |
| ステータス | 200 |
| 応答本文 | `Label` の配列（0 件の場合は空配列） |

---

## 10　処理詳細（GET /api/labels/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | getLabel | 検索 |

### 処理概要

ラベルを 1 件検索する。

### 処理詳細

対象メソッドは `LabelResource#getLabel(@PathVariable("id") Long id)` である。

`LabelRepository#findById(id)` で検索し、`ResponseUtil#wrapOrNotFound` で応答に変換する。

| 条件 | ステータス | 応答本文 |
| --- | --- | --- |
| 該当あり | 200 | `Label` |
| 該当なし | 404 | Problem Details |

---

## 11　処理詳細（DELETE /api/labels/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | deleteLabel | 削除 |

### 処理概要

ラベルを削除する。

### 処理詳細

対象メソッドは `LabelResource#deleteLabel(@PathVariable("id") Long id)` である。

#### １）削除する。

`LabelRepository#deleteById(id)` で削除する。

#### ２）応答を返す。

ステータス 204、応答本文なし。ヘッダ `X-jhipsterSampleApplicationApp-alert: label.deleted`、`X-jhipsterSampleApplicationApp-params: {id}` を返す。

取引が紐付いているラベルを削除しようとすると、外部キー制約 `fk_rel_operation__label__label_id` に反するため、コードからの導出では 500 となる。

---

## 12　エラー一覧

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | LabelResource | ラベル管理 | エラー一覧 | 検索・登録・更新・削除 |

### 処理詳細

エラー応答の本文は RFC 7807（Problem Details）形式である。変換規則は「01_共通仕様書 例外処理設計書」を参照。

| No | HTTPメソッド | 発生条件 | ステータス | `type` | `message` | 根拠 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST、PUT | 単項目チェックエラー | 400 | `constraint-violation` | `error.validation` | POST の場合は `LabelResourceIT#checkLabelIsRequired`。PUT の場合はコード（`@Valid`） |
| 2 | POST | リクエスト本文に `id` が設定されている | 400 | `problem-with-message` | `error.idexists` | `LabelResourceIT#createLabelWithExistingId` |
| 3 | PUT、PATCH | リクエスト本文に `id` がない | 400 | `problem-with-message` | `error.idnull` | コード（`BadRequestAlertException`） |
| 4 | PUT、PATCH | パス変数の `id` と本文の `id` が不一致 | 400 | `problem-with-message` | `error.idinvalid` | `LabelResourceIT#putWithIdMismatchLabel`、`patchWithIdMismatchLabel` |
| 5 | PUT、PATCH | `id` に該当するラベルがない | 400 | `problem-with-message` | `error.idnotfound` | `LabelResourceIT#putNonExistingLabel`、`patchNonExistingLabel` |
| 6 | PUT、PATCH | `/api/labels` に `id` を付けずに呼び出した | 405 | `problem-with-message` | `error.http.405` | `LabelResourceIT#putWithMissingIdPathParamLabel`、`patchWithMissingIdPathParamLabel` |
| 7 | GET（一件） | `id` に該当するラベルがない | 404 | `problem-with-message` | `error.http.404` | `LabelResourceIT#getNonExistingLabel` |
| 8 | 全エンドポイント | 未認証、または JWT が無効 | 401 | － | 本文なし | コード（`SecurityConfiguration`） |
| 9 | DELETE | 取引が紐付いているラベルを削除した | 500 | `problem-with-message` | `error.http.500` | コードからの導出（外部キー制約） |

---
