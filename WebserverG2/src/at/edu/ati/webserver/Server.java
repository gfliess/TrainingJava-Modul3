/** 
 * Copyright 2013 SSI Schaefer PEEM GmbH. All Rights reserved. 
 * <br /> <br />
 * 
 * $Id$
 * <br /> <br />
 *
 */

package at.edu.ati.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.edu.ati.webserver.processor.IRequestProcessor;

/**
 * @author <a href="mailto:a.leitner@ssi-schaefer-peem.com">Andreas Leitner</a>
 * @author <a href="mailto:j.gruber@ssi-schaefer-peem.com">Johannes Gruber</a>
 * @author <a href="mailto:p.clement@ssi-schaefer-peem.com">Patrice Clement</a>
 */

public class Server implements Runnable {

  private int port;
  private Selector selector;
  private ServerSocketChannel serverChannel;
  private ExecutorService executor;
  private IRequestProcessor processor;
  
  
  private boolean isRunning = true;

  public Server(int port, String htDocs, IRequestProcessor processor) throws IOException {
    this.processor = processor;
    this.port = port;
    selector = initSelector();
    executor = Executors.newFixedThreadPool(10);
    System.out.println("Set up server socket channel [PORT=" + port + "]");
  }

  public void run() {
    System.out.println("Start server");
    while (isRunning) {
      try {
        selector.select();

        // Iterate over the set of keys for which events are available
        Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
        while (selectedKeys.hasNext()) {
          SelectionKey key = (SelectionKey) selectedKeys.next();
          selectedKeys.remove();
          if (!key.isValid()) {
            continue;
          }
          if (key.isAcceptable()) {
            accept(key);
          } else if (key.isReadable()) {
            read(key);
          } else if (key.isWritable()) {
            write(key);
          }
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    System.out.println("Stopped processing requests");
  }
  
  public void shutdown() {
    isRunning = false;
    try {
      selector.close();
      serverChannel.close();
      executor.shutdown();
    } catch (IOException ioe) {
      System.err.println("Failed to shutdown server");
      ioe.printStackTrace();
    }
  }
  
  private void write(SelectionKey key) throws IOException {
    
  }
  
  private void read(SelectionKey key) throws IOException {
 		SocketChannel channel = (SocketChannel) key.channel();
    
    System.out.println("read");
    ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    int read = channel.read(readBuffer);
    
    if (read == -1) {
      key.channel().close();
      key.cancel();
    }
    
    String requestString = new String(readBuffer.array());
    processor.addData(requestString, channel);
    executor.execute(processor);
    System.out.println("read end");
  
  }
  
  private void accept(SelectionKey key) throws IOException {
  	System.out.println("accept begin");
  	ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

    SocketChannel socketChannel = serverSocketChannel.accept();
    socketChannel.configureBlocking(false);

    socketChannel.register(this.selector, SelectionKey.OP_READ);
  	System.out.println("accept end");
  }
  
  private Selector initSelector() throws IOException {
    Selector socketSelector = SelectorProvider.provider().openSelector();
    serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);
    serverChannel.socket().bind(new InetSocketAddress(port));
    serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

    return socketSelector;
  }
  
}

//---------------------------- Revision History ----------------------------
//$Log$
//
