# 《分布式缓存管理平台XXL-CACHE》

[TOCM]

[TOC]

## 一、简介

### 1.1 概述
XXL-CACHE是一个分布式缓存管理平台，其核心设计目标是“让分布式缓存的接入和管理的更加的简洁和高效”。现已开放源代码，开箱即用。

XXL-CACHE核心思想：将分布式缓存抽象成公共RPC服务，对外提供公共API进行缓存操作; 提供缓存公共的管理和监控平台：方便的查询、管理和监控线上缓存数据；

### 1.2 特性
- 1、多种缓存支持：支持Redis、Memcached两种缓存在线的查询和管理；
- 2、分布式缓存管理：支持分布式环境下，集群缓存服务的查询和管理，自动命中缓存服务节点；
- 3、方便：支持通过Web界管理缓存模板，查询和管理缓存数据；
- 4、透明：集群节点变动时，缓存命中的分片逻辑保持线上一致，自动命中缓存数据；
- 5、查看序列化缓存数据：通常缓存中保存的是序列化的Java数据，因此当需要查看缓存键值数据非常麻烦，本系统支持方便的查看缓存数据内容，反序列化数据；
- 6、查看缓存数据长度：直观显示缓存数据的长度；
- 7、查看缓存JSON格式内容：支持将缓存数据转换成JSON格式，直观查看缓存数据内容；

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
- Maven3+
- Jdk1.7+
- Tomcat7+
- Mysql5.5+

## 二、快速入门

### 2.1 初始化“数据库”
请下载项目源码并解压，获取 "调度数据库初始化SQL脚本"(脚本文件为: 源码解压根目录/xxl-cache/doc/db/xxl-cache-mysql.sql) 并执行即可。

### 2.2 编译源码
解压源码,按照maven格式将源码导入IDE, 使用maven进行编译即可，源码结构如下图所示：

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_WuIp.png "在这里输入图片标题")

- xxl-cache-admin：缓存管理平台
- xxl-cache-core：公共依赖，为缓存服务抽象成公共RPC服务做准备

### 2.3 配置部署“缓存管理平台”
    项目：xxl-cache-admin
    作用：查询和管理线上分布式缓存数据

- **A：配置“JDBC链接”**：请在下图所示位置配置jdbc链接地址，链接地址请保持和 2.1章节 所创建的调度数据库的地址一致。

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_eJb0.png "在这里输入图片标题")

- **B：配置“分布式缓存配置”**：请在下图所示位置配置分布树缓存信息，和线上项目中缓存配置务必保持一致。

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_EPzL.png "在这里输入图片标题")

配置详解：

    # 缓存类型, 取值范围: Memcached, Redis；（如配置Redis，则Redis地址生效，Memcached配置则被忽略，可删除）
    cache.type=Redis

    # redis集群地址配置, 多个地址用逗号分隔（当cache.type为Redis时生效）
    sharded.jedis.address=192.168.56.101:6379

    # memcached集群地址配置, 多个地址用逗号分隔（当cache.type为Memcached时生效）
    xmemcached.address=192.168.56.101:11211

    # for login （登录账号）
    login.username=admin
    login.password=123456

### 2.4 查询线上缓存

进入“缓存管理”界面，点击“新增缓存模板界面”，配置模板信息
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_3uNc.png "在这里输入图片标题")

然后，点击缓存模板右侧的“缓存操作”按钮 
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_aDwT.png "在这里输入图片标题")

Set缓存数据，代码如下
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_GwE5.png "在这里输入图片标题")

点击“查询缓存”，即可直观查看缓存信息
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-cache/images/img_VuTP.png "在这里输入图片标题")

## 二、缓存模板详解
### 3.1 XXl-CACHE系统中常用名词（缓存属性）解释

    缓存模板：生成缓存Key的模板，占位符用{0}、{1}、{2}依次替代；
    缓存描述：缓存的描述说明；
    缓存参数：“缓存模板”中占位符对应的参数，多个参数逗号分隔,依次替换占位符{0}、{1}、{2}的位置；
    FinalKey：保存在分布式缓存服务中最终的Key的值，根据“缓存模板”和“缓存参数”生成；
   
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

### 规划中
- 1、支持遍历线上缓存, 比如Redis模式, 通过 keys * 获取线上所有缓存Key的列表；
- 2、专注Redis：DB、KV在线申请和管理；

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
- 1、[迭代中]项目修改为springboot格式；
- 2、[迭代中]存储组件统一为Redis；
- 3、[迭代中]架构模型规范：Local + Remote + Broascast;
    - 缓存管理平台：界面管理配置数据，包括：环境、业务线、缓存Key、CRUD；同时，提供缓存RPC服务；
    - 核心依赖：内置缓存RPC客户端，底层通过RPC服务操作缓存。方便数据序列化与通讯；
    - 示例项目：客户端接入示例项目；
- 4、[迭代中]核心依赖推送maven仓库；原生提供docker镜像；
- 4、[迭代中]缓存优化：
    - 缓存穿透：
    - 缓存雪崩：
    - 缓存击穿：



## 七、其他

### 7.1 报告问题
XXL-CACHE托管在Github上，如有问题可在 [ISSUES](https://github.com/xuxueli/xxl-cache/issues) 上提问，也可以加入上文技术交流群；

### 7.2 接入登记
更多接入公司，欢迎在github [登记](https://github.com/xuxueli/xxl-cache/issues/1 )