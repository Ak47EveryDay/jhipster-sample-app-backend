<!-- ginga:ai-generated -->
<!-- 本文件由 ginga 平台的 AI 分析生成，重新分析时会自动更新；不想被自动更新请删除上面这行标记 -->
# プロジェクトナレッジ（ginga AI 分析生成）

jhipster-sample-app-backend は、JHipster 9.4.0 生成のサンプルアプリケーション（銀行口座・ラベル・取引を扱う）から切り出した Spring Boot 4.1.1／Java 21 の REST API サーバーである（README.md）。

- 画面は持たず API のみ。Angular フロントエンドは別リポジトリ jhipster-sample-app-frontend が担う（README.md）。
- レイヤーは web.rest → service → repository → domain の構成で、依存方向は ArchUnit テスト TechnicalStructureTest で検証されている（docs/999_標準化/レイヤー別開発ガイド.md）。
- 認証は JWT（HS512）＋ Spring Security のステートレス構成。dev はインメモリ H2、prod は PostgreSQL、スキーマは Liquibase で管理する（docs/999_標準化/システム知識整理.md）。
- キャッシュは Ehcache 3（JCache）でプロセス内に保持し、Hibernate 第 2 レベルキャッシュにも共用する（docs/03_詳細設計/01_共通仕様書/【JHipsterサンプルアプリ】キャッシュ設計書.md）。
- ビルドは Maven（./mvnw）。単体テストと統合テストは ./mvnw verify、CI は -Pprod verify で Testcontainers の PostgreSQL 上で統合テストを実行する（.github/workflows/github-actions.yml）。
- 日本語の詳細設計書を docs/03_詳細設計/ 配下に、規約・システム知識を docs/999_標準化/ 配下に整備している（docs/README.md、docs/999_標準化/README.md）。

## ナレッジ索引

- [ナレッジの全ファイル一覧（毎分析で更新）](.ginga/knowledge/INDEX.md)
- [システムナレッジ](.ginga/knowledge/system/)：レイヤー構成・エントリポイント・契約・共通部品
