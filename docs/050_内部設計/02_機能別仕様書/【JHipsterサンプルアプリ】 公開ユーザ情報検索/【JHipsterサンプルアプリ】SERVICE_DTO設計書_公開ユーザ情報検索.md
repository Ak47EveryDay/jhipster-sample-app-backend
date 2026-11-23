# 【JHipsterサンプルアプリ】SERVICE_DTO設計書_公開ユーザ情報検索

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

本書では `UserDTO` を定義する。

| No | クラス | 種別 | パッケージ | ファイルパス（リポジトリルート基準） | 用途 |
| --- | --- | --- | --- | --- | --- |
| 1 | `UserDTO` | DTO | `io.github.jhipster.sample.service.dto` | `src/main/java/io/github/jhipster/sample/service/dto/UserDTO.java` | `GET /api/users` の応答本文 |

### UserDTO

| No | プロパティ名 | 日本語名 | データ型 | 必須 | 桁数 | validation属性 | デフォルト値 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | ユーザID | Long | － | － | なし | なし |  |
| 2 | `login` | ログインID | String | － | － | なし | なし |  |

`UserDTO` はユーザの公開情報のみを持つ DTO である。メールアドレス、氏名、権限などは持たない（`PublicUserResourceIT#getAllPublicUsers` が、応答のキーが `id` と `login` のみであることを確認している）。

`equals` と `hashCode` は `id` と `login` で比較する。

### エンティティとのマッピング

`UserDTO(User user)` コンストラクタでエンティティ `User` から変換する。

| No | DTO側プロパティ | 変換元（`User`） | 備考 |
| --- | --- | --- | --- |
| 1 | `id` | `User#getId()` |  |
| 2 | `login` | `User#getLogin()` |  |

`UserMapper#userToUserDTO` も同じコンストラクタを呼び出すが、本機能では使っていない。

---

## API定義

| No | URL | HTTPメソッド | コントローラパッケージ | コントローラクラス名 | inDTO | outDTO | サービスクラス名 | 説明 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `/api/users` | GET | `io.github.jhipster.sample.web.rest` | PublicUserResource | ページング | UserDTO の配列 | UserService | 公開ユーザ情報の検索 |

処理の内容は「サービス詳細設計書_公開ユーザ情報検索」を参照。

---
