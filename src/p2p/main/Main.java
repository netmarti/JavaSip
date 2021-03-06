/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.main;

import java.net.InetAddress;
import javax.swing.JOptionPane;
import p2p.client.Frame;
import p2p.model.SipLayer;
import p2p.server.Server;

/**
 *
 * @author Jorge
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(-1);
        }
        // configura el host del servidor
        String ipServer = "192.168.0.12";
        //configura el puerto del servidor
        int portServer = 5061;
        try {
//            String username = args[0];
            String username = JOptionPane.showInputDialog("Username");
            String ip = InetAddress.getLocalHost().getHostAddress();
            if ("server".equalsIgnoreCase(username)) {
                SipLayer sipLayer = new SipLayer("j", "192.168.0.12", 5061);
                Server server = new Server(sipLayer);
                sipLayer.setMessageProcessor(server);
            } else {
                if (args.length != 2) {
                    printUsage();
                    System.exit(-2);
                }
//                int port = Integer.parseInt(args[1]);
                int port = Integer.parseInt(JOptionPane.showInputDialog("puerto"));
                SipLayer sipLayer = new SipLayer(username, ip, port);

                Frame tc = new Frame(sipLayer, ipServer, portServer);
                sipLayer.setMessageProcessor(tc);
                tc.show();
            }
        } catch (Throwable e) {

            System.out.println("Problem initializing the SIP stack.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void printUsage() {
        System.out.println("Syntax:");
        System.out.println("  java -jar textclient.jar <username> <port>");
        System.out.println("where <username> is the nickname of this user");
        System.out.println("<username> != server");
        System.out.println("and <port> is the port number to use. Usually 5060 if not used by another process.");
        System.out.println("<username> != server");
        System.out.println("Example:");
        System.out.println("  java -jar javasip.jar jorge1409 5062");
        System.out.println(" as server: ");
        System.out.println(" java -jar javasip.jar server");
    }
}
