# 【JHipsterサンプルアプリ】共通Utilクラス

## 改版履歴

| **版数** | **改版日付** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- |
| 1.0 | 2026/11/23 | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 共通処理一覧 |
| 2 | 処理詳細１．セキュリティユーティリティ（SecurityUtils） |
| 3 | 処理詳細２．監査者の取得（SpringSecurityAuditorAware） |
| 4 | 処理詳細３．改行文字の置き換え（CRLFLogConverter） |
| 5 | 処理詳細４．JHipster ライブラリのユーティリティ |

---

## 共通処理一覧

共通のユーティリティは、本リポジトリのクラスと JHipster ライブラリ（`jhipster-framework`）のクラスの 2 種類がある。

| No | 処理名 | クラス | 区分 | 主な利用箇所 |
| --- | --- | --- | --- | --- |
| 1 | 現在ユーザのログインIDの取得 | `SecurityUtils` | 本リポジトリ | `AccountResource`、`UserService`、`SpringSecurityAuditorAware` |
| 2 | JWT のクレーム名と署名アルゴリズムの定義 | `SecurityUtils` | 本リポジトリ | `AuthenticateController`、`SecurityJwtConfiguration` |
| 3 | 監査者の取得 | `SpringSecurityAuditorAware` | 本リポジトリ | Spring Data JPA の監査機能 |
| 4 | ログの改行文字の置き換え | `CRLFLogConverter` | 本リポジトリ | `logback-spring.xml` のパターン（`%crlf`） |
| 5 | アラートヘッダの生成 | `HeaderUtil` | JHipster ライブラリ | `UserResource`、`AuthorityResource`、`BankAccountResource`、`OperationResource`、`LabelResource`、`ExceptionTranslator` |
| 6 | ページングヘッダの生成 | `PaginationUtil` | JHipster ライブラリ | `UserResource`、`PublicUserResource`、`OperationResource` |
| 7 | 検索結果の 404 変換 | `ResponseUtil` | JHipster ライブラリ | `UserResource`、`AuthorityResource`、`BankAccountResource`、`OperationResource`、`LabelResource` |
| 8 | ランダムな文字列の生成 | `RandomUtil` | JHipster ライブラリ | `UserService` |
| 9 | エラー情報の生成 | `ProblemDetailWithCause` | JHipster ライブラリ | `ExceptionTranslator`、`BadRequestAlertException`、`InvalidPasswordException` |
| 10 | 既定プロファイルの設定 | `DefaultProfileUtil` | JHipster ライブラリ | `JhipsterSampleApplicationApp`、`ApplicationWebXml` |
| 11 | Liquibase の生成 | `SpringLiquibaseUtil` | JHipster ライブラリ | `LiquibaseConfiguration` |
| 12 | H2 コンソールの初期化 | `H2ConfigurationHelper` | JHipster ライブラリ | `WebConfigurer`、`DatabaseConfiguration` |
| 13 | 非同期実行の例外処理 | `ExceptionHandlingAsyncTaskExecutor` | JHipster ライブラリ | `AsyncConfiguration` |
| 14 | キーの生成 | `PrefixedKeyGenerator` | JHipster ライブラリ | `CacheKeyGeneratorConfiguration` |
| 15 | ログ出力先の追加 | `LoggingUtils` | JHipster ライブラリ | `LoggingConfiguration` |
| 16 | アプリケーション設定の読み込み | `JHipsterProperties` | JHipster ライブラリ | `SecurityConfiguration`、`WebConfigurer`、`CacheConfiguration`、`MailService`、`LoggingConfiguration` |
| 17 | プロファイル名の定数 | `JHipsterConstants` | JHipster ライブラリ | `JhipsterSampleApplicationApp`、`SecurityConfiguration`、`DatabaseConfiguration`、`LoggingAspect`、`ExceptionTranslator`、`LiquibaseConfiguration`、`LoggingAspectConfiguration`、`WebConfigurer` |

---

## 処理詳細１．セキュリティユーティリティ（SecurityUtils）

