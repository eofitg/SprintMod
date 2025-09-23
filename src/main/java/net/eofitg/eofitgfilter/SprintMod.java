package net.eofitg.eofitgfilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.eofitg.eofitgfilter.command.SprintModCommand;
import net.eofitg.eofitgfilter.config.SprintModConfig;
import net.eofitg.eofitgfilter.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.MOD_VERSION,
        acceptedMinecraftVersions = "[1.8.9]",
        clientSideOnly = true
)
public class SprintMod {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile = null;
    public static SprintModConfig config;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        configFile = new File(e.getModConfigurationDirectory(), "sprint-mod.json");
        loadConfig();
        Runtime.getRuntime().addShutdownHook(new Thread(SprintMod::saveConfig));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new SprintModCommand());
    }

    public static void loadConfig() {
        if (configFile == null) return;
        if (configFile.exists()) {
            try {
                String json = FileUtils.readFileToString(configFile);
                config = gson.fromJson(json, SprintModConfig.class);
            } catch (Exception ignored) {}
        } else {
            config = new SprintModConfig();
            saveConfig();
        }
    }

    public static void saveConfig() {
        if (configFile == null) return;
        try {
            String json = gson.toJson(config);
            FileUtils.write(configFile, json);
        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && mc.thePlayer != null && mc.theWorld != null && mc.inGameHasFocus && config.isEnabled()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }

}
