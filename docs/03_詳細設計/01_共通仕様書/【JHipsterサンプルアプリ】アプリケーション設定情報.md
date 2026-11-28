# 【JHipsterサンプルアプリ】アプリケーション設定情報

## 改版履歴

| **版数** | **改版日付** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- |
| 1.0 | 2026/11/23 | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　プロファイル構成 |
| 2 | 2　設定ファイル一覧 |
| 3 | 3　共通設定（application.yml） |
| 4 | 4　開発環境設定（dev） |
| 5 | 5　本番環境設定（prod） |
| 6 | 6　TLS 設定（tls） |
| 7 | 7　サンプル秘密情報（secret-samples） |
| 8 | 8　テスト用設定 |
| 9 | 9　環境変数による上書き |

---

## 1　プロファイル構成

設定は Spring Boot のプロファイルごとにファイルを分ける。共通の設定を `application.yml` に記述し、各プロファイルのファイルが上書きする。

| No | Spring プロファイル | 用途 | 有効にする方法 |
| --- | --- | --- | --- |
| 1 | （なし） | 全プロファイルで読み込む共通設定 | 常に読み込む |
| 2 | `dev` | 開発環境（インメモリの H2、サンプルデータあり） | Maven プロファイル `dev`（既定で有効） |
| 3 | `prod` | 本番環境（PostgreSQL） | Maven プロファイル `prod`、または `--spring.profiles.active=prod` |
| 4 | `tls` | TLS と HTTP/2 を有効にする | `spring.profiles.group.dev` のコメントを外す、Maven プロファイル `tls`、または起動時に指定 |
| 5 | `secret-samples` | 開発と CI 用のサンプルの秘密情報（JWT 秘密鍵、データソースのユーザ） | `dev` プロファイルのグループに含まれる |
| 6 | `api-docs` | API ドキュメントを有効にする | `dev` プロファイルのグループに含まれる。`prod` では Maven プロファイル `api-docs` で追加 |
| 7 | `no-liquibase` | Liquibase の実行を無効にする | Maven プロファイル `no-liquibase`、または起動時に指定 |

プロファイルのグループは `application.yml` の `spring.profiles.group` で定義している。`dev` は `secret-samples` と `api-docs` を含む（`tls` はコメントアウトしている）。

Maven のプロファイルと Spring のプロファイルの対応は `pom.xml` が定める。既定の `spring.profiles.active` は `dev` であり、`pom.xml` の値がビルド時に `application.yml` の `@spring.profiles.active@` に埋め込まれる。コマンドライン引数 `--spring.profiles.active` または環境変数で上書きできる。

---

## 2　設定ファイル一覧

| No | ファイルパス（リポジトリルート基準） | プロファイル | 内容 |
| --- | --- | --- | --- |
| 1 | `src/main/resources/config/application.yml` | 共通 | 全プロファイルの共通設定 |
| 2 | `src/main/resources/config/application-dev.yml` | `dev` | 開発環境 |
| 3 | `src/main/resources/config/application-prod.yml` | `prod` | 本番環境 |
| 4 | `src/main/resources/config/application-tls.yml` | `tls` | TLS |
| 5 | `src/main/resources/config/application-secret-samples.yml` | `secret-samples` | サンプルの秘密情報 |
| 6 | `src/main/resources/logback-spring.xml` | － | ログの設定 |
| 7 | `src/test/resources/config/application.yml` | テスト | テスト用の共通設定 |
| 8 | `src/test/resources/config/application-testdev.yml` | `testdev` | テスト用（H2） |
| 9 | `src/test/resources/config/application-testprod.yml` | `testprod` | テスト用（Testcontainers の PostgreSQL） |

---

## 3　共通設定（application.yml）

