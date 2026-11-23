# 【JHipsterサンプルアプリ】DTOマッピング設計書

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　REST の入出力データの構成 |
| 2 | 2　データ構造の一覧 |
| 3 | 3　DTO とエンティティの対応 |
| 4 | 4　マッパークラス（UserMapper） |
| 5 | 5　JSON との変換 |

---

## 1　REST の入出力データの構成

REST のリクエスト本文と応答本文は、次の 3 種類のいずれかのクラスで受け渡す。

| No | 種類 | パッケージ | 用途 |
| --- | --- | --- | --- |
| 1 | DTO | `io.github.jhipster.sample.service.dto` | ユーザ関連の入出力。エンティティの `User` からパスワードなどの内部項目を除いたデータを表す |
| 2 | VM（ビューモデル） | `io.github.jhipster.sample.web.rest.vm` | REST 層だけで使う入力データ（ログイン情報、登録情報、リセットキー） |
| 3 | エンティティ | `io.github.jhipster.sample.domain` | 権限、口座、取引、ラベルの入出力。DTO を介さずエンティティを直接使う |

DAO の入出力用の DTO は持たない。リポジトリはエンティティをそのまま入出力とする。

---

## 2　データ構造の一覧

| No | クラス | 種別 | 対象のエンティティ | 使うエンドポイント | 役割 |
| --- | --- | --- | --- | --- | --- |
| 1 | `AdminUserDTO` | DTO | `User` | `GET`、`POST /api/account`、`POST`、`PUT`、`GET /api/admin/users` | リクエスト・応答 |
| 2 | `UserDTO` | DTO | `User` | `GET /api/users` | 応答（ユーザIDとログインIDのみ） |
| 3 | `PasswordChangeDTO` | DTO | － | `POST /api/account/change-password` | リクエスト |
| 4 | `LoginVM` | VM | － | `POST /api/authenticate` | リクエスト |
| 5 | `ManagedUserVM` | VM | `User` | `POST /api/register` | リクエスト |
| 6 | `KeyAndPasswordVM` | VM | － | `POST /api/account/reset-password/finish` | リクエスト |
| 7 | `JWTToken` | REST 層の内部クラス | － | `POST /api/authenticate` | 応答 |
| 8 | `Authority` | エンティティ | `jhi_authority` | `/api/authorities` の各エンドポイント | リクエスト・応答 |
| 9 | `BankAccount` | エンティティ | `bank_account` | `/api/bank-accounts` の各エンドポイント | リクエスト・応答 |
| 10 | `Operation` | エンティティ | `operation` | `/api/operations` の各エンドポイント | リクエスト・応答 |
| 11 | `Label` | エンティティ | `label` | `/api/labels` の各エンドポイント | リクエスト・応答 |
| 12 | `User` | エンティティ | `jhi_user` | `POST /api/admin/users` | 応答 |

各クラスの項目定義は、DTO と VM は各機能の「SERVICE_DTO設計書」、エンティティは「05_TblDto仕様書」を参照。

---

## 3　DTO とエンティティの対応

### エンティティから DTO への変換

DTO のコンストラクタで行う。

| No | DTO | コンストラクタ | 変換元の項目 |
| --- | --- | --- | --- |
| 1 | `AdminUserDTO` | `AdminUserDTO(User user)` | `User` の全項目（`id`、`login`、`firstName`、`lastName`、`email`、`activated`、`imageUrl`、`langKey`、監査項目）。`authorities` は権限名の集合（`Set<String>`）に変換 |
| 2 | `UserDTO` | `UserDTO(User user)` | `id` と `login` のみ |

`AdminUserDTO(User user)` は `user.getAuthorities()` を呼び出す。このため、権限を読み込み済みの `User` を渡す必要がある（`UserRepository#findOneWithAuthoritiesByLogin` などで取得したもの）。

### DTO からエンティティへの変換

一括で変換するメソッドはない。`UserService` の各メソッドが、DTO の項目を `User` に個別に設定する。

| No | メソッド | 入力 | 設定する `User` の項目 |
| --- | --- | --- | --- |
| 1 | `UserService#registerUser(AdminUserDTO userDTO, String password)` | `ManagedUserVM`（`AdminUserDTO` として受け取る）とパスワード | `login`（小文字化）、`password`（ハッシュ化）、`firstName`、`lastName`、`email`（小文字化）、`imageUrl`、`langKey`、`activated`（`false`）、`activationKey`、`authorities`（`ROLE_USER` のみ） |
| 2 | `UserService#createUser(AdminUserDTO userDTO)` | `AdminUserDTO` | `login`（小文字化）、`firstName`、`lastName`、`email`（小文字化）、`imageUrl`、`langKey`、`password`（ランダム生成）、`resetKey`、`resetDate`、`activated`（`true`）、`authorities` |
| 3 | `UserService#updateUser(AdminUserDTO userDTO)` | `AdminUserDTO` | `login`（小文字化）、`firstName`、`lastName`、`email`（小文字化）、`imageUrl`、`activated`、`langKey`、`authorities` |
| 4 | `UserService#updateUser(firstName, lastName, email, langKey, imageUrl)` | `AdminUserDTO` の項目（引数として渡す） | `firstName`、`lastName`、`email`（小文字化）、`langKey`、`imageUrl` |

