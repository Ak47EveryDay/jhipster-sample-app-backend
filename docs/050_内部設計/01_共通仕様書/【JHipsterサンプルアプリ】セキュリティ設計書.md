# 【JHipsterサンプルアプリ】セキュリティ設計書

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　認証・認可の方式 |
| 2 | 2　JWT |
| 3 | 3　セキュリティフィルタチェインの設定 |
| 4 | 4　メソッドセキュリティ |
| 5 | 5　パスワードの管理 |
| 6 | 6　ユーザの読み込みと認証 |
| 7 | 7　未認証と権限不足の応答 |
| 8 | 8　セキュリティヘッダ |
| 9 | 9　CORS |
| 10 | 10　JWT の検証エラーのメトリクス |
| 11 | 11　開発環境向けの設定 |
| 12 | 12　実装上の留意点 |

---

## 1　認証・認可の方式

本システムはステートレスな REST API であり、セッションを使わない。認証には JWT（JSON Web Token）を使う。

| 項目 | 内容 |
| --- | --- |
| 認証 | クライアントが `Authorization: Bearer {JWT}` ヘッダを送り、サーバが署名と有効期限を検証する |
| 認可 | JWT の `auth` クレームに格納した権限（`ROLE_ADMIN`、`ROLE_USER`）で判定する |
| トークンの発行 | `POST /api/authenticate`（ログイン認証）のみ |
| トークンの失効 | 失効の仕組みはない。有効期限が切れるまで有効 |
| パスワードの保存 | BCrypt のハッシュ（60 桁） |

対象クラスは次のとおりである。

| No | クラス | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- | --- |
| 1 | `SecurityConfiguration` | `src/main/java/io/github/jhipster/sample/config/SecurityConfiguration.java` | セキュリティフィルタチェインと `PasswordEncoder` の定義 |
| 2 | `SecurityJwtConfiguration` | `src/main/java/io/github/jhipster/sample/config/SecurityJwtConfiguration.java` | `JwtDecoder`、`JwtEncoder` の定義 |
| 3 | `AuthenticateController` | `src/main/java/io/github/jhipster/sample/web/rest/AuthenticateController.java` | 認証と JWT の発行 |
| 4 | `DomainUserDetailsService` | `src/main/java/io/github/jhipster/sample/security/DomainUserDetailsService.java` | データベースのユーザ情報による認証 |
| 5 | `SecurityUtils` | `src/main/java/io/github/jhipster/sample/security/SecurityUtils.java` | 現在ユーザの取得、JWT の定数 |
| 6 | `AuthoritiesConstants` | `src/main/java/io/github/jhipster/sample/security/AuthoritiesConstants.java` | 権限名の定数 |
| 7 | `WebConfigurer` | `src/main/java/io/github/jhipster/sample/config/WebConfigurer.java` | CORS のフィルタ |
| 8 | `SecurityMetersService` | `src/main/java/io/github/jhipster/sample/management/SecurityMetersService.java` | JWT の検証エラーのメトリクス |

---

## 2　JWT

### 署名

| 項目 | 内容 |
| --- | --- |
| アルゴリズム | `HS512`（`SecurityUtils.JWT_ALGORITHM`） |
| 秘密鍵 | 設定 `jhipster.security.authentication.jwt.base64-secret` を Base64 デコードしたバイト列 |
| エンコーダ | `SecurityJwtConfiguration#jwtEncoder`（`NimbusJwtEncoder`） |
| デコーダ | `SecurityJwtConfiguration#jwtDecoder`（`NimbusJwtDecoder`） |

### クレーム

| No | クレーム | 内容 |
| --- | --- | --- |
| 1 | `sub` | ログインID |
| 2 | `auth` | 権限名を半角空白で連結した文字列 |
| 3 | `userId` | ユーザID（認証プリンシパルが `UserWithId` の場合のみ） |
| 4 | `iat` | 発行日時 |
| 5 | `exp` | 有効期限 |

リソースサーバの設定（`application.yml`）で `authorities-claim-name: auth`、`authority-prefix: ''` としている。このため `auth` クレームの値を半角空白で分割した各要素を、そのまま権限名として扱う。

