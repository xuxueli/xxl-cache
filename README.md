<p align="center">
    <img src="https://www.xuxueli.com/doc/static/xxl-job/images/xxl-logo.jpg" width="150">
    <h3 align="center">XXL-CACHE</h3>
    <p align="center">
        XXL-CACHE is a multi-level cache framework.
        <br>
        <a href="https://www.xuxueli.com/xxl-cache/"><strong>-- Home Page --</strong></a>
        <br>
        <br>
        <a href="https://github.com/xuxueli/xxl-cache/actions">
            <img src="https://github.com/xuxueli/xxl-cache/workflows/Java%20CI/badge.svg" >
        </a>
        <a href="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-cache-core/">
            <img src="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-cache-core/badge.svg" >
        </a>
        <a href="https://github.com/xuxueli/xxl-cache/releases">
            <img src="https://img.shields.io/github/release/xuxueli/xxl-cache.svg" >
        </a>
        <a href="https://github.com/xuxueli/xxl-cache/">
            <img src="https://img.shields.io/github/stars/xuxueli/xxl-cache" >
        </a>
        <img src="https://img.shields.io/github/license/xuxueli/xxl-cache.svg" >
        <a href="https://www.xuxueli.com/page/donate.html">
            <img src="https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square" >
        </a>
    </p>    
</p>

## Introduction
XXL-CACHE is a multilevel cache framework that efficiently combines local cache and distributed cache (Redis+Caffeine), supporting "multilevel cache, consistency assurance, TTL, Category isolation, penetration prevention" and other capabilities. With "high performance, high scalability, flexible and easy to use" and other features, to provide high-performance multi-level caching solutions;

XXL-CACHE 是一个 多级缓存框架，高效组合本地缓存和分布式缓存(Redis+Caffeine)，支持“多级缓存、一致性保障、TTL、Category隔离、防穿透”等能力；拥有“高性能、高扩展、灵活易用”等特性，提供高性能多级缓存解决方案；

## Documentation
- [中文文档](https://www.xuxueli.com/xxl-cache/)

## Communication
- [社区交流](https://www.xuxueli.com/page/community.html)


## Features
- 1、灵活易用: 接入灵活方便，一分钟上手；
- 2、多级缓存：高效组合本地缓存和分布式缓存(Redis+Caffeine)，支持L1、L2级别缓存，支持多场景缓存诉求；
- 3、高扩展：框架进行模块化抽象设计，本地缓存、分布式缓存以及序列化方案均支持自定义扩展；
- 4、高性能：底层设计L1(Local)+L2(Remote)多级缓存模型，除分布式缓存之外前置在应用层设置本地缓存，高热查询前置本地处理避免远程通讯，最大化提升性能；
- 5、一致性保障：支持多层级、集群多节点之间缓存数据一致性保障，借助广播消息（Redis Pub/Sub）以及客户端主动过期，实现L1及L2之间以及L1各集群节点间缓存数据一致性同步；
- 6、TTL：支持TTL，支持缓存数据主动过期及清理；
- 7、Category隔离：支持自定义缓存Category分类，缓存数据存储隔离；
- 8、缓存风险治理：针对典型缓存风险，如缓存穿透，底层进行针对性设计进行风险防护；
- 9、透明接入：支持业务透明接入，屏蔽底层实现细节，降低业务开发成本，以及学习认知成本；


## Contributing
Contributions are welcome! Open a pull request to fix a bug, or open an [Issue](https://github.com/xuxueli/xxl-api/issues/) to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-cache/issues/) 讨论新特性或者变更。

## 接入登记
Contributions are welcome! Open a pull request to fix a bug, or open an [Issue](https://github.com/xuxueli/xxl-cache/issues/) to discuss a new feature or change.

更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-cache/issues/1 ) 登记，登记仅仅为了产品推广。

## Copyright and License
This product is open source and free, and will continue to provide free community technical support. Individual or enterprise users are free to access and use.

- Licensed under the Apache License, Version 2.0.
- Copyright (c) 2015-present, xuxueli.

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

## Donate
No matter how much the donation amount is enough to express your thought, thank you very much ：）     [To donate](https://www.xuxueli.com/page/donate.html )

无论捐赠金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )
