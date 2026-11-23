# REST・エンティティ追加手順

> **位置づけ**：REST エンドポイント、エンティティとマイグレーションを追加するときの手順を、既存コードを見本にして示したものである。各レイヤーの書き方の詳細は `レイヤー別開発ガイド.md`、名前の付け方は `ネーミング・コーディング規約.md`、テストの書き方は `テスト作成ガイド.md` を参照。
> 「見本」として挙げるファイルは、すべてリポジトリに実在する。新しいファイルは見本をコピーし、必要な箇所だけを書き換えて作成する。
> 本リポジトリは JHipster で再生成できない（`README.md`）。エンティティやエンドポイントの追加は、コードの手書きで行う。
> パスは `src/main/java/io/github/jhipster/sample/` 基準で表記する。

---

## 1. やりたいことと該当する節

| やりたいこと | 節 |
|---|---|
| 既存の REST コントローラにエンドポイントを追加する | 3 節 |
| エンティティを伴わない REST コントローラを新しく作る | 4 節 |
| エンドポイントを未認証で呼べるようにする | 5 節 |
| エンドポイントを管理者限定にする | 6 節 |
| 新しいエンティティ（テーブル）を追加する | 7 節 |
| 既存のエンティティに項目を追加する | 8 節 |
| 作成・変更するファイルの一覧を確認する | 9 節 |

## 2. 共通の確認事項

### 2.1 セキュリティ

新しいエンドポイントのパスを決めたら、`config/SecurityConfiguration.java` の `filterChain` のルールでどう扱われるかを先に確認する。

| パス | 何も設定しなかった場合の扱い |
|---|---|
| `/api/admin/...` | `ROLE_ADMIN` が必要 |
| `/api/...`（`/api/admin/` の下を除く） | 認証が必要 |
| `/api` と `/management` のいずれにも入らないパス | 拒否される（未認証なら `401`、認証済みなら `403`） |

### 2.2 レイヤーの依存方向

新しいクラスを置くパッケージと、それが参照するクラスのパッケージの組み合わせが、`TechnicalStructureTest` の規則に適合することを確認する（`レイヤー別開発ガイド.md` の 3 節）。とくに、`service` から `web.rest` のクラスを参照しないこと。

## 3. 既存の REST コントローラにエンドポイントを追加する

`web/rest/LabelResource.java` の `getLabel` を、メソッドの書き方の見本にする。

```java
/**
 * {@code GET  /labels/:id} : get the "id" label.
 *
 * @param id the id of the label to retrieve.
 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the label, or with status {@code 404 (Not Found)}.
 */
@GetMapping("/{id}")
public ResponseEntity<Label> getLabel(@PathVariable("id") Long id) {
    LOG.debug("REST request to get Label : {}", id);
    Optional<Label> label = labelRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(label);
}
```

| 手順 | 作業 | 見本 |
|---|---|---|
| 1 | パスを決める。クラスの `@RequestMapping` の下に、`@GetMapping` などの注釈でパスを追加する | `web/rest/LabelResource.java` の `@RequestMapping("/api/labels")` |
| 2 | DB の検索や保存が必要なら、リポジトリにメソッドを追加する | `repository/BankAccountRepository.java` の `@Query` を使うメソッド |
| 3 | コントローラのメソッドを追加する。Javadoc、`LOG.debug`、入力チェック、レスポンスの組み立てを書く | `web/rest/LabelResource.java`、`web/rest/BankAccountResource.java` |
| 4 | 入力チェックに失敗したときの例外を用意する | `web/rest/BankAccountResource.java` の `BadRequestAlertException` |
| 5 | 対応する統合テストにケースを追加する | テストソースの `web/rest/LabelResourceIT.java` |
| 6 | `./mvnw verify` で単体テストと統合テストを実行する | `README.md` の「Building and testing」 |

### 3.1 メソッドに付ける要素と見本

| 要素 | 書き方 | 見本 |
|---|---|---|
| Javadoc | `{@code GET  /labels/:id} : get the "id" label.` の書式で、HTTP メソッド、パス、目的を書く | 各 `*Resource` |
| ログ | メソッドの先頭に `LOG.debug("REST request to <処理> <対象> : {}", ...)` を置く | 各 `*Resource` |
| リクエストボディの入力チェック | 引数に `@Valid @RequestBody` を付ける。オブジェクト内の項目には Bean Validation の注釈を付ける | `web/rest/LabelResource.java` の `createLabel`、`domain/Label.java` の `@NotNull` と `@Size` |
| パス変数の入力チェック | `@PathVariable` と併せて `@Pattern` などを付ける | `web/rest/UserResource.java` の `getUser` |
| メソッド引数の入力チェック | クラスに `@Validated` を付け、引数に制約の注釈を付ける | `web/rest/AccountResource.java` の `requestPasswordReset` |
| 404 を返す | `ResponseUtil.wrapOrNotFound(Optional)` を使う | `getLabel`、`getBankAccount` |
| 400 を返す | `BadRequestAlertException(メッセージ, ENTITY_NAME, エラーキー)` を投げる | `createLabel` の `idexists` |
| ページングを伴う一覧 | 引数に `@org.springdoc.core.annotations.ParameterObject Pageable pageable` を取り、`PaginationUtil.generatePaginationHttpHeaders(...)` でヘッダーを組み立てる | `web/rest/OperationResource.java` の `getAllOperations` |
| 関連を含めて返す | リポジトリの `find...WithEagerRelationships` を使う | `getBankAccount`、`web/rest/OperationResource.java` の `getOperation` |
| 成功時のヘッダー | `HeaderUtil.createEntityCreationAlert` などを `ResponseEntity` の `headers` に設定する | `createLabel`、`updateLabel`、`deleteLabel` |

- `@Transactional(rollbackFor = Exception.class)` がクラスに付いているのは、`AuthorityResource`、`BankAccountResource`、`LabelResource`、`OperationResource` の 4 クラスだけである。これらに追加したメソッドもそのトランザクションの中で動く。`AccountResource`、`UserResource`、`PublicUserResource` には付いていない。DB の更新は `UserService` のトランザクションの中で行われ、REST コントローラ自体はトランザクションを持たない。
- ページングを伴う一覧でソート対象を制限する場合は、`web/rest/UserResource.java` の `ALLOWED_ORDERED_PROPERTIES` と `onlyContainsAllowedProperties` を見本にする。

## 4. エンティティを伴わない REST コントローラを新しく作る

`web/rest/PublicUserResource.java`（サービスを呼ぶ最小の構成）と `web/rest/UserResource.java`（管理者向けの構成）を見本にする。

| 手順 | 作成するファイル | 見本 |
|---|---|---|
| 1 | REST コントローラ `web/rest/<担当>Resource.java`。`@RestController` と `@RequestMapping("/api/<パス>")` を付ける | `web/rest/PublicUserResource.java` |
| 2 | サービス `service/<担当>Service.java`。`@Service`、コンストラクタ注入、`@Transactional` を付ける | `service/UserService.java` |
| 3 | 入出力のオブジェクト `service/dto/<内容>DTO.java`。入力専用のオブジェクトは `web/rest/vm/<内容>VM.java` に置く | `service/dto/AdminUserDTO.java`、`web/rest/vm/LoginVM.java` |
| 4 | 統合テスト（テストソース）`web/rest/<担当>ResourceIT.java` | `web/rest/PublicUserResourceIT.java` |

- 処理をサービスに置く場合は、業務例外は `service` パッケージに置く（`service/UsernameAlreadyUsedException.java` を参照）。エラー応答への変換は `web/rest/errors/ExceptionTranslator.java` が行う。対応する変換がない例外は、`ExceptionTranslator` に追加する必要がある。

## 5. エンドポイントを未認証で呼べるようにする

1. `config/SecurityConfiguration.java` の `filterChain` 内にある `authorizeHttpRequests` のブロックに、`.requestMatchers("/api/<パス>").permitAll()` を追加する。
2. 追加する位置は、`.requestMatchers("/api/**").authenticated()` より前である。既存の `permitAll()` の並び（`/api/register`、`/api/activate` など）に合わせて置く。
3. テストでは、未認証でリクエストを送るメソッドに `@WithMockUser` を付けない（テストソースの `web/rest/AccountResourceIT.java` の `testRegisterValid`）。明示的に未認証にしたいときは `@WithUnauthenticatedMockUser` を付ける（同クラスの `testNonAuthenticatedUser`）。

- CSRF は無効で、セッションも使わない。エンドポイントの保護は、このパスのルールだけで決まる。
- 既存の未認証エンドポイントは、ログイン、ユーザー登録、アカウントの有効化、パスワードリセットの 4 種類である（`モジュール構成と共通部品.md` の 2.1 節）。

## 6. エンドポイントを管理者限定にする

既存のコードには 2 通りの方法がある。

| 方法 | URL のルール | メソッドの注釈 | 見本 |
|---|---|---|---|
| `/api/admin/` の下に置く | `/api/admin/**` に `ROLE_ADMIN` が必要 | 各メソッドに `@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")` も付ける | `web/rest/UserResource.java` |
| `/api/` の下に置き、メソッドで制限する | 「認証が必要」のルールに含まれる | 各メソッドに `@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")` を付ける | `web/rest/AuthorityResource.java` |

テストでは、クラスまたはメソッドに `@WithMockUser(authorities = AuthoritiesConstants.ADMIN)` を付ける（テストソースの `web/rest/UserResourceIT.java`）。テストソースの `web/rest/AuthorityResourceIT.java` は、同じ権限を文字列で `@WithMockUser(authorities = { "ROLE_ADMIN" })` と指定している。

