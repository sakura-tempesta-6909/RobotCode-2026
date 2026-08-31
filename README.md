# RobotCode-2026

FRC チーム 6909 Sakura Tempesta の 2026 シーズン（REBUILT）用ロボットコード。
スワーブドライブの機体を Java と WPILib のコマンドベースで制御する。

## 開発環境

WPILib 2026（VS Code 版）を入れれば、同梱の JDK 17 でそのままビルドできる。
チーム番号 6909 とプロジェクト年は `.wpilib/wpilib_preferences.json` に設定済み。
ベンダーライブラリは `vendordeps/` でバージョンを固定してあり、初回ビルド時に取得される。

| ライブラリ     | バージョン | 用途                        |
| -------------- | ---------- | --------------------------- |
| AdvantageKit   | 26.0.1     | ロギングとリプレイ          |
| PathplannerLib | 2026.1.2   | 自動運転のパス生成と追従    |
| REVLib         | 2026.0.4   | SPARK MAX（NEO）            |
| CTRE Phoenix 6 | 26.1.1     | CANcoder                    |
| PhotonLib      | 2026.3.1   | AprilTag による自己位置推定 |

パスの編集には PathPlanner GUI、ログの確認には AdvantageScope を使う。

## ビルドとデプロイ

Windows では `./gradlew` を `.\gradlew.bat` に読み替える。

| コマンド                 | 内容                   |
| ------------------------ | ---------------------- |
| `./gradlew build`        | ビルドとテスト         |
| `./gradlew deploy`       | roboRIO へのデプロイ   |
| `./gradlew simulateJava` | シミュレーションの起動 |
| `./gradlew test`         | テストのみ実行         |

デプロイでは `src/main/deploy` 以下も転送され、roboRIO に残っている古い auto ファイルは削除される。

## アーキテクチャ

ハードウェアを触るコードと、ロボットの動きを決めるコードを分けている。
上の層は下の層のインターフェースだけを知る。

```
コントローラー入力
  ↓
mode                          ボタンとコマンドの対応だけを持つ
  ↓
usecase/commands              Command を組み立てる
  ↓
domain/repository             サブシステムのインターフェース
  ↓
components/*/infrastructure   モーターとセンサーの実装
  ↓ periodic() が書き込む
domain/state                  ロボットの現在値（usecase と DomainUtil が読む）
```

書くときのルールは次のとおり。

- `domain/state` に書き込むのは `infrastructure` の `periodic()` だけで、他の層は読むだけにする。
- `usecase` は `BasicDrive` のような実装クラスを知らない。実装の選択は `RobotContainer` が行う。
- **Const**：CAN ID やギア比のように、機構で決まって動かせない値。
- **Parameter**：PID ゲインや速度のように、現場で調整する値。
- **Tools**：単位変換や計算だけを行う関数。
- 複数のサブシステムをまたぐ動作は `CommandsGroup`、単体の動作は `XxxCommands` に置く。
- 複数の `State` を組み合わせた判定は `StateGroup` に置く。

## ディレクトリ構成

```
src/main/java/frc/robot/
├── Robot.java             ロガーの初期化とモード切り替えの後処理
├── RobotContainer.java    実装の選択、Commands の初期化、モードの定義
├── auto/                  AutoChooser の構築と NamedCommands の登録
├── components/            サブシステムごとの Const、Parameter、Tools、infrastructure
├── domain/                repository、state、option
├── mode/                  ボタンの割り当て
├── usecase/               Command の生成とフィールド座標などの定数
└── util/                  デッドバンドやログ送信の共通処理

src/main/deploy/pathplanner/   PathPlanner の paths と autos
```

## ハードウェア構成

| サブシステム | 実装                           | ハードウェア                                              | CAN ID |
| ------------ | ------------------------------ | --------------------------------------------------------- | ------ |
| Drive        | `BasicDrive` / `BasicDriveSim` | SPARK MAX 8 個、CANcoder 4 個、ADXRS450 ジャイロ          | 下表   |
| Intake       | `Intake`                       | SPARK MAX 1 個                                            | 13     |
| Indexer      | `Indexer`                      | SPARK MAX 2 個（StarWheel と LongRoller）                 | 14、15 |
| Extender     | `Extender`                     | SPARK MAX 1 個とリミットスイッチ                          | 16     |
| Shooter      | `Shooter`                      | SPARK MAX 2 個（従動側は反転追従）                        | 18、17 |
| LED          | `LED`                          | AddressableLED                                            | PWM 0  |
| Vision       | `Vision`                       | PhotonVision カメラ 2 台（`leftCamera` と `rightCamera`） | なし   |

スワーブモジュールの割り当ては次のとおり。

| モジュール  | Drive | Turning | CANcoder |
| ----------- | ----- | ------- | -------- |
| Front Left  | 1     | 3       | 2        |
| Front Right | 4     | 6       | 5        |
| Back Left   | 10    | 12      | 11       |
| Back Right  | 7     | 9       | 8        |

