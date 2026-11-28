# 【JHipsterサンプルアプリ】_label_TblDto

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

### クラス情報

| 項目 | 内容 |
| --- | --- |
| テーブル名 | `label` |
| テーブル論理名 | ラベル |
| エンティティクラス名 | `Label` |
| 基底クラス | なし（`Serializable` のみ実装。監査項目を持たない） |
| パッケージ | `io.github.jhipster.sample.domain` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/domain/Label.java` |
| 主キー | `id`（シーケンス `sequence_generator` で採番） |
| キャッシュ | 第二レベルキャッシュを使用する（`READ_WRITE`） |
| テーブル定義の作成箇所 | `src/main/resources/config/liquibase/changelog/20150805124936_added_entity_Label.xml`（変更セット `20150805124936-1`） |

### 項目定義

| No | プロパティ名 | 日本語名 | データ型 | カラム名 | DB型 | NULL | キー・制約 | validation属性 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | ラベルID | Long | `id` | bigint | NOT NULL | 主キー | なし | `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")` |
| 2 | `label` | ラベル名 | String | `label` | varchar(255) | NOT NULL | － | `@NotNull`、`@Size(min = 3)` | 3 桁以上。一意制約はない |
| 3 | `operations` | 取引の集合 | Set\<Operation\> | （結合テーブル `rel_operation__label`） | － | － | 多対多 | なし | `@ManyToMany(fetch = FetchType.LAZY, mappedBy = "labels")`。遅延読み込み。`@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)` |

`operations` には `@JsonIgnoreProperties(value = { "bankAccount", "labels" }, allowSetters = true)` を付与している。

### 関連

| 関連先 | プロパティ | 関連の種類 | 結合テーブル |
| --- | --- | --- | --- |
| `operation` | `operations` | 多対多（被所有側。`Operation.labels` の `mappedBy`） | `rel_operation__label` |

### 双方向の関連を保つメソッド

| メソッド | 処理 |
| --- | --- |
| `setOperations(Set<Operation>)` | 既存の各取引の `labels` から自分を削除し、新しい各取引の `labels` に自分を追加してから置き換える |
| `addOperation(Operation)` | 集合に追加し、取引の `labels` に自分を追加する |
| `removeOperation(Operation)` | 集合から削除し、取引の `labels` から自分を削除する |

### 等価性の判定

`equals` は `id` が null でない場合に `id` だけで比較する。`hashCode` はクラスのハッシュ値を返す（`getClass().hashCode()`）。

---
