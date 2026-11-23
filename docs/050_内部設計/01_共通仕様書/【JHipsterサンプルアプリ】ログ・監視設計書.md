# 【JHipsterサンプルアプリ】ログ・監視設計書

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　ログの基本設定 |
| 2 | 2　プロファイル別のログレベル |
| 3 | 3　JSON 形式と Logstash への出力 |
| 4 | 4　アプリケーションのログ出力箇所 |
| 5 | 5　ログアスペクト（LoggingAspect） |
| 6 | 6　改行文字の置き換え |
| 7 | 7　管理エンドポイント（Actuator） |
| 8 | 8　メトリクス |
| 9 | 9　監視基盤（Docker Compose） |
| 10 | 10　コンテナのヘルスチェック |

---

## 1　ログの基本設定

| 項目 | 内容 |
| --- | --- |
| ログの実装 | SLF4J と Logback |
| 設定ファイル | `src/main/resources/logback-spring.xml` |
| 出力先 | コンソール（Spring Boot 標準のコンソール出力先を組み込む） |
| ルートのレベル | 設定 `logging.level.root`（既定値 `INFO`） |
| アプリケーションのパッケージ | `io.github.jhipster.sample` を `INFO` に設定（設定ファイルで上書きされるまで） |
| パターン | メッセージ部分に `%crlf(%m){red}` を使う（「6　改行文字の置き換え」） |

ファイルへの出力と非同期の出力先の定義は、設定ファイル内にコメントアウトしてある（本番向けの例として残している）。現状はコンソールへの出力のみである。

設定ファイルでは、フレームワークの主要なロガー（`org.springframework`、`org.hibernate`、`org.ehcache`、`org.thymeleaf`、`org.postgresql` など）を `WARN` に設定している。

---

## 2　プロファイル別のログレベル

| ロガー | `dev` | `prod` |
| --- | --- | --- |
| ルート（`ROOT`） | `DEBUG` | `INFO` |
| `tech.jhipster` | `DEBUG` | `INFO` |
| `org.hibernate.SQL` | `DEBUG` | （設定なし。`org.hibernate` の `WARN` に従う） |
| `io.github.jhipster.sample` | `DEBUG` | `INFO` |

設定ファイルは `application-dev.yml`、`application-prod.yml` である。

---

## 3　JSON 形式と Logstash への出力

`LoggingConfiguration`（`src/main/java/io/github/jhipster/sample/config/LoggingConfiguration.java`）が、設定に応じて Logback に出力先を追加する。

| No | 設定キー | 条件 | 処理 |
| --- | --- | --- | --- |
| 1 | `jhipster.logging.use-json-format` | `true` の場合 | JSON 形式のコンソール出力先を追加する（`addJsonConsoleAppender`） |
| 2 | `jhipster.logging.logstash.enabled` | `true` の場合 | Logstash への TCP 出力先を追加する（`addLogstashTcpSocketAppender`）。接続先は `host`、`port`、バッファは `ring-buffer-size` |
| 3 | No 1 または No 2 | いずれかが `true` の場合 | コンテキストリスナーを追加する（`addContextListener`） |

出力先には、アプリケーション名（`app_name`）とポート（`app_port`）を共通のフィールドとして付ける。`dev` と `prod` の既定値は、JSON 形式と Logstash のいずれも `false` である。

---

## 4　アプリケーションのログ出力箇所

主コードが直接出力するログのうち、主要なものは次のとおりである。