### 有効期限

| 条件 | 設定キー | 秒数 |
| --- | --- | --- |
| `rememberMe` が false | `jhipster.security.authentication.jwt.token-validity-in-seconds` | 86400（24 時間） |
| `rememberMe` が true | `jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me` | 2592000（30 日） |

JWT の生成処理の詳細は「サービス詳細設計書_ログイン認証」を参照。

### 秘密鍵の設定

`base64-secret` のサンプル値は、主コードの設定ではプロファイル `secret-samples` にのみ定義している（テスト用の設定 `src/test/resources/config/application.yml` にも同じ値がある）。`dev` はこのプロファイルをグループに含むためサンプル値で動作する。`prod` はグループに含まないため、環境変数 `JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET` などで設定する必要がある。

---

## 3　セキュリティフィルタチェインの設定

`SecurityConfiguration#filterChain(HttpSecurity)` の設定内容は次のとおりである。

| No | 項目 | 設定内容 |
| --- | --- | --- |
| 1 | CORS | 有効。`WebConfigurer#corsFilter` の Bean を使う |
| 2 | CSRF | 無効（API はステートレスであるため） |
| 3 | Content Security Policy | 設定 `jhipster.security.content-security-policy` の値 |
| 4 | X-Frame-Options | `SAMEORIGIN` |
| 5 | Referrer-Policy | `strict-origin-when-cross-origin` |
| 6 | Permissions-Policy | `camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()` |
| 7 | URL による認可 | 「キー・ロール一覧」の判定方法を参照 |
| 8 | セッション | 作成しない（`SessionCreationPolicy.STATELESS`） |
| 9 | 未認証の処理 | `BearerTokenAuthenticationEntryPoint` |
| 10 | 権限不足の処理 | `BearerTokenAccessDeniedHandler` |
| 11 | リソースサーバ | JWT を有効化。デコーダは `SecurityJwtConfiguration#jwtDecoder` |

`dev` プロファイルのときは、`/h2-console/**` を認証不要として追加する。

`SecurityConfiguration` のクラスには `@EnableMethodSecurity(securedEnabled = true)` を付与している。

---

## 4　メソッドセキュリティ

URL による認可以外に、リソースクラスのメソッド単位で権限を判定する。

| No | クラス | 対象メソッド | アノテーション |
| --- | --- | --- | --- |
| 1 | `UserResource` | 全メソッド | `@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")` |
| 2 | `AuthorityResource` | 全メソッド | `@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")` |

`UserResource` の URL（`/api/admin/**`）は URL による認可でも `ROLE_ADMIN` を要求するため、判定は二重になる。`AuthorityResource` の URL（`/api/authorities`）は URL による認可では「認証済み」となるため、管理者であることはこのアノテーションだけで判定する。

---

## 5　パスワードの管理

| 項目 | 内容 |
| --- | --- |
| ハッシュ化 | `SecurityConfiguration#passwordEncoder` で定義した `BCryptPasswordEncoder` |
| 保存先 | テーブル `jhi_user` の `password_hash`（varchar(60)） |
| 初期パスワード | ユーザ登録（`POST /api/register`）では利用者が指定したパスワード。管理者によるユーザ作成（`POST /api/admin/users`）では `RandomUtil.generatePassword()` で生成した 20 桁のランダムな英数字 |
| パスワードの規則 | 桁数のみ（4 桁以上 100 桁以下）。文字種の規則はない |
| 変更 | `POST /api/account/change-password`（現在のパスワードの照合あり）、パスワードリセット |
| パスワードの再利用 | 再利用を禁止する履歴の管理はない |

パスワードの照合は、ログイン認証では Spring Security が `PasswordEncoder` で行い、パスワード変更では `UserService#changePassword` が `PasswordEncoder#matches` で行う。

---

## 6　ユーザの読み込みと認証

| 項目 | 内容 |
| --- | --- |
| クラス | `DomainUserDetailsService`（Bean 名 `userDetailsService`） |
| インターフェース | Spring Security の `UserDetailsService` |
| 呼び出され方 | `AuthenticateController` が `AuthenticationManagerBuilder` から取得した認証マネージャを経由して呼び出す |

