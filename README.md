# xxl-cache
分布式缓存管理平台

##### 项目拆分
- xxl-cache-admin   : 缓存管理中心,维护缓存Key,缓存模板(xxxx{0}xxxx{1}),缓存内容,包括: 新增、删除、清除内容;
- xxl-cache-service : 缓存的RPC服务 ,统一线上缓存API操作流程,如:Set、Get、Remove等操作; (热点模式:只有一个线程,异步更新缓存)
- xxl-cache-core    : 公共依赖,接入xxl-cache的项目依赖该JAR即可使用xxl-cache;
- xxl-cache-examples: 接入xxl-cache的Demo项目,可以参考该项目接入xxl-cache

概念:
- 缓存Key: xxl-cache中每个缓存的唯一标示,必须通过该Key才允许操作缓存,负责服务端拒绝服务;

缓存Key一般是一个模板,通过不同参数最终组合成不同的缓存Final Key,每个缓存,包括属性为:
1、缓存Key: 如 "product.num"
2、缓存模板: 如 "id{}city{}"
3、失效时间: 2H;
4、避免缓存雪崩: 存储一个永久的数据副本,当缓存失效时,只有第一个线程会获取空值,其余线程会取到副本数据; 只有第一个线程去异步更新缓存;
5、描述;

FinalKey存储格式: {分组}_{模板xxx{0}xxx{1}xxx}_${版本}
MCacheKey mkey = new MCacheKey("group{0}{1}", ...);



查询:类型、长度、值

##### 汇总
 * ehcache 进程缓存, 虽然目前ehcache提供集群方案，但是分布式缓存还是使用memcached/redis比较好;
 * memcached(Xmemcached、spymemcached、memcached-java-client) 分布式缓存, 分布是通常通过分片方式实现, 多核, 数据结构单一, key250k和value1M容量首限;
 * redis(jedis) 分布式缓存, 单核, 支持数据结构丰富, key和value512M容量巨大;
 * mongodb Nosql, 非关系型数据库(平级于mysql)，存储海量数据;
 * <p/>
 * ehcache本地对象缓存：EhcacheUtil (ehcacheObj)
 * ehcache页面缓存：SimplePageCachingFilter (ehcacheUrl)
 
