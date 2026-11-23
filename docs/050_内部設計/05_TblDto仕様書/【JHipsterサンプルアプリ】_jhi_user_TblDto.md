# 【JHipsterサンプルアプリ】_jhi_user_TblDto

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

### クラス情報

| 項目 | 内容 |
| --- | --- |
| テーブル名 | `jhi_user` |
| テーブル論理名 | ユーザ |
| エンティティクラス名 | `User` |
| 基底クラス | `AbstractAuditingEntity<Long>`（監査項目を持つ抽象クラス） |
| パッケージ | `io.github.jhipster.sample.domain` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/domain/User.java`、`src/main/java/io/github/jhipster/sample/domain/AbstractAuditingEntity.java` |
| 主キー | `id`（シーケンス `sequence_generator` で採番） |
| キャッシュ | 第二レベルキャッシュを使用する（`NONSTRICT_READ_WRITE`） |
| テーブル定義の作成箇所 | `src/main/resources/config/liquibase/changelog/00000000000000_initial_schema.xml`（変更セット `00000000000001`） |

### 項目定義

| No | プロパティ名 | 日本語名 | データ型 | カラム名 | DB型 | NULL | キー・制約 | validation属性 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | ユーザID | Long | `id` | bigint | NOT NULL | 主キー | なし | `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")` |
| 2 | `login` | ログインID | String | `login` | varchar(50) | NOT NULL | 一意（`ux_user_login`） | `@NotNull`、`@Pattern(regexp = Constants.LOGIN_REGEX)`、`@Size(min = 1, max = 50)` | setter が値を小文字化する |
| 3 | `password` | パスワードハッシュ | String | `password_hash` | varchar(60) | NOT NULL | － | `@NotNull`、`@Size(min = 60, max = 60)` | BCrypt のハッシュ。`@JsonIgnore` |
| 4 | `firstName` | 名 | String | `first_name` | varchar(50) | NULL 可 | － | `@Size(max = 50)` |  |
| 5 | `lastName` | 姓 | String | `last_name` | varchar(50) | NULL 可 | － | `@Size(max = 50)` |  |
| 6 | `email` | メールアドレス | String | `email` | varchar(191) | NULL 可 | 一意（`ux_user_email`） | `@Email`、`@Size(min = 5, max = 254)` | エンティティの `@Column` は `length = 254` だが、DB は varchar(191) で定義している |
| 7 | `activated` | 有効化状態 | boolean | `activated` | boolean | NOT NULL | － | `@NotNull` | 既定値 `false`（DB の既定値も false） |
| 8 | `langKey` | 言語キー | String | `lang_key` | varchar(10) | NULL 可 | － | `@Size(min = 2, max = 10)` |  |
| 9 | `imageUrl` | 画像URL | String | `image_url` | varchar(256) | NULL 可 | － | `@Size(max = 256)` |  |
| 10 | `activationKey` | 有効化キー | String | `activation_key` | varchar(20) | NULL 可 | － | `@Size(max = 20)` | ユーザ登録時に設定し、有効化後に null にする。`@JsonIgnore` |
| 11 | `resetKey` | リセットキー | String | `reset_key` | varchar(20) | NULL 可 | － | `@Size(max = 20)` | パスワードリセットの要求時に設定し、完了後に null にする。`@JsonIgnore` |
| 12 | `resetDate` | リセット要求日時 | Instant | `reset_date` | timestamp | NULL 可 | － | なし | リセットキーの有効期限（1 日）の起点 |
| 13 | `createdBy` | 登録者 | String | `created_by` | varchar(50) | NOT NULL | － | なし | 基底クラスの項目。`@CreatedBy`。更新不可（`updatable = false`） |
| 14 | `createdDate` | 登録日時 | Instant | `created_date` | timestamp | NULL 可 | － | なし | 基底クラスの項目。`@CreatedDate`。更新不可。既定値はインスタンス生成日時 |
| 15 | `lastModifiedBy` | 最終更新者 | String | `last_modified_by` | varchar(50) | NULL 可 | － | なし | 基底クラスの項目。`@LastModifiedBy` |
| 16 | `lastModifiedDate` | 最終更新日時 | Instant | `last_modified_date` | timestamp | NULL 可 | － | なし | 基底クラスの項目。`@LastModifiedDate`。既定値はインスタンス生成日時 |
| 17 | `authorities` | 権限の集合 | Set\<Authority\> | （結合テーブル `jhi_user_authority`） | － | － | 多対多 | なし | 結合カラムは `user_id`、`authority_name`。遅延読み込み。`@JsonIgnore`、`@BatchSize(size = 20)` |

### 監査項目の設定

監査項目（`createdBy`、`createdDate`、`lastModifiedBy`、`lastModifiedDate`）は `AbstractAuditingEntity` が持ち、`AuditingEntityListener` が設定する。設定者は `SpringSecurityAuditorAware#getCurrentAuditor` が返す値（現在ユーザのログインID、未認証の場合は `system`）である。

`AbstractAuditingEntity` には `@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)` を付与している。JSON としては出力するが、リクエスト本文からは受け取らない。

### 関連

| 関連先 | プロパティ | 関連の種類 | 結合テーブル |
| --- | --- | --- | --- |
| `jhi_authority` | `authorities` | 多対多（所有側） | `jhi_user_authority` |
| `bank_account` | （`User` 側にはプロパティがない） | 1 対多 | － |

### 等価性の判定

`equals` は `id` が null でない場合に `id` だけで比較する。`hashCode` はクラスのハッシュ値を返す（`getClass().hashCode()`）。

---
