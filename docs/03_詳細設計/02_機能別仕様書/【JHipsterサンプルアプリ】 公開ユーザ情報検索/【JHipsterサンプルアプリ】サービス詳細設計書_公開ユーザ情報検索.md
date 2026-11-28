# 【JHipsterサンプルアプリ】サービス詳細設計書_公開ユーザ情報検索

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## 目次

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 |  | 検索 |

目次

| 項番 | シート名 |
| --- | --- |
| 1 | 処理概要 |
| 2 | 1　対象クラス |
| 3 | 2　API定義 |
| 4 | 3　単項目チェック |
| 5 | 4　依存クラス |
| 6 | 5　処理詳細 |
| 7 | 6　エラー一覧 |

---

## 処理概要

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 |  | 検索 |

### 処理概要

| ・ | 有効化済みの全ユーザについて、公開情報（ユーザIDとログインID）のみを検索する。 |
| --- | --- |

コール元に対する補足説明

フロントエンド（別リポジトリ）が HTTP で呼び出す。認証済みであれば権限を問わず呼び出せる。

---

## 1　対象クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 | 対象クラス | 検索 |

### 処理詳細

| No | クラス | ファイルパス（リポジトリルート基準） | 役割 |
| --- | --- | --- | --- |
| 1 | `PublicUserResource` | `src/main/java/io/github/jhipster/sample/web/rest/PublicUserResource.java` | エンドポイント |
| 2 | `UserDTO` | `src/main/java/io/github/jhipster/sample/service/dto/UserDTO.java` | 応答の DTO |
| 3 | `UserService` | `src/main/java/io/github/jhipster/sample/service/UserService.java` | ユーザの検索 |
| 4 | `UserRepository` | `src/main/java/io/github/jhipster/sample/repository/UserRepository.java` | ユーザテーブルへのアクセス |

---

## 2　API定義

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 | API定義 | 検索 |

### 処理詳細

| No | HTTPメソッド | URL | リソースクラス | メソッド | 認可 | リクエスト | 応答本文 | 正常時ステータス | トランザクション |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | GET | `/api/users` | PublicUserResource | `getAllPublicUsers` | 認証済み | ページング（`page`、`size`、`sort`） | `UserDTO` の配列 | 200 | サービス層がトランザクションを管理する（読み取り専用） |

サービス層が読み取り専用トランザクションで実行する。

`PublicUserResource` のクラスコメントには「誰でも呼び出せる」と記載されている。しかし `SecurityConfiguration` には `/api/users` のための `permitAll` がなく、URL は `/api/**` の `authenticated()` に従う。そのため本書では認証済みを要求するものとして記載する（`PublicUserResourceIT` も管理者権限を持つユーザで実行している）。

---

## 3　単項目チェック

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 | 単項目チェック | 検索 |

### 処理概要

ソートプロパティの許可チェックを行う。

### 処理詳細

リクエスト本文はない。`sort` で指定されたプロパティが次の許可リストに含まれない場合、400（応答本文なし）を返す。許可リストは `PublicUserResource.ALLOWED_ORDERED_PROPERTIES` である。

| No | 許可するソートプロパティ |
| --- | --- |
| 1 | `id` |
| 2 | `login` |
| 3 | `firstName` |
| 4 | `lastName` |
| 5 | `email` |
| 6 | `activated` |
| 7 | `langKey` |

---

## 4　依存クラス

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 | 依存クラス | 検索 |

### 処理概要

コンストラクタインジェクションで、本クラスが使う次の Bean を注入する。

### 処理詳細

| No | クラス | フィールド | 用途 |
| --- | --- | --- | --- |
| 1 | `UserService` | `userService` | ユーザの検索 |

---

## 5　処理詳細

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 | getAllPublicUsers | 検索 |

### 処理概要

有効化済みのユーザをページング付きで検索し、公開情報のみを返す。

### 処理詳細

対象メソッドは `PublicUserResource#getAllPublicUsers(@ParameterObject Pageable pageable)` である。

#### １）ソートプロパティをチェックする。

`onlyContainsAllowedProperties(pageable)` が false の場合、400（応答本文なし）とする。

#### ２）ユーザを検索する。

| クラス | `UserService` |  |
| --- | --- | --- |
| メソッド | `getAllPublicUsers` |  |
| 引数 | 引数名 | 値 |
|  | parameter1 | `pageable` |
| 戻り値 | `Page<UserDTO>` |  |

下記の条件で、`jhi_user` テーブルからデータを抽出する。

| 項番 | 取得テーブル名 | 取得メソッド | 取得条件 |
| --- | --- | --- | --- |
| 1 | `jhi_user` | `UserRepository#findAllByIdNotNullAndActivatedIsTrue` | `jhi_user.id` が null 以外、かつ `jhi_user.activated` が true |

抽出した各 `User` を `UserDTO` のコンストラクタで変換する。`UserDTO` に設定する項目は `id` と `login` のみである。

#### ３）応答を返す。

| 項目 | 内容 |
| --- | --- |
| ステータス | 200 |
| ヘッダ | `X-Total-Count: {総件数}`、`Link: {先頭、前、次、最終ページへのリンク}` |
| 応答本文 | `UserDTO` の配列（ページ内の分のみ） |

ヘッダは `PaginationUtil#generatePaginationHttpHeaders` が生成する。

---

## 6　エラー一覧

| サブシステム名(ID) | 機能ID | 機能日本語名 | 処理名 | 処理種別 |
| --- | --- | --- | --- | --- |
| JHipsterサンプルアプリ | PublicUserResource | 公開ユーザ情報検索 | エラー一覧 | 検索 |

### 処理詳細

| No | 発生条件 | ステータス | 応答 | 根拠 |
| --- | --- | --- | --- | --- |
| 1 | 許可されていないソートプロパティ | 400 | 本文なし | `PublicUserResourceIT#getAllUsersSortedByParameters` |
| 2 | 未認証、または JWT が無効 | 401 | 本文なし | コード（`SecurityConfiguration`） |

---
