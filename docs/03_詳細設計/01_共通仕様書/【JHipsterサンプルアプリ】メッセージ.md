# 【JHipsterサンプルアプリ】メッセージ

## 改版履歴

| **版数** | **改版日付** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- |
| 1.0 | 2026/11/23 | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　メッセージリソース一覧 |
| 2 | 2　エラー応答のメッセージ一覧 |
| 3 | 3　アラートのメッセージ一覧 |
| 4 | 4　例外クラスの固定メッセージ |

本リポジトリのメッセージはすべて英語である。日本語のメッセージリソースはない。エラー応答とアラートは、メッセージの文字列ではなくメッセージキー（`error.*` 等）を返し、文言はフロントエンド側で決める。

---

## 1　メッセージリソース一覧

| 項目 | 内容 |
| --- | --- |
| ファイル | `src/main/resources/i18n/messages.properties`、`src/main/resources/i18n/messages_en.properties` |
| 基底名 | `i18n/messages`（設定 `spring.messages.basename`） |
| 備考 | 2 つのファイルの内容は同じである |

| **キー** | **標準定義** | **用途** |
| --- | --- | --- |
| `error.title` | Your request cannot be processed | エラー画面のタイトル（`templates/error.html`） |
| `error.subtitle` | Sorry, an error has occurred. | エラー画面の副題 |
| `error.status` | Status: | エラー画面のステータスのラベル |
| `error.message` | Message: | エラー画面のメッセージのラベル |
| `email.activation.title` | jhipsterSampleApplication account activation | アカウント有効化メールと作成通知メールの件名 |
| `email.activation.greeting` | Dear {0} | 挨拶（{0} はログインID） |
| `email.activation.text1` | Your jhipsterSampleApplication account has been created, please click on the URL below to activate it: | アカウント有効化メールの本文 |
| `email.activation.text2` | Regards, | 署名の前置き |
| `email.signature` | jhipsterSampleApplication Team. | 署名 |
| `email.creation.text1` | Your jhipsterSampleApplication account has been created, please click on the URL below to access it: | 作成通知メールの本文 |
| `email.reset.title` | jhipsterSampleApplication password reset | パスワードリセットメールの件名 |
| `email.reset.greeting` | Dear {0} | 挨拶（{0} はログインID） |
| `email.reset.text1` | For your jhipsterSampleApplication account a password reset was requested, please click on the URL below to reset it: | パスワードリセットメールの本文 |
| `email.reset.text2` | Regards, | 署名の前置き |

Java のコードから直接参照するのは件名の 2 キー（`email.activation.title`、`email.reset.title`）である（`MailService`）。残りのキーはテンプレート（Thymeleaf の `#{キー}`）から参照する。メッセージの言語はユーザの `langKey` から決める。詳細は「メール送信設計書」を参照。

テスト用には `src/test/resources/i18n/messages_en.properties` があり、`email.test.title` を定義している。

---

## 2　エラー応答のメッセージ一覧

エラー応答の `message` プロパティに設定するキーである。対応する文言のリソースは定義していない。

| **キー** | **発生条件** | **`type`（略称）** | **`params`** |
| --- | --- | --- | --- |
| `error.validation` | Bean Validation による入力チェックエラー | `constraint-violation` | － |
| `error.concurrencyFailure` | 排他制御の失敗（`ConcurrencyFailureException`） | `problem-with-message` | － |
| `error.idexists` | 登録時に `id` が指定されている、または同一キーのデータが既に存在する | `problem-with-message` | エンティティ名 |
| `error.idnull` | 更新時にリクエスト本文に `id` がない | `problem-with-message` | エンティティ名 |
| `error.idinvalid` | 更新時にパス変数の `id` とリクエスト本文の `id` が不一致 | `problem-with-message` | エンティティ名 |
| `error.idnotfound` | 更新対象の `id` に該当するデータがない | `problem-with-message` | エンティティ名 |
| `error.userexists` | ログインIDが他ユーザと重複 | `login-already-used` | `userManagement` |
| `error.emailexists` | メールアドレスが他ユーザと重複 | `email-already-used` | `userManagement` |
| `error.http.400` | 上記以外の 400（パスワードの桁数エラー、現在パスワードの不一致、有効化キーやリセットキーの該当なし、Spring MVC 標準の入力エラー等） | 状況により異なる | － |
| `error.http.401` | 認証失敗（ログインIDまたはパスワードの誤り） | `problem-with-message` | － |
| `error.http.403` | リソースクラス内でアクセスが拒否された（`@PreAuthorize`） | `problem-with-message` | － |
| `error.http.404` | 取得対象が存在しない（`ResponseUtil#wrapOrNotFound`） | `problem-with-message` | － |
| `error.http.405` | サポートしない HTTP メソッド | `problem-with-message` | － |
| `error.http.500` | 上記以外の例外（未処理のエラー） | `problem-with-message` | － |

