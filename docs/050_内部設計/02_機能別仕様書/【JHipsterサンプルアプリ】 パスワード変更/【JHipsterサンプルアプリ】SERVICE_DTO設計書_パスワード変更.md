# 【JHipsterサンプルアプリ】SERVICE_DTO設計書_パスワード変更

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

本書では `PasswordChangeDTO` を定義する。

| No | クラス | 種別 | パッケージ | ファイルパス（リポジトリルート基準） | 用途 |
| --- | --- | --- | --- | --- | --- |
| 1 | `PasswordChangeDTO` | DTO | `io.github.jhipster.sample.service.dto` | `src/main/java/io/github/jhipster/sample/service/dto/PasswordChangeDTO.java` | リクエスト本文 |

### PasswordChangeDTO

| No | プロパティ名 | 日本語名 | データ型 | 必須 | 桁数 | validation属性 | デフォルト値 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `currentPassword` | 現在のパスワード | String | － | － | なし | なし | 平文 |
| 2 | `newPassword` | 新しいパスワード | String | ○ | 4～100 | なし | なし | 平文。桁数はリソースクラスでチェックする |

必須と桁数はアノテーションでなくコードでチェックするため、「validation属性」は「なし」である。

### エンティティとのマッピング

`PasswordChangeDTO` をエンティティに変換することはない。`UserService#changePassword` が `newPassword` をハッシュ化し、`User` の `password` に設定する。

---

## API定義

| No | URL | HTTPメソッド | コントローラパッケージ | コントローラクラス名 | inDTO | outDTO | サービスクラス名 | 説明 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `/api/account/change-password` | POST | `io.github.jhipster.sample.web.rest` | AccountResource | PasswordChangeDTO | なし | UserService | パスワードの変更 |

処理の内容は「サービス詳細設計書_パスワード変更」を参照。

---