## 7. 新しいエンティティ（テーブル）を追加する

`Label` の一式を最小の見本にする。`Label` にはほかのエンティティへの参照（多対 1 や外部キー）がない。外部キーを持つ場合は `BankAccount`（`user_id`）、結合テーブルを伴う場合は `Operation`（`rel_operation__label`）を参照する。

| 手順 | 作成・変更するファイル | 見本 |
|---|---|---|
| 1 | エンティティ `domain/<エンティティ>.java` | `domain/Label.java` |
| 2 | リポジトリ `repository/<エンティティ>Repository.java` | `repository/LabelRepository.java`（関連をまとめて読む場合は `repository/BankAccountRepository.java`） |
| 3 | チェンジログ `src/main/resources/config/liquibase/changelog/<yyyyMMddHHmmss>_added_entity_<エンティティ>.xml` | `src/main/resources/config/liquibase/changelog/20150805124936_added_entity_Label.xml` |
| 4 | 親チェンジログ `src/main/resources/config/liquibase/master.xml` に `<include>` を追加する | 同ファイルの `<include file="config/liquibase/changelog/20150805124936_added_entity_Label.xml" relativeToChangelogFile="false"/>` |
| 5 | （外部キーがある場合）制約のチェンジログ `<yyyyMMddHHmmss>_added_entity_constraints_<エンティティ>.xml` と、`master.xml` への `<include>` | `20150805124838_added_entity_constraints_BankAccount.xml` |
| 6 | （サンプルデータが必要な場合）`src/main/resources/config/liquibase/fake-data/<テーブル名>.csv` と、`context="faker"` のチェンジセット | `src/main/resources/config/liquibase/fake-data/label.csv`、手順 3 のチェンジログ内の `-data` チェンジセット |
| 7 | （`@Cache` を付ける場合）`config/CacheConfiguration.java` の `cacheManagerCustomizer` に `createCache` を追加 | 同クラスの `createCache(cm, io.github.jhipster.sample.domain.Label.class.getName())` |
| 8 | REST コントローラ `web/rest/<エンティティ>Resource.java` | `web/rest/LabelResource.java` |
| 9 | テスト（テストソース）`domain/<エンティティ>Test.java`、`domain/<エンティティ>TestSamples.java`、`domain/<エンティティ>Asserts.java`、`web/rest/<エンティティ>ResourceIT.java` | `domain/LabelTest.java`、`domain/LabelTestSamples.java`、`domain/LabelAsserts.java`、`web/rest/LabelResourceIT.java` |
| 10 | （負荷試験が必要な場合）`src/test/java/gatling/simulations/<エンティティ>GatlingTest.java` | `src/test/java/gatling/simulations/LabelGatlingTest.java` |

### 7.1 チェンジログの形式

手順 3 の見本の内容は次のとおりである（コメントは省略）。

```xml
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd
                        http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">

    <changeSet id="20150805124936-1" author="jhipster">
        <createTable tableName="label">
            <column name="id" type="bigint">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="label" type="varchar(255)">
                <constraints nullable="false" />
            </column>
        </createTable>
    </changeSet>

    <changeSet id="20150805124936-1-data" author="jhipster" context="faker">
        <loadData
                  file="../fake-data/label.csv"
                  relativeToChangelogFile="true"
                  separator=";"
                  tableName="label"
                  usePreparedStatements="true">
            <column name="id" type="numeric"/>
            <column name="label" type="string"/>
        </loadData>
    </changeSet>
</databaseChangeLog>
```

| 決めること | 既存のやり方 | 根拠 |
|---|---|---|
| ファイル名 | `<yyyyMMddHHmmss>_added_entity_<エンティティ>.xml`。タイムスタンプは既存のファイルより後ろになる値にする | `config/liquibase/changelog/` の既存ファイル |
| チェンジセットの ID と author | ID は `<タイムスタンプ>-1`、`author` は `jhipster` | 既存のチェンジログ |
| テーブル名とカラム名 | スネークケース。エンティティの `@Table(name = ...)`、`@Column(name = ...)` と一致させる | `domain/Label.java` |
| 主キー | `id` を `bigint` の主キーにする。エンティティは既存と同じ `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")` を使う。シーケンス `sequence_generator` は初期スキーマで作成済み | `00000000000000_initial_schema.xml` の `createSequence` |
| 日時のカラム | 型に `${datetimeType}` を使い、チェンジセットの最後で `<dropDefaultValue>` を記述する | `20150805125054_added_entity_Operation.xml` |
| 金額のカラム | `decimal(21,2)` | `20150805124838_added_entity_BankAccount.xml` の `balance` |
| 外部キー | 参照先の id のカラム（`user_id` など）をテーブルに作り、制約は別ファイルの `-2` チェンジセットで追加する | `20150805124838_added_entity_BankAccount.xml`、`20150805124838_added_entity_constraints_BankAccount.xml` |
| 多対多 | 結合テーブルを `-1-relations` チェンジセットで作り、主キーは 2 列の複合にする | `20150805125054_added_entity_Operation.xml` |
| `master.xml` への `<include>` | テーブルを作るチェンジログは `jhipster-needle-liquibase-add-changelog` の位置に、制約のチェンジログは `jhipster-needle-liquibase-add-constraints-changelog` の位置に追加する。`relativeToChangelogFile="false"` を付ける | `config/liquibase/master.xml` |

