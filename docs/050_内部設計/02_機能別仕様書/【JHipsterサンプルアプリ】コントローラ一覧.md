# 【JHipsterサンプルアプリ】コントローラ一覧

## 改版履歴

| **版数** | **改版日付** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- |
| 1.0 | 2026/11/23 | 初版作成 |  |  |

---

## コントローラ一覧

本書では、「コントローラ」は `@RestController` を付与されたリソースクラスを指す。配置先は `src/main/java/io/github/jhipster/sample/web/rest/` であり、JSON の REST API のみを提供し、画面（HTML）は提供しない。

「認可」欄の読み方は次のとおりである。

- **不要**：未認証でも呼び出せる（`SecurityConfiguration` で `permitAll`）。
- **認証済み**：有効な JWT を持つユーザなら権限を問わず呼び出せる（`/api/**` の `authenticated()`）。
- **ROLE_ADMIN**：管理者権限が必要である。

| **No** | **機能名** | **機能ID** | **コントローラ** | **コントローラ名** | **リクエストマッピング** | **認可** | **対応文書** |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | ログイン認証 | AuthenticateController | AuthenticateController | ログイン認証Controller | `/api` | 不要 | [サービス詳細設計書_ログイン認証](./【JHipsterサンプルアプリ】%20ログイン認証/【JHipsterサンプルアプリ】サービス詳細設計書_ログイン認証.md) |
| 2 | アカウント情報取得・更新 | AccountResource | AccountResource | アカウント情報Controller | `/api` | 認証済み | [サービス詳細設計書_アカウント情報取得・更新](./【JHipsterサンプルアプリ】%20アカウント情報取得・更新/【JHipsterサンプルアプリ】サービス詳細設計書_アカウント情報取得・更新.md) |
| 3 | パスワード変更 | AccountResource | AccountResource | アカウント情報Controller | `/api` | 認証済み | [サービス詳細設計書_パスワード変更](./【JHipsterサンプルアプリ】%20パスワード変更/【JHipsterサンプルアプリ】サービス詳細設計書_パスワード変更.md) |
| 4 | ユーザ登録・アカウント有効化 | AccountResource | AccountResource | アカウント情報Controller | `/api` | 不要 | [サービス詳細設計書_ユーザ登録・アカウント有効化](./【JHipsterサンプルアプリ】%20ユーザ登録・アカウント有効化/【JHipsterサンプルアプリ】サービス詳細設計書_ユーザ登録・アカウント有効化.md) |
| 5 | パスワードリセット | AccountResource | AccountResource | アカウント情報Controller | `/api` | 不要 | [サービス詳細設計書_パスワードリセット](./【JHipsterサンプルアプリ】%20パスワードリセット/【JHipsterサンプルアプリ】サービス詳細設計書_パスワードリセット.md) |
| 6 | ユーザ管理 | UserResource | UserResource | ユーザ管理Controller | `/api/admin` | ROLE_ADMIN | [サービス詳細設計書_ユーザ管理](./【JHipsterサンプルアプリ】%20ユーザ管理/【JHipsterサンプルアプリ】サービス詳細設計書_ユーザ管理.md) |
| 7 | 権限管理 | AuthorityResource | AuthorityResource | 権限管理Controller | `/api/authorities` | ROLE_ADMIN | [サービス詳細設計書_権限管理](./【JHipsterサンプルアプリ】%20権限管理/【JHipsterサンプルアプリ】サービス詳細設計書_権限管理.md) |
| 8 | 公開ユーザ情報検索 | PublicUserResource | PublicUserResource | 公開ユーザ情報検索Controller | `/api` | 認証済み | [サービス詳細設計書_公開ユーザ情報検索](./【JHipsterサンプルアプリ】%20公開ユーザ情報検索/【JHipsterサンプルアプリ】サービス詳細設計書_公開ユーザ情報検索.md) |
| 9 | 口座管理 | BankAccountResource | BankAccountResource | 口座管理Controller | `/api/bank-accounts` | 認証済み | [サービス詳細設計書_口座管理](./【JHipsterサンプルアプリ】%20口座管理/【JHipsterサンプルアプリ】サービス詳細設計書_口座管理.md) |
| 10 | 取引管理 | OperationResource | OperationResource | 取引管理Controller | `/api/operations` | 認証済み | [サービス詳細設計書_取引管理](./【JHipsterサンプルアプリ】%20取引管理/【JHipsterサンプルアプリ】サービス詳細設計書_取引管理.md) |
| 11 | ラベル管理 | LabelResource | LabelResource | ラベル管理Controller | `/api/labels` | 認証済み | [サービス詳細設計書_ラベル管理](./【JHipsterサンプルアプリ】%20ラベル管理/【JHipsterサンプルアプリ】サービス詳細設計書_ラベル管理.md) |

