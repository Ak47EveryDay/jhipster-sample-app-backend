# 【JHipsterサンプルアプリ】リポジトリ設計書_label

## 改版履歴

| **版数（Ver）** | **版数（Rev）** | **改版日付** | **改版シート** | **改版内容** | **担当者** | **確認者** |
| --- | --- | --- | --- | --- | --- | --- |
| 1.0 |  | 2026/11/23 |  | 初版作成 |  |  |

---

## リポジトリ情報

| 項目 | 内容 |
| --- | --- |
| システム名 | JHipsterサンプルアプリ |
| パッケージ | `io.github.jhipster.sample.repository` |
| テーブル論理名 | ラベル |
| テーブル名 | `label` |
| エンティティ | `Label`（主キー `Long`） |
| リポジトリクラス物理名 | `LabelRepository` |
| ファイルパス（リポジトリルート基準） | `src/main/java/io/github/jhipster/sample/repository/LabelRepository.java` |
| 親インターフェース | `JpaRepository<Label, Long>` |
| 呼び出し元 | `LabelResource`（サービスクラスを介さない） |

---

## (1)メソッド一覧

リポジトリには独自のメソッドはない。`JpaRepository` から継承したメソッドのうち、`LabelResource` で使うものは次のとおりである。

| No | メソッド名 | 用途 |
| --- | --- | --- |
| 1 | `save(Label)` | 登録、全項目更新、部分更新 |
| 2 | `existsById(Long)` | 更新前の存在チェック |
| 3 | `findById(Long)` | 一件検索、部分更新時の既存データの取得 |
| 4 | `findAll()` | 一覧検索 |
| 5 | `deleteById(Long)` | 削除 |

`Label` の `operations` は被所有側の多対多（`mappedBy = "labels"`）であり、ラベルを起点に取引を同時に読み込むメソッドはない。

---