| 項目 | 内容 |
| --- | --- |
| クラス | `io.github.jhipster.sample.security.SecurityUtils` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/security/SecurityUtils.java` |
| 種別 | `final` クラス。static メソッドのみ（インスタンス化しない） |

### 処理詳細

現在のリクエストの認証情報は `SecurityContextHolder` から取得する。REST API での認証プリンシパルは JWT（`Jwt`）である。ログインのリクエスト処理中だけは、`AuthenticateController` が認証情報を直接設定するため、プリンシパルが `UserWithId`（`UserDetails` の実装）となる。

| メソッド | 戻り値 | 処理 |
| --- | --- | --- |
| `getCurrentUserLogin()` | `Optional<String>` | 認証情報がない、または認証プリンシパルが匿名ユーザの場合は空を返す。プリンシパルが `UserDetails` の場合は `getUsername()`、`Jwt` の場合は `getSubject()`、`String` の場合はその値を返す |
| `getCurrentUserJWT()` | `Optional<String>` | 認証情報の資格情報が `String` の場合はその値、`Jwt` の場合は `getTokenValue()` を返す。それ以外は空を返す |
| `getCurrentUserId()` | `Optional<Long>` | プリンシパルが `ClaimAccessor` を実装している場合、クレーム `userId` の値を返す。それ以外は空を返す |
| `isAuthenticated()` | `boolean` | 認証情報があり、かつ権限に `ROLE_ANONYMOUS` が含まれない場合に true を返す |
| `hasCurrentUserAnyOfAuthorities(String... authorities)` | `boolean` | 引数の権限のいずれかを現在ユーザが持つ場合に true を返す |
| `hasCurrentUserNoneOfAuthorities(String... authorities)` | `boolean` | 引数の権限を現在ユーザが 1 つも持たない場合に true を返す |
| `hasCurrentUserThisAuthority(String authority)` | `boolean` | `hasCurrentUserAnyOfAuthorities` と同じ処理を行う |

主コードで呼び出し元があるのは `getCurrentUserLogin` と定数（`JWT_ALGORITHM`、`AUTHORITIES_CLAIM`、`USER_ID_CLAIM`）のみである。他のメソッドはテスト（`SecurityUtilsUnitTest`）だけが使う。

---

## 処理詳細２．監査者の取得（SpringSecurityAuditorAware）

| 項目 | 内容 |
| --- | --- |
| クラス | `io.github.jhipster.sample.security.SpringSecurityAuditorAware` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/security/SpringSecurityAuditorAware.java` |
| インターフェース | `AuditorAware<String>`（Spring Data JPA） |
| 有効化 | `DatabaseConfiguration` の `@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")` |

### 処理詳細

`getCurrentAuditor()` は `SecurityUtils.getCurrentUserLogin()` の値を返す。未認証の場合は `Constants.SYSTEM`（`system`）を返す。この値が、監査項目 `createdBy` と `lastModifiedBy` に設定される。

---

## 処理詳細３．改行文字の置き換え（CRLFLogConverter）