機構値は `DriveConst` にある。
ホイール径 4 inch、Drive のギア比 6.75:1、Turning のギア比 150:7、トレッド幅 0.57205 m、ホイールベース 0.52205 m。

`DriveState.drivePosition` の座標系は PathPlanner と同じで、青アライアンスを手前に見て右手前の角が原点、奥が +X、左が +Y、反時計回りが角度の正方向になる。
Hub と Feed 位置はアライアンスに応じて `UsecaseUtil` が切り替える。

## 操作方法

コントローラーは 2 本で、ポート 0 が走行、ポート 1 が機構の操作。
走行側の Back で `DriveMode`、Start で `ManualMode` に切り替わる。
起動時は `DriveMode` になっている。

### DriveMode

走行（ポート 0）の操作。

| 操作               | 動作                                                 |
| ------------------ | ---------------------------------------------------- |
| 左スティック       | 平行移動（既定は Field Oriented）                    |
| 右スティック X     | 旋回                                                 |
| RB（押している間） | Robot Oriented に切り替え                            |
| B                  | Hub まで移動して向きを合わせる                       |
| X                  | Hub の方向へ機体を向ける（移動は左スティックで継続） |
| Y / A              | 0 度 / 180 度へ機体を向ける                          |
| POV 上             | ジャイロのリセット                                   |
| POV 下             | Vision による位置補正の切り替え                      |

機構（ポート 1）の操作。

| 操作 | 動作                                                        |
| ---- | ----------------------------------------------------------- |
| RT   | シュート（Shooter の回転数が乗ってから Indexer で送る）     |
| LT   | インテーク（Extender を Intake 角度へ下げてローラーを回す） |
| RB   | フィード                                                    |
| LB   | Extender を初期位置へ戻す                                   |
| A    | Extender を上下に振りながらインテーク                       |
| B    | 3000 RPM 固定でシュート（自己位置がずれたとき用）           |

### ManualMode

エンコーダーや PID が信用できないときに使う。
走行側の操作は `DriveMode` と同じで、機構側が出力の直接指定になる。

| 操作    | 動作                                                        |
| ------- | ----------------------------------------------------------- |
| RT / LT | シュート / インテーク                                       |
| LB / RB | Extender を一定出力で初期位置方向 / Intake 方向へ動かす     |
| B       | Shooter と Indexer と Intake をすべて逆回転して詰まりを解消 |
| A       | フィード                                                    |
| POV 上  | Extender のエンコーダーと PID をリセット                    |
| POV 下  | Shooter の PID をリセット                                   |

## 自動運転

SmartDashboard の Auto Chooser で選ぶ。
既定は `LeftTrench` で、赤アライアンスではパスが自動でミラーされる。
auto は `src/main/deploy/pathplanner/autos` にあり、`LeftTrench`、`RightTrench`、`LeftBump`、`RightBump`、`StartOutpostHub`、`StartDepotHub`、`StartDepotOutpostHub`、`StartOutpostDepotHub` を用意している。

PathPlanner から呼ぶコマンドは `AutoCommandConfigure` で登録する。
現在は `Intake`、`shoot`、`move to intake angle`、`intakePreload` が使える。
Drive の kS と kV を測る `feedforward characterization` も選択肢に入っており、実行するとコンソールに結果が出る。

## シミュレーション

`./gradlew simulateJava` で起動する。
`RobotBase.isSimulation()` の判定で `RobotContainer` が `BasicDriveSim` を選ぶため、実機がなくても動作を確認できる。
Vision は `VisionSim` が PhotonVision のカメラ 2 台を模擬し、FUEL は `FuelSim` が飛翔と Hub の得点まで再現する。
SmartDashboard には `Reset Fuel` と `Shoot` のボタンが出る。

## ログ

AdvantageKit の `LoggedRobot` を使い、実機では WPILOG と NetworkTables、シミュレーションでは NetworkTables に出力する。
`Util.allSendConsole()` が毎周期、各サブシステムで実行中の Command 名、推定位置、Hub までの距離、Extender の角度、Shooter の速度、各モーターの電圧と電流と出力を送る。
記録したログは AdvantageScope で開く。

## CI

`main` への push と Pull Request で GitHub Actions が動く。
`gradle.yml` がビルドし、`simulate.yml` が xvfb 上でシミュレーションを 30 秒動かして起動ログを確認する。

## ライセンス

WPILib BSD ライセンス（3 条項 BSD）に従う。
全文は [`WPILib-License.md`](WPILib-License.md) にある。

```
Copyright (c) 2009-2026 FIRST and other WPILib contributors
All rights reserved.
```

`FuelSim.java` は [hammerheads5000/FuelSim](https://github.com/hammerheads5000/FuelSim) 由来のコードを含む。
