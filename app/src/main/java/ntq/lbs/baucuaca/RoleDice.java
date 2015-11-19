package ntq.lbs.baucuaca;

import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by PhuongQuynh on 19/11/2015.
 */
public class RoleDice extends TimerTask {
    private Handler handler;

    public RoleDice(Handler.Callback callback){
        handler = new Handler(callback);
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(0);
    }
}