- `context` を付けていないチェンジセットは、どのプロファイルでも実行される。`faker` を付けたチェンジセットは、`faker` コンテキストを指定する dev プロファイルだけで実行される。
- `spring.jpa.hibernate.ddl-auto` は `none` である。エンティティを変更しても、チェンジログを追加しなければ DB は変わらない。
- エンティティとリポジトリは、既存の設定のままで読み込まれる（エンティティは起動クラスのパッケージ配下、リポジトリは `config/DatabaseConfiguration.java` の `@EnableJpaRepositories` で指定した `io.github.jhipster.sample.repository` 配下に置く）。

## 8. 既存のエンティティに項目を追加する

| 手順 | 作業 | 見本・根拠 |
|---|---|---|
| 1 | エンティティにフィールドとアクセサーを追加する。`@Column(name = "...")` と Bean Validation の注釈を付ける | `domain/Operation.java` の `description`、`amount` |
| 2 | チェンジログを新しく作り、`master.xml` の `jhipster-needle-liquibase-add-incremental-changelog` の位置で `<include>` する | `config/liquibase/master.xml` |
| 3 | 部分更新（PATCH）の対象にするなら、`web/rest/<エンティティ>Resource.java` の `partialUpdate<Entity>` に `updateIfPresent(existing::set<項目>, <引数>.get<項目>())` を追加する | `web/rest/OperationResource.java` の `partialUpdateOperation` |
| 4 | テストソースの `domain/<エンティティ>Asserts.java` の `assert<エンティティ>UpdatableFieldsEquals` に、新しい項目の検証を追加する | `domain/OperationAsserts.java` |
| 5 | 必須項目なら、`web/rest/<エンティティ>ResourceIT.java` に `check<項目>IsRequired` を追加する | テストソースの `web/rest/BankAccountResourceIT.java` |

- 本リポジトリには、既存のテーブルにカラムを追加したチェンジログの見本がない。新しいチェンジログは、7.1 の形式（名前空間の宣言、`<changeSet id author>` の書き方）に合わせて作ること。
- 手順 3 を行わないと、その項目は PUT では更新できても PATCH では更新できない状態になる。
- エンティティと DB の定義の差分は、`pom.xml` の `liquibase-maven-plugin` に設定がある `liquibase:diff` で確認できる（DB に接続できる環境が必要）。

## 9. 作成・変更するファイルの一覧

| 変更の種類 | 本体 | リソース | テストソース |
|---|---|---|---|
| 既存のコントローラにエンドポイントを追加 | `web/rest/<エンティティ>Resource.java`（変更）、必要なら `repository/<エンティティ>Repository.java`（変更） | なし | `web/rest/<エンティティ>ResourceIT.java`（変更） |
| 未認証にする | `config/SecurityConfiguration.java`（変更） | なし | 未認証で呼ぶケースを追加 |
| エンティティを伴わないコントローラを追加 | `web/rest/<担当>Resource.java`、`service/<担当>Service.java`、`service/dto/<内容>DTO.java` または `web/rest/vm/<内容>VM.java`（新規） | なし | `web/rest/<担当>ResourceIT.java`（新規） |
| エンティティを追加 | `domain/<エンティティ>.java`、`repository/<エンティティ>Repository.java`、`web/rest/<エンティティ>Resource.java`（新規）、`config/CacheConfiguration.java`（`@Cache` を付ける場合に変更） | `config/liquibase/changelog/*.xml`（新規）、`config/liquibase/master.xml`（変更）、必要なら `config/liquibase/fake-data/*.csv`（新規） | `domain/<エンティティ>Test.java`、`<エンティティ>TestSamples.java`、`<エンティティ>Asserts.java`、`web/rest/<エンティティ>ResourceIT.java`（新規） |
| エンティティに項目を追加 | `domain/<エンティティ>.java`、`web/rest/<エンティティ>Resource.java`（変更） | `config/liquibase/changelog/*.xml`（新規）、`config/liquibase/master.xml`（変更） | `domain/<エンティティ>Asserts.java`、`web/rest/<エンティティ>ResourceIT.java`（変更） |
