package com.system.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.system.spring.WsTestUtils.StompSessionHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebSaleClipApplicationTests {

	private int port = 8080;
	private WebSocketStompClient stompClient;
	private StompSession stompSession;
	private WsTestUtils wsTestUtils = new WsTestUtils();
	private WebSocketHttpHeaders headers;

	@Before
	public void setUp() throws Exception {
		String url = "ws://localhost:" + port + "/ws";
		headers.add("Authorization",
				"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbImFjdG9yIiwidmlld2VyIiwiYWRtaW4iXSwiZW1haWwiOiIkMmEkMTAkREhPaWRCQ3UwMktFL2RneVNhQlN2ZTFGa040NUhVdFZ4TGoyaFJiTkVZOTJyR1BiRzZhZnUiLCJpc0VuYWJsZWQiOnRydWUsImlzUHJlbWl1bSI6ZmFsc2UsImlhdCI6MTY1MDY4MTU2NCwiZXhwIjoxNjUwNjgzMzY0fQ.I-UsUEx__K7hQuQoVMtm8rELcmZ9WCgCsMNL1j-yGgoicjelXJWbNp4-qIXPOnaN4ysNoavT2p1aK0lQBNaUIA");
		stompClient = wsTestUtils.createSocketStompClient();
		stompSession = stompClient.connect(URI.create(url), headers, null, new StompSessionHandler()).get();
	}

	@Test
	public void connectsToSocket() throws Exception {
		assertThat(stompSession.isConnected()).isTrue();
	}
}
