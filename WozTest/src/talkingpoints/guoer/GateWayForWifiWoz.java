package talkingpoints.guoer;

 

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector.OnDoubleTapListener;
import android.widget.Toast;

public class GateWayForWifiWoz extends GestureUI {

	private static String GET_POI_INFO = "http://app.talking-points.org/locations/";
	private String ID;
	private String MAC;
	private String content;
	private MsgParser p;
	private Intent intent;
	AngleCalculator oc;
	
//	private static final int SWIPE_MIN_DISTANCE = 120;
//	private static final int SWIPE_MAX_OFF_PATH = 250;
//	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
//	
	private static final int SWIPE_MIN_DISTANCE = 10; //120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private static final int CHECK_DISTANCE =100; 
	private static final int CHECK_DISTANCE_2=10; 
	private static final int SWIPE_MIN_DISTANCE_RIGHT_LEFT=100; //more distance require for left and right gesture 

    private static boolean flag = false;  //scroll with fling 
    private static boolean flag2 = false;  //keydown and keyup 
    private static boolean flag3 = false;  //keydown and keyup 
    private static boolean flagForScrolling=false; 
    private static boolean flagTrackball = false; 
    
    private float FirstX;
    private float FirstY;
    private float LastX;
    private float LastY;
    
    private static int count1=0;
	private static int countGesture=0; 
	
	public void onCreate(Bundle savedInstanceState) {
	 	
//		oc = new AngleCalculator(byCoordinateParser.lat, byCoordinateParser.lng
//				,WozParser.getLatitude(),
//				WozParser.getLongitude());
//		oc.getAngle();
		
		pageName = new String();
		pageName = "Gateway for Wifi and Woz";
		super.onCreate(savedInstanceState);
		this.options.add("WozTest");
		this.options.add("Wifi");
//		this.options.add("What's around" + pageName);
 

 
	
		
		// TODO: get poi menu from xml parser

		super.gestureScanner.setOnDoubleTapListener(new OnDoubleTapListener() {
			public boolean onDoubleTap(MotionEvent e) {
//				if(flag)
//				{
					switch (GestureUI.selected) {
					case 0:
						releaseSoundEffect();
						playSound(NEXT_PAGE);
						
 						Intent intent0 = new Intent(GateWayForWifiWoz.this, BTlist.class);
 						startActivity(intent0);
						break;
					case 1:	
						releaseSoundEffect();
						playSound(NEXT_PAGE);
						
 						Intent intent1 = new Intent(GateWayForWifiWoz.this, WifiList.class);
 						startActivity(intent1);
 
						}
 
//				}
			 
 				return true;
			}

			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}

			public boolean onSingleTapConfirmed(MotionEvent e) {
				countGesture++;
				
				if(countGesture==2)
				{	countGesture=0;
 
				switch (GestureUI.selected) {
			
				case 0:
					releaseSoundEffect();
					playSound(NEXT_PAGE);
						
					Intent intent0 = new Intent(GateWayForWifiWoz.this, BTlist.class);
					startActivity(intent0);
					
					break;
				case 1:	
					releaseSoundEffect();
					playSound(NEXT_PAGE);
					
					Intent intent1 = new Intent(GateWayForWifiWoz.this, WifiList.class);
					startActivity(intent1);

					break; 
					}
				}
				return false;
			}

		});
	 
		
		
//		if (ID == null)
//		{
//		 this.mTts
//						.speak(
//								"Nothing interesting detected yet",
//								TextToSpeech.QUEUE_FLUSH, null); 
// 		this.sayPageName("Nothing interesting detected yet");
//		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {

		int action = e.getAction();
    	//down 
		if(action == MotionEvent.ACTION_DOWN||action==MotionEvent.ACTION_MOVE)
			flagForScrolling=true;
		
