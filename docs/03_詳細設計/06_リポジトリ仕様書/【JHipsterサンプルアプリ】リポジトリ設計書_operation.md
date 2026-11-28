# 【JHipsterサンプルアプリ】リポジトリ設計書_operation

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
| テーブル論理名 | 取引 |
| テーブル名 | `operation` |
| エンティティ | `Operation`（主キー `Long`） |
| リポジトリクラス物理名 | `OperationRepository` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/repository/OperationRepository.java` |
| 親インターフェース | `OperationRepositoryWithBagRelationships`、`JpaRepository<Operation, Long>` |
| 呼び出し元 | `OperationResource`（サービスクラスを介さない） |

---

## (1)構成

取引は、所属口座（多対 1 の `bankAccount`）とラベル（多対多の `labels`）の 2 つの関連を持つ。この 2 つを 1 つのクエリで同時に読み込むと Hibernate がエラー（複数のコレクションの一括読み込み）となるため、リポジトリを 2 段構成にしている。

| No | クラス | 種別 | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- | --- | --- |
| 1 | `OperationRepository` | Spring Data JPA のインターフェース | `src/main/java/io/github/jhipster/sample/repository/OperationRepository.java` | 所属口座（`bankAccount`）の一括読み込みと、全体の入口 |
| 2 | `OperationRepositoryWithBagRelationships` | カスタムフラグメントのインターフェース | `src/main/java/io/github/jhipster/sample/repository/OperationRepositoryWithBagRelationships.java` | ラベル（`labels`）の一括読み込みの宣言 |
| 3 | `OperationRepositoryWithBagRelationshipsImpl` | カスタムフラグメントの実装 | `src/main/java/io/github/jhipster/sample/repository/OperationRepositoryWithBagRelationshipsImpl.java` | ラベル（`labels`）の一括読み込みを `EntityManager` の JPQL で実装 |

No 3 は、インターフェース名（No 2）の末尾に `Impl` を付けたクラス名を持つため、Spring Data が No 2 の実装として認識する。

---

## (2)メソッド一覧

### 既定のメソッド（default メソッド）

「EagerRelationships」の名前を持つメソッドは、関連を同時に読み込むための入口である。まず所属口座を読み込み、次にラベルを読み込む。

| No | メソッド名 | 引数 | 戻り値型 | 処理 | 主コードでの呼び出し元 |
| --- | --- | --- | --- | --- | --- |
| 1 | `findOneWithEagerRelationships` | `Long id` | `Optional<Operation>` | `fetchBagRelationships(this.findOneWithToOneRelationships(id))` | `OperationResource#getOperation` |
| 2 | `findAllWithEagerRelationships` | なし | `List<Operation>` | `fetchBagRelationships(this.findAllWithToOneRelationships())` | なし（主コードでは未使用） |
| 3 | `findAllWithEagerRelationships` | `Pageable pageable` | `Page<Operation>` | `fetchBagRelationships(this.findOneWithToOneRelationships(pageable))` | `OperationResource#getAllOperations`（`eagerload` が true の場合） |

### JPQL を記述したメソッド（OperationRepository）

| No | メソッド名 | 引数 | 戻り値型 | JPQL | 呼び出し元 |
| --- | --- | --- | --- | --- | --- |
| 1 | `findAllWithToOneRelationships` | `Pageable pageable` | `Page<Operation>` | 問合せ: `select operation from Operation operation left join fetch operation.bankAccount`<br>件数: `select count(operation) from Operation operation` | `findAllWithEagerRelationships(Pageable)` |
| 2 | `findAllWithToOneRelationships` | なし | `List<Operation>` | `select operation from Operation operation left join fetch operation.bankAccount` | `findAllWithEagerRelationships()` |
| 3 | `findOneWithToOneRelationships` | `@Param("id") Long id` | `Optional<Operation>` | `select operation from Operation operation left join fetch operation.bankAccount where operation.id =:id` | `findOneWithEagerRelationships(Long)` |

### ラベルの一括読み込み（OperationRepositoryWithBagRelationships）

| No | メソッド名 | 引数 | 戻り値型 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `fetchBagRelationships` | `Optional<Operation> operation` | `Optional<Operation>` | 値がある場合、`fetchLabels(Operation)` を呼び出す |
| 2 | `fetchBagRelationships` | `Page<Operation> operations` | `Page<Operation>` | ページ内容のリストに対して `fetchLabels(List)` を呼び出し、ページ情報と総件数を引き継いだ `PageImpl` を返す |
| 3 | `fetchBagRelationships` | `List<Operation> operations` | `List<Operation>` | `fetchLabels(List)` を呼び出す（null の場合は空のリスト） |

`OperationRepositoryWithBagRelationshipsImpl` の JPQL は次のとおりである。

| No | 実装メソッド | JPQL | 備考 |
| --- | --- | --- | --- |
| 1 | `fetchLabels(Operation result)` | `select operation from Operation operation left join fetch operation.labels where operation.id = :id` | 1 件を取得する |
| 2 | `fetchLabels(List<Operation> operations)` | `select operation from Operation operation left join fetch operation.labels where operation in :operations` | 取得後、引数のリストの並び順（id の出現順）に並べ替えてから返す |

パラメータ名は `id` と `operations` である（定数 `ID_PARAMETER`、`OPERATIONS_PARAMETER`）。

### 親インターフェースのメソッドの利用

`JpaRepository` から継承したメソッドのうち、`OperationResource` で使うものは次のとおりである。

| No | メソッド名 | 用途 |
| --- | --- | --- |
| 1 | `save(Operation)` | 登録、全項目更新、部分更新 |
| 2 | `existsById(Long)` | 更新前の存在チェック |
| 3 | `findById(Long)` | 部分更新時の既存データの取得 |
| 4 | `findAll(Pageable)` | 一覧検索（`eagerload` が false の場合） |
| 5 | `deleteById(Long)` | 削除 |

---