`AccountResource` は 1 クラスで 7 エンドポイントを持ち、機能単位（NO 2～5）に分けて設計書を分割している。認可の判定方法（URL による判定とメソッドセキュリティ）の詳細は「01_共通仕様書 キー・ロール一覧」を参照。

エンドポイントを持たない機能（HTTP の入口がない処理）は次のとおりである。

| **NO** | **機能名** | **機能ID** | **クラス** | **起動方法** | **対応ドキュメント** |
| --- | --- | --- | --- | --- | --- |
| 12 | 未有効化ユーザ一括削除 | UserService | `UserService#removeNotActivatedUsers` | 定期実行（毎日 01:00） | [サービス詳細設計書_未有効化ユーザ一括削除](./【JHipsterサンプルアプリ】%20未有効化ユーザ一括削除/【JHipsterサンプルアプリ】サービス詳細設計書_未有効化ユーザ一括削除.md) |

---

## AuthenticateController

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | AuthenticateController |

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/authenticate` | `authorize` | 不要 | 200 | 400、401 |
| 2 | GET | `/api/authenticate` | `isAuthenticated` | 不要 | 204（未認証の場合は 401） | 401 |

### Service

| **パッケージ** | `io.github.jhipster.sample.security` |
| --- | --- |
| **クラス名** | DomainUserDetailsService |

直接は呼び出さない。`AuthenticateController` が `AuthenticationManagerBuilder` から取得した認証マネージャを経由して、`DomainUserDetailsService#loadUserByUsername` が実行される。

---

## AccountResource

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | AccountResource |

クラスに `@Validated` を付与し、リクエストパラメータに対する Bean Validation を有効にしている。

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/register` | `registerAccount` | 不要 | 201 | 400 |
| 2 | GET | `/api/activate` | `activateAccount` | 不要 | 200 | 400 |
| 3 | GET | `/api/account` | `getAccount` | 認証済み | 200 | 400 |
| 4 | POST | `/api/account` | `saveAccount` | 認証済み | 200 | 400 |
| 5 | POST | `/api/account/change-password` | `changePassword` | 認証済み | 200 | 400 |
| 6 | POST | `/api/account/reset-password/init` | `requestPasswordReset` | 不要 | 200 | 400 |
| 7 | POST | `/api/account/reset-password/finish` | `finishPasswordReset` | 不要 | 200 | 400 |

### Service

| **パッケージ** | `io.github.jhipster.sample.service` |
| --- | --- |
| **クラス名** | UserService、MailService |

### Repository

| **パッケージ** | `io.github.jhipster.sample.repository` |
| --- | --- |
| **クラス名** | UserRepository |

---

## UserResource

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | UserResource |

全メソッドに `@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")` を付与している（`AuthoritiesConstants.ADMIN` の値は `ROLE_ADMIN`）。

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/admin/users` | `createUser` | ROLE_ADMIN | 201 | 400 |
| 2 | PUT | `/api/admin/users`、`/api/admin/users/{login}` | `updateUser` | ROLE_ADMIN | 200 | 400、404 |
| 3 | GET | `/api/admin/users` | `getAllUsers` | ROLE_ADMIN | 200 | 400 |
| 4 | GET | `/api/admin/users/{login}` | `getUser` | ROLE_ADMIN | 200 | 404 |
| 5 | DELETE | `/api/admin/users/{login}` | `deleteUser` | ROLE_ADMIN | 204 | なし |

### Service

| **パッケージ** | `io.github.jhipster.sample.service` |
| --- | --- |
| **クラス名** | UserService、MailService |

### Repository

| **パッケージ** | `io.github.jhipster.sample.repository` |
| --- | --- |
| **クラス名** | UserRepository |

---

## PublicUserResource

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | PublicUserResource |

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | GET | `/api/users` | `getAllPublicUsers` | 認証済み | 200 | 400 |

### Service

| **パッケージ** | `io.github.jhipster.sample.service` |
| --- | --- |
| **クラス名** | UserService |

### Repository

| **パッケージ** | `io.github.jhipster.sample.repository` |
| --- | --- |
| **クラス名** | UserRepository |

---

## AuthorityResource

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | AuthorityResource |

クラスに `@Transactional(rollbackFor = Exception.class)` を付与し、全メソッドに `@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")` を付与している。

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/authorities` | `createAuthority` | ROLE_ADMIN | 201 | 400 |
| 2 | GET | `/api/authorities` | `getAllAuthorities` | ROLE_ADMIN | 200 | なし |
| 3 | GET | `/api/authorities/{id}` | `getAuthority` | ROLE_ADMIN | 200 | 404 |
| 4 | DELETE | `/api/authorities/{id}` | `deleteAuthority` | ROLE_ADMIN | 204 | なし |

### Service

なし（リソースクラスがリポジトリを直接使用する）。

### Repository

| **パッケージ** | `io.github.jhipster.sample.repository` |
| --- | --- |
| **クラス名** | AuthorityRepository |

---

## BankAccountResource

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | BankAccountResource |

クラスに `@Transactional(rollbackFor = Exception.class)` を付与している。

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/bank-accounts` | `createBankAccount` | 認証済み | 201 | 400 |
| 2 | PUT | `/api/bank-accounts/{id}` | `updateBankAccount` | 認証済み | 200 | 400、405 |
| 3 | PATCH | `/api/bank-accounts/{id}` | `partialUpdateBankAccount` | 認証済み | 200 | 400、404、405 |
| 4 | GET | `/api/bank-accounts` | `getAllBankAccounts` | 認証済み | 200 | なし |
| 5 | GET | `/api/bank-accounts/{id}` | `getBankAccount` | 認証済み | 200 | 404 |
| 6 | DELETE | `/api/bank-accounts/{id}` | `deleteBankAccount` | 認証済み | 204 | なし |

