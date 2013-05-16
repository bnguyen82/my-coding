package com.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;



public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            long time = System.currentTimeMillis();
            String message = slurp(input, 9999);
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
    public static String slurp(final InputStream is, final int bufferSize)
    {
      final char[] buffer = new char[bufferSize];
      final StringBuilder out = new StringBuilder();
      try {
        final Reader in = new InputStreamReader(is, "UTF-8");
        try {
          for (;;) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
              break;
            out.append(buffer, 0, rsz);
          }
        }
        finally {
          in.close();
        }
      }
      catch (UnsupportedEncodingException ex) {
        /* ... */
      }
      catch (IOException ex) {
          /* ... */
      }
      return out.toString();
    }

}
