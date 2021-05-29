import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class Server {
    private static int record;
    public static void main(String[] args) throws IOException {
        InputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        BufferedWriter fw;

        ServerSocket ss;
        Scanner scn = new Scanner(System.in);
        System.out.println("Server start");

        File f = new File("score.txt");

        // 파일 존재 여부 판단
        if (!f.isFile()) {
            System.out.println("그런 파일 없습니다.");

            try {
                new FileWriter("score.txt", true);
                record = 0;
                System.out.println("파일 만듦.");
                FileOutputStream fos = new FileOutputStream("score.txt");
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("0");
                bw.close();
                osw.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //파일 있으면 읽고, 없으면 만들기

        ss = new ServerSocket(4949);
        Socket soc = ss.accept();
        //연결 완
        
        while(true) {
            try {
                fis = new FileInputStream("score.txt");
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);

                java.io.OutputStream out = soc.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                String best=br.readLine();
                dos.writeUTF(best);
                dos.flush();

                InputStream in = soc.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                String rec = dis.readUTF();
                System.out.println("received from client " + rec);

                if((Integer.parseInt(rec) > Integer.parseInt(best))){
                    System.out.println("new record");
                    FileOutputStream fos = new FileOutputStream("score.txt");
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write(rec);
                    bw.close();
                    osw.close();
                    fos.close();
                }
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                System.exit(0);
            } catch (IOException e) {
                //e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