### Service

なし（リソースクラスがリポジトリを直接使用する）。

### Repository

| **パッケージ** | `io.github.jhipster.sample.repository` |
| --- | --- |
| **クラス名** | BankAccountRepository |

---

## OperationResource

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | OperationResource |

クラスに `@Transactional(rollbackFor = Exception.class)` を付与している。

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/operations` | `createOperation` | 認証済み | 201 | 400 |
| 2 | PUT | `/api/operations/{id}` | `updateOperation` | 認証済み | 200 | 400、405 |
| 3 | PATCH | `/api/operations/{id}` | `partialUpdateOperation` | 認証済み | 200 | 400、404、405 |
| 4 | GET | `/api/operations` | `getAllOperations` | 認証済み | 200 | なし |
| 5 | GET | `/api/operations/{id}` | `getOperation` | 認証済み | 200 | 404 |
| 6 | DELETE | `/api/operations/{id}` | `deleteOperation` | 認証済み | 204 | なし |

### Service

なし（リソースクラスがリポジトリを直接使用する）。

### Repository

| **パッケージ** | `io.github.jhipster.sample.repository` |
| --- | --- |
| **クラス名** | OperationRepository |

---

## LabelResource

### Controller

| **パッケージ** | `io.github.jhipster.sample.web.rest` |
| --- | --- |
| **クラス名** | LabelResource |

クラスに `@Transactional(rollbackFor = Exception.class)` を付与している。

| No | HTTPメソッド | パス | メソッド | 認可 | 正常時ステータス | 主なエラー |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/labels` | `createLabel` | 認証済み | 201 | 400 |
| 2 | PUT | `/api/labels/{id}` | `updateLabel` | 認証済み | 200 | 400、405 |
| 3 | PATCH | `/api/labels/{id}` | `partialUpdateLabel` | 認証済み | 200 | 400、404、405 |
| 4 | GET | `/api/labels` | `getAllLabels` | 認証済み | 200 | なし |
| 5 | GET | `/api/labels/{id}` | `getLabel` | 認証済み | 200 | 404 |
| 6 | DELETE | `/api/labels/{id}` | `deleteLabel` | 認証済み | 204 | なし |

### Service

なし（リソースクラスがリポジトリを直接使用する）。

### Repository

| **パッケージ** | `io.github.jhipster.sample.repository` |
| --- | --- |
| **クラス名** | LabelRepository |

---
