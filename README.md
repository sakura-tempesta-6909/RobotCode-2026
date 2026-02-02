## 解答編
### Q1. ExampleUsecaseの問題
#### 問題
```java
    public static Command moveToTarget(double target) {
        return Commands.startRun(() -> {
            templateRepository.resetPID();
        }, () -> {
            templateRepository.moveToTarget(target);
        }, templateRepository);
    }

   public static Command moveToTargetBasedOnApriltag() {
        return Commands.startRun(() -> {
            templateRepository.resetPID();
        }, () -> {
            double target = ExampleTools.apriltagPositionToExamplePosition(Example2State.apriltagPosition);
            templateRepository.moveToTarget(target);
        }, templateRepository);

        /**
         * Q1.
         * return moveToTarget(ExampleTools.apriltagPositionToExamplePosition(Example2State.apriltagPosition));
         * 実はダメなパターン。なぜでしょう？
         * ちなみに、他のものを使いまわして書くやり方もあります。
         */
    }
```
#### 回答
これだと、コマンドを生成されたときのapriltagの値に固定されてしまい、自動でapriltagに追尾してくれるという機能が付けられないから

##### 使いまわしをするための書き方
doubleSupplierを使うことにより、使い回しが可能になるが、初心者には難易度高め

```java
public static Command moveToTarget2Command(DoubleSupplier targetSupplier) {
    return Commands.startRun(() -> {
        templateRepository.resetPID();
    }, () -> {
        templateRepository.moveToTarget(targetSupplier.getAsDouble());
    }, templateRepository);
}

public static Command moveToTargetBasedOnApriltag2() {
    return moveToTarget2Command(() -> ExampleTools.apriltagPositionToExamplePosition(Example2State.apriltagPosition));
}
```

呼び出し部分を少しわかりやすくと、
```java
public static Command moveToTargetBasedOnApriltag2() {
    Command command = moveToTarget2Command(() -> {
        double apriltagPosition = Example2State.apriltagPosition;
        double examplePosition = ExampleTools.apriltagPositionToExamplePosition(apriltagPosition);
        return examplePosition
    });
    return command;
}
```