`DomainUserDetailsService#loadUserByUsername(login)` の処理は次のとおりである。

| No | 条件 | 検索メソッド | 備考 |
| --- | --- | --- | --- |
| 1 | `login` がメールアドレス形式 | `UserRepository#findOneWithAuthoritiesByEmailIgnoreCase(login)` | `EmailValidator#isValid` で判定する |
| 2 | 上記以外 | `UserRepository#findOneWithAuthoritiesByLogin(login)` | `login` を小文字化して検索する |

| No | 検索結果 | 処理 |
| --- | --- | --- |
| 1 | なし | `UsernameNotFoundException` をスローする |
| 2 | あり、`activated` が false | `UserNotActivatedException` をスローする |
| 3 | あり、`activated` が true | `UserWithId.fromUser(user)` を返す |

パスワードが一致しない場合は `BadCredentialsException` となる。ユーザが存在しない場合とパスワードが一致しない場合は、どちらも 401 で `message`（`error.http.401`）も同じであるが、応答の `detail` は異なる（ユーザが存在しない場合は `User <ログインID> was not found in the database`、パスワードが一致しない場合は `Bad credentials`）。そのため、ログイン ID が登録済みかどうかを応答から判別できる。詳細は「サービス詳細設計書_ログイン認証」を参照。

---

## 7　未認証と権限不足の応答

未認証と権限不足の応答は、拒否した段階によって異なる。

| No | 発生条件 | 拒否する段階 | ステータス | 応答本文 | ヘッダ |
| --- | --- | --- | --- | --- | --- |
| 1 | 認証が必要な URL に、有効な JWT を付けずにリクエストした | セキュリティフィルタチェイン | 401 | なし | `WWW-Authenticate: Bearer` |
| 2 | JWT が無効（署名不正、期限切れ、形式不正） | セキュリティフィルタチェイン | 401 | なし | `WWW-Authenticate: Bearer`（エラーの内容を含む） |
| 3 | URL が要求する権限を持たない（`/api/admin/**` に一般ユーザがリクエストした場合など） | セキュリティフィルタチェイン | 403 | なし | `WWW-Authenticate: Bearer` |
| 4 | `@PreAuthorize` が拒否した | リソースクラス | 403 | Problem Details（`message` は `error.http.403`） | なし |
| 5 | ログイン認証に失敗した（ログインIDまたはパスワードの誤り） | リソースクラス | 401 | Problem Details（`message` は `error.http.401`） | なし |

No 1～3 は Spring Security の `BearerTokenAuthenticationEntryPoint` と `BearerTokenAccessDeniedHandler` が応答する。No 4、No 5 は例外がリソースクラスから伝播し、`ExceptionTranslator` が変換する。No 2 の `WWW-Authenticate` ヘッダの内容は Spring Security が決める。

---

## 8　セキュリティヘッダ

応答に付与するセキュリティ関連のヘッダは「3　セキュリティフィルタチェインの設定」の No 3～6 のとおりである。設定値は次のとおりである。

| ヘッダ | 値 |
| --- | --- |
| Content-Security-Policy | `default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:` |
| X-Frame-Options | `SAMEORIGIN` |
| Referrer-Policy | `strict-origin-when-cross-origin` |
| Permissions-Policy | `camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()` |

---

## 9　CORS

| 項目 | 内容 |
| --- | --- |
| 定義箇所 | `WebConfigurer#corsFilter` |
| 有効条件 | 設定 `jhipster.cors.allowed-origins` または `jhipster.cors.allowed-origin-patterns` に値がある場合のみフィルタを登録する |
| 適用先のパス | `/api/**`、`/management/**`、`/v3/api-docs`、`/swagger-ui/**` |

| プロファイル | CORS の設定 |
| --- | --- |
| `dev` | あり（`application-dev.yml` の `jhipster.cors`）。許可するオリジンは `http://localhost:8100`、`https://localhost:8100`、`http://localhost:9060`、`https://localhost:9060`、パターンは `https://*.githubpreview.dev` |
| `prod` | なし（CORS は無効） |

