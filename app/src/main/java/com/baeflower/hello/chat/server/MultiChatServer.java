package com.baeflower.hello.chat.server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiChatServer {
	// 5000
	private static final int PORT = 5000;

	private static final int CONNECT = 100;
	private static final int CHAT_START = 101;
	private static final int MESSAGING = 102;


	private List<ClientInfo> mClientList;		// Client 목록 리스트
	private ServerSocket mServerSocket;			// 서버 소켓

	public MultiChatServer() {
		// 동기화 된 ArrayList
		mClientList = Collections.synchronizedList(new ArrayList<ClientInfo>());
	}

	public void start() {
		Socket socket;

		try {
			mServerSocket = new ServerSocket(PORT);
			System.out.println("서버 시작!!");

			while (true) {
				socket = mServerSocket.accept();
				System.out.println(socket.getInetAddress() + "에서 접속함");

				new ServerReciver(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addClient(ClientInfo client) {
		mClientList.add(client);
		sendToAll(client.getNickName() + " 님이 접속하였습니다. " + mClientList.size() + "명 접속중");
	}

	private void removeClient(ClientInfo client) {
		mClientList.remove(client);
		sendToAll(client.getNickName() + " 님이 퇴장하였습니다. " + mClientList.size() + "명 접속중");
	}

	private void sendToAll(String message) {
		System.out.println(message);

		// 멀티 스레딩 처리
		// 여러 쓰레드에서 mClientList 에 접근 시 하나의 쓰레드만 사용하도록 하는 방법
		synchronized (mClientList) {
			//for (int i = 0; i < mClientList.size(); i++)
			for (ClientInfo client : mClientList) {
//				try {
//					client.getOutput().writeUTF(message);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}
	}

	class ServerReciver extends Thread {
		private DataInputStream mInputStream;
		private DataOutputStream mOutputStream;

		private ClientInfo mClientInfo;

		public ServerReciver(Socket socket) {
			try {
				mInputStream = new DataInputStream(socket.getInputStream());
				mOutputStream = new DataOutputStream(socket.getOutputStream());

				//수신부분
				byte[] in = new byte[100];
				mInputStream.read(in, 0, in.length); // buffer, byte offset, byte count

				Integer flag = new Integer(in[0]);
				System.out.println("클라이언트에서 받은 flag : " + flag);

				switch (flag) {
					case CONNECT:
						connect();
						break;
					case CHAT_START:
						chatStart();
						break;
					case MESSAGING:
						messaging();
						break;
				}

				byte[] withoutFlag = new byte[in.length - 1];
				for (int i = 1; i < in.length; i++) {
					withoutFlag[i - 1] = in[i];
				}

				String nickName = new String(withoutFlag, 0 , withoutFlag.length, "UTF-8"); //byte형을 string형으로 바꿔 초기화 한다.
				nickName = nickName.trim();   //trim()을 해주지 않으면 공백부분이 제대로 출력되지 않는다.

				System.out.println("서버에서 받은 string byte : " + nickName);

				mClientInfo = new ClientInfo(nickName, mOutputStream);
				addClient(mClientInfo);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				// 계속 듣기만
				while (mInputStream != null) {
					sendToAll(mInputStream.readUTF());
				}
			} catch (IOException e) {

			} finally {
				// 접속 종료시
				removeClient(mClientInfo);
			}
		}


		// 처음 접속했을 때
		private void connect() {
			// 접속자 리스트 넘겨주기

			/*
				Object 에서 byte[]

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = new ObjectOutputStream(bos);
				out.writeObject(yourObject);
				byte[] yourBytes = bos.toByteArray();

				byte[]에서 Object

				ByteArrayIntputSream bis = new ByteArrayInputStream(yourBytes);
				ObjectInput in = new ObjectInputStream(bis);
				Object o = in.readObject();
			*/

			synchronized (mClientList) {
				try {
					/* */

					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutput out = new ObjectOutputStream(bos);

					out.writeObject(mClientList);
					byte[] byteDataOfClient = bos.toByteArray();

					// byteDataOfClient = addFlagToByteData(byteDataOfClient, CHAT_START);

					bos.write(byteDataOfClient);
					bos.flush();

//					ObjectOutputStream out = new ObjectOutputStream(mOutputStream);
//					out.writeObject(mClientList);
//					out.flush();


				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void chatStart() {

		}

		private void messaging() {

		}

	}



	private byte[] addFlagToByteData(byte[] data, Integer flag) {

		byte[] newData = new byte[data.length + 1];
		newData[0] = flag.byteValue();

		for (int i = 1; i < newData.length; i++) {
			newData[i] = data[i - 1];
		}

		return newData;
	}


	public static void main(String[] args) {
		new MultiChatServer().start();
	}
}
