# 【JHipsterサンプルアプリ】キー・ロール一覧

## 改版履歴

| **版数** | **改版日付** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- |
| 1.0 | 2026/11/23 | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | キー・ロール一覧 |
| 2 | 判定方法 |
| 3 | キー・ロール対象外一覧 |

---

## キー・ロール一覧

本書では、KEY_NAME を REST のエンドポイント（HTTP メソッドとパス）とする。各エンドポイントを呼び出せる権限の対応を次の表に示す。権限はテーブル `jhi_authority` の権限名であり、JWT の `auth` クレームに格納される。

|  |  |  |  |  | 利用可能なロール |  |  |
| --- | --- | --- | --- | --- | --- | --- | --- |
| No | 機能分類 | 機能名 | HTTPメソッド | KEY_NAME（パス） | 未認証 | ROLE_USER | ROLE_ADMIN |
| 1 | 認証 | ログイン | POST | `/api/authenticate` | ○ | ○ | ○ |
| 2 | 認証 | 認証状態の確認 | GET | `/api/authenticate` | ○ | ○ | ○ |
| 3 | アカウント | ユーザ登録 | POST | `/api/register` | ○ | ○ | ○ |
| 4 | アカウント | アカウント有効化 | GET | `/api/activate` | ○ | ○ | ○ |
| 5 | アカウント | パスワードリセットの要求 | POST | `/api/account/reset-password/init` | ○ | ○ | ○ |
| 6 | アカウント | パスワードリセットの完了 | POST | `/api/account/reset-password/finish` | ○ | ○ | ○ |
| 7 | アカウント | アカウント情報の取得 | GET | `/api/account` | － | ○ | ○ |
| 8 | アカウント | アカウント情報の更新 | POST | `/api/account` | － | ○ | ○ |
| 9 | アカウント | パスワード変更 | POST | `/api/account/change-password` | － | ○ | ○ |
| 10 | ユーザ管理 | ユーザ登録 | POST | `/api/admin/users` | － | － | ○ |
| 11 | ユーザ管理 | ユーザ更新 | PUT | `/api/admin/users` | － | － | ○ |
| 12 | ユーザ管理 | ユーザ更新 | PUT | `/api/admin/users/{login}` | － | － | ○ |
| 13 | ユーザ管理 | ユーザ一覧検索 | GET | `/api/admin/users` | － | － | ○ |
| 14 | ユーザ管理 | ユーザ一件検索 | GET | `/api/admin/users/{login}` | － | － | ○ |
| 15 | ユーザ管理 | ユーザ削除 | DELETE | `/api/admin/users/{login}` | － | － | ○ |
| 16 | 公開ユーザ情報 | 一覧検索 | GET | `/api/users` | － | ○ | ○ |
| 17 | 権限管理 | 権限登録 | POST | `/api/authorities` | － | － | ○ |
| 18 | 権限管理 | 権限一覧検索 | GET | `/api/authorities` | － | － | ○ |
| 19 | 権限管理 | 権限一件検索 | GET | `/api/authorities/{id}` | － | － | ○ |
| 20 | 権限管理 | 権限削除 | DELETE | `/api/authorities/{id}` | － | － | ○ |
| 21 | 口座管理 | 口座登録 | POST | `/api/bank-accounts` | － | ○ | ○ |
| 22 | 口座管理 | 口座更新 | PUT | `/api/bank-accounts/{id}` | － | ○ | ○ |
| 23 | 口座管理 | 口座部分更新 | PATCH | `/api/bank-accounts/{id}` | － | ○ | ○ |
| 24 | 口座管理 | 口座一覧検索 | GET | `/api/bank-accounts` | － | ○ | ○ |
| 25 | 口座管理 | 口座一件検索 | GET | `/api/bank-accounts/{id}` | － | ○ | ○ |
| 26 | 口座管理 | 口座削除 | DELETE | `/api/bank-accounts/{id}` | － | ○ | ○ |
| 27 | 取引管理 | 取引登録 | POST | `/api/operations` | － | ○ | ○ |
| 28 | 取引管理 | 取引更新 | PUT | `/api/operations/{id}` | － | ○ | ○ |
| 29 | 取引管理 | 取引部分更新 | PATCH | `/api/operations/{id}` | － | ○ | ○ |
| 30 | 取引管理 | 取引一覧検索 | GET | `/api/operations` | － | ○ | ○ |
| 31 | 取引管理 | 取引一件検索 | GET | `/api/operations/{id}` | － | ○ | ○ |
| 32 | 取引管理 | 取引削除 | DELETE | `/api/operations/{id}` | － | ○ | ○ |
| 33 | ラベル管理 | ラベル登録 | POST | `/api/labels` | － | ○ | ○ |
| 34 | ラベル管理 | ラベル更新 | PUT | `/api/labels/{id}` | － | ○ | ○ |
| 35 | ラベル管理 | ラベル部分更新 | PATCH | `/api/labels/{id}` | － | ○ | ○ |
| 36 | ラベル管理 | ラベル一覧検索 | GET | `/api/labels` | － | ○ | ○ |
| 37 | ラベル管理 | ラベル一件検索 | GET | `/api/labels/{id}` | － | ○ | ○ |
| 38 | ラベル管理 | ラベル削除 | DELETE | `/api/labels/{id}` | － | ○ | ○ |

