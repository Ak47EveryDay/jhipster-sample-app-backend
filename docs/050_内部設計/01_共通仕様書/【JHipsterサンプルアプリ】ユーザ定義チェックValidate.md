# 【JHipsterサンプルアプリ】ユーザ定義チェックValidate

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 機能一覧 |
| 2 | １．アノテーションによるチェック |
| 3 | ２．パスワードの桁数チェック |
| 4 | ３．メールアドレスの重複チェック |
| 5 | ４．ログインIDとメールアドレスの重複チェック（ユーザ登録） |
| 6 | ５．現在のパスワードの照合 |
| 7 | ６．リセットキーの有効期限チェック |
| 8 | ７．ID の指定・一致・存在チェック |
| 9 | ８．ソートプロパティの許可チェック |

本書では、入力チェックのうちコードで実装したチェックを「ユーザ定義チェック」と呼ぶ。アノテーションによる標準のチェック（Bean Validation）は機能一覧と「１」にまとめて記載する。各チェックの業務上の位置付けは、各機能の「サービス詳細設計書」を参照。

---

## 機能一覧

| No | 分類 | チェック名 | 対象 | 実装箇所 |
| --- | --- | --- | --- | --- |
| 1 | アノテーション | 必須チェック | DTO、VM、エンティティ | `@NotNull`、`@NotBlank` |
| 2 | アノテーション | 桁数チェック | DTO、VM、エンティティ | `@Size` |
| 3 | アノテーション | メールアドレス形式のチェック | DTO、VM | `@Email` |
| 4 | アノテーション | ログインIDの使用文字のチェック | `AdminUserDTO`、`User` | `@Pattern(regexp = Constants.LOGIN_REGEX)` |
| 5 | ユーザ定義 | パスワードの桁数チェック | ユーザ登録、パスワード変更、パスワードリセット完了 | `AccountResource#isPasswordLengthInvalid` |
| 6 | ユーザ定義 | メールアドレスの重複チェック | アカウント情報の更新 | `AccountResource#saveAccount` |
| 7 | ユーザ定義 | ログインIDとメールアドレスの重複チェック | ユーザ登録 | `UserService#registerUser` |
| 8 | ユーザ定義 | ログインIDとメールアドレスの重複チェック | ユーザ管理（登録、更新） | `UserResource#createUser`、`UserResource#updateUser` |
| 9 | ユーザ定義 | 現在のパスワードの照合 | パスワード変更 | `UserService#changePassword` |
| 10 | ユーザ定義 | リセットキーの有効期限チェック | パスワードリセット完了 | `UserService#completePasswordReset` |
| 11 | ユーザ定義 | ID の指定チェック（登録時） | ユーザ管理、口座、取引、ラベル、権限の登録 | `UserResource#createUser`、`AuthorityResource#createAuthority`、各リソースクラスの登録メソッド |
| 12 | ユーザ定義 | ID の有無・一致・存在チェック（更新時） | 口座、取引、ラベルの更新 | `BankAccountResource`、`OperationResource`、`LabelResource` の更新メソッド |
| 13 | ユーザ定義 | ソートプロパティの許可チェック | ユーザ一覧検索、公開ユーザ情報検索 | `UserResource#onlyContainsAllowedProperties`、`PublicUserResource#onlyContainsAllowedProperties` |

---

## １．アノテーションによるチェック

### 処理概要

リクエスト本文に `@Valid` を付けて受け取り、Bean Validation のアノテーションでチェックする。

### チェックエラー出力

`MethodArgumentNotValidException` となり、`ExceptionTranslator` がステータス 400 のエラー応答に変換する。`type` は `constraint-violation`、`message` は `error.validation`、`title` は `Method argument not valid` である。エラーの明細は `fieldErrors` の配列に格納する（各要素は `objectName`、`field`、`message`）。

### アノテーションの一覧

