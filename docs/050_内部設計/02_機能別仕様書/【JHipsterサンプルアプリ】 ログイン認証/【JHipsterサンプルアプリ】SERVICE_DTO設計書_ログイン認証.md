# 【JHipsterサンプルアプリ】SERVICE_DTO設計書_ログイン認証

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

本機能で使うデータ定義を次の表に示す。

| No | クラス | 種別 | パッケージ | ファイルパス（リポジトリルート基準） | 用途 |
| --- | --- | --- | --- | --- | --- |
| 1 | `LoginVM` | VM | `io.github.jhipster.sample.web.rest.vm` | `src/main/java/io/github/jhipster/sample/web/rest/vm/LoginVM.java` | リクエスト本文 |
| 2 | `JWTToken` | static ネストクラス | `io.github.jhipster.sample.web.rest` | `src/main/java/io/github/jhipster/sample/web/rest/AuthenticateController.java` | 応答本文 |

### LoginVM

| No | プロパティ名 | 日本語名 | データ型 | 必須 | 桁数 | validation属性 | デフォルト値 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `username` | ログインID | String | ○ | 1～254 | `@NotNull`、`@Size(min = 1, max = 254)` | なし | ユーザ名またはメールアドレス |
| 2 | `password` | パスワード | String | ○ | 4～100 | `@NotNull`、`@Size(min = 4, max = 100)` | なし | 平文 |
| 3 | `rememberMe` | ログイン状態の保持 | boolean | － | － | なし | `false` | true の場合は JWT の有効期限が長くなる |

`LoginVM#toString` は `password` を出力しない。

### JWTToken

`AuthenticateController` の static ネストクラスである。

| No | プロパティ名 | JSONキー | データ型 | 備考 |
| --- | --- | --- | --- | --- |
| 1 | `idToken` | `id_token` | String | 生成された JWT |

---

## API定義

| No | URL | HTTPメソッド | コントローラパッケージ | コントローラクラス名 | inDTO | outDTO | サービスクラス名 | 説明 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `/api/authenticate` | POST | `io.github.jhipster.sample.web.rest` | AuthenticateController | LoginVM | JWTToken | なし | ログイン認証と JWT の発行 |
| 2 | `/api/authenticate` | GET | `io.github.jhipster.sample.web.rest` | AuthenticateController | なし | なし | なし | 認証状態の確認 |

本機能にはサービスクラスがない。認証は `AuthenticationManagerBuilder` から取得した認証マネージャを経由して `DomainUserDetailsService` が行う。処理の内容は「サービス詳細設計書_ログイン認証」を参照。

---
