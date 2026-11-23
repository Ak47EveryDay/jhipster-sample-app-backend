# 【JHipsterサンプルアプリ】SERVICE_DTO設計書_ユーザ管理

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

本機能で使うデータ定義を次の表に示す。

| No | クラス | 種別 | パッケージ | ファイルパス（リポジトリルート基準） | 用途 |
| --- | --- | --- | --- | --- | --- |
| 1 | `AdminUserDTO` | DTO | `io.github.jhipster.sample.service.dto` | `src/main/java/io/github/jhipster/sample/service/dto/AdminUserDTO.java` | `POST`、`PUT` のリクエスト本文と `PUT`、`GET` の応答本文 |
| 2 | `User` | エンティティ | `io.github.jhipster.sample.domain` | `src/main/java/io/github/jhipster/sample/domain/User.java` | `POST` の応答本文 |

### AdminUserDTO

`AdminUserDTO` の項目定義は「SERVICE_DTO設計書_アカウント情報取得・更新」を参照。本機能でのエンドポイント別の使われ方は次のとおりである。

| No | プロパティ名 | POST（登録） | PUT（更新） | GET（検索） | 備考 |
| --- | --- | --- | --- | --- | --- |
| 1 | `id` | 指定すると 400 | 更新対象の特定に使う | 応答に設定 |  |
| 2 | `login` | 設定 | 設定（変更可能） | 応答に設定 | 小文字化して保存する |
| 3 | `firstName` | 設定 | 設定 | 応答に設定 |  |
| 4 | `lastName` | 設定 | 設定 | 応答に設定 |  |
| 5 | `email` | 設定 | 設定 | 応答に設定 | 小文字化して保存する |
| 6 | `imageUrl` | 設定 | 設定 | 応答に設定 |  |
| 7 | `activated` | 未使用（常に `true` で作成） | 設定 | 応答に設定 |  |
| 8 | `langKey` | 設定（null の場合は `en`） | 設定 | 応答に設定 |  |
| 9 | `createdBy`、`createdDate` | 未使用 | 未使用 | 応答に設定 | 監査項目 |
| 10 | `lastModifiedBy`、`lastModifiedDate` | 未使用 | 未使用 | 応答に設定 | 監査項目 |
| 11 | `authorities` | 設定（存在する権限名のみ） | 設定（既存を置換） | 応答に設定 | 権限名の集合 |

### User（POST の応答本文）

`POST /api/admin/users` の応答本文はエンティティ `User` の JSON である。項目定義は「05_TblDto仕様書 _jhi_user_TblDto」を参照。

| No | 項目 | 備考 |
| --- | --- | --- |
| 1 | `id`、`login`、`firstName`、`lastName`、`email`、`imageUrl`、`activated`、`langKey` | 作成した値 |
| 2 | `resetDate` | 作成時に設定したリセット要求日時 |
| 3 | `createdBy`、`createdDate`、`lastModifiedBy`、`lastModifiedDate` | 監査項目 |
| 4 | `password`、`activationKey`、`resetKey`、`authorities` | `@JsonIgnore` が付いているため、JSON に含まれない |

---

## API定義

| No | URL | HTTPメソッド | コントローラパッケージ | コントローラクラス名 | inDTO | outDTO | サービスクラス名 | 説明 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `/api/admin/users` | POST | `io.github.jhipster.sample.web.rest` | UserResource | AdminUserDTO | User | UserService | ユーザの登録 |
| 2 | `/api/admin/users`、`/api/admin/users/{login}` | PUT | `io.github.jhipster.sample.web.rest` | UserResource | AdminUserDTO | AdminUserDTO | UserService | ユーザの更新 |
| 3 | `/api/admin/users` | GET | `io.github.jhipster.sample.web.rest` | UserResource | ページング | AdminUserDTO の配列 | UserService | ユーザの一覧検索 |
| 4 | `/api/admin/users/{login}` | GET | `io.github.jhipster.sample.web.rest` | UserResource | パス変数 `login` | AdminUserDTO | UserService | ユーザの一件検索 |
| 5 | `/api/admin/users/{login}` | DELETE | `io.github.jhipster.sample.web.rest` | UserResource | パス変数 `login` | なし | UserService | ユーザの削除 |

処理の内容は「サービス詳細設計書_ユーザ管理」を参照。

---