| クラス | 項目 | アノテーション | チェックを適用するエンドポイント |
| --- | --- | --- | --- |
| `AdminUserDTO` | `login` | `@NotBlank`、`@Pattern(regexp = Constants.LOGIN_REGEX)`、`@Size(min = 1, max = 50)` | `POST /api/account`、`POST /api/admin/users`、`PUT /api/admin/users` |
| `AdminUserDTO` | `firstName`、`lastName` | `@Size(max = 50)` | 同上 |
| `AdminUserDTO` | `email` | `@Email`、`@Size(min = 5, max = 254)` | 同上 |
| `AdminUserDTO` | `imageUrl` | `@Size(max = 256)` | 同上 |
| `AdminUserDTO` | `langKey` | `@Size(min = 2, max = 10)` | 同上 |
| `ManagedUserVM` | `password` | `@Size(min = 4, max = 100)` | `POST /api/register` |
| `LoginVM` | `username` | `@NotNull`、`@Size(min = 1, max = 254)` | `POST /api/authenticate` |
| `LoginVM` | `password` | `@NotNull`、`@Size(min = 4, max = 100)` | `POST /api/authenticate` |
| `Authority` | `name` | `@NotNull`、`@Size(max = 50)` | `POST /api/authorities` |
| `BankAccount` | `name`、`balance` | `@NotNull` | `POST /api/bank-accounts`、`PUT /api/bank-accounts/{id}` |
| `Operation` | `date`、`amount` | `@NotNull` | `POST /api/operations`、`PUT /api/operations/{id}` |
| `Label` | `label` | `@NotNull`、`@Size(min = 3)` | `POST /api/labels`、`PUT /api/labels/{id}` |
| リクエスト本文（文字列） | メールアドレス | `@Email`、`@Size(min = 5, max = 254)` | `POST /api/account/reset-password/init` |

最後の行は、メソッドの引数に直接アノテーションを付けている。このチェックを有効にするため、`AccountResource` のクラスに `@Validated` を付与している。この場合の例外は `ConstraintViolationException` であり、`ExceptionTranslator` は同じ形式の 400 に変換する。

エンティティ `User` にも `login`、`password`、`email` などのアノテーションを宣言しているが、主コードには `@Valid` を付けて `User` をリクエスト本文として受け取る箇所はない。`PATCH` のリクエスト本文には `@Valid` を付けていないため、項目のチェックは行わない。

---

## ２．パスワードの桁数チェック

### アノテーション引数

なし（コードによるチェックである）。

### 処理概要

`AccountResource#isPasswordLengthInvalid(String password)` が、パスワードが空文字、`ManagedUserVM.PASSWORD_MIN_LENGTH`（4）未満、または `ManagedUserVM.PASSWORD_MAX_LENGTH`（100）超の場合に true を返す。

### チェックエラー出力

`web.rest.errors.InvalidPasswordException` をスローする。ステータス 400、`type` は `invalid-password`、`message` は `error.http.400` である。

### 利用方法

| No | エンドポイント | 対象の項目 |
| --- | --- | --- |
| 1 | `POST /api/register` | `password` |
| 2 | `POST /api/account/change-password` | `newPassword` |
| 3 | `POST /api/account/reset-password/finish` | `newPassword` |

---

## ３．メールアドレスの重複チェック

### 処理概要

`AccountResource#saveAccount` が、`UserRepository#findOneByEmailIgnoreCase` でリクエスト本文のメールアドレスを検索する。検索結果があり、かつ抽出したユーザの `login` が現在ユーザと異なる場合にエラーとする。

### チェックエラー出力

`web.rest.errors.EmailAlreadyUsedException` をスローする。ステータス 400、`type` は `email-already-used`、`message` は `error.emailexists`、`params` は `userManagement` である。

### 利用方法

`POST /api/account`。

---

## ４．ログインIDとメールアドレスの重複チェック（ユーザ登録）

### 処理概要

`UserService#registerUser` が、次の 2 つの検索を行う。

| No | 検索メソッド | 条件 | 検索結果が未有効化ユーザの場合 |
| --- | --- | --- | --- |
| 1 | `UserRepository#findOneByLogin` | リクエスト本文の `login` を小文字化した値 | 既存のユーザを削除して登録を続行する |
| 2 | `UserRepository#findOneByEmailIgnoreCase` | リクエスト本文の `email` | 同上 |

検索結果が有効化済みユーザの場合にエラーとする。

### チェックエラー出力

| No | 条件 | 例外（サービス層） | エラー応答の `type` | `message` |
| --- | --- | --- | --- | --- |
| 1 | ログインIDが有効化済みユーザと重複 | `service.UsernameAlreadyUsedException` | `login-already-used` | `error.userexists` |
| 2 | メールアドレスが有効化済みユーザと重複 | `service.EmailAlreadyUsedException` | `email-already-used` | `error.emailexists` |

