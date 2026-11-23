# 【JHipsterサンプルアプリ】_jhi_user_authority_TblDto

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

### クラス情報

| 項目 | 内容 |
| --- | --- |
| テーブル名 | `jhi_user_authority` |
| テーブル論理名 | ユーザ・権限関連（ユーザと権限の多対多を結ぶ） |
| エンティティクラス名 | なし（`User.authorities` の `@JoinTable` として定義） |
| 定義箇所 | `src/main/java/io/github/jhipster/sample/domain/User.java`（`authorities` に付与した `@JoinTable`。`name` は `jhi_user_authority`） |
| テーブル定義の作成箇所 | `src/main/resources/config/liquibase/changelog/00000000000000_initial_schema.xml`（変更セット `00000000000001`） |

### 項目定義

| No | カラム名 | 日本語名 | DB型 | NULL | キー・制約 | 備考 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `user_id` | ユーザID | bigint | NOT NULL | 主キー（`authority_name` との複合）、外部キー `fk_user_id` → `jhi_user.id` | エンティティの `joinColumns` |
| 2 | `authority_name` | 権限名 | varchar(50) | NOT NULL | 主キー（`user_id` との複合）、外部キー `fk_authority_name` → `jhi_authority.name` | エンティティの `inverseJoinColumns` |

### 初期データ

初期データは `src/main/resources/config/liquibase/data/user_authority.csv` である。内容は「07_DBマイグレーション仕様書 初期データ・サンプルデータ設計書」を参照。

---
