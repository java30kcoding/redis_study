# Redis

## 简介

​	Redis是一个开源的使用C语言编写、支持网络、可基于内存亦可基于持久化的日志型、Key-Value数据库，并提供多种语言的API。

​	本质时客户端-服务端应用程序软件。

​	特点是使用简单、性能强悍、功能应用场景丰富。

## 应用场景

- 缓存
- 分布式锁

## 通用命令

| 命令               | 说明                                  |
| ------------------ | ------------------------------------- |
| DEL key            | 该命令用于在key存在是删除指定key      |
| DUMP kdy           | 序列化给定的key，并返回被序列化的值   |
| EXISTS key         | 检查给定的key是否存在                 |
| EXPIRE key seconds | 为给定可以设置国企时间，以秒计算      |
| TTL key            | 以秒为单位，返回给定key的剩余生存时间 |
| TYPE key           | 返回key所存储的值的类型               |

**Redis官网的展示的所有命令**：https://redis.io/commands

# Redis的数据结构

​	Redis的所有数据结构都是以唯一的key字符串作为名称，然后通过唯一的key值来获取对应value的数据。不同类型的数据结构的差异就在于value的结构不同。

## 数据结构 - String

​	String是简单的key-value类型，value其实不仅是String，也可以是数字。

​	使用场景：记录信息Json，微博数，粉丝数(常规技术)

​	Redis的字符串是动态字符串，是可以修改的字符串，内部结构类似ArrayList，采用预分配冗余空间的方式减少内存的频繁分配。当字符串长度小于1M时，扩容都是加倍现有空间的，如果超过1M，每次只会增加1M的空间。最大长度为512M。

**常用命令**

| 命令 | 说明                            |
| ---- | ------------------------------- |
| get  | 获取指定key的值                 |
| set  | 设置指定key的值                 |
| incr | 将key中存储的数字加1            |
| decr | 将key中存储的数字减1            |
| mget | 获取所有(一个或多个)给定key的值 |
| mset | 批量设置指定key的值             |

## 数据结构 - List

​	List就是链表。相当于LinkedList。

​	Redis的列表结构常用来做异步队列使用。将需要延时处理的任务结构体序列化成字符串塞进Redis列表，另一个线程从这个列表中轮询数据进行处理。

​	使用场景：微博的关注列表，粉丝列表。

​	**慢操作**：lindex，相当于get(int index)，需要遍历链表。ltrim后面跟两个参数start_index和end_index这个区间保留，其他的删除。其中index可以为负数，-1为最后一个元素，-2为倒数第二个元素。

​	**快速列表**：再深入一些，Redis底层的List不是简单的LinkedList，而是称为快速链表(`quicklist`)的一种结构。

​	首先，在列表元素较少的时候Redis会选择使用一块连续的内存来存储数据，即ziplist(压缩列表)。它将所有的元素紧密的存储在一起，是一块连续的内存。当数据量变大时，才会改成quicklist。因为普通的链表需要的附加指针空间太大，会浪费空间，而且会加重内存碎片化。

​	Redis将链表和ziplist结合起来形成了quicklist。也就是将多个ziplist使用双向指针串起来使用。这样既满足了快速的插入删除性能，又不会出现太多的空间冗余。

**常用命令**

| 命令   | 说明                                       |
| ------ | ------------------------------------------ |
| lpush  | 将一个或多个值插入到列表头部               |
| rpush  | 在列表中添加一个或多个值                   |
| lpop   | 移出并获取列表的第一个元素                 |
| rpop   | 移除列表的最后一个元素，返回值为移除的元素 |
| lrange | 获取所有(一个或多个)给定的key              |

## 数据结构 - Set

​	相当于HashSet，它内部的键值对是无需且唯一的。

​	使用场景：实现如共同关注、共同喜好、二度好友

**常用命令**

| 命令     | 说明                           |
| -------- | ------------------------------ |
| sadd     | 向集合中添加一个或多个成员     |
| spop     | 移除并返回集合中的一个随机元素 |
| smembers | 返回集合中的所有成员           |
| sunion   | 返回所有给定集合的并集         |

## 数据结构 - Sorted set

​	类似于SortedSet和HashMap的结合体，一方面它是一个Set

​	使用场景：排行榜、按照用户投票和时间排序

**常用命令**