        if(action == MotionEvent.ACTION_DOWN)
        {
         	FirstX=e.getX();
        	FirstY=e.getY();
        }
    	else if(action == MotionEvent.ACTION_UP)
    	{
    		LastX=e.getX();
    		LastY=e.getY();
    		
     		
    		if(FirstX>0||FirstY>0)
    		{
    			final float xD=Math.abs(FirstX-LastX);
    			final float yD=Math.abs(FirstY-LastY);
    			
    			try{
    				if(FirstX-LastX>SWIPE_MIN_DISTANCE_RIGHT_LEFT&&yD< CHECK_DISTANCE)
    				{    // this.mTts.speak("LEFT MOTION", TextToSpeech.QUEUE_FLUSH,null);
		    				releaseSoundEffect();
							playSound(NEXT_PAGE);
								finish();

    				}
    				else if(LastX - FirstX >SWIPE_MIN_DISTANCE_RIGHT_LEFT&& yD< CHECK_DISTANCE) 
    				{
    					this.sayPageName();
    					vibrate();
    				}

     				   //   this.mTts.speak("Right motion", TextToSpeech.QUEUE_FLUSH,null);
     				else if(FirstY - LastY > SWIPE_MIN_DISTANCE&& xD< CHECK_DISTANCE)  
     				{
     					 // this.mTts.speak("UP Motion", TextToSpeech.QUEUE_FLUSH,null);
     					 if(flag||flagForScrolling)
     					 {
     						 vibrate();
     						 upMotion();
     						flagForScrolling=false;
     					 }
     					
     				}
     				else if(LastY - FirstY > SWIPE_MIN_DISTANCE && xD< CHECK_DISTANCE)  
     				{	
     					//this.mTts.speak("down motion", TextToSpeech.QUEUE_FLUSH,null);
     					
     					 if(flag||flagForScrolling)
     					 {
     						 
     						vibrate();
     						downMotion();
     						flagForScrolling = false; 
     					 }
     				}//missed
     				else if(xD>CHECK_DISTANCE_2&&yD>CHECK_DISTANCE_2)
     				{
     					releaseSoundEffect();
						playSound(MISSED_IT);
      				}

    		 

    			}
    			catch (Exception e0) {
    				// nothing
    			}
    			
    		}
    	}
		
		gestureScanner.onTouchEvent(e);
		return true;

	}
	 @Override
    public boolean onTrackballEvent(MotionEvent _event)
		{
			float vertical = _event.getY();
			float horizontal = _event.getX();
 			   //viewA.setText("x"+horizontal+"Y"+vertical);
				
			   if(vertical!=0.0||horizontal!=0.0)
			   {
				  flagTrackball=true; 
			   }
			
			
			   if(horizontal==-0.16666667 &&vertical==0.0 ) //left
			   {
				  // finish();
			   }
			   else if(horizontal==0.16666667 &&vertical==0.0 ) //Right
			   {
					this.sayPageName();

			   }
			   else if(horizontal==0.0 &&vertical==-0.16666667 ) //up
			   {
				   if(flagTrackball)
					 {	
 						upMotion();
						flagTrackball=false;
					 }
			   }
			   else if(horizontal==0.0 &&vertical==0.16666667  ) //down
			   { 
				   if(flagTrackball)
					 {	
 						downMotion();
						flagTrackball=false;
					 }

			   }
			   
			return false; 
		}
	 @Override
		public void vibrate()
		{
			String vibratorService = Context.VIBRATOR_SERVICE;
			Vibrator vibrator = (Vibrator)getSystemService(vibratorService);


			vibrator.vibrate(100);
		}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
//flag = true;
		final float xDistance = Math.abs(e1.getX() - e2.getX());
		final float yDistance = Math.abs(e1.getY() - e2.getY());
		
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE_RIGHT_LEFT
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY &&yDistance< CHECK_DISTANCE) {
			
			releaseSoundEffect();
			playSound(NEXT_PAGE);
			finish();
		}else if(e2.getX() - e1.getX() >SWIPE_MIN_DISTANCE_RIGHT_LEFT
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY &&yDistance< CHECK_DISTANCE) {
			this.sayPageName();
			
		}else if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY &&xDistance< CHECK_DISTANCE) {
			//	this.sayPageName("up");
				 if(flag)
				 {
					 upMotion();
				 }
				
		    //  viewA.setText("-" + "Fling up?" + "-");

			}else if(e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY &&xDistance< CHECK_DISTANCE) {
				 if(flag) 
				 {
					 downMotion();
				 }
				 
	 
	 
				
				}
 
	 
		
		return false;
	}
	@Override
	// disable scroll gesture
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		flag = true; 
		
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode_1, KeyEvent event) {
		// if we get any key, clear the Splash Screen
		if (keyCode_1 == KeyEvent.KEYCODE_DPAD_CENTER) {
	 
		}else if(keyCode_1 == KeyEvent.KEYCODE_VOLUME_DOWN){
 
			flag3=true; 
			
			if(options.size()!=0){
				if(flag2)
				{
					if(count1==options.size()-1)
						count1=1;
					else 
						count1+=2;
					
					flag2=false; 
				}
				
				if(count1==options.size()){
					
					count1=0;
					
				}
				
				if(count1<options.size()) //count<.size() 
				{
					
					// viewA.setText("Down"+count1);
	     			message = options.get(count1);
	 			
	 
	 				selected = count1;
	  				text.setText(message);
	 
	 				this.mTts.speak(message, TextToSpeech.QUEUE_FLUSH,
	 					null);
	 			
	 				releaseSoundEffect();
	 				playSound(ITEM_BY_ITEM);
	 			
	  
//	 				if(count1==(options.size()-1)) 
//					{
//						
//						if((options.get(count1).length()>8)&&(options.get(count1).length()<16))
//						{
//							try {
//								
//								Thread.sleep(1400);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e41){
//								e41.printStackTrace();
//							}
//				 
//						}
//						else if(options.get(count1).length()>16)
//						{
//							try {
//								
//								Thread.sleep(2100);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e42){
//								e42.printStackTrace();
//							}
//				 
//						}else 
//						{
//							try {
//								
//								Thread.sleep(700);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e43){
//								e43.printStackTrace();
//							}
//						}
//
//					} 
			    
					count1++;
	
				}
			} 
 
		}else if(keyCode_1 == KeyEvent.KEYCODE_VOLUME_UP){
		
			flag2=true;
			
			if(options.size()!=0){
				if(flag3)
				{
					if(count1==1)
						count1=options.size()-1;
	//				else if(count==5)
	//					count=3;
					else 
					{
						if(count1!=0)
						count1-=2;
					}
					
					flag3=false;
				}
				
				if(count1!=0)
				{
					if(count1==options.size()){
				
					
						count1=options.size()-2;
					}	
				}
	
				
				if(count1==0){
				//	this.sayPageName("0");
					
					message = options.get(count1);
					
	
					selected = count1;
					text.setText(message);
	
					this.mTts.speak(message, TextToSpeech.QUEUE_FLUSH,
						null);
				    
					
					releaseSoundEffect();
					playSound(ITEM_BY_ITEM);
				
			    //	 viewA.setText("UP"+count1);
					count1=options.size()-1;
					
				}
				else if(count1<options.size())
					{
					
					
					message = options.get(count1);
				
	
					selected = count1;
					text.setText(message);
	
					this.mTts.speak(message, TextToSpeech.QUEUE_FLUSH,
						null);
				
					releaseSoundEffect();
					playSound(ITEM_BY_ITEM);
				
	
//					if(count1==(options.size()-1)) 
//					{
//						
//						if((options.get(count1).length()>8)&&(options.get(count1).length()<16))
//						{
//							try {
//								
//								Thread.sleep(1400);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e51){
//								e51.printStackTrace();
//							}
//				 
//						}
//						else if(options.get(count1).length()>16)
//						{
//							try {
//								
//								Thread.sleep(2100);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e52){
//								e52.printStackTrace();
//							}
//				 
//						}else 
//						{
//							try {
//								
//								Thread.sleep(700);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e53){
//								e53.printStackTrace();
//							}
//						}
//
//					} 
				//	 viewA.setText("Up"+count1);
			   
					count1--;
	
				}
			} 
		}

		return true;// return super.onKeyDown(keyCode, event);
	}
	
	private void upMotion()
	{
		flag2=true;
		
		 if(options.size()!=0){
				if(flag3)
				{
					if(count1==1)
						count1=options.size()-1;
	//				else if(count==5)
	//					count=3;
					else 
					{
						if(count1!=0)
						  count1-=2;
					}
					flag3=false;
				}
				
				if(count1!=0)
				{
					if(count1==options.size()){
				
					
						count1=options.size()-2;
					}	
				}
	
				
				if(count1==0){
				//	this.sayPageName("0");
					
					message = options.get(count1);
					
	
					selected = count1;
					text.setText(message);
	
					this.mTts.speak(message, TextToSpeech.QUEUE_FLUSH,
						null);
				    
					
					releaseSoundEffect();
					playSound(ITEM_BY_ITEM);
				
			    //	 viewA.setText("UP"+count1);
					count1=options.size()-1;
					
				}
				else if(count1<options.size())
					{
					
					
					message = options.get(count1);
				
	
					selected = count1;
					text.setText(message);
	
					this.mTts.speak(message, TextToSpeech.QUEUE_FLUSH,
						null);
				
					releaseSoundEffect();
					playSound(ITEM_BY_ITEM);
				
	
//					if(count1==(options.size()-1)) 
//					{
//						
//						if((options.get(count1).length()>8)&&(options.get(count1).length()<16))
//						{
//							try {
//								
//								Thread.sleep(1400);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e11){
//								e11.printStackTrace();
//							}
//				 
//						}
//						else if(options.get(count1).length()>16)
//						{
//							try {
//								
//								Thread.sleep(2100);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e12){
//								e12.printStackTrace();
//							}
//				 
//						}else 
//						{
//							try {
//								
//								Thread.sleep(700);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e13){
//								e13.printStackTrace();
//							}
//						}
//
//					} 
				//	 viewA.setText("Up"+count1);
			   
					count1--;
	
				}
			} 
			
		 

		 flag = false;
	}
	private void downMotion()
	{
		flag3=true; 
		 if(options.size()!=0){
			 
				if(flag2)
				{
					if(count1==options.size()-1)
						count1=1;
					else 
						count1+=2;
					
					flag2=false; 
				}
				
				if(count1==options.size()){
					
					count1=0;
					
				}
				
				if(count1<options.size()) //count<.size() 
				{
					
					// viewA.setText("Down"+count1);
	     			message = options.get(count1);
	 			
	 
	 				selected = count1;
	  				text.setText(message);
	 
	 				this.mTts.speak(message, TextToSpeech.QUEUE_FLUSH,
	 					null);
	 			
	 				releaseSoundEffect();
	 				playSound(ITEM_BY_ITEM);
	 			
	 
//					if(count1==(options.size()-1)) 
//					{
//						
//						if((options.get(count1).length()>8)&&(options.get(count1).length()<16))
//						{
//							try {
//								
//								Thread.sleep(1400);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e21){
//								e21.printStackTrace();
//							}
//				 
//						}
//						else if(options.get(count1).length()>16)
//						{
//							try {
//								
//								Thread.sleep(2100);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e22){
//								e22.printStackTrace();
//							}
//				 
//						}else 
//						{
//							try {
//								
//								Thread.sleep(700);
//								releaseSoundEffect();
//								playSound(EDGE);
//								
//							}catch(InterruptedException e23){
//								e23.printStackTrace();
//							}
//						}
//
//					} 
			    
					count1++;

				}
		 }  

		 flag = false;
	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent msg) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//		
//		//	Intent intent = new Intent(POImenu.this, POIsAhead.class);
//		//	startActivity(intent); 
//			Toast.makeText(this,"FlashLight!", Toast.LENGTH_SHORT).show();
//
//		}
//		return true;
//
//	}
//	
//	@Override
//	public void onAccuracyChanged(Sensor arg0, int arg1) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onSensorChanged(SensorEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
}
