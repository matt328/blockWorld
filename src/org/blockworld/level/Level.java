package org.blockworld.level;

public class Level {
	private final LevelChunk[] chunks;

	public Level(final int numChunks) {
		chunks = new LevelChunk[numChunks];
		for (int i = 0; i < numChunks; i++) {
			chunks[i] = new LevelChunk(16, 16, 128);
		}
	}

	public int getSizeInBytes() {
		final int chunkSizeInbytes = chunks[0].getSizeInBytes();
		return chunkSizeInbytes * chunks.length;
	}
}
