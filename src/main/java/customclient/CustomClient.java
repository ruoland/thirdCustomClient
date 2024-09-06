package customclient;

import com.example.ScreenCommand;
import com.example.event.KeyEvent;
import com.example.event.ScreenMouseEvent;
import com.example.event.ScreenRenderEvent;
import com.example.event.TitleInitEvent;
import com.example.packet.*;
import com.mojang.blaze3d.platform.Window;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@Mod(CustomClient.MODID)
public class CustomClient {
    public static final String MODID = "customclient";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CustomClient(IEventBus modEventBus) {
        // 모드 이벤트 버스에 리스너 등록
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloadHandlers);

        if (FMLEnvironment.dist == Dist.CLIENT) {

        }
        // NeoForge 이벤트 버스에 리스너 등록
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new KeyEvent());
        NeoForge.EVENT_BUS.register(new ScreenRenderEvent());
        NeoForge.EVENT_BUS.register(new ScreenMouseEvent());
        NeoForge.EVENT_BUS.register(new TitleInitEvent());

        // DeferredRegister 등록

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        if (ConfigCustomClient.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        LOGGER.info(ConfigCustomClient.magicNumberIntroduction + ConfigCustomClient.magicNumber);
    }

    private void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playBidirectional(
                ScreenData.TYPE,
                ScreenData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ScreenClientPayloadHandler::handleData,
                        ScreenServerPayloadHandler::handleData
                )
        );
        registrar.playBidirectional(
                ImageData.TYPE,
                ImageData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ImageClientPayloadHandler::handleData,
                        ImageServerPayloadHandler::handleData
                )
        );
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ScreenCommand.register(event.getDispatcher());
    }
}