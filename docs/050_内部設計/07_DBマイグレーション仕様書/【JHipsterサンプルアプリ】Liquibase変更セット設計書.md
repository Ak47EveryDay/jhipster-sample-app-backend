# 【JHipsterサンプルアプリ】Liquibase変更セット設計書

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## (1)全体構成

データベースのスキーマは、JPA による自動生成ではなく、Liquibase の変更セットで管理する。JPA の設定は `spring.jpa.hibernate.ddl-auto: none` であり、テーブルの作成と変更はすべて Liquibase が行う。

### ファイル構成

| No | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- |
| 1 | `src/main/resources/config/liquibase/master.xml` | マスターの変更ログ。読み込む変更セットファイルと、DB ごとの型の定義を記述する |
| 2 | `src/main/resources/config/liquibase/changelog/00000000000000_initial_schema.xml` | ユーザ、権限とその関連のテーブル、シーケンス、初期データの読み込み |
| 3 | `src/main/resources/config/liquibase/changelog/20150805124838_added_entity_BankAccount.xml` | 口座テーブルとサンプルデータ |
| 4 | `src/main/resources/config/liquibase/changelog/20150805124838_added_entity_constraints_BankAccount.xml` | 口座テーブルの外部キー |
| 5 | `src/main/resources/config/liquibase/changelog/20150805124936_added_entity_Label.xml` | ラベルテーブルとサンプルデータ |
| 6 | `src/main/resources/config/liquibase/changelog/20150805125054_added_entity_Operation.xml` | 取引テーブル、取引・ラベル関連テーブルとサンプルデータ |
| 7 | `src/main/resources/config/liquibase/changelog/20150805125054_added_entity_constraints_Operation.xml` | 取引テーブルと取引・ラベル関連テーブルの外部キー |
| 8 | `src/main/resources/config/liquibase/data/*.csv` | 初期データ（権限、ユーザ、ユーザ・権限関連） |
| 9 | `src/main/resources/config/liquibase/fake-data/*.csv` | サンプルデータ（口座、ラベル、取引） |

### 実行方法

| 項目 | 内容 |
| --- | --- |
| 定義クラス | `LiquibaseConfiguration`（`src/main/java/io/github/jhipster/sample/config/LiquibaseConfiguration.java`） |
| Bean | `liquibase`（`SpringLiquibase`） |
| 変更ログの場所 | `classpath:config/liquibase/master.xml`（`setChangeLog`） |
| 実行タイミング | アプリケーションの起動時 |
| 非同期実行 | 設定 `application.liquibase.async-start` が `true`（既定値）の場合、`SpringLiquibaseUtil#createAsyncSpringLiquibase` で生成し、`taskExecutor` 上で非同期に実行する。`false` の場合は `SpringLiquibaseUtil#createSpringLiquibase` で同期実行する |
| 無効化 | Spring プロファイル `no-liquibase` が有効な場合は実行しない。それ以外は設定 `spring.liquibase.enabled` に従う |
| 適用対象の絞り込み | コンテキスト（`spring.liquibase.contexts`）とラベル（`spring.liquibase.label-filter`） |
| データソース | `spring.liquibase.url` を設定した場合は Liquibase 専用のデータソース、未設定の場合はアプリケーションのデータソース |

`application.liquibase.async-start` は `ApplicationProperties`（プレフィックス `application`）で読み込む。変更セットは適用済みかどうかを Liquibase の管理テーブルで判定し、未適用のものだけを適用する。

上の表の `spring.liquibase.enabled`、`spring.liquibase.label-filter`、`spring.liquibase.database-change-log-table`、`spring.liquibase.database-change-log-lock-table`、`spring.liquibase.url` は、Spring Boot の標準の設定キーである。本リポジトリの設定ファイルには記述がなく、既定値で使う。コードは `LiquibaseProperties` の getter と `@LiquibaseDataSource` を経由してこれらを参照する。

### DB ごとの型

`master.xml` の `<property>` で、DB ごとに型名を切り替える。変更セットでは `${プロパティ名}` の形式で参照する。

