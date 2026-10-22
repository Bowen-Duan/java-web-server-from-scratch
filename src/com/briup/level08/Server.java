package com.briup.level08;

import com.briup.level08.system.SystemConstant;
import com.briup.level08.task.Task;
import com.briup.level08.utils.LoadArgsUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        //声明服务器对象
        ServerSocket serverSocket = null;

        //声明线程池
        ExecutorService pool = null;

        try {
            //创建服务器对象
            serverSocket = new ServerSocket(Integer.parseInt(LoadArgsUtils.getValue(SystemConstant.PORT_KEY, SystemConstant.CONFIG_FLAG)));
            //创建线程池对象
            pool = Executors.newFixedThreadPool(Integer.parseInt(LoadArgsUtils.getValue(SystemConstant.THREAD_COUNT_KEY,SystemConstant.CONFIG_FLAG)));

            System.out.println("服务器启动成功，等待客户端连接.....");
            while (true){
                //服务器接受客户端连接
                Socket socket = serverSocket.accept();

                System.out.println("客户端连接成功,客户端信息"+socket);

                //创建任务对象
                Runnable task = new Task(socket);
                //将任务分配给线程池
                pool.submit(task);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (pool != null){
                    pool.shutdown();
                }

                if (serverSocket != null){
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}