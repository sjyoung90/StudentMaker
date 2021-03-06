package com.android.progBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class GameInfo extends Activity {
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        setContentView(R.layout.gameinfo); 
    
    }
    
    public void onMenu(View v){ 
    	
    	Intent intent = new Intent(GameInfo.this, Menu.class); //HOME화면 이동
		intent.putExtra("end", true); // 다시넘어가면 SPLASH화면부터 보여주기때문에 안보여주기위해
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	      
        return true;
       
    }
    
}
