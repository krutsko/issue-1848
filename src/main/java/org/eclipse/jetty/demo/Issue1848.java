package org.eclipse.jetty.demo;

import org.eclipse.jetty.io.ArrayByteBufferPool;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Issue1848
{
    private static final Logger LOG = Log.getLogger(Issue1848.class);

    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        final ServerConnector connector = new ServerConnector(server,
                Executors.newFixedThreadPool(10),
                new ScheduledExecutorScheduler(),
                new ArrayByteBufferPool(),
                2,
                2,
                new HttpConnectionFactory());
        connector.setPort(8005);

        if (args.length > 0)
        {
            int lingerTimeMs = Integer.parseInt(args[0]);
            LOG.info("Using connector.setSoLingerTime({})", lingerTimeMs);
            connector.setSoLingerTime(lingerTimeMs);
        }
        else
        {
            LOG.info("Not setting connector.setSoLingerTime()");
        }

        int payloadSize = 5_000_000;
        if (args.length == 2)
        {
            payloadSize = Integer.parseInt(args[1]);
        }
        LOG.info("Using response payload size={} bytes", payloadSize);
        byte[] chars = new byte[payloadSize];

        connector.setName("default");
        server.addConnector(connector);
        server.setHandler(new AbstractHandler()
        {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
            {
                response.setContentType("application/octet-stream");
                response.setStatus(HttpServletResponse.SC_OK);
                Arrays.fill(chars, (byte) 'x');
                response.getOutputStream().write("Start of content...\n".getBytes(UTF_8));
                response.getOutputStream().write(chars);
                response.getOutputStream().write("\nEnd of content\n".getBytes(UTF_8));
                baseRequest.setHandled(true);
            }
        });
        server.start();
        server.join();
    }
}
