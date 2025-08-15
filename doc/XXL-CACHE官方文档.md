# 《多级缓存框架XXL-CACHE》

[![Actions Status](https://github.com/xuxueli/xxl-cache/workflows/Java%20CI/badge.svg)](https://github.com/xuxueli/xxl-cache/actions)
[![Maven Central](https://img.shields.io/maven-central/v/com.xuxueli/xxl-cache-core)](https://central.sonatype.com/artifact/com.xuxueli/xxl-cache-core/)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-cache.svg)](https://github.com/xuxueli/xxl-cache/releases)
[![GitHub stars](https://img.shields.io/github/stars/xuxueli/xxl-cache)](https://github.com/xuxueli/xxl-cache/)
![License](https://img.shields.io/github/license/xuxueli/xxl-cache.svg)
[![donate](https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square)](https://www.xuxueli.com/page/donate.html)

[TOCM]

[TOC]

## 一、简介

### 1.1 概述
XXL-CACHE 是一个 多级缓存框架，高效组合本地缓存和分布式缓存(Redis+Caffeine)，支持“多级缓存、一致性保障、TTL、Category隔离、防穿透”等能力；拥有“高性能、高扩展、灵活易用”等特性，提供高性能多级缓存解决方案；


### 1.2 特性
- 1、灵活易用: 接入灵活方便，一分钟上手；
- 2、多级缓存：高效组合本地缓存和分布式缓存(Redis+Caffeine)，支持L1、L2级别缓存，支持多场景缓存诉求；
- 3、高扩展：框架进行模块化抽象设计，本地缓存、分布式缓存以及序列化方案均支持自定义扩展；
- 4、高性能：底层设计L1(Local)+L2(Remote)多级缓存模型，除分布式缓存之外前置在应用层设置本地缓存，高热查询前置本地处理避免远程通讯，最大化提升性能；
- 5、一致性保障：支持多层级、集群多节点之间缓存数据一致性保障，借助广播消息（Redis Pub/Sub）以及客户端主动过期，实现L1及L2之间以及L1各集群节点间缓存数据一致性同步；
- 6、TTL：支持TTL，支持缓存数据主动过期及清理；
- 7、Category隔离：支持自定义缓存Category分类，缓存数据存储隔离；
- 8、缓存风险治理：针对典型缓存风险，如缓存穿透，底层进行针对性设计进行风险防护；
- 9、透明接入：支持业务透明接入，屏蔽底层实现细节，降低业务开发成本，以及学习认知成本；
- 10、多序列化协议支持：组件化抽象Serializer，可灵活扩展更多序列化协议；如 JDK、HESSIAN2、JSON、PROTOSTUFF、KRYO 等；

### 1.3 下载
#### 文档地址

- [中文文档](https://www.xuxueli.com/xxl-cache/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-cache](https://github.com/xuxueli/xxl-cache) | [Download](https://github.com/xuxueli/xxl-cache/releases)  
[http://gitee.com/xuxueli0323/xxl-cache](http://gitee.com/xuxueli0323/xxl-cache) | [Download](http://gitee.com/xuxueli0323/xxl-cache/releases)

#### 技术交流
- [社区交流](https://www.xuxueli.com/page/community.html)

### 1.4 环境
- JDK：1.8+


## 二、快速入门

XXL-CACHE 支持与springboot无缝集成，同时也支持无框架方式使用（不依赖任意三方框架），多种接入方式可参考如下示例代码：
- a、SpringBoot集成方式：示例代码位置 “./xxl-cache-samples/xxl-cache-sample-springboot”。
- b、无框架方式：示例代码位置 “./xxl-cache-samples/xxl-cache-sample-frameless”。

下文以 “SpringBoot集成方式” 介绍如何配置接入。

### 2.1 Maven引入
```
<!-- https://mvnrepository.com/artifact/com.xuxueli/xxl-cache-core -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-cache-core</artifactId>
    <version>${最新稳定版}</version>
</dependency>
```

### 2.2 配置文件

参考代码位置：
```
/xxl-cache/xxl-cache-samples/xxl-cache-sample-springboot/src/main/resources/application.properties
```

配置项说明：
```
# xxl-cache
## L1缓存（本地）提供者，默认 caffeine
xxl.cache.l1.provider=caffeine
## L1缓存最大容量，默认10000；
xxl.cache.l1.maxSize=-1
## L1缓存过期时间，单位秒，默认10min；
xxl.cache.l1.expireAfterWrite=-1
## L2缓存（分布式）提供者，默认 redis
xxl.cache.l2.provider=redis
## L2缓存序列化方式，默认 java
xxl.cache.l2.serializer=java
## L2缓存节点配置，多个节点用逗号分隔；示例 “127.0.0.1:6379,127.0.0.1:6380”
xxl.cache.l2.nodes=127.0.0.1:6379
## L2缓存用户名配置
xxl.cache.l2.user=
## L2缓存密码配置
xxl.cache.l2.password=
```

### 2.3 组件初始化配置

参考代码位置：
```
/xxl-cache/xxl-cache-samples/xxl-cache-sample-springboot/src/main/java/com/xxl/cache/core/sample/config/XxlCacheConf.java
```

组件配置示例：
```
@Bean(initMethod = "start", destroyMethod = "stop")
public XxlCacheFactory xxlCacheFactory() {
    XxlCacheFactory xxlCacheFactory = new XxlCacheFactory();
    xxlCacheFactory.setL1Provider(l1Provider);
    xxlCacheFactory.setMaxSize(maxSize);
    xxlCacheFactory.setExpireAfterWrite(expireAfterWrite);
    xxlCacheFactory.setL2Provider(l2Provider);
    xxlCacheFactory.setSerializer(serializer);
    xxlCacheFactory.setNodes(nodes);
    xxlCacheFactory.setUser(user);
    xxlCacheFactory.setPassword(password);
    return xxlCacheFactory;
}
```

至此，全部配置完成，业务代码接入使用。

### 2.4 接入示例

参考代码位置：
```
/xxl-cache/xxl-cache-samples/xxl-cache-sample-springboot/src/main/java/com/xxl/cache/core/sample/controller/IndexController.java
```

示例代码：
```
String category = "user";
long survivalTime = 60*1000;
String key = "user03";

/**
* 1、定义缓存对象，并指定 “缓存category + 过期时间”
*/
XxlCacheHelper.XxlCache userCache = XxlCacheHelper.getCache(category, survivalTime);

/**
* 2、缓存写：按照 L1 -> L2 顺序依次写缓存，同时借助内部广播机制更新全局L1节点缓存；
*/
userCache.set(key, value);

/**
* 3、缓存读：按照 L1 -> L2 顺序依次读取缓存，如果L1存在缓存则返回，否则读取L2缓存并同步L1；
*/
userCache.get(key);

/**
* 4、缓存删：按照 L1 -> L2 顺序依次删缓存，同时借助内部广播机制更新全局L1节点缓存；
*/
userCache.del(key);
... 

```

部署启动 xxl-cache-sample-springboot 示例项目后，可访问如下地址体验缓存操作：
- 缓存写入地址：http://localhost:8080/set?value=999 
- 缓存查询地址：http://localhost:8080/ 
- 缓存查询地址：http://localhost:8080/delete 

   

## 三、总体设计

### 3.1 架构图

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_01.png "在这里输入图片标题")

### 3.2 核心思想
XXL-CACHE 定位多级缓存框架，高效组合本地缓存和分布式缓存(Redis+Caffeine)，支持“多级缓存、一致性保障、TTL、Category隔离、防穿透”等能力；拥有“高性能、高扩展、灵活易用”等特性，提供高性能多级缓存解决方案；

### 3.3 多级缓存设计
高效组合本地缓存和分布式缓存(Redis+Caffeine)，支持L1、L2级别缓存，支持多场景缓存诉求；
底层设计L1(Local)+L2(Remote)多级缓存模型，除分布式缓存之外前置在应用层设置本地缓存，高热查询前置本地处理避免远程通讯，最大化提升性能；

### 3.4 一致性保障
支持多层级、集群多节点之间缓存数据一致性保障，借助广播消息（Redis Pub/Sub）以及客户端主动过期，实现L1及L2之间以及L1各集群节点间缓存数据一致性同步；

### 3.5 TTL
支持TTL，支持缓存数据主动过期及清理；

### 3.6 Category隔离
支持自定义缓存Category分类，缓存数据存储隔离；

### 3.7 缓存风险治理
针对典型缓存风险，如缓存穿透，底层进行针对性设计进行风险防护；


## 四、历史版本

### v1.0.0 Release Notes[2016-07-25]
- 1、多种缓存支持：支持Redis、Memcached两种缓存在线的查询和管理；
- 2、分布式缓存管理：支持分布式环境下，集群缓存服务的查询和管理，自动命中缓存服务节点；
- 3、方便：支持通过Web界管理缓存模板，查询和管理缓存数据；
- 4、透明：集群节点变动时，缓存命中的分片逻辑保持线上一致，自动命中缓存数据；
- 5、查看序列化缓存数据：通常缓存中保存的是序列化的Java数据，因此当需要查看缓存键值数据非常麻烦，本系统支持方便的查看缓存数据内容，反序列化数据；
- 6、查看缓存数据长度：直观显示缓存数据的长度；
- 7、查看缓存JSON格式内容：支持将缓存数据转换成JSON格式，直观查看缓存数据内容；

### v1.1.0 Release Notes[2025-02-04]
- 1、【升级】项目重构升级；定位 多级缓存框架，高效组合本地缓存和分布式缓存(Redis+Caffeine)，支持“多级缓存、一致性保障、TTL、Category隔离、防穿透”等能力，提供高性能多级缓存解决方案。
- 2、【重构】高性能系统重构设计，底层设计L1(Local)+L2(Remote)多级缓存模型，除分布式缓存之外前置在应用层设置本地缓存，高热查询前置本地处理避免远程通讯，最大化提升性能；
- 3、【重构】一致性保障设计，支持多层级、集群多节点之间缓存数据一致性保障，借助广播消息（Redis Pub/Sub）以及客户端主动过期，实现L1及L2之间以及L1各集群节点间缓存数据一致性同步；
- 4、【重构】框架进行模块化抽象设计，本地缓存、分布式缓存以及序列化方案均支持自定义扩展；
- 5、【易用性】缓存API优化改造，多级缓存框架支持业务透明接入，屏蔽底层实现细节，降低业务开发成本，以及学习认知成本；
- 6、【升级】多个依赖升级最新版本，如jedis、spring等；
- 7、【优化】核心依赖推送maven中央仓库, 方便用户接入和使用;

### v1.2.0 Release Notes[2025-02-07]
- 1、【增强】多序列化协议支持：针对L2缓存，组件化抽象Serializer，可灵活扩展更多序列化协议；如 JDK、HESSIAN2、JSON、PROTOSTUFF、KRYO 等；
- 2、【优化】移除冗余依赖，精简Core体积；

### v1.3.1 Release Notes[2025-08-16]
- 1、【优化】L1缓存广播发布/订阅断连重连优化(ISSUE-32)；
- 2、【重构】合并PR-28，重构 getCache 方法并优化性能；
- 3、【优化】合并PR-35，引入单元测试框架，重写单元测试方法；
- 4、【升级】多个依赖升级最新版本，如jedis、caffeine等；

### v1.4.0 Release Notes[2025-08-16]
- 1、【升级】项目升级JDK17；
- 2、【升级】项目部分依赖升级，如caffeine，适配JDK17；


### TODO LIST
- 1、缓存监控：L1、L2缓存命中率，L1缓存容量、内容占用等；
- 2、多序列化方案；
- 3、redis接入方式丰富，单节点、sentinel、cluster等；
- 4、L1缓存，过期主动清理；


## 七、其他

### 7.1 项目贡献
欢迎参与项目贡献！比如提交PR修一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-cache/issues/) 讨论新特性或者变更。

### 7.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-cache/issues/1 ) 登记，登记仅仅为了产品推广。

### 7.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the Apache License, Version 2.0.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论捐赠金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )
