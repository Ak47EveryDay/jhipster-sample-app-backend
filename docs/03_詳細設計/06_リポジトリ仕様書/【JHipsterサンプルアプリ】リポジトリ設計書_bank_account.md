# 【JHipsterサンプルアプリ】リポジトリ設計書_bank_account

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
| テーブル論理名 | 銀行口座 |
| テーブル名 | `bank_account` |
| エンティティ | `BankAccount`（主キー `Long`） |
| リポジトリクラス物理名 | `BankAccountRepository` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/repository/BankAccountRepository.java` |
| 親インターフェース | `JpaRepository<BankAccount, Long>` |
| 呼び出し元 | `BankAccountResource`（サービスクラスを介さない） |

---

## (1)メソッド一覧

### JPQL を記述したメソッド

| No | メソッド名 | 引数 | 戻り値型 | JPQL | 呼び出し元 |
| --- | --- | --- | --- | --- | --- |
| 1 | `findByUserIsCurrentUser` | なし | `List<BankAccount>` | `select bankAccount from BankAccount bankAccount where bankAccount.user.login = ?#{authentication.name}` | なし（主コードでは未使用） |
| 2 | `findAllWithToOneRelationships` | `Pageable pageable` | `Page<BankAccount>` | 問合せ: `select bankAccount from BankAccount bankAccount left join fetch bankAccount.user`<br>件数: `select count(bankAccount) from BankAccount bankAccount` | `findAllWithEagerRelationships(Pageable)`（主コードでは未使用） |
| 3 | `findAllWithToOneRelationships` | なし | `List<BankAccount>` | `select bankAccount from BankAccount bankAccount left join fetch bankAccount.user` | `findAllWithEagerRelationships()` |
| 4 | `findOneWithToOneRelationships` | `@Param("id") Long id` | `Optional<BankAccount>` | `select bankAccount from BankAccount bankAccount left join fetch bankAccount.user where bankAccount.id =:id` | `findOneWithEagerRelationships(Long)` |

No 1 は、Spring Security の認証プリンシパルの名前（現在ユーザのログインID）をクエリのパラメータに使う記法である。

### 既定のメソッド（default メソッド）

「EagerRelationships」の名前を持つメソッドは、関連（所有ユーザ `user`）を同時に読み込むための入口である。

| No | メソッド名 | 引数 | 戻り値型 | 処理 | 主コードでの呼び出し元 |
| --- | --- | --- | --- | --- | --- |
| 1 | `findOneWithEagerRelationships` | `Long id` | `Optional<BankAccount>` | `findOneWithToOneRelationships(id)` を呼び出す | `BankAccountResource#getBankAccount` |
| 2 | `findAllWithEagerRelationships` | なし | `List<BankAccount>` | `findAllWithToOneRelationships()` を呼び出す | `BankAccountResource#getAllBankAccounts`（`eagerload` が true の場合） |
| 3 | `findAllWithEagerRelationships` | `Pageable pageable` | `Page<BankAccount>` | `findAllWithToOneRelationships(pageable)` を呼び出す | なし（主コードでは未使用） |

### 親インターフェースのメソッドの利用

`JpaRepository` から継承したメソッドのうち、`BankAccountResource` で使うものは次のとおりである。

| No | メソッド名 | 用途 |
| --- | --- | --- |
| 1 | `save(BankAccount)` | 登録、全項目更新、部分更新 |
| 2 | `existsById(Long)` | 更新前の存在チェック |
| 3 | `findById(Long)` | 部分更新時の既存データの取得 |
| 4 | `findAll()` | 一覧検索（`eagerload` が false の場合） |
| 5 | `deleteById(Long)` | 削除 |

---
