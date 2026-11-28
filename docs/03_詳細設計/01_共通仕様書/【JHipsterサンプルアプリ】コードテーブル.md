# 【JHipsterサンプルアプリ】コードテーブル

## 改版履歴

| **版数** | **改版日付** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- |
| 1.0 | 2026/11/23 | 初版作成 |  |  |

---

## 目次

| 項番 | シート名 |
| --- | --- |
| 1 | 項目テーブル |
| 2 | 項目値テーブル |

本リポジトリで、コード値をテーブルとして持つのは権限（`jhi_authority`）のみである。その他の区分値（ユーザの有効化状態 `activated` など）はテーブル化していない。

---

## 項目テーブル

| No | 項目名 | テーブル名 | テーブル論理名 | コードのカラム | 主キー | 管理クラス | 登録・変更の方法 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | 権限 | `jhi_authority` | 権限テーブル | `name` | `name` | `AuthorityResource`（`/api/authorities`）、`AuthorityRepository` | 権限管理のエンドポイント（管理者のみ）、または Liquibase の初期データ |

---

## 項目値テーブル

| No | 項目名 | コード値 | 日本語名 | 定数（`AuthoritiesConstants`） | 付与されるタイミング | 主な用途 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | 権限 | `ROLE_ADMIN` | 管理者権限 | `ADMIN` | 初期データとしてユーザ `admin` に付与。管理者が `POST /api/admin/users`、`PUT /api/admin/users` で指定 | ユーザ管理（`/api/admin/**`）、権限管理（`/api/authorities`）、API ドキュメント、管理エンドポイント |
| 2 | 権限 | `ROLE_USER` | 一般ユーザ権限 | `USER` | 初期データとしてユーザ `admin`、`user` に付与。ユーザ登録（`POST /api/register`）で新規ユーザに自動的に付与 | 認証済みユーザとして API を呼び出す |

コード値は Liquibase の初期データ `src/main/resources/config/liquibase/data/authority.csv` として登録する。

権限とユーザの関連はテーブル `jhi_user_authority` で管理する。権限コードは JWT の `auth` クレームに半角空白区切りで格納され、API を呼び出すたびに参照される。

---