| No | キー | 説明 | 設定値 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `spring.application.name` | アプリケーション名 | `jhipsterSampleApplication` | メトリクスのタグなどに使う |
| 2 | `spring.profiles.active` | 有効なプロファイル | `@spring.profiles.active@` | ビルド時に Maven が置換する |
| 3 | `spring.profiles.group.dev` | `dev` に含めるプロファイル | `secret-samples`、`api-docs` | `tls` はコメントアウト |
| 4 | `spring.docker.compose.enabled` | Docker Compose 連携の有効化 | `true` | `dev` では `false` |
| 5 | `spring.docker.compose.lifecycle-management` | コンテナの起動と停止の方法 | `start-only` | 起動のみ行い、停止はしない |
| 6 | `spring.docker.compose.file` | Compose ファイル | `src/main/docker/services.yml` | PostgreSQL の定義 |
| 7 | `spring.jmx.enabled` | JMX の有効化 | `false` |  |
| 8 | `spring.data.jpa.repositories.bootstrap-mode` | リポジトリの初期化の方法 | `deferred` |  |
| 9 | `spring.jpa.open-in-view` | Open Session in View の有効化 | `false` | トランザクションの外で遅延読み込みをしない |
| 10 | `spring.jpa.hibernate.ddl-auto` | スキーマの自動生成 | `none` | スキーマは Liquibase が管理する |
| 11 | `spring.jpa.hibernate.naming.physical-strategy` | カラム名の命名規則 | `org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy` |  |
| 12 | `spring.jpa.hibernate.naming.implicit-strategy` | 暗黙の命名規則 | `org.springframework.boot.hibernate.SpringImplicitNamingStrategy` |  |
| 13 | `spring.jpa.properties.hibernate.jdbc.time_zone` | JDBC のタイムゾーン | `UTC` |  |
| 14 | `spring.jpa.properties.hibernate.timezone.default_storage` | タイムゾーン付与日時の保存方法 | `NORMALIZE` |  |
| 15 | `spring.jpa.properties.hibernate.type.preferred_instant_jdbc_type` | `Instant` の JDBC 型 | `TIMESTAMP` |  |
| 16 | `spring.jpa.properties.hibernate.id.new_generator_mappings` | 識別子の採番方式の新仕様 | `true` |  |
| 17 | `spring.jpa.properties.hibernate.connection.provider_disables_autocommit` | 接続側で自動コミットを無効にしているか | `true` | HikariCP の `auto-commit: false` に対応 |
| 18 | `spring.jpa.properties.hibernate.cache.use_second_level_cache` | 第二レベルキャッシュの有効化 | `true` |  |
| 19 | `spring.jpa.properties.hibernate.cache.use_query_cache` | クエリキャッシュの有効化 | `false` |  |
| 20 | `spring.jpa.properties.hibernate.generate_statistics` | Hibernate の統計情報の有効化 | `false` |  |
| 21 | `spring.jpa.properties.hibernate.jdbc.batch_size` | バッチ更新の件数 | `25` |  |
| 22 | `spring.jpa.properties.hibernate.order_inserts` | INSERT の並べ替え | `true` |  |
| 23 | `spring.jpa.properties.hibernate.order_updates` | UPDATE の並べ替え | `true` |  |
| 24 | `spring.jpa.properties.hibernate.query.fail_on_pagination_over_collection_fetch` | コレクションの一括読み込みとページングの併用をエラーにする | `true` |  |
| 25 | `spring.jpa.properties.hibernate.query.in_clause_parameter_padding` | IN 句のパラメータ数の揃え方 | `true` |  |
| 26 | `spring.messages.basename` | メッセージの基底名 | `i18n/messages` |  |
| 27 | `spring.main.allow-bean-definition-overriding` | Bean 定義の上書きの許可 | `true` |  |
| 28 | `spring.mvc.problemdetails.enabled` | Problem Details のサポートの有効化 | `true` |  |
| 29 | `spring.security.oauth2.resourceserver.jwt.authority-prefix` | 権限名の接頭辞 | `''`（空文字） | JWT の `auth` クレームの値をそのまま権限名として使う |
| 30 | `spring.security.oauth2.resourceserver.jwt.authorities-claim-name` | 権限を格納するクレーム名 | `auth` |  |
| 31 | `spring.task.execution.thread-name-prefix` | 非同期処理のスレッド名の接頭辞 | `jhipster-sample-application-task-` |  |
| 32 | `spring.task.execution.pool.core-size` | 非同期処理のスレッドプールの基本サイズ | `2` |  |
| 33 | `spring.task.execution.pool.max-size` | 非同期処理のスレッドプールの最大サイズ | `50` |  |
| 34 | `spring.task.execution.pool.queue-capacity` | 非同期処理のキューの容量 | `10000` |  |
| 35 | `spring.task.scheduling.thread-name-prefix` | 定期処理のスレッド名の接頭辞 | `jhipster-sample-application-scheduling-` |  |
| 36 | `spring.task.scheduling.pool.size` | 定期処理のスレッドプールのサイズ | `2` |  |
| 37 | `spring.thymeleaf.mode` | Thymeleaf のモード | `HTML` | メールのテンプレート用 |
| 38 | `spring.output.ansi.console-available` | コンソールの ANSI エスケープの有効化 | `true` |  |
| 39 | `server.servlet.session.cookie.http-only` | セッションクッキーの HttpOnly | `true` | セッションは使わない（ステートレス） |
| 40 | `spring.messages.cache-duration` | メッセージのキャッシュ期間 | （共通では未設定） | `dev` では `PT1S` |

