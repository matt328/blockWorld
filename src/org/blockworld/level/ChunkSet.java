package org.blockworld.level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class ChunkSet extends Node {
	private ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
		public Thread newThread(Runnable r) {
			Thread th = new Thread(r);
			th.setDaemon(true);
			return th;
		}
	});
	private Lock chunkLock;
	private int numChunks;
	private LevelChunk[][] levelChunks;
	
	public ChunkSet(int newNumChunks) {
		numChunks = newNumChunks;
		levelChunks = new LevelChunk[numChunks][numChunks];
	}

	public void updateLoadedChunks(Vector3f location) {
		//Vector2f localCamPosition = getLocalCamPosition(location);
		// if(!localCamPosition.equals(new Vector2f(1.0f, 1.0f))) {
		// LoadedChunkUpdater lcu = new LoadedChunkUpdater(location);
		// executor.submit(lcu);
		// }
	}
	
	private class LoadedChunkUpdater implements Runnable {
		private Vector3f cameraLocation;
		public LoadedChunkUpdater(Vector3f location) {
			cameraLocation = location;
		}
		@Override
		public void run() {
			
		}
	}
}
