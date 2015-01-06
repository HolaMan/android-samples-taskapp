package com.holasoft.samples.taskapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class TaskSample {
	
	public static void TestThread() {
		
		final Handler mHandler = new Handler();
		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				// do task!!
				Message m = new Message();
				m.what = 123;
				mHandler.sendMessage(m); // send progress or message
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// update UI view, ...etc
					}});
			}});
		thread.start();
		
		
	}
	
	public static void TestHandlerThread() {
		
		HandlerThread mHandlerThread = new HandlerThread("Worker");
		mHandlerThread.start();
				
		Handler mHandler = new Handler(mHandlerThread.getLooper());
		Message m = new Message();
		m.what = 123;
		mHandler.sendMessage(m);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// do something!!!
		}});
		
	}
	
	
	public static class MyThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) { 
			return new Thread(r);
		}
		
	}
	
	public static class MyRunnableTask implements Runnable {
		@Override
		public void run() {
			// do task!!
		}
	}
	public static class MyCallableTask implements Callable<String> {
		@Override
		public String call() throws Exception {
			// do task!!
			return null;
		}		
	}
	public static class MyFutureTask extends FutureTask<String> {

		public MyFutureTask(Callable<String> callable) {
			super(callable); 
		}
		
		@Override
		protected void done() { 
			// get result by calling get()
			// do some post execution after task completed, such as change state or fork another task
		}
	}
	
	public static void TestFuture() {
		
		MyCallableTask task = new MyCallableTask();
		FutureTask<String> futureTask = new FutureTask<String>(task);
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.execute(futureTask);
		
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
		// Executors
		ExecutorService executor3 = Executors.newFixedThreadPool(5);
		
		
		// Runnable Execution
		executor.execute(new MyRunnableTask());
		Future<?> f1 = executor.submit(new MyRunnableTask());
		// Callable Execution
		Future<String> f2 = executor.submit(new MyCallableTask());
		// Future Task Execution
		executor.execute(new MyFutureTask(new MyCallableTask()));
		
		// AsyncTask Execution
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