監視（Actuator）の設定は次のとおりである。

| No | キー | 説明 | 設定値 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `management.endpoints.web.base-path` | 管理エンドポイントの基底パス | `/management` |  |
| 2 | `management.endpoints.web.exposure.include` | 公開する管理エンドポイント | `configprops`、`env`、`health`、`info`、`jhimetrics`、`jhiopenapigroups`、`logfile`、`loggers`、`prometheus`、`threaddump`、`caches`、`liquibase` |  |
| 3 | `management.endpoint.health.show-details` | ヘルス詳細の表示条件 | `when_authorized` |  |
| 4 | `management.endpoint.health.roles` | ヘルス詳細を表示できる権限 | `ROLE_ADMIN` |  |
| 5 | `management.endpoint.health.probes.enabled` | liveness と readiness のグループの有効化 | `true` |  |
| 6 | `management.endpoint.health.group.liveness.include` | liveness グループの構成 | `livenessState` |  |
| 7 | `management.endpoint.health.group.readiness.include` | readiness グループの構成 | `readinessState`、`db` |  |
| 8 | `management.endpoint.jhimetrics.access` | `jhimetrics` エンドポイントのアクセス権 | `read-only` |  |
| 9 | `management.info.git.mode` | Git 情報の表示モード | `full` |  |
| 10 | `management.info.env.enabled` | `info` に環境情報を含める | `true` |  |
| 11 | `management.health.mail.enabled` | メールのヘルスチェック | `false` |  |
| 12 | `management.prometheus.metrics.export.enabled` | Prometheus 形式の出力の有効化 | `true` | `prod` では `false` |
| 13 | `management.prometheus.metrics.export.step` | メトリクスの集計間隔（秒） | `60` |  |
| 14 | `management.observations.key-values.application` | 観測データに付けるタグ | `${spring.application.name}` |  |
| 15 | `management.metrics.enable.http`、`jvm`、`logback`、`process`、`system` | 収集するメトリクスの有効化 | `true` |  |
| 16 | `management.metrics.distribution.percentiles-histogram.all` | パーセンタイルヒストグラムの有効化 | `true` |  |
| 17 | `management.metrics.distribution.percentiles.all` | 計算するパーセンタイル | `0`、`0.5`、`0.75`、`0.95`、`0.99`、`1.0` |  |
| 18 | `management.metrics.data.repository.autotime.enabled` | リポジトリメソッドの計測 | `true` |  |
| 19 | `management.metrics.tags.application` | メトリクスの共通タグ | `${spring.application.name}` |  |

JHipster 関連とその他の設定は次のとおりである。

| No | キー | 説明 | 設定値 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `jhipster.clientApp.name` | アプリケーション名（アラートヘッダの名前に使う） | `jhipsterSampleApplicationApp` |  |
| 2 | `jhipster.mail.from` | メールの送信元 | `jhipsterSampleApplication@localhost` |  |
| 3 | `jhipster.mail.base-url` | メールに記載するリンクの基底 URL | （共通では未設定） | プロファイル別に設定 |
| 4 | `jhipster.security.content-security-policy` | Content Security Policy | `default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:` |  |
| 5 | `jhipster.cors.*` | CORS の設定 | コメントアウト（無効） | `dev` でのみ設定 |
| 6 | `jhipster.api-docs.default-include-pattern` | API ドキュメントの対象パス | `/api/**` |  |
| 7 | `jhipster.api-docs.management-include-pattern` | API ドキュメントの管理エンドポイント | `/management/**` |  |
| 8 | `jhipster.api-docs.title`、`description`、`version` | API ドキュメントの説明 | `Jhipster Sample Application API`、`Jhipster Sample Application API documentation`、`0.0.1` |  |
| 9 | `springdoc.api-docs.enabled` | API ドキュメントの有効化 | `false` | プロファイル `api-docs` を有効にしないときに適用される値（共通のファイルで定義） |
| 10 | `springdoc.show-actuator` | API ドキュメントに管理エンドポイントを含める | `true` |  |
| 11 | `info.display-ribbon-on-profiles` | リボンを表示するプロファイル | `dev` |  |
| 12 | `application.liquibase.async-start` | Liquibase を非同期で実行するか | `true`（`ApplicationProperties` の既定値） | `application.yml` には記述なし |

