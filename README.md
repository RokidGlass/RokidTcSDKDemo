## 测温控制SDK
**Version：tcsdk 1.0**

---

## 接口使用示例demo
https://github.com/RokidGlass/RokidTcSDKDemo

## 一. 测温控制SDK介绍

### 概述:
RokidTcSDK跨进程与红外测温APP通信，获取温度数据，并控制红外测温APP开启、退出、回到后台

### 使用前提：
在调用该SDK接口前，必须先安装红外测温APP

## 二. 集成说明
---
### 2.1 添加三方依赖库
在project的build.gradle中添加jcenter依赖
```java
allprojects {
    repositories {
        google()
        jcenter()
    }
}
```

在app的build.gradle中添加依赖
```java
dependencies {
    implementation 'com.rokid.glass:tclib:1.0'
}
```

## 三. 接口说明及示例
---
### 3.1 sdk创建、初始化
示例代码：
```java
TcSDK tcSDK = new TcSDK();
tcSDK.initSDK(context);
```
参数说明：
参数|含义
------|---------
context | 上下文context

### 3.2 打开红外测温APP
示例代码：
```java
tcSDK.startAPP(int mode)
```
参数说明：
参数|含义
------|---------
mode | 0为区域测温模式;1为多人测温模式

### 3.3 结束红外测温APP
示例代码：
```
tcSDK.finishAPP()
```

### 3.4 红外测温APP退到后台
示例代码：
```
tcSDK.moveTaskToBack();
```

### 3.5 获取当前红外测温数据
示例代码：
```
List<TemperatureInfo> temperatureInfos = tcSDK.getTemperatureInfo();
```
TemperatureInfo类属性说明：
类型|属性名|含义
------|------|---
float | temperature | 温度数值
String | unit | 温度单位 "°C"表示摄氏度 "°F"表示华氏度
int | precision | 测温精度 0表示设备未预热；1表示测温数据可能低于实际数值；2表示正常测温
String | mode |当前模式 "mode_region"表示区域测温模式，"mode_multi_face"表示多人测温模式
String | timeStamp | 当前系统时间 格式为"yyyy-MM-dd HH:mm:ss"

### 3.6 sdk释放
示例代码：
```
tcSDK.release();
```
