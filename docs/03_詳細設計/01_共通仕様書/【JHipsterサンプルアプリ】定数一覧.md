# 【JHipsterサンプルアプリ】定数一覧

## 改版履歴

| **版数** | **改版日付** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- |
| 1.0 | 2026/11/23 | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　アプリケーション定数（Constants） |
| 2 | 2　権限定数（AuthoritiesConstants） |
| 3 | 3　JWT 関連の定数（SecurityUtils） |
| 4 | 4　エラー応答の定数（ErrorConstants） |
| 5 | 5　リソースクラスの定数 |
| 6 | 6　DTO・VM の定数 |
| 7 | 7　リポジトリとサービスの定数 |
| 8 | 8　監視の定数（SecurityMetersService） |
| 9 | 9　例外処理の定数（ExceptionTranslator） |
| 10 | 10　ログの定数（CRLFLogConverter） |

---

## 1　アプリケーション定数（Constants）

| 定義クラス | ファイルパス（リポジトリルート基準） |
| --- | --- |
| `Constants` | `src/main/java/io/github/jhipster/sample/config/Constants.java` |

| **定数名** | **型** | **値** | **説明** |
| --- | --- | --- | --- |
| `LOGIN_REGEX` | String | 下記の正規表現 | ログインIDとして使用できる文字のパターン |
| `SYSTEM` | String | `system` | 監査者として記録する既定値（未認証のとき） |
| `DEFAULT_LANGUAGE` | String | `en` | ユーザ作成時の既定の言語キー |

`LOGIN_REGEX` の値は次のとおりである。

```text
^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$
```

半角英数字と記号（`_ . @ -`）のみ、またはメールアドレス形式を許容する。

---

## 2　権限定数（AuthoritiesConstants）

| 定義クラス | ファイルパス（リポジトリルート基準） |
| --- | --- |
| `AuthoritiesConstants` | `src/main/java/io/github/jhipster/sample/security/AuthoritiesConstants.java` |

| **定数名** | **型** | **値** | **説明** |
| --- | --- | --- | --- |
| `ADMIN` | String | `ROLE_ADMIN` | 管理者権限 |
| `USER` | String | `ROLE_USER` | 一般ユーザ権限 |
| `ANONYMOUS` | String | `ROLE_ANONYMOUS` | 匿名ユーザの権限（認証済みかどうかの判定に使う） |

`ROLE_ANONYMOUS` はテーブル `jhi_authority` の初期データには含まれない。

---

## 3　JWT 関連の定数（SecurityUtils）

| 定義クラス | ファイルパス（リポジトリルート基準） |
| --- | --- |
| `SecurityUtils` | `src/main/java/io/github/jhipster/sample/security/SecurityUtils.java` |

| **定数名** | **型** | **値** | **説明** |
| --- | --- | --- | --- |
| `JWT_ALGORITHM` | `MacAlgorithm` | `HS512` | JWT の署名アルゴリズム |
| `AUTHORITIES_CLAIM` | String | `auth` | JWT の権限を格納するクレーム名 |
| `USER_ID_CLAIM` | String | `userId` | JWT のユーザIDを格納するクレーム名 |

---

## 4　エラー応答の定数（ErrorConstants）

| 定義クラス | ファイルパス（リポジトリルート基準） |
| --- | --- |
| `ErrorConstants` | `src/main/java/io/github/jhipster/sample/web/rest/errors/ErrorConstants.java` |

| **定数名** | **型** | **値** | **説明** |
| --- | --- | --- | --- |
| `ERR_CONCURRENCY_FAILURE` | String | `error.concurrencyFailure` | 排他制御エラーの `message` |
| `ERR_VALIDATION` | String | `error.validation` | 入力チェックエラーの `message` |
| `PROBLEM_BASE_URL` | String | `https://www.jhipster.tech/problem` | エラー種別の URI の基底 |
| `DEFAULT_TYPE` | URI | `https://www.jhipster.tech/problem/problem-with-message` | エラー種別（既定） |
| `CONSTRAINT_VIOLATION_TYPE` | URI | `https://www.jhipster.tech/problem/constraint-violation` | エラー種別（入力チェックエラー） |
| `INVALID_PASSWORD_TYPE` | URI | `https://www.jhipster.tech/problem/invalid-password` | エラー種別（パスワードエラー） |
| `EMAIL_ALREADY_USED_TYPE` | URI | `https://www.jhipster.tech/problem/email-already-used` | エラー種別（メールアドレスの重複） |
| `LOGIN_ALREADY_USED_TYPE` | URI | `https://www.jhipster.tech/problem/login-already-used` | エラー種別（ログインIDの重複） |

各 URI の末尾の要素（`problem-with-message` 等）は、本設計書ではエラー応答の `type` の略称として使う。

---

## 5　リソースクラスの定数

| No | 定数名 | 型 | 値 | 定義クラス |
| --- | --- | --- | --- | --- |
| 1 | `ENTITY_NAME` | String | `adminAuthority` | `AuthorityResource` |
| 2 | `ENTITY_NAME` | String | `bankAccount` | `BankAccountResource` |
| 3 | `ENTITY_NAME` | String | `operation` | `OperationResource` |
| 4 | `ENTITY_NAME` | String | `label` | `LabelResource` |
| 5 | `ALLOWED_ORDERED_PROPERTIES` | List\<String\> | `id`、`login`、`firstName`、`lastName`、`email`、`activated`、`langKey`、`createdBy`、`createdDate`、`lastModifiedBy`、`lastModifiedDate` | `UserResource` |
| 6 | `ALLOWED_ORDERED_PROPERTIES` | List\<String\> | `id`、`login`、`firstName`、`lastName`、`email`、`activated`、`langKey` | `PublicUserResource` |
| 7 | `applicationName` の既定値 | String | `jhipsterSampleApplication` | `UserResource`、`AuthorityResource`、`BankAccountResource`、`OperationResource`、`LabelResource`、`ExceptionTranslator`（`@Value` の既定値。実際の値は設定 `jhipster.clientApp.name`） |

リソースクラスがアラートヘッダに設定するメッセージキーは、コード中の文字列リテラルである。

| No | メッセージキー | 設定箇所 | 用途 |
| --- | --- | --- | --- |
| 1 | `userManagement.created`、`userManagement.updated`、`userManagement.deleted` | `UserResource` | ユーザの登録、更新、削除 |
| 2 | `adminAuthority.created`、`adminAuthority.deleted` | `AuthorityResource` | 権限の登録、削除 |
| 3 | `bankAccount.created`、`bankAccount.updated`、`bankAccount.deleted` | `BankAccountResource` | 口座の登録、更新、削除 |
| 4 | `operation.created`、`operation.updated`、`operation.deleted` | `OperationResource` | 取引の登録、更新、削除 |
| 5 | `label.created`、`label.updated`、`label.deleted` | `LabelResource` | ラベルの登録、更新、削除 |
| 6 | `userManagement` | `UserResource`、`web.rest.errors.EmailAlreadyUsedException`、`web.rest.errors.LoginAlreadyUsedException` | エラー応答の `params`（エンティティ名） |

エラーキー（`idexists`、`idnull`、`idinvalid`、`idnotfound`、`userexists`、`emailexists`）もコード中の文字列リテラルである。詳細は「メッセージ」を参照。

---

## 6　DTO・VM の定数

| 定数名 | 型 | 値 | 定義クラス | 説明 |
| --- | --- | --- | --- | --- |
| `PASSWORD_MIN_LENGTH` | int | 4 | `ManagedUserVM` | パスワードの最小桁数 |
| `PASSWORD_MAX_LENGTH` | int | 100 | `ManagedUserVM` | パスワードの最大桁数 |

---

## 7　リポジトリとサービスの定数

| No | 定数名 | 型 | 値 | 定義クラス | 説明 |
| --- | --- | --- | --- | --- | --- |
| 1 | `USERS_BY_LOGIN_CACHE` | String | `usersByLogin` | `UserRepository` | ログインIDによる検索のキャッシュ名 |
| 2 | `USERS_BY_EMAIL_CACHE` | String | `usersByEmail` | `UserRepository` | メールアドレスによる検索のキャッシュ名 |
| 3 | `ID_PARAMETER` | String | `id` | `OperationRepositoryWithBagRelationshipsImpl` | JPQL のパラメータ名 |
| 4 | `OPERATIONS_PARAMETER` | String | `operations` | `OperationRepositoryWithBagRelationshipsImpl` | JPQL のパラメータ名 |
| 5 | `USER` | String | `user` | `MailService` | メールテンプレートの変数名 |
| 6 | `BASE_URL` | String | `baseUrl` | `MailService` | メールテンプレートの変数名 |