---

## 4　開発環境設定（dev）

| No | キー | 説明 | 設定値 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `logging.level.ROOT` | ルートロガーのレベル | `DEBUG` |  |
| 2 | `logging.level.tech.jhipster` | JHipster ライブラリのレベル | `DEBUG` |  |
| 3 | `logging.level.org.hibernate.SQL` | SQL ログのレベル | `DEBUG` |  |
| 4 | `logging.level.io.github.jhipster.sample` | アプリケーションのレベル | `DEBUG` |  |
| 5 | `spring.devtools.restart.enabled` | Devtools の自動再起動 | `true` |  |
| 6 | `spring.devtools.restart.additional-exclude` | 再起動の監視対象外 | `static/**`、`.h2.server.properties` |  |
| 7 | `spring.devtools.livereload.enabled` | LiveReload | `false` |  |
| 8 | `spring.docker.compose.enabled` | Docker Compose 連携 | `false` |  |
| 9 | `spring.docker.compose.profiles.active` | Compose のプロファイル | `dev` |  |
| 10 | `spring.jackson.serialization.indent-output` | JSON のインデント付き出力 | `true` |  |
| 11 | `spring.datasource.type` | データソースの型 | `com.zaxxer.hikari.HikariDataSource` |  |
| 12 | `spring.datasource.url` | 接続先 | `jdbc:h2:mem:jhipsterSampleApplication;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` | インメモリの H2 |
| 13 | `spring.datasource.hikari.poolName` | コネクションプールの名前 | `Hikari` |  |
| 14 | `spring.datasource.hikari.auto-commit` | 自動コミット | `false` |  |
| 15 | `spring.h2.console.enabled` | H2 コンソールの有効化 | `true` |  |
| 16 | `spring.liquibase.contexts` | 適用する変更セットのコンテキスト | `dev, faker` | サンプルデータを読み込む |
| 17 | `spring.mail.host`、`spring.mail.port` | SMTP サーバ | `localhost`、`25` |  |
| 18 | `spring.messages.cache-duration` | メッセージのキャッシュ期間 | `PT1S` | 1 秒 |
| 19 | `spring.thymeleaf.cache` | テンプレートのキャッシュ | `false` |  |
| 20 | `server.port` | ポート | `8080` |  |
| 21 | `server.forward-headers-strategy` | 転送ヘッダの処理 | `native` |  |
| 22 | `jhipster.cache.ehcache.time-to-live-seconds` | キャッシュの生存期間（秒） | `3600` |  |
| 23 | `jhipster.cache.ehcache.max-entries` | キャッシュの最大件数 | `100` |  |
| 24 | `jhipster.cors.allowed-origins` | 許可するオリジン | `http://localhost:8100`、`https://localhost:8100`、`http://localhost:9060`、`https://localhost:9060` | カンマ区切り |
| 25 | `jhipster.cors.allowed-origin-patterns` | 許可するオリジンのパターン | `https://*.githubpreview.dev` |  |
| 26 | `jhipster.cors.allowed-methods` | 許可する HTTP メソッド | `*` |  |
| 27 | `jhipster.cors.allowed-headers` | 許可するリクエストヘッダ | `*` |  |
| 28 | `jhipster.cors.exposed-headers` | ブラウザに公開する応答ヘッダ | `Authorization`、`Link`、`X-Total-Count`、`X-${jhipster.clientApp.name}-alert`、`X-${jhipster.clientApp.name}-error`、`X-${jhipster.clientApp.name}-params` |  |
| 29 | `jhipster.cors.allow-credentials` | 認証情報の送信を許可 | `true` |  |
| 30 | `jhipster.cors.max-age` | プリフライト応答のキャッシュ期間（秒） | `1800` |  |
| 31 | `jhipster.security.authentication.jwt.token-validity-in-seconds` | JWT の有効期限（秒） | `86400` | 24 時間 |
| 32 | `jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me` | ログイン状態を保持する場合の JWT の有効期限（秒） | `2592000` | 30 日 |
| 33 | `jhipster.mail.base-url` | メールに記載するリンクの基底 URL | `http://localhost:9060` | フロントエンドのアドレス |
| 34 | `jhipster.logging.use-json-format` | ログの JSON 形式出力 | `false` |  |
| 35 | `jhipster.logging.logstash.enabled` | Logstash への転送 | `false` |  |
| 36 | `jhipster.logging.logstash.host`、`port`、`ring-buffer-size` | Logstash の接続先とバッファ | `localhost`、`5000`、`512` |  |