| No | クラス | レベル | 内容 |
| --- | --- | --- | --- |
| 1 | 各リソースクラス | DEBUG | リクエストの受付（`REST request to ...`） |
| 2 | `UserService` | DEBUG | ユーザの作成、変更、削除、有効化、パスワード変更、パスワードリセット |
| 3 | `MailService` | DEBUG | メールの送信内容と送信先 |
| 4 | `MailService` | WARN | メールの送信失敗（`Email could not be sent to user '{}'`） |
| 5 | `AccountResource` | WARN | 存在しないメールアドレスへのパスワードリセットの要求（`Password reset requested for non existing mail`） |
| 6 | `SecurityJwtConfiguration` | ERROR | 分類できない JWT の検証エラー（`Unknown JWT error {}`） |
| 7 | `ExceptionTranslator` | DEBUG | 例外の変換（スタックトレース付き） |
| 8 | `JhipsterSampleApplicationApp` | INFO | 起動時のアプリケーション情報（URL、有効なプロファイル） |
| 9 | `JhipsterSampleApplicationApp` | ERROR | プロファイルの誤設定（`dev` と `prod` の同時有効、`dev` と `cloud` の同時有効） |
| 10 | `LoggingAspect` | DEBUG、ERROR | 「5　ログアスペクト」 |

`ExceptionTranslator` が失敗アラートのヘッダを生成するとき、`HeaderUtil`（JHipster ライブラリ）がエラーログ `Entity processing failed, {}` を出力する。

---

## 5　ログアスペクト（LoggingAspect）

| 項目 | 内容 |
| --- | --- |
| クラス | `LoggingAspect`（`src/main/java/io/github/jhipster/sample/aop/logging/LoggingAspect.java`） |
| 有効化 | `LoggingAspectConfiguration`（`src/main/java/io/github/jhipster/sample/config/LoggingAspectConfiguration.java`） |
| 有効なプロファイル | `dev` のみ（`LoggingAspectConfiguration` の Bean 定義に `@Profile` が付いている） |
| AOP の有効化 | `LoggingAspectConfiguration` の `@EnableAspectJAutoProxy` |

### 対象範囲

2 つのポイントカットの組み合わせで、対象を決める。

| ポイントカット | 条件 |
| --- | --- |
| `applicationPackagePointcut` | パッケージ `io.github.jhipster.sample.repository`、`io.github.jhipster.sample.service`、`io.github.jhipster.sample.web.rest` 配下のクラスである |
| `springBeanPointcut` | クラスに `@Repository`、`@Service`、`@RestController` のいずれかが付いている |

対象となるのは、リポジトリ、サービス、REST の各層の Spring Bean である。`security` と `config` のパッケージは対象外である。

### 出力内容

| No | アドバイス | レベル | 出力内容 |
| --- | --- | --- | --- |
| 1 | `logAround` | DEBUG | メソッドの開始（`Enter: {メソッド名}() with argument[s] = {引数}`）と終了（`Exit: {メソッド名}() with result = {結果}`） |
| 2 | `logAround` | ERROR | `IllegalArgumentException` が発生した場合（`Illegal argument: {引数} in {メソッド名}()`） |
| 3 | `logAfterThrowing` | ERROR | 例外が発生した場合。`dev` のときは原因、例外のメッセージ、スタックトレースを出力する。`dev` 以外のときは原因のみを出力する |

ロガー名は、対象メソッドを宣言したクラスの名前である。引数と結果は `toString` の結果を出力する。DTO と VM のうちパスワードを持つ `LoginVM`、`AdminUserDTO`、`ManagedUserVM` は、`toString` にパスワードを含めていない。

---

## 6　改行文字の置き換え

ログメッセージ中の改行文字（CR、LF、タブ）をアンダースコアに置き換え、ログへの不正な行の挿入を防ぐ。対象外とする条件は「共通Utilクラス」の `CRLFLogConverter` を参照。

---

## 7　管理エンドポイント（Actuator）

| 項目 | 内容 |
| --- | --- |
| 実装 | Spring Boot Actuator |
| 基底パス | `/management` |
| 公開する設定 | `management.endpoints.web.exposure.include`（`src/main/resources/config/application.yml`） |
| API ドキュメントへの掲載 | 設定 `springdoc.show-actuator: true` |

