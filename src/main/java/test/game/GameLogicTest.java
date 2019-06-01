package test.game;

import game.communication.LobbyServerDispatcher;
import game.communication.ServerDispatcher;
import game.logging.Log;
import game.vo.Message;


public class GameLogicTest {
	private static final String TAG = GameLogicTest.class.getSimpleName();

//	@Mock
//	Socket mockSocket;
//	@Mock
//	private InputStream mockInputStream;

//	@Before
//	public void setUp() {
//		MockitoAnnotations.initMocks(this);
//	}

//	@Test
	public void testGameClosesCorrectWithoutUsers() {
		Log.i(TAG, "Test started");
//		mockSocket = mock(Socket.class);
//		mockInputStream = mock(InputStream.class);

		LobbyServerDispatcher lobby = new LobbyServerDispatcher();
//		ClientInfo clientInfo = createClientInfo(lobby);

		ServerDispatcher server = lobby.createServer("");

		String jsonReq = "{request_type='JOIN_GAME', user_id='6', hero_id='16', game_id='" + server.getServerId() + "'}";
		Message aMessage = new Message();
		aMessage.setMessage(jsonReq);
//		lobby.handleClientRequest(clientInfo, aMessage);
	}

//	private ClientInfo createClientInfo(LobbyServerDispatcher lobby) {
//		ClientInfo clientInfo = new ClientInfo();
//		ClientListener clientListener = null;
//		try {
//			clientListener = new ClientListener(clientInfo, lobby);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			when(mockSocket.getInputStream()).thenReturn(mockInputStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		clientInfo.setmSocket(mockSocket);
//
//		clientInfo.setClientListener(clientListener);
//		return clientInfo;
//	}
}
