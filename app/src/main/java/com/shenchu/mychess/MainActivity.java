package com.shenchu.mychess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private Button btn_restart;
    private Button btn_regret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initUI();
    }

    private void initUI() {
        btn_restart = (Button) findViewById(R.id.btn_restart);
        btn_restart.setOnClickListener(this);
        btn_regret = (Button) findViewById(R.id.btn_regret);
        btn_regret.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_restart:
                GameManager.getInstance().removeAllPiece();
                break;
            case R.id.btn_regret:
                GameManager.getInstance().removeLastPiece();
                break;
        }
    }

    // 显示结束信息
    public void showWinner(int result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title = "";
        String msg = "";
        switch (result) {
            case GameRule.RESULT_WIN:// 玩家赢了
                title = mContext.getResources().getString(R.string.title_victor);
                msg = mContext.getResources().getString(R.string.text_victor);
                break;
            case GameRule.RESULT_LOSE:// 电脑赢了
                title = mContext.getResources().getString(R.string.title_lose);
                msg = mContext.getResources().getString(R.string.text_lose);
                break;
            case GameRule.RESULT_HAND_CUT_THREE:// 三三禁手
                title = mContext.getResources().getString(R.string.title_lose);
                msg = mContext.getResources().getString(R.string.text_handCut33);
                break;
            case GameRule.RESULT_HAND_CUT_FOUR:// 四四禁手
                title = mContext.getResources().getString(R.string.title_lose);
                msg = mContext.getResources().getString(R.string.text_handCut44);
                break;
            case GameRule.RESULT_HAND_CUT_LONG:// 长连禁手
                title = mContext.getResources().getString(R.string.title_lose);
                msg = mContext.getResources().getString(R.string.text_handCut66);
                break;
        }
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(
                mContext.getResources().getString(R.string.btn_restart),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameManager.getInstance().removeAllPiece();
                    }
                });
        builder.setNegativeButton(
                mContext.getResources().getString(R.string.btn_show), null);
        builder.show();
    }
}