| No | パス | 内容 | アクセス条件 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `/management/health` | ヘルスチェック | 認証不要 | 詳細は `ROLE_ADMIN` のユーザにだけ表示する（`show-details: when_authorized`） |
| 2 | `/management/health/liveness` | liveness グループ（`livenessState`） | 認証不要 | Kubernetes の liveness probe 向け |
| 3 | `/management/health/readiness` | readiness グループ（`readinessState`、`db`） | 認証不要 | Kubernetes の readiness probe 向け |
| 4 | `/management/info` | アプリケーションの情報（Git 情報、`info.*` の設定値） | 認証不要 |  |
| 5 | `/management/prometheus` | Prometheus 形式のメトリクス | 認証不要 | `prod` では既定で無効 |
| 6 | `/management/configprops` | 設定プロパティの一覧 | `ROLE_ADMIN` |  |
| 7 | `/management/env` | 環境変数とプロパティの一覧 | `ROLE_ADMIN` |  |
| 8 | `/management/loggers` | ロガーの一覧とレベルの変更 | `ROLE_ADMIN` |  |
| 9 | `/management/logfile` | ログファイルの内容 | `ROLE_ADMIN` | ログファイルの出力が有効なときだけ |
| 10 | `/management/threaddump` | スレッドダンプ | `ROLE_ADMIN` |  |
| 11 | `/management/caches` | キャッシュの一覧 | `ROLE_ADMIN` |  |
| 12 | `/management/liquibase` | Liquibase の変更セットの適用状況 | `ROLE_ADMIN` |  |
| 13 | `/management/jhimetrics` | JHipster のメトリクス | `ROLE_ADMIN` | アクセス権は読み取り専用 |
| 14 | `/management/jhiopenapigroups` | Open API のグループ | `ROLE_ADMIN` |  |

No 13、No 14 は JHipster ライブラリ（`jhipster-framework`）が提供するエンドポイントである。メールのヘルスチェックは無効にしている（`management.health.mail.enabled: false`）。

---

## 8　メトリクス

| 項目 | 設定 |
| --- | --- |
| 実装 | Micrometer（Prometheus レジストリ） |
| 出力の有効化 | `management.prometheus.metrics.export.enabled`（共通は `true`、`prod` は `false`） |
| 集計間隔 | 60 秒（`management.prometheus.metrics.export.step`） |
| 共通のタグ | `application`（`spring.application.name` の値） |
| 収集するグループ | `http`、`jvm`、`logback`、`process`、`system` |
| 分布 | すべてのメトリクスでパーセンタイル（`0`、`0.5`、`0.75`、`0.95`、`0.99`、`1.0`）とパーセンタイルヒストグラムを有効にする |
| リポジトリ | リポジトリメソッドの実行時間を自動で計測する（`management.metrics.data.repository.autotime.enabled`） |

アプリケーションが独自に定義するメトリクスは、JWT の検証エラーのカウンタ（`security.authentication.invalid-tokens`、タグ `cause`）である。詳細は「セキュリティ設計書」を参照。

---

## 9　監視基盤（Docker Compose）

開発・検証用に、Prometheus と Grafana を起動する Docker Compose の定義を用意している。

| No | ファイルパス（リポジトリルート基準） | 内容 |
| --- | --- | --- |
| 1 | `src/main/docker/monitoring.yml` | `prometheus`（`prom/prometheus:v3.14.0`、ポート 9090）と `grafana`（`grafana/grafana:13.2.2`、ポート 3000）の定義 |
| 2 | `src/main/docker/prometheus/prometheus.yml` | 取得の設定。`localhost:8080/management/prometheus` を 5 秒間隔で取得する |
| 3 | `src/main/docker/grafana/provisioning/datasources/datasource.yml` | Grafana のデータソース（Prometheus、`http://localhost:9090`） |
| 4 | `src/main/docker/grafana/provisioning/dashboards/dashboard.yml` | ダッシュボードの読み込み設定 |
| 5 | `src/main/docker/grafana/provisioning/dashboards/JVM.json` | JVM のダッシュボードの定義 |

各ファイルのコメントに記載のとおり、開発向けの設定であり、本番環境では強化が必要である。

---

## 10　コンテナのヘルスチェック

コンテナの定義 `src/main/docker/app.yml` の `healthcheck` は、`/management/health` にリクエストし、応答に `"status":"UP"` が含まれるかを確認する（5 秒間隔、最大 40 回）。

---