「未認証」が「○」のエンドポイントは、認証済みのユーザも呼び出せる。「未認証」が「－」で「ROLE_USER」が「○」のエンドポイントは、`ROLE_USER` と `ROLE_ADMIN` のどちらかを持つユーザが呼び出せる（URL の判定は `authenticated()` であり、権限の種類を問わない）。

---

## 判定方法

判定は 2 段階である。

| No | 段階 | 判定箇所 | 内容 |
| --- | --- | --- | --- |
| 1 | URL による判定 | `SecurityConfiguration#filterChain`（`src/main/java/io/github/jhipster/sample/config/SecurityConfiguration.java`） | リクエストのパスと HTTP メソッドでアクセス可否を判定する。リソースクラスに到達する前に実行される |
| 2 | メソッドセキュリティ | `UserResource`、`AuthorityResource` の `@PreAuthorize` | リソースクラスのメソッド単位で権限を判定する |

URL による判定の規則は、記述順に評価される。

| No | HTTPメソッド | パスパターン | 要求する条件 |
| --- | --- | --- | --- |
| 1 | POST、GET | `/api/authenticate` | 認証不要 |
| 2 | － | `/api/register` | 認証不要 |
| 3 | － | `/api/activate` | 認証不要 |
| 4 | － | `/api/account/reset-password/init` | 認証不要 |
| 5 | － | `/api/account/reset-password/finish` | 認証不要 |
| 6 | － | `/api/admin/**` | 権限 `ROLE_ADMIN` |
| 7 | － | `/api/**` | 認証済み |
| 8 | － | `/v3/api-docs/**` | 権限 `ROLE_ADMIN` |
| 9 | － | `/management/health` | 認証不要 |
| 10 | － | `/management/health/**` | 認証不要 |
| 11 | － | `/management/info` | 認証不要 |
| 12 | － | `/management/prometheus` | 認証不要 |
| 13 | － | `/management/**` | 権限 `ROLE_ADMIN` |

メソッドセキュリティの規則は次のとおりである。

| No | クラス | アノテーション | 備考 |
| --- | --- | --- | --- |
| 1 | `UserResource` | `@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")`（全メソッド） | URL の規則 6 と二重に制限する |
| 2 | `AuthorityResource` | `@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")`（全メソッド） | URL `/api/authorities` は規則 7（認証済み）に該当するため、管理者であることはこのアノテーションで判定する |

`dev` プロファイルでは `/h2-console/**` も認証不要となる。

---

## キー・ロール対象外一覧

業務機能以外のパスのアクセス制御は次のとおりである。

| No | パス | アクセス条件 | 内容 |
| --- | --- | --- | --- |
| 1 | `/v3/api-docs/**` | `ROLE_ADMIN` | API ドキュメント（Spring プロファイル `api-docs` が有効なときだけ提供する） |
| 2 | `/management/health`、`/management/health/**` | 認証不要 | ヘルスチェック（liveness と readiness のグループを含む） |
| 3 | `/management/info` | 認証不要 | アプリケーションの情報 |
| 4 | `/management/prometheus` | 認証不要 | Prometheus 形式のメトリクス |
| 5 | `/management/**`（上記以外） | `ROLE_ADMIN` | 環境変数、ログレベル、スレッドダンプなどの管理エンドポイント |
| 6 | `/h2-console/**` | 認証不要（`dev` のみ） | H2 のコンソール |
| 7 | 上記以外のすべてのパス | 認証済み | 画面や静的ファイルはない。未認証の場合は 401、認証済みの場合は 403 を返す（`ApiOnlyIT` が確認している） |

---