`exposed-headers` にアラートとエラーのヘッダ（`Authorization`、`Link`、`X-Total-Count`、`X-jhipsterSampleApplicationApp-alert`、`X-jhipsterSampleApplicationApp-error`、`X-jhipsterSampleApplicationApp-params`）を指定しているため、フロントエンドはこれらの応答ヘッダを読み取れる。上記以外のオリジンから呼び出す場合は、`application-dev.yml` の `jhipster.cors.allowed-origins` にオリジンを追加する。

---

## 10　JWT の検証エラーのメトリクス

`SecurityJwtConfiguration#jwtDecoder` は、デコードに失敗したときに例外のメッセージの内容で原因を判別し、`SecurityMetersService` のカウンタを加算してから例外を再スローする。

| No | 例外のメッセージに含まれる文字列 | カウンタを加算するメソッド | タグ `cause` の値 |
| --- | --- | --- | --- |
| 1 | `Invalid signature` | `trackTokenInvalidSignature` | `invalid-signature` |
| 2 | `Jwt expired at` | `trackTokenExpired` | `expired` |
| 3 | `Invalid JWT serialization`、`Malformed token`、`Invalid unsecured/JWS/JWE` のいずれか | `trackTokenMalformed` | `malformed` |
| 4 | 上記以外 | 加算せず、エラーログ `Unknown JWT error {}` を出力する | － |

カウンタの名前は `security.authentication.invalid-tokens` である。`trackTokenUnsupported`（`cause` は `unsupported`）も定義しているが、主コードからの呼び出し元はない。メトリクスの参照方法は「ログ・監視設計書」を参照。

---

## 11　開発環境向けの設定

| No | 項目 | 内容 |
| --- | --- | --- |
| 1 | H2 コンソール | `dev` プロファイルで `/h2-console/**` を認証不要にする。`spring.h2.console.enabled` が `true` のとき、`WebConfigurer` がコンソールを初期化し、`DatabaseConfiguration` が TCP サーバを起動する |
| 2 | TLS | プロファイル `tls` で HTTPS と HTTP/2 を有効にする。キーストアは自己署名証明書（`src/main/resources/config/tls/keystore.p12`） |
| 3 | サンプルの秘密鍵 | プロファイル `secret-samples` の JWT 秘密鍵はサンプル値である |

---

## 12　実装上の留意点

コードから読み取れたセキュリティ上の実装特徴を記載する。

| No | 項目 | 内容 | 根拠 |
| --- | --- | --- | --- |
| 1 | パスワードリセットの要求 | メールアドレスに該当するユーザがいなくても 200 を返す。ユーザの存在有無を応答から判別できないようにする | `AccountResource#requestPasswordReset` |
| 2 | パスワードリセットの完了 | リセットキーが該当しない場合も、成功時と同じ計算量のハッシュ化を実行してから 400 を返す。応答時間からキーの有無を推測させないようにする | `AccountResource#finishPasswordReset` |
| 3 | リセットキーの有効期限 | リセットの要求から 1 日 | `UserService#completePasswordReset` |
| 4 | 未有効化ユーザ | 有効化しないまま登録後 3 日を過ぎたユーザは削除される | `UserService#removeNotActivatedUsers` |
| 5 | ログインID | 保存時に小文字化する。大文字小文字のみが異なる同一のログインIDは登録できない | `User#setLogin` |
| 6 | ユーザ登録時の権限 | リクエスト本文の `authorities` は無視し、`ROLE_USER` のみを付与する | `UserService#registerUser` |
| 7 | 公開ユーザ情報検索 | クラスのコメントには「誰でも呼び出せる」とあるが、実際には認証済みが必要 | `SecurityConfiguration` の URL による認可 |
| 8 | 認証情報のログ出力 | `LoginVM#toString` はパスワードを出力しない | `LoginVM#toString` |
| 9 | ログの改行文字 | 改行文字を置き換え、ログへの不正な行の挿入を防ぐ | `CRLFLogConverter` |
| 10 | JWT の失効 | 失効の仕組みはない。権限を変更しても、発行済みの JWT は有効期限が切れるまで古い権限のまま使える | JWT はステートレスに検証する方式 |

---