| 項目 | 内容 |
| --- | --- |
| クラス | `io.github.jhipster.sample.config.CRLFLogConverter` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/config/CRLFLogConverter.java` |
| 種別 | Logback の `CompositeConverter<ILoggingEvent>` |

### 処理詳細

ログへの不正な行の挿入（ログインジェクション）を防ぐため、ログメッセージ中の改行文字（CR、LF、タブ）をアンダースコア（`_`）に置き換える。`logback-spring.xml` のパターンで `%crlf(%m){red}` として使い、置き換えた文字を赤色で表示する。

置き換えを行わない条件は次のとおりである。

| No | 条件 |
| --- | --- |
| 1 | ログにマーカー `CRLF_SAFE_MARKER`（`CRLF_SAFE`）が付いている |
| 2 | ロガー名が `SAFE_LOGS` のいずれかで始まる（`org.hibernate`、`org.springframework.boot.autoconfigure`、`org.springframework.boot.diagnostics`） |

`JhipsterSampleApplicationApp` は、起動時のログ（複数行）に `CRLF_SAFE_MARKER` を付けている。

---

## 処理詳細４．JHipster ライブラリのユーティリティ

JHipster ライブラリ（パッケージ `tech.jhipster`）のクラスのうち、本リポジトリが使うものを記載する。

### HeaderUtil（`tech.jhipster.web.util.HeaderUtil`）

| メソッド | 生成するヘッダ | 備考 |
| --- | --- | --- |
| `createAlert(applicationName, message, param)` | `X-{applicationName}-alert: {message}`、`X-{applicationName}-params: {param}` | `param` は URL エンコードする |
| `createEntityCreationAlert(applicationName, enableTranslation, entityName, param)` | `createAlert` を呼び出す。メッセージは `{entityName}.created` | `enableTranslation` が false の場合は英文の既定メッセージ |
| `createEntityUpdateAlert` | 同上。メッセージは `{entityName}.updated` |  |
| `createEntityDeletionAlert` | 同上。メッセージは `{entityName}.deleted` |  |
| `createFailureAlert(applicationName, enableTranslation, entityName, errorKey, defaultMessage)` | `X-{applicationName}-error: error.{errorKey}`、`X-{applicationName}-params: {entityName}` | `enableTranslation` が false の場合は `defaultMessage` |

`applicationName` には設定 `jhipster.clientApp.name`（`jhipsterSampleApplicationApp`）を使う。

### PaginationUtil（`tech.jhipster.web.util.PaginationUtil`）

| メソッド | 生成するヘッダ | 備考 |
| --- | --- | --- |
| `generatePaginationHttpHeaders(uriBuilder, page)` | `X-Total-Count: {総件数}`、`Link: {ページへのリンク}` | `Link` ヘッダは `LinkHeaderUtil` が生成する |

### ResponseUtil（`tech.jhipster.web.util.ResponseUtil`）

| メソッド | 戻り値 | 備考 |
| --- | --- | --- |
| `wrapOrNotFound(Optional<T>)` | 値がある場合はステータス 200 の `ResponseEntity`、ない場合は `ResponseStatusException`（404） |  |
| `wrapOrNotFound(Optional<T>, HttpHeaders)` | 同上。値がある場合はヘッダを付ける |  |

値がない場合は例外となり、`ExceptionTranslator` が Problem Details の 404 に変換する。

### RandomUtil（`tech.jhipster.security.RandomUtil`）

| メソッド | 戻り値 | 備考 |
| --- | --- | --- |
| `generatePassword()` | 20 桁のランダムな英数字 | `SecureRandom` を使う |
| `generateActivationKey()` | 同上 |  |
| `generateResetKey()` | 同上 |  |

### ProblemDetailWithCause（`tech.jhipster.web.rest.errors.ProblemDetailWithCause`）

Spring の `ProblemDetail` に原因となった例外の情報を持たせたクラスである。`ProblemDetailWithCauseBuilder` の `instance()` から生成し、`withStatus`、`withType`、`withTitle`、`withProperty` で値を設定する。

### その他

| クラス | 本リポジトリでの使われ方 |
| --- | --- |
| `DefaultProfileUtil` | `addDefaultProfile` を呼び出し、Spring Boot アプリケーションに既定のプロファイルを設定する |
| `SpringLiquibaseUtil` | `createAsyncSpringLiquibase` または `createSpringLiquibase` で `SpringLiquibase` を生成する |
| `H2ConfigurationHelper` | `createServer` で H2 の TCP サーバを生成する。`initH2Console` で H2 コンソールを初期化する |
| `ExceptionHandlingAsyncTaskExecutor` | `ThreadPoolTaskExecutor` をラップし、タスク内の未処理の例外をログに出力する |
| `PrefixedKeyGenerator` | Spring Cache の `KeyGenerator` として Bean 登録する。`GitProperties` と `BuildProperties` から接頭辞を作り、キャッシュのキーに付ける（詳細は「キャッシュ設計書」） |
| `LoggingUtils` | JSON 形式のコンソール出力先（`addJsonConsoleAppender`）、Logstash への出力先（`addLogstashTcpSocketAppender`）、コンテキストリスナー（`addContextListener`）を Logback に追加する |
| `JHipsterProperties` | 設定キー `jhipster.*` を読み込む。本リポジトリはキャッシュ（`getCache().getEhcache()`）、CORS（`getCors()`）、ロギング（`getLogging()`）、メール（`getMail()`）、Content Security Policy（`getSecurity().getContentSecurityPolicy()`）を使う |
| `JHipsterConstants` | Spring プロファイル名の定数（`SPRING_PROFILE_DEVELOPMENT`、`SPRING_PROFILE_PRODUCTION`、`SPRING_PROFILE_CLOUD`、`SPRING_PROFILE_NO_LIQUIBASE`） |

---
