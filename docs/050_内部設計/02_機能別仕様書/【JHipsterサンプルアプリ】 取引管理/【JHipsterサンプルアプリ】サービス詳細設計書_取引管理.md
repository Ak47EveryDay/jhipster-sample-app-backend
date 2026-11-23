# 【JHipsterサンプルアプリ】サービス詳細設計書_取引管理

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 |  | 検索・登録・更新・削除 |

目次

| 項番 | シート名 |
| --- | --- |
| 1 | 処理概要 |
| 2 | 1　対象クラス |
| 3 | 2　API定義 |
| 4 | 3　単項目チェック |
| 5 | 4　相関項目チェック |
| 6 | 5　依存クラス |
| 7 | 6　処理詳細（POST /api/operations） |
| 8 | 7　処理詳細（PUT /api/operations/{id}） |
| 9 | 8　処理詳細（PATCH /api/operations/{id}） |
| 10 | 9　処理詳細（GET /api/operations） |
| 11 | 10　処理詳細（GET /api/operations/{id}） |
| 12 | 11　処理詳細（DELETE /api/operations/{id}） |
| 13 | 12　エラー一覧 |

---

## 処理概要

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 |  | 検索・登録・更新・削除 |

### 処理概要

| ・ | 取引（取引日時、説明、金額）を検索、登録、更新、削除する。 |
| --- | --- |

コール元に対する補足説明

フロントエンド（別リポジトリ）が HTTP で呼び出す。認証済みであれば権限を問わず呼び出せる。本機能にはサービスクラスがなく、リソースクラスがリポジトリを直接使う。リクエスト・応答の本文には DTO を使わず、エンティティ `Operation` を直接使う。取引は口座（多対 1）とラベル（多対多）に関連付く。

---

## 1　対象クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | 対象クラス | 検索・登録・更新・削除 |

### 処理詳細

| No | クラス | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- | --- |
| 1 | `OperationResource` | `src/main/java/io/github/jhipster/sample/web/rest/OperationResource.java` | 取引管理のエンドポイント |
| 2 | `Operation` | `src/main/java/io/github/jhipster/sample/domain/Operation.java` | 取引のエンティティ。リクエスト・応答の本文としても使う |
| 3 | `OperationRepository` | `src/main/java/io/github/jhipster/sample/repository/OperationRepository.java` | 取引テーブルへのアクセス |
| 4 | `OperationRepositoryWithBagRelationshipsImpl` | `src/main/java/io/github/jhipster/sample/repository/OperationRepositoryWithBagRelationshipsImpl.java` | ラベル（多対多）の一括読み込み |
| 5 | `BadRequestAlertException` | `src/main/java/io/github/jhipster/sample/web/rest/errors/BadRequestAlertException.java` | ID の指定エラー |

---

## 2　API定義

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | API定義 | 検索・登録・更新・削除 |

### 処理詳細

| No | HTTPメソッド | URL | リソースクラス | メソッド | 認可 | リクエスト | 応答本文 | 正常時ステータス | トランザクション |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/operations` | OperationResource | `createOperation` | 認証済み | 本文 `Operation`（JSON） | `Operation` | 201 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 2 | PUT | `/api/operations/{id}` | OperationResource | `updateOperation` | 認証済み | パス変数 `id`、本文 `Operation`（JSON） | `Operation` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 3 | PATCH | `/api/operations/{id}` | OperationResource | `partialUpdateOperation` | 認証済み | パス変数 `id`、本文 `Operation`（JSON または `application/merge-patch+json`） | `Operation` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 4 | GET | `/api/operations` | OperationResource | `getAllOperations` | 認証済み | ページング（`page`、`size`、`sort`）、クエリパラメータ `eagerload`（既定 `true`） | `Operation` の配列 | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 5 | GET | `/api/operations/{id}` | OperationResource | `getOperation` | 認証済み | パス変数 `id` | `Operation` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 6 | DELETE | `/api/operations/{id}` | OperationResource | `deleteOperation` | 認証済み | パス変数 `id` | なし | 204 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |

リソースクラスに `@Transactional(rollbackFor = Exception.class)` を付与しており、全メソッドがトランザクションの中で実行される。

---

## 3　単項目チェック

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | 単項目チェック | 検索・登録・更新・削除 |

### 処理概要

リクエストデータ部の単項目チェックを実施する。

### 処理詳細

`POST` と `PUT` が対象である。チェックは `Operation` エンティティの Bean Validation アノテーションで行う。

| No | 項目 | 型 | 必須 | 桁数・精度 | アノテーション | 備考 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | Long | － | － | なし | 登録時は null とする（「4　相関項目チェック」） |
| 2 | `date` | Instant | ○ | － | `@NotNull` | 取引日時。ISO-8601 形式の文字列で入出力する |
| 3 | `description` | String | － | 255 以内 | なし | 説明 |
| 4 | `amount` | BigDecimal | ○ | 整数部 19 桁、小数部 2 桁 | `@NotNull` | 金額 |
| 5 | `bankAccount` | `BankAccount` | － | － | なし | 所属口座（多対 1） |
| 6 | `labels` | `Label` の集合 | － | － | なし | ラベル（多対多） |

`PATCH` は本文に `@Valid` を付けていないため、項目のチェックは行わない（パラメータには `@NotNull` のみ付与している）。桁数・精度はエンティティの `@Column` とデータベース定義（`varchar(255)`、`decimal(21,2)`）による制約であり、アプリケーション側のチェックではない。

チェックエラーの場合、業務エラーと返す。

---

## 4　相関項目チェック

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | 相関項目チェック | 検索・登録・更新・削除 |

### 処理概要

ID の指定と存在をチェックする。

### 処理詳細

チェックはリソースクラス内のコードで行う。エンティティ名（`ENTITY_NAME`）は `operation` である。

#### １）登録時（POST）

| No | チェック | 発生条件 | デフォルトメッセージ | エラーキー |
| --- | --- | --- | --- | --- |
| 1 | ID の指定 | リクエスト本文．`id` が null 以外 | `A new operation cannot already have an ID` | `idexists` |

#### ２）更新時（PUT、PATCH）

| No | チェック | 発生条件 | デフォルトメッセージ | エラーキー |
| --- | --- | --- | --- | --- |
| 1 | ID の有無 | リクエスト本文．`id` が null | `Invalid id` | `idnull` |
| 2 | ID の一致 | パス変数．`id` とリクエスト本文．`id` が不一致 | `Invalid ID` | `idinvalid` |
| 3 | 取引の存在 | `OperationRepository#existsById(パス変数．id)` が false | `Entity not found` | `idnotfound` |