いずれもステータス 400、`params` は `userManagement` である。

### 利用方法

`POST /api/register`。

---

## ５．現在のパスワードの照合

### 処理概要

`UserService#changePassword` が、`PasswordEncoder#matches(リクエスト本文の currentPassword, 登録済みのハッシュ)` で照合する。

### チェックエラー出力

一致しない場合、`service.InvalidPasswordException` をスローする。ステータス 400、`type` は `invalid-password`、`message` は `error.http.400` である。

### 利用方法

`POST /api/account/change-password`。

---

## ６．リセットキーの有効期限チェック

### 処理概要

`UserService#completePasswordReset` が、`UserRepository#findOneByResetKey` で検索したユーザの `resetDate` が現在日時の 1 日前より後であることを確認する。

### チェックエラー出力

検索結果がない場合と、有効期限を過ぎている場合を区別しない。いずれも `AccountResource#finishPasswordReset` が `AccountResourceException("No user was found for this reset key")` をスローする。ステータス 400、`message` は `error.http.400` である。

### 利用方法

`POST /api/account/reset-password/finish`。

---

## ７．ID の指定・一致・存在チェック

### 処理概要

| No | 処理 | 登録時 | 更新時（PUT、PATCH） |
| --- | --- | --- | --- |
| 1 | ID の指定 | リクエスト本文の `id` が null 以外の場合はエラー | － |
| 2 | ID の有無 | － | リクエスト本文の `id` が null の場合はエラー |
| 3 | ID の一致 | － | パス変数の `id` とリクエスト本文の `id` が不一致の場合はエラー |
| 4 | ID の存在 | 権限のみ実施。同一の権限名が既に存在する場合はエラー（`AuthorityResource#createAuthority` の `existsById`） | `existsById(パス変数の id)` が false の場合はエラー |

### チェックエラー出力

いずれも `BadRequestAlertException` をスローする。ステータス 400、`type` は `problem-with-message` である。`message` と `params` は次のとおりである。

| No | 処理 | `message` | `params` |
| --- | --- | --- | --- |
| 1 | ID の指定 | `error.idexists` | エンティティ名（`userManagement`、`bankAccount`、`operation`、`label`、`adminAuthority`） |
| 2 | ID の有無 | `error.idnull` | エンティティ名 |
| 3 | ID の一致 | `error.idinvalid` | エンティティ名 |
| 4 | ID の存在 | `error.idnotfound` | エンティティ名 |

応答ヘッダには、`X-jhipsterSampleApplicationApp-error` に `message` と同じ値、`X-jhipsterSampleApplicationApp-params` に `params` と同じ値を設定する。

### 利用方法

| No | クラス | エンドポイント |
| --- | --- | --- |
| 1 | `UserResource` | `POST /api/admin/users` |
| 2 | `AuthorityResource` | `POST /api/authorities` |
| 3 | `BankAccountResource` | `POST /api/bank-accounts`、`PUT`、`PATCH /api/bank-accounts/{id}` |
| 4 | `OperationResource` | `POST /api/operations`、`PUT`、`PATCH /api/operations/{id}` |
| 5 | `LabelResource` | `POST /api/labels`、`PUT`、`PATCH /api/labels/{id}` |

---

## ８．ソートプロパティの許可チェック

### 処理概要

ページング付きの一覧検索で、`sort` に指定されたすべてのプロパティが許可リストに含まれることを確認する。

| No | クラス | 許可リスト（`ALLOWED_ORDERED_PROPERTIES`） |
| --- | --- | --- |
| 1 | `UserResource` | `id`、`login`、`firstName`、`lastName`、`email`、`activated`、`langKey`、`createdBy`、`createdDate`、`lastModifiedBy`、`lastModifiedDate` |
| 2 | `PublicUserResource` | `id`、`login`、`firstName`、`lastName`、`email`、`activated`、`langKey` |

### チェックエラー出力

許可されていないプロパティを指定した場合、リソースクラスがステータス 400 を直接返す。応答本文はない（Problem Details ではない）。

### 利用方法

`GET /api/admin/users`、`GET /api/users`。

---
