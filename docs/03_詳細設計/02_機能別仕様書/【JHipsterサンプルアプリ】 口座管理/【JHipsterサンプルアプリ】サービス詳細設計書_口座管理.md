# 【JHipsterサンプルアプリ】サービス詳細設計書_口座管理

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 |  | 検索・登録・更新・削除 |

目次

| 項番 | シート名 |
| --- | --- |
| 1 | 処理概要 |
| 2 | 1　対象クラス |
| 3 | 2　API定義 |
| 4 | 3　単項目チェック |
| 5 | 4　相関項目チェック |
| 6 | 5　依存クラス |
| 7 | 6　処理詳細（POST /api/bank-accounts） |
| 8 | 7　処理詳細（PUT /api/bank-accounts/{id}） |
| 9 | 8　処理詳細（PATCH /api/bank-accounts/{id}） |
| 10 | 9　処理詳細（GET /api/bank-accounts） |
| 11 | 10　処理詳細（GET /api/bank-accounts/{id}） |
| 12 | 11　処理詳細（DELETE /api/bank-accounts/{id}） |
| 13 | 12　エラー一覧 |

---

## 処理概要

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 |  | 検索・登録・更新・削除 |

### 処理概要

| ・ | 銀行口座（口座名、残高）を検索、登録、更新、削除する。 |
| --- | --- |

コール元に対する補足説明

フロントエンド（別リポジトリ）が HTTP で呼び出す。認証済みであれば権限を問わず呼び出せる。本機能にはサービスクラスがなく、リソースクラスがリポジトリを直接使う。リクエスト・応答の本文には DTO を使わず、エンティティ `BankAccount` を直接使う。

---

## 1　対象クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | 対象クラス | 検索・登録・更新・削除 |

### 処理詳細

| No | クラス | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- | --- |
| 1 | `BankAccountResource` | `src/main/java/io/github/jhipster/sample/web/rest/BankAccountResource.java` | 口座管理のエンドポイント |
| 2 | `BankAccount` | `src/main/java/io/github/jhipster/sample/domain/BankAccount.java` | 口座のエンティティ。リクエスト・応答の本文としても使う |
| 3 | `BankAccountRepository` | `src/main/java/io/github/jhipster/sample/repository/BankAccountRepository.java` | 口座テーブルへのアクセス |
| 4 | `BadRequestAlertException` | `src/main/java/io/github/jhipster/sample/web/rest/errors/BadRequestAlertException.java` | ID の指定エラー |

---

## 2　API定義

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | API定義 | 検索・登録・更新・削除 |

### 処理詳細

| No | HTTPメソッド | URL | リソースクラス | メソッド | 認可 | リクエスト | 応答本文 | 正常時ステータス | トランザクション |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/bank-accounts` | BankAccountResource | `createBankAccount` | 認証済み | 本文 `BankAccount`（JSON） | `BankAccount` | 201 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 2 | PUT | `/api/bank-accounts/{id}` | BankAccountResource | `updateBankAccount` | 認証済み | パス変数 `id`、本文 `BankAccount`（JSON） | `BankAccount` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 3 | PATCH | `/api/bank-accounts/{id}` | BankAccountResource | `partialUpdateBankAccount` | 認証済み | パス変数 `id`、本文 `BankAccount`（JSON または `application/merge-patch+json`） | `BankAccount` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 4 | GET | `/api/bank-accounts` | BankAccountResource | `getAllBankAccounts` | 認証済み | クエリパラメータ `eagerload`（既定 `true`） | `BankAccount` の配列 | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 5 | GET | `/api/bank-accounts/{id}` | BankAccountResource | `getBankAccount` | 認証済み | パス変数 `id` | `BankAccount` | 200 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |
| 6 | DELETE | `/api/bank-accounts/{id}` | BankAccountResource | `deleteBankAccount` | 認証済み | パス変数 `id` | なし | 204 | リソースクラスがトランザクションを管理する（クラスレベルの `@Transactional(rollbackFor = Exception.class)`） |

リソースクラスに `@Transactional(rollbackFor = Exception.class)` を付与しており、全メソッドがトランザクションの中で実行される。

---

## 3　単項目チェック

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | 単項目チェック | 検索・登録・更新・削除 |

### 処理概要

リクエストデータ部の単項目チェックを実施する。

### 処理詳細

`POST` と `PUT` が対象である。チェックは `BankAccount` エンティティの Bean Validation アノテーションで行う。

| No | 項目 | 型 | 必須 | 桁数・精度 | アノテーション | 備考 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | Long | － | － | なし | 登録時は null とする（「4　相関項目チェック」） |
| 2 | `name` | String | ○ | 255 以内 | `@NotNull` | 口座名 |
| 3 | `balance` | BigDecimal | ○ | 整数部 19 桁、小数部 2 桁 | `@NotNull` | 残高 |
| 4 | `user` | `User` | － | － | なし | 所有ユーザ（関連） |
| 5 | `operations` | `Operation` の集合 | － | － | なし | 取引（関連） |

`PATCH` は本文に `@Valid` を付けていないため、項目のチェックは行わない（パラメータには `@NotNull` のみ付与している）。桁数・精度はエンティティの `@Column` とデータベース定義（`varchar(255)`、`decimal(21,2)`）による制約であり、アプリケーション側のチェックではない。

チェックエラーの場合、業務エラーと返す。

---

## 4　相関項目チェック

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | 相関項目チェック | 検索・登録・更新・削除 |

### 処理概要

ID の指定と存在をチェックする。

### 処理詳細

チェックはリソースクラス内のコードで行う。エンティティ名（`ENTITY_NAME`）は `bankAccount` である。

#### １）登録時（POST）

| No | チェック | 発生条件 | デフォルトメッセージ | エラーキー |
| --- | --- | --- | --- | --- |
| 1 | ID の指定 | リクエスト本文．`id` が null 以外 | `A new bankAccount cannot already have an ID` | `idexists` |

#### ２）更新時（PUT、PATCH）

| No | チェック | 発生条件 | デフォルトメッセージ | エラーキー |
| --- | --- | --- | --- | --- |
| 1 | ID の有無 | リクエスト本文．`id` が null | `Invalid id` | `idnull` |
| 2 | ID の一致 | パス変数．`id` とリクエスト本文．`id` が不一致 | `Invalid ID` | `idinvalid` |
| 3 | 口座の存在 | `BankAccountRepository#existsById(パス変数．id)` が false | `Entity not found` | `idnotfound` |