いずれの場合も `BadRequestAlertException` をスローし、業務エラー（400）とする。パス変数の `id` は `required = false` で宣言しているが、`/api/operations` に `id` を付けずに `PUT`、`PATCH` を呼び出した場合は該当するマッピングがなく、405 となる。所属口座やラベルの存在チェックは行わない。

---

## 5　依存クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | 依存クラス | 検索・登録・更新・削除 |

### 処理概要

コンストラクタインジェクションで、本クラスが使う次の Bean を注入する。

### 処理詳細

| No | クラス | フィールド | 用途 |
| --- | --- | --- | --- |
| 1 | `OperationRepository` | `operationRepository` | 取引の検索、保存、削除 |

`@Value("${jhipster.clientApp.name:jhipsterSampleApplication}")` で読み込む `applicationName` は、アラートヘッダの名前に使う。

`OperationRepository` の `save`、`findById`、`findAll`、`existsById`、`deleteById` は、`JpaRepository` から継承したメソッドである。リポジトリのインターフェースには宣言がない。

---

## 6　処理詳細（POST /api/operations）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | createOperation | 登録 |

### 処理概要

取引を新規登録する。

### 処理詳細

対象メソッドは `OperationResource#createOperation(@Valid @RequestBody Operation operation)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）保存する。

`OperationRepository#save(operation)` で保存する。`id` はシーケンス `sequence_generator` で採番する。

#### ３）応答を返す。

| 項目 | 内容 |
| --- | --- |
| ステータス | 201 |
| ヘッダ | `Location: /api/operations/{id}`、`X-jhipsterSampleApplicationApp-alert: operation.created`、`X-jhipsterSampleApplicationApp-params: {id}` |
| 応答本文 | 登録した `Operation` の JSON |

---

## 7　処理詳細（PUT /api/operations/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | updateOperation | 更新 |

### 処理概要

取引を全項目更新する。

### 処理詳細

対象メソッドは `OperationResource#updateOperation(@PathVariable(value = "id", required = false) Long id, @Valid @RequestBody Operation operation)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）保存する。

`OperationRepository#save(operation)` で保存する。リクエスト本文のエンティティをそのまま保存するため、本文に含めない項目は上書きされる（部分更新ではない）。

#### ３）応答を返す。

| 項目 | 内容 |
| --- | --- |
| ステータス | 200 |
| ヘッダ | `X-jhipsterSampleApplicationApp-alert: operation.updated`、`X-jhipsterSampleApplicationApp-params: {id}` |
| 応答本文 | 更新した `Operation` の JSON |

---

## 8　処理詳細（PATCH /api/operations/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | partialUpdateOperation | 更新 |

### 処理概要

取引を部分更新する。

### 処理詳細

対象メソッドは `OperationResource#partialUpdateOperation(@PathVariable(value = "id", required = false) Long id, @NotNull @RequestBody Operation operation)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）既存の取引を取得し、指定のあった項目だけを更新する。

`OperationRepository#findById(リクエスト本文．id)` で既存の取引を取得し、リクエスト本文で null 以外の項目を設定する。

| No | 編集項目 | 編集仕様 | 備考 |
| --- | --- | --- | --- |
| 1 | `date` | リクエスト本文．`date` | null の場合は更新しない |
| 2 | `description` | リクエスト本文．`description` | null の場合は更新しない |
| 3 | `amount` | リクエスト本文．`amount` | null の場合は更新しない |