---

## 5　本番環境設定（prod）

`dev` と同じキーで値が異なるものを中心に記載する。

| No | キー | 説明 | 設定値 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `logging.level.ROOT`、`tech.jhipster`、`io.github.jhipster.sample` | ログレベル | `INFO` |  |
| 2 | `management.prometheus.metrics.export.enabled` | Prometheus 形式の出力 | `false` | 有効にする場合は環境変数で指定する |
| 3 | `spring.devtools.restart.enabled`、`livereload.enabled` | Devtools | `false` |  |
| 4 | `spring.datasource.type` | データソースの型 | `com.zaxxer.hikari.HikariDataSource` |  |
| 5 | `spring.datasource.url` | 接続先 | `jdbc:postgresql://localhost:5432/jhipsterSampleApplication` | PostgreSQL |
| 6 | `spring.datasource.hikari.poolName`、`auto-commit` | コネクションプール | `Hikari`、`false` |  |
| 7 | `spring.liquibase.contexts` | 適用する変更セットのコンテキスト | `prod` | サンプルデータを読み込まない |
| 8 | `spring.mail.host`、`spring.mail.port` | SMTP サーバ | `localhost`、`25` |  |
| 9 | `spring.thymeleaf.cache` | テンプレートのキャッシュ | `true` |  |
| 10 | `server.port` | ポート | `8080` |  |
| 11 | `server.shutdown` | 停止方法 | `graceful` |  |
| 12 | `server.compression.enabled` | 応答の圧縮 | `true` |  |
| 13 | `server.compression.mime-types` | 圧縮対象の MIME 型 | `text/html`、`text/xml`、`text/plain`、`text/css`、`application/javascript`、`application/json`、`image/svg+xml` |  |
| 14 | `server.compression.min-response-size` | 圧縮対象の最小サイズ（バイト） | `1024` |  |
| 15 | `jhipster.cache.ehcache.time-to-live-seconds` | キャッシュの生存期間（秒） | `3600` |  |
| 16 | `jhipster.cache.ehcache.max-entries` | キャッシュの最大件数 | `1000` |  |
| 17 | `jhipster.security.authentication.jwt.token-validity-in-seconds` | JWT の有効期限（秒） | `86400` |  |
| 18 | `jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me` | ログイン状態を保持する場合の JWT の有効期限（秒） | `2592000` |  |
| 19 | `jhipster.mail.base-url` | メールに記載するリンクの基底 URL | `http://my-server-url-to-change` | 環境に合わせて変更する |
| 20 | `jhipster.http.cache.timeToLiveInDays` | HTTP キャッシュの生存期間（日） | `1461` | 設定ファイルのコメントは `CachingHttpHeadersFilter` 用と記しているが、本リポジトリにそのクラスはなく、主コードから参照していない |
| 21 | `jhipster.logging.use-json-format`、`logstash.*` | ログの出力先 | `false`、`localhost`、`5000`、`512` | `dev` と同じ |

`prod` には `jhipster.cors` の設定がないため、CORS は無効である。`prod` は `secret-samples` を読み込まないため、JWT の秘密鍵 `jhipster.security.authentication.jwt.base64-secret` を環境変数などで設定する必要がある。

---

## 6　TLS 設定（tls）

| No | キー | 説明 | 設定値 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `server.ssl.key-store` | キーストア | `classpath:config/tls/keystore.p12` | 自己署名証明書 |
| 2 | `server.ssl.key-store-password` | キーストアのパスワード | `password` |  |
| 3 | `server.ssl.key-store-type` | キーストアの型 | `PKCS12` |  |
| 4 | `server.ssl.key-alias` | 鍵のエイリアス | `selfsigned` |  |
| 5 | `server.ssl.ciphers` | 有効にする暗号スイート | `TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256`、`TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384`、`TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA`、`TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA`、`TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256`、`TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384`、`TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA`、`TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` |  |
| 6 | `server.ssl.enabled-protocols` | 有効にするプロトコル | `TLSv1.2` |  |
| 7 | `server.http2.enabled` | HTTP/2 の有効化 | `true` |  |

