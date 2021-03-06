package com.android.progBar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
    /** Called when the activity is first created. */
    	
	ProgressBar bar_knowledge, bar_love, bar_strength, bar_money;		// 지수값에 따른 프로그래스바
	DBManager dManager;
	DBManager2 dManager2;
	DBManager3 dManager3;
	DBManager4 dManager4;
	SQLiteDatabase db, db2, db3, db4;
	private static final String DATABASE_NAME = "character.db";
	private static final String DATABASE_NAME2 = "calendar.db";
	private static final String DATABASE_NAME3 = "event.db";
	private static final String DATABASE_NAME4 = "result.db";
	private static final int EXIT_DIALOG = 1;
	public static int count_month = 1;
	String d_pic, d_order, d_start, d_exam, d_knowledge, d_love, d_strength, d_money, d_stage, d_major;
	int d_month, d_delay, d_nonpayment, d_nnonpayment;

	TextView knowledge_value, love_value, strength_value, money_value;// TEXT로 보여줄 지수값
	TextView pic_calendar, yojung_Text;//요정이하는말
	int k_v, l_v, s_v, m_v, i = 0;	
	int d_cnt = 0, d_day = 1, e_day = 1, e_month = 1;
	String e_speak = null, e_eventOrder = null, e_yn = null, e_exe = null;
	String e_speak2 = null, e_eventOrder2 = null, e_exe2 = null, e2_exe2 = null;	 
	Button btn_schedule, btn_account, btn_info, yoNext;
	ArrayList<String> arr, arr2;
	int irand, rand, rand2;
	String gameOrder, a; // 딜레이랑 지식합쳐서 결론내린거
	String rand_rid, rand_job, rand_final;
	LinearLayout main_back;
	    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                       
        setContentView(R.layout.main);        
                
        main_back = (LinearLayout)findViewById(R.id.main_back);
        
        bar_knowledge = (ProgressBar)findViewById(R.id.bar_knowledge);
        bar_love = (ProgressBar)findViewById(R.id.bar_love);
        bar_strength = (ProgressBar)findViewById(R.id.bar_strength);
        bar_money = (ProgressBar)findViewById(R.id.bar_money); 
        
        bar_knowledge.setMax(100);
        bar_love.setMax(100);
        bar_strength.setMax(100);
        bar_money.setMax(200);
        
        pic_calendar = (TextView)findViewById(R.id.pic_calendar);
        btn_schedule = (Button)findViewById(R.id.btn_schedule);
        btn_account = (Button)findViewById(R.id.btn_account);
        btn_info = (Button)findViewById(R.id.btn_info);
        yoNext = (Button)findViewById(R.id.yoNext);
        
        knowledge_value = (TextView)findViewById(R.id.knowledge_value);
        love_value = (TextView)findViewById(R.id.love_value);
        strength_value = (TextView)findViewById(R.id.strength_value);
        money_value = (TextView)findViewById(R.id.money_value);
        yojung_Text = (TextView)findViewById(R.id.text_yo);
        
        arr = new ArrayList<String>();
        arr2 = new ArrayList<String>();
        
        rand = (int)((Math.random()*4));
        rand2 = (int)((Math.random()*4));
        
        if (db == null) { 
        	db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		}        
        if (db2 == null) { 
            db2 = openOrCreateDatabase(DATABASE_NAME2, Context.MODE_PRIVATE, null);
        }
        if (db3 == null) { 
            db3 = openOrCreateDatabase(DATABASE_NAME3, Context.MODE_PRIVATE, null);
        }
        if (db4 == null) { 
            db4 = openOrCreateDatabase(DATABASE_NAME4, Context.MODE_PRIVATE, null);
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
        
        dManager4 = new DBManager4(this, "result.db", null, 1);
        db4 = dManager4.getWritableDatabase();
        db4.close();
           
        getPic();	// 캐릭터사진
        if(d_pic.equalsIgnoreCase("a")){ 
        	main_back.setBackgroundResource(R.drawable.bg1);
        	 }
        else if(d_pic.equalsIgnoreCase("b")){   
        	main_back.setBackgroundResource(R.drawable.bg2);
        	 }
        /*     
        if(CharacterAni4.firstStart == true){
        	Toast.makeText(Main.this, "새로하기에서 넘어온 Main", Toast.LENGTH_SHORT).show();        	
        }
        else if(CharacterAni4.firstStart == false){
        	Toast.makeText(Main.this, "이어하기에서 넘어온 Main", Toast.LENGTH_SHORT).show();
        }*/        
        getOrder();  // 스케쥴순서
        getValue();
        
        if(Calendar.stageUp == true){ // 학년올라갈때 함수
        	
        	AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
			alertDlg.setTitle("새학기 시작");
			alertDlg.setMessage("\n"  + "학년이 올라갔습니다." + "\n" + "500만원 학자금 대출을 받았습니다." + "\n"
					+ "남은 대출금에서 500만원이 추가됩니다." + "\n");   
			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
     		
     			public void onClick(DialogInterface dialog, int which) {         					
     				Calendar.stageUp = false;
     				d_nnonpayment = d_nnonpayment + 500;
     				setPayment(d_nnonpayment);
     			}   
     		});
           		
			alertDlg.show();			
        }    
        
        getEvent(d_month,d_day);   // 현재날짜에 맞는 이벤트 가져오기(알림창)
        
        if(d_start.equalsIgnoreCase("1") && d_order.equalsIgnoreCase("0")){  // 메인에 날짜 써주는    
        	pic_calendar.setText((d_month + 1) + " / " + d_day);
        }
        else pic_calendar.setText(d_month + " / " + d_day);       
       
        getEvent2(d_day); //현재 날짜에 맞는 이벤트 가져오기(요정이 하는말)
        
        if(e_speak2 != null){ 
         
        	if(e_exe2.equalsIgnoreCase("2") && d_day != 16){    //스케줄 짜기전에 요정이하는말		
        		yojung_Text.setText(e_speak2);
        	} 
        	else if(e_exe2.equalsIgnoreCase("2") && d_day == 16){    //스케줄 짜고나서 요정이하는말       
        		if(e2_exe2.equalsIgnoreCase("0")){
        			yojung_Text.setText(arr.get(rand));
        			resetEvent3(e_eventOrder2); //모두다 1로  //중복을 제거하기위해서
        		}          
        		else if(e2_exe2.equalsIgnoreCase("1")){
        			yojung_Text.setText(arr2.get(rand2));
        			resetEvent2(e_eventOrder2); //모두다 0으로           
        		}          
        	} 
        }
                
        if(e_speak != null){        	
          
        	if(e_exe.equalsIgnoreCase("0")){        		// 알림창 이벤트
        	
        	//e6->e2
        	if(e_eventOrder.equals("e2") || e_eventOrder.equals("e10") || e_eventOrder.equals("e15")
        			|| e_eventOrder.equals("e16")|| e_eventOrder.equals("e17")){
        		
        		resetEvent(e_eventOrder);
        		
        		AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
    			alertDlg.setTitle("*** Event ***");
    			alertDlg.setMessage("\n"  + e_speak  + "\n");   
    			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
         		
         			public void onClick(DialogInterface dialog, int which) {         					
         				setEvent(e_eventOrder);         				
         			}   
         		});
          
    			alertDlg.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
         			public void onClick(DialogInterface dialog, int which) {
       
         				dialog.cancel();
         			}
         		});
         		
    			alertDlg.show();        		
        	}
        	
            else if(e_eventOrder.equals("a1")){
             resetEvent(e_eventOrder);
             yoNext.setVisibility(View.VISIBLE);
             yojung_Text.setText(e_speak);
             
             delete3(e_eventOrder);
             
            }
            
            else if(e_eventOrder.equals("a2")){
             resetEvent(e_eventOrder);
             delete3(e_eventOrder);
             
            }
                   	
        	else if(e_eventOrder.equals("e8") || e_eventOrder.equals("e14")){
        		
        		resetEvent(e_eventOrder);
        		
        		AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
    			alertDlg.setTitle("*** 시험기간 ***");
    			alertDlg.setMessage("\n"  + e_speak  + "\n");   
    			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
    				
    				public void onClick(DialogInterface dialog, int whichButton) {
    					startActivity(new Intent(Main.this, Exam.class));
    					reSetOrder();    					
    				}    
    			});
    			alertDlg.show();
        	}
        	
        	else if(e_eventOrder.equals("e4")){
        		      		
        		resetEvent(e_eventOrder);
        		
        		String speak = "";
        		if(d_exam.equalsIgnoreCase("AA")){
        			speak = "장학금 지급일입니다." + "\n" + "당신의 성적은 " + d_exam + "입니다." + "\n" +
        					"300만원의 장학금이 지급됩니다." + "\n" + "다음 학기도 열심히 공부하세요!";
        		}
        		else if(d_exam.equalsIgnoreCase("AB") || d_exam.equalsIgnoreCase("BA")){
        			speak = "장학금 지급일입니다." + "\n" + "당신의 성적은 " + d_exam + "입니다." + "\n" +
        					"200만원의 장학금이 지급됩니다." + "\n" + "다음 학기도 열심히 공부하세요!";
        		}        		
        		else{
        			speak = "장학금 지급일입니다." + "\n" + "당신의 성적은 " + d_exam + "입니다." + "\n" +
        					"성적이 낮아 장학금이 지급되지 않습니다. " + "\n" + "다음 학기는 장학금을 받을 수 있도록 열심히 공부하세요!";
        		}
        		
    			AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
    			alertDlg.setTitle("*** Event ***");
    			alertDlg.setMessage("\n"  + speak  + "\n");   
    			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
    				
    				public void onClick(DialogInterface dialog, int whichButton) {
    					 	
    						setEvent(e_eventOrder);        					 	
    		        	    getValue(); resetExam();
    						bar_knowledge.setProgress(k_v);
    						bar_love.setProgress(l_v);
    						bar_strength.setProgress(s_v);
    						bar_money.setProgress(m_v);
    						
    						knowledge_value.setText(d_knowledge + "/100");
    						love_value.setText(d_love + "/100");
    						strength_value.setText(d_strength + "/100");
    						
    						money_value.setText(d_money + "만원");
     	   
    				}    
    			});
    			alertDlg.show();        		
        	}
        	        	
        	else if(e_eventOrder.equals("e1") || e_eventOrder.equals("e3")
        			|| e_eventOrder.equals("e5")||e_eventOrder.equals("e6") || e_eventOrder.equals("e7") 
        			|| e_eventOrder.equals("e9")||e_eventOrder.equals("e11") 
        			|| e_eventOrder.equals("e12") || e_eventOrder.equals("e13")
        			|| e_eventOrder.equals("e33")|| e_eventOrder.equals("e34")
        			|| e_eventOrder.equals("e37")|| e_eventOrder.equals("e39")|| e_eventOrder.equals("e311")){
        		
        			resetEvent(e_eventOrder);
        		
        			AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
        			alertDlg.setTitle("*** Event ***");
        			alertDlg.setMessage("\n"  + e_speak  + "\n");   
        			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
        				
        				public void onClick(DialogInterface dialog, int whichButton) {
        					 	
        						setEvent(e_eventOrder);        					 	
        		        	    getValue();
        						bar_knowledge.setProgress(k_v);
        						bar_love.setProgress(l_v);
        						bar_strength.setProgress(s_v);
        						bar_money.setProgress(m_v);
        						
        						knowledge_value.setText(d_knowledge + "/100");
        						love_value.setText(d_love + "/100");
        						strength_value.setText(d_strength + "/100");
        						
        						money_value.setText(d_money + "만원");
         	   
        				}    
        			});
        			alertDlg.show() ;
        	}
        	
			//대출금 상환
        	else if(e_eventOrder.equals("e171") || e_eventOrder.equals("e172") || e_eventOrder.equals("e173")
        			|| e_eventOrder.equals("e174")|| e_eventOrder.equals("e175")
        			||e_eventOrder.equals("e176") || e_eventOrder.equals("e177") || e_eventOrder.equals("e178")
        			|| e_eventOrder.equals("e179")|| e_eventOrder.equals("e1710")
        			|| e_eventOrder.equals("e1711")|| e_eventOrder.equals("e1712")){
        		
        		resetEvent(e_eventOrder);
        		
        		if(m_v>=40){
        		
        		
        		AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
    			alertDlg.setTitle("*** Event ***");
    			alertDlg.setMessage("\n"  + e_speak  + "\n");   
    			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
         		
         			public void onClick(DialogInterface dialog, int which) { 
         				setEvent(e_eventOrder);         				
         			}   
         		});
          
    			
    			alertDlg.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
         			public void onClick(DialogInterface dialog, int which) {
       
         				
         				AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(Main.this) ;   
         				alertDlg2.setTitle("*** Event ***");
         				alertDlg2.setMessage("\n"  + "다음달로 상환금이 이월됩니다."  + "\n");   
         				alertDlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
         	   		
         	   			public void onClick(DialogInterface dialog, int which) {    
         	   				         	   			
         	   			d_delay++;
         	   			d_nonpayment = d_nonpayment + 40;
         	   			
         	   			setValue(m_v, s_v, l_v, k_v, d_delay,d_nonpayment,d_nnonpayment);
         	   			getValue();        
         	   			
         	           bar_knowledge.setProgress(k_v);
         	           bar_love.setProgress(l_v);
         	           bar_strength.setProgress(s_v);
         	           bar_money.setProgress(m_v);
         	           
         	           knowledge_value.setText(d_knowledge + "/100");
         	           love_value.setText(d_love + "/100");
         	           strength_value.setText(d_strength + "/100");
         	           money_value.setText(d_money + "만원");
         	   			 
         	   				    		    	
         	   			}   
         	   		});
         				alertDlg2.show();
         				
         			}
         		});
         		
    			alertDlg.show();
        		
        	}
        		
        		
        		else if(m_v<40){
        			
        			AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(Main.this) ;   
     				alertDlg2.setTitle("*** Event ***");
     				alertDlg2.setMessage("\n"  + "매월 4일은 학자금 대출금 상환일이지만,"
     									+ "\n"+"돈이 모자라서 다음달로 상환금이 이월됩니다."  + "\n");   
     				alertDlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
     	   		
     	   			public void onClick(DialogInterface dialog, int which) {    
     	   				
     	   				
     	   			d_delay++;
     	   			d_nonpayment = d_nonpayment + 40;
     	   			
     	   			setValue(m_v, s_v, l_v, k_v, d_delay,d_nonpayment,d_nnonpayment);
     	   			getValue();        
     	   			
     	           bar_knowledge.setProgress(k_v);
     	           bar_love.setProgress(l_v);
     	           bar_strength.setProgress(s_v);
     	           bar_money.setProgress(m_v);
     	           
     	           knowledge_value.setText(d_knowledge + "/100");
     	           love_value.setText(d_love + "/100");
     	           strength_value.setText(d_strength + "/100");
     	           money_value.setText(d_money + "만원");
     	   			 
     	   				    		    	
     	   			}   
     	   		});
     				alertDlg2.show();
        			
        			}
        		}  
        	
			//추가납부
        	if(e_eventOrder.equals("e181") || e_eventOrder.equals("e182") 
            		|| e_eventOrder.equals("e183")){
            	
        		resetEvent(e_eventOrder);
        		
            	AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
    			alertDlg.setTitle("*** Event ***");
    			alertDlg.setMessage("\n"  + e_speak  + "\n");   
    			alertDlg.setPositiveButton("네", new DialogInterface.OnClickListener() {
         		
         			public void onClick(DialogInterface dialog, int which) {
         				
         				Intent intent = new Intent(Main.this, Account.class);
         				intent.putExtra("event", true);
         	            startActivity(intent);  
                 	
         			}   
         		});
          
    			alertDlg.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
         			public void onClick(DialogInterface dialog, int which) {
       
         				dialog.cancel();
         			}
         		});
         		
    			alertDlg.show();
            		
        	}  
        	
        	}
        }		
        	
        getValue();        
        bar_knowledge.setProgress(k_v);
        bar_love.setProgress(l_v);
        bar_strength.setProgress(s_v);
        bar_money.setProgress(m_v);
        
        knowledge_value.setText(d_knowledge + "/100");
        love_value.setText(d_love + "/100");
        strength_value.setText(d_strength + "/100");
        money_value.setText(d_money + "만원");        	 
        	 
        char str = d_order.charAt(d_cnt);
    	if(str == '0'){ btn_schedule.setClickable(true); }     	
    	    	
    	if(d_stage.equals("4") && d_month == 2 && d_day == 10){ //졸업일때 조건
    	  
    	  getValue();
    	 
    	  if(k_v >= 90) a = "A"; 
          else a = "B";
      	   
      	  if(d_delay <= 10) a += "A";
      	  else a += "B";  
      	   
    	  getLast(a, d_major);
    	  setLast();
    	  
    	}
    }    
	
	public void onYoNext(View v){ //처음시작할때 요정이하는말2
		  
		  yojung_Text.setText("달력으로 가서 2주동안의 스케쥴을 정해봐. 공부는 한번 이상 넣도록 해." +
		  		"그렇지 않으면 시험을 망치게 될거야. 너무 무리하게 일만 하지말고 휴식시간도 가져." +
		  		"그럼 이제 스케쥴을 정하러가자!");
		  yoNext.setVisibility(View.GONE);
	}
    
    public void onButton2(View v){  //db에서 받아온 순서에 따라게임실행  	
    	executeGame(d_order);
    }
    
    public void setMonth(int m){
        
        if(m == 12){
         m = 1;
        }
        else m++;
        
        String sql = "";     
        db2 = dManager2.getWritableDatabase();      
        
        sql = "update calendar set month = '" + m + "';";      
       
        db2.execSQL(sql);
        db2.close();
        
       }
    
    public void setPayment(int d){
    	
    	db = dManager.getWritableDatabase();      
        
    	String sql = ""; 
        sql = "update character set nnonpayment = '" + d + "';";      
       
        db.execSQL(sql);
        db.close();    	
    	
    }       
    
    public void getLast(String a, String d_major){
        
        String[] r_rid_arr;
        String[] r_job_arr;
        String[] r_final_arr;
        int k = 0;
        
        if(a.equals("BB")){ //나쁜결과
        	r_rid_arr = new String[5];
        	r_job_arr = new String[5];
        	r_final_arr = new String[5];
        }else{ //좋은결과
        	r_rid_arr = new String[2];
        	r_job_arr = new String[2];
        	r_final_arr = new String[2];
        }
        
        db4 = dManager4.getWritableDatabase();   
        
        String sql = "SELECT rid, job, final FROM result WHERE major = '" + d_major + "' and point = '" + a + "' ;";
            
        try{   
            
             Cursor cur = db4.rawQuery(sql, null);
                   
         while(cur.moveToNext()){
              r_rid_arr[k] = cur.getString(cur.getColumnIndex("rid")); 
              r_job_arr[k] = cur.getString(cur.getColumnIndex("job")); 
              r_final_arr[k]= cur.getString(cur.getColumnIndex("final"));
                 
              k++;
         }
         
           }catch (SQLException se) {
             // TODO: handle exception       
           }   
               db4.close();            


              double rand = Math.random();
              if(a.equals("BB")) irand = (int) (rand * 4); //6개중에 랜덤으로 나옴
              else irand = (int) (rand * 2);
     
              rand_rid = r_rid_arr[irand]; //랜덤으로 선택된 결말이 저장되는 변수
              rand_job = r_job_arr[irand];
              rand_final = r_final_arr[irand];
        
       }
    
       public void setLast(){ //해당결과에 맞는 그림을 result액티비티로 넘겨줌
        
        Intent intent =new Intent(new Intent(Main.this, Result.class));
        intent.putExtra("pic", d_pic);
        intent.putExtra("rid", rand_rid);
        intent.putExtra("job", rand_job);
        intent.putExtra("final", rand_final);
        startActivity(intent);
        
       }
    
  public void getEvent(int month,int day){
    	
    	db3 = dManager3.getWritableDatabase();   
    	
    	String sql = "SELECT event_order, month, day, speak, exe FROM event WHERE month = '" + month + "' and day = '" + day + "' ;";
         try{   
          
          Cursor cur = db3.rawQuery(sql, null);
          
          while(cur.moveToNext()){
           e_eventOrder = cur.getString(cur.getColumnIndex("event_order")); 
           e_month = cur.getInt(cur.getColumnIndex("month")); 
           e_day = cur.getInt(cur.getColumnIndex("day"));
           e_speak = cur.getString(cur.getColumnIndex("speak")); 
           e_exe = cur.getString(cur.getColumnIndex("exe")); 
          
          }        
          
        }catch (SQLException se) {
          // TODO: handle exception       
        }   
            db3.close();       	
    }
  
  	public void getEvent2(int day){
	    
	    db3 = dManager3.getWritableDatabase();   
	    int i = 0;      
	    
	    String sql = "SELECT event_order,speak,exe,exe2 FROM event WHERE day = '" + day + "'and exe2 = '" + 2 + "';";
	        try{   
	         
	         Cursor cur = db3.rawQuery(sql, null);
	         
	         while(cur.moveToNext()){
	          
	        	 e_eventOrder2 = cur.getString(cur.getColumnIndex("event_order")); 
	        	 e_speak2 = cur.getString(cur.getColumnIndex("speak")); 
	        	 e2_exe2 = cur.getString(cur.getColumnIndex("exe")); 
	        	 e_exe2 = cur.getString(cur.getColumnIndex("exe2")); 	        
	          
	          if(e_eventOrder2.startsWith("d")){
	        	  arr.add(cur.getString(cur.getColumnIndex("speak")));  
	           }
	          else if(e_eventOrder2.startsWith("f")){
	        	  arr2.add(cur.getString(cur.getColumnIndex("speak")));  
	           }
	         }
	         
	       }catch (SQLException se) {
	         // TODO: handle exception       
	       }   
	           db3.close();    

	   }
  
  	public void resetEvent(String s){  		
  		db3 = dManager3.getWritableDatabase();
		String sql = "update event set exe = '1' where event_order = '" + s + "';";				    		
		db3.execSQL(sql);
		db3.close();		
  	}
  	
  	public void resetEvent2(String s){  	    
  	  db3 = dManager3.getWritableDatabase();
  	  
  	  String sql1 = "update event set exe = '0' where event_order = 'd1';";
  	  String sql2 = "update event set exe = '0' where event_order = 'd2';";
  	  String sql3 = "update event set exe = '0' where event_order = 'd3';";
  	  String sql4 = "update event set exe = '0' where event_order = 'd4';";
  	  
  	  String sql5 = "update event set exe = '0' where event_order = 'f1';";
  	  String sql6 = "update event set exe = '0' where event_order = 'f2';";
  	  String sql7 = "update event set exe = '0' where event_order = 'f3';";
  	  String sql8 = "update event set exe = '0' where event_order = 'f4';";
  	  
  	  db3.execSQL(sql1);
  	  db3.execSQL(sql2);
  	  db3.execSQL(sql3);
  	  db3.execSQL(sql4);
  	  db3.execSQL(sql5);
  	  db3.execSQL(sql6);
  	  db3.execSQL(sql7);
  	  db3.execSQL(sql8);
  	  
  	  db3.close();
  	  
  	   }  	 
  	 
  	 
  	 public void resetEvent3(String s){  	    
  	  db3 = dManager3.getWritableDatabase();
  	  
  	  String sql1 = "update event set exe = '1' where event_order = 'd1';";
  	  String sql2 = "update event set exe = '1' where event_order = 'd2';";
  	  String sql3 = "update event set exe = '1' where event_order = 'd3';";
  	  String sql4 = "update event set exe = '1' where event_order = 'd4';";
  	  
  	  String sql5 = "update event set exe = '1' where event_order = 'f1';";
  	  String sql6 = "update event set exe = '1' where event_order = 'f2';";
  	  String sql7 = "update event set exe = '1' where event_order = 'f3';";
  	  String sql8 = "update event set exe = '1' where event_order = 'f4';";
  	  
  	  db3.execSQL(sql1);
  	  db3.execSQL(sql2);
  	  db3.execSQL(sql3);
  	  db3.execSQL(sql4);
  	  db3.execSQL(sql5);
  	  db3.execSQL(sql6);
  	  db3.execSQL(sql7);
  	  db3.execSQL(sql8);
  	  
  	  db3.close();
  	  
  	   } 	 
  	 
  	 public void delete3(String s){
  	  
  	  
  	  db3 = dManager3.getWritableDatabase();
  	  
  	  String sql2 = "DELETE FROM event WHERE event_order = '" + s + "';";
  	 
  	     db3.execSQL(sql2);
  	     db3.close();
  	  
  	 }
  	
  	public void resetExam(){  		
  		db = dManager.getWritableDatabase();
		String sql = "update character set exam = '0';";				    		
		db.execSQL(sql);
		db.close(); 		  		
  	}
  
  public void setEvent(String e){
	  
	  String e2 = "e2";
	  String e3 = "e3";
	  String e33 = "e33";
	  String e34 = "e34";
	  String e37 = "e37";
	  String e39 = "e39";
	  String e311 = "e311";	  
	  String e4 = "e4";
	  String e5 = "e5";
	  String e6 = "e6";
	  String e7 = "e7";
	  String e8 = "e8";
	  String e9 = "e9";
	  String e10 = "e10";
	  String e11 = "e11";
	  String e12 = "e12";
	  String e13 = "e13";
	  String e14 = "e14";
	  String e15 = "e15";
	  String e16 = "e16";
	  String e171 = "e171";	 
	  
		  if(e.equals(e2)){ //겨울휴가 
		  
		  	AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
			alertDlg.setTitle("*** Event ***");
			alertDlg.setMessage("\n"  + "스키장으로 떠납니다."+ "\n" + " 휴가비용으로 10만원이 차감되고 체력이 +50 되었습니다."   + "\n");   
			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
 		
 			public void onClick(DialogInterface dialog, int which) { 
 				
 				s_v = s_v + 50; 				
 				if(s_v >= 100) s_v = 100;
 				m_v = m_v - 10;
 				setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment,d_nnonpayment);
 				getValue();
 				
				bar_knowledge.setProgress(k_v);
				bar_love.setProgress(l_v);
				bar_strength.setProgress(s_v);
				bar_money.setProgress(m_v);
				
				knowledge_value.setText(d_knowledge + "/100");
				love_value.setText(d_love + "/100");
				strength_value.setText(d_strength + "/100");
				money_value.setText(d_money + "만원"); 			 
 				    		    	
 			}   
 		});
			alertDlg.show() ;
	  }
	  else if(e.equals(e3)){ //토익
		  m_v = m_v - 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e33)){ //토익
		  m_v = m_v - 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e34)){ //토익
		  m_v = m_v - 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e37)){ //토익
		  m_v = m_v - 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e39)){ //토익
		  m_v = m_v - 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e311)){ //토익
		  m_v = m_v - 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e4)){ //장학금(성적에 따라서 장학금 지급)
			
		  getValue();		  
		  if(d_exam.equalsIgnoreCase("AA")){	//300만원
			  m_v = m_v + 200;
			  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);		  
		  }
		  else if(d_exam.equalsIgnoreCase("AB") || d_exam.equalsIgnoreCase("AB")){	//200만원
			  m_v = m_v + 100;
			  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
		  }		  
		  		  		  
	  }
	  else if(e.equals(e5)){ //설날
		  m_v = m_v + 10;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
	  }
	 
	  else if(e.equals(e6)){ //생일
		  
   				m_v = m_v + 10;
   				setValue(m_v, s_v, l_v, k_v, d_delay,d_nonpayment, d_nnonpayment);
   				getValue();
   				
				bar_knowledge.setProgress(k_v);
				bar_love.setProgress(l_v);
				bar_strength.setProgress(s_v);
				bar_money.setProgress(m_v);
				
				knowledge_value.setText(d_knowledge + "/100");
				love_value.setText(d_love + "/100");
				strength_value.setText(d_strength + "/100");
				money_value.setText(d_money + "만원");
   			 
		  }
	  
	  else if(e.equals(e7)){ //어버이날
		  m_v = m_v - 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay,d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e8)){ //중간고사
		  
	  }
	  else if(e.equals(e9)){ //공모전
		  k_v = k_v + 5;
		  if(k_v >= 100) k_v = 100;
		  setValue(m_v, s_v, l_v, k_v, d_delay,d_nonpayment, d_nnonpayment);
	  }
	  else if(e.equals(e10)){ //여름휴가
		  
		  	AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
			alertDlg.setTitle("*** Event ***");
			alertDlg.setMessage("\n"  + "해수욕장으로 떠납니다."+ "\n" + "휴가비용으로 10만원이 차감되고 체력이 +50 되었습니다."   + "\n");   
			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {  
				
				m_v = m_v - 10;
				s_v = s_v + 50;
				if(s_v >= 100) s_v = 100;
				
				setValue(m_v, s_v, l_v, k_v, d_delay,d_nonpayment, d_nnonpayment);
				getValue();
				
				bar_knowledge.setProgress(k_v);
				bar_love.setProgress(l_v);
				bar_strength.setProgress(s_v);
				bar_money.setProgress(m_v);
				
				knowledge_value.setText(d_knowledge + "/100");
				love_value.setText(d_love + "/100");
				strength_value.setText(d_strength + "/100");
				money_value.setText(d_money + "만원");			 
				    		    	
			}   
		});
			alertDlg.show() ;
		  
		 
	  }
	  else if(e.equals(e11)){ //추석
		  m_v = m_v + 10;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);		  
	  }
	  else if(e.equals(e12)){ //직원의날
		  m_v = m_v + 5;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);	  }
	  else if(e.equals(e13)){ //용돈
		  m_v = m_v + 10;
		  setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);	  }
	  else if(e.equals(e14)){ //기말
		  
	  }
	  else if(e.equals(e15)){ //구세군
		  
		  	AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
			alertDlg.setTitle("*** Event ***");
			alertDlg.setMessage("\n"  + "구세군 모금함에 5만원을 기부하였습니다."   + "\n");   
			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {    
				
				
				m_v = m_v - 5;
				
				setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
				getValue();
				
				bar_knowledge.setProgress(k_v);
				bar_love.setProgress(l_v);
				bar_strength.setProgress(s_v);
				bar_money.setProgress(m_v);
				
				knowledge_value.setText(d_knowledge + "/100");
				love_value.setText(d_love + "/100");
				strength_value.setText(d_strength + "/100");
				money_value.setText(d_money + "만원");			 
				    		    	
			}   
		});
			alertDlg.show() ;
		  
	  }
	  else if(e.equals(e16)){ //크리스마스
		  
		  	AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
			alertDlg.setTitle("*** Event ***");
			alertDlg.setMessage("\n"  + "파티비용으로 10만원이 차감되고 애정지수가 +5되었습니다."   + "\n");   
			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {  			
				
				m_v = m_v - 10;
				l_v = l_v + 5;
				if(l_v >= 100) l_v = 100;
				
				setValue(m_v, s_v, l_v, k_v, d_delay, d_nonpayment, d_nnonpayment);
				getValue();
				
				bar_knowledge.setProgress(k_v);
				bar_love.setProgress(l_v);
				bar_strength.setProgress(s_v);
				bar_money.setProgress(m_v);
				
				knowledge_value.setText(d_knowledge + "/100");
				love_value.setText(d_love + "/100");
				strength_value.setText(d_strength + "/100");
				money_value.setText(d_money + "만원");			 
				    		    	
			}   
		});
			alertDlg.show() ;
		 
	  }	  
		  
	  else if(e_eventOrder.equals("e171") || e_eventOrder.equals("e172") || e_eventOrder.equals("e173")
  			|| e_eventOrder.equals("e174")|| e_eventOrder.equals("e175")
  			|| e_eventOrder.equals("e176") || e_eventOrder.equals("e177") || e_eventOrder.equals("e178")
  			|| e_eventOrder.equals("e179")|| e_eventOrder.equals("e1710")
  			|| e_eventOrder.equals("e1711")|| e_eventOrder.equals("e1712")){ //등록금미루기
		  
		
		  AlertDialog.Builder alertDlg = new AlertDialog.Builder(Main.this) ;   
			alertDlg.setTitle("*** Event ***");
			alertDlg.setMessage("\n"  + "통장에서 40만원이 차감됩니다."   + "\n");   
			alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
		
			public void onClick(DialogInterface dialog, int which) {			
				
				m_v = m_v - 40;
				
				d_nnonpayment = d_nnonpayment - 40;
				setValue(m_v, s_v, l_v, k_v, d_delay,d_nonpayment, d_nnonpayment);
				getValue();
				
				bar_knowledge.setProgress(k_v);
				bar_love.setProgress(l_v);
				bar_strength.setProgress(s_v);
				bar_money.setProgress(m_v);
				
				knowledge_value.setText(d_knowledge + "/100");
				love_value.setText(d_love + "/100");
				strength_value.setText(d_strength + "/100");
				money_value.setText(d_money + "만원");			 
				    		    	
			}   
		});
			alertDlg.show() ;
	  }
	 
	 // setValue(m_v, s_v, l_v, k_v);		  
	   
  }
    
   
    
 public void setValue(int m, int s, int l, int k, int d, int n,int nn){
        
        db = dManager.getWritableDatabase();
        //'"+ s_major +"'
        String sql = null, sql2 = null, sql3 = null, sql4 = null, sql5 = null, sql6 = null, sql7 = null;
                
        if(s < 0){s = 0;}
        
        String m_v2 = String.valueOf(m);
        String s_v2 = String.valueOf(s);
        String l_v2 = String.valueOf(l);
        String k_v2 = String.valueOf(k);
          
        sql = "update character set money='"+ m_v2 +"';";
        sql2 = "update character set strength='"+ s_v2 +"';";
        sql3 = "update character set knowledge='"+ k_v2 +"';";
        sql4 = "update character set love='"+ l_v2 +"';";
        sql5 = "update character set delay='"+ d +"';";
        sql6 = "update character set nonpayment='"+ n +"';";
        sql7 = "update character set nnonpayment='"+ nn +"';";
          
         db.execSQL(sql);
         db.execSQL(sql2);
         db.execSQL(sql3);
         db.execSQL(sql4);
         db.execSQL(sql5);
         db.execSQL(sql6);
         db.execSQL(sql7);
         
         db.close();
       }
    
    
    public void getValue(){
    	
    	  db = dManager.getWritableDatabase();   
    	  String sql = "SELECT distinct knowledge, love, strength, money, delay, nonpayment, nnonpayment, exam, stage, major FROM character;";
      	  try{   
      		  
      	   Cursor cur = db.rawQuery(sql, null);
      	   
      	   while(cur.moveToNext()){
      	   
      		   d_knowledge = cur.getString(cur.getColumnIndex("knowledge"));
      		   d_love = cur.getString(cur.getColumnIndex("love"));
      		   d_strength = cur.getString(cur.getColumnIndex("strength"));
      		   d_money = cur.getString(cur.getColumnIndex("money"));
      		   d_delay = cur.getInt(cur.getColumnIndex("delay"));
      		   d_nonpayment = cur.getInt(cur.getColumnIndex("nonpayment"));
      		   d_nnonpayment = cur.getInt(cur.getColumnIndex("nnonpayment"));
      		   d_exam = cur.getString(cur.getColumnIndex("exam"));
      		   d_stage = cur.getString(cur.getColumnIndex("stage"));
      		   d_major = cur.getString(cur.getColumnIndex("major")); 
      		     		  
      	   }    	 
      	   
      	 }catch (SQLException se) {
      	   // TODO: handle exception    	  
      	 }   
            db.close();
            
        k_v = Integer.parseInt(d_knowledge);
        l_v = Integer.parseInt(d_love);
        s_v = Integer.parseInt(d_strength);
        m_v = Integer.parseInt(d_money);
         	   
    }
   
    public void getPic(){     
  	  db = dManager.getWritableDatabase();   
  	  String sql = "SELECT distinct pic FROM character;";
  	  
  	  try{   
  		  
  	   Cursor cur = db.rawQuery(sql, null);
  	   
  	   while(cur.moveToNext()){
  	   
  		   d_pic = cur.getString(cur.getColumnIndex("pic"));
  		     		  
  	   }    	   
  	   
  	 }catch (SQLException se) {
  	   // TODO: handle exception    	  
  	 }   
        db.close();    
  }
    
   
    
  public void getOrder(){
	  
	  db2 = dManager2.getWritableDatabase();   
  	  String sql = "SELECT distinct start, month, day, order1, ordercnt FROM calendar;";
  	  
  	  try{   
  		  
  	   Cursor cur = db2.rawQuery(sql, null);
  	   
  	   while(cur.moveToNext()){
  	   
  		   d_order = cur.getString(cur.getColumnIndex("order1"));
  		   d_cnt = cur.getInt(cur.getColumnIndex("ordercnt"));
  		   d_start = cur.getString(cur.getColumnIndex("start"));
		   d_month = cur.getInt(cur.getColumnIndex("month"));
		   d_day = cur.getInt(cur.getColumnIndex("day")); 
  		     		  
  	   }    	   
  	   
  	 }catch (SQLException se) {
  	   // TODO: handle exception    	  
  	 }   
        db2.close();    
  }
  
  public void reSetOrder(){
	  
	  String sql = "", sql2 = "";
	  db2 = dManager2.getWritableDatabase();    	
	   		
	  sql = "update calendar set order1 = '0';";
	  sql2 = "update calendar set ordercnt = '0';";
			 
	  db2.execSQL(sql);
	  db2.execSQL(sql2);
	  
	  db2.close();
  }
  
  public void setOrdercnt(int i){ //
	  
	  String sql = "";
	  db2 = dManager2.getWritableDatabase();    	
 		
	  sql = "update calendar set ordercnt = '" + i + "';";
	  db2.execSQL(sql);
	  db2.close();
  }
    
    public void executeGame(String order){
    	
    	char str = order.charAt(d_cnt);
    	
    	
    	if(str == '0'){    		
    		Toast.makeText(Main.this, "달력으로 가서 일정을 정해주세요", Toast.LENGTH_SHORT).show();
    		btn_schedule.setClickable(true);
    	} 
    	
    	else{      	 		  
    			switch(str){    			
    			case 'a': //과외
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder(); //오더를 o으로 만듬
    					startActivity(new Intent(Main.this, Teaching.class));
    					break;}
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++; 
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, Teaching.class));     			
    					break;} 
    			case 'b': //패티
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder();
    					startActivity(new Intent(Main.this, BurgerShow.class));
    					break;}
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++;
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, BurgerShow.class));     			
    					break;} 
    			case 'c': //고기
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder();
    					startActivity(new Intent(Main.this, MeatMain.class));
    					break;}
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++;
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, MeatMain.class));     			
    					break;} 
    			case 'd': //팝콘
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder();
    					startActivity(new Intent(Main.this, PopShow.class));
    					break;}  
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++;
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, PopShow.class));     			
    					break;} 
    			case 'e': //공부
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder();
    					startActivity(new Intent(Main.this, Study.class));
    					break;}
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++;
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, Study.class));     			
    					break;}  				
    			case 'f': //데이트(빼뺴로)
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder();
    					startActivity(new Intent(Main.this, Bbebbero.class));
    					break;}
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++;
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, Bbebbero.class));     			
    					break;} 
    			case 'g': //여가(영화&쇼핑)
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder();
    					startActivity(new Intent(Main.this, Movie.class));
    					break;}
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++;
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, Movie.class));     			
    					break;} 
    			case 'h': //여가(집에서휴식)
    				if(d_cnt == order.length()-1){
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					reSetOrder();
    					startActivity(new Intent(Main.this, Rest.class));
    					break;}
    				else{
    					//Toast.makeText(Main.this, order.charAt(d_cnt) + ", " + d_cnt, Toast.LENGTH_SHORT).show();
    					d_cnt++;
    					setOrdercnt(d_cnt);
    					startActivity(new Intent(Main.this, Rest.class));     			
    					break;} 
    		}
    	}    	
    }
    
    public void onSchedule(View v){    //달력	
    	startActivity(new Intent(Main.this, Calendar.class));  	
    }   
    
    public void onAccount(View v){ //통장
    	startActivity(new Intent(Main.this, Account.class));    	 	
    }

    public void onInfo(View v){ //정보
    	startActivity(new Intent(Main.this, Information.class));     	
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) { //백버튼
        switch(keyCode){
        case KeyEvent.KEYCODE_BACK:
    
        		AlertDialog.Builder alertDlg =  new AlertDialog.Builder(Main.this); 
 				alertDlg.setMessage("\n" + "프로그램이 종료됩니다." + "\n" + "종료하시겠습니까?" + "\n");
 				alertDlg.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
 		
 					public void onClick(DialogInterface dialog, int which) {
 						startActivity(new Intent(Main.this, Main.class));
					}
				})
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						Intent intent = new Intent(Main.this, Menu.class); //HOME화면 이동
						intent.putExtra("end", true);
				        intent.setAction(Intent.ACTION_MAIN);
				        intent.addCategory(Intent.CATEGORY_HOME);
				        startActivity(intent);
				        android.os.Process.killProcess(android.os.Process.myPid()); 
				        //메모리 상태에서 완전한 종료를 취한다.
					}
				});
 				alertDlg.show() ;
    		break;	    	
	}
        return true;
       }
    

    
}