| プロパティ名 | h2 | postgresql | 用途 |
| --- | --- | --- | --- |
| `now` | `now()` | `current_timestamp` | 現在日時 |
| `floatType` | `float4` | `float4` | 浮動小数 |
| `uuidType` | `uuid` | `uuid` | UUID |
| `datetimeType` | `datetime(6)` | `datetime` | 日時（取引の `date` カラムで使用） |
| `timeType` | `time(6)` | `time(6)` | 時刻 |
| `clobType` | `longvarchar` | `clob` | 文字列のラージオブジェクト |
| `blobType` | `blob` | `blob` | バイナリのラージオブジェクト |

現在の変更セットで実際に使っているプロパティは `datetimeType` のみである。

### プロファイル別のコンテキスト

| Spring プロファイル | 設定ファイル | `spring.liquibase.contexts` | 意味 |
| --- | --- | --- | --- |
| `dev` | `src/main/resources/config/application-dev.yml` | `dev, faker` | サンプルデータを読み込む |
| `prod` | `src/main/resources/config/application-prod.yml` | `prod` | サンプルデータを読み込まない |
| テスト | `src/test/resources/config/application.yml` | `test` | テスト用のテーブルを作成する |

コンテキストの指定がない変更セットは、すべてのコンテキストで適用される。

---

## (2)変更セット一覧

`master.xml` の `<include>` の並び順に記載する。

| No | ファイル | changeSet id | author | context | 内容 |
| --- | --- | --- | --- | --- | --- |
| 1 | `00000000000000_initial_schema.xml` | `00000000000000` | jhipster | なし | シーケンス `sequence_generator` の作成 |
| 2 | 同上 | `00000000000001` | jhipster | なし | テーブル `jhi_user`、`jhi_authority`、`jhi_user_authority` の作成、初期データの読み込み |
| 3 | 同上 | `00000000000002` | jhipster | `test` | テスト用テーブル `jhi_date_time_wrapper` の作成 |
| 4 | `20150805124838_added_entity_BankAccount.xml` | `20150805124838-1` | jhipster | なし | テーブル `bank_account` の作成 |
| 5 | 同上 | `20150805124838-1-data` | jhipster | `faker` | サンプルデータ（口座）の読み込み |
| 6 | `20150805124936_added_entity_Label.xml` | `20150805124936-1` | jhipster | なし | テーブル `label` の作成 |
| 7 | 同上 | `20150805124936-1-data` | jhipster | `faker` | サンプルデータ（ラベル）の読み込み |
| 8 | `20150805125054_added_entity_Operation.xml` | `20150805125054-1` | jhipster | なし | テーブル `operation` の作成 |
| 9 | 同上 | `20150805125054-1-relations` | jhipster | なし | テーブル `rel_operation__label` の作成 |
| 10 | 同上 | `20150805125054-1-data` | jhipster | `faker` | サンプルデータ（取引）の読み込み |
| 11 | `20150805124838_added_entity_constraints_BankAccount.xml` | `20150805124838-2` | jhipster | なし | `bank_account` の外部キーの作成 |
| 12 | `20150805125054_added_entity_constraints_Operation.xml` | `20150805125054-2` | jhipster | なし | `operation`、`rel_operation__label` の外部キーの作成 |

初期データとサンプルデータの内容は「初期データ・サンプルデータ設計書」を参照。テーブルの項目定義は「05_TblDto仕様書」を参照。

---

## (3)変更セット詳細

### changeSet `00000000000000`（シーケンス）

| 項目 | 内容 |
| --- | --- |
| シーケンス名 | `sequence_generator` |
| 開始値 | 1050 |
| 増分 | 50 |

`User`、`BankAccount`、`Operation`、`Label` の各エンティティは、`@SequenceGenerator(name = "sequenceGenerator")` でこのシーケンスを使う。

### changeSet `00000000000001`（コアのテーブルと初期データ）

