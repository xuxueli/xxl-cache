# 《多级缓存框架XXL-CACHE》

[![Actions Status](https://github.com/xuxueli/xxl-cache/workflows/Java%20CI/badge.svg)](https://github.com/xuxueli/xxl-cache/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-cache-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-cache-core/)
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
## L2缓存（分布式）提供者，默认 redis
xxl.cache.l2.provider=redis
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
    xxlCacheFactory.setL2Provider(l2Provider);
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
String key = "user03";

// 缓存写入
XxlCacheHelper.getCache(category).set(key, value);

// 缓存查询
String value = XxlCacheHelper.getCache(category).get(key);

// 缓存删除
XxlCacheHelper.getCache(category).del(key);
... 

```
   
## 四、缓存管理
略

## 五、总体设计
### 5.1 源码目录介绍
    - /db :“数据库”建表脚本
    - /xxl-cache-admin :缓存管理平台，项目源码；
    - /xxl-cache-core : 公共依赖；（规划中）

### 5.2 核心思想

XXL-CACHE核心思想：

- 1、将分布式缓存抽象成公共RPC服务，对外提供公共API进行缓存操作：
    - 1、项目接入缓存服务更加方便：接入方只需要依赖一个RPC服务的API即可；
    - 2、统一监控和维护缓存服务;
    - 3、方便控制client连接数量;
    - 4、缓存节点变更更加方便;
    - 5、在节点变更时, 缓存分片很大可能会受影响, 这将导致不同服务的分片逻辑出现不一致的情况, 统一缓存服务可以避免之;
    - 6、可以屏蔽底层API操作,提供公共API,避免API误操作;

- 2、提供缓存管理和监控平台：方便的查询、管理和监控线上缓存数据；


## 六、历史版本

### 6.1 版本 v1.0.0 Release Notes[2016-07-25]
- 1、多种缓存支持：支持Redis、Memcached两种缓存在线的查询和管理；
- 2、分布式缓存管理：支持分布式环境下，集群缓存服务的查询和管理，自动命中缓存服务节点；
- 3、方便：支持通过Web界管理缓存模板，查询和管理缓存数据；
- 4、透明：集群节点变动时，缓存命中的分片逻辑保持线上一致，自动命中缓存数据；
- 5、查看序列化缓存数据：通常缓存中保存的是序列化的Java数据，因此当需要查看缓存键值数据非常麻烦，本系统支持方便的查看缓存数据内容，反序列化数据；
- 6、查看缓存数据长度：直观显示缓存数据的长度；
- 7、查看缓存JSON格式内容：支持将缓存数据转换成JSON格式，直观查看缓存数据内容；

### 6.2 版本 v1.1.0 Release Notes[迭代中]
- 1、【升级】项目重构升级：定位 多级缓存框架，有效整合分布式及本地缓存(Redis+Caffeine)，拥有“高性能、一致性保障、易用性”等特性，提供高性能多级缓存解决方案；
- 2、【性能】多级缓存模型设计：底层设计 L1（Local） + L2（Remote）两层缓存模型；除分布式缓存之外，前置在应用层设置本地缓存，最大化提升性能；
- 3、【一致性】多级缓存一致性保障：L1及L2之间、以及L1各集群节点间，借助广播消息（Redis Pub/Sub）以及客户端主动过期，实现多层级、集群多节点之间缓存数据一致性；
- 4、【易用性】多级缓存框架支持业务透明接入，屏蔽底层实现细节，降低业务开发成本，以及学习认知成本；
- 5、【升级】多个依赖升级最新版本，如jedis、spring等；
- 6、[迭代中]核心依赖推送maven仓库；原生提供docker镜像；
- 7、[迭代中]缓存优化：
    - 缓存穿透：
    - 缓存雪崩：
    - 缓存击穿：
- 其他：
  - 两级缓存：Local + Remote
  - TTL：主动过期；
  - 分布式自动刷新：

### 规划中
- 1、支持两级缓存；一致性保障；


## 七、其他

### 7.1 报告问题
XXL-CACHE托管在Github上，如有问题可在 [ISSUES](https://github.com/xuxueli/xxl-cache/issues) 上提问，也可以加入上文技术交流群；

### 7.2 接入登记
更多接入公司，欢迎在github [登记](https://github.com/xuxueli/xxl-cache/issues/1 )