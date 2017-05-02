package com.android.progBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Result extends Activity {
    /** Called when the activity is first created. */
  
	String rand_rid, rand_job, rand_final, d_pic;
	TextView text;
	ImageView final_pic;
	LinearLayout background;
	DBManager dManager;
	DBManager2 dManager2;
	DBManager3 dManager3;
	SQLiteDatabase db, db2, db3;
	private static final String DATABASE_NAME = "character.db";
	private static final String DATABASE_NAME2 = "calendar.db";
	private static final String DATABASE_NAME3 = "event.db";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        setContentView(R.layout.result);        
              
        if (db == null) { 
        	db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		}
        
        if (db2 == null) { 
            db2 = openOrCreateDatabase(DATABASE_NAME2, Context.MODE_PRIVATE, null);
        }
        
        if (db3 == null) { 
            db3 = openOrCreateDatabase(DATABASE_NAME3, Context.MODE_PRIVATE, null);
        }
        
        dManager = new DBManager(this, "character.db", null, 1);
        db = dManager.getWritableDatabase();
        db.close();        
        
        dManager2 = new DBManager2(this, "calendar.db", null, 1);
        db2 = dManager2.getWritableDatabase();
        db2.close();
        
        dManager3 = new DBManager3(this, "event.db", null, 1);
        db3 = dManager3.getWritableDatabase();
        db3.close();        
        
        text = (TextView)findViewById(R.id.textView1);
        final_pic = (ImageView)findViewById(R.id.finalpic);
        background = (LinearLayout)findViewById(R.id.finalback);
        
        Intent intent = getIntent();
        
        d_pic = intent.getStringExtra("pic");
        rand_rid = intent.getStringExtra("rid");
        rand_job = intent.getStringExtra("job");
        rand_final = intent.getStringExtra("final");
       
//메인에서 발생한 이벤트로 결말이 나오는데 지식과 미룬횟수에 따른 결과로 결말이 출력된다

        if(d_pic.equalsIgnoreCase("a")){ final_pic.setImageResource(R.drawable.result_ch1); } 
        else if(d_pic.equalsIgnoreCase("b")){ final_pic.setImageResource(R.drawable.result_ch2); }
        
        if(rand_rid.equalsIgnoreCase("a1") || rand_rid.equalsIgnoreCase("a3")){ //메인에서 저장한 변수와 그림id값을 비교후 해당하면 맞는 그림 뿌려줌
        			background.setBackgroundResource(R.drawable.school);
        }        
        else if(rand_rid.equalsIgnoreCase("a2") || rand_rid.equalsIgnoreCase("a4") ||
        		rand_rid.equalsIgnoreCase("a5") || rand_rid.equalsIgnoreCase("e5")){
        			background.setBackgroundResource(R.drawable.classroom);
        }
        else if(rand_rid.equalsIgnoreCase("a6") || rand_rid.equalsIgnoreCase("b5") ||
        		rand_rid.equalsIgnoreCase("d6")){
        			background.setBackgroundResource(R.drawable.oneroom);
        }
    	else if(rand_rid.equalsIgnoreCase("a7") || rand_rid.equalsIgnoreCase("b7") ||
    			rand_rid.equalsIgnoreCase("c7") || rand_rid.equalsIgnoreCase("d7") ||
    			rand_rid.equalsIgnoreCase("e7")){
    				background.setBackgroundResource(R.drawable.nojob);
		}
    	else if(rand_rid.equalsIgnoreCase("a8") || rand_rid.equalsIgnoreCase("b8") ||
    			rand_rid.equalsIgnoreCase("c8") || rand_rid.equalsIgnoreCase("d8") ||
    			rand_rid.equalsIgnoreCase("e8")){
    				background.setBackgroundResource(R.drawable.club);
    	}
    	else if(rand_rid.equalsIgnoreCase("a9") || rand_rid.equalsIgnoreCase("b9") ||
    			rand_rid.equalsIgnoreCase("c6") || rand_rid.equalsIgnoreCase("c9") ||
    			rand_rid.equalsIgnoreCase("d9") || rand_rid.equalsIgnoreCase("e9")){
    				background.setBackgroundResource(R.drawable.office);
    	}
    	else if(rand_rid.equalsIgnoreCase("a10") || rand_rid.equalsIgnoreCase("b10") ||
    			rand_rid.equalsIgnoreCase("c10") || rand_rid.equalsIgnoreCase("d10") ||
    			rand_rid.equalsIgnoreCase("e10")){
    				background.setBackgroundResource(R.drawable.cvs);
    	}
    	else if(rand_rid.equalsIgnoreCase("a11") || rand_rid.equalsIgnoreCase("b11") ||
    			rand_rid.equalsIgnoreCase("c11") || rand_rid.equalsIgnoreCase("d11") ||
    			rand_rid.equalsIgnoreCase("e11")){
    				background.setBackgroundResource(R.drawable.wedding);
    	}
    	else if(rand_rid.equalsIgnoreCase("b1") || rand_rid.equalsIgnoreCase("b3")){
    				background.setBackgroundResource(R.drawable.surgery);
    	}
    	else if(rand_rid.equalsIgnoreCase("b2") || rand_rid.equalsIgnoreCase("b4") ||
    			rand_rid.equalsIgnoreCase("c2") || rand_rid.equalsIgnoreCase("c4")){
    				background.setBackgroundResource(R.drawable.lab);
    	}
    	else if(rand_rid.equalsIgnoreCase("b6") ){
    				background.setBackgroundResource(R.drawable.harlem);
    	}
    	else if(rand_rid.equalsIgnoreCase("c1") || rand_rid.equalsIgnoreCase("c3") ||
    			rand_rid.equalsIgnoreCase("d1") || rand_rid.equalsIgnoreCase("d3")){
    				background.setBackgroundResource(R.drawable.comp);
    	}
    	else if(rand_rid.equalsIgnoreCase("c5") ){
    				background.setBackgroundResource(R.drawable.chicken);
    	}        
    	else if(rand_rid.equalsIgnoreCase("e1") || rand_rid.equalsIgnoreCase("e3")){
            		background.setBackgroundResource(R.drawable.red);
        }
        else if(rand_rid.equalsIgnoreCase("e2") || rand_rid.equalsIgnoreCase("e4")){
            		background.setBackgroundResource(R.drawable.ice);
        }        
        else if(rand_rid.equalsIgnoreCase("e6")){
            		background.setBackgroundResource(R.drawable.artstr);
        }   
        else if(rand_rid.equalsIgnoreCase("d2") || rand_rid.equalsIgnoreCase("d4")){
            background.setBackgroundResource(R.drawable.news);
        }
        else if(rand_rid.equalsIgnoreCase("d5")){
            background.setBackgroundResource(R.drawable.desk);
        }     
        
        text.setText("\n" + "졸업 후 너의 모습은....." + "'" + rand_job + "'" + 
        					"\n" + "\n" + rand_final + "\n");
        }
    
    public void onMenu(View v){  //돌아가기
    	
    	db = dManager.getWritableDatabase();
		db.delete("character", null, null);
		db.close();
						    		
		db2 = dManager2.getWritableDatabase();
		db2.delete("calendar", null, null);
		db2.close();
		
		db3 = dManager3.getWritableDatabase();
		db3.delete("event", null, null);				   
		db3.close();
		
    	startActivity(new Intent(Result.this, Menu.class));
    	
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	      
        return true;
       
    }
}