| 命令   | 说明                                                 |
| ------ | ---------------------------------------------------- |
| zadd   | 向有序集合添加一个或多个成员，或更新已存在成员的分数 |
| zrange | 通过索引区间返回有序集合中指定区间内的成员           |
| zrem   | 移除有序集合中的一个或多个成员                       |
| zcard  | 获取有序集合的成员数                                 |

## 数据结构 - Hash

​	相当于HashMap，同样是数组 + 链表。不同的是，Redis的值只能是字符串且rehash方式不一样。Java的HashMap在rehash时是全量的，非常耗费时间。而Redis由于单线程，为了提高性能，所以采用了渐进式rehash策略。

​	**渐进式rehash**：会在rehash的同时，保留新旧的两个hash结构，查询时会同事查询两个hash结构，然后在后续的定时任务中以及hash的子指令中，循序渐进地将旧hash内容一点点的迁移到新的hash结构中。当hash移除了最后一个元素后，该数据结构自动被删除，内存被回收。

​	使用场景：存储部分变更数据，如用户信息

| 命令    | 说明                                  |
| ------- | ------------------------------------- |
| hget    | 获取存储在哈希表中指定字段的值        |
| hset    | 将哈希表key中的字段field的值设为value |
| hgetall | 获取在哈希表中指定key的所有字段和值   |

## 数据结构 - GEO

​	3.2版本后开始对GEO(地理位置)支持岗

​	使用场景：LBS应用开发

| 命令              | 说明                                               |
| ----------------- | -------------------------------------------------- |
| geoadd            | 增加地理位置的坐标，可以批量添加地理位置           |
| geodist           | 获取两个地理位置的距离                             |
| geohash           | 获取某个地理位置的geohash值                        |
| geopos            | 获取指定位置的坐标，可以批量获取多个地理位置的坐标 |
| georadius         | 根据给定地理位置坐标获取指定范围内的地理位置集合   |
| georadiusbymember | 根据给定成员的位置获取指定范围内的位置信息集合     |

## 数据结构 - Stream

​	5.0版本后开始有的结构流

​	使用场景：消费者生产者场景(类似mq)

| 命令   | 说明                                   |
| ------ | -------------------------------------- |
| xadd   | 往指定的流中添加消息                   |
| xlen   | stream流中的消息数量                   |
| xdel   | 删除流中的消息                         |
| xrange | 返回流中满足给定id范围的消息           |
| xread  | 从一个或多个流中读取消息               |
| xinfo  | 检索关于刘和关联的消费者组的不同的信息 |

### 主从命令行

1. 防火墙
2. redis配置的bind到具体的ip
3. 从服务器命令：slaveof ip port / slaveof no one

### 主从配置文件

1. 配置文件里添加 slaveof ip port
2. 也可以用 replicaof

从服务器默认只读

### 主从复制流程

1. 从服务器通过psync命令发送服务器已有的同步进度(同步源ID、同步进度offset)
2. master收到请求，同步源为当前master，根据偏移量增量同步
3. 否则全量同步：master生成rdb文件，传输到slave

### 主从核心知识

+ 默认使用异步复制，slave和master之间异步确认处理的数据量
+ 一个master可以拥有多个slave
+ slave可以接受其他的slave链接。slave可以有下级的sub slave
+ 主从同步过程在master侧是非阻塞的
+ slave初次同步需要删除旧数据，加载新数据，会阻塞到来的链接请求

### 主从应用场景

+ 主从复制可以用来支持读写分离
+ slave服务器设定为只读，可以再数据安全的场景下
+ 可以使用主从复制来避免master持久化造成的开销。master关闭持久化，slave配置为不定期保存或者启用aof

> 重新启动的master从空数据集开始，如果一个slave试图和它同步会被清空
>
> 解决方案，手动讲从服务器变成主服务器或者将rdb文件转移到master

### 注意事项

+ 读写分离场景

  + 数据复制延时导致读取到国企数据或者读不到数据(网络原因、slave阻塞)

  + 从节点故障(多个client如何迁移)

+ 全量复制情况如下

  + 第一次建立主从关系或者runid不匹配会导致全量复制
  + 故障转移的时候会出现全量复制

+ 复制风暴

  + master故障重启，如果slave节点较多，所有slave都要复制，对服务器的性能，网络的压力都很大
  + 如果一个机器部署了多个master

