<!-- ginga:ai-generated -->
<!-- 本文件由 ginga 平台的 AI 分析生成，重新分析时会自动更新；不想被自动更新请删除上面这行标记 -->
# プロジェクトナレッジ（ginga AI 分析生成）

**jhipster-sample-app-backend** は、JHipster 9.4.0 で生成したサンプルアプリケーション（銀行口座・ラベル・取引の管理）をバックエンドとして切り出した、REST API 専用の Spring Boot アプリケーションである。フロントエンド（Angular）は別リポジトリ `jhipster-sample-app-frontend` が担う。

- 技術スタック：Java 21、Spring Boot 4.1.1、Spring Data JPA（H2 / PostgreSQL）、Liquibase、Ehcache、Spring Security + JWT（HS512）、`jhipster-framework` 9.4.0（`pom.xml`、`README.md`）
- 起動クラスは `JhipsterSampleApplicationApp`、ベースパッケージは `io.github.jhipster.sample`（`src/main/java/io/github/jhipster/sample/JhipsterSampleApplicationApp.java`）
- 画面・静的ファイルの配信は行わず、API パス以外は拒否される（`README.md`、テストの `ApiOnlyIT`）
- dev プロファイルはインメモリ H2、prod は PostgreSQL。ビルドは Maven wrapper（`./mvnw`）、CI は `./mvnw -Pprod verify`（`.github/workflows/github-actions.yml`）
- 日本語の設計書・規約文書が `docs/050_内部設計/` と `docs/999_標準化/` に整備されており、実装・テスト・規約の一次情報となっている（`docs/README.md`）
- テストは単体（surefire）、統合（failsafe、`*IT*`）、負荷試験（Gatling）に分かれ、統合テストは Testcontainers の PostgreSQL で実行する（`docs/999_標準化/テスト作成ガイド.md`）

## ナレッジ索引

- [ナレッジの全ファイル一覧（毎分析で更新）](.ginga/knowledge/INDEX.md)
- [システムナレッジ](.ginga/knowledge/system/)：レイヤー構成・エントリポイント・契約・共通部品
- [開発ナレッジ](.ginga/knowledge/development/)：言語・フレームワーク・ビルドとエンジニアリング慣習
- [テストナレッジ](.ginga/knowledge/testing/)：テストフレームワーク・構成・手法
- [プロジェクト規約](.ginga/knowledge/standards/)：命名・コメント・コミットとブランチ規約
