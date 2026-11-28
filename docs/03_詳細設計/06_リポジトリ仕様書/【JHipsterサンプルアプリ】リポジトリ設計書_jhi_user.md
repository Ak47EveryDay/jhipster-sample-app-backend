# 【JHipsterサンプルアプリ】リポジトリ設計書_jhi_user

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## リポジトリ情報

| 項目 | 内容 |
| --- | --- |
| システム名 | JHipsterサンプルアプリ |
| パッケージ | `io.github.jhipster.sample.repository` |
| テーブル論理名 | ユーザ |
| テーブル名 | `jhi_user` |
| エンティティ | `User`（主キー `Long`） |
| リポジトリクラス物理名 | `UserRepository` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/repository/UserRepository.java` |
| 親インターフェース | `JpaRepository<User, Long>` |

リポジトリは Spring Data JPA のインターフェースである。SQL を直接記述せず、メソッド名から検索条件を導く派生クエリを使う。トランザクションはリポジトリでは管理せず、呼び出し元のサービスクラスが管理する。

---

## (1)メソッド一覧

検索条件は、メソッド名から導かれる条件である（`IgnoreCase` は大文字小文字を区別しない比較を表す）。

| No | メソッド名 | 引数 | 戻り値型 | 検索条件 | キャッシュ | 呼び出し元 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `findOneByActivationKey` | `String activationKey` | `Optional<User>` | `activation_key` ＝ 引数 | なし | `UserService#activateRegistration` |
| 2 | `findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore` | `Instant dateTime` | `List<User>` | `activated` ＝ false かつ `activation_key` IS NOT NULL かつ `created_date` ＜ 引数 | なし | `UserService#removeNotActivatedUsers` |
| 3 | `findOneByResetKey` | `String resetKey` | `Optional<User>` | `reset_key` ＝ 引数 | なし | `UserService#completePasswordReset` |
| 4 | `findOneWithAuthoritiesByEmailIgnoreCase` | `String email` | `Optional<User>` | `email` ＝ 引数（大文字小文字を区別しない）。権限（`authorities`）を同時に読み込む | `usersByEmail` | `DomainUserDetailsService#loadUserByUsername` |
| 5 | `findOneByEmailIgnoreCase` | `String email` | `Optional<User>` | `email` ＝ 引数（大文字小文字を区別しない） | なし | `AccountResource#saveAccount`、`UserResource#createUser`、`UserResource#updateUser`、`UserService#registerUser`、`UserService#requestPasswordReset` |
| 6 | `findOneWithAuthoritiesByLogin` | `String login` | `Optional<User>` | `login` ＝ 引数。権限（`authorities`）を同時に読み込む | `usersByLogin` | `DomainUserDetailsService#loadUserByUsername`、`UserService#getUserWithAuthoritiesByLogin`、`UserService#getUserWithAuthorities` |
| 7 | `findOneByLogin` | `String login` | `Optional<User>` | `login` ＝ 引数 | なし | `AccountResource#saveAccount`、`UserResource#createUser`、`UserResource#updateUser`、`UserService`（`registerUser`、`removeNonActivatedUser`、`updateUser`、`changePassword`、`deleteUser`） |
| 8 | `findAllByIdNotNullAndActivatedIsTrue` | `Pageable pageable` | `Page<User>` | `id` IS NOT NULL かつ `activated` ＝ true | なし | `UserService#getAllPublicUsers` |

### キャッシュ

| No | 定数名 | キャッシュ名 | 対象メソッド | 条件 |
| --- | --- | --- | --- | --- |
| 1 | `USERS_BY_LOGIN_CACHE` | `usersByLogin` | `findOneWithAuthoritiesByLogin` | `@Cacheable(cacheNames = USERS_BY_LOGIN_CACHE, unless = "#result == null")` |
| 2 | `USERS_BY_EMAIL_CACHE` | `usersByEmail` | `findOneWithAuthoritiesByEmailIgnoreCase` | `@Cacheable(cacheNames = USERS_BY_EMAIL_CACHE, unless = "#result == null")` |

検索結果が null の場合はキャッシュしない。キャッシュの削除は `UserService#clearUserCaches` が行う。詳細は「01_共通仕様書 キャッシュ設計書」を参照。

### 権限の同時読み込み

`findOneWithAuthoritiesByLogin` と `findOneWithAuthoritiesByEmailIgnoreCase` には `@EntityGraph(attributePaths = "authorities")` を付与している。遅延読み込みの `authorities` を、ユーザの検索と同じクエリで読み込む。

---

## (2)親インターフェースのメソッドの利用

`JpaRepository` から継承したメソッドのうち、主コードで使うものは次のとおりである。

| No | メソッド名 | 主コードでの呼び出し元 |
| --- | --- | --- |
| 1 | `findAll(Pageable)` | `UserService#getAllManagedUsers` |
| 2 | `findById(Long)` | `UserService#updateUser(AdminUserDTO)` |
| 3 | `save(User)` | `UserService`（`registerUser`、`createUser`、`updateUser` の 2 つのオーバーロード、`changePassword`） |
| 4 | `delete(User)` | `UserService`（`removeNonActivatedUser`、`deleteUser`、`removeNotActivatedUsers`） |
| 5 | `flush()` | `UserService#removeNonActivatedUser` |

---
