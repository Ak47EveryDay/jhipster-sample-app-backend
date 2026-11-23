# 【JHipsterサンプルアプリ】非同期・定期処理設計書

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　処理一覧 |
| 2 | 2　非同期処理の設定（AsyncConfiguration） |
| 3 | 3　メール送信の非同期実行 |
| 4 | 4　定期処理 |
| 5 | 5　Liquibase の非同期実行 |
| 6 | 6　テスト用の設定 |

---

## 1　処理一覧

リクエストの処理とは別のスレッドで実行する処理は、次の 3 つである。メッセージキューなどの外部の仕組みは使わない。

| No | 処理名 | 起動の方法 | 実装箇所 | 設計書 |
| --- | --- | --- | --- | --- |
| 1 | メール送信 | リクエストの処理から `@Async` のメソッドを呼び出す | `MailService` | メール送信設計書 |
| 2 | 未有効化ユーザ一括削除 | `@Scheduled` により毎日 01:00 に実行 | `UserService#removeNotActivatedUsers` | サービス詳細設計書_未有効化ユーザ一括削除 |
| 3 | Liquibase によるスキーマの更新 | アプリケーションの起動時に非同期で実行 | `LiquibaseConfiguration` | Liquibase変更セット設計書 |

---

## 2　非同期処理の設定（AsyncConfiguration）

| 項目 | 内容 |
| --- | --- |
| クラス | `AsyncConfiguration` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/config/AsyncConfiguration.java` |
| 実装インターフェース | `AsyncConfigurer` |
| クラスのアノテーション | `@Configuration`、`@EnableAsync`、`@EnableScheduling`、`@Profile("!testdev & !testprod")` |
| Bean 名 | `taskExecutor`（`Executor`） |

`@Profile` により、テスト用のプロファイル（`testdev`、`testprod`）ではこの設定は読み込まれない。

### 非同期処理用のスレッドプール

| No | 項目 | 設定キー | 値 |
| --- | --- | --- | --- |
| 1 | 基本サイズ | `spring.task.execution.pool.core-size` | 2 |
| 2 | 最大サイズ | `spring.task.execution.pool.max-size` | 50 |
| 3 | キューの容量 | `spring.task.execution.pool.queue-capacity` | 10000 |
| 4 | スレッド名の接頭辞 | `spring.task.execution.thread-name-prefix` | `jhipster-sample-application-task-` |

`ThreadPoolTaskExecutor` を `ExceptionHandlingAsyncTaskExecutor`（JHipster ライブラリ）でラップしたものを返す。戻り値のない非同期メソッドで発生した未処理の例外は、`getAsyncUncaughtExceptionHandler` が返す `SimpleAsyncUncaughtExceptionHandler` がログに出力する。

### 定期処理用のスレッドプール

| No | 項目 | 設定キー | 値 |
| --- | --- | --- | --- |
| 1 | サイズ | `spring.task.scheduling.pool.size` | 2 |
| 2 | スレッド名の接頭辞 | `spring.task.scheduling.thread-name-prefix` | `jhipster-sample-application-scheduling-` |

---

## 3　メール送信の非同期実行

`MailService` の公開メソッドには `@Async` を付与している。リソースクラスから呼び出すと、呼び出し元のリクエスト処理を待たずに `taskExecutor` 上で実行される。

実装上の特徴は次のとおりである。

| No | 項目 | 内容 |
| --- | --- | --- |
| 1 | 実行のタイミング | 呼び出し元のトランザクションの完了を待たない |
| 2 | 引数 | 呼び出し元で登録・更新した `User` をそのまま渡す。テンプレートは遅延読み込みのプロパティを使わない |
| 3 | 例外 | `MailService` が送信の例外を捕捉し、警告ログを出力する。呼び出し元には伝わらない |
| 4 | 呼び出し箇所 | `AccountResource#registerAccount`、`AccountResource#requestPasswordReset`、`UserResource#createUser` |

---

## 4　定期処理

定期処理は「未有効化ユーザ一括削除」の 1 つだけである。`@Scheduled(cron = "0 0 1 * * ?")` により、毎日 01:00 に定期処理用のスレッドプールで実行する。抽出条件と処理の内容は「サービス詳細設計書_未有効化ユーザ一括削除」を参照。

リトライの仕組みはない。処理で例外が発生した場合はトランザクションがロールバックされ、翌日の実行を待つことになる。

---

## 5　Liquibase の非同期実行

設定 `application.liquibase.async-start` が `true`（既定値）の場合、Liquibase は `AsyncSpringLiquibase` でラップされ、`taskExecutor` 上で起動時に実行される。そのため、アプリケーションがリクエストを受け付けられる状態になった直後は、スキーマの更新がまだ完了していない可能性がある。詳細は「Liquibase変更セット設計書」を参照。

---

## 6　テスト用の設定

統合テストでは非同期処理を同期で実行する。`AsyncSyncConfiguration`（`src/test/java/io/github/jhipster/sample/config/AsyncSyncConfiguration.java`）が、Bean 名 `taskExecutor` で `SyncTaskExecutor` を定義する。テスト用の基本アノテーション `IntegrationTest`（`src/test/java/io/github/jhipster/sample/IntegrationTest.java`）が、この設定をテストのアプリケーションに加える。

テスト用のプロファイルでは `AsyncConfiguration` が読み込まれないため、`@EnableScheduling` も無効で、定期処理は実行されない。

---
