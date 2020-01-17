package example.hello.client;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.metadata.CompositeMetadataFlyweight;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.ByteBufPayload;
import io.rsocket.util.DefaultPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;

public class HelloClient {
    private static final Logger LOG = LoggerFactory.getLogger(HelloClient.class);

    public static void main(String... args) throws Exception {
        final String name = getNameFromArgs(args);

        RSocket rSocket = RSocketFactory.connect()
                .transport(TcpClientTransport.create(7000))
                .start()
                .block();

        LOG.info("Sending request for '{}'", name);

        // Sending the request
        String response = rSocket.requestResponse(DefaultPayload.create(name))
                .map(Payload::getDataUtf8)
                .block();

        LOG.info("Response: {}", response);

        LOG.info("Sending metadata push to change locale to 'fr_fr'");

        // Create composite metadata to send
        CompositeByteBuf metadataByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        CompositeMetadataFlyweight.encodeAndAddMetadata(
                metadataByteBuf,
                ByteBufAllocator.DEFAULT,
                "messaging/x.hello.messageformat",
                ByteBufAllocator.DEFAULT.buffer().writeBytes("fr_fr".getBytes()));

        rSocket.metadataPush(ByteBufPayload.create(Unpooled.EMPTY_BUFFER, metadataByteBuf))
                .block();

        LOG.info("Sending request for '{}'", name);

        response = rSocket.requestResponse(DefaultPayload.create(name))
                .map(Payload::getDataUtf8)
                .block();

        LOG.info("Response: {}", response);
    }

    private static String getNameFromArgs(String... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("parameter 0 must be the name of the hello message recipient");
        }

        return args[0];
    }
}
