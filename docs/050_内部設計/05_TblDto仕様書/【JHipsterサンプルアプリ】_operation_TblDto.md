# 【JHipsterサンプルアプリ】_operation_TblDto

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

### クラス情報

| 項目 | 内容 |
| --- | --- |
| テーブル名 | `operation` |
| テーブル論理名 | 取引 |
| エンティティクラス名 | `Operation` |
| 基底クラス | なし（`Serializable` のみ実装。監査項目を持たない） |
| パッケージ | `io.github.jhipster.sample.domain` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/domain/Operation.java` |
| 主キー | `id`（シーケンス `sequence_generator` で採番） |
| キャッシュ | 第二レベルキャッシュを使用する（`READ_WRITE`） |
| テーブル定義の作成箇所 | `src/main/resources/config/liquibase/changelog/20150805125054_added_entity_Operation.xml`（変更セット `20150805125054-1`） |
| 外部キーの作成箇所 | `src/main/resources/config/liquibase/changelog/20150805125054_added_entity_constraints_Operation.xml`（変更セット `20150805125054-2`） |

### 項目定義

| No | プロパティ名 | 日本語名 | データ型 | カラム名 | DB型 | NULL | キー・制約 | validation属性 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | 取引ID | Long | `id` | bigint | NOT NULL | 主キー | なし | `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")` |
| 2 | `date` | 取引日時 | Instant | `date` | `${datetimeType}`（H2 は `datetime(6)`、PostgreSQL は `datetime`） | NOT NULL | － | `@NotNull` | ISO-8601 形式の文字列で入出力する |
| 3 | `description` | 説明 | String | `description` | varchar(255) | NULL 可 | － | なし |  |
| 4 | `amount` | 金額 | BigDecimal | `amount` | decimal(21,2) | NOT NULL | － | `@NotNull` | `@Column(name = "amount", precision = 21, scale = 2, nullable = false)` |
| 5 | `bankAccount` | 所属口座 | `BankAccount` | `bank_account_id` | bigint | NULL 可 | 外部キー `fk_operation__bank_account_id` → `bank_account.id` | なし | `@ManyToOne(fetch = FetchType.LAZY)` |
| 6 | `labels` | ラベルの集合 | Set\<Label\> | （結合テーブル `rel_operation__label`） | － | － | 多対多 | なし | 結合カラムは `operation_id`、`label_id`。遅延読み込み。`@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)` |

`bankAccount` には `@JsonIgnoreProperties(value = { "user", "operations" }, allowSetters = true)`、`labels` には `@JsonIgnoreProperties(value = { "operations" }, allowSetters = true)` を付与している。

`date` の DB 型は Liquibase のプロパティ `datetimeType` で DB ごとに切り替える。定義は `src/main/resources/config/liquibase/master.xml` である。

### 関連

| 関連先 | プロパティ | 関連の種類 | 外部キー・結合テーブル |
| --- | --- | --- | --- |
| `bank_account` | `bankAccount` | 多対 1 | `operation.bank_account_id` |
| `label` | `labels` | 多対多（所有側） | `rel_operation__label` |

### 関連を保つメソッド

| メソッド | 処理 |
| --- | --- |
| `addLabel(Label)` | 集合にラベルを追加する |
| `removeLabel(Label)` | 集合からラベルを削除する |

### 等価性の判定

`equals` は `id` が null でない場合に `id` だけで比較する。`hashCode` はクラスのハッシュ値を返す（`getClass().hashCode()`）。

---
