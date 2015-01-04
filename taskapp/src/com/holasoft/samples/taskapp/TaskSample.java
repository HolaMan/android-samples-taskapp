package com.holasoft.samples.taskapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;

public class TaskSample {
	
	public static void TestThread() {
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				// do task!!
			}});
		thread.start();
	}
	
	
	public static class MyThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) { 
			return new Thread(r);
		}
		
	}
	
	public static void TestThreadPool() throws MalformedURLException {
		
		MyThreadFactory threadFactory = new MyThreadFactory();
		
		// newCachedThreadPool
		ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
														60L, TimeUnit.SECONDS,
														new SynchronousQueue<Runnable>(),
														threadFactory);
		// newFixedThreadPool
				int nThreads = 5;
				ThreadPoolExecutor executor2 = new ThreadPoolExecutor(nThreads, nThreads,
																0L, TimeUnit.MILLISECONDS,
																new LinkedBlockingQueue<Runnable>(),
																threadFactory);
								
		DownloadAsyncTask asyncTask = new DownloadAsyncTask();
		URL url = new URL("http://www.google.com");
		asyncTask.execute(url); // execute asyncTask
		asyncTask.executeOnExecutor(executor, url); // execute asyncTask on Executor		
	}
	
	
	
	/* AsyncTask<Params, Progress, Result> */
	public static class DownloadAsyncTask extends AsyncTask<URL, Integer, Long> {

		
		@Override
		protected void onPreExecute () {};
				
		@Override
		protected Long doInBackground(URL... params) {
			// do task & report progress by calling publishProgress(values);
			// check isCancelled() if the task is cancelled or not
			return null;
		}
		
		@Override 
		protected void onProgressUpdate (Integer... values) {}
		
		@Override
		protected void onPostExecute (Long result) {}
		
	}
}

