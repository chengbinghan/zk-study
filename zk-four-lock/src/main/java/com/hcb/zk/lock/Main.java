package com.hcb.zk.lock;

import java.net.InetAddress;

public class Main {

	/**
	 * @param args
	 * 控制不同进程使用某公共资源
	 * 
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		InetAddress address = InetAddress.getLocalHost();
		Lock lock = LockFactory.getLock("/root/test7", address.toString());
		
		while(true)
		{
			if (lock != null) {
				//to do
                boolean lock1 = lock.isLock();
                System.out.println("是否lock 了1：" + lock1);
                lock.lock();
                boolean lock2 = lock.isLock();
                System.out.println("是否lock 了2：" + lock2);
                lock.unLock();
                boolean lock3 = lock.isLock();
                System.out.println("是否lock 了3：" + lock3);
            } else {
				Thread.sleep(1000);
			}
		}
		
		
	}

}