`bankAccount` と `labels` は部分更新の対象外である。

#### ３）保存して応答を返す。

`OperationRepository#save` で保存し、`ResponseUtil#wrapOrNotFound` で応答に変換する。

| 条件 | ステータス | ヘッダ | 応答本文 |
| --- | --- | --- | --- |
| 更新できた | 200 | `X-jhipsterSampleApplicationApp-alert: operation.updated`、`X-jhipsterSampleApplicationApp-params: {id}` | 更新した `Operation` |
| 既存の取引が取得できない | 404 | なし | Problem Details |

---

## 9　処理詳細（GET /api/operations）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | getAllOperations | 検索 |

### 処理概要

取引をページング付きで検索する。

### 処理詳細

対象メソッドは `OperationResource#getAllOperations(@ParameterObject Pageable pageable, @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload)` である。

| No | 条件 | 取得メソッド | 備考 |
| --- | --- | --- | --- |
| 1 | `eagerload` が `true`（既定） | `OperationRepository#findAllWithEagerRelationships(pageable)` | 所属口座（`bankAccount`）とラベル（`labels`）を同時に読み込む |
| 2 | `eagerload` が `false` | `OperationRepository#findAll(pageable)` | 関連は読み込まない |

抽出条件はない。ラベルの一括読み込みの仕組みは「06_リポジトリ仕様書 リポジトリ設計書_operation」を参照。

| 項目 | 内容 |
| --- | --- |
| ステータス | 200 |
| ヘッダ | `X-Total-Count: {総件数}`、`Link: {先頭、前、次、最終ページへのリンク}` |
| 応答本文 | `Operation` の配列（ページ内の分のみ） |

---

## 10　処理詳細（GET /api/operations/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | getOperation | 検索 |

### 処理概要

取引を 1 件検索する。

### 処理詳細

対象メソッドは `OperationResource#getOperation(@PathVariable("id") Long id)` である。

`OperationRepository#findOneWithEagerRelationships(id)` で所属口座とラベルを含めて取得し、`ResponseUtil#wrapOrNotFound` で応答に変換する。

| 条件 | ステータス | 応答本文 |
| --- | --- | --- |
| 該当あり | 200 | `Operation` |
| 該当なし | 404 | Problem Details |

---

## 11　処理詳細（DELETE /api/operations/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | deleteOperation | 削除 |

### 処理概要

取引を削除する。

### 処理詳細

対象メソッドは `OperationResource#deleteOperation(@PathVariable("id") Long id)` である。

#### １）削除する。

`OperationRepository#deleteById(id)` で削除する。

#### ２）応答を返す。

ステータス 204、応答本文なし。ヘッダ `X-jhipsterSampleApplicationApp-alert: operation.deleted`、`X-jhipsterSampleApplicationApp-params: {id}` を返す。

---

## 12　エラー一覧

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | OperationResource | 取引管理 | エラー一覧 | 検索・登録・更新・削除 |

### 処理詳細

エラー応答の本文は RFC 7807（Problem Details）形式である。変換規則は「01_共通仕様書 例外処理設計書」を参照。

| No | HTTPメソッド | 発生条件 | ステータス | `type` | `message` | 根拠 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST、PUT | 単項目チェックエラー | 400 | `constraint-violation` | `error.validation` | POST の場合は `OperationResourceIT#checkDateIsRequired`、`checkAmountIsRequired`。PUT の場合はコード（`@Valid`） |
| 2 | POST | リクエスト本文に `id` が設定されている | 400 | `problem-with-message` | `error.idexists` | `OperationResourceIT#createOperationWithExistingId` |
| 3 | PUT、PATCH | リクエスト本文に `id` がない | 400 | `problem-with-message` | `error.idnull` | コード（`BadRequestAlertException`） |
| 4 | PUT、PATCH | パス変数の `id` と本文の `id` が不一致 | 400 | `problem-with-message` | `error.idinvalid` | `OperationResourceIT#putWithIdMismatchOperation`、`patchWithIdMismatchOperation` |
| 5 | PUT、PATCH | `id` に該当する取引がない | 400 | `problem-with-message` | `error.idnotfound` | `OperationResourceIT#putNonExistingOperation`、`patchNonExistingOperation` |
| 6 | PUT、PATCH | `/api/operations` に `id` を付けずに呼び出した | 405 | `problem-with-message` | `error.http.405` | `OperationResourceIT#putWithMissingIdPathParamOperation`、`patchWithMissingIdPathParamOperation` |
| 7 | GET（一件） | `id` に該当する取引がない | 404 | `problem-with-message` | `error.http.404` | `OperationResourceIT#getNonExistingOperation` |
| 8 | 全エンドポイント | 未認証、または JWT が無効 | 401 | － | 本文なし | コード（`SecurityConfiguration`） |

---
