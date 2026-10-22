package com.briup.level07;

import com.briup.level07.task.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        //声明服务器对象
        ServerSocket serverSocket = null;

        //声明线程池对象
        ExecutorService pool = null;

        try {
            //初始化服务器对象
            serverSocket = new ServerSocket(9999);

            //初始化线程池对象
            pool = Executors.newFixedThreadPool(10);

            System.out.println("服务器启动成功，等待客户端连接.....");

            //服务器要一直保持启动状态  死循环
            while (true){
                //服务器接受客户端连接  accept是一个阻塞方法
                Socket socket = serverSocket.accept();
                System.out.println("客户端连接成功，客户端："+socket);
                //开辟线程 专门服务某一个客户端
                //创建任务
                Runnable task = new Task(socket);
                //将任务交给线程池处理
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