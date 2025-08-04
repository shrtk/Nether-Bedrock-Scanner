package com.github.shrtk;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BedrockScanner implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("bedrockscan")
					.executes(context -> executeScan(context.getSource())));
		});
	}

	private int executeScan(ServerCommandSource source) {
		ServerPlayerEntity player = source.getPlayer();
		MinecraftServer server = source.getServer();
		World world = player.getWorld();
		BlockPos playerPos = player.getBlockPos();
		ChunkPos centerChunk = new ChunkPos(playerPos);

		List<String> resultLines = new ArrayList<>();
		int[] ys = {4, 123};

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				ChunkPos chunkPos = new ChunkPos(centerChunk.x + dx, centerChunk.z + dz);
				for (int y : ys) {
					for (int lx = 0; lx < 16; lx++) {
						for (int lz = 0; lz < 16; lz++) {
							int wx = chunkPos.getStartX() + lx;
							int wz = chunkPos.getStartZ() + lz;
							BlockPos pos = new BlockPos(wx, y, wz);
							BlockState state = world.getBlockState(pos);
							if (state.isOf(Blocks.BEDROCK)) {
								resultLines.add(wx + " " + y + " " + wz + " Bedrock");
							}
						}
					}
				}
			}
		}

		saveToDownloads(resultLines, server, player);
		return Command.SINGLE_SUCCESS;
	}

	private void saveToDownloads(List<String> lines, MinecraftServer server, ServerPlayerEntity player) {
		try {
			File downloadsFolder = getDownloadsFolder();
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			File outFile = new File(downloadsFolder, "bedrockscan_" + timestamp);

			Files.write(outFile.toPath(), lines, StandardCharsets.UTF_8);

			player.sendMessage(Text.literal("[BedrockScan] Results saved: " + outFile.getAbsolutePath()), false);
		} catch (IOException e) {
			e.printStackTrace();
			player.sendMessage(Text.literal("[BedrockScan] Error occurred: " + e.getMessage()), false);
		}
	}

	private File getDownloadsFolder() {
		String userHome = System.getProperty("user.home");
		return new File(userHome, "Downloads");
	}
}