DTO からエンティティへの変換で、DTO の `id`、`createdBy`、`createdDate`、`lastModifiedBy`、`lastModifiedDate` を `User` に設定することはない。監査項目は `AuditingEntityListener` が設定する。

### DTO 同士の関係

`ManagedUserVM` は `AdminUserDTO` を継承し、`password` を追加したクラスである。ユーザ登録では、`ManagedUserVM` をスーパークラスの `AdminUserDTO` として `UserService#registerUser` に渡し、`password` は第 2 引数で渡す。

---

## 4　マッパークラス（UserMapper）

| 項目 | 内容 |
| --- | --- |
| クラス | `UserMapper`（`@Service`） |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/service/mapper/UserMapper.java` |
| 性格 | 手で記述したマッパー（MapStruct の `@Mapper` ではない） |

| No | メソッド | 引数 | 戻り値 | 処理 |
| --- | --- | --- | --- | --- |
| 1 | `userToUserDTO` | `User` | `UserDTO` | `UserDTO` のコンストラクタを呼び出す |
| 2 | `usersToUserDTOs` | `List<User>` | `List<UserDTO>` | null の要素を除いて変換する |
| 3 | `userToAdminUserDTO` | `User` | `AdminUserDTO` | `AdminUserDTO` のコンストラクタを呼び出す |
| 4 | `usersToAdminUserDTOs` | `List<User>` | `List<AdminUserDTO>` | null の要素を除いて変換する |
| 5 | `userDTOToUser` | `AdminUserDTO` | `User` | 新規に `User` を生成し、項目を設定する。`authorities` は権限名から `Authority` を生成して設定する |
| 6 | `userDTOsToUsers` | `List<AdminUserDTO>` | `List<User>` | null の要素を除いて変換する |
| 7 | `userFromId` | `Long id` | `User` | `id` だけを設定した `User` を返す |
| 8 | `toDtoId`（`@Named("id")`） | `User` | `UserDTO` | `id` だけを設定した `UserDTO` を返す |
| 9 | `toDtoIdSet`（`@Named("idSet")`） | `Set<User>` | `Set<UserDTO>` | 各要素を `toDtoId` で変換する |
| 10 | `toDtoLogin`（`@Named("login")`） | `User` | `UserDTO` | `id` と `login` を設定した `UserDTO` を返す |
| 11 | `toDtoLoginSet`（`@Named("loginSet")`） | `Set<User>` | `Set<UserDTO>` | 各要素を `toDtoLogin` で変換する |

No 8～11 には MapStruct のアノテーション（`@Named`、`@BeanMapping`、`@Mapping`）を付けているが、クラス自体は手で記述しており、MapStruct はコードを生成しない。主コードに `UserMapper` の呼び出し元はない（テストの `UserMapperTest` だけが使う）。

---

## 5　JSON との変換

リクエスト本文の JSON と応答本文の JSON の変換は、Spring MVC と Jackson が行う。JSON のキーはプロパティ名（キャメルケース）である。本リポジトリが変換に関して個別に設定している点は次のとおりである。

| No | 項目 | 内容 | 定義箇所 |
| --- | --- | --- | --- |
| 1 | Hibernate 型の対応 | `Hibernate7Module` を登録する。遅延読み込みで未読み込みのオブジェクトは、識別子を出力する | `JacksonHibernateConfiguration` |
| 2 | 日時 | `Instant` 型の項目は ISO-8601 形式の文字列で入出力する（例 `Operation.date`） | － |
| 3 | 出力の整形 | `dev` はインデント付きで出力する（`spring.jackson.serialization.indent-output: true`） | `application-dev.yml` |
| 4 | 出力から除外する項目 | `User` の `password`、`activationKey`、`resetKey`、`authorities`（`@JsonIgnore`） | `User` |
| 5 | 入力として受け取らない項目 | 監査項目（`createdBy`、`createdDate`、`lastModifiedBy`、`lastModifiedDate`）。出力はする（`allowGetters = true`） | `AbstractAuditingEntity` |
| 6 | 入出力から除外する項目 | `Authority` の `new`、`id`（`id` は `Persistable#getId` による。主キーは `name` で入出力する） | `Authority` |
| 7 | 関連の出力から除外する項目 | `BankAccount.operations` の各取引の `bankAccount` と `labels`、`Label.operations` の各取引の `bankAccount` と `labels`、`Operation.bankAccount` の口座の `user` と `operations`、`Operation.labels` の各ラベルの `operations` | `BankAccount`、`Label`、`Operation` |
| 8 | キー名の変更 | `JWTToken` の `idToken` は `id_token` として出力する（`@JsonProperty`） | `AuthenticateController` |

No 7 は、関連が互いに参照し合って出力が無限に続くことを防ぐための設定である。

---
