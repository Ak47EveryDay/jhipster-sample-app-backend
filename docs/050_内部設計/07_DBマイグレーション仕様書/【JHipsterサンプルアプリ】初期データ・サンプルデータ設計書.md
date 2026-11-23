# 【JHipsterサンプルアプリ】初期データ・サンプルデータ設計書

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## (1)データファイル一覧

初期データとサンプルデータは、Liquibase の変更セットから `loadData` で読み込む CSV ファイルである。

| No | ファイルパス（リポジトリルート基準） | 読み込み先テーブル | 読み込む変更セット | context | 件数 |
| --- | --- | --- | --- | --- | --- |
| 1 | `src/main/resources/config/liquibase/data/authority.csv` | `jhi_authority` | `00000000000001` | なし（常に読み込む） | 2 |
| 2 | `src/main/resources/config/liquibase/data/user.csv` | `jhi_user` | `00000000000001` | なし（常に読み込む） | 2 |
| 3 | `src/main/resources/config/liquibase/data/user_authority.csv` | `jhi_user_authority` | `00000000000001` | なし（常に読み込む） | 3 |
| 4 | `src/main/resources/config/liquibase/fake-data/bank_account.csv` | `bank_account` | `20150805124838-1-data` | `faker` | 30 |
| 5 | `src/main/resources/config/liquibase/fake-data/label.csv` | `label` | `20150805124936-1-data` | `faker` | 30 |
| 6 | `src/main/resources/config/liquibase/fake-data/operation.csv` | `operation` | `20150805125054-1-data` | `faker` | 30 |

`faker` コンテキストは `dev` プロファイルで有効になる（`spring.liquibase.contexts: dev, faker`）。`prod` プロファイルでは初期データ（No 1～3）だけを読み込む。変更セットの詳細は「Liquibase変更セット設計書」を参照。

CSV の形式はすべて共通である。

| 項目 | 内容 |
| --- | --- |
| 区切り文字 | `;` |
| 1 行目 | カラム名（見出し行） |
| 読み込み方法 | `usePreparedStatements="true"`。カラムごとに型を指定する（例 `id` は `numeric`、`activated` は `boolean`、`created_date` は `timestamp`） |

---

## (2)権限の初期データ（authority.csv）

| No | `name` | 日本語名 | 備考 |
| --- | --- | --- | --- |
| 1 | `ROLE_ADMIN` | 管理者権限 | ユーザ管理（`/api/admin/**`）、API ドキュメント（`/v3/api-docs/**`）、`/management/**` の一部で要求する |
| 2 | `ROLE_USER` | 一般ユーザ権限 | ユーザ登録（`POST /api/register`）で新規ユーザに付与する |

コード上の定義は `AuthoritiesConstants.ADMIN`、`AuthoritiesConstants.USER` である。

---

## (3)ユーザの初期データ（user.csv）

| No | `id` | `login` | `first_name` | `last_name` | `email` | `activated` | `lang_key` | `created_by` | `last_modified_by` | 権限 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | 1 | `admin` | Administrator | Administrator | `admin@localhost` | true | `en` | `system` | `system` | `ROLE_ADMIN`、`ROLE_USER` |
| 2 | 2 | `user` | User | User | `user@localhost` | true | `en` | `system` | `system` | `ROLE_USER` |

| 項目 | 内容 |
| --- | --- |
| `password_hash` | BCrypt のハッシュ（60 桁）。CSV に固定値として記載している |
| `image_url` | 空欄 |
| `created_date`、`reset_date`、`activation_key`、`reset_key` | CSV に列がないため、NULL となる |

平文のパスワードは CSV には記載されていない。ハッシュ値を照合すると、`admin` と `user` である。開発・検証用の初期データであり、既定のパスワードのまま運用しないこと。

---

## (4)ユーザ・権限関連の初期データ（user_authority.csv）

| No | `user_id` | `authority_name` | 備考 |
| --- | --- | --- | --- |
| 1 | 1 | `ROLE_ADMIN` | ユーザ `admin` |
| 2 | 1 | `ROLE_USER` | ユーザ `admin` |
| 3 | 2 | `ROLE_USER` | ユーザ `user` |

---

## (5)サンプルデータ（fake-data）

サンプルデータは Faker.js で生成した値である。`dev` プロファイルだけで読み込む。

| No | ファイル | カラム | id の範囲 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `bank_account.csv` | `id`、`name`、`balance` | 1～30 | `user_id` の列がないため、所有ユーザは NULL となる |
| 2 | `label.csv` | `id`、`label` | 1～30 |  |
| 3 | `operation.csv` | `id`、`date`、`description`、`amount` | 1～30 | `bank_account_id` の列がないため、所属口座は NULL となる。`date` は `date` 型として読み込む |

取引・ラベル関連（`rel_operation__label`）のサンプルデータはない。

シーケンス `sequence_generator` は 1050 から採番する。サンプルデータの id（1～30）と初期ユーザの id（1、2）は、シーケンスの採番範囲の外にある。

---