`UserService` にコード中のリテラルとして定義されている値は次のとおりである。

| No | 値 | 箇所 | 説明 |
| --- | --- | --- | --- |
| 1 | `0 0 1 * * ?` | `@Scheduled` の `cron` | 未有効化ユーザ一括削除の実行時刻（毎日 01:00） |
| 2 | `3`（`ChronoUnit.DAYS`） | `removeNotActivatedUsers` | 未有効化ユーザを削除するまでの日数 |
| 3 | `1`（`ChronoUnit.DAYS`） | `completePasswordReset` | リセットキーの有効期間 |

---

## 8　監視の定数（SecurityMetersService）

| 定義クラス | ファイルパス（リポジトリルート基準） |
| --- | --- |
| `SecurityMetersService` | `src/main/java/io/github/jhipster/sample/management/SecurityMetersService.java` |

| **定数名** | **型** | **値** | **説明** |
| --- | --- | --- | --- |
| `INVALID_TOKENS_METER_NAME` | String | `security.authentication.invalid-tokens` | JWT の検証エラーを数えるカウンタの名前 |
| `INVALID_TOKENS_METER_DESCRIPTION` | String | `Indicates validation error count of the tokens presented by the clients.` | カウンタの説明 |
| `INVALID_TOKENS_METER_BASE_UNIT` | String | `errors` | カウンタの単位 |
| `INVALID_TOKENS_METER_CAUSE_DIMENSION` | String | `cause` | エラー原因のタグ名 |

タグ `cause` の値は `invalid-signature`、`expired`、`unsupported`、`malformed` の 4 つである。

---

## 9　例外処理の定数（ExceptionTranslator）

| 定義クラス | ファイルパス（リポジトリルート基準） |
| --- | --- |
| `ExceptionTranslator` | `src/main/java/io/github/jhipster/sample/web/rest/errors/ExceptionTranslator.java` |

| **定数名** | **型** | **値** | **説明** |
| --- | --- | --- | --- |
| `FIELD_ERRORS_KEY` | String | `fieldErrors` | 入力チェックエラーの明細を格納するプロパティ名 |
| `MESSAGE_KEY` | String | `message` | メッセージキーを格納するプロパティ名 |
| `PATH_KEY` | String | `path` | リクエストパスを格納するプロパティ名 |
| `CAUSAL_CHAIN_ENABLED` | boolean | `false` | エラー応答に原因となった例外の連鎖を含めない |

`ExceptionTranslator` にコード中のリテラルとして定義されている値は次のとおりである。

| No | 値 | 用途 |
| --- | --- | --- |
| 1 | `error.http.` | ステータスごとのメッセージキーの接頭辞（`error.http.400` 等） |
| 2 | `Method argument not valid` | 入力チェックエラーの `title` |
| 3 | `Unable to convert http message` | `prod` のときの `HttpMessageConversionException` の `detail` |
| 4 | `Failure during data access` | `prod` のときの `DataAccessException` の `detail` |
| 5 | `Unexpected runtime exception` | `prod` のときにメッセージへパッケージ名が含まれる場合の `detail` |

---

## 10　ログの定数（CRLFLogConverter）

| 定義クラス | ファイルパス（リポジトリルート基準） |
| --- | --- |
| `CRLFLogConverter` | `src/main/java/io/github/jhipster/sample/config/CRLFLogConverter.java` |

| **定数名** | **型** | **値** | **説明** |
| --- | --- | --- | --- |
| `CRLF_SAFE_MARKER` | Marker | `CRLF_SAFE` | このマーカーを付けたログは、改行文字を置き換えない |
| `SAFE_LOGS` | String[] | `org.hibernate`、`org.springframework.boot.autoconfigure`、`org.springframework.boot.diagnostics` | これらの名前で始まるロガーのログは、改行文字を置き換えない |

---