いずれの場合も `BadRequestAlertException` をスローし、業務エラー（400）とする。パス変数の `id` は `required = false` で宣言しているが、`/api/bank-accounts` に `id` を付けずに `PUT`、`PATCH` を呼び出した場合は該当するマッピングがなく、405 となる。

---

## 5　依存クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | 依存クラス | 検索・登録・更新・削除 |

### 処理概要

コンストラクタインジェクションで、本クラスが使う次の Bean を注入する。

### 処理詳細

| No | クラス | フィールド | 用途 |
| --- | --- | --- | --- |
| 1 | `BankAccountRepository` | `bankAccountRepository` | 口座の検索、保存、削除 |

`@Value("${jhipster.clientApp.name:jhipsterSampleApplication}")` で読み込む `applicationName` は、アラートヘッダの名前に使う。

`BankAccountRepository` の `save`、`findById`、`findAll`、`existsById`、`deleteById` は、`JpaRepository` から継承したメソッドである。リポジトリのインターフェースには宣言がない。

---

## 6　処理詳細（POST /api/bank-accounts）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | createBankAccount | 登録 |

### 処理概要

口座を新規登録する。

### 処理詳細

対象メソッドは `BankAccountResource#createBankAccount(@Valid @RequestBody BankAccount bankAccount)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）保存する。

`BankAccountRepository#save(bankAccount)` で保存する。`id` はシーケンス `sequence_generator` で採番する。

#### ３）応答を返す。

| 項目 | 内容 |
| --- | --- |
| ステータス | 201 |
| ヘッダ | `Location: /api/bank-accounts/{id}`、`X-jhipsterSampleApplicationApp-alert: bankAccount.created`、`X-jhipsterSampleApplicationApp-params: {id}` |
| 応答本文 | 登録した `BankAccount` の JSON |

---

## 7　処理詳細（PUT /api/bank-accounts/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | updateBankAccount | 更新 |

### 処理概要

口座を全項目更新する。

### 処理詳細

対象メソッドは `BankAccountResource#updateBankAccount(@PathVariable(value = "id", required = false) Long id, @Valid @RequestBody BankAccount bankAccount)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）保存する。

`BankAccountRepository#save(bankAccount)` で保存する。リクエスト本文のエンティティをそのまま保存するため、本文に含めない項目は上書きされる（部分更新ではない）。

#### ３）応答を返す。

| 項目 | 内容 |
| --- | --- |
| ステータス | 200 |
| ヘッダ | `X-jhipsterSampleApplicationApp-alert: bankAccount.updated`、`X-jhipsterSampleApplicationApp-params: {id}` |
| 応答本文 | 更新した `BankAccount` の JSON |

---

## 8　処理詳細（PATCH /api/bank-accounts/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | partialUpdateBankAccount | 更新 |

### 処理概要

口座を部分更新する。

### 処理詳細

対象メソッドは `BankAccountResource#partialUpdateBankAccount(@PathVariable(value = "id", required = false) Long id, @NotNull @RequestBody BankAccount bankAccount)` である。

#### １）相関項目チェックを行う。

「4　相関項目チェック」のとおり。

#### ２）既存の口座を取得し、指定のあった項目だけを更新する。

`BankAccountRepository#findById(リクエスト本文．id)` で既存の口座を取得し、リクエスト本文で null 以外の項目を設定する。

| No | 編集項目 | 編集仕様 | 備考 |
| --- | --- | --- | --- |
| 1 | `name` | リクエスト本文．`name` | null の場合は更新しない |
| 2 | `balance` | リクエスト本文．`balance` | null の場合は更新しない |

