package ChatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat {

    int size=0;

    int port = 8080;//广播的目的端口

    private JTextField ip;

    private JTextArea message;

    private JTextArea edit;

    public void work() throws IOException {

        input();

        window();
//        System.out.println(size);

        Receive();
    }

    public void input() throws IOException {
        String dirname="ChatRoom.txt";
        InputStreamReader input = new InputStreamReader(new FileInputStream(dirname), StandardCharsets.UTF_8);
        while(input.ready()) {
            char ch = (char) input.read();
            if(ch=='\n'||ch=='\r')break;
            size=size*10+ch-'0';
        }
        input.close();
    }
    public void window() {
        JFrame chatroom = new JFrame("ChatRoom");

        chatroom.setLayout(new BorderLayout());

        chatroom.setBounds(650, 200, 800, 680);

        JPanel north = new JPanel();

        JPanel center = new JPanel();

        JPanel south = new JPanel();

        north.setPreferredSize(new Dimension(820,80));

        south.setPreferredSize(new Dimension(820,160));

        chatroom.add(north,BorderLayout.NORTH);

        chatroom.add(center);

        chatroom.add(south,BorderLayout.SOUTH);

        JLabel jl = new JLabel("IP");

        north.add(jl);

        ip = new JTextField("",15);

        north.add(ip);

        message = new JTextArea("",20,70);

        JScrollPane jp = new JScrollPane(message);

        center.add(jp);

        message.setEditable(false);

        edit = new JTextArea("",6,70);

        jp = new JScrollPane(edit);

        south.add(jp);

        JButton jb = new JButton("Send");

        message.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        jb.addActionListener(new Send());

        south.add(jb);

        chatroom.setVisible(true);

        chatroom.setDefaultCloseOperation(chatroom.EXIT_ON_CLOSE);
    }
    public String getLocalIp() throws UnknownHostException{
        return InetAddress.getLocalHost().getHostAddress();
    }
    class Send implements ActionListener {
        public void actionPerformed(ActionEvent arg0){
            try {
                String messageStr = getLocalIp().concat(" : \n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n").concat(edit.getText());//用于发送的字符串
//                String messageStr = edit.getText();//用于发送的字符串
                InetAddress adds = InetAddress.getByName(ip.getText());
                DatagramSocket ds = new DatagramSocket();
                DatagramPacket dp = new DatagramPacket(messageStr.getBytes(),messageStr.getBytes().length, adds, port);
                ds.send(dp);
                ds.close();
//                System.out.println(edit.getText());
                edit.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Receive() {
        DatagramSocket ds;
        DatagramPacket dp;
        while(true) {

            try {
                byte[] buf = new byte[size];

                ds = new DatagramSocket(port);

                dp = new DatagramPacket(buf, buf.length);

                ds.receive(dp);

                ds.close();

                DatagramPacket re = new DatagramPacket(buf,buf.length);

                re.setAddress(InetAddress.getByName(ip.getText()));

                String str = new String(re.getData(),0,re.getLength());

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                message.append(format.format(new Date()) + '\n' + str + "\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}