---

## 7　サンプル秘密情報（secret-samples）

開発と CI で `prod` を使うときに必要な秘密情報のサンプルである。ファイルのコメントのとおり、開発と CI 以外では使わない。

| No | キー | 説明 | 設定値 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `jhipster.security.authentication.jwt.base64-secret` | JWT の秘密鍵（Base64） | サンプル値（ファイルを参照） | 全プロファイルで適用 |
| 2 | `spring.datasource.username` | データソースのユーザ | `jhipsterSampleApplication` | プロファイル `dev` と `prod` のそれぞれで適用 |
| 3 | `spring.datasource.password` | データソースのパスワード | 空 | 同上 |

---

## 8　テスト用設定

テストは `src/test/resources/config/application.yml` を使う。主な設定値は次のとおりである。

| No | キー | 設定値 | 備考 |
| --- | --- | --- | --- |
| 1 | `spring.liquibase.contexts` | `test` | テスト用テーブルを作成する |
| 2 | `spring.task.execution.pool.core-size` | `1` |  |
| 3 | `spring.task.scheduling.pool.size` | `20` |  |
| 4 | `server.port` | `10344` |  |
| 5 | `server.address` | `localhost` |  |
| 6 | `jhipster.mail.from` | `jhipsterSampleApplication@localhost.com` |  |
| 7 | `jhipster.mail.base-url` | `http://127.0.0.1:8080` |  |
| 8 | `jhipster.security.authentication.jwt.base64-secret` | サンプル値（ファイルを参照） |  |
| 9 | `jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me` | `86400` |  |
| 10 | `management.health.mail.enabled` | `false` |  |

プロファイル `testdev` は H2（`jdbc:h2:mem:jhipstersampleapplication:12344;...`）に接続し、`testprod` は Testcontainers が起動した PostgreSQL に接続する。どちらも第二レベルキャッシュを無効にする（`spring.jpa.properties.hibernate.cache.use_second_level_cache: false`）。テスト用のプロファイルでは `AsyncConfiguration` が読み込まれず、代わりに `AsyncSyncConfiguration` が同期実行の `taskExecutor` を定義する。

---

## 9　環境変数による上書き

Spring Boot の規則により、設定キーは環境変数で上書きできる（キーの `.` を `_` に、小文字を大文字に置き換える）。本リポジトリのファイルで使われている例は次のとおりである。

| No | 環境変数 | 対応するキー | 記載箇所 |
| --- | --- | --- | --- |
| 1 | `SPRING_PROFILES_ACTIVE` | `spring.profiles.active` | `src/main/docker/app.yml` |
| 2 | `SPRING_DATASOURCE_URL`、`SPRING_DATASOURCE_USERNAME`、`SPRING_DATASOURCE_PASSWORD` | `spring.datasource.*` | `src/main/docker/app.yml`、`src/main/docker/jib/entrypoint.sh` |
| 3 | `SPRING_LIQUIBASE_URL`、`SPRING_LIQUIBASE_USER`、`SPRING_LIQUIBASE_PASSWORD` | `spring.liquibase.*` | 同上 |
| 4 | `MANAGEMENT_PROMETHEUS_METRICS_EXPORT_ENABLED` | `management.prometheus.metrics.export.enabled` | `src/main/docker/app.yml` |
| 5 | `JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET` | `jhipster.security.authentication.jwt.base64-secret` | `src/main/docker/jhipster-control-center.yml`、リポジトリの `README.md` |

`src/main/docker/jib/entrypoint.sh` では、`SPRING_DATASOURCE_PASSWORD_FILE` のように変数名の末尾に `_FILE` を付けた環境変数で、ファイルに格納した値を読み込める（Docker の secrets 機能）。対象は `SPRING_DATASOURCE_URL`、`SPRING_DATASOURCE_USERNAME`、`SPRING_DATASOURCE_PASSWORD`、`SPRING_LIQUIBASE_URL`、`SPRING_LIQUIBASE_USER`、`SPRING_LIQUIBASE_PASSWORD`、`JHIPSTER_REGISTRY_PASSWORD` である。

---
