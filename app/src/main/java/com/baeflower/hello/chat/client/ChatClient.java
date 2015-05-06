package com.baeflower.hello.chat.client;

import android.util.Log;

import com.baeflower.hello.chat.ChatMessageActivity;
import com.baeflower.hello.chat.handler.ChatMessageHandler;
import com.baeflower.hello.chat.util.ChatConstants;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

	private static final String TAG = ChatClient.class.getSimpleName();


	// suwonsmartapp.iptime.org
	// 192.168.0.222
	// 192.168.0.27
	private final static String SERVER_HOST = "192.168.0.27";

	private final static int SERVER_PORT = 5000; // 5000



	private String mNickname;

	private Socket mSocket;

	private ClientReciver mReceiveThread;
	private String mReceiveMessage;

	private ChatMessageHandler mChatMessageHandler;
	private ChatMessageActivity.MessageHandler mMessageHandler;


	public ChatClient() {
	}

	public ChatClient(ChatMessageActivity.MessageHandler messageHandler) {
		this.mMessageHandler = messageHandler;
		mNickname = "배꽃그";
	}

	public ChatClient(ChatMessageHandler chatMessageHandler) {
		// this.mMessageHandler = messageHandler;
		this.mChatMessageHandler = chatMessageHandler;
		mNickname = "배꽃그";
	}


	public static void main(String[] args) {
		new ChatClient().connect();
	}
	
	public void connect() {
		try {

			mSocket = new Socket(SERVER_HOST, SERVER_PORT);

			mReceiveThread = new ClientReciver(mSocket, mNickname);
			mReceiveThread.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		mReceiveThread.sendMessage(message);
	}

	public String getReceiveMessage() {
		try {
			return mReceiveMessage;
		} catch (NullPointerException exception) {
			return null;
		}
	}

	public void stopChat() {
		System.out.println("id : " + mNickname + "접속 종료");
		System.exit(0);
	}



	class ClientReciver extends Thread {

		private DataInputStream mInputStream;
		private DataOutputStream mOutputStream;

		public ClientReciver(Socket socket, String nickName) {
			try {
				mInputStream = new DataInputStream(socket.getInputStream());
				mOutputStream = new DataOutputStream(socket.getOutputStream());

				/*
					Socket socket;
					try{
						socket= new Socket("192.168.111.111", 5555);//ip와 port번호 입력
						System.out.println("[client] server connect success");
					}catch(Exception io){
						System.out.println("[client] connect error!");
					}

					DataOutputStream dos;  // 데이터를 보낼 때 쓰는 스트림. 간단하게 통로정도로 생각하면 된다.
					DataInputStream dis;    // 데이터를 보낼 때 쓰는 스트림.

					try {
						dis = new DataInputStream(socket.getInputStream());    // 내가 데이터를 수신하기를 원하는 소켓의 수신용 스트림을 가져온다.
						dos = new DataOutputStream(socket.getOutputStream());  //  내가 데이터를 전송하기를 원하는 소켓의 전송용 스트림을 가져온다.

						while(true) {
							//수신부분
							byte[] in = new byte[100];
							dis.read(in, 0, in.length);
							String input = new String(in,0,in.length); //byte형을 string형으로 바꿔 초기화 한다.
							input = input.trim();   //trim()을 해주지 않으면 공백부분이 제대로 출력되지 않는다.

							//송신부분
							byte[] out = new byte[100];
							String s = "someStr";
							out = s.getBytes();  //string 형을 byte형으로 바꿔 대입한다.
							dos.write(out);
							dos.flush(); //이것은 꼭 해주자
						}
					}catch (IOException e) {
					}
				 */

				byte[] out;
				out = nickName.getBytes("UTF-8");
				out = addFlagToByteData(out, ChatConstants.CONNECT);

				mOutputStream.write(out);
				mOutputStream.flush();

				System.out.println("접속완료 : " + nickName);

				// mOutputStream.writeUTF(nickName);
				// mOutputStream.flush();
				// System.out.println("id : " + nickName + "접속 완료");


			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("writeUTF IOException");
			}
		}

		private byte[] addFlagToByteData(byte[] data, Integer flag) {
			Log.d(TAG, String.valueOf(flag));

			byte[] newData = new byte[data.length + 1];
			newData[0] = flag.byteValue();

			for (int i = 1; i < newData.length; i++) {
				newData[i] = data[i - 1];
			}

			return newData;
		}


		public void sendMessage(String message) {
			if (mOutputStream != null) {
				try {
					mOutputStream.writeUTF(message);
					mOutputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			try {
				// 계속 듣기만
				while (mInputStream != null) {
					/*
						mReceiveMessage = mInputStream.readUTF();
						System.out.println(mReceiveMessage);

						// Message msg = mMessageHandler.obtainMessage();
						// msg.what = GET_MESSAGE;
						// msg.obj = mReceiveMessage;

						// mMessageHandler.sendMessage(msg);

						Message msg = mChatMessageHandler.obtainMessage();
						msg.what = ChatConstants.GET_CONNECTED_CLIENT;
						msg.obj = mReceiveMessage;

						// 현재 스레드가 아닌 메인스레드에서 실행 시킴...?
						mChatMessageHandler.sendMessage(msg);
					*/

					byte[] in = new byte[100];
					mInputStream.read(in, 0, in.length); // buffer, byte offset, byte count

//					Integer flag = new Integer(in[0]);
//					System.out.println("서버에서 받은 flag : " + flag);
//
//					byte[] withoutFlag = new byte[in.length - 1];
//					for (int i = 1; i < in.length; i++) {
//						withoutFlag[i - 1] = in[i];
//					}

					ByteArrayInputStream bis = new ByteArrayInputStream(in);
					ObjectInput objIn = new ObjectInputStream(bis);
					Object obj = objIn.readObject();

					System.out.println(obj);

//					ObjectInputStream ois = new ObjectInputStream(mInputStream);
//					Object o = ois.readObject();
//					System.out.println("Received this object on the server: " + o);

				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				// 접속 종료시
				mSocket = null;
			}
		}
	}

}
