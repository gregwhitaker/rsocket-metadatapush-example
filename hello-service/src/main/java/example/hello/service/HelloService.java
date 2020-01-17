package example.hello.service;

import io.rsocket.AbstractRSocket;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class HelloService {
    private static final Logger LOG = LoggerFactory.getLogger(HelloService.class);

    private static final String EN_US_MESSAGE = "Hello, %s!";
    private static final String FR_FR_MESSAGE = "Bonjour, %s!";

    public static void main(String... args) throws Exception {
        RSocketFactory.receive()
                .frameDecoder(PayloadDecoder.DEFAULT)
                .acceptor(new SocketAcceptor() {
                    @Override
                    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
                        return Mono.just(new AbstractRSocket() {
                            volatile String messageFormat = EN_US_MESSAGE;

                            @Override
                            public Mono<Payload> requestResponse(Payload payload) {
                                final String name = payload.getDataUtf8();
                                return Mono.just(DefaultPayload.create(String.format(messageFormat, name)));
                            }

                            @Override
                            public Mono<Void> metadataPush(Payload payload) {
                                final String locale = payload.getDataUtf8();

                                if (locale.equalsIgnoreCase("en_us")) {
                                    messageFormat = EN_US_MESSAGE;
                                    return Mono.empty();
                                } else if (locale.equalsIgnoreCase("fr_fr")) {
                                    messageFormat = FR_FR_MESSAGE;
                                    return Mono.empty();
                                } else {
                                    return Mono.error(new IllegalArgumentException("Invalid local specified"));
                                }
                            }
                        });
                    }
                })
                .transport(TcpServerTransport.create(7000))
                .start()
                .block();

        LOG.info("RSocket server started on port: 7000");

        Thread.currentThread().join();
    }
}
