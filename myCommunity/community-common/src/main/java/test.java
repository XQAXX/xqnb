import lombok.Data;

import java.util.Timer;
import java.util.TimerTask;


public class test {
    public static void main(String[] args) {
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("我执行了"+"  "+System.currentTimeMillis());
            }
        },1000,2000);

    }
}
