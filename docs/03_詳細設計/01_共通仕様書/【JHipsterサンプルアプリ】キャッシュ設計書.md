# 【JHipsterサンプルアプリ】キャッシュ設計書

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 1　概要 |
| 2 | 2　構成 |
| 3 | 3　キャッシュ名一覧 |
| 4 | 4　Hibernate の第二レベルキャッシュ |
| 5 | 5　ユーザ検索のキャッシュ（Spring Cache） |
| 6 | 6　キャッシュの削除 |
| 7 | 7　プロファイル別の設定 |

---

## 1　概要

キャッシュはアプリケーションのプロセス内に持つ（Ehcache、JCache 経由）。Redis などの外部のキャッシュサーバは使わない。用途は 2 つである。

| No | 用途 | 使う側 | 根拠 |
| --- | --- | --- | --- |
| 1 | エンティティとそのコレクションの第二レベルキャッシュ | Hibernate | エンティティの `@Cache` アノテーション |
| 2 | ログインID、メールアドレスによるユーザ検索（権限を含む）の結果 | Spring Cache | `UserRepository` の `@Cacheable` アノテーション |

全レベルのキャッシュを 1 つのキャッシュマネージャで管理するため、プロファイルによるキャッシュ方式の切り替えはない（`dev` と `prod` でキャッシュの最大件数だけが異なる）。

---

## 2　構成

| No | クラス | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- | --- |
| 1 | `CacheConfiguration` | `src/main/java/io/github/jhipster/sample/config/CacheConfiguration.java` | キャッシュの有効化、キャッシュマネージャと Hibernate の連携、キャッシュの作成 |
| 2 | `CacheKeyGeneratorConfiguration` | `src/main/java/io/github/jhipster/sample/config/CacheKeyGeneratorConfiguration.java` | Spring Cache のキー生成器の Bean 定義 |
| 3 | `UserService` | `src/main/java/io/github/jhipster/sample/service/UserService.java` | キャッシュの削除（`clearUserCaches`） |
| 4 | `UserRepository` | `src/main/java/io/github/jhipster/sample/repository/UserRepository.java` | 検索結果のキャッシュ（`@Cacheable`） |

`CacheConfiguration` の処理は次のとおりである。

| No | 処理 | 内容 |
| --- | --- | --- |
| 1 | キャッシュの有効化 | クラスに `@EnableCaching` を付与する |
| 2 | キャッシュの共通設定 | ヒープ上に、最大 `jhipster.cache.ehcache.max-entries` 件、生存期間 `jhipster.cache.ehcache.time-to-live-seconds` 秒のキャッシュを作る設定を生成する（`ExpiryPolicyBuilder.timeToLiveExpiration`） |
| 3 | Hibernate との連携 | `HibernatePropertiesCustomizer` で、Hibernate の第二レベルキャッシュが JCache のキャッシュマネージャを使うように設定する（`ConfigSettings.CACHE_MANAGER`） |
| 4 | キャッシュの作成 | `JCacheManagerCustomizer` で、起動時にキャッシュ名の一覧を作成する。同名のキャッシュが既にあれば内容を消去し、なければ作成する |

`CacheKeyGeneratorConfiguration` は `PrefixedKeyGenerator`（JHipster ライブラリ）を `KeyGenerator` の Bean として登録する。このキー生成器は、キャッシュのキーに接頭辞を付ける。接頭辞には Git の短いコミット ID、ビルドの時刻、ビルドのバージョンの順で最初に見つかった値を使い、いずれもない場合はランダムな文字列を使う。ビルドごとにキーが変わるため、以前のビルドのキャッシュを使わない。キーは、接頭辞、メソッド名、引数からなる `PrefixedSimpleKey` である。

---

## 3　キャッシュ名一覧

| No | キャッシュ名 | 用途 | 使う側 |
| --- | --- | --- | --- |
| 1 | `usersByLogin` | ログインIDによるユーザ検索（権限を含む）の結果 | Spring Cache |
| 2 | `usersByEmail` | メールアドレスによるユーザ検索（権限を含む）の結果 | Spring Cache |
| 3 | `io.github.jhipster.sample.domain.User` | ユーザのエンティティ | Hibernate |
| 4 | `io.github.jhipster.sample.domain.User.authorities` | ユーザの権限の集合 | Hibernate |
| 5 | `io.github.jhipster.sample.domain.Authority` | 権限のエンティティ | Hibernate |
| 6 | `io.github.jhipster.sample.domain.BankAccount` | 口座のエンティティ | Hibernate |
| 7 | `io.github.jhipster.sample.domain.BankAccount.operations` | 口座の取引の集合 | Hibernate |
| 8 | `io.github.jhipster.sample.domain.Label` | ラベルのエンティティ | Hibernate |
| 9 | `io.github.jhipster.sample.domain.Label.operations` | ラベルの取引の集合 | Hibernate |
| 10 | `io.github.jhipster.sample.domain.Operation` | 取引のエンティティ | Hibernate |
| 11 | `io.github.jhipster.sample.domain.Operation.labels` | 取引のラベルの集合 | Hibernate |

