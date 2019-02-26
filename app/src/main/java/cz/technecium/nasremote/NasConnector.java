package cz.technecium.nasremote;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

/**
 * Methods for communication with NAS over network.
 */
public class NasConnector {

    private static final String LOGIN_URL_PATTERN = "http://%s/r51009,/adv,/cgi-bin/weblogin.cgi";
    private static final String COMMAND_URL_PATTERN = "http://%s/r51009,/adv,/cgi-bin/zysh-cgi";

    /**
     * Turns NAS off by sending command via Zyxel Web interface.
     * <p>
     * Method is executed in new thread due to Android no network on main thread policy.
     *
     * @param ipAddress
     * @param username
     * @param password
     */
    public void turnOffNas(final String ipAddress, final String username, final String password) {
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(String.format(LOGIN_URL_PATTERN, ipAddress));
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                CloseableHttpResponse response = client.execute(httpPost);
                String cookie = null;
                for (Header header : response.getAllHeaders()) {
                    if (header.getName().equalsIgnoreCase("set-cookie")) {
                        cookie = header.getValue();
                        break;
                    }
                }

                client = HttpClients.createDefault();
                httpPost = new HttpPost(String.format(COMMAND_URL_PATTERN, ipAddress));
                httpPost.setHeader("Cookie", cookie);
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("write", "0"));
                params.add(new BasicNameValuePair("c0", "shutdown"));
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                client.execute(httpPost);
                client.close();
            }

        }).start();
    }

    /**
     * Turns NAS on by sending magic packet to NAS MAC address via broadcast IP.
     * For now, broadcast IP address is determined by replacing last part of IP with .255
     * <p>
     * Method is executed in new thread due to Android no network on main thread policy.
     *
     * @param nasIpAddres
     * @param nasMacAddress
     */
    public void turnOnNas(final String nasIpAddres, final String nasMacAddress) {
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                byte[] macBytes = getMacBytes(nasMacAddress);
                byte[] bytes = new byte[6 + 16 * macBytes.length];
                for (int i = 0; i < 6; i++) {
                    bytes[i] = (byte) 0xff;
                }
                for (int i = 6; i < bytes.length; i += macBytes.length) {
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
                }

                String ipPart = nasIpAddres.substring(0, nasIpAddres.lastIndexOf('.'));
                // TODO maso: this can be done better by using network mask from network interface
                String broadCastIp = ipPart + ".255";

                InetAddress address = InetAddress.getByName(broadCastIp);
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 9);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.close();
            }
        }).start();
    }

    private static byte[] getMacBytes(String macAddress) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macAddress.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
