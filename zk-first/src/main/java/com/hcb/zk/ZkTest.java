package com.hcb.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;




public class ZkTest {

    /**
     * @param args
     */
    public static String url = "192.168.92.130:2181";
    public static String root = "/zk5";
    public static String child1 = "/zk5/child5";

    public static void main(String[] args) throws Exception {

        //参数watcher的值可以为null, 不添表明，不监听，及获取zk对象之后，服务端的变化
        //不会再对此客户端产生影响
        ZooKeeper zk = new ZooKeeper(url, 3000, new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                //如果没有连接，type 是null
                System.out.println("触发了事件：" + event.getType());
                System.out.println("事件状态：" + event.getState());
            }
        });

        //确保已经连接上了，再执行下面的，不然会抛出错误。
        while ( ZooKeeper.States.CONNECTED != zk.getState()) {
            Thread.sleep(1000);
        }


        //要先判断是否存在，如果已经存在了或抛出异常。
        if (zk.exists(root, true) == null) {
            //root是这个节点的数据。
            zk.create(root, "root".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //Ids.OPEN_ACL_UNSAFE node 的访问权限， CreateMode.PERSISTENT 四种module
        }
        if (zk.exists(child1, true) == null) {
            //就CreateMode.EPHEMERAL 表示临时节点，创建之后就删除了。
            zk.create(child1, "child1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // zk。getData(,,版本号)， null 表示最新。
        //zk.getData 第二个参数可以指定一个wathcer,  也可以是boolean 值，如果boolean，表示是否监听。 设置为false,
        //那么， 比如我在java 代码中用死循环不断getData,
        // 这时另外一个客户端，修改了该节点的值， 本处读到的值却没有修改，应为false 是不监听。
        String rootDataString = new String(zk.getData(root, true, null));
        System.out.println("rootDataString-null:" + rootDataString);

        //-1是版本号
        zk.setData(root, "rootUpdate1".getBytes(), -1);
        rootDataString = new String(zk.getData(root, true, null));
        System.out.println("rootDataString-1:" + rootDataString);

        //获取root节点的子节点。
        System.out.println(zk.getChildren(root, true));

        System.out.println("----------------------");
        System.out.println(new String(zk.getData(child1, false, null)));
        zk.exists(child1, false);
        zk.setData(child1, "child1Update1".getBytes(), -1);
        System.out.println(new String(zk.getData(child1, false, null)));
//		zk.setData(child1, "child1Update1".getBytes(), -1) ;






        zk.close();








    }

}
