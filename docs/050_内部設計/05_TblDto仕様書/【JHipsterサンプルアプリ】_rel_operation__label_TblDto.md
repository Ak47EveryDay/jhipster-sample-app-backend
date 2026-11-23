# 【JHipsterサンプルアプリ】_rel_operation__label_TblDto

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

### クラス情報

| 項目 | 内容 |
| --- | --- |
| テーブル名 | `rel_operation__label` |
| テーブル論理名 | 取引・ラベル関連（取引とラベルの多対多を結ぶ） |
| エンティティクラス名 | なし（`Operation.labels` の `@JoinTable` として定義） |
| 定義箇所 | `src/main/java/io/github/jhipster/sample/domain/Operation.java`（`labels` に付与した `@JoinTable`。`name` は `rel_operation__label`） |
| テーブル定義の作成箇所 | `src/main/resources/config/liquibase/changelog/20150805125054_added_entity_Operation.xml`（変更セット `20150805125054-1-relations`） |
| 外部キーの作成箇所 | `src/main/resources/config/liquibase/changelog/20150805125054_added_entity_constraints_Operation.xml`（変更セット `20150805125054-2`） |

### 項目定義

| No | カラム名 | 日本語名 | DB型 | NULL | キー・制約 | 備考 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `operation_id` | 取引ID | bigint | NOT NULL | 主キー（`label_id` との複合）、外部キー `fk_rel_operation__label__operation_id` → `operation.id` | エンティティの `joinColumns` |
| 2 | `label_id` | ラベルID | bigint | NOT NULL | 主キー（`operation_id` との複合）、外部キー `fk_rel_operation__label__label_id` → `label.id` | エンティティの `inverseJoinColumns` |

主キーのカラム順は `operation_id, label_id` である。

---
