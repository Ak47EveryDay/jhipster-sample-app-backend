# 【JHipsterサンプルアプリ】リポジトリ設計書_jhi_authority

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
| テーブル論理名 | 権限 |
| テーブル名 | `jhi_authority` |
| エンティティ | `Authority`（主キー `String`） |
| リポジトリクラス物理名 | `AuthorityRepository` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/repository/AuthorityRepository.java` |
| 親インターフェース | `JpaRepository<Authority, String>` |

---

## (1)メソッド一覧

リポジトリには独自のメソッドはない。`JpaRepository` から継承したメソッドのうち、主コードで使うものは次のとおりである。

| No | メソッド名 | 引数 | 戻り値型 | 呼び出し元 |
| --- | --- | --- | --- | --- |
| 1 | `findById` | `String name` | `Optional<Authority>` | `UserService`（`registerUser`、`createUser`、`updateUser`） |
| 2 | `findAll` | なし | `List<Authority>` | `UserService#getAuthorities`、`AuthorityResource#getAllAuthorities` |
| 3 | `existsById` | `String name` | `boolean` | `AuthorityResource#createAuthority` |
| 4 | `save` | `Authority` | `Authority` | `AuthorityResource#createAuthority` |
| 5 | `deleteById` | `String name` | なし | `AuthorityResource#deleteAuthority` |

`UserService#getAuthorities` は主コードからの呼び出し元がない。

---