| No | 処理 | 対象 | 内容 |
| --- | --- | --- | --- |
| 1 | テーブル作成 | `jhi_user` | 項目は「05_TblDto仕様書 _jhi_user_TblDto」を参照 |
| 2 | テーブル作成 | `jhi_authority` | 同上（_jhi_authority_TblDto） |
| 3 | テーブル作成 | `jhi_user_authority` | 同上（_jhi_user_authority_TblDto） |
| 4 | 主キー追加 | `jhi_user_authority` | `user_id, authority_name` |
| 5 | 外部キー追加 | `jhi_user_authority` | `fk_authority_name`（`authority_name` → `jhi_authority.name`） |
| 6 | 外部キー追加 | `jhi_user_authority` | `fk_user_id`（`user_id` → `jhi_user.id`） |
| 7 | NOT NULL 制約追加 | `jhi_user` | `password_hash` |
| 8 | データ読み込み | `jhi_authority` | `config/liquibase/data/authority.csv` |
| 9 | データ読み込み | `jhi_user` | `config/liquibase/data/user.csv` |
| 10 | データ読み込み | `jhi_user_authority` | `config/liquibase/data/user_authority.csv` |
| 11 | 既定値の削除 | `jhi_user` | `created_date`（`dropDefaultValue`） |

### changeSet `00000000000002`（テスト用テーブル、context `test`）

| No | カラム名 | DB型 | NULL | キー |
| --- | --- | --- | --- | --- |
| 1 | `id` | BIGINT | NOT NULL | 主キー（`jhi_date_time_wrapperPK`） |
| 2 | `instant` | timestamp | NULL 可 | － |
| 3 | `local_date_time` | timestamp | NULL 可 | － |
| 4 | `offset_date_time` | timestamp | NULL 可 | － |
| 5 | `zoned_date_time` | timestamp | NULL 可 | － |
| 6 | `local_time` | time | NULL 可 | － |
| 7 | `offset_time` | time | NULL 可 | － |
| 8 | `local_date` | date | NULL 可 | － |

本テーブルはテストクラス `DateTimeWrapper`、`DateTimeWrapperRepository`（`src/test/java/io/github/jhipster/sample/repository/timezone/`）だけが使う。

### changeSet `20150805124838-1`、`20150805124936-1`、`20150805125054-1`（業務テーブルの作成）

| No | テーブル | 主キー | NOT NULL のカラム | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `bank_account` | `id` | `id`、`name`、`balance` | 項目は「05_TblDto仕様書 _bank_account_TblDto」を参照 |
| 2 | `label` | `id` | `id`、`label` | 同上（_label_TblDto） |
| 3 | `operation` | `id` | `id`、`date`、`amount` | 同上（_operation_TblDto）。作成後に `dropDefaultValue` で `date` の既定値を削除する |

### changeSet `20150805125054-1-relations`（取引・ラベル関連テーブル）

テーブル `rel_operation__label` を作成し、主キー（`operation_id, label_id`）を追加する。項目は「05_TblDto仕様書 _rel_operation__label_TblDto」を参照。

### changeSet `20150805124838-2`、`20150805125054-2`（外部キー）

| changeSet id | 外部キー名 | テーブル | カラム | 参照先 |
| --- | --- | --- | --- | --- |
| `20150805124838-2` | `fk_bank_account__user_id` | `bank_account` | `user_id` | `jhi_user.id` |
| `20150805125054-2` | `fk_operation__bank_account_id` | `operation` | `bank_account_id` | `bank_account.id` |
| `20150805125054-2` | `fk_rel_operation__label__operation_id` | `rel_operation__label` | `operation_id` | `operation.id` |
| `20150805125054-2` | `fk_rel_operation__label__label_id` | `rel_operation__label` | `label_id` | `label.id` |

---

## (4)管理テーブル

Liquibase は、適用済みの変更セットを管理するためのテーブルを対象のデータベースに作成し、更新する。テーブル名は設定 `spring.liquibase.database-change-log-table` と `spring.liquibase.database-change-log-lock-table` で変更できるが、本リポジトリでは既定のままである。アプリケーションのコードからこれらのテーブルを参照することはない。

---