キャッシュ名は `CacheConfiguration#cacheManagerCustomizer` に記述している（No 3 以降はクラスの完全修飾名、コレクションは「クラス名.プロパティ名」）。

---

## 4　Hibernate の第二レベルキャッシュ

| 項目 | 設定 |
| --- | --- |
| 第二レベルキャッシュ | 有効（`spring.jpa.properties.hibernate.cache.use_second_level_cache: true`） |
| クエリキャッシュ | 無効（`spring.jpa.properties.hibernate.cache.use_query_cache: false`） |

エンティティ側の設定は次のとおりである。

| エンティティ | プロパティ | `@Cache` の `usage` | 備考 |
| --- | --- | --- | --- |
| `User` | （クラス） | `NONSTRICT_READ_WRITE` |  |
| `User` | `authorities` | `NONSTRICT_READ_WRITE` | `@BatchSize(size = 20)` も付与 |
| `Authority` | （クラス） | `READ_WRITE` |  |
| `BankAccount` | （クラス） | `READ_WRITE` |  |
| `BankAccount` | `operations` | `READ_WRITE` |  |
| `Label` | （クラス） | `READ_WRITE` |  |
| `Label` | `operations` | `READ_WRITE` |  |
| `Operation` | （クラス） | `READ_WRITE` |  |
| `Operation` | `labels` | `READ_WRITE` |  |

`READ_WRITE` は読み書きの整合性を厳密に保つ方式、`NONSTRICT_READ_WRITE` は同一データの同時更新が起きにくいデータ向けの方式である（Hibernate の規定による）。エンティティのキャッシュは Hibernate が管理し、エンティティの更新時に無効化される。アプリケーションのコードから直接操作することはない。

---

## 5　ユーザ検索のキャッシュ（Spring Cache）

`UserRepository` の 2 つのメソッドに `@Cacheable` を付与している。

| メソッド | キャッシュ名 | キー | 条件 |
| --- | --- | --- | --- |
| `findOneWithAuthoritiesByLogin(String login)` | `usersByLogin` | 引数の `login` | 結果が null の場合はキャッシュしない（`unless = "#result == null"`） |
| `findOneWithAuthoritiesByEmailIgnoreCase(String email)` | `usersByEmail` | 引数の `email` | 同上 |

検索結果が空の `Optional` の場合はキャッシュされる。キャッシュの生存期間は「7　プロファイル別の設定」のとおりである。

キャッシュされた `User` は権限（`authorities`）を読み込み済みの状態で保持される。利用側はこのメソッドを、権限をキャッシュから取得するために使う（`UserResource` のクラスコメントに、ユーザと権限の関連を遅延読み込みのままにし、権限はキャッシュで補う意図が記されている）。

---

## 6　キャッシュの削除

ユーザを登録、更新、削除したときは、`UserService#clearUserCaches(User user)` で該当ユーザの検索結果をキャッシュから削除する。

| No | キャッシュ名 | 削除するキー | 条件 |
| --- | --- | --- | --- |
| 1 | `usersByLogin` | `user.getLogin()` | 常に |
| 2 | `usersByEmail` | `user.getEmail()` | `email` が null 以外の場合のみ |

削除には `evictIfPresent` を使う。`clearUserCaches` を呼び出すメソッドは次のとおりである。

| No | メソッド | 呼び出しのタイミング |
| --- | --- | --- |
| 1 | `activateRegistration` | 有効化の後 |
| 2 | `completePasswordReset` | パスワード再設定の後 |
| 3 | `requestPasswordReset` | リセットキーの設定の後 |
| 4 | `registerUser` | 保存の後、および既存の未有効化ユーザの削除の後 |
| 5 | `removeNonActivatedUser` | 削除の後 |
| 6 | `createUser` | 保存の後 |
| 7 | `updateUser(AdminUserDTO)` | 更新の前と保存の後 |
| 8 | `updateUser(String, String, String, String, String)` | 保存の後 |
| 9 | `changePassword` | 保存の後 |
| 10 | `deleteUser` | 削除の後 |
| 11 | `removeNotActivatedUsers` | 各ユーザの削除の後 |

キャッシュはプロセス内に持つため、アプリケーションを複数のインスタンスで動かす場合はインスタンス間でキャッシュは共有されない。

---

## 7　プロファイル別の設定

| No | キー | `dev` | `prod` | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `jhipster.cache.ehcache.max-entries` | 100 | 1000 | キャッシュごとの最大件数 |
| 2 | `jhipster.cache.ehcache.time-to-live-seconds` | 3600 | 3600 | 生存期間（秒） |

テスト用のプロファイル（`testdev`、`testprod`）では第二レベルキャッシュを無効にする（`spring.jpa.properties.hibernate.cache.use_second_level_cache: false`）。

---
