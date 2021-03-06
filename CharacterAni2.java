package com.android.progBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


public class CharacterAni2 extends Activity {
    /** Called when the activity is first created. */
    			
	DBManager dManager;
	SQLiteDatabase db;
	private static final String DATABASE_NAME = "character.db";
	String d_name, d_university;
	TextView text;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        setContentView(R.layout.characterani2);        
        
        text = (TextView)findViewById(R.id.textView1);
        
        if (db == null) { 
        	db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		}
        
        dManager = new DBManager(this, "character.db", null, 1);        
        db.close();
        onOutput();
        
        text.setText(d_university+"���б�  "+d_name+"�� ����");
    }

    public void onNext(View v){
    	
    	startActivity(new Intent(CharacterAni2.this, CharacterAni3.class));
    	
    }
    
    public void onOutput(){     
    	
    	  db = dManager.getWritableDatabase();    	 
    	  String sql = "SELECT distinct name,university FROM character;";
    	  
    	  try{   
    		  
    	   Cursor cur = db.rawQuery(sql, null);
    	   
    	   while(cur.moveToNext()){

    		   //int num = cur.getInt(0);
    		   d_name = cur.getString(cur.getColumnIndex("name"));
    		   d_university = cur.getString(cur.getColumnIndex("university"));
    	   }
    	   
    	   
    	 }catch (SQLException se) {
    	   // TODO: handle exception    	  
    	 } 
          db.close();    
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	      
        return true;
       
    }
}
