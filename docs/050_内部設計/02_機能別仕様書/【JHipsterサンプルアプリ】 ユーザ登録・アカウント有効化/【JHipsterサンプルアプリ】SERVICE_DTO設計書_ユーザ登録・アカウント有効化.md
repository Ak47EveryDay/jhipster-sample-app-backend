# 【JHipsterサンプルアプリ】SERVICE_DTO設計書_ユーザ登録・アカウント有効化

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

本書では `ManagedUserVM` を定義する。`GET /api/activate` はリクエスト本文を持たないため、DTO はない。

| No | クラス | 種別 | パッケージ | ファイルパス（リポジトリルート基準） | 用途 |
| --- | --- | --- | --- | --- | --- |
| 1 | `ManagedUserVM` | VM | `io.github.jhipster.sample.web.rest.vm` | `src/main/java/io/github/jhipster/sample/web/rest/vm/ManagedUserVM.java` | `POST /api/register` のリクエスト本文 |

### ManagedUserVM

`ManagedUserVM` は `AdminUserDTO` を継承し、`password` を追加したクラスである。継承する項目の定義は「SERVICE_DTO設計書_アカウント情報取得・更新」を参照。

| No | プロパティ名 | 日本語名 | データ型 | 必須 | 桁数 | validation属性 | デフォルト値 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `password` | パスワード | String | ○ | 4～100 | `@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)` | なし | 平文 |

クラスが持つ定数は次のとおりである。

| 定数名 | 型 | 値 | 説明 |
| --- | --- | --- | --- |
| `PASSWORD_MIN_LENGTH` | int | 4 | パスワードの最小桁数 |
| `PASSWORD_MAX_LENGTH` | int | 100 | パスワードの最大桁数 |

### エンティティとのマッピング

`UserService#registerUser` が `ManagedUserVM` から `User` を作成する。使う項目は `login`、`firstName`、`lastName`、`email`、`imageUrl`、`langKey`、`password` である。`id`、`activated`、`authorities`、監査項目は使わない。

---

## API定義

| No | URL | HTTPメソッド | コントローラパッケージ | コントローラクラス名 | inDTO | outDTO | サービスクラス名 | 説明 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `/api/register` | POST | `io.github.jhipster.sample.web.rest` | AccountResource | ManagedUserVM | なし | UserService | ユーザの登録 |
| 2 | `/api/activate` | GET | `io.github.jhipster.sample.web.rest` | AccountResource | クエリパラメータ `key` | なし | UserService | アカウントの有効化 |

処理の内容は「サービス詳細設計書_ユーザ登録・アカウント有効化」を参照。

---
