# BlockWorld
Minecraft-style Terrain Generator/Renderer in Java

Clone the repository
```bash
cd blockworld
./gradlew run
```

- Uses Perlin noise function to generate landscape
- Pages in new chunks as the player navigates around the world

TODO:
- more variation on landscape via higher order functions (biomes)
- different block types and textures (stone, sand, dirt, etc)
- collision detection
- dynamic skybox/lighting
- 'next-gen' graphics, pbr textures, bloom, shadows
- Fix that concurrency bug where more than 2 threads causes incoherent noise generation