+ 写能力有限

  + 主从复制还是只有一台master，提供的写服务能力有限

+ master故障情况下

  + 如果是master无持久化，slave开启持久化来保留数据的场景，建议不要配置redis自动重启
  + 启动redis自动重启，master启动后，无数据备份，可能导致集群数据丢失的情况

+ 带有效期的key

  + slave不会让key过期，而是等待master让key过期
  + 在lua脚本执行期间，不执行任何可以过期操作

### 读写分离

使用redis连接池实现

### 哨兵机制

![image.png](https://i.loli.net/2020/03/26/XPZYSiKzj1DmBuW.png)

> 使用sentinel的时候不需要连接redis，连接sentinel就可以

当主服务器挂掉时，哨兵会自动选举从服务器为主，并且之前的主服务器重新连接时会变成从节点。

停掉哨兵的时候不受影响，命令不经过哨兵，只有主服务器挂的时候才会用到哨兵，按照配置轮询哨兵，去选举新的主服务器

### 核心运作流程

哨兵通过ping和info来监控主从集群

```shell
SENTINEL get-master-addr-by-name mymaster
# 返回两个字段，端口和地址
1)"117.114.111.12"
2)"6379"
```

> 哨兵的配置文件会自动修改

![image.png](https://i.loli.net/2020/03/28/j5qZclshIaVRE9A.png)

### 哨兵机制7大核心概念

1. 哨兵如何知道Redis主从信息(自动发现机制)

   > 哨兵配置文件中，保存着主从集群中master的信息，可以通过info命令主动发现。

2. 什么是master主观下线

   > 单个哨兵认为Redis实例已经不能提供服务
   >
   > 检测机制：哨兵向redis发送ping请求，+PONG、-LOADING、-MASTERDOWN这三种情况视为正常，其他回复均无效

   对应配置文件：sentinel down-after-millseconds mymaster 1000

3. 什么是客观下线

   > 一定数量的哨兵认为master已经下线。
   >
   > 检测机制：当哨兵主管认为master下线后，会通过SENTINEL is-master-down-by-addr命令询问其他哨兵是否认为master已经下线，如果达成共识(打到quorum个数)，就会认为master节点客观下线，开始故障转移流程

   对应配置文件：sentinel monitor mymaster 117.114.111.12 6379 2

4. 哨兵之间如何通信(哨兵之间的自动发现)

   ![image.png](https://i.loli.net/2020/03/28/oYkflWwNxJGOHFD.png)

   > 哨兵间在redis中有一个主题订阅
   >
   > 哨兵之间通过命令进行通信
   >
   > 哨兵之间通过订阅发布进行通信

5. 那个哨兵负责故障转移(哨兵领导选举机制)
   基于Raft算法实现的选举机制，流程如下

   1. 拉票阶段：每个哨兵节点希望自己成为领导者，随机等待一定时间再拉票
   2. sentinel节点收到拉票命令后，如果没有收到或同意过其他sentinel节点的请求，就该同意sentinel的请求(每个sentinel只持有一个同意票数)
   3. 如果sentinel节点发现自己的票数已经超过一半的值，那么它就是领导者，进行故障转移
   4. 投票结束后，如果超过failover-timeout的时间内没有进行实际的故障转移，重新选举

   参考：http://thesecretlivesofdata.com

6. slave选举机制

   1. slave节点状态：非S_DOWN, O_DOWN, DISCONNECTED，判断规则：(down-after-milliseconds * 10) + milliseconds_since_master_is_in_SDOWN_state SENTINEL slaves mymaster
   2. 优先级；redis.conf中的一个配置项：slave-priority值越小，优先级越高
   3. 数据同步情况：Replication offset processed
   4. 最小的run id：run id比较方案：字典顺序，ASCII码

7. 最终主从切换流程

   + 针对即将成为master的slave节点，将其撤除主从集群
     自动执行：slaveof no one
   + 针对其他slave节点，使它们成为新的master的从属
     自动执行：slaveof new_master_host new_master_port

### 哨兵的启动和配置

![image.png](https://i.loli.net/2020/03/28/4re3NnPqEDy1SCj.png)

### 部署方案

1主2从，3哨兵。Redis集群非强一致性，有最终一致性

### Redis集群分片





























