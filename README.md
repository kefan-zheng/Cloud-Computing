# 云计算课程项目报告



## 项目成员

**1950072 郑柯凡 1953981 吴昊天**



## 目录

+ ### [系统架构](#系统架构)

+ ### [文件管理方法](#文件管理方法)

+ ###  [计算任务分配机制]()

+ ### [实现过程]()

























## 系统架构



### 系统组成

+ 4台CentOS Linux release 7.9.2009 (Core)虚拟机

|       Master       |     Slave1     |     Slave2     |     Slave3     |
| :----------------: | :------------: | :------------: | :------------: |
| IP：192.168.43.220 | 192.168.43.221 | 192.168.43.222 | 192.168.43.223 |
|      NameNode      |     —————      |     —————      |     —————      |
|       —————        |    DataNode    |    DataNode    |    DataNode    |

+ 虚拟机软件
  + VMware Fusion
  + VMware Workstation 15 Pro

+ 软件版本
  + hadoop 2.10.1
  + java-1.8.0-openjdk-devel.x86_64
  + hive ❌❌❌
  + 

### hadoop架构

#### 概念

- Apache Hadoop是一款支持数据密集型分布式应用并以Apache 2.0许可协议发布的开源软件框架。它支持在商品硬件构建的大型集群上运行的应用程序。Hadoop是根据Google公司发表的MapReduce和Google档案系统的论文自行实作而成。
- Hadoop是一套开源的软件平台，利用服务器集群，根据用户的自定义业务逻辑，对海量数据进行分布式处理。诞生于2006年。主要目标是对分布式环境下的“大数据”以一种可靠、高效、可伸缩的方式处理。
- Hadoop框架透明地为应用提供可靠性和数据移动。它实现了名为MapReduce的编程范式：应用程序被分割成许多小部分，而每个部分都能在集群中的任意节点上执行或重新执行。
- Hadoop还提供了分布式文件系统，用以存储所有计算节点的数据，这为整个集群带来了非常高的带宽。

#### 基本组成部分

![Dpz74XZ](%E9%A1%B9%E7%9B%AE%E6%8A%A5%E5%91%8A.assets/Dpz74XZ.jpg)

### 文件管理方法



#### HDFS

HDFS是一个分布式的文件存储系统，它起源于Apache Nutch项目

- 支持高容错，它可以部署在低成本的硬件上。
- 支持高吞吐量的访问，尤其当程序需要读取大的数据集合时
- 支持流式访问，因此并没有严格的遵守POSIX协议

一个HDFS实例包含了成千上万个服务器，他能够检测故障并快速的自动恢复；应用程序需要通过流式的方式，去访问它们在HDFS上的数据；HDFS应用程序的访问模型是，通常需要一次写入、多次读取。一个文件除了创建（created）、写入（written）、关闭（closed）之后，就不需要再进行修改了，除了追加（appends）和截断（truncates）之外；HDFS提供了接口，让应用程序在离它们需要的数据更近的地方，进行运行和计算。

##### NameNode职责：

- 执行文件系统命名空间的相关操作，如打开，关闭和重命名文件和目录。
- 决定数据块（blocks）到DataNode的映射。

##### DataNode职责：

- 在HDFS内部，一个文件会被分成一个或多个块，这些块存储在DataNode中。
- 负责提供来自文件系统客户端的读取和写入请求。
- 根据来自NameNode的指令，执行数据块创建，删除和复制操作。

![v2-a5fe82bfe8c0987a02eadee658e9216c_1440w](%E9%A1%B9%E7%9B%AE%E6%8A%A5%E5%91%8A.assets/v2-a5fe82bfe8c0987a02eadee658e9216c_1440w.png)

#### 文件分块方法

hdfs将所有的文件全部抽象成为block块来进行存储，不管文件大小，全部一视同仁都是以block块的统一大小和形式进行存储，方便我们的分布式文件系统对文件的管理

所有的文件都是以block块的方式存放在HDFS文件系统当中，在Hadoop1当中，文件的block块默认大小是64M，Hadoop2当中，文件的block块大小默认是128M，block块的大小可以通过hdfs-site.xml当中的配置文件进行指定

在项目中使用的配置是分块大小是128M，这个大小是基于最佳传输损耗理论而来的，**最佳传输损耗理论**：在一次传输中，寻址时间占用总传输时间的1%时，本次传输的损耗最小，为最佳性价比传输。目前硬件的发展条件，普通磁盘写的速率大概为100M/S, 寻址时间一般为10ms。虽然现在的固态硬盘的读写速度远大于这个值，但受网络和虚拟机限制，没有修改默认的分块大小

