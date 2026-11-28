# 【JHipsterサンプルアプリ】_jhi_authority_TblDto

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

### クラス情報

| 項目 | 内容 |
| --- | --- |
| テーブル名 | `jhi_authority` |
| テーブル論理名 | 権限 |
| エンティティクラス名 | `Authority` |
| 実装インターフェース | `Persistable<String>`、`Serializable` |
| パッケージ | `io.github.jhipster.sample.domain` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/domain/Authority.java` |
| 主キー | `name`（採番しない。値を指定して登録する） |
| キャッシュ | 第二レベルキャッシュを使用する（`READ_WRITE`） |
| テーブル定義の作成箇所 | `src/main/resources/config/liquibase/changelog/00000000000000_initial_schema.xml`（変更セット `00000000000001`） |

### 項目定義

| No | プロパティ名 | 日本語名 | データ型 | カラム名 | DB型 | NULL | キー・制約 | validation属性 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `name` | 権限名 | String | `name` | varchar(50) | NOT NULL | 主キー | `@NotNull`、`@Size(max = 50)` | 例 `ROLE_ADMIN`、`ROLE_USER` |
| 2 | `isPersisted` | 登録済みフラグ | boolean | （カラムなし） | － | － | － | － | `@Transient`。`@PostLoad`、`@PostPersist` のタイミングで `updateEntityState` が true にする |

`isPersisted` は `Persistable#isNew` の戻り値に使い、保存時に登録（INSERT）と更新（MERGE）を切り替えるために使う。テーブルにはカラムを持たない。

### JSON 変換

クラスに `@JsonIgnoreProperties(value = { "new", "id" })` を付与している。JSON に含まれる項目は `name` のみである。

### 関連

| 関連先 | プロパティ | 関連の種類 | 結合テーブル |
| --- | --- | --- | --- |
| `jhi_user` | （`Authority` 側にはプロパティがない） | 多対多（`User.authorities` の被所有側） | `jhi_user_authority` |

### 等価性の判定

`equals` は `name` が null でない場合に `name` だけで比較する。`hashCode` は `name` のハッシュ値を返す。

---
