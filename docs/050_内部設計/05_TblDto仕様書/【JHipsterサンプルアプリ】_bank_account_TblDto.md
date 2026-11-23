# 【JHipsterサンプルアプリ】_bank_account_TblDto

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

### クラス情報

| 項目 | 内容 |
| --- | --- |
| テーブル名 | `bank_account` |
| テーブル論理名 | 銀行口座 |
| エンティティクラス名 | `BankAccount` |
| 基底クラス | なし（`Serializable` のみ実装。監査項目を持たない） |
| パッケージ | `io.github.jhipster.sample.domain` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/domain/BankAccount.java` |
| 主キー | `id`（シーケンス `sequence_generator` で採番） |
| キャッシュ | 第二レベルキャッシュを使用する（`READ_WRITE`） |
| テーブル定義の作成箇所 | `src/main/resources/config/liquibase/changelog/20150805124838_added_entity_BankAccount.xml`（変更セット `20150805124838-1`） |
| 外部キーの作成箇所 | `src/main/resources/config/liquibase/changelog/20150805124838_added_entity_constraints_BankAccount.xml`（変更セット `20150805124838-2`） |

### 項目定義

| No | プロパティ名 | 日本語名 | データ型 | カラム名 | DB型 | NULL | キー・制約 | validation属性 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | 口座ID | Long | `id` | bigint | NOT NULL | 主キー | なし | `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")` |
| 2 | `name` | 口座名 | String | `name` | varchar(255) | NOT NULL | － | `@NotNull` |  |
| 3 | `balance` | 残高 | BigDecimal | `balance` | decimal(21,2) | NOT NULL | － | `@NotNull` | `@Column(name = "balance", precision = 21, scale = 2, nullable = false)` |
| 4 | `user` | 所有ユーザ | `User` | `user_id` | bigint | NULL 可 | 外部キー `fk_bank_account__user_id` → `jhi_user.id` | なし | `@ManyToOne(fetch = FetchType.LAZY)` |
| 5 | `operations` | 取引の集合 | Set\<Operation\> | （カラムなし） | － | － | － | なし | `@OneToMany(fetch = FetchType.LAZY, mappedBy = "bankAccount")`。遅延読み込み。`@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)` |

`operations` には `@JsonIgnoreProperties(value = { "bankAccount", "labels" }, allowSetters = true)` を付与している。

### 関連

| 関連先 | プロパティ | 関連の種類 | 外部キー |
| --- | --- | --- | --- |
| `jhi_user` | `user` | 多対 1（口座から見て所有ユーザ） | `bank_account.user_id` |
| `operation` | `operations` | 1 対多（取引の `bankAccount` が所有側） | `operation.bank_account_id` |

### 双方向の関連を保つメソッド

`BankAccount` は関連の両側を同時に編集するメソッドを持つ。

| メソッド | 処理 |
| --- | --- |
| `setOperations(Set<Operation>)` | 既存の各取引の `bankAccount` を null にし、新しい各取引の `bankAccount` に自分を設定してから置き換える |
| `addOperation(Operation)` | 集合に追加し、取引の `bankAccount` に自分を設定する |
| `removeOperation(Operation)` | 集合から削除し、取引の `bankAccount` を null にする |

### 等価性の判定

`equals` は `id` が null でない場合に `id` だけで比較する。`hashCode` はクラスのハッシュ値を返す（`getClass().hashCode()`）。

---
