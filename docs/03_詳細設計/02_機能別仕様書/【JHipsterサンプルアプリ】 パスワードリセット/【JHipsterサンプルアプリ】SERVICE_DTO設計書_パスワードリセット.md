# 【JHipsterサンプルアプリ】SERVICE_DTO設計書_パスワードリセット

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## データ定義

本書では `KeyAndPasswordVM` を定義する。リセット要求（init）のリクエスト本文は DTO ではなくメールアドレスの文字列そのものである。

| No | クラス | 種別 | パッケージ | ファイルパス（リポジトリルート基準） | 用途 |
| --- | --- | --- | --- | --- | --- |
| 1 | `KeyAndPasswordVM` | VM | `io.github.jhipster.sample.web.rest.vm` | `src/main/java/io/github/jhipster/sample/web/rest/vm/KeyAndPasswordVM.java` | `POST /api/account/reset-password/finish` のリクエスト本文 |

### KeyAndPasswordVM

| No | プロパティ名 | 日本語名 | データ型 | 必須 | 桁数 | validation属性 | デフォルト値 | 備考 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `key` | リセットキー | String | ○ | － | なし | なし | リセットメールで通知したキー |
| 2 | `newPassword` | 新しいパスワード | String | ○ | 4～100 | なし | なし | 平文。桁数はリソースクラスでチェックする |

### エンティティとのマッピング

`KeyAndPasswordVM` をエンティティに変換することはない。`UserService#completePasswordReset` が `newPassword` をハッシュ化し、`User` の `password` に設定する。

---

## API定義

| No | URL | HTTPメソッド | コントローラパッケージ | コントローラクラス名 | inDTO | outDTO | サービスクラス名 | 説明 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `/api/account/reset-password/init` | POST | `io.github.jhipster.sample.web.rest` | AccountResource | メールアドレスの文字列 | なし | UserService | リセットキーの発行とメール通知 |
| 2 | `/api/account/reset-password/finish` | POST | `io.github.jhipster.sample.web.rest` | AccountResource | KeyAndPasswordVM | なし | UserService | パスワードの再設定 |

処理の内容は「サービス詳細設計書_パスワードリセット」を参照。

---
