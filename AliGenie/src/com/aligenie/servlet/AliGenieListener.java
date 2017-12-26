package com.aligenie.servlet;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.aligenie.action.AliGenieAction;


public class AliGenieListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {


		Timer timer = new Timer(true);
		timer.schedule(timeTick,0, 1000);
		
	}
	TimerTask timeTick = new TimerTask(){  
        public void run() {  
        	if(AliGenieAction.action!=null) AliGenieAction.action.timeTickSecond();
        }  
    };

}
