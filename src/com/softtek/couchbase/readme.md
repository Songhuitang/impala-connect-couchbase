```
 Note:任何形式的转载请注明原文
```
>此文章关注于单机模式的Couchbase,重点在于Java连接Couchbase的入门教程。具体如何搭建Couchbase集群请自行搜索。

----------------------------------------------------
> Couchbase，是MemBase与couchDb这两个NoSQL数据库的合并的产物，是一个分布式的面向文档的NoSQL数据库管理系统,即拥有CouchDB的简单和可靠以及Memcached的高性能。

> [官方文档](https://developer.couchbase.com/documentation/server/4.5/concepts/concepts-intro.html)关于Couchbase使用场景的说明
```
Couchbase Server can be used as a
- managed cache tier
- a key-value store
- a document database
```

-------------------------------------------------------
####安装Couchbase
进入官方网站，找到[下载链接](https://www.couchbase.com/downloads#couchbase-server)。里面可以选择不同的版本 `社区版 / 商业版`，我们此篇文章是讲单机Couchbase的使用，不涉及到商业版本的功能，所以选择社区版。 笔者使用的是Mac OS X, 所以选择Mac的社区版。

![](http://upload-images.jianshu.io/upload_images/4077251-f05ff4f4e368f2db.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

将下载的文件解压得到`app`文件，拖入到应用程序即可。点击应用里的Couchbase直接运行。

```
在启动时会提示进入配置页面，自带的测试数据库可以选择性安装，以方便开始我们的demo. 
总共有三个自带`buckets`
- beer-sample
- gamesim-sample
- travel-sample
```
```
也可以在以后的Couchbase UI页面找到 Setting -> Sample Buckets 选择安装
```

####Couchbase UI

因为我们搭建的是本地环境没有集群，所以访问`http://localhost:8091/`即可看到我们的Couchbase信息。

具体的页面分为几个标签页（可点击标题的链接查看官网的说明）

> [Overview](https://developer.couchbase.com/documentation/server/4.5/admin/ui-intro.htmlhttps://developer.couchbase.com/documentation/server/4.5/admin/ui-intro.html)  - **Couchbase 概览**
```
显示我们的所有的Cluster / Buckets / Servers 信息
```

-------------------------------------------------------
> [Server Node](https://developer.couchbase.com/documentation/server/4.5/clustersetup/server-setup.html) - **服务器节点**
```
对于服务器节点的管理，包括增加、修改和删除。
```

-------------------------------------------------------
> [Data Buckets](https://developer.couchbase.com/documentation/server/4.5/clustersetup/bucket-setup.html)
Buckets 通常用作系统间的数据分发。默认Couchbase服务器会创建一个名字为default的Buckets
```
此页面可以对Buckets进行增加 / 删除 / 修改 / flush
```

-------------------------------------------------------
> **Query**
这是我们最经常用到的功能，可以在此处输入类SQL语句(`N1ql`)进行数据的增删改查操作。

-------------------------------------------------------
> [Indexs](https://developer.couchbase.com/documentation/server/4.5/indexes/indexing-overview.html) - **索引**
索引主要用于加快数据查询的效率。Couchbase的索引一般是放在单节点的。

-------------------------------------------------------
> 其他
```
Secrity / Log / Settings
```
其他选项就不单独介绍，在此入门使用中基本用不到。具体内容可在参阅官网教程。

-------------------------------------------------------
####Couchbase的使用
[官网](https://developer.couchbase.com/documentation/server/4.5/data-modeling/data-modeling-phases.html)关于Couchbase和传统RMDB的对比

| Couchbase Server | Relational databases |
| ------| ------ |
| Buckets | Databases |
| Buckets or Items (with type designator attribute) | Tables |
| Items (key-value or document) | Rows |
| Index | Index |

如果在安装设置的时候已经配置安装了自带数据库`beer-sample`,则可以在Couchbase UI 的Query页面执行以下指令
```
SELECT brewery_id, name FROM `beer-sample` 
     WHERE brewery_id IS NOT MISSING AND type="beer" LIMIT 5;
```

![执行结果](http://upload-images.jianshu.io/upload_images/4077251-023301b1505e42b1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

最终会得到JSON数据格式的结果。由此可以看出，我们执行和SQL语句非常接近的语法，得到的是一个JSON串(文档)。

-------------------------------------------------------
####Java 调用Couchbase
[官网Demo](https://developer.couchbase.com/documentation/server/current/sdk/java/start-using-sdk.html)已经写的比较清楚。 这儿我们把完整的代码展示一下。
首先我们在`IntelliJ Idea`(`Eclipse`也可)里创建一个Maven项目。 在项目的根目录下的`pom.xml`里增加jar包依赖
```
<dependencies>
  <dependency>
    <groupId>com.couchbase.client</groupId>
    <artifactId>java-client</artifactId>
    <version>2.4.6</version>
  </dependency>
</dependencies>
```
创建`Example.java`

```
Note: 在查询指定的buckets之前一定要保证有相应的PrimaryIndex，否则会提示查询错误。
```
```
import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.query.*;

public class Example {

    public static void main(String... args) throws Exception {
        // Initialize the Connection
        Cluster cluster = CouchbaseCluster.create("localhost");

        // For more than one server -->
        // Connects to a cluster on 10.0.0.1 and tries 10.0.0.2
        //Cluster cluster = CouchbaseCluster.create("10.0.0.1", "10.0.0.2");

        Bucket bucket = cluster.openBucket("default");

        // Create a JSON Document
        JsonObject arthur = JsonObject.create()
            .put("name", "Arthur")
            .put("email", "kingarthur@couchbase.com")
            .put("interests", JsonArray.from("Holy Grail", "African Swallows"));

        // Store the Document
        bucket.upsert(JsonDocument.create("u:king_arthur", arthur));

        // Load the Document and print it
        // Prints Content and Metadata of the stored Document
        System.out.println(bucket.get("u:king_arthur"));

        // Create a N1QL Primary Index (but ignore if it exists)
        bucket.bucketManager().createN1qlPrimaryIndex(true, false);

        // Perform a N1QL Query
        N1qlQueryResult result = bucket.query(
            N1qlQuery.parameterized("SELECT name FROM default WHERE $1 IN interests",
            JsonArray.from("African Swallows"))
        );

        // Print each found Row
        for (N1qlQueryRow row : result) {
            // Prints {"name":"Arthur"}
            System.out.println(row);
        }

      // Just close a single bucket
      bucket.close();

      // Disconnect and close all buckets
      cluster.disconnect();
    }
}
```
详细的Demo请参考[代码](https://github.com/Songhuitang/impala-connect-couchbase/tree/master/src/com/softtek/couchbase)

--------------------------------------------------
######至此你已经是Couchbase的入门选手了🍺🍺
