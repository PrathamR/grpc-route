package gash.grpc.route.client;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import route.Route;
import route.RouteServiceGrpc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * copyright 2018, gash
 *
 * Gash licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public class RouteClient {
	private static long clientID = 501;
	private static int port = 2345;

	public static void main(String[] args) throws IOException {
		ManagedChannel ch;

		RouteServiceGrpc.RouteServiceBlockingStub stub;

		BufferedReader br = new BufferedReader(new FileReader("resources/ips.txt"));
		String line = null;
		int i = 1;

		String[] ips = new String[4];

		if(args[0].equals("get_servers")) {
			System.out.println("Here's the list of servers");

			while ((line = br.readLine()) != null) {
				System.out.println(i + " " + line);
				ips[i] = line;
				i++;
			}
			return;
		} else if (args[0].equals("usage")) {
			System.out.println("sh runClient.sh get_servers - To get the list of servers");
			System.out.println("sh runClient.sh 2 hello - To send 'hello' to server with index 2");
			return;
		}

		while ((line = br.readLine()) != null) {
			ips[i] = line;
			i++;
		}

		int server = Integer.parseInt(args[0]);
		ch   = ManagedChannelBuilder.forAddress(ips[server], RouteClient.port).usePlaintext(true).build();
		stub = RouteServiceGrpc.newBlockingStub(ch);

		int I = 10;
		for (i = 0; i < I; i++) {
			Route.Builder bld = Route.newBuilder();
			bld.setId(i);
			bld.setOrigin(RouteClient.clientID);
			bld.setPath("/to/somewhere");

			byte[] hello = args[1].getBytes();
			bld.setPayload(ByteString.copyFrom(hello));

			// blocking!
			Route r = stub.request(bld.build());

			// TODO response handling
			String payload = new String(r.getPayload().toByteArray());
			System.out.println("reply: " + r.getId() + ", from: " + r.getOrigin() + ", payload: " + payload);
		}

		ch.shutdown();
	}
}
