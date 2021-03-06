package com.android.progBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Exam extends Activity {
    /** Called when the activity is first created. */      
 
 DBManager dManager;
 DBManager2 dManager2;
 SQLiteDatabase db, db2;
 private static final String DATABASE_NAME = "character.db";
 private static final String DATABASE_NAME2 = "calendar.db";
 int d_day = 1;
 String d_knowledge, d_exam;
 int k_v, pres, prev;
 
 SQLiteDatabase mDatabase, mDatabase2, mDatabase3; 
 String voca[] = new String[5];
 char test[] = new char[20];
 String show = "", str4 = "";
 static String score = "";
 String str, str2, str3;
 TextView eng, kor, ans, ans2;
 EditText eb;
 Button btn;
 int rand = 50;
 int num, tem, len, testR;
 int correct = 0, wrong = 0, count = 0;
 static int ti3, know;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        setContentView(R.layout.exam);        
        
        if (db == null) { 
         db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        }
        if (db2 == null) { 
         db2 = openOrCreateDatabase(DATABASE_NAME2, Context.MODE_PRIVATE, null);
        }
        
        dManager = new DBManager(this, "character.db", null, 1);
        db = dManager.getWritableDatabase();
        db.close();
        
        dManager2 = new DBManager2(this, "calendar.db", null, 1);
        db2 = dManager2.getWritableDatabase();
        db2.close(); 
        
        getDay();
        getValue();
        
       // Toast.makeText(Exam.this, "" + d_exam, Toast.LENGTH_SHORT).show();
        
        eng = (TextView) findViewById(R.id.text);
        kor = (TextView) findViewById(R.id.text2);
        ans = (TextView) findViewById(R.id.text3);  //
        ans2 = (TextView) findViewById(R.id.text4); //
        eb = (EditText) findViewById(R.id.editText1);
        btn = (Button) findViewById(R.id.button1);
  
        takeInfo(); 
        initialize(this);
        setAdaptor();
    }

    public void takeInfo() {
    	initialize2(this);

  if (mDatabase2 == null) {
	  mDatabase2 = openOrCreateDatabase(DATABASE_TOTAL, Context.MODE_PRIVATE, null);
	  // 데이터베이스열기
  }

  Cursor cursor = null;
  //final CursorAdapter adaptor;
  String[] columns = new String[] { "prev", "pres", "know" };
  cursor = mDatabase2.query(TABLE_NAME2, columns, null, null, null, null, null);

  while (cursor.moveToNext()) {
 
   int prev = cursor.getInt(cursor.getColumnIndex("prev"));
   int pres = cursor.getInt(cursor.getColumnIndex("pres"));
   int kno = cursor.getInt(cursor.getColumnIndex("know"));
   prev = prev;
   pres = pres;
   ti3 = kno;
  }
 }

 public void test() { //선택된 단어중에 한 알파벳을 랜덤으로 빼기위해 만든 함수
  double rand = Math.random();
  int ir = (int) (rand * 10);
  len = str.length();
  tem = ir % len;

  for (int i = 0; i < len; i++) {
   if (i == tem) {  //선택된 랜덤 값을 _로 표시
    show += "_";  
    str4 += test[i];  // 빠진글자 한개만
   } else
    show += test[i];
  }
   eng.setText(show);
   kor.setText(str2);  
 }
 
 public static final String ROOT_DIR = "/data/data/com.android.progBar/";
 private static final String DATABASE_NAME3 = "daneo.sqlite";
 private static final String DATABASE_TOTAL = "saved1.sqlite";
 public static final String TABLE_NAME = "voc";
 public static final String TABLE_NAME2 = "saved1";
 private static final String COLUMN_NUMBER = "num";
 private static final String COLUMN_ENGL = "eng";
 private static final String COLUMN_KORE = "kor";
 
 public static void initialize(Context ctx) {  //DB를 파일로만듬
  // check
  File folder = new File(ROOT_DIR + "databases");
  folder.mkdirs();
  File outfile = new File(ROOT_DIR + "databases/" + DATABASE_NAME3);
  if (outfile.length() <= 0) {
   AssetManager assetManager = ctx.getResources().getAssets(); // 파일읽어오기
   try {
    InputStream is = assetManager.open(DATABASE_NAME3,
      AssetManager.ACCESS_BUFFER);
    // 파일열어서 is에 저장하기
    long filesize = is.available();
    byte[] tempdata = new byte[(int) filesize];
    is.read(tempdata);
    is.close();

    outfile.createNewFile(); // 새파일생성
    FileOutputStream fo = new FileOutputStream(outfile); // 생성한파일
                  // 아웃풋스트림
    fo.write(tempdata); // 파일에다가 디비파일넣기
    fo.close();
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }

 public static void initialize2(Context ctx) { // DB를 파일로 만드는 함수 
  // check
  File folder = new File(ROOT_DIR + "databases");
  folder.mkdirs();
  File outfile = new File(ROOT_DIR + "databases/" + DATABASE_TOTAL);
  if (outfile.length() <= 0) {
   AssetManager assetManager = ctx.getResources().getAssets(); // 파일읽어오기
   try {
    InputStream is = assetManager.open(DATABASE_TOTAL,
      AssetManager.ACCESS_BUFFER);
    // 파일열어서 is에 저장하기
    long filesize = is.available();
    byte[] tempdata = new byte[(int) filesize];
    is.read(tempdata);
    is.close();

    outfile.createNewFile(); // 새파일생성
    FileOutputStream fo = new FileOutputStream(outfile); // 생성한파일
                  // 아웃풋스트림
    fo.write(tempdata); // 파일에다가 디비파일넣기
    fo.close();
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }

 private void setAdaptor() { //DB파일 읽어오기

  if (mDatabase == null) {
   mDatabase = openOrCreateDatabase(DATABASE_NAME3,
     Context.MODE_PRIVATE, null);
   // 데이터베이스열기
  }

  double te = Math.random(); 
  int ran = (int) (te * 1000);
  tem = ran % (pres - prev) + prev + 1; //현재까지 공부한 단어를 랜덤으로 뽑아오기 위해 TEM이라는 변수생성 
  String temp = String.valueOf(tem); 
  ans2.setText(temp);//

  Cursor cursor = null;  // DB커서
  //final CursorAdapter adaptor;
  String[] columns = new String[] { COLUMN_NUMBER, COLUMN_ENGL,
    COLUMN_KORE };
  // COLUMN_NUMBER +"=" + rand

  cursor = mDatabase.query(TABLE_NAME, columns, COLUMN_NUMBER + "=" + tem, null, null, null, null);  //TEM값에 해당하는 DB컬럼을 선택한다

  while (cursor.moveToNext()) { 
  
   int number = cursor.getInt(cursor.getColumnIndex("num")); 
   String eng = cursor.getString(cursor.getColumnIndex("eng"));
   String kor = cursor.getString(cursor.getColumnIndex("kor"));
   str = eng;
   str2 = kor;
   num = number;
  }
  for (int i = 0; i < str.length(); i++)
   test[i] = str.charAt(i);  
  test(); //빈칸넣어주는 함수

 }

 public void check(View v) {  //버튼함수
 
if (count < 9) {  //9번째문제까지는 버튼누르면 다음으로 넘어감 
   str3 = eb.getText().toString(); //입력값

   if (str3.equals(str4)) { //입력값과 정답값 비교
    Toast.makeText(Exam.this, "정답입니다!", Toast.LENGTH_SHORT).show();
    correct++;
   } else {
    Toast.makeText(Exam.this, "정답이 아닙니다!", Toast.LENGTH_SHORT).show();
    wrong++;
   }
   count++; //문제수
   rand++; 
   str4 = ""; //초기화
   show = "";
   eb.setText("");
   setAdaptor(); // DB에서 다음문제부르는 함수
  } else { //마지막문제 
   str3 = eb.getText().toString();

   if (str3.equals(str4)) {
    Toast.makeText(Exam.this, "정답입니다!", Toast.LENGTH_SHORT).show();
    correct++;
   } else {
    Toast.makeText(Exam.this, "정답이 아닙니다!", Toast.LENGTH_SHORT).show();
    wrong++;
   }
   
   AlertDialog.Builder alertDlg = new AlertDialog.Builder(Exam.this);   
   alertDlg.setTitle("결과");
   
   if (correct > 8) {
	    score = "A";
	    if(d_exam.equalsIgnoreCase("0")) d_exam = score;
	    else d_exam += score;
	    alertDlg.setMessage("\n" + "맞은 갯수 : " + correct + "개 \n" + "당신의 성적은 " + score + "입니다." + "\n");
	    }
	   else if (5 < correct && correct < 9) {
	    score = "B";
	    if(d_exam.equalsIgnoreCase("0")) d_exam = score;
	    else d_exam += score;
	    alertDlg.setMessage("\n" + "맞은 갯수 : " + correct + "개 \n" + "당신의 성적은 " + score + "입니다." + "\n");
	   }
	   else {
	    score = "F";
	    if(d_exam.equalsIgnoreCase("0")) d_exam = score;
	    else d_exam += score;
	    alertDlg.setMessage("\n" + "맞은 갯수 : " + correct + "개 \n" + "당신의 성적은 " + score + "입니다." + "\n"
	      + "지력이 30 감소합니다." + "\n");
	    k_v -= 30;    
	    
	   }   
   
   alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int whichButton) {    	
    	setValue(k_v, d_exam);
        d_day = d_day + 6;
        setDay(d_day);
        startActivity(new Intent(Exam.this, Main.class));    	
    }    
   });
   alertDlg.show() ;
   
  }
 }

   public void getDay(){
    
    db2 = dManager2.getWritableDatabase();   
     String sql = "SELECT day FROM calendar;";

      try{   
       
       Cursor cur = db2.rawQuery(sql, null);
       
       while(cur.moveToNext()){
        
        d_day = cur.getInt(cur.getColumnIndex("day"));          
       
       }        
       
     }catch (SQLException se) {
       // TODO: handle exception       
     }   
         db2.close();          
      
   }  
   
   public void setDay(int d){
     
     String sql = "";     
     db2 = dManager2.getWritableDatabase(); 
     
     if(d > 30){
      d = 1;
      sql = "update calendar set day = '" + d + "';"; 
     }   
     else sql = "update calendar set day = '" + d + "';";  
    
  db2.execSQL(sql);
  db2.close();
    
  }
   
   public void getValue(){
     
     db = dManager.getWritableDatabase();   
     String sql = "SELECT distinct knowledge, exam, examcnt, studycnt FROM character;";
    
       try{   
        
        Cursor cur = db.rawQuery(sql, null);
        
        while(cur.moveToNext()){
        
         d_knowledge = cur.getString(cur.getColumnIndex("knowledge"));
         d_exam = cur.getString(cur.getColumnIndex("exam"));
         prev = cur.getInt(cur.getColumnIndex("examcnt"));
         pres = cur.getInt(cur.getColumnIndex("studycnt"));         
              
        }       
        
      }catch (SQLException se) {
        // TODO: handle exception      
      }   
         db.close();          
      
       k_v = Integer.parseInt(d_knowledge);
   }
   
   public void setValue(int k, String s){
    
    if(k < 0) { k = 0; }
    String str_k = String.valueOf(k);
    
    String sql = "", sql2 = "", sql3 = "";
    db = dManager.getWritableDatabase();
    
    sql = "update character set knowledge = '" + str_k + "';";
    sql2 = "update character set exam = '" + s + "';";
    sql3 = "update character set examcnt = '" + pres + "' ;";
    
    db.execSQL(sql);
    db.execSQL(sql2);
    db.execSQL(sql3);
    
    db.close();
    }
   
   public boolean onKeyDown(int keyCode, KeyEvent event) {
	      
       return true;
      
   }
}