**文件分块截图**：❌❌❌❌



#### 文件备份方法

- 集群中的名称节点（NameNode）会把文件系统的变化以追加保存到日志文件edits中。

- 当名称节点（NameNode）启动时，会从镜像文件 fsimage 中读取HDFS的状态，并且把edits文件中记录的操作应用到fsimage，也就是合并到fsimage中去。合并后更新fsimage的HDFS状态，创建一个新的edits文件来记录文件系统的变化

- Secondary NameNode定期合并fsimage和edits日志，把edits日志文件大小控制在一个限度下

- fs.checkpoint.period 指定两次checkpoint的最大时间间隔，默认3600秒

- #### 文件备份测试

  + ❌❌❌❌❌
  + ❌❌❌❌

#### 文件一致性方法

客户端上传文件时，NameNode首先往edits log文件中记录元数据的操作日志。与此同时，NameNode将会在磁盘做一份持久化处理（fsimage文件）

+ 在edits logs满之前对内存和fsimage的数据做同步
+ （实际上只需要合并edits logs和fsimage上的数据即可，然后edits logs上的数据即可清除）
+ 而当edits logs满之后，文件的上传不能中断，所以将会往一个新的文件edits.new上写数据，
+ 而老的edits logs的合并操作将由secondNameNode来完成，即所谓的checkpoint操作。

SecondaryNameNode工作过程：

+ secondary通知namenode切换edits文件
+ secondary从namenode获得fsimage和edits(通过http)
+ secondary将fsimage载入内存，然后开始合并edits
+ secondary将新的fsimage发回给namenode
+ namenode用新的fsimage替换旧的fsimage

**文件一致性测试**

+ ❌❌❌❌❌



### 计算任务分配机制



#### Mapreduce作业运行过程

1. JobTracker接收到Job对象对其submitJob()方法的调用后，就会把这个调用放入一个内部队列中，交由作业调度器(Job Scheduler)进行调度,并对其进行初始化。
2. 初始化工作：创建一个表示正在运行作业的对象(它封装任务和记录信息，以便跟踪任务的状态和进程)
3. 创建任务运行列表，包括map和reduce任务，创建任务过程分析
   + 作业调度器从HDFS中获取JobClient已计算好的输入分片信息，然后为每个分片创建一个map任务，并且创建Reduce任务
   + 除了map和reduce任务，还有setupJob和cleanupJob需要建立
4. 任务分配
   + TaskTracker定期通过“心跳”与JobTracker进行通信，主要是告知JobTracker自身是否还存活，以及是否已经准备好运行新的任务等
   + JobTracker在为TaskTracker选择任务之前，必须先通过作业调度器选定任务所在的作业
   + 每个TaskTracker都有固定数量的map和reduce任务槽，数量取决于TaskTracker节点的CPU内核数量和内存大小
   + JobTracker分配map任务时会选取与输入分片最近的TaskTracker(任务本地化)。
5. 任务执行
   + TaskTracker分配到一个任务后，通过从HDFS把作业的Jar文件复制到TaskTracker所在的文件系统（Jar本地化用来启动JVM），同时TaskTracker将应用程序所需要的全部文件从分布式缓存复制到本地磁盘
   + TaskTracker为任务新建一个本地工作目录，并把Jar文件中的内容解压到这个文件夹中
   + TaskTracker新建一个TaskRunner实例来启动一个新的JVM来运行每个Task(包括MapTask和ReduceTask)。子进程通过umbilical接口与父进程进行通信，Task的子进程每隔几秒便告知父进程它的进度，直到任务完成
6. 进度和状态更新过程
   + Child JVM有独立的线程，每隔3秒检查一次任务更新标志，如有更新则报告给 TaskTracker,TaskTracker每隔5秒给JobTracker发一次心跳信息
   + 同时JobClient通过每秒查询JobTracker来获得最新状态，并且输出到控制台上
7. 作业完成：当JobTracker收到作业最后一个任务已完成的通知后，便把作业的状态设置为"成功"



### 实现过程

#### 搭建尝试

+ 在项目初期，打算通过Docker在服务器配置hadoop环境，实现四台服务器上的完全分布式集群，在完成基本配置后，启动hadoop或者运行mapreduce

#### 实际搭建

#### 程序编码

#### 测试
