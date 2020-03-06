# MultiProcessEventBus
MultiProcessEventBus是一个基于EventBus3.2.0的多进程通信框架，突破了EventBus单进程的限制，能够在进程间发送和接收event，提供了EventBus常用的一些接口，如register、unregister、post、postSticky等。

[EventBus](https://github.com/greenrobot/EventBus)是一个非常常用的模块间通信框架，它的底层是利用了观察者模式，巧妙地对通信进行解耦，并且利用注解的方式减少处理事件时的代码入侵，但是它不支持进程间收发事件。MultiProcessEventBus是对EventBus的扩展，保持了EventBus的优势，并为其提供跨进程的能力。

## 原理
MultiProcessEventBus利用Messenger实现了IPC，事件收发是基于EventBus。
### 子进程初始化
1. 绑定主进程service，获取与主进程进行通信的Messenger对象，并将本进程的Messenger对象发送给主进程，从而实现双向通信。
2. 主进程收到子进程的Messenger对象后，将主进程持有的所有Messenger对象发送到每一个进程，使得每一个进程都有了与其他进程通信的能力。
### 发布事件
1. 在本进程直接使用EventBus发布事件。
2. 通过IPC发送事件到所有其他进程后，其他进程使用EventBus发布事件。

## 使用方式
### Step1
通过gradle添加依赖
```gradle
implementation 'org.zhulinyin:multiprocess-eventbus:1.0.0'
```
或通过Maven
```xml
<dependency>
  <groupId>org.zhulinyin</groupId>
  <artifactId>multiprocess-eventbus</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
### Step2
在Application的onCreate()中进行初始化。
```java
MultiProcessEventBus.getDefault().init(this);
```
### Step3
创建事件类。
```java
public static class MessageEvent { /* Additional fields if needed */ }
```
### Step4
在需要监听事件的地方注册事件处理。
```java
@Subscribe(threadMode = ThreadMode.MAIN)  
public void onMessageEvent(MessageEvent event) {/* Do something */};
```
### Step5
注册和反注册观察者。
```java
@Override
public void onStart() {
    super.onStart();
    MultiProcessEventBus.getDefault().register(this);
}

@Override
public void onStop() {
    super.onStop();
    MultiProcessEventBus.getDefault().unregister(this);
}
```
### Step6
发布事件
```java
MultiProcessEventBus.getDefault().post(new MessageEvent());
```

## 优势
1. 支持跨进程通信。
2. 速度快。
- 无论是在主进程还是子进程发布事件，都只需要进行一次跨进程通信就能将事件发布到其他进程。
- 对于发布事件的进程，由于使用了单线程池进行IPC，因此不会阻塞发布进程。
- 使用oneway的方式进行IPC，发送方不需要阻塞等待接收方收到消息。
3. 不需要主进程做中转，就算主进程挂掉，子进程之间仍然能够进行通信。

## 不足
1. 不支持一次性发送大量数据。由于使用oneway的方式，如果发送方一次性发送大量数据，接收方处理不过来，有可能导致部分数据丢失。
2. 不支持EventBus在多进程间按照优先级发布事件，仅支持在单进程时按照优先级发布事件。
3. 仅支持单App跨进程通信。