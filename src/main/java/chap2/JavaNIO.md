# Java NIO

## Linux网络IO模型

UNIX提供了5种IO模型：

* 阻塞IO：缺省情况下，所有文件操作都是阻塞的。
* 非阻塞IO
* IO复用
* 信号驱动IO
* 异步IO

### IO多路复用

## 传统BIO通信

ServerSocket负责绑定IP地址，启动监听端口；Socket负责发起连接操作。成功后双方通过输入输出流进行同步阻塞式通信。BIO的服务端通常由一个Acceptor线程负责监听客户端，并为每个客户端创建一个线程进行处理。

