# 【JHipsterサンプルアプリ】SERVICE_DTO設計書_アカウント情報取得・更新

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

本書では `AdminUserDTO` を定義する。`AdminUserDTO` はアカウント情報の取得・更新、ユーザ管理、ユーザ登録（継承元）で共有する DTO である。

| No | クラス | 種別 | パッケージ | ファイルパス（リポジトリルート基準） | 用途 |
| --- | --- | --- | --- | --- | --- |
| 1 | `AdminUserDTO` | DTO | `io.github.jhipster.sample.service.dto` | `src/main/java/io/github/jhipster/sample/service/dto/AdminUserDTO.java` | `GET /api/account` の応答本文、`POST /api/account` のリクエスト本文 |

### AdminUserDTO

| No | プロパティ名 | 日本語名 | データ型 | 必須 | 桁数 | validation属性 | デフォルト値 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `id` | ユーザID | Long | － | － | なし | なし | 登録時は null |
| 2 | `login` | ログインID | String | ○ | 1～50 | `@NotBlank`、`@Pattern(regexp = Constants.LOGIN_REGEX)`、`@Size(min = 1, max = 50)` | なし | 使用できる文字は `Constants.LOGIN_REGEX` で定義する |
| 3 | `firstName` | 名 | String | － | 50 以内 | `@Size(max = 50)` | なし |  |
| 4 | `lastName` | 姓 | String | － | 50 以内 | `@Size(max = 50)` | なし |  |
| 5 | `email` | メールアドレス | String | － | 5～254 | `@Email`、`@Size(min = 5, max = 254)` | なし | 値がある場合のみチェックする |
| 6 | `imageUrl` | 画像URL | String | － | 256 以内 | `@Size(max = 256)` | なし |  |
| 7 | `activated` | 有効化状態 | boolean | － | － | なし | `false` |  |
| 8 | `langKey` | 言語キー | String | － | 2～10 | `@Size(min = 2, max = 10)` | なし |  |
| 9 | `createdBy` | 登録者 | String | － | － | なし | なし | 応答にのみ使う |
| 10 | `createdDate` | 登録日時 | Instant | － | － | なし | なし | 応答にのみ使う。ISO-8601 形式の文字列で出力する |
| 11 | `lastModifiedBy` | 最終更新者 | String | － | － | なし | なし | 応答にのみ使う |
| 12 | `lastModifiedDate` | 最終更新日時 | Instant | － | － | なし | なし | 応答にのみ使う。ISO-8601 形式の文字列で出力する |
| 13 | `authorities` | 権限名の集合 | Set\<String\> | － | － | なし | なし | 例 `ROLE_ADMIN`、`ROLE_USER` |

「必須」が「－」の項目は、値が null の場合にチェック対象外である（`@Size`、`@Email` は null を許容する）。

### エンティティとのマッピング

`AdminUserDTO(User user)` コンストラクタでエンティティ `User` から変換する。

| No | DTO側プロパティ | 変換元（`User`） | 備考 |
| --- | --- | --- | --- |
| 1 | `id` | `User#getId()` |  |
| 2 | `login` | `User#getLogin()` |  |
| 3 | `firstName` | `User#getFirstName()` |  |
| 4 | `lastName` | `User#getLastName()` |  |
| 5 | `email` | `User#getEmail()` |  |
| 6 | `imageUrl` | `User#getImageUrl()` |  |
| 7 | `activated` | `User#isActivated()` |  |
| 8 | `langKey` | `User#getLangKey()` |  |
| 9 | `createdBy` | `User#getCreatedBy()` |  |
| 10 | `createdDate` | `User#getCreatedDate()` |  |
| 11 | `lastModifiedBy` | `User#getLastModifiedBy()` |  |
| 12 | `lastModifiedDate` | `User#getLastModifiedDate()` |  |
| 13 | `authorities` | `User#getAuthorities()` の各 `Authority#getName()` |  |

DTO からエンティティへの変換は一括では行わない。`UserService` の各メソッドがプロパティを個別に設定する。

---

## API定義

| No | URL | HTTPメソッド | コントローラパッケージ | コントローラクラス名 | inDTO | outDTO | サービスクラス名 | 説明 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `/api/account` | GET | `io.github.jhipster.sample.web.rest` | AccountResource | なし | AdminUserDTO | UserService | アカウント情報の取得 |
| 2 | `/api/account` | POST | `io.github.jhipster.sample.web.rest` | AccountResource | AdminUserDTO | なし | UserService | アカウント基本情報の更新 |

処理の内容は「サービス詳細設計書_アカウント情報取得・更新」を参照。

---
