package com.sds.study.bluetootharduino;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lee on 2016-12-09.
 */

public class GameActivity extends AppCompatActivity{
    GameView gameView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.mainActivity.gameActivity=this;
        setContentView(R.layout.game_layout);
        gameView=(GameView)findViewById(R.id.gameView);
    }
}