`user` と `operations` は部分更新の対象外である。

#### ３）保存して応答を返す。

`BankAccountRepository#save` で保存し、`ResponseUtil#wrapOrNotFound` で応答に変換する。

| 条件 | ステータス | ヘッダ | 応答本文 |
| --- | --- | --- | --- |
| 更新できた | 200 | `X-jhipsterSampleApplicationApp-alert: bankAccount.updated`、`X-jhipsterSampleApplicationApp-params: {id}` | 更新した `BankAccount` |
| 既存の口座が取得できない | 404 | なし | Problem Details |

---

## 9　処理詳細（GET /api/bank-accounts）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | getAllBankAccounts | 検索 |

### 処理概要

全口座を検索する。

### 処理詳細

対象メソッドは `BankAccountResource#getAllBankAccounts(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload)` である。

| No | 条件 | 取得メソッド | 備考 |
| --- | --- | --- | --- |
| 1 | `eagerload` が `true`（既定） | `BankAccountRepository#findAllWithEagerRelationships` | 所有ユーザ（`user`）を同時に読み込む |
| 2 | `eagerload` が `false` | `BankAccountRepository#findAll` | 関連は読み込まない |

ページングと抽出条件はない。

| 項目 | 内容 |
| --- | --- |
| ステータス | 200 |
| 応答本文 | `BankAccount` の配列（0 件の場合は空配列） |

---

## 10　処理詳細（GET /api/bank-accounts/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | getBankAccount | 検索 |

### 処理概要

口座を 1 件検索する。

### 処理詳細

対象メソッドは `BankAccountResource#getBankAccount(@PathVariable("id") Long id)` である。

`BankAccountRepository#findOneWithEagerRelationships(id)` で所有ユーザを含めて取得し、`ResponseUtil#wrapOrNotFound` で応答に変換する。

| 条件 | ステータス | 応答本文 |
| --- | --- | --- |
| 該当あり | 200 | `BankAccount` |
| 該当なし | 404 | Problem Details |

---

## 11　処理詳細（DELETE /api/bank-accounts/{id}）

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | deleteBankAccount | 削除 |

### 処理概要

口座を削除する。

### 処理詳細

対象メソッドは `BankAccountResource#deleteBankAccount(@PathVariable("id") Long id)` である。

#### １）削除する。

`BankAccountRepository#deleteById(id)` で削除する。

#### ２）応答を返す。

ステータス 204、応答本文なし。ヘッダ `X-jhipsterSampleApplicationApp-alert: bankAccount.deleted`、`X-jhipsterSampleApplicationApp-params: {id}` を返す。

取引（`operation`）が紐付いている口座を削除しようとすると、外部キー制約 `fk_operation__bank_account_id` に反するため 500 となる（動作確認済み。2026/11/24）。

---

## 12　エラー一覧

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | BankAccountResource | 口座管理 | エラー一覧 | 検索・登録・更新・削除 |

### 処理詳細

エラー応答の本文は RFC 7807（Problem Details）形式である。変換規則は「01_共通仕様書 例外処理設計書」を参照。

| No | HTTPメソッド | 発生条件 | ステータス | `type` | `message` | 根拠 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST、PUT | 単項目チェックエラー | 400 | `constraint-violation` | `error.validation` | POST の場合は `BankAccountResourceIT#checkNameIsRequired`、`checkBalanceIsRequired`。PUT の場合はコード（`@Valid`） |
| 2 | POST | リクエスト本文に `id` が設定されている | 400 | `problem-with-message` | `error.idexists` | `BankAccountResourceIT#createBankAccountWithExistingId` |
| 3 | PUT、PATCH | リクエスト本文に `id` がない | 400 | `problem-with-message` | `error.idnull` | コード（`BadRequestAlertException`） |
| 4 | PUT、PATCH | パス変数の `id` と本文の `id` が不一致 | 400 | `problem-with-message` | `error.idinvalid` | `BankAccountResourceIT#putWithIdMismatchBankAccount`、`patchWithIdMismatchBankAccount` |
| 5 | PUT、PATCH | `id` に該当する口座がない | 400 | `problem-with-message` | `error.idnotfound` | `BankAccountResourceIT#putNonExistingBankAccount`、`patchNonExistingBankAccount` |
| 6 | PUT、PATCH | `/api/bank-accounts` に `id` を付けずに呼び出した | 405 | `problem-with-message` | `error.http.405` | `BankAccountResourceIT#putWithMissingIdPathParamBankAccount`、`patchWithMissingIdPathParamBankAccount` |
| 7 | GET（一件） | `id` に該当する口座がない | 404 | `problem-with-message` | `error.http.404` | `BankAccountResourceIT#getNonExistingBankAccount` |
| 8 | 全エンドポイント | 未認証、または JWT が無効 | 401 | － | 本文なし | コード（`SecurityConfiguration`） |
| 9 | DELETE | 取引が紐付いている口座を削除した | 500 | `problem-with-message` | `error.http.500` | 動作確認（2026/11/24。`DataIntegrityViolationException`）。口座は削除されない |

---
