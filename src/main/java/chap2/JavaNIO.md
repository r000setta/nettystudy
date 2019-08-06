# Java NIO

## Linux网络IO模型

UNIX提供了5种IO模型：

* 阻塞IO：缺省情况下，所有文件操作都是阻塞的。
* 非阻塞IO
* IO复用
* 信号驱动IO
* 异步IO

### IO多路复用

## 传统BIO通信(同步阻塞)

ServerSocket负责绑定IP地址，启动监听端口；Socket负责发起连接操作。成功后双方通过输入输出流进行同步阻塞式通信。BIO的服务端通常由一个Acceptor线程负责监听客户端，并为每个客户端创建一个线程进行处理。

## 伪异步IO

后端通过一个线程池处理多个客户端的请求连入，通过线程池调配线程资源。

新客户端接入时，将Socket封装为Task投递到线程池，线程池维护一个消息队列和N个活跃线程。

## NIO(非阻塞)

### Buffer——缓冲区

包含要写入或读出的数据，NIO中所有的数据都用缓冲区处理。本质上是一个字节数组。

### channel——通道

和流最大的区别在于通道是双向的，可以同时进行读写，可以更好地映射底层操作系统的API。

### selector——多路复用器

## AIO(异步通道)

不需要通过Selector对注册的通道进行轮询操作即可实现异步读写，简化了NIO。

## NIO服务端流程

1. 创建ServerSocketChannel，配置为非阻塞模式
2. 绑定监听，配置TCP餐数。
3. 创建独立IO线程，用于轮询Selector。
4. 创建Selector，将创建的ServerSocketChannel注册到Selector上，监听ACCEPT
5. 启动IO线程，循环执行select方法，轮询就绪的Channel
6. 轮询到就绪状态的channel时进行判断，如果是ACCEPT，说明新客户端接入，调用ServerSocketChannel.accept()接受新客户端。
7. 设置值新接入的客户端链路SocketChannel为非阻塞，配置TCP参数。
8. 将SocketChannel注册到Selector,监听READ
9. 如果轮询的Channel为READ，说明SocketChannel中有新就绪的数据包需要读取，构造ByteBuffer读取数据包。
10. 如果轮询的Channel为WRITE，说明还有数据没有发送完成，需要继续发送。