`error.http.{ステータス}` は、例外に固有のメッセージキーがない場合に `ExceptionTranslator` がステータスから組み立てる。セキュリティフィルタチェインで拒否された未認証（401）と URL による権限不足（403）は、応答本文を持たないためメッセージキーもない。詳細は「例外処理設計書」を参照。

---

## 3　アラートのメッセージ一覧

登録、更新、削除が成功したときに、応答ヘッダ `X-jhipsterSampleApplicationApp-alert` に設定するキーである。対応する文言のリソースは定義していない。

| **キー** | **発生条件** | **`X-jhipsterSampleApplicationApp-params`** |
| --- | --- | --- |
| `userManagement.created` | `POST /api/admin/users` が成功 | ログインID（URL エンコード済み） |
| `userManagement.updated` | `PUT /api/admin/users` が成功 | ログインID（URL エンコード済み） |
| `userManagement.deleted` | `DELETE /api/admin/users/{login}` が成功 | ログインID（URL エンコード済み） |
| `adminAuthority.created` | `POST /api/authorities` が成功 | 権限名（URL エンコード済み） |
| `adminAuthority.deleted` | `DELETE /api/authorities/{id}` が成功 | 権限名（URL エンコード済み） |
| `bankAccount.created`、`bankAccount.updated`、`bankAccount.deleted` | 口座の登録、更新、削除が成功 | `id`（URL エンコード済み） |
| `operation.created`、`operation.updated`、`operation.deleted` | 取引の登録、更新、削除が成功 | `id`（URL エンコード済み） |
| `label.created`、`label.updated`、`label.deleted` | ラベルの登録、更新、削除が成功 | `id`（URL エンコード済み） |

ヘッダ名の `jhipsterSampleApplicationApp` は設定 `jhipster.clientApp.name` の値である。ヘッダの生成は `HeaderUtil`（JHipster ライブラリ）が行う。

---

## 4　例外クラスの固定メッセージ

例外クラスのコンストラクタに英語の文言を固定値として持つものである。これらの文言は、例外が持つエラー情報の `title` の初期値として設定される。`ExceptionTranslator` は `title` を独自の規則で決め直すため、応答の `title` にこれらの文言がそのまま現れるとは限らない。詳細は「例外処理設計書」を参照。

| **クラス** | **固定メッセージ** | **エラーキー** |
| --- | --- | --- |
| `web.rest.errors.EmailAlreadyUsedException` | Email is already in use! | `emailexists` |
| `web.rest.errors.LoginAlreadyUsedException` | Login name already used! | `userexists` |
| `web.rest.errors.InvalidPasswordException` | Incorrect password | － |
| `service.EmailAlreadyUsedException` | Email is already in use! | － |
| `service.UsernameAlreadyUsedException` | Login name already used! | － |
| `service.InvalidPasswordException` | Incorrect password | － |

`BadRequestAlertException` を生成するときに指定するメッセージは次のとおりである。

| **固定メッセージ** | **エンティティ名** | **エラーキー** | **指定箇所** |
| --- | --- | --- | --- |
| A new user cannot already have an ID | `userManagement` | `idexists` | `UserResource#createUser` |
| A new bankAccount cannot already have an ID | `bankAccount` | `idexists` | `BankAccountResource#createBankAccount` |
| A new operation cannot already have an ID | `operation` | `idexists` | `OperationResource#createOperation` |
| A new label cannot already have an ID | `label` | `idexists` | `LabelResource#createLabel` |
| authority already exists | `adminAuthority` | `idexists` | `AuthorityResource#createAuthority` |
| Invalid id | `bankAccount`、`operation`、`label` | `idnull` | 各リソースクラスの更新メソッド |
| Invalid ID | `bankAccount`、`operation`、`label` | `idinvalid` | 各リソースクラスの更新メソッド |
| Entity not found | `bankAccount`、`operation`、`label` | `idnotfound` | 各リソースクラスの更新メソッド |

`AccountResource` の内部クラス `AccountResourceException` に指定するメッセージは次のとおりである。いずれもステータス 400、`message` は `error.http.400` となる。

| **固定メッセージ** | **指定箇所** |
| --- | --- |
| No user was found for this activation key | `AccountResource#activateAccount` |
| User could not be found | `AccountResource#getAccount`、`AccountResource#saveAccount` |
| Current user login not found | `AccountResource#saveAccount` |
| No user was found for this reset key | `AccountResource#finishPasswordReset` |

`AccountResourceException` には `@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account resource request invalid")` を付与している。エラー応答の `title` はこの `reason` の値になる。

---
