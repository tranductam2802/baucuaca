package ntq.lbs.baucuaca;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    public static Integer[] deal = new Integer[6];
    private GridView gridView;
    private CustomGridView adapter;
    private AnimationDrawable animDiceOne, animDiceTwo, aminDiceThere;
    private ImageView imgDiceOne, imgDiceTwo, imgDiceThere;
    private TextView txtMoney, txtTime, txtBonus;
    private CheckBox cbxSound;

    private int valueDiceOne, valueDiceTwo, valueDiceThere;
    private int sumOld, sumNew;
    private int check, resSound;

    private Timer timer = new Timer();

    private SharedPreferences preferences;
    private SoundPool diceSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private CountDownTimer countDownTimer;

    private final String PRE_MONEY = "money";

    private final int COUNTDOWN_TIME = 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        // Assign view
        imgDiceOne = (ImageView) findViewById(R.id.xingau1);
        imgDiceTwo = (ImageView) findViewById(R.id.xingau2);
        imgDiceThere = (ImageView) findViewById(R.id.xingau3);
        txtMoney = (TextView) findViewById(R.id.tvTien);
        cbxSound = (CheckBox) findViewById(R.id.checkBox1);
        txtTime = (TextView) findViewById(R.id.tvTime);
        txtBonus = (TextView) findViewById(R.id.bonus);

        gridView = (GridView) findViewById(R.id.gvBanCo);
        adapter = new CustomGridView(this, R.layout.custom, Utility.getImageList());
        gridView.setAdapter(adapter);

        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        sumOld = preferences.getInt(PRE_MONEY, 1000);
        txtMoney.setText(String.valueOf(sumOld));

        resSound = diceSound.load(this, R.raw.dice, 1);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound_background);

        mediaPlayer.start();

        cbxSound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isOn) {
                if (isOn) {
                    mediaPlayer.stop();
                } else {
                    try {
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                txtTime.setText(String.format("%02d:%02d:%02d", hour, minute, second));
            }

            @Override
            public void onFinish() {
                Editor edit = preferences.edit();
                int plusMoney = 1000;
                sumOld = preferences.getInt(PRE_MONEY, plusMoney);
                sumNew = sumOld + plusMoney;
                edit.putInt(PRE_MONEY, sumNew);
                edit.commit();

                txtMoney.setText(String.valueOf(sumNew));
                countDownTimer.cancel();
                countDownTimer.start();
            }
        };

        countDownTimer.start();

        for (int i = 0; i < deal.length; i++) {
            deal[i] = 0;
        }
    }

    private void saveMoney(int money) {
        Editor edit = preferences.edit();
        sumOld = preferences.getInt(PRE_MONEY, 1000);
        sumNew = sumOld + money;
        edit.putInt(PRE_MONEY, sumNew);
        edit.commit();
    }

    public void roleDice(View v) {
        imgDiceOne.setImageResource(R.drawable.dice);
        imgDiceTwo.setImageResource(R.drawable.dice);
        imgDiceThere.setImageResource(R.drawable.dice);

        animDiceOne = (AnimationDrawable) imgDiceOne.getDrawable();
        animDiceTwo = (AnimationDrawable) imgDiceTwo.getDrawable();
        aminDiceThere = (AnimationDrawable) imgDiceThere.getDrawable();

        check = 0;
        for (int i = 0; i < deal.length; i++) {
            check += deal[i];
        }

        if (check == 0) {
            txtBonus.setText("Bạn vui lòng đặt cược ! ");
        } else {
            if (check > sumOld) {
                txtBonus.setText("Bạn không đủ tiền để đặt cược ! Chờ " + txtTime.getText() + " để lấy thêm tiền...");
            } else {
                diceSound.play(resSound, 1.0f, 1.0f, 1, 0, 1.0f);
                animDiceOne.start();
                animDiceTwo.start();
                aminDiceThere.start();
                timer.schedule(new RoleDice(callback), 1000);
            }
        }
    }

    Callback callback = new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            valueDiceOne = Utility.random(imgDiceOne);
            valueDiceTwo = Utility.random(imgDiceTwo);
            valueDiceThere = Utility.random(imgDiceThere);
            int money = 0;

            for (int i = 0; i < deal.length; i++) {
                if (deal[i] != 0) {
                    if (i == valueDiceOne)
                        money += deal[i];
                    if (i == valueDiceTwo)
                        money += deal[i];
                    if (i == valueDiceThere)
                        money += deal[i];
                    if (i != valueDiceOne && i != valueDiceTwo && i != valueDiceThere)
                        money -= deal[i];
                }
            }

            if (money > 0) {
                txtBonus.setText("Quá dữ bạn trúng được " + money);
            } else if (money == 0) {
                txtBonus.setText("Hên quá mém chết ! ");
            } else {
                txtBonus.setText("Ôi xui quá mất " + money + " rồi !");
            }

            saveMoney(money);
            txtMoney.setText(String.valueOf(sumNew));
            return false;
        }
    